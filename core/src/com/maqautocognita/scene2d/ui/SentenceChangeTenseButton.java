package com.maqautocognita.scene2d.ui;

import com.maqautocognita.prototype.sentence.SentenceModule;
import com.maqautocognita.prototype.sentence.SentenceTenseEnum;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by siu-chun.chi on 5/29/2017.
 */

public class SentenceChangeTenseButton extends AbstractSentenceChangeButton {


    private final SentenceTenseEnum sentenceTenseEnum;

    public SentenceChangeTenseButton(SentenceModule sentenceModule, SentenceTenseEnum sentenceTenseEnum,
                                     Texture texture, Texture selectedTexture) {
        super(sentenceModule, texture, selectedTexture);
        this.sentenceTenseEnum = sentenceTenseEnum;
    }

    public SentenceTenseEnum getSentenceTenseEnum() {
        return sentenceTenseEnum;
    }

    @Override
    protected void onClick() {
        if (null != sentenceTenseEnum) {
            sentenceModule.changeTense(sentenceTenseEnum);
        }
    }

}
