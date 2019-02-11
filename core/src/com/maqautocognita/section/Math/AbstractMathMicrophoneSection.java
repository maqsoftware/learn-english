package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.MicrophoneService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.NumberToWordsUtils;
import com.maqautocognita.utils.StringUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractMathMicrophoneSection extends AbstractMathSection implements MicrophoneService.IMicrophoneListener {

    protected final MicrophoneService microphoneService;
    protected final MathAudioScriptWithElementCode mathAudioScriptWithElementCode;

    public AbstractMathMicrophoneSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.microphoneService = new MicrophoneService(this);
        this.mathAudioScriptWithElementCode = mathAudioScriptWithElementCode;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        microphoneService.changeRecognizeFile();
    }

    @Override
    protected void render() {
        super.render();
        if (isMicrophoneRequired()) {
            batch.begin();
            ScreenObjectUtils.draw(batch, microphoneService.getMicrophone());
            batch.end();
        }
    }

    protected boolean isMicrophoneRequired() {
        return true;
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return mathAudioScriptWithElementCode;
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        microphoneService.touchUp();
    }

    @Override
    public float getMicrophoneStartXPosition() {
        return 1550;
    }

    @Override
    public float getMicrophoneStartYPosition() {
        return Config.SPEAKING_SOUND_START_Y_POSITION + 93;
    }

    @Override
    public boolean isSoundRecognizeRequired() {
        return true;
    }

    @Override
    public void onCorrectAnswerPlayed() {
        abstractAutoCognitaScreen.playCorrectSound();
    }

    @Override
    public boolean onSpeechRecognizeResult(String[] results) {
        String englishText = NumberToWordsUtils.convert(getNumberRequiredForSpeechRecognize());

        if (StringUtils.isNotBlank(englishText)) {
            for (String text : englishText.split(" ")) {
                if (!ArrayUtils.isContainIgnoreCase(results, text)) {
                    //if on the number word is not include in the result from speech recogize
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public String getRecognizeFileName() {
        return "digits.gram";
    }

    protected abstract int getNumberRequiredForSpeechRecognize();
}
