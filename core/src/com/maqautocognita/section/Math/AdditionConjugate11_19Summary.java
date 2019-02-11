package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class AdditionConjugate11_19Summary extends AdditionConjugateSummary {


    public AdditionConjugate11_19Summary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected int getStartNumber() {
        return 11;
    }

    @Override
    protected int getEndNumber() {
        return 19;
    }

    @Override
    protected int getNumberOfRow() {
        return 5;
    }

    @Override
    protected String getBodyTextFromRowColumn(int row, int column) {
        String text = null;
        int leftValue = column - 10 + row;
        if (leftValue <= 10 - row) {
            text = getFormula(leftValue, row, column);
        }

        return text;
    }

    protected String getFormula(int leftValue, int row, int column) {
        return leftValue + "+" + (10 - row) + "=" + column;
    }

}
