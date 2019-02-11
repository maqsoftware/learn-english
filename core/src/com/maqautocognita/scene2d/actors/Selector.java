package com.maqautocognita.scene2d.actors;

import com.maqautocognita.scene2d.actions.IOptionSelectListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Selector<T extends ImageActor> extends AbstractCameraActor {

    private final float optionImageHeight;
    private final float optionImagePadding;
    private final Table scrollTable = new Table();
    private SelectorTable selectorTable;
    private ImageActor border;
    private IOptionSelectListener<T> optionSelectListener;

    public Selector(ImageActor border, List<T> optionList) {
        this(border, optionList, true);
    }

    public Selector(ImageActor border, List<T> optionList, boolean isPaddingRequired) {

        if (isPaddingRequired) {
            optionImageHeight = border.getHeight() * 0.8f;

            optionImagePadding = border.getHeight() * 0.2f / 2;
        } else {
            optionImageHeight = border.getHeight();

            optionImagePadding = 0;
        }

        reloadOptionList(optionList);

        final ScrollPane scroller = new ScrollPane(scrollTable);
        scroller.setScrollingDisabled(false, true);

        selectorTable = new SelectorTable();
        selectorTable.add(scroller).fill().expand();
        selectorTable.setSize(border.getWidth(), border.getHeight());
        selectorTable.setPosition(border.getX(), border.getY());

        border.setIsRequiredToCheckCameraBeforeDraw(false);
        this.border = border;

        setPosition(border.getX(), border.getY());
        setSize(border.getWidth(), border.getHeight());

    }

    public void reloadOptionList(List<T> optionList) {

        if (CollectionUtils.isNotEmpty(optionList)) {

            if (scrollTable.hasChildren()) {
                //dispose all previous children
                for (Actor actor : scrollTable.getChildren().items) {
                    if (null != actor) {
                        actor.clear();
                        actor.remove();
                        if (actor instanceof IStoryModeActor) {
                            ((IStoryModeActor) actor).dispose();
                        }
                    }
                }
            }

            scrollTable.clearChildren();

            for (final T option : optionList) {

                float ratio = 1;
                if (option.getHeight() > optionImageHeight) {
                    ratio = Math.min(optionImageHeight, option.getHeight()) / Math.max(optionImageHeight, option.getHeight());
                }

                option.setSize(option.getWidth() * ratio, option.getHeight() * ratio);
                option.setIsRequiredToCheckCameraBeforeDraw(false);
                scrollTable.add(option).pad(optionImagePadding);

                option.addListener(new ActorGestureListener() {

                    public void tap(InputEvent event, float x, float y, int count, int button) {
                        if (null != optionSelectListener) {
                            optionSelectListener.onTap(option);
                        }
                    }

                });
            }
        }
    }

    public void setOptionSelectListener(IOptionSelectListener<T> optionSelectListener) {
        this.optionSelectListener = optionSelectListener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isInsideCamera()) {
            border.draw(batch, parentAlpha);
        }
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (null != stage && null == selectorTable.getStage()) {
            stage.addActor(selectorTable);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        selectorTable.setVisible(visible);
    }

    @Override
    public void dispose() {
        if (null != border) {
            border.dispose();
        }
    }


    /**
     * This is mainly used to render the table only if it is inside the camera
     */
    private class SelectorTable extends Table {

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (isInsideCamera()) {
                super.draw(batch, parentAlpha);
            }
        }
    }
}
