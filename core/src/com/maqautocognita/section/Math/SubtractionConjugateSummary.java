package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class SubtractionConjugateSummary extends AdditionConjugateSummary {


    public SubtractionConjugateSummary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected String getBodyTextFromRowColumn(int row, int column) {
        String text = null;
        if (column > row) {
            text = column + " - " + (column - row) + " = " + row;
        }

        return text;
    }

}
