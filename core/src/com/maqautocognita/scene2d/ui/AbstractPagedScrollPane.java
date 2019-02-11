package com.maqautocognita.scene2d.ui;

import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractPagedScrollPane<I> extends ScrollPane {

    protected Table content;
    protected IScrollPaneListener<I> actionListener;
    protected List<I> informationList;
    protected List<Actor> actorList;
    private boolean wasPanDragFling = false;

    public AbstractPagedScrollPane(IScrollPaneListener<I> actionListener) {
        super(null);
        initContent();
        this.actionListener = actionListener;
    }

    protected void initContent() {
        content = new Table();
        setWidget(content);
        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(Color.WHITE);
        pm1.fill();
        content.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(pm1))));

    }

    public void clearChildren() {
        content.clearChildren();
        if (null != informationList) {
            informationList.clear();
            informationList = null;
        }
    }

    public void addPage(Actor page, I information) {
        addPage(page);

        if (null == actorList) {
            actorList = new ArrayList<Actor>();
        }

        actorList.add(page);

        if (null == informationList) {
            informationList = new ArrayList<I>();
        }
        informationList.add(information);
    }

    protected abstract void addPage(Actor page);

    @Override
    public void act(float delta) {
        super.act(delta);
        if (wasPanDragFling && !isPanning() && !isDragging() && !isFlinging()) {
            wasPanDragFling = false;
        } else if (isPanning() || isDragging() || isFlinging()) {
            wasPanDragFling = true;
        }
    }

    protected abstract void scrollToPage();

    @Override
    public boolean remove() {
        dispose();
        return super.remove();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (content != null) {
            for (Cell cell : content.getCells()) {
                cell.width(width);
            }
            content.invalidate();
        }
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        if (content != null) {
            for (Cell cell : content.getCells()) {
                cell.height(height);
            }
            content.invalidate();
        }
    }

    private void dispose() {
        if (CollectionUtils.isNotEmpty(actorList)) {
            for (Actor actor : actorList) {
                actor.remove();
                actor = null;
            }
            actorList.clear();

        }
    }

    public List<I> getInformationList() {
        return informationList;
    }

}
