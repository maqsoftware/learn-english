package com.maqautocognita.scene2d.ui;

import com.maqautocognita.prototype.sentence.SentenceModule;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by siu-chun.chi on 5/29/2017.
 */

public class SentenceSinglePluralButton extends AbstractSentenceChangeButton {

    private final String clauseType;

    private final String count;

    public SentenceSinglePluralButton(
            String clauseType, String count,
            SentenceModule sentenceModule,
            Texture texture, Texture selectedTexture) {
        super(sentenceModule, texture, selectedTexture);
        this.clauseType = clauseType;
        this.count = count;
    }

    public String getClauseType() {
        return clauseType;
    }

    public String getCount() {
        return count;
    }

    @Override
    protected void onClick() {
        sentenceModule.changeSinglePlural(clauseType, count);
    }

}
