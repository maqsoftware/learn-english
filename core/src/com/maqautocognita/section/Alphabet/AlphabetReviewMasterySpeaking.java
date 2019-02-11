package com.maqautocognita.section.Alphabet;

import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Phonic.MultipleWordSpeakingSection;
import com.maqautocognita.utils.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetReviewMasterySpeaking extends MultipleWordSpeakingSection {

    private String[] words;

    public AlphabetReviewMasterySpeaking(String moduleName, AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(moduleName, alphabetScreen, onHelpListener);
    }

    @Override
    public void reset() {
        super.reset();
        words = null;
    }

    @Override
    public String getRecognizeFileName() {
        return "alphabet.gram";
    }

    @Override
    protected String[] getWords() {
        if (null != selectedActivity && ArrayUtils.isNotEmpty(selectedActivity.getWords()) && ArrayUtils.isEmpty(words)) {
            words = selectedActivity.getWords().clone();
            Collections.shuffle(Arrays.asList(words));
        }

        return words;
    }

    @Override
    protected int getNumberOfWordToBeDisplay() {
        return 5;
    }
}
