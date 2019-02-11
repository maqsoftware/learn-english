
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.Alphabet.AlphabetReadListenSection;
import com.maqautocognita.section.Alphabet.AlphabetReviewMasteryListeningSection;
import com.maqautocognita.section.Alphabet.AlphabetReviewMasteryReadingSection;
import com.maqautocognita.section.Alphabet.AlphabetReviewMasterySpeaking;
import com.maqautocognita.section.Alphabet.AlphabetReviewMasteryWritingSection;
import com.maqautocognita.section.Alphabet.AlphabetWriteSection;
import com.maqautocognita.section.SingleLetterSpeakingSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.utils.SectionUtils;

import java.util.List;

public class AlphabetScreen extends AbstractLetterScreen {

    private AlphabetReadListenSection alphabetReadListenWordSection;
    private AlphabetWriteSection alphabetWriteSection;
    private AlphabetReviewMasterySpeaking alphabetReviewSpeakingSection;
    private AlphabetReviewMasteryReadingSection alphabetReviewReadingSection;
    private AlphabetReviewMasteryListeningSection alphabetReviewListeningSection;
    private AlphabetReviewMasteryWritingSection alphabetReviewWritingSection;
    private SingleLetterSpeakingSection singleLetterSpeakingSection;


    public AlphabetScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);

        alphabetReadListenWordSection = new AlphabetReadListenSection(this, this);
        alphabetReviewSpeakingSection = new AlphabetReviewMasterySpeaking("alphabet", this, this);
        alphabetReviewReadingSection = new AlphabetReviewMasteryReadingSection(this);
        alphabetReviewListeningSection = new AlphabetReviewMasteryListeningSection(this);
        singleLetterSpeakingSection = new SingleLetterSpeakingSection(this, this);
        alphabetWriteSection = new AlphabetWriteSection(this);
        alphabetReviewWritingSection = new AlphabetReviewMasteryWritingSection(this);

        autoCognitaSectionList.add(alphabetReadListenWordSection);
        autoCognitaSectionList.add(singleLetterSpeakingSection);
        autoCognitaSectionList.add(alphabetWriteSection);
        autoCognitaSectionList.add(alphabetReviewSpeakingSection);
        autoCognitaSectionList.add(alphabetReviewReadingSection);
        autoCognitaSectionList.add(alphabetReviewListeningSection);
        autoCognitaSectionList.add(alphabetReviewWritingSection);
    }

    @Override
    protected void initAllLesson() {
        AlphabetLessonService.getInstance().initAllLesson();
    }

    @Override
    protected boolean isRequiredToShowBothCaseLetter() {
        return true;
    }

    @Override
    protected List<LessonWithReview> getAllLessonList() {
        return AlphabetLessonService.getInstance().getLessonList();
    }

    @Override
    protected LessonWithReview getAllMasteryTest() {
        return AlphabetLessonService.getInstance().getMasteryTestList();
    }

    @Override
    public void doRender() {

        super.doRender();

        //if the menu item writing is selected
        alphabetWriteSection.render(isNormalActivitySelected() && SectionUtils.isWriting(selectedActivity.getActivityCode()));

        singleLetterSpeakingSection.render(isNormalActivitySelected() && SectionUtils.isSpeaking(selectedActivity.getActivityCode()));

        alphabetReadListenWordSection.render(isNormalActivitySelected() && SectionUtils.isReadingAndListening(selectedActivity.getActivityCode()));

        alphabetReviewReadingSection.render((isMasteryTestSelected() && SectionUtils.isReading(selectedMasteryTest.getActivityCode()) ||
                (isReviewSelected() && SectionUtils.isReading(selectedReview.getActivityCode()))));

        alphabetReviewListeningSection.render((isMasteryTestSelected() && SectionUtils.isListenAndType(selectedMasteryTest.getActivityCode()) ||
                (isReviewSelected() && SectionUtils.isListenAndType(selectedReview.getActivityCode()))));

        alphabetReviewSpeakingSection.render((isMasteryTestSelected() && SectionUtils.isSpeaking(selectedMasteryTest.getActivityCode()) ||
                (isReviewSelected() && SectionUtils.isSpeaking(selectedReview.getActivityCode()))));

        alphabetReviewWritingSection.render((isMasteryTestSelected() && SectionUtils.isWriting(selectedMasteryTest.getActivityCode()) ||
                (isReviewSelected() && SectionUtils.isWriting(selectedReview.getActivityCode()))));

    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.ALPHABET;
    }

    @Override
    public AbstractLessonService getLessonService() {
        return AlphabetLessonService.getInstance();
    }

}
