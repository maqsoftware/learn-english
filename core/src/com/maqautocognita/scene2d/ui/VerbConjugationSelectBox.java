package com.maqautocognita.scene2d.ui;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.listener.IScrollPaneListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class VerbConjugationSelectBox extends AbstractSelectBox<String> {

    public VerbConjugationSelectBox(int width, int height,
                                    String imageName,
                                    IScrollPaneListener<String> scrollPaneListener) {
        super(width, height, imageName, scrollPaneListener);
    }

    @Override
    protected Label getItemLabel(String item, Label.LabelStyle labelStyle) {
        return new Label("-" + item, labelStyle);
    }

    @Override
    protected TextFontSizeEnum getItemFontSize() {
        return TextFontSizeEnum.FONT_54;
    }

    @Override
    protected boolean isMatchToSelectedItem(String item, String selectedItem) {
        return item.equals(selectedItem);
    }
}
