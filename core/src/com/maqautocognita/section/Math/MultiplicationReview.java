package com.maqautocognita.section.Math;


import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;

/**
 * User to answer multiplication questions up to 10x10.  The multiplication matrix is no longer available.  Repeat 8 times.
 *
 * @author sc.chi csc19840914@gmail.com
 */

public class MultiplicationReview extends MultiplicationMatrixReview {


    public MultiplicationReview(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }


    @Override
    protected boolean isMultiplicationMatrixRequired() {
        return false;
    }

    @Override
    protected void drawFormulaWithoutChangeExistingYPosition(String formula, float xPosition, float yPosition, TextFontSizeEnum textFontSizeEnum) {
        float[] size = LetterUtils.getSizeOfWord(formula, textFontSizeEnum);
        xPosition = ScreenUtils.getXPositionForCenterObject(size[0] + (null == writingPad ? 0 : writingPad.width));
        yPosition = ScreenUtils.getBottomYPositionForCenterObject(size[1]);
        super.drawFormulaWithoutChangeExistingYPosition(formula, xPosition, yPosition, textFontSizeEnum);
    }

    @Override
    protected void initNumberWritingPad() {
        if (null != formulaTextScreenObject) {
            super.initNumberWritingPad();
            writingPad.yPositionInScreen = ScreenUtils.getBottomYPositionForCenterObject(writingPad.height);
        }
    }

    @Override
    protected void nextRound() {
        resetScreen();
        yValue = RandomUtils.getRandomWithExclusion(1, 10);
        xValue = RandomUtils.getRandomWithExclusion(1, 10);
        result = xValue * yValue;
    }
}
