package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actors.BodyGroup;
import com.maqautocognita.scene2d.actors.MessageActor;
import com.maqautocognita.scene2d.actors.StoryModeCharacter;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractStoryScreen extends AbstractAutoCognitaScreen {

    protected StoryModeCharacter character;
    protected CustomCamera fixedStageCamera;
    protected Stage fixedStage;
    private InputMultiplexer inputMultiplexer;
    private Music backgroundMusic;
    private float BACKGROUND_MUSIC_VOLUME = 0.1f;
    private MessageActor messageActor;

    public AbstractStoryScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
    }

    protected MessageActor getMessageActor() {

        initFixedStage();

        if (null == messageActor) {
            messageActor = new MessageActor(this);
            fixedStage.addActor(messageActor);
        }
        return messageActor;
    }

    private void initFixedStage() {
        if (null == fixedStageCamera) {
            fixedStageCamera = new CustomCamera();
            fixedStageCamera.setWorldWidth(Config.TABLET_SCREEN_WIDTH);
        }
        if (null == fixedStage) {
            fixedStage = new Stage(new ScreenViewport(fixedStageCamera));
            Gdx.app.log(getClass().getName(), "add fixed stage camera");
            getInputMultiplexer().addProcessor(0, fixedStage);
        }
    }

    private InputMultiplexer getInputMultiplexer() {
        if (null == inputMultiplexer) {
            inputMultiplexer = new InputMultiplexer();
        }

        return inputMultiplexer;
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    protected boolean isRequiredToShowCharacterSetupButton() {
        return true;
    }

    @Override
    protected List<AbstractAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    protected String getAudioPath() {
        return super.getAudioPath() + "/" + UserPreferenceUtils.getInstance().getLanguage().toLowerCase() + "/storymode";
    }

    @Override
    public void show() {
        super.show();
        reloadInputProcessor();
        addCharacter();
    }

    protected void reloadInputProcessor() {
        getInputMultiplexer().addProcessor(0, Gdx.input.getInputProcessor());
        Gdx.input.setInputProcessor(getInputMultiplexer());
    }

    protected void addCharacter() {
        if (isCharacterRequiredToShow() && null == character) {
            addCharacterToMainStage();
        }
    }

    protected boolean isCharacterRequiredToShow() {
        return true;
    }

    protected StoryModeCharacter addCharacterToMainStage() {
        if (null == character && null != getMainStage()) {
            character = new StoryModeCharacter(new BodyGroup(getCharacterStartXPosition(), 50));
            getMainStage().addActor(character);
            Gdx.app.log(getClass().getName(), "add character");
        }
        return character;
    }

    protected abstract Stage getMainStage();

    protected float getCharacterStartXPosition() {
        return 10;
    }

    @Override
    public void doRender() {
        playBackgroundMusic();
        if (ArrayUtils.isNotEmpty(getStages())) {

            for (Stage stage : getStages()) {
                if (null != stage) {
                    stage.act(Gdx.graphics.getDeltaTime());
                    stage.draw();
                }
            }
        }

        if (null != fixedStage) {
            fixedStage.act(Gdx.graphics.getDeltaTime());
            fixedStage.draw();
        }

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);


        if (ArrayUtils.isNotEmpty(getStages())) {
            for (Stage stage : getStages()) {
                if (null != stage && null != stage.getViewport()) {
                    stage.getViewport().update(width, height, true);
                }
            }
        }

        reloadCamera();

        afterCameraSetup();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();

        clearCharacterAndInputTouchDetector();

        clearBackgroundMusic();

        disposeStage(fixedStage);
        fixedStageCamera = null;
        fixedStage = null;
        messageActor = null;
    }

    protected void setScreenOrientation() {
        ScreenUtils.setLandscapeMode();
    }

    protected void clearCharacterAndInputTouchDetector() {
        inputMultiplexer = null;

        if (null != character) {
            character.remove();
            character = null;
        }
    }

    protected void clearBackgroundMusic() {
        if (null != backgroundMusic) {
            if (AssetManagerUtils.assetManager.isLoaded(getBackgroundMusicFilePath())) {
                AssetManagerUtils.assetManager.unload(getBackgroundMusicFilePath());
            }
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    protected void disposeStage(Stage stage) {
        if (null != stage) {
            StageUtils.dispose(stage);
        }
    }

    private String getBackgroundMusicFilePath() {
        String backgroundMusicFileName = getBackgroundMusicFileName();
        if (StringUtils.isNotBlank(backgroundMusicFileName)) {
            return File.separator + backgroundMusicFileName;
        }
        return null;
    }

    protected abstract String getBackgroundMusicFileName();

    protected abstract Stage[] getStages();

    protected void reloadCamera() {
        if (ArrayUtils.isNotEmpty(getCameras())) {
            for (CustomCamera camera : getCameras()) {
                updateCamera(camera);
            }
        }
    }

    /**
     * It will be call after all camera are set the viewport size and start position
     */
    protected void afterCameraSetup() {
        initFixedStage();
        updateCamera(fixedStageCamera);
    }

    protected abstract CustomCamera[] getCameras();

    protected void updateCamera(CustomCamera customCamera) {
        if (null != customCamera) {
            customCamera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            customCamera.update();
            customCamera.setOriginalPosition(customCamera.position.x, customCamera.position.y);
        }
    }

    private void playBackgroundMusic() {
        if (null == backgroundMusic) {
            String backgroundMusicFilePath = getBackgroundMusicFilePath();
            if (StringUtils.isNotBlank(backgroundMusicFilePath)) {
                backgroundMusic = Gdx.audio.newMusic(Gdx.files.absolute(AbstractGame.audioService.getAudioStorePath() + backgroundMusicFilePath));
                backgroundMusic.setVolume(BACKGROUND_MUSIC_VOLUME);
                backgroundMusic.play(); // play new sound and keep handle for further manipulation
                backgroundMusic.setLooping(true); // keeps the sound looping
                Gdx.app.log(getClass().getName(), "playing background music = " + getBackgroundMusicFilePath());
            }
        }
    }

    protected void addInputProcessor(Stage stage) {
        getInputMultiplexer().addProcessor(stage);
    }

    public abstract void preload();

}
