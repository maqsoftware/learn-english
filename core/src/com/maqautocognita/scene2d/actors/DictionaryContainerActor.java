package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class DictionaryContainerActor extends WidgetGroup {

    private final ScrollPane dictionaryActorScroller;
    private ShapeRenderer background;
    private Image downArrowImage;
    private DictionaryActor dictionaryActor;

    public DictionaryContainerActor(float width, float height, TextFontSizeEnum textFontSizeEnum, TextFontSizeEnum wordTextFontSizeEnum, final IActionListener actionListener) {

        downArrowImage = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "down_arrow.png"));

        float startYPosition = height - downArrowImage.getHeight();

        downArrowImage.setPosition(ScreenUtils.getXPositionForCenterObject(downArrowImage.getWidth(), width), startYPosition);

        downArrowImage.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                actionListener.onComplete();

            }
        });

        addActor(downArrowImage);

        dictionaryActor = new DictionaryActor(width, textFontSizeEnum, wordTextFontSizeEnum);
        dictionaryActor.setHeight(height);

        dictionaryActorScroller = new ScrollPane(dictionaryActor);
        dictionaryActorScroller.setScrollingDisabled(true, false);
        dictionaryActorScroller.setSize(width, height - downArrowImage.getHeight());

        addActor(dictionaryActorScroller);

        setSize(width, height);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (null == background) {
            background = new ShapeRenderer();
        }
        batch.end();
        background.setProjectionMatrix(batch.getProjectionMatrix());
        background.begin(ShapeRenderer.ShapeType.Filled);
        background.rect(getParent().getX(), getY(), getWidth(), getHeight());
        background.setColor(ColorProperties.LIFE_SKILL_BOTTOM_BACKGROUND);
        background.end();

        batch.begin();

        super.draw(batch, parentAlpha);
    }

    public void setWord(String word) {
        dictionaryActorScroller.setScrollY(0);
        dictionaryActor.setWord(word);
    }


}
