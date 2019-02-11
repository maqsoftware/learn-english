package com.maqautocognita.scene2d.actors;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TextPropertiesUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class ProgressActor extends WidgetGroup {

    protected static final int PROGRESS_ICON_HEIGHT = 50;
    protected static final int PROGRESS_ICON_WIDTH = 30;
    protected static final IconPosition DISABLE_PROGRESS_ICON = new IconPosition(0, 1000, PROGRESS_ICON_WIDTH, PROGRESS_ICON_HEIGHT);
    protected static final int MOBILE_MENU_TEXT_WIDTH = 250;
    protected static final int MENU_TEXT_WIDTH = 350;
    protected static final int ICON_WIDTH = 50;
    protected static final int ICON_HEIGHT = 50;
    protected static final int ROW_SPACE = 35;
    protected static final int SPACE_BETWEEN_ICON_AND_PROGRESS = 10;
    protected static final TextFontSizeEnum SUBTITLE_FONT_SIZE = TextFontSizeEnum.FONT_48;
    protected static final TextFontSizeEnum SMALL_TITLE_FONT_SIZE = TextFontSizeEnum.FONT_36;
    protected static final int ACTIVITY_ICON_WIDTH = 100;
    protected static final IconPosition READING_PROGRESS_ICON_1 = new IconPosition(150, DISABLE_PROGRESS_ICON.y, PROGRESS_ICON_WIDTH, PROGRESS_ICON_HEIGHT);
    protected static final IconPosition READING_PROGRESS_ICON_2 = new IconPosition(180, DISABLE_PROGRESS_ICON.y, PROGRESS_ICON_WIDTH, PROGRESS_ICON_HEIGHT);
    protected static final IconPosition READING_PROGRESS_ICON_3 = new IconPosition(210, DISABLE_PROGRESS_ICON.y, PROGRESS_ICON_WIDTH, PROGRESS_ICON_HEIGHT);
    protected static final IconPosition ACTIVITY_DISABLE_PROGRESS_ICON = new IconPosition(10, DISABLE_PROGRESS_ICON.y, PROGRESS_ICON_WIDTH / 2, PROGRESS_ICON_HEIGHT);
    protected static final IconPosition ACTIVITY_PROGRESS_ICON = new IconPosition(225, DISABLE_PROGRESS_ICON.y, PROGRESS_ICON_WIDTH / 2, PROGRESS_ICON_HEIGHT);
    protected static final String DUMMY_TEXT = "A";
    protected final Vector2 largeTitleScreenPosition;
    protected List<ImageActor> progressIconList;
    protected CustomCamera camera;
    private TextCell titleTextCell;
    private TextCell topicsTextCell;
    private TextCell activitiesTextCell;

    public ProgressActor(
            int topMenuMarginTop, int arrowIconSize) {
        largeTitleScreenPosition = new Vector2(arrowIconSize,
                ScreenUtils.getScreenHeight() - topMenuMarginTop - 20);
        setSize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        drawProgressTopicTextAndIcon();
    }

    protected void drawProgressTopicTextAndIcon() {

        if (null == titleTextCell) {
            titleTextCell = new TextCell(DUMMY_TEXT,
                    TextFontSizeEnum.FONT_72, getWidth(),
                    largeTitleScreenPosition.x, largeTitleScreenPosition.y, Align.left);
            addActor(titleTextCell);
        }

        if (null == topicsTextCell) {
            topicsTextCell = createMenuText(DUMMY_TEXT,
                    ScreenUtils.isLandscapeMode ? 0 : getProgressBarStartXPosition(),
                    titleTextCell.getY() - titleTextCell.getHeight() - 60, SUBTITLE_FONT_SIZE, Align.left);
            addActor(topicsTextCell);
        }

        TextCell lastTextCell = drawTopicProgress(topicsTextCell);

        if (null == activitiesTextCell) {
            activitiesTextCell = createMenuText(DUMMY_TEXT,
                    ScreenUtils.isLandscapeMode ? 0 : getProgressBarStartXPosition(),
                    lastTextCell.getY() - lastTextCell.getHeight() - 110, SUBTITLE_FONT_SIZE, Align.left);
            addActor(activitiesTextCell);
        }

        drawActivityProgress(activitiesTextCell);

        switchLanguage();
    }

    protected TextCell createMenuText(String text, float xPosition, float yPosition, TextFontSizeEnum textFontSizeEnum, int alignment) {
        return new TextCell(null, text, textFontSizeEnum,
                getMenuTextWidth(), xPosition + 10, yPosition,
                alignment);
    }

    protected float getProgressBarStartXPosition() {
        return getMenuTextWidth() + ACTIVITY_ICON_WIDTH + SPACE_BETWEEN_ICON_AND_PROGRESS;
    }

    protected abstract TextCell drawTopicProgress(TextCell topicsTextCell);

    protected abstract void drawActivityProgress(TextCell activitiesTextCell);

    public void switchLanguage() {
        drawProgress();
        topicsTextCell.setText(TextPropertiesUtils.getTitleTopic());
        activitiesTextCell.setText(TextPropertiesUtils.getTitleActivities());
        titleTextCell.setText(getTitleText());
        changeLanguage();
    }

    protected float getMenuTextWidth() {
        return ScreenUtils.isLandscapeMode ? MENU_TEXT_WIDTH : MOBILE_MENU_TEXT_WIDTH;
    }

    protected abstract void drawProgress();

    protected abstract String getTitleText();

    protected abstract void changeLanguage();

    @Override
    public boolean remove() {
        titleTextCell = null;
        topicsTextCell = null;
        activitiesTextCell = null;
        return super.remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isInsideCamera()) {
            super.draw(batch, parentAlpha);
        }
    }

    protected boolean isInsideCamera() {
        if (null == getCamera()) {
            return false;
        }
        return getCamera().isObjectInsideCamera(getX(), getWidth());
    }

    protected CustomCamera getCamera() {
        if (null == camera && getStage().getCamera() instanceof CustomCamera) {
            camera = (CustomCamera) getStage().getCamera();
        }

        return camera;
    }

    protected TextCell createMenuText(String text, TextCell upperTextCell) {
        return createMenuText(text, 0, upperTextCell.getY() - upperTextCell.getHeight() - ROW_SPACE, SMALL_TITLE_FONT_SIZE);
    }

    protected TextCell createMenuText(String text, float xPosition, float yPosition, TextFontSizeEnum textFontSizeEnum) {
        return createMenuText(text, xPosition, yPosition, textFontSizeEnum, Align.right);
    }

    protected TextCell createMenuTextForMultipleRow(String text, TextCell upperTextCell) {
        return createMenuText(text, 0, upperTextCell.getY() - upperTextCell.getHeight() - PROGRESS_ICON_HEIGHT * 2, SMALL_TITLE_FONT_SIZE);
    }

    protected TextCell createMenuTextInRightSide(String text, TextCell upperTextCell) {
        return createMenuText(text, ScreenUtils.getScreenWidth() / 2, upperTextCell.getY() - upperTextCell.getHeight() - ROW_SPACE, SMALL_TITLE_FONT_SIZE);
    }
}
