package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.service.AbstractSentenceLessonService;
import com.maqautocognita.service.SentenceU4LessonService;

/**
 * Created by siu-chun.chi on 5/18/2017.
 */

public class SentenceU4Screen extends AbstractSentenceScreen {

    public SentenceU4Screen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    @Override
    protected AbstractSentenceLessonService getSentenceLessonService() {
        return SentenceU4LessonService.getInstance();
    }

    @Override
    public String getUnitCode() {
        return "4";
    }
}
