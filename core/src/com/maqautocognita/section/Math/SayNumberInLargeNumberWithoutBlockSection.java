package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * User is asked to say random numbers (without blocks) shown on the screen 1-999.  Repeat {@link #NUMBER_OF_ROUND_NEED_TO_PLAY} times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class SayNumberInLargeNumberWithoutBlockSection extends SayNumberInLargeNumberBlockSection {

    private final static int NUMBER_OF_ROUND_NEED_TO_PLAY = 10;

    public SayNumberInLargeNumberWithoutBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 1, 999, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isNumberBlocksShowRequired() {
        return false;
    }

    @Override
    protected int getNumberOfRoundNeedToPlay() {
        return NUMBER_OF_ROUND_NEED_TO_PLAY;
    }

}
