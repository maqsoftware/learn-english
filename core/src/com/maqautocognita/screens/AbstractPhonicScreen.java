package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.utils.SectionUtils;

/**
 * Created by sc.chi on 2/6/16.
 */
public abstract class AbstractPhonicScreen extends AbstractLetterScreen {

    public AbstractPhonicScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    protected boolean isFillInTheBlank() {
        return (isNormalActivitySelected() && SectionUtils.isFillInTheBlanks(selectedActivity.getActivityCode())) ||
                (isReviewSelected() && SectionUtils.isFillInTheBlanks(selectedReview.getActivityCode()));
    }

    protected boolean isReadAndListen() {
        return isNormalActivitySelected() && SectionUtils.isReadingAndListening(selectedActivity.getActivityCode());
    }

    protected boolean isSingleLetterSpeaking() {
        return (isNormalActivitySelected() && SectionUtils.isSpeaking(selectedActivity.getActivityCode()));
    }

    protected boolean isGrouping() {
        return (isNormalActivitySelected() && SectionUtils.isGrouping(selectedActivity.getActivityCode())) ||
                (isReviewSelected() && SectionUtils.isGrouping(selectedReview.getActivityCode()));
    }

    protected boolean isWordBlend() {
        return (isNormalActivitySelected() && SectionUtils.isWordBlend(selectedActivity.getActivityCode()))
                ||
                (isReviewSelected() && SectionUtils.isWordBlend(selectedReview.getActivityCode()))
                ||
                (isMasteryTestSelected() && SectionUtils.isWordBlend(selectedMasteryTest.getActivityCode()));
    }

    protected boolean isListenAndType() {
        return (isNormalActivitySelected() && SectionUtils.isListenAndType(selectedActivity.getActivityCode())) ||
                (isReviewSelected() && SectionUtils.isListenAndType(selectedReview.getActivityCode()))
                ||
                (isMasteryTestSelected() && SectionUtils.isListenAndType(selectedMasteryTest.getActivityCode()));
    }

    protected boolean isReviewSpeaking() {
        return (isReviewSelected() && SectionUtils.isSpeaking(selectedReview.getActivityCode()))
                || (isMasteryTestSelected() && SectionUtils.isSpeaking(selectedMasteryTest.getActivityCode()));
    }

}
