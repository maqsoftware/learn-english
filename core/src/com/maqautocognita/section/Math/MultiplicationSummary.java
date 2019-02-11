package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class MultiplicationSummary extends AdditionConjugateSummary {


    public MultiplicationSummary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected int getCellWidth() {
        return 190;
    }

    @Override
    protected String getBodyTextFromRowColumn(int row, int column) {
        return column + " x " + (10 - row) + " = " + (column * (10 - row));
    }

}
