package com.maqautocognita.section.Phonic;

import com.maqautocognita.Config;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.SingleLetterSpeakingSection;
import com.maqautocognita.service.PhonicSoundScreenService;
import com.maqautocognita.utils.ScreenUtils;

import java.util.ArrayList;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicSoundSpeakingSection extends SingleLetterSpeakingSection {

    private PhonicSoundScreenService phonicSoundScreenService;

    public PhonicSoundSpeakingSection(AbstractAutoCognitaScreen alphabetScreen, IOnHelpListener onHelpListener) {
        super(alphabetScreen, onHelpListener);
    }

    @Override
    protected void initScreenWordList() {
        if (null == phonicSoundScreenService) {
            phonicSoundScreenService = new PhonicSoundScreenService();
        }

        if (null == screenWordList) {
            screenWordList = new ArrayList<ScreenObject<String, ScreenObjectType>>();
        }

        screenWordList.addAll(phonicSoundScreenService.drawPhonicSoundWithLetter(selectedActivity,
                ScreenUtils.isLandscapeMode ? Config.SPEAKING_SOUND_START_Y_POSITION : ScreenUtils.getScreenHeightWithoutNavigationBar() / 2 +
                        LetterUtils.getMaximumHeight(TextFontSizeEnum.FONT_288)));
    }

    @Override
    public boolean isSoundRecognizeRequired() {
//        return false;
        return true;
    }
}
