
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.Phonic.PhonicSwahiliListenAndTypeFreeTypeSection;
import com.maqautocognita.service.PhonicU4LessonService;
import com.maqautocognita.utils.UserPreferenceUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicU4Screen extends PhonicU2Screen {

    private PhonicSwahiliListenAndTypeFreeTypeSection phonicSwahiliListenAndTypeFreeTypeSection;

    public PhonicU4Screen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
        phonicSwahiliListenAndTypeFreeTypeSection = new PhonicSwahiliListenAndTypeFreeTypeSection(this, this);
        autoCognitaSectionList.add(phonicSwahiliListenAndTypeFreeTypeSection);
    }

    @Override
    public PhonicU4LessonService getLessonService() {
        return PhonicU4LessonService.getInstance();
    }


    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_4;
    }


    @Override
    public void doRender() {
        super.doRender();
        if (UserPreferenceUtils.getInstance().isSwahili()) {
            phonicSwahiliListenAndTypeFreeTypeSection.render(true);
        }

    }

    @Override
    protected boolean isSectionRequiredToShow() {
        return UserPreferenceUtils.getInstance().isEnglish();
    }
}
