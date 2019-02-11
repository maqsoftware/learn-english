package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */
public class VerbRootFor3PrefixWordBlockActor extends VerbRootFor2PrefixWordBlockActor {

    private static final IconPosition OBJECT_ICON_POSITION =
            new IconPosition(231, SUBJECT_ICON_POSITION.y, SUBJECT_ICON_POSITION.width, SUBJECT_ICON_POSITION.height);

    public VerbRootFor3PrefixWordBlockActor(String givenWord, final AbstractAutoCognitaScreen screen) {
        super(givenWord, screen);
        setWordBlock(new Texture(Config.SENTENCE_IMAGE_PATH + "/verb root for 3 prefixes arrange.png"), screen);
    }

    protected void configLabelText(Label label, Image wordBlock) {
        label.setX(338);
        label.setHeight(wordBlock.getHeight());
        label.setAlignment(Align.center | Align.left);
    }

    public boolean isTouchObject(float x, float y) {
        return TouchUtils.isTouched(OBJECT_ICON_POSITION.x + getX(), OBJECT_ICON_POSITION.y + getY(),
                OBJECT_ICON_POSITION.width, OBJECT_ICON_POSITION.height, x, y
        );
    }

    public void addObjectBlock(WordBlockActor objectBlock) {
        objectBlock.setPosition(OBJECT_ICON_POSITION.x, OBJECT_ICON_POSITION.y);
        objectBlock.clearListeners();
        addActor(objectBlock);
        isSubjectAdded = true;
    }


}
