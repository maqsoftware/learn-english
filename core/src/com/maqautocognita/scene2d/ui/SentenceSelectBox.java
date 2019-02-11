package com.maqautocognita.scene2d.ui;

import com.maqautocognita.Config;
import com.maqautocognita.constant.SentenceClauseType;
import com.maqautocognita.constant.SentenceWordType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.prototype.sentence.SentenceDisplayItem;
import com.maqautocognita.scene2d.actors.AdvanceImage;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.SentenceWordTypeUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/26/2017.
 */

public class SentenceSelectBox extends AbstractSelectBox<SentenceDisplayItem> {

    private static final TextFontSizeEnum ITEM_FONT_SIZE = TextFontSizeEnum.FONT_48;
    private SentenceClauseType sentenceClauseType;
    private SentenceWordType sentenceWordType;
    private List<AdvanceImage> addFunctionButtonList;
    private List<AdvanceImage> changeOrDeleteFunctionButtonList;

    public SentenceSelectBox(int width, int height, IScrollPaneListener scrollPaneListener) {
        super(width, height, "noun space.png", scrollPaneListener);
    }

    @Override
    protected Label getItemLabel(SentenceDisplayItem item, Label.LabelStyle labelStyle) {
        Label label = new Label(item.getDisplay(), labelStyle);
        labelStyle.font.getData().setLineHeight(labelStyle.font.getLineHeight());
        label.setAlignment(Align.center);
        label.setWrap(true);
        return label;
    }

    @Override
    protected TextFontSizeEnum getItemFontSize() {
        return ITEM_FONT_SIZE;
    }

    @Override
    protected boolean isMatchToSelectedItem(SentenceDisplayItem item, SentenceDisplayItem selectedItem) {
        return item.getDisplay().equals(selectedItem.getDisplay());
    }

    public void clearAndHide() {
        super.clearAndHide();
        if (null != addFunctionButtonList) {
            addFunctionButtonList.clear();
            addFunctionButtonList = null;
        }

        if (null != changeOrDeleteFunctionButtonList) {
            changeOrDeleteFunctionButtonList.clear();
            changeOrDeleteFunctionButtonList = null;
        }
    }

    public void changeBackground() {
        background.setDrawable(new TextureRegionDrawable(new TextureRegion(
                new Texture(Config.SENTENCE_IMAGE_PATH + SentenceWordTypeUtils.getImageNameByWordType(sentenceWordType) + " space.png"))));
    }

    public List<AdvanceImage> getAddFunctionButtonList() {
        return addFunctionButtonList;
    }

    public List<AdvanceImage> getChangeOrDeleteFunctionButtonList() {
        return changeOrDeleteFunctionButtonList;
    }

    public void addChangeOrDeleteFunctionButton(AdvanceImage patternFunctionButton) {
        if (null == changeOrDeleteFunctionButtonList) {
            changeOrDeleteFunctionButtonList = new ArrayList<AdvanceImage>();
        }

        changeOrDeleteFunctionButtonList.add(patternFunctionButton);
    }

    public void addAddFunctionButton(AdvanceImage patternFunctionButton) {
        if (null == addFunctionButtonList) {
            addFunctionButtonList = new ArrayList<AdvanceImage>();
        }

        addFunctionButtonList.add(patternFunctionButton);
    }

    public float getTotalWidthOfChangeOrDeleteButton(float space) {
        float totalWidth = 0;
        if (CollectionUtils.isNotEmpty(changeOrDeleteFunctionButtonList)) {
            for (AdvanceImage changeOrDeleteButton : changeOrDeleteFunctionButtonList) {
                totalWidth += changeOrDeleteButton.getWidth() + space;
            }
        }
        if (totalWidth > 0) {
            //delete the last space of the last button
            totalWidth -= space;
        }
        return totalWidth;
    }

    public SentenceClauseType getSentenceClauseType() {
        return sentenceClauseType;
    }

    public void setSentenceClauseType(SentenceClauseType sentenceClauseType) {
        this.sentenceClauseType = sentenceClauseType;
    }

    public SentenceWordType getSentenceWordType() {
        return sentenceWordType;
    }

    public void setSentenceWordType(SentenceWordType sentenceWordType) {
        this.sentenceWordType = sentenceWordType;
    }
}
