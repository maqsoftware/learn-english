package com.maqautocognita.scene2d.ui;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.List;

public abstract class AbstractSelectBox<T> extends Group {

    protected final Image background;
    private final VerticalPagedScrollPane<T> verticalPagedScrollPane;

    public AbstractSelectBox(int width, int height, String backgroundImageName, IScrollPaneListener<T> scrollPaneListener) {
        background = new Image(new Texture(Config.SENTENCE_IMAGE_PATH + backgroundImageName));
        addActor(background);

        verticalPagedScrollPane = new VerticalPagedScrollPane(scrollPaneListener);
        verticalPagedScrollPane.setSize(width, height);

        addActor(verticalPagedScrollPane);

        setSize(width, height);
    }

    public void setItems(List<T> itemList) {
        verticalPagedScrollPane.clearChildren();
        if (CollectionUtils.isNotEmpty(itemList)) {
            for (T item : itemList) {
                Label label = getItemLabel(item, getItemStyle());
                verticalPagedScrollPane.addPage(label, item);
            }
        }
    }

    protected abstract Label getItemLabel(T item, Label.LabelStyle labelStyle);

    private Label.LabelStyle getItemStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(getItemFontSize());
        labelStyle.fontColor = ColorProperties.TEXT;

        return labelStyle;
    }

    protected abstract TextFontSizeEnum getItemFontSize();

    public T getSelectedItem() {
        return verticalPagedScrollPane.getSelectedItem();
    }

    public void setSelectedItem(T selectedItem) {

        if (CollectionUtils.isNotEmpty(verticalPagedScrollPane.getInformationList())) {

            for (int i = 0; i < verticalPagedScrollPane.getInformationList().size(); i++) {
                if (isMatchToSelectedItem(verticalPagedScrollPane.getInformationList().get(i), selectedItem)
                        ) {
                    verticalPagedScrollPane.setSelectedPage(i);
                    break;
                }
            }
        }
    }

    protected abstract boolean isMatchToSelectedItem(T item, T selectedItem);

    public void clearAndHide() {
        setVisible(false);
        verticalPagedScrollPane.clearChildren();
    }
}
