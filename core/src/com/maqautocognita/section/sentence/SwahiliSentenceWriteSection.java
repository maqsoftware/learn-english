package com.maqautocognita.section.sentence;

import com.maqautocognita.bo.SwahiliSentence;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.StringUtils;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public class SwahiliSentenceWriteSection extends SentenceWriteSection<SwahiliSentence> {

    public SwahiliSentenceWriteSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    protected String getLabelText() {
        String labelText = super.getLabelText();
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.pronoun;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.nounPrefix;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.nounRoot;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.verbSubjectPrefix;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.verbTensePrefix;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.verbObjectPrefix;
        }
        if (StringUtils.isBlank(labelText)) {
            labelText = sentence.verbRoot;
        }
        return labelText;
    }
}
