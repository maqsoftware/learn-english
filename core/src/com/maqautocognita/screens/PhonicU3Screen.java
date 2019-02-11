
package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.PhonicU3LessonService;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicU3Screen extends PhonicU2Screen {

    public PhonicU3Screen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return PhonicU3LessonService.getInstance();
    }

    @Override
    protected LessonUnitCode getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_3;
    }

}
