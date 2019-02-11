package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.service.AbstractSentenceLessonService;
import com.maqautocognita.service.ReadingComprehensionU3Service;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionU3Screen extends AbstractReadingComprehensionScreen {

    public ReadingComprehensionU3Screen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    @Override
    protected AbstractSentenceLessonService getSentenceLessonService() {
        return ReadingComprehensionU3Service.getInstance();
    }

    @Override
    public String getUnitCode() {
        return "3";
    }
}
