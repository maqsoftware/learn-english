package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * User is asked to say each number presented (number and blocks are automatically generated).  Repeat {@link #NUMBER_OF_ROUND_NEED_TO_PLAY} times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class SayNumberInLargeNumberBlockSection extends ReadAndMakeLargeNumberBlockSection {

    public SayNumberInLargeNumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumValue, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, minimumValue, maximumValue, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isNumberDisplayRequired() {
        return false;
    }

    @Override
    protected boolean isMicrophoneRequired() {
        return true;
    }

    @Override
    protected void afterNumberBlockAdded() {
        //nothing will be do in here, because in this module, the user is no required to touch the number block
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        afterPlayCorrectlyAndNextRoundNumberIsGenerated();
    }

    @Override
    protected void afterPlayCorrectlyAndNextRoundNumberIsGenerated() {
        super.afterPlayCorrectlyAndNextRoundNumberIsGenerated();
        for (int i = 0; i < playingNumber % 10; i++) {
            addNumber1Block();
        }
        for (int i = 0; i < playingNumber / 10 % 10; i++) {
            addNumber10Block();
        }

        for (int i = 0; i < playingNumber / 100 % 100; i++) {
            addNumber100Block();
        }
    }

    @Override
    protected boolean isAllowTouchNumberBlock() {
        return false;
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    public void onCorrectAnswerPlayed() {
        whenRoundPlayed();
    }
}
