package com.maqautocognita.scene2d.ui;

import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class VerticalPagedScrollPane<I> extends AbstractPagedScrollPane<I> {

    private int currentSelectedInformationIndex;

    public VerticalPagedScrollPane(IScrollPaneListener<I> actionListener) {
        super(actionListener);
    }

    @Override
    protected void initContent() {
        content = new Table();
        setWidget(content);
    }

    @Override
    protected void addPage(Actor page) {
        page.setSize(getWidth(), getHeight());
        content.add(page).size(getWidth(), getHeight()).align(Align.center);
        content.row();
    }

    @Override
    protected void scrollToPage() {
        final float scrollY = getScrollY();

        Array<Actor> pages = content.getChildren();
        float pageY = 0;
        float pageHeight = 0;
        if (pages.size > 0) {
            int scrollToPageIndex = -1;
            for (int i = pages.size - 1; i >= 0; i--) {
                Actor page = pages.get(i);
                pageY = page.getY();
                pageHeight = page.getHeight();
                if (scrollY < (pageY + pageHeight * 0.3)) {
                    scrollToPageIndex = pages.indexOf(page, false);
                    break;
                }
            }

            boolean isViewChanged = getCurrentPagedIndex(scrollToPageIndex) != getCurrentPagedIndex();

            currentSelectedInformationIndex = scrollToPageIndex;

            //pageY = pages.get(scrollToPageIndex).getY();

            Gdx.app.log(getClass().getName(), "scroll Y to " + scrollY + " page y = " + pageY +
                    ", isViewChanged?" + isViewChanged +
                    ", scrollToPageIndex = " + getCurrentPagedIndex());
            setScrollY(pageY);

            if (isViewChanged) {
                triggerListener();
            }
        }
    }

    private int getCurrentPagedIndex(int scrolledPageIndex) {
        return informationList.size() - scrolledPageIndex - 1;
    }

    private int getCurrentPagedIndex() {
        return getCurrentPagedIndex(currentSelectedInformationIndex);
    }

    private void triggerListener() {
        if (null != actionListener) {
            if (CollectionUtils.isNotEmpty(informationList) && informationList.size() > getCurrentPagedIndex()) {
                actionListener.onPaneChanged(informationList.get(getCurrentPagedIndex()), getCurrentPagedIndex());
            }
        }
    }

    public I getSelectedItem() {
        if (CollectionUtils.isNotEmpty(informationList)) {
            int currentPagedIndex = getCurrentPagedIndex();
            if (currentPagedIndex >= 0 && currentPagedIndex < informationList.size()) {
                return informationList.get(getCurrentPagedIndex());
            }
        }
        return null;
    }

    public void setSelectedPage(int pageIndex) {
        layout();
        setScrollY(pageIndex * getHeight());
        currentSelectedInformationIndex = getInformationList().size() - pageIndex - 1;
        triggerListener();
    }

}
