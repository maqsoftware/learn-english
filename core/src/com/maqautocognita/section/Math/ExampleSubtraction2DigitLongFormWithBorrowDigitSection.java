package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.badlogic.gdx.Gdx;


/**
 * Shows {@link #NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY} examples.
 * Each is a 2-digit subtraction, where the ones in the top number is smaller than the ones in the bottom number.
 * Highlight the ones column,  then put a small 1 just to the upper left side of the upper ones number, then show the ones answer,
 * then highlight the tens column, then put a "slash" across the upper number in the tens column, then put a smaller number equal to <upper number in tens column minus 1> above the slashed number, then show the tens answer.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleSubtraction2DigitLongFormWithBorrowDigitSection extends InstructionSubtraction2DigitLongFormWithBorrowDigitSection implements TimerService.ITimerListener {

    private static final int NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY = 4;

    private final TimerService timerService;

    public ExampleSubtraction2DigitLongFormWithBorrowDigitSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.timerService = new TimerService(this);
    }

    @Override
    protected void onHide() {
        super.onHide();
        if (null != timerService) {
            timerService.clearTimer();
        }
    }

    @Override
    protected boolean isAnswerContainerRequired() {
        return false;
    }

    @Override
    protected int getNumberOfRoundRequiredToPlay() {
        return NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        hideAllBorrowDigitHints();
        abstractAutoCognitaScreen.setTouchAllow(false);
        resetFormula();
    }

    @Override
    protected void hideAllBorrowDigitHints() {
        super.hideAllBorrowDigitHints();
        ScreenObjectUtils.hideAllScreenObject(answerScreenObjectList);
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        startExample(0);
    }

    private void startExample(int startIndex) {
        ScreenObjectUtils.unhighLightAllScreenObject(upperNumberScreenObjectList);
        ScreenObjectUtils.unhighLightAllScreenObject(lowerNumberScreenObjectList);
        ScreenObjectUtils.unhighLightAllScreenObject(answerScreenObjectList);
        carryDigitNumberScreenObject.isHighlighted = false;
        tensDigitMinusCarryDigitNumberScreenObject.isHighlighted = false;
        answerScreenObjectList.get(0).isHighlighted = false;
        answerScreenObjectList.get(1).isHighlighted = false;
        upperNumberScreenObjectList.get(0).isHighlighted = false;
        upperNumberScreenObjectList.get(1).isHighlighted = false;
        lowerNumberScreenObjectList.get(0).isHighlighted = false;
        lowerNumberScreenObjectList.get(1).isHighlighted = false;

        if (0 == startIndex) {
            upperNumberScreenObjectList.get(0).isHighlighted = true;
        } else if (1 == startIndex) {
            carryDigitNumberScreenObject.isVisible = true;
            carryDigitNumberScreenObject.isHighlighted = true;
            upperNumberScreenObjectList.get(0).isHighlighted = true;
        } else if (2 == startIndex) {
            lowerNumberScreenObjectList.get(0).isHighlighted = true;
        } else if (3 == startIndex) {
            answerScreenObjectList.get(0).isVisible = true;
            answerScreenObjectList.get(0).isHighlighted = true;
        } else if (4 == startIndex) {
            isStrokeLineOnTensDigitRequiredToShow = true;
            tensDigitMinusCarryDigitNumberScreenObject.isVisible = true;
            tensDigitMinusCarryDigitNumberScreenObject.isHighlighted = true;
        } else if (5 == startIndex) {
            lowerNumberScreenObjectList.get(1).isHighlighted = true;
        } else if (6 == startIndex) {
            answerScreenObjectList.get(1).isVisible = true;
            answerScreenObjectList.get(1).isHighlighted = true;
        } else {
            onExampleComplete();
            return;
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Gdx.app.error(getClass().getName(), "", e);
        }
        startExample(startIndex + 1);
    }

    protected void onExampleComplete() {
        numberOfRoundPlayed++;
        if (numberOfRoundPlayed < getNumberOfRoundRequiredToPlay()) {
            timerService.startTimer(null, 2);
        } else {
            abstractAutoCognitaScreen.setTouchAllow(true);
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
        }
    }

    @Override
    public void beforeStartTimer() {
        next();
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        //it will be call after the previous example is complete and the timer is complete
        startExample(0);
    }
}
