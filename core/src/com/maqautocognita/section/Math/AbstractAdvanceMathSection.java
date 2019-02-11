package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ScreenUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAdvanceMathSection extends AbstractNumberPadSection {


    protected TextScreenObject formulaTextScreenObject;
    private AutoCognitaTextureRegion dividerAutoCognitaTextureRegion;
    private TextScreenObject minusTextScreenObject;
    private TextScreenObject equalsTextScreenObject;

    public AbstractAdvanceMathSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        dividerAutoCognitaTextureRegion = null;
        minusTextScreenObject = null;
        equalsTextScreenObject = null;
        formulaTextScreenObject = null;

    }

    protected void drawFormula(String formula) {
        drawFormulaWithoutChangeExistingYPosition(formula, ScreenUtils.getXPositionForCenterObject(LetterUtils.getTotalWidthOfWord(formula, TextFontSizeEnum.FONT_144)),
                BIG_NUMBER_START_Y_POSITION, TextFontSizeEnum.FONT_144);
    }

    protected void drawFormulaWithoutChangeExistingYPosition(String formula, float xPosition, float yPosition, TextFontSizeEnum textFontSizeEnum) {
        if (null == formulaTextScreenObject) {
            formulaTextScreenObject = new TextScreenObject(formula, xPosition, yPosition, textFontSizeEnum, true);
        } else {
            formulaTextScreenObject.xPositionInScreen = xPosition;
            formulaTextScreenObject.setDisplayText(formula);
        }
        ScreenObjectUtils.draw(batch, formulaTextScreenObject);
    }

    protected void drawFormula(String formula, float xPosition, float yPosition, TextFontSizeEnum textFontSizeEnum) {
        if (null == formulaTextScreenObject) {
            formulaTextScreenObject = new TextScreenObject(formula, xPosition, yPosition, textFontSizeEnum, true);
        } else {
            formulaTextScreenObject.xPositionInScreen = xPosition;
            formulaTextScreenObject.yPositionInScreen = yPosition;
            formulaTextScreenObject.setDisplayText(formula);
        }
        ScreenObjectUtils.draw(batch, formulaTextScreenObject);

    }

    protected void drawMinusScreenObject(float xPosition, float yPosition) {

        if (null == minusTextScreenObject) {
            minusTextScreenObject = new TextScreenObject("-", xPosition, yPosition, TextFontSizeEnum.FONT_144);
        }

        ScreenObjectUtils.draw(batch, minusTextScreenObject);
    }

    protected void drawEqualsScreenObject(float xPosition, float yPosition) {

        if (null == equalsTextScreenObject) {
            equalsTextScreenObject = new TextScreenObject("=", xPosition, yPosition, TextFontSizeEnum.FONT_144);
        }

        ScreenObjectUtils.draw(batch, equalsTextScreenObject);
    }

    protected AutoCognitaTextureRegion getDividerAutoCognitaTextureRegion() {
        if (null == dividerAutoCognitaTextureRegion) {
            dividerAutoCognitaTextureRegion = new AutoCognitaTextureRegion(numberTrayTexture, 0, 1000, 1620, 2);
        }
        return dividerAutoCognitaTextureRegion;
    }


}
