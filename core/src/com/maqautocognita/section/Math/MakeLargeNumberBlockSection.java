package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * User swipes finger to place white and orange blocks to make different numbers.
 * If the given {@link #maximumValue} is reached, the lesson is make  as complete
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class MakeLargeNumberBlockSection extends AbstractLargeNumberBlockSection {

    public MakeLargeNumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, maximumValue, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        resetScreen();
        addNumber10Block();
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return false;
    }

    @Override
    protected void afterNumberBlockAdded() {
        super.afterNumberBlockAdded();
        if (result == maximumValue) {
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
        }
        playNumberAudio();
    }

    @Override
    protected void afterNumberBlockRemoved() {
        super.afterNumberBlockRemoved();
        playNumberAudio();
    }

    private void playNumberAudio() {
        if (isIntroductionAudioPlayed) {
            abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getNumberAudioFileList(result));
        }
    }
}
