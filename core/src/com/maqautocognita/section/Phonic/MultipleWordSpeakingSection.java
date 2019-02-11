package com.maqautocognita.section.Phonic;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractSpeakingSection;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.UserPreferenceUtils;


/**
 * This is mainly used to draw the "speaking" section for phonic module.
 * All word and the microphone will be draw hort in the screen
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class MultipleWordSpeakingSection extends AbstractSpeakingSection {

    private final String moduleName;

    private final float GAP_BETWEEN_MICROPHONE_AND_WORD_IN_MOBILE_VERSION = 80;

    private float wordStartBottomYPositionInMobileVersion;

    public MultipleWordSpeakingSection(String moduleName, AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
        this.moduleName = moduleName;
    }

    @Override
    public float getMicrophoneStartXPosition() {
        return ScreenUtils.isLandscapeMode ? 1600 : ScreenUtils.getXPositionForCenterObject(ImageProperties.MICROPHONE_SIZE);
    }


    @Override
    public float getMicrophoneStartYPosition() {
        return ScreenUtils.isLandscapeMode ? 100 :
                wordStartBottomYPositionInMobileVersion - GAP_BETWEEN_MICROPHONE_AND_WORD_IN_MOBILE_VERSION - ImageProperties.MICROPHONE_SIZE * 2;
    }

    /**
     * Each activity will has their own speech recognize file, and the file name will be named as [module name]-[unit code]-[lesson code] in lower case
     * <p/>
     * For example : "phonic-u1-r1.gram"
     *
     * @return
     */
    @Override
    public String getRecognizeFileName() {
        return new String(moduleName + "-" + selectedActivity.getUnitCode() + "-" + selectedActivity.getLessonCode() + "-" + selectedActivity.getSequence() + ".gram").toLowerCase();
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    protected boolean isVerticalAlign() {
        if (ScreenUtils.isLandscapeMode) {
            return false;
        } else {
            if (ArrayUtils.isNotEmpty(getWords())) {
                for (String word : getWords()) {
                    if (word.length() > 1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }


    @Override
    protected String[] getWords() {
        return selectedActivity.getWords();
    }

    @Override
    protected int getNumberOfWordToBeDisplay() {
        return selectedActivity.getWords().length;
    }

    @Override
    protected TextFontSizeEnum getLetterFontSize() {
        return TextFontSizeEnum.FONT_144;
    }

    @Override
    protected float getWordStartYPosition(float totalWordHeight) {

        wordStartBottomYPositionInMobileVersion = ScreenUtils.getBottomYPositionForCenterObject(totalWordHeight + ImageProperties.MICROPHONE_SIZE + GAP_BETWEEN_MICROPHONE_AND_WORD_IN_MOBILE_VERSION)
                + ImageProperties.MICROPHONE_SIZE + GAP_BETWEEN_MICROPHONE_AND_WORD_IN_MOBILE_VERSION;

        return ScreenUtils.isLandscapeMode ? 700 : wordStartBottomYPositionInMobileVersion;

    }

    @Override
    public boolean isSoundRecognizeRequired() {
        return UserPreferenceUtils.getInstance().isEnglish();
    }


}
