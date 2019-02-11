package com.maqautocognita.africanstorybookslibrary.service;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.maqautocognita.listener.IMediaRecordListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * lessonList
 */
public class AudioService implements com.maqautocognita.adapter.IAudioService {

    private static final String AUDIO_FOLDER_NAME = "audio";

    private static final String AUDIO_RECORD_OUTPUT_FILE_PATH = File.separator + "/recording.mp4";
    /**
     * This is used to control the waiting time for next execution in {@link #musicPlayerRunnableCode}
     */
    private static final int MUSIC_PLAYING_IN_MILLISECOND = 1;

    private final Context context;

    private ISoundPlayListener musicPlayerListener;
    private MediaPlayer musicPlayer;
    private Handler musicPlayerHandler = new Handler();

    private Handler recorderHandler = new Handler();

    private MediaRecorder mediaRecorder;

    private boolean isStopRecord;

    private boolean isMusicStopped = true;

    private Visualizer mVisualizer;

    /**
     * This is mainly used to check  which alphabet is singing in the song, the runnable will be continue to execute after {@link #MUSIC_PLAYING_IN_MILLISECOND}.
     */
    private Runnable musicPlayerRunnableCode = new Runnable() {
        @Override
        public void run() {
            if (null != musicPlayer) {
                if (null != musicPlayerListener) {
                    try {
                        musicPlayerListener.onPlay(0, musicPlayer.getCurrentPosition());
                    } catch (Exception ex) {
                        Log.e(this.getClass().getName(), "on get player current position", ex);
                    }
                }
                musicPlayerHandler.postDelayed(this, MUSIC_PLAYING_IN_MILLISECOND);
            }
        }
    };

    private Runnable mediaRecorderRunnableCode;

    public AudioService(Context context, String audioPath) {
        this.context = context;
    }

    @Override
    public void playAudio(final String audioFileName, final ISoundPlayListener musicPlayerListener) {
        try {

            stopMusic();
            isMusicStopped = false;
            this.musicPlayerListener = musicPlayerListener;
            Log.i(AudioService.class.getName(), "going to play " + audioFileName + " in asset folder");
            AssetFileDescriptor descriptor = context.getAssets().openFd(audioFileName);

            if (null == musicPlayer) {
                musicPlayer = new MediaPlayer();
            }

            musicPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());

            musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer arg0) {
                    try {
                        musicPlayer.start();
                        // Kick off the first runnable task right away
                        musicPlayerHandler.post(musicPlayerRunnableCode);
                    } catch (Exception ex) {
                        Log.e(AudioService.class.getName(), "when start to play the audio " + audioFileName, ex);
                    }

                }
            });

            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i(AudioService.class.getName(), "after playing music " + audioFileName + " to call complete listener");
                    onMusicPlayComplete();
                }
            });

            if (null != musicPlayerListener && musicPlayerListener.isWaveChangeListenerRequired()) {
                if (isOSVersionAboveGingerBread()) {
                    mVisualizer = new Visualizer(musicPlayer.getAudioSessionId());
                    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
                    mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
                                                          int samplingRate) {
                            if (isOSVersionAboveGingerBread()) {
                                if (mVisualizer.getEnabled()) {
                                    int max = 0, min = 255;
                                    for (int i = 0; i < waveform.length; i++) {
                                        int w = (int) waveform[i] & 0xFF;
                                        max = Math.max(w, max);
                                        min = Math.min(w, min);
                                    }
                                    Log.i(this.getClass().getName(), "waveform " + max + " / " + min);
                                    musicPlayerListener.onWaveChanged(max - min);
                                }
                            }
                        }

                        public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                        }
                    }, Visualizer.getMaxCaptureRate() / 2, true, false);
                    mVisualizer.setEnabled(true);
                }
            }


            musicPlayer.prepare();

        } catch (Exception e) {
            Log.e(AudioService.class.getName(), "when playing audio", e);
            onMusicPlayStop();
        }

    }

    @Override
    public void stopMusic() {
        try {
            if (null != musicPlayer) {
                if (!isMusicStopped) {
                    onMusicPlayStop();
                    Log.i(AudioService.class.getName(), "going to stop music");
                }
                //stop and release the previous music
                musicPlayer.stop();
                if (null != musicPlayer) {
                    musicPlayer.reset();
                    musicPlayer.release();

                }
            }
        } catch (Exception ex) {
            Log.e(getClass().getName(), "stopMusic", ex);
        } finally {
            musicPlayer = null;
        }
    }

    @Override
    public String getAudioStorePath() {
        return "";
    }

    @Override
    public boolean isMusicPlaying() {
        return null != musicPlayer && musicPlayer.isPlaying();
    }

    @Override
    public void startRecord(final IMediaRecordListener mediaRecordListener) {

        //stop the current playing music
        stopMusic();
        stopRecord();

        if (null == mediaRecorder) {
            mediaRecorder = new MediaRecorder();
        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(AUDIO_RECORD_OUTPUT_FILE_PATH);

        try {
            isStopRecord = false;
            mediaRecorder.prepare();
            mediaRecorder.start();

            mediaRecorderRunnableCode = new Runnable() {
                @Override
                public void run() {
                    if (!isStopRecord) {
                        double amplitude = mediaRecorder.getMaxAmplitude() / (double) 32768;
                        mediaRecordListener.onRecording(amplitude);
                        recorderHandler.postDelayed(this, 10);
                    }
                }
            };

            mediaRecorderRunnableCode.run();
        } catch (IOException e) {
            Log.e(AudioService.class.getName(), e.getMessage());
        }


    }

    private void onMusicPlayStop() {
        doMusicStop(false);
    }

    private void doMusicStop(boolean isMusicComplete) {

        if (isOSVersionAboveGingerBread()) {
            if (null != mVisualizer) {
                mVisualizer.setEnabled(false);
                //mVisualizer.release();
            }
        }

        if (!isMusicStopped) {
            isMusicStopped = true;
            removeMusicCallback();
            Log.i(AudioService.class.getName(), "going to call stop listener");
            if (null != musicPlayerListener) {
                Log.i(AudioService.class.getName(), "call music stop listener");
                if (isMusicComplete) {
                    musicPlayerListener.onComplete();
                } else {
                    musicPlayerListener.onStop();
                }
            }


        }
    }

    private boolean isOSVersionAboveGingerBread() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
    }

    private void removeMusicCallback() {
        if (null != musicPlayerHandler) {
            musicPlayerHandler.removeCallbacks(musicPlayerRunnableCode);
        }
    }

    @Override
    public String stopRecord() {
        if (null != mediaRecorder) {
            mediaRecorder.reset();
            recorderHandler.removeCallbacks(mediaRecorderRunnableCode);
            mediaRecorderRunnableCode = null;
            isStopRecord = true;

            return AUDIO_RECORD_OUTPUT_FILE_PATH;
        }

        return null;
    }

    @Override
    public void playBackRecord(final ISoundPlayListener musicPlayerListener) {
        playAbsoluteMusicFile(AUDIO_RECORD_OUTPUT_FILE_PATH, musicPlayerListener);
    }

    @Override
    public void playInputStream(InputStream inputStream, final ISoundPlayListener soundPlayListener) {


    }

    @Override
    public void stopPlayInputStream() {

    }

    @Override
    public boolean isAudioExists(String audioFileName) {
        return Gdx.files.internal(audioFileName).exists();
    }

    private void playAbsoluteMusicFile(String filePath, final ISoundPlayListener soundPlayListener) {
        File audioRecord = new File(filePath);
        if (audioRecord.exists()) {
            stopMusic();

            Uri audioRecordUri = Uri.fromFile(audioRecord);
            musicPlayer = MediaPlayer.create(context, audioRecordUri);
            musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (null != soundPlayListener) {
                        Log.i(AudioService.class.getName(), "when playing back record going to call complete listener");
                        musicPlayer.release();
                        musicPlayer = null;
                        soundPlayListener.onComplete();
                    }
                }
            });
            musicPlayer.start();
        }
    }

    private void onMusicPlayComplete() {
        doMusicStop(true);
    }

}
