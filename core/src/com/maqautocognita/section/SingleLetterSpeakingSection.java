package com.maqautocognita.section;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SingleLetterSpeakingSection extends AbstractSpeakingSection {

    private String[] playLetter;

    public SingleLetterSpeakingSection(AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return null;
    }

    @Override
    public float getMicrophoneStartXPosition() {
        return ScreenUtils.isLandscapeMode ? 1550 : ScreenUtils.getXPositionForCenterObject(ImageProperties.MICROPHONE_SIZE);
    }

    @Override
    public float getMicrophoneStartYPosition() {
        return ScreenUtils.isLandscapeMode ? Config.SPEAKING_SOUND_START_Y_POSITION + 93 : ScreenUtils.getStartYPositionForCenterObject(ImageProperties.MICROPHONE_SIZE, ScreenUtils.getScreenHeightWithoutNavigationBar() / 2);
    }

    @Override
    public String getRecognizeFileName() {
        return "alphabet.gram";
    }

    @Override
    protected String[] getWords() {
        if (null != selectedActivity && null == playLetter && StringUtils.isNotBlank(selectedActivity.getLetter())) {
            playLetter = new String[]{selectedActivity.getLetter()};
        }
        return playLetter;
    }

    @Override
    protected int getNumberOfWordToBeDisplay() {
        return 1;
    }

    @Override
    protected TextFontSizeEnum getLetterFontSize() {
        return TextFontSizeEnum.FONT_288;
    }

    @Override
    protected float getWordStartYPosition(float totalWordHeight) {
        return ScreenUtils.isLandscapeMode ? Config.SPEAKING_SOUND_START_Y_POSITION :
                ScreenUtils.getBottomYPositionForCenterObject(totalWordHeight, ScreenUtils.getScreenHeightWithoutNavigationBar() / 2) + ScreenUtils.getScreenHeightWithoutNavigationBar() / 2;
    }

    @Override
    public void reset() {
        super.reset();
        playLetter = null;
    }

    @Override
    public boolean isSoundRecognizeRequired() {
        return UserPreferenceUtils.getInstance().isEnglish();
    }
}
