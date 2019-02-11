package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractTableSummary extends AbstractMathSection {

    public AbstractTableSummary(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, final AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        resetScreen();
        super.onShowAgain();
        stage.addActor(createTable());
    }

    protected abstract Table createTable();

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

}
