package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.ISpeechRecognizeResultListener;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.graphics.Texture;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MicrophoneService implements ISpeechRecognizeResultListener {


    //in order to play the user sound correctly without any cutoff in last second, below is the millisecond here will delay the recording
//    private static final int RECORD_DELAY_MILLISECOND = 400;
    private static final int RECORD_DELAY_MILLISECOND = 0;
    private static final IconPosition NOT_PRESSED_MICROPHONE = new IconPosition(ImageProperties.NOT_PRESSED_MICROPHONE.x,
            ImageProperties.NOT_PRESSED_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private static final IconPosition PRESSED_MICROPHONE = new IconPosition(ImageProperties.PRESSED_MICROPHONE.x,
            ImageProperties.PRESSED_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private static final IconPosition PRESSED_20_40_MICROPHONE = new IconPosition(ImageProperties.PRESSED_20_40_MICROPHONE.x,
            ImageProperties.PRESSED_20_40_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private static final IconPosition PRESSED_40_60_MICROPHONE = new IconPosition(ImageProperties.PRESSED_40_60_MICROPHONE.x,
            ImageProperties.PRESSED_40_60_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private static final IconPosition PRESSED_60_80_MICROPHONE = new IconPosition(ImageProperties.PRESSED_60_80_MICROPHONE.x,
            ImageProperties.PRESSED_60_80_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private static final IconPosition PRESSED_80_100_MICROPHONE = new IconPosition(ImageProperties.PRESSED_80_100_MICROPHONE.x,
            ImageProperties.PRESSED_80_100_MICROPHONE.y, ImageProperties.MICROPHONE_SIZE, ImageProperties.MICROPHONE_SIZE);
    private final IMicrophoneListener microphoneListener;
    /**
     * Indicate if the sound recognizing is processing, during the processing, no another recognizing will be process
     */
    private boolean isRecognizing;
    private Texture microphoneTexture;
    private TextureScreenObject<Object, ScreenObjectType> microphone;

    public MicrophoneService(IMicrophoneListener microphoneListener) {
        this.microphoneListener = microphoneListener;
    }

    public void reset() {
        isRecognizing = false;
    }

    public void changeRecognizeFile() {
        if (microphoneListener.isSoundRecognizeRequired() && AutoCognitaGame.speechRecognizeService.isSpeechRecognizeSupport()) {
            AutoCognitaGame.speechRecognizeService.changeRecognizeFile(microphoneListener.getRecognizeFileName());
        }
    }

    public TextureScreenObject<Object, ScreenObjectType> getMicrophone() {
        if (null == microphone) {
            AssetManagerUtils.loadTexture(AssetManagerUtils.MICROPHONE);
            AssetManagerUtils.blockUtilsFinishLoading();
            microphoneTexture = AssetManagerUtils.getTexture(AssetManagerUtils.MICROPHONE);
            microphone = new TextureScreenObject<Object, ScreenObjectType>(null, ScreenObjectType.MICROPHONE, NOT_PRESSED_MICROPHONE,
                    microphoneListener.getMicrophoneStartXPosition(), microphoneListener.getMicrophoneStartYPosition(), microphoneTexture);
        }

        return microphone;
    }

    public boolean touchDown(int screenX, int screenY) {

        TextureScreenObject<Object, ScreenObjectType> touchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(microphone, screenX, screenY);
        if (null != touchingScreenObject && !isRecognizing) {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_MICROPHONE);
            microphone.isHighlighted = true;
            isRecognizing = true;
            if (microphoneListener.isSoundRecognizeRequired() && AutoCognitaGame.speechRecognizeService.isSpeechRecognizeSupport()) {
                AutoCognitaGame.speechRecognizeService.startSpeechListening(this);
            }

            return true;
        }
        return false;


    }

    public void touchUp() {
        if (null != microphone && microphone.isHighlighted && isRecognizing) {
            {
                microphone.isHighlighted = false;
                //in order to play the user sound correctly without any cutoff in last second, here will delay the recording
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (microphoneListener.isSoundRecognizeRequired() && AutoCognitaGame.speechRecognizeService.isSpeechRecognizeSupport()) {
                            AutoCognitaGame.speechRecognizeService.stopSpeechListening();
                        } else {
                            //always handle it as sound played correctly
                            microphoneListener.onCorrectAnswerPlayed();
                            isRecognizing = false;
                        }
                    }

                }, RECORD_DELAY_MILLISECOND);

            }
        }
    }

    @Override
    public void onResult(String[] results) {
        if (microphoneListener.onSpeechRecognizeResult(results)) {
            microphoneListener.onCorrectAnswerPlayed();
        }
        isRecognizing = false;
    }

    @Override
    public void onRecording(float amplitudePercentage) {
        if (amplitudePercentage < 0.2) {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_MICROPHONE);
        } else if (amplitudePercentage < 0.4) {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_20_40_MICROPHONE);
        } else if (amplitudePercentage < 0.6) {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_40_60_MICROPHONE);
        } else if (amplitudePercentage < 0.8) {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_60_80_MICROPHONE);
        } else {
            microphone.textureRegionInHighlightState = new AutoCognitaTextureRegion(microphoneTexture, PRESSED_80_100_MICROPHONE);
        }
    }

    public interface IMicrophoneListener {
        float getMicrophoneStartXPosition();

        float getMicrophoneStartYPosition();

        boolean isSoundRecognizeRequired();

        void onCorrectAnswerPlayed();

        boolean onSpeechRecognizeResult(String[] results);

        String getRecognizeFileName();
    }


}
