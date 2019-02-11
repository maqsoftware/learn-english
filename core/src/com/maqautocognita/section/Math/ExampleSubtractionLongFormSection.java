package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleSubtractionLongFormSection extends InstructionSubtractionLongFormSection {

    private static final int NUMBER_OF_EXAMPLE_REQUIRED_TO_PLAY = 3;

    public ExampleSubtractionLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumAnswer, int maximumAnswer, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, minimumAnswer, maximumAnswer, abstractAutoCognitaScreen, onHelpListener);
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
    protected void next() {
        if (numberOfRoundPlayed < getNumberOfRoundRequiredToPlay()) {
            resetFormula();
            afterResetFormula();
        } else {
            abstractAutoCognitaScreen.setTouchAllow(true);
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
        }
    }

    @Override
    protected void afterResetFormula() {
        onHelp();
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        abstractAutoCognitaScreen.setTouchAllow(false);
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void onHide() {
        super.onHide();
        abstractAutoCognitaScreen.setTouchAllow(true);
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(upperNumber, lowerNumber, upperNumber - lowerNumber);
    }

    @Override
    protected void beforePlayHelpAudio(int index) {
        ScreenObjectUtils.unhighLightAllScreenObject(upperNumberScreenObjectList);
        ScreenObjectUtils.unhighLightAllScreenObject(lowerNumberScreenObjectList);
        ScreenObjectUtils.unhighLightAllScreenObject(answerScreenObjectList);

        if (1 == index) {
            ScreenObjectUtils.highLightAllScreenObject(upperNumberScreenObjectList);
        } else if (3 == index) {
            ScreenObjectUtils.highLightAllScreenObject(lowerNumberScreenObjectList);
        } else if (5 == index) {
            ScreenObjectUtils.highLightAllScreenObject(answerScreenObjectList);
        }
    }

    @Override
    protected void onHelpAudioComplete() {
        numberOfRoundPlayed++;
        next();
    }
}
