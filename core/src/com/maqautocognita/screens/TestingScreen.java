package com.maqautocognita.screens;

import com.maqautocognita.section.sentence.SentenceBuildSection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by siu-chun.chi on 5/18/2017.
 */

public class TestingScreen implements Screen {

    private SentenceBuildSection sentenceBuildSection;

    @Override
    public void show() {
        if (null == sentenceBuildSection) {
            sentenceBuildSection = new SentenceBuildSection(null);
        }
        sentenceBuildSection.show(null, null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sentenceBuildSection.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        sentenceBuildSection.hide();
    }

    @Override
    public void resume() {
        sentenceBuildSection.show(null, null);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }
}
