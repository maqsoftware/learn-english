package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionAddition2DigitLongFormSection extends InstructionAdditionLongFormSection {


    public InstructionAddition2DigitLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 0, 0, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected List<String> generateRandomList() {

        List<String> upperLowerNumberList = new ArrayList<String>(getNumberOfRoundRequiredToPlay());
        List<Integer> upperNumberList = new ArrayList<Integer>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int answer = RandomUtils.getRandomWithExclusion(20, 99);
            int upperValue = RandomUtils.getRandomWithExclusion(10, answer - 10, ArrayUtils.toArray(upperNumberList));

            if (answer % 10 < upperValue % 10) {
                //the ones digit add up must be <=9
                upperValue = upperValue - (upperValue % 10) + RandomUtils.getRandomWithExclusion(0, answer % 10);
            }
            upperNumberList.add(upperValue);
            int lowerValue = answer - upperValue;
            upperLowerNumberList.add(upperValue + "_" + lowerValue);
        }

        Collections.shuffle(upperLowerNumberList);


        return upperLowerNumberList;
    }

}
