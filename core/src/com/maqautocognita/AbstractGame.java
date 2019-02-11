package com.maqautocognita;

import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.service.SingletonConnectionPool;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractGame extends Game implements InputProcessor {

    public static IAudioService audioService;

    protected Screen previousScreen;

    public AbstractGame() {

    }

    protected void start(String lessonJdbcUrl, String dictionaryJdbcUrl, IAudioService audioService) {
        SingletonConnectionPool.getInstance(lessonJdbcUrl, dictionaryJdbcUrl);
        this.audioService = audioService;

    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        setInputProcessor();
    }

    private void setInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {
        Gdx.app.log(getClass().getName(), "dispose");
        super.dispose();
        AssetManagerUtils.dispose();
        FontGeneratorManager.clear();
        addMinutesUsed();
    }

    @Override
    public void pause() {
        super.pause();
        FontGeneratorManager.clear();
        addMinutesUsed();
    }

    @Override
    public void resume() {
        setInputProcessor();
        super.resume();
    }

    @Override
    public void render() {
        // set viewport
        Gdx.gl.glViewport((int) ScreenUtils.viewport.x, (int) ScreenUtils.viewport.y,
                (int) ScreenUtils.viewport.width,
                (int) ScreenUtils.viewport.height);

        super.render();

    }

    @Override
    public void resize(int width, int height) {
        //ScreenUtils.isLandscapeMode = width > height;

        // calculate new viewport
        ScreenUtils.setViewport(new Rectangle(0, 0, width, height));

        super.resize(width, height);
    }

    private void addMinutesUsed() {

    }

    protected void showScreen(Screen newScreen) {
        previousScreen = screen;
        if (null != previousScreen) {
            previousScreen.hide();
            previousScreen.dispose();
        }
        if (null != audioService) {
            //make sure no audio is playing
            audioService.stopMusic();
        }
        setScreen(newScreen);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
