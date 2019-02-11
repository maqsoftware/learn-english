package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.service.AbstractSentenceLessonService;
import com.maqautocognita.service.ReadingComprehensionU2Service;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionU2Screen extends AbstractReadingComprehensionScreen {

    public ReadingComprehensionU2Screen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    @Override
    protected AbstractSentenceLessonService getSentenceLessonService() {
        return ReadingComprehensionU2Service.getInstance();
    }

    @Override
    public String getUnitCode() {
        return "2";
    }
}
