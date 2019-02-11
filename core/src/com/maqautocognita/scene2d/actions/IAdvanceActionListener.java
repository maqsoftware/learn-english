package com.maqautocognita.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Listen the process of {@link Action}
 *
 * @author sc.chi csc19840914@gmail.com
 */
public interface IAdvanceActionListener<I> {

    void onComplete(I information);
}
