package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionAddition2DigitLongFormWithCarryDigitSection extends InstructionAddition2DigitLongFormSection {


    public InstructionAddition2DigitLongFormWithCarryDigitSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    protected List<String> generateRandomList() {
        List<Integer> xList = new ArrayList<Integer>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int upperValue = RandomUtils.getRandomWithExclusion(10, 79, ArrayUtils.toArray(xList));
            if (upperValue % 10 == 0) {
                upperValue++;
            }
            xList.add(upperValue);
        }

        Collections.shuffle(xList);

        List upperLowerList = new ArrayList<String>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int upperValue = xList.get(i);

            int lowerNumberDigit = RandomUtils.getRandomWithExclusion(10 - upperValue % 10, 9);
            int lowerNumberTensDigit = RandomUtils.getRandomWithExclusion(1, 10 - upperValue / 10 - 2);
            int lowerValue = lowerNumberTensDigit * 10 + lowerNumberDigit;

            upperLowerList.add(upperValue + "_" + lowerValue);
        }

        return upperLowerList;
    }

    @Override
    protected void afterStraightFormulaInitialized(List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList, int carryDigit) {
        super.afterStraightFormulaInitialized(abstractBitmapFontScreenObjectList, carryDigit);
        carryDigitNumberScreenObject.isVisible = false;
    }

    @Override
    protected boolean isCarryDigitContainerRequired() {
        return true;
    }
}
