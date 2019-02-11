
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.PhonicU2LessonService;

public class PhonicU2Screen extends PhonicU1Screen {

    public PhonicU2Screen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return PhonicU2LessonService.getInstance();
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_2;
    }

}
