package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionSubtractionLongFormSection extends AbstractLongFormSection {

    private static final int NUMBER_OF_ROUND_REQUIRED_TO_PLAY = 10;

    private final int minimumAnswer;

    private final int maximumAnswer;

    public InstructionSubtractionLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumAnswer, int maximumAnswer, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.minimumAnswer = minimumAnswer;
        this.maximumAnswer = maximumAnswer;
        generateRandomList();
    }

    @Override
    protected int getCarryDigitToTensDigit(int theFirstDigitInUpperNumber, int theSecondDigitInLowerNumber) {
        return theFirstDigitInUpperNumber < theSecondDigitInLowerNumber ? 1 : 0;
    }

    @Override
    protected String getFormulaSymbol() {
        return "-";
    }

    @Override
    protected int getAnswer(int upper, int lower) {
        return upper - lower;
    }

    @Override
    protected boolean isAnswerContainerRequired() {
        return true;
    }

    @Override
    protected boolean isCarryDigitContainerRequired() {
        return false;
    }

    protected List<String> generateRandomList() {
        //generate all possible formula
        //Ten random examples are given.    Answer is >=0
        List<String> formulaList = new ArrayList<String>();
        for (int upperNumber = maximumAnswer; upperNumber > minimumAnswer; upperNumber--) {
            for (int lowerNumber = minimumAnswer; lowerNumber < upperNumber; lowerNumber++) {
                formulaList.add(upperNumber + "_" + lowerNumber);
            }
        }

        Collections.shuffle(formulaList);


        return formulaList.subList(0, getNumberOfRoundRequiredToPlay());
    }

    @Override
    protected boolean isCarryDigitNeedToShow() {
        return false;
    }

    @Override
    protected float getCarryDigitXPosition(NumberScreenObject tensDigitNumberScreenObject, float carryDigitWith, float numberTargetWidth) {
        return tensDigitNumberScreenObject.xPositionInScreen + tensDigitNumberScreenObject.width;
    }

    protected int getNumberOfRoundRequiredToPlay() {
        return NUMBER_OF_ROUND_REQUIRED_TO_PLAY;
    }

    @Override
    protected void next() {
        if (numberOfRoundPlayed < getNumberOfRoundRequiredToPlay()) {
            super.next();
            afterResetFormula();
        } else {
            abstractAutoCognitaScreen.setTouchAllow(true);
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
        }
    }

    protected void afterResetFormula() {
    }
}
