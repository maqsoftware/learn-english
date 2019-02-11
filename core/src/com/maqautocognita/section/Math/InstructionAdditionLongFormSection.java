package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.RandomUtils;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionAdditionLongFormSection extends AbstractLongFormSection {

    private static final int NUMBER_OF_ROUND_REQUIRED_TO_PLAY = 10;

    private final int minimumAnswer;

    private final int maximumAnswer;

    public InstructionAdditionLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumAnswer, int maximumAnswer, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.minimumAnswer = minimumAnswer;
        this.maximumAnswer = maximumAnswer;
        generateRandomList();
    }

    @Override
    public void dispose() {
        super.dispose();
        numberOfRoundPlayed = 0;
    }

    @Override
    protected int getCarryDigitToTensDigit(int theFirstDigitInUpperNumber, int theSecondDigitInLowerNumber) {
        return (theFirstDigitInUpperNumber + theSecondDigitInLowerNumber) / 10;
    }

    @Override
    protected String getFormulaSymbol() {
        return "+";
    }

    @Override
    protected int getAnswer(int upper, int lower) {
        return upper + lower;
    }

    @Override
    protected boolean isAnswerContainerRequired() {
        return true;
    }

    @Override
    protected List<String> generateRandomList() {
        int maximumNumberOfX = maximumAnswer - 1;
        List<Integer> upperValueList = new ArrayList<Integer>(maximumNumberOfX);
        for (int i = 0; i < maximumNumberOfX; i++) {
            upperValueList.add(RandomUtils.getRandomWithExclusion(1, maximumAnswer - 1, ArrayUtils.toArray(upperValueList)));
        }

        Collections.shuffle(upperValueList);

        List upperLowerList = new ArrayList<String>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int upperValue;
            if (i < upperValueList.size()) {
                upperValue = upperValueList.get(i);
            } else {
                if (0 == i % upperValueList.size()) {
                    Collections.shuffle(upperValueList);
                }
                upperValue = upperValueList.get(i % upperValueList.size());
            }
            Gdx.app.log(getClass().getName(), "lower value range = " + Math.max(1, minimumAnswer - upperValue) + " " + (maximumAnswer - upperValue));
            upperLowerList.add(upperValue + "_" + RandomUtils.getRandomWithExclusion(Math.max(1, minimumAnswer - upperValue), maximumAnswer - upperValue));
        }

        return upperLowerList;
    }

    @Override
    protected float getCarryDigitXPosition(NumberScreenObject tensDigitNumberScreenObject, float carryDigitWith, float numberTargetWidth) {
        return tensDigitNumberScreenObject.xPositionInScreen + (tensDigitNumberScreenObject.width - carryDigitWith) / 2;
    }

    @Override
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
