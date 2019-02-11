package com.maqautocognita.section.sentence;

import com.maqautocognita.screens.AbstractSentenceScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class PositiveVerbConjugationSection extends AbstractVerbConjugationSection {

    public PositiveVerbConjugationSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    protected String getVerbStemTrayImageName() {
        return "verb stem tray.png";
    }

    @Override
    protected String getPastTenseText() {
        return "li";
    }

    @Override
    protected String getPresentTenseText() {
        return "na";
    }

    @Override
    protected String getFutureTenseText() {
        return "ta";
    }

    @Override
    protected String getFirstSingleText() {
        return "ni";
    }

    @Override
    protected String getSecondSingleText() {
        return "u";
    }

    @Override
    protected String getThirdSingleText() {
        return "a";
    }

    @Override
    protected String getFirstPluralText() {
        return "tu";
    }

    @Override
    protected String getSecondPluralText() {
        return "m";
    }

    @Override
    protected String getThirdPluralText() {
        return "wa";
    }
}
