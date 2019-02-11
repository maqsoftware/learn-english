package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SubtractionConjugate11_19Summary extends AdditionConjugate11_19Summary {


    public SubtractionConjugate11_19Summary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    protected String getFormula(int leftValue, int row, int column) {
        return column + "-" + (10 - row) + "=" + (column - (10 - row));
    }

}
