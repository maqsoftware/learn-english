package com.maqautocognita.scene2d.ui;

import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class PagedScrollPane<I> extends AbstractPagedScrollPane<I> {

    private int currentPageIndex;

    public PagedScrollPane(IScrollPaneListener scrollPaneListener) {
        super(scrollPaneListener);
    }

    @Override
    protected void addPage(Actor page) {
        content.add(page).align(Align.top).size(page.getWidth(), page.getHeight());
    }

    @Override
    protected void scrollToPage() {
        final float width = getWidth();
        final float maxX = getMaxX();
        float scrollX = getScrollX();

        Array<Actor> pages = content.getChildren();
        float pageX = 0;
        float pageWidth = 0;
        if (pages.size > 0) {
            int scrollToPageIndex = -1;
            for (Actor page : pages) {
                pageX = page.getX();
                pageWidth = page.getWidth();
                if (scrollX < (pageX + pageWidth * 0.3)) {
                    scrollToPageIndex = pages.indexOf(page, false);
                    break;
                }
            }

            Gdx.app.log(getClass().getName(), "scrollToPageIndex = " + scrollToPageIndex + ",currentPageIndex=" + currentPageIndex);

            boolean isViewChanged = scrollToPageIndex != currentPageIndex;

            if (scrollToPageIndex > currentPageIndex) {
                currentPageIndex++;
            } else if (scrollToPageIndex < currentPageIndex) {
                currentPageIndex--;
            }

            pageX = pages.get(currentPageIndex).getX();
            scrollX = MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX);
            Gdx.app.log(getClass().getName(), "scroll X to " + scrollX + ", isViewChanged?" + isViewChanged);
            setScrollX(scrollX);
            if (isViewChanged) {
                triggerActionListener(currentPageIndex);
            }
        }
    }

    private void triggerActionListener(int index) {
        if (null != actionListener) {
            if (CollectionUtils.isNotEmpty(informationList) && -1 != index && informationList.size() > index) {
                setScrollY(0);
                actionListener.onPaneChanged(informationList.get(index), index);
            }
        }
    }

    public void scrollToPage(int pageIndex) {
        scrollToPageWithoutTriggerListener(pageIndex);
        triggerActionListener(pageIndex);
    }

    public void scrollToPageWithoutTriggerListener(int pageIndex) {
        Gdx.app.log(getClass().getName(), "switch to page = " + pageIndex);
        currentPageIndex = pageIndex;
        Actor page = content.getChildren().get(pageIndex);
        final float width = getWidth();
        final float maxX = getMaxX();
        final float pageX = page.getX();
        final float pageWidth = page.getWidth();

        setScrollX(MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX));
    }

}
