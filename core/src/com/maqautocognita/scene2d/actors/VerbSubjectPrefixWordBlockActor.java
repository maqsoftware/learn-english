package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */
public class VerbSubjectPrefixWordBlockActor extends WordBlockActor {

    public VerbSubjectPrefixWordBlockActor(String givenWord, final AbstractAutoCognitaScreen screen) {
        super(givenWord);
        setWordBlock(new Texture(Config.SENTENCE_IMAGE_PATH + "/verb subject prefix arrange.png"), screen);
    }

    @Override
    public boolean isAllowDragToUnderLine() {
        return false;
    }

}
