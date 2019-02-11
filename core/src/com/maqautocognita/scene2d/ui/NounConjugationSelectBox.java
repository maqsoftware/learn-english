package com.maqautocognita.scene2d.ui;

import com.maqautocognita.bo.SentenceNounConjugation;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.listener.IScrollPaneListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class NounConjugationSelectBox extends AbstractSelectBox<SentenceNounConjugation> {

    public NounConjugationSelectBox(int width, int height,
                                    String imageName,
                                    IScrollPaneListener<SentenceNounConjugation> scrollPaneListener) {
        super(width, height, imageName, scrollPaneListener);
    }

    @Override
    protected Label getItemLabel(SentenceNounConjugation item, Label.LabelStyle labelStyle) {
        return new Label("-" + item.root, labelStyle);
    }

    @Override
    protected TextFontSizeEnum getItemFontSize() {
        return TextFontSizeEnum.FONT_54;
    }

    @Override
    protected boolean isMatchToSelectedItem(SentenceNounConjugation item, SentenceNounConjugation selectedItem) {
        return item.equals(selectedItem);
    }
}
