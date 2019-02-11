package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.NumberWritingScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.RandomUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionSubtraction2DigitLongFormSection extends InstructionSubtractionLongFormSection {

    protected boolean isStrokeLineOnTensDigitRequiredToShow = true;
    protected NumberScreenObject tensDigitMinusCarryDigitNumberScreenObject;
    private ShapeRenderer strokeLineOnTensDigit;

    public InstructionSubtraction2DigitLongFormSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, 0, 0, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected boolean isCarryDigitContainerRequired() {
        return true;
    }

    @Override
    protected List<String> generateRandomList() {

        List<String> upperLowerNumberList = new ArrayList<String>(getNumberOfRoundRequiredToPlay());
        List<Integer> upperNumberList = new ArrayList<Integer>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int upperValue = RandomUtils.getRandomWithExclusion(20, 99, ArrayUtils.toArray(upperNumberList));
            upperNumberList.add(upperValue);

            int lowerNumberTensDigit = RandomUtils.getRandomWithExclusion(1, upperValue / 10 - 1);
            int lowerNumberDigit = RandomUtils.getRandomWithExclusion(0, upperValue % 10);
            int lowerValue = lowerNumberTensDigit * 10 + lowerNumberDigit;

            upperLowerNumberList.add(upperValue + "_" + lowerValue);
        }

        Collections.shuffle(upperLowerNumberList);


        return upperLowerNumberList;
    }

    @Override
    protected boolean isCarryDigitNeedToShow() {
        return true;
    }

    @Override
    protected void render() {
        super.render();

        if (null != strokeLineOnTensDigit && isStrokeLineOnTensDigitRequiredToShow) {
            //draw stroke line on the tens digit of the upper number
            strokeLineOnTensDigit.setProjectionMatrix(batch.getProjectionMatrix());
            strokeLineOnTensDigit.begin(ShapeRenderer.ShapeType.Filled);
            strokeLineOnTensDigit.setColor(ColorProperties.TEXT);
            strokeLineOnTensDigit.rectLine(straightFormulaStartXPosition + maximumSizeOfNumber[0],
                    straightFormulaStartYPosition + maximumSizeOfNumber[1] * 3,
                    straightFormulaStartXPosition + maximumSizeOfNumber[0] * 2,
                    straightFormulaStartYPosition + maximumSizeOfNumber[1] * 2,
                    HORIZONTAL_LINE_HEIGHT);
            strokeLineOnTensDigit.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != strokeLineOnTensDigit) {
            strokeLineOnTensDigit.dispose();
            strokeLineOnTensDigit = null;
        }
        if (null != tensDigitMinusCarryDigitNumberScreenObject) {
            tensDigitMinusCarryDigitNumberScreenObject = null;
        }
    }

    @Override
    protected void afterStraightFormulaInitialized(List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList, int carryDigit) {
        if (carryDigit > 0) {
            //the number after the ten digit minus the carry digit
            tensDigitMinusCarryDigitNumberScreenObject = new NumberWritingScreenObject(
                    upperNumber / 10 - carryDigit, maximumSizeOfNumber[0],
                    straightFormulaStartXPosition + maximumSizeOfNumber[0],
                    straightFormulaStartYPosition + maximumSizeOfNumber[1] * 3 + GAP_BETWEEN_NUMBER_IN_STRAIGHT_FORMULA,
                    TextFontSizeEnum.FONT_144, true);

            abstractBitmapFontScreenObjectList.add(tensDigitMinusCarryDigitNumberScreenObject);

            if (null == strokeLineOnTensDigit) {
                strokeLineOnTensDigit = new ShapeRenderer();
            }
        } else {
            strokeLineOnTensDigit = null;
        }

    }
}
