package com.maqautocognita.desktop;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.Config;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.adapter.IDeviceCameraService;
import com.maqautocognita.adapter.IDeviceService;
import com.maqautocognita.adapter.IHandWritingRecognizeService;
import com.maqautocognita.adapter.IOCR;
import com.maqautocognita.adapter.IShareService;
import com.maqautocognita.adapter.ISpeechRecognizeService;
import com.maqautocognita.bo.OCRResult;
import com.maqautocognita.desktop.databases.DesktopDatabase;
import com.maqautocognita.listener.IMediaRecordListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.listener.ISpeechRecognizeResultListener;
import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DesktopLauncher {

    private static final boolean IS_TABLET_MODE = false;
    private static final String DICTIONARY_DATABASE_PATH = "C:\\GLEXP-Team-AutoCognita\\expansion_files\\autoCognita.db";
    private static final String AUDIO_PATH = "C:\\GLEXP-Team-AutoCognita\\expansion_files\\audio\\";
    private static Music speechSound;

    public static void main(String[] arg) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (IS_TABLET_MODE ? Config.TABLET_SCREEN_WIDTH : Config.MOBILE_SCREEN_WIDTH) / 2;
        config.height = (IS_TABLET_MODE ? Config.TABLET_SCREEN_HEIGHT : Config.MOBILE_SCREEN_HEIGHT) / 2;

        final AutoCognitaGame game = new AutoCognitaGame(IS_TABLET_MODE,21);

        new LwjglApplication(game, config);

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                game.showLoadingScreen();

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            startGame(game);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private static void startGame(AutoCognitaGame game) {
        game.start("jdbc:sqlite:autocognita_lesson.db", "jdbc:sqlite:" + DICTIONARY_DATABASE_PATH, new IAudioService() {

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
                        return AUDIO_PATH;
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
                    public void playBackRecord(String fileName, final ISoundPlayListener musicPlayerListener) {
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
                        return new File(AUDIO_PATH + File.separator + audioFileName).exists() || Gdx.files.internal(audioFileName).exists();
                    }


                }, new IHandWritingRecognizeService() {

                    @Override
                    public void addPoint(float pointX, float pointY) {

                    }

                    @Override
                    public void clearPoints() {

                    }

                    @Override
                    public boolean isCorrect(String letterToBeCheck) {
                        return true;
                    }


                }, new ISpeechRecognizeService() {

                    private ISpeechRecognizeResultListener speechRecognizeResultListener;

                    @Override
                    public void changeRecognizeFile(String fileName) {

                    }

                    @Override
                    public void startSpeechListening(ISpeechRecognizeResultListener speechRecognizeResultListener) {
                        this.speechRecognizeResultListener = speechRecognizeResultListener;
                    }

                    @Override
                    public void stopSpeechListening() {
                        //this is mainly used to simulate the result of the array after the Speech Recognize in alphabet speaking module is played
                        speechRecognizeResultListener.onResult(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "loud", "star", "page", "fur", "cow"});
                    }

                    @Override
                    public void startIBMSpeechToTextListener(AbstractAdvanceListener<String> advanceActionListener, com.ibm.watson.developer_cloud.language_translator.v2.model.Language language) {

                    }


                    @Override
                    public void stopIBMSpeechToTextListener() {

                    }

                    @Override
                    public boolean isSpeechRecognizeSupport() {
                        return false;
                    }
                }, new IDeviceService() {
                    @Override
                    public void setScreenOrientationToLandscape() {
                        Gdx.graphics.setWindowedMode(Config.TABLET_SCREEN_WIDTH / 2, Config.TABLET_SCREEN_HEIGHT / 2);
                    }

                    @Override
                    public void setScreenOrientationToPortrait() {
                        Gdx.graphics.setWindowedMode(Config.MOBILE_SCREEN_WIDTH / 2, Config.MOBILE_SCREEN_HEIGHT / 2);
                    }

                    @Override
                    public double[] getUserCurrentLocation() {
                        return null;
                    }

                    @Override
                    public boolean isStoryModeEnable() {
                        return false;
                    }

                    @Override
                    public String getVersionName() {
                        return "1.17";
                    }

                    @Override
                    public boolean isSpanishLocale() {
                        return false;
                    }

                },
                new IDeviceCameraService() {

                    @Override
                    public void stopPreview() {

                    }

                    @Override
                    public void takePicture(float heightRatioFromPictureTop, String savePictureFullPath, final IAdvanceActionListener<Boolean> actionListener, int compressQuality) {
                        takePhoto(actionListener);
                    }

                    @Override
                    public void takePictureFromTablet(float widthRatio, String savePictureFullPath, IAdvanceActionListener<Boolean> actionListener, int compressQuality) {
                        takePhoto(actionListener);
                    }

                    @Override
                    public void startPreviewAsync() {

                    }

                    @Override
                    public boolean isReady() {
                        return true;
                    }

                    private void takePhoto(final IAdvanceActionListener<Boolean> actionListener) {
                        //simulate the picture process time
                        new Thread(new Runnable() {
                            long millisecond = 0;

                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    millisecond++;
                                    if (millisecond == 2000) {
                                        if (null != actionListener) {
                                            actionListener.onComplete(true);
                                        }
                                    }
                                }
                            }
                        }).start();
                    }
                },

                new DesktopDatabase("jdbc:sqlite:autocognita_storymode.db"), new IOCR() {
                    @Override
                    public ArrayList<int[]> getOCRRect(String pImage) {
                        return null;
                    }

                    @Override
                    public void processImage(String imageFilePath, IAdvanceActionListener<OCRResult> actionListener) {
                        if (null != actionListener) {
                            actionListener.onComplete(null);
                        }
                    }


                    @Override
                    public ArrayList<String> processImage(String pImageFilePath, int left, int top, int width, int height) {
                        return null;
                    }
                }, new IShareService() {
                    @Override
                    public void sharePhotoWithTextToFacebook(String imagePath, String text) {

                    }

                    @Override
                    public void shareTextToFacebook(String text) {

                    }

                    @Override
                    public void shareToWhatsapp(String text) {

                    }

                    @Override
                    public boolean isFacebookInstalled() {
                        return false;
                    }

                    @Override
                    public boolean isWhatsappInstalled() {
                        return false;
                    }
                }, new IAnalyticSpotService() {

                    @Override
                    public void updateScore(int ProgressCompleted, String UnitCode, String LessonCode, String ElementSequence, String ElementCode, String ProgressType, String language) {

                    }

                    @Override
                    public void updateMathScore(int ProgressCompleted, String ElementCode, String language) {

                    }
                });
    }
}
