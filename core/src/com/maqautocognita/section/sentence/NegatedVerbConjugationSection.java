package com.maqautocognita.section.sentence;

import com.maqautocognita.screens.AbstractSentenceScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class NegatedVerbConjugationSection extends AbstractVerbConjugationSection {

    public NegatedVerbConjugationSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    protected String getVerbStemTrayImageName() {
        return "verb stem negation tray.png";
    }

    @Override
    protected String getPastTenseText() {
        return "ku";
    }

    @Override
    protected String getPresentTenseText() {
        return "";
    }

    @Override
    protected String getFutureTenseText() {
        return "ta";
    }

    @Override
    protected String getFirstSingleText() {
        return "si";
    }

    @Override
    protected String getSecondSingleText() {
        return "hu";
    }

    @Override
    protected String getThirdSingleText() {
        return "ha";
    }

    @Override
    protected String getFirstPluralText() {
        return "hatu";
    }

    @Override
    protected String getSecondPluralText() {
        return "ham";
    }

    @Override
    protected String getThirdPluralText() {
        return "hawa";
    }
}
