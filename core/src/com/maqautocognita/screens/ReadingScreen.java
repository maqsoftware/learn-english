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
import com.maqautocognita.section.Phonic.MultipleWordSpeakingSection;
import com.maqautocognita.section.Phonic.PhonicFillInTheBlankSection;
import com.maqautocognita.section.Phonic.PhonicGroupingSection;
import com.maqautocognita.section.Phonic.PhonicListenAndTypeSection;
import com.maqautocognita.section.Phonic.PhonicReadListenSection;
import com.maqautocognita.section.Phonic.PhonicSoundSpeakingSection;
import com.maqautocognita.section.Phonic.PhonicSwahiliListenAndTypeSection;
import com.maqautocognita.section.Phonic.PhonicSwahiliWordBlendSection;
import com.maqautocognita.section.Phonic.PhonicWordBlendSection;
import com.maqautocognita.section.SingleLetterSpeakingSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.service.PhonicLessonService;
import com.maqautocognita.service.PhonicU2LessonService;
import com.maqautocognita.service.PhonicU3LessonService;
import com.maqautocognita.service.PhonicU4LessonService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.SectionUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ReadingScreen extends AbstractPhonicScreen {

    private PhonicReadListenSection phonicReadListenSection;
    private PhonicFillInTheBlankSection phonicFillInTheBlankSection;
    private PhonicGroupingSection phonicGroupingSection;
    private PhonicListenAndTypeSection phonicListenAndTypeSection;
    private PhonicWordBlendSection phonicWordBlendSection;
    private MultipleWordSpeakingSection multipleWordSpeakingSection;
    private PhonicSoundSpeakingSection phonicSoundSpeakingSection;

    private PhonicSwahiliWordBlendSection phonicSwahiliWordBlendSection;
    private PhonicSwahiliListenAndTypeSection phonicSwahiliListenAndTypeSection;

    private AlphabetReadListenSection alphabetReadListenWordSection;
    private AlphabetWriteSection alphabetWriteSection;
    private AlphabetReviewMasterySpeaking alphabetReviewSpeakingSection;
    private AlphabetReviewMasteryReadingSection alphabetReviewReadingSection;
    private AlphabetReviewMasteryListeningSection alphabetReviewListeningSection;
    private AlphabetReviewMasteryWritingSection alphabetReviewWritingSection;
    private SingleLetterSpeakingSection singleLetterSpeakingSection;

    public ReadingScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener, ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD,
                ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD_DISABLED : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_DISABLED,
                ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD_HIGHLIGHTED : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_HIGHLIGHTED,
                AssetManagerUtils.PHONICS_SYMBOL, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_DISABLED, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_HIGHLIGHTED);

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

        phonicReadListenSection = new PhonicReadListenSection(this, this);
        phonicFillInTheBlankSection = new PhonicFillInTheBlankSection(this, this);
        phonicGroupingSection = new PhonicGroupingSection(this, this);
        phonicWordBlendSection = new PhonicWordBlendSection(this, this);
        multipleWordSpeakingSection = new MultipleWordSpeakingSection("phonic", this, this);
        phonicListenAndTypeSection = new PhonicListenAndTypeSection(this, this);
        phonicSoundSpeakingSection = new PhonicSoundSpeakingSection(this, this);

        phonicSwahiliWordBlendSection = new PhonicSwahiliWordBlendSection(this, this);
        phonicSwahiliListenAndTypeSection = new PhonicSwahiliListenAndTypeSection(this, this);

        autoCognitaSectionList.add(phonicSoundSpeakingSection);
        autoCognitaSectionList.add(phonicListenAndTypeSection);
        autoCognitaSectionList.add(phonicReadListenSection);
        autoCognitaSectionList.add(phonicFillInTheBlankSection);
        autoCognitaSectionList.add(phonicGroupingSection);
        autoCognitaSectionList.add(phonicWordBlendSection);
        autoCognitaSectionList.add(multipleWordSpeakingSection);
        autoCognitaSectionList.add(phonicSwahiliListenAndTypeSection);
        autoCognitaSectionList.add(phonicSwahiliWordBlendSection);
    }

    @Override
    protected void initAllLesson() {
        AlphabetLessonService.getInstance().initAllLesson();
        PhonicLessonService.getInstance().initAllLesson();
        PhonicU2LessonService.getInstance().initAllLesson();
        PhonicU3LessonService.getInstance().initAllLesson();
        PhonicU4LessonService.getInstance().initAllLesson();
    }

    @Override
    protected List<LessonWithReview> getAllLessonWithReviewMasterList() {
        List<LessonWithReview> lessonWithReviewList = AlphabetLessonService.getInstance().getLessonList();
        lessonWithReviewList.add(AlphabetLessonService.getInstance().getMasteryTestList());
        lessonWithReviewList.addAll(PhonicLessonService.getInstance().getLessonList());
        lessonWithReviewList.add(PhonicLessonService.getInstance().getMasteryTestList());

        lessonWithReviewList.addAll(PhonicU2LessonService.getInstance().getLessonList());
        lessonWithReviewList.add(PhonicU2LessonService.getInstance().getMasteryTestList());

        lessonWithReviewList.addAll(PhonicU3LessonService.getInstance().getLessonList());
        lessonWithReviewList.add(PhonicU3LessonService.getInstance().getMasteryTestList());

        lessonWithReviewList.addAll(PhonicU4LessonService.getInstance().getLessonList());
        lessonWithReviewList.add(PhonicU4LessonService.getInstance().getMasteryTestList());

        //make sure no null inside the list
        lessonWithReviewList.removeAll(Collections.singleton(null));

        for (int i = 0; i < lessonWithReviewList.size(); i++) {
            //only the first lesson should be selected
            lessonWithReviewList.get(i).setSelected(i == 0);
        }

        return lessonWithReviewList;
    }

    @Override
    protected boolean isRequiredToShowBothCaseLetter() {
        return false;
    }

    @Override
    protected List<LessonWithReview> getAllLessonList() {
        return null;
    }

    @Override
    protected LessonWithReview getAllMasteryTest() {
        return null;
    }

    @Override
    public void doRender() {

        super.doRender();

        if (isAlphabetUnitSelected()) {
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
        } else {
            phonicReadListenSection.render(isReadAndListen());
            phonicSoundSpeakingSection.render(isSingleLetterSpeaking());

            phonicFillInTheBlankSection.render(isFillInTheBlank());

            phonicGroupingSection.render(isGrouping());

            multipleWordSpeakingSection.render(isReviewSpeaking());

            phonicWordBlendSection.render(UserPreferenceUtils.getInstance().isEnglish() && isWordBlend());
            phonicSwahiliWordBlendSection.render(UserPreferenceUtils.getInstance().isSwahili() && isWordBlend());

            phonicListenAndTypeSection.render(UserPreferenceUtils.getInstance().isEnglish() && isListenAndType());
            phonicSwahiliListenAndTypeSection.render(UserPreferenceUtils.getInstance().isSwahili() && isListenAndType());
        }

    }

    private boolean isAlphabetUnitSelected() {
        return (null != selectedActivity && "A".equals(selectedActivity.getUnitCode())) ||
                (null != selectedReview && "A".equals(selectedReview.getUnitCode())) ||
                (null != selectedMasteryTest && "A".equals(selectedMasteryTest.getUnitCode()));
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return null;
    }

    @Override
    public AbstractLessonService getLessonService() {
        return PhonicLessonService.getInstance();
    }

}
