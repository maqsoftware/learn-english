package com.maqautocognita.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAction extends Action {

    private IActionListener actionListener;

    public void setActionListener(IActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public boolean act(float delta) {
        boolean complete = doAct(delta);

        if (complete && null != actionListener) {
            actionListener.onComplete();
        }

        return complete;
    }

    protected abstract boolean doAct(float delta);
}
