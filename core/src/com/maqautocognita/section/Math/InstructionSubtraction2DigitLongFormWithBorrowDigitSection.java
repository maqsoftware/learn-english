package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AbstractBitmapFontScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class InstructionSubtraction2DigitLongFormWithBorrowDigitSection extends InstructionSubtraction2DigitLongFormSection {

    private Rectangle tensDigitMinusCarryDigitNumberContainer;

    public InstructionSubtraction2DigitLongFormWithBorrowDigitSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected List<String> generateRandomList() {

        List<String> upperLowerNumberList = new ArrayList<String>(getNumberOfRoundRequiredToPlay());
        List<Integer> upperNumberList = new ArrayList<Integer>(getNumberOfRoundRequiredToPlay());
        for (int i = 0; i < getNumberOfRoundRequiredToPlay(); i++) {
            int upperValue = RandomUtils.getRandomWithExclusion(30, 99, ArrayUtils.toArray(upperNumberList));
            upperNumberList.add(upperValue);

            int upperNumberDigit = upperValue % 10;
            if (upperNumberDigit == 9) {
                upperValue--;
                upperNumberDigit = 8;
            }

            int lowerNumberDigit = RandomUtils.getRandomWithExclusion(upperNumberDigit + 1, 9);

            int lowerNumberTensDigit = RandomUtils.getRandomWithExclusion(1, upperValue / 10 - 2);

            int lowerValue = lowerNumberTensDigit * 10 + lowerNumberDigit;

            upperLowerNumberList.add(upperValue + "_" + lowerValue);
        }

        Collections.shuffle(upperLowerNumberList);


        return upperLowerNumberList;
    }


    @Override
    protected void afterStraightFormulaInitialized(List<AbstractBitmapFontScreenObject> abstractBitmapFontScreenObjectList, int carryDigit) {
        super.afterStraightFormulaInitialized(abstractBitmapFontScreenObjectList, carryDigit);
        hideAllBorrowDigitHints();
    }

    protected void hideAllBorrowDigitHints() {
        if (null != carryDigitNumberScreenObject) {
            carryDigitNumberScreenObject.isVisible = false;
        }
        isStrokeLineOnTensDigitRequiredToShow = false;
        if (null != tensDigitMinusCarryDigitNumberScreenObject) {
            tensDigitMinusCarryDigitNumberScreenObject.isVisible = false;
        }
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (TouchUtils.isTouched(tensDigitMinusCarryDigitNumberContainer, screenX, screenY)) {
            handWritingRecognizeScreenService.addDrawPoint(carrayDigitDrawPoints, screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    protected void drawCarryDigitContainer(ShapeRenderer shapeRenderer, Rectangle carryDigitNumberContainerArea) {
        super.drawCarryDigitContainer(shapeRenderer, carryDigitNumberContainerArea);
        tensDigitMinusCarryDigitNumberContainer = new Rectangle(carryDigitNumberContainerArea.x - carryDigitNumberContainerArea.width,
                carryDigitNumberContainerArea.y, carryDigitNumberContainerArea.width, carryDigitNumberContainerArea.height);
        shapeRenderer.rect(tensDigitMinusCarryDigitNumberContainer.x, tensDigitMinusCarryDigitNumberContainer.y, tensDigitMinusCarryDigitNumberContainer.width, tensDigitMinusCarryDigitNumberContainer.height);
    }
}
