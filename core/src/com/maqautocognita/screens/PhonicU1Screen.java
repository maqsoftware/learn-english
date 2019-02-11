
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.Phonic.MultipleWordSpeakingSection;
import com.maqautocognita.section.Phonic.PhonicFillInTheBlankSection;
import com.maqautocognita.section.Phonic.PhonicGroupingSection;
import com.maqautocognita.section.Phonic.PhonicListenAndTypeSection;
import com.maqautocognita.section.Phonic.PhonicReadListenSection;
import com.maqautocognita.section.Phonic.PhonicSoundSpeakingSection;
import com.maqautocognita.section.Phonic.PhonicSwahiliListenAndTypeSection;
import com.maqautocognita.section.Phonic.PhonicSwahiliWordBlendSection;
import com.maqautocognita.section.Phonic.PhonicWordBlendSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.PhonicLessonService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.util.List;

public class PhonicU1Screen extends AbstractPhonicScreen {

    private PhonicReadListenSection phonicReadListenSection;
    private PhonicFillInTheBlankSection phonicFillInTheBlankSection;
    private PhonicGroupingSection phonicGroupingSection;
    private PhonicListenAndTypeSection phonicListenAndTypeSection;
    private PhonicWordBlendSection phonicWordBlendSection;
    private MultipleWordSpeakingSection multipleWordSpeakingSection;
    private PhonicSoundSpeakingSection phonicSoundSpeakingSection;

    private PhonicSwahiliWordBlendSection phonicSwahiliWordBlendSection;
    private PhonicSwahiliListenAndTypeSection phonicSwahiliListenAndTypeSection;

    public PhonicU1Screen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener,
                ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD,
                ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD_DISABLED : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_DISABLED,
                ScreenUtils.isLandscapeMode ? AssetManagerUtils.PHONICS_KEYBOARD_HIGHLIGHTED : AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_HIGHLIGHTED,
                AssetManagerUtils.PHONICS_SYMBOL, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_DISABLED, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_HIGHLIGHTED);

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
        getLessonService().initAllLesson();
        PhonicLessonService.getInstance().initPhonicSoundMapping();
    }

    @Override
    public AbstractLessonService getLessonService() {
        return PhonicLessonService.getInstance();
    }

    @Override
    protected boolean isRequiredToShowBothCaseLetter() {
        return false;
    }

    @Override
    protected List<LessonWithReview> getAllLessonList() {
        return getLessonService().getLessonList();
    }

    @Override
    protected LessonWithReview getAllMasteryTest() {
        return getLessonService().getMasteryTestList();
    }

    @Override
    public void doRender() {
        super.doRender();

        if (isSectionRequiredToShow()) {
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

    protected boolean isSectionRequiredToShow() {
        return true;
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_1;
    }
}
