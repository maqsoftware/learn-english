package com.maqautocognita.desktop;

import com.maqautocognita.AutoCognitaStoryLibraryGame;
import com.maqautocognita.Config;
import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.listener.IMediaRecordListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StoryLibraryDesktopLauncher {

    private static final String DATABASE_PATH = "C:\\Users\\siu-chun.chi\\Desktop\\workspace\\java\\android\\GLEXP-Team-AutoCognita\\africanStoryBooksLibrary\\assets\\autocognita_storybook.db";
    private static final boolean IS_TABLET_MODE = true;
    private static Music speechSound;

    public static void main(String[] arg) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (IS_TABLET_MODE ? Config.TABLET_SCREEN_WIDTH : Config.MOBILE_SCREEN_WIDTH) / 2;
        config.height = (IS_TABLET_MODE ? Config.TABLET_SCREEN_HEIGHT : Config.MOBILE_SCREEN_HEIGHT) / 2;

        new LwjglApplication(new AutoCognitaStoryLibraryGame("jdbc:sqlite:" + DATABASE_PATH, new IAudioService() {

            @Override
            public void playAudio(String fileNameWithExtension,
                                  final ISoundPlayListener musicPlayerListener) {
                if (null != musicPlayerListener) {

                    new Thread(new Runnable() {
                        long millisecond = 0;

                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    Thread.sleep(1);
                                    millisecond++;
                                    musicPlayerListener.onPlay(0, millisecond);
                                    if (millisecond == 1000) {
                                        //simulate the play audio task, assume the audio will be play 1 second
                                        musicPlayerListener.onComplete();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }


            @Override
            public void stopMusic() {
                Gdx.app.log(getClass().getName(), "going to stop music");
            }

            @Override
            public String getAudioStorePath() {
                return "";
            }

            @Override
            public boolean isMusicPlaying() {
                return false;
            }

            @Override
            public void startRecord(IMediaRecordListener mediaRecordListener) {

            }

            @Override
            public void startRecord(IMediaRecordListener mediaRecordListener, String fileName) {

            }

            @Override
            public String stopRecord() {
                return null;
            }

            @Override
            public void playBackRecord(ISoundPlayListener musicPlayerListener) {
                if (null != musicPlayerListener) {
                    musicPlayerListener.onComplete();
                }
            }

            @Override
            public void playBackRecord(String fileName, ISoundPlayListener musicPlayerListener) {

            }

            @Override
            public void playInputStream(InputStream inputStream, final ISoundPlayListener soundPlayListener) {
                stopPlayInputStream();

                OutputStream out = null;
                InputStream in = null;
                try {
                    String wavFilePath = Gdx.files.getLocalStoragePath() + "/speech.wav";
                    in = WaveUtils.reWriteWaveHeader(inputStream);
                    out = new FileOutputStream(wavFilePath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    speechSound = Gdx.audio.newMusic(Gdx.files.absolute(wavFilePath));

                    speechSound.setOnCompletionListener(new Music.OnCompletionListener() {
                        @Override
                        public void onCompletion(Music music) {
                            if (null != soundPlayListener) {
                                soundPlayListener.onComplete();
                            }

                            music.dispose();
                            speechSound = null;
                        }
                    });

                    speechSound.play();

                } catch (Exception ex) {

                    if (null != out) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            //nothing can do in here
                        }
                    }
                    if (null != in) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            //nothing can do in here
                        }
                    }

                }

            }

            @Override
            public void stopPlayInputStream() {
                if (null != speechSound && speechSound.isPlaying()) {
                    speechSound.stop();
                }
            }

            @Override
            public boolean isAudioExists(String audioFileName) {
                return Gdx.files.internal(audioFileName).exists();
            }


        }), config);

    }


}
