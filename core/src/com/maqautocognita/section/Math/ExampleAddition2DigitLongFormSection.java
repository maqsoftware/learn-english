package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.badlogic.gdx.Gdx;

import java.util.List;


/**
 * shows {@link #NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY} examples.
 * Each is a 2-digit addition, where the ones digit add up to <=9.  Highlight the ones column, then show the ones answer, then highlight the tens column, then show the tens answer.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleAddition2DigitLongFormSection extends InstructionAddition2DigitLongFormSection implements TimerService.ITimerListener {

    private static final int NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY = 4;

    private TimerService timerService;

    public ExampleAddition2DigitLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);
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
        abstractAutoCognitaScreen.setTouchAllow(false);
        resetFormula();
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void afterStraightFormulaInitialized(List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList, int carryDigit) {
        super.afterStraightFormulaInitialized(abstractBitmapFontScreenObjectList, carryDigit);
        ScreenObjectUtils.hideAllScreenObject(answerScreenObjectList);
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
        if (0 == startIndex) {
            upperNumberScreenObjectList.get(0).isHighlighted = true;
        } else if (1 == startIndex) {
            lowerNumberScreenObjectList.get(0).isHighlighted = true;
        } else if (2 == startIndex) {
            answerScreenObjectList.get(0).isVisible = true;
            answerScreenObjectList.get(0).isHighlighted = true;
        } else if (3 == startIndex) {
            upperNumberScreenObjectList.get(1).isHighlighted = true;
        } else if (4 == startIndex) {
            lowerNumberScreenObjectList.get(1).isHighlighted = true;
        } else if (5 == startIndex) {
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
