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
public class VerbRootFor2PrefixWordBlockActor extends WordBlockActor {

    protected static final IconPosition SUBJECT_ICON_POSITION = new IconPosition(11, 11, 101, 79);
    protected static final IconPosition TENSE_ICON_POSITION = new IconPosition(121, SUBJECT_ICON_POSITION.y, SUBJECT_ICON_POSITION.width, SUBJECT_ICON_POSITION.height);

    protected boolean isSubjectAdded;
    protected boolean isTenseAdded;

    public VerbRootFor2PrefixWordBlockActor(String givenWord, final AbstractAutoCognitaScreen screen) {
        super(givenWord);
        setWordBlock(new Texture(Config.SENTENCE_IMAGE_PATH + "/verb root for 2 prefixes arrange.png"), screen);
    }

    protected void configLabelText(Label label, Image wordBlock) {
        label.setX(227);
        label.setHeight(wordBlock.getHeight());
        label.setAlignment(Align.center | Align.left);
    }

    public boolean isTouchSubject(float x, float y) {
        return TouchUtils.isTouched(SUBJECT_ICON_POSITION.x + getX(), SUBJECT_ICON_POSITION.y + getY(),
                SUBJECT_ICON_POSITION.width, SUBJECT_ICON_POSITION.height, x, y
        );
    }

    public void addSubjectBlock(WordBlockActor subjectBlock) {
        subjectBlock.setPosition(SUBJECT_ICON_POSITION.x, SUBJECT_ICON_POSITION.y);
        subjectBlock.clearListeners();
        addActor(subjectBlock);
        isSubjectAdded = true;
    }

    public boolean isTouchTense(float x, float y) {
        return TouchUtils.isTouched(TENSE_ICON_POSITION.x + getX(), TENSE_ICON_POSITION.y + getY(),
                TENSE_ICON_POSITION.width, TENSE_ICON_POSITION.height, x, y
        );
    }

    public void addTenseBlock(WordBlockActor tenseBlock) {
        tenseBlock.setPosition(TENSE_ICON_POSITION.x, TENSE_ICON_POSITION.y);
        tenseBlock.clearListeners();
        addActor(tenseBlock);
        isTenseAdded = true;
    }

    public boolean isAddCompleted() {
        return isSubjectAdded && isTenseAdded;
    }

}
