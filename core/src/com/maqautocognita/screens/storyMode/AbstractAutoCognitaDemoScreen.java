
package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.ISoundPlayListListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.screens.AutoCognitaGestureListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.util.List;

public abstract class AbstractAutoCognitaDemoScreen implements Screen, AutoCognitaGestureListener {

    public static final String CORRECT_SOUND_AUDIO_FILE_NAMES[] = {"correct1", "correct2", "correct3", "correct4"};
    protected static final int SPACE_BETWEEN_TOP_ICON = 50;
    protected static IconPosition CLOTHES_ICON_POSITION = new IconPosition(165, 390, 100, 100);
    protected static IconPosition HOME_ICON_POSITION = new IconPosition(300, 300, 100, 100);
    protected final IMenuScreenListener menuScreenListener;
    private final String[] images;
    protected SpriteBatch correctBatch;
    protected SpriteBatch mainBatch;
    protected AbstractGame game;
    private TextureScreenObject homeScreenObject;
    private TextureScreenObject clothesScreenObject;
    private boolean showCorrectFrame;
    private boolean showWrongFrame;
    private int screenWidth;
    private List<ScreenObject> debugMessageScreenObjectList;
    private boolean isTouchAllow = true;
    /**
     * This is the flag to control is the render is required
     */
    private boolean isRequiredRender = true;

    public AbstractAutoCognitaDemoScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        this.game = game;
        this.images = images;
        this.menuScreenListener = menuScreenListener;
    }

    public abstract AbstractLessonService getLessonService();

    public void setRequiredRender(boolean requiredRender) {
        Gdx.app.log(getClass().getName(), "requiredRender = " + requiredRender);
        isRequiredRender = requiredRender;
    }

    public void restart() {

    }

    public abstract void showNextSection(int numberOfFails);

    public void playCorrectSound() {
        playCorrectSound(null);
    }

    public void playCorrectSound(final ICorrectSoundListener correctSoundListener) {
        showCorrectFrame = true;
        String correctSoundAudioFileName = CORRECT_SOUND_AUDIO_FILE_NAMES[RandomUtils.getRandomWithExclusion(0, CORRECT_SOUND_AUDIO_FILE_NAMES.length - 1)];
        playSoundWithFullPath(correctSoundAudioFileName, new AbstractSoundPlayListener() {

            @Override
            public void onComplete() {
                onFinish();
            }

            @Override
            public void onStop() {
                onFinish();
            }

            private void onFinish() {
                showCorrectFrame = false;
                if (null != correctSoundListener) {
                    correctSoundListener.onCorrectSoundPlayed();
                }
            }
        });
    }

    public void playWrongSound() {
        playWrongSound(null);
    }

    public void playWrongSound(final ICorrectSoundListener wrongSoundListener) {
        showWrongFrame = true;
        String correctSoundAudioFileName = CORRECT_SOUND_AUDIO_FILE_NAMES[RandomUtils.getRandomWithExclusion(0, CORRECT_SOUND_AUDIO_FILE_NAMES.length - 1)];
        playSoundWithFullPath(correctSoundAudioFileName, new AbstractSoundPlayListener() {

            @Override
            public void onComplete() {
                onFinish();
            }

            @Override
            public void onStop() {
                onFinish();
            }

            private void onFinish() {
                showWrongFrame = false;
                if (null != wrongSoundListener) {
                    wrongSoundListener.onCorrectSoundPlayed();
                }
            }
        });
    }

    private void playSoundWithFullPath(String audioPath, ISoundPlayListener musicPlayerListener) {
        //TODO all given fileNameWithExtension must has extension and using the extension instead of search the extension,
        // if no, no need help to add extension
        if (AbstractGame.audioService.isAudioExists(audioPath + ".flac")) {
            audioPath = audioPath + ".flac";
        } else if (AbstractGame.audioService.isAudioExists(audioPath + ".wav")) {
            audioPath = audioPath + ".wav";
        } else if (AbstractGame.audioService.isAudioExists(audioPath + ".m4a")) {
            audioPath = audioPath + ".m4a";
        }

        if (AbstractGame.audioService.isAudioExists(audioPath)) {
            Gdx.app.log(this.getClass().getName(), "Going to play audio " + audioPath);
            AbstractGame.audioService.playAudio(audioPath, musicPlayerListener);
        } else {
            String message = "Audio : " + audioPath + " is not found";
            Gdx.app.error(this.getClass().getName(), message);

//            debugMessageScreenObjectList = LetterUtils.getTextScreenObjectList(Config.SCREEN_CENTER_START_X_POSITION, 1180, message,
//                    TextFontSizeEnum.FONT_48);
//
//            for (ScreenObject screenObject : debugMessageScreenObjectList) {
//                screenObject.highlighted = true;
//            }

            if (null != musicPlayerListener) {
                musicPlayerListener.onAudioFileMissing();
            }
        }
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {


        if (isTouchAllow()) {
            if (CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
                for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                    autoCognitaSection.whenTouchDown((int) x, (int) y, (int) x, (int) y);
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (isTouchAllow()) {
            int screenX = (int) ScreenUtils.toViewPosition(x);
            int screenY = (int) ScreenUtils.getExactYPositionOnScreen(y);

            if (isRequiredToShowHomeButton()) {
                if (TouchUtils.isTouched(homeScreenObject, screenX, screenY)) {
                    onHomeClick();
                    return false;
                }
            }

            if (isRequiredToShowCharacterSetupButton()) {

                if (TouchUtils.isTouched(clothesScreenObject, screenX, screenY)) {
                    if (null != menuScreenListener) {
                        menuScreenListener.onCharacterSetupSelected();
                    }
                    return false;
                }
            }

            if (CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
                for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                    autoCognitaSection.whenSingleTap(screenX, screenY);
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (isTouchAllow()) {
            if (CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
                int screenX = (int) ScreenUtils.toViewPosition(x);
                int screenY = (int) ScreenUtils.getExactYPositionOnScreen(y);
                for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                    autoCognitaSection.whenTouchDragged(screenX, screenY, (int) x, (int) y);
                }
            }
            return false;

        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    protected boolean isRequiredToShowHomeButton() {
        return true;
    }

    protected void onHomeClick() {
        if (null != menuScreenListener) {
            menuScreenListener.onHomeSelected();
        }
    }

    protected boolean isRequiredToShowCharacterSetupButton() {
        return false;
    }

    protected boolean isTouchAllow() {
        return !showCorrectFrame && isTouchAllow;
    }

    /**
     * This is mainly used to indicate if the touch event is send the the sub class,
     * if false is set, the touch event will not trigger the touch listener in children {@link #getAutoCognitaSectionList()}
     *
     * @param isTouchAllow
     */
    public void setTouchAllow(boolean isTouchAllow) {
        this.isTouchAllow = isTouchAllow;
        Gdx.app.log(getClass().getName(), "isTouchAllow = " + isTouchAllow);
    }

    protected abstract List<? extends IAutoCognitaSection> getAutoCognitaSectionList();

    @Override
    public boolean touchUp(float x, float y, int pointer, int button) {
        if (isTouchAllow()) {
            if (CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
//                int screenX = (int) ScreenUtils.toViewPosition(x);
//                int screenY = (int) ScreenUtils.getExactYPositionOnScreen(y);
                for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                    autoCognitaSection.whenTouchUp((int) x, (int) y, (int) x, (int) y);
                }
            }
            return false;
        }
        return true;
    }

    public void playSound(String audioFileNameWithExtension) {
        playSound(audioFileNameWithExtension, null);
    }

    public void playSound(String audioFileNameWithExtension, ISoundPlayListener musicPlayerListener) {
        if (StringUtils.isNotBlank(audioFileNameWithExtension)) {
            //remove the fileNameExtension
            audioFileNameWithExtension = audioFileNameWithExtension.replaceAll("\\.m4a", "").replaceAll("\\.wav", "");

            String audioPath = getAudioPath() + File.separator + audioFileNameWithExtension;

            playSoundWithFullPath(audioPath, musicPlayerListener);

        } else {
            if (null != musicPlayerListener) {
                musicPlayerListener.onStop();
            }
        }
    }

    protected String getAudioPath() {
        return "";
    }

    protected boolean isSoundFileExists(String audioFileNameWithExtension) {
        if (StringUtils.isNotBlank(audioFileNameWithExtension)) {
            //remove the fileNameExtension
            audioFileNameWithExtension = audioFileNameWithExtension.replaceAll(".m4a", "").replaceAll(".wav", "");

            String audioPath = getAudioPath() + File.separator + audioFileNameWithExtension;

            //TODO all given fileNameWithExtension must has extension and using the extension instead of search the extension,
            // if no, no need help to add extension
            if (Gdx.files.internal(audioPath + ".flac").exists()) {
                audioPath = audioPath + ".flac";
            } else if (Gdx.files.internal(audioPath + ".wav").exists()) {
                audioPath = audioPath + ".wav";
            } else if (Gdx.files.internal(audioPath + ".m4a").exists()) {
                audioPath = audioPath + ".m4a";
            }

            return Gdx.files.internal(audioPath).exists();
        }
        return false;
    }

    public void playSound(List<String> audioFileNameWithExtensionList) {
        playSound(audioFileNameWithExtensionList, 0, null);
    }

    public void playSound(List<String> audioFileNameWithExtensionList, ISoundPlayListListener soundPlayListListener) {
        playSound(audioFileNameWithExtensionList, 0, soundPlayListListener);
    }

    public void playSound(final List<String> audioFileNameWithExtensionList, final int audioFileNameIndex, final ISoundPlayListListener soundPlayListListener) {
        if (CollectionUtils.isNotEmpty(audioFileNameWithExtensionList)) {
            if (null != soundPlayListListener) {
                soundPlayListListener.beforePlaySound(audioFileNameIndex);
            }
            playSound(audioFileNameWithExtensionList.get(audioFileNameIndex), new AbstractSoundPlayListener() {
                @Override
                public void onPlay(int audioListIndex, long millisecond) {
                    if (null != soundPlayListListener) {
                        soundPlayListListener.onPlay(audioFileNameIndex, millisecond);
                    }
                }

                @Override
                public void onComplete() {
                    if (audioFileNameIndex < audioFileNameWithExtensionList.size() - 1) {
                        playAudio();
                    } else {
                        if (null != soundPlayListListener) {
                            soundPlayListListener.onComplete();
                        }
                    }
                }

                private void playAudio() {

                    playSound(audioFileNameWithExtensionList, audioFileNameIndex + 1, soundPlayListListener);
                }

                @Override
                public void onStop() {
                    if (null != soundPlayListListener) {
                        soundPlayListListener.onStop();
                    }
                }


            });
        } else if (null != soundPlayListListener) {
            soundPlayListListener.onStop();
        }

    }

    public interface ICorrectSoundListener {
        void onCorrectSoundPlayed();
    }

    class AutoCognitaGestureDetector extends GestureDetector {

        private final AutoCognitaGestureListener listener;

        public AutoCognitaGestureDetector(AutoCognitaGestureListener listener) {
            super(listener);
            this.listener = listener;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            listener.touchUp(screenX, screenY, pointer, button);
            super.touchUp(screenX, screenY, pointer, button);
            return false;
        }
    }

    @Override
    public void show() {
        //final boolean previousLandscape = ScreenUtils.isLandscapeMode;

        setScreenOrientation();

//        if (previousLandscape != ScreenUtils.isLandscapeMode) {
//            if (Application.ApplicationType.Android.equals(Gdx.app.getType())) {
//                resize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
//            }
//        }

        Gdx.app.log(getClass().getName(), "show");

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(new AutoCognitaGestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        AssetManagerUtils.loadTexture(getIconPath());
        AssetManagerUtils.loadTexture(AssetManagerUtils.CORRECT_FRAME);
        AssetManagerUtils.loadTexture(AssetManagerUtils.WRONG_FRAME);
        AssetManagerUtils.loadTexture(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH);

        for (String image : images) {
            AssetManagerUtils.loadTexture(image);
        }

        correctBatch = new SpriteBatch();
        correctBatch.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

        mainBatch = new SpriteBatch();
        mainBatch.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

        isRequiredRender = true;
    }


    @Override
    public void render(float delta) {

        if (isRequiredRender) {
            clearScreen();
            /**
             * check if all texture in {@link AutoCognitaGame#create()} in loaded
             */
            if (!isRequiredToWaitImageLoading() || AssetManagerUtils.isFinishLoading()) {


                doRender();

                if (showCorrectFrame && null != correctBatch) {
                    correctBatch.begin();
                    correctBatch.draw(AssetManagerUtils.getTexture(AssetManagerUtils.CORRECT_FRAME), (ScreenUtils.getScreenWidth()/2)-250,(ScreenUtils.getScreenHeight()/2)-250, 500, 500);
                    correctBatch.end();
                }

                if (isRequiredToShowHomeButton()) {

                    if (null == homeScreenObject
                            //TODO in the home story mode, if it enter to another scene ,  go back to the menu screen, and enter to home scene again, suppose the homeScreenObject is null ,
                            // but it is here and with the empty texture
                            || null == homeScreenObject.getTexture()
                            ) {
                        Vector2 homeIconScreenPosition = getHomeIconScreenPosition();
                        homeScreenObject = new TextureScreenObject(null, null, HOME_ICON_POSITION,
                                homeIconScreenPosition.x, homeIconScreenPosition.y,
                                AssetManagerUtils.getTexture(getIconPath()));
                    }

                    if (null != mainBatch) {
                        mainBatch.begin();
                        ScreenObjectUtils.draw(mainBatch, homeScreenObject);

                        if (isRequiredToShowCharacterSetupButton()) {
                            if (null == clothesScreenObject || null == clothesScreenObject.getTexture()
                                    ) {
                                Vector2 clothesIconScreenPosition = getClothesScreenPosition();
                                clothesScreenObject = new TextureScreenObject(null, null, CLOTHES_ICON_POSITION, clothesIconScreenPosition.x,
                                        clothesIconScreenPosition.y, AssetManagerUtils.getTexture(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH));

                            }

                            ScreenObjectUtils.draw(mainBatch, clothesScreenObject);
                        }

                        if (CollectionUtils.isNotEmpty(debugMessageScreenObjectList)) {
                            ScreenObjectUtils.draw(mainBatch, debugMessageScreenObjectList);
                        }

                        mainBatch.end();
                    }
                }
            }
        }
    }

    protected void clearScreen() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected boolean isRequiredToWaitImageLoading() {
        return true;
    }

    public abstract void doRender();

    protected Vector2 getHomeIconScreenPosition() {
        return new Vector2(50, ScreenUtils.getNavigationBarStartYPosition());
    }

    protected String getIconPath() {
        return AssetManagerUtils.ICONS;
    }

    protected Vector2 getClothesScreenPosition() {
        Vector2 homeIconScreenPosition = getHomeIconScreenPosition();
        return new Vector2(homeIconScreenPosition.x + HOME_ICON_POSITION.width + SPACE_BETWEEN_TOP_ICON, homeIconScreenPosition.y);
    }

    @Override
    public void resize(int width, int height) {
        ScreenUtils.setViewport(new Rectangle(0, 0, width, height));
        boolean isRotated = false;
        if (screenWidth != 0 && screenWidth != width) {
            //if the screen is not the first time show and the device is rotated
            //hide();
            //isRotated = true;
        }
        this.screenWidth = width;

        if (null != correctBatch) {
            correctBatch.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        }

        if (null != mainBatch) {
            mainBatch.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        }

        //if (isRotated) {
        //show();
        //}

        if (CollectionUtils.isNotEmpty(getAutoCognitaSectionList())) {
            for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                autoCognitaSection.onResize();
            }
        }

    }

    @Override
    public void pause() {
        hide();
    }

    @Override
    public void resume() {
        show();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {

        Gdx.app.log(getClass().getName(), "hide");

        AbstractGame.audioService.stopMusic();
        AbstractGame.audioService.stopRecord();

        homeScreenObject = null;
        clothesScreenObject = null;

        AssetManagerUtils.unloadTexture(getIconPath());
        AssetManagerUtils.unloadTexture(AssetManagerUtils.CORRECT_FRAME);
        AssetManagerUtils.unloadTexture(AssetManagerUtils.WRONG_FRAME);
        AssetManagerUtils.unloadTexture(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH);

        for (String image : images) {
            AssetManagerUtils.unloadTexture(image);
        }
        if (null != getAutoCognitaSectionList()) {
            for (IAutoCognitaSection autoCognitaSection : getAutoCognitaSectionList()) {
                autoCognitaSection.dispose();
            }
        }

        Gdx.input.setInputProcessor(null);

        //FontGeneratorManager.clear();

        if (null != correctBatch) {
            correctBatch.dispose();
            correctBatch = null;
        }

        if (null != mainBatch) {
            mainBatch.dispose();
            mainBatch = null;
        }

        clearDebugMessage();

        AssetManagerUtils.unloadAllTexture();
    }

    @Override
    public void dispose() {
        homeScreenObject = null;
    }

    protected void clearDebugMessage() {
        debugMessageScreenObjectList = null;
    }

    protected void setScreenOrientation() {
        if (ScreenUtils.isTablet) {
            ScreenUtils.setLandscapeMode();
        } else {
            ScreenUtils.setPortraitMode();
        }
    }


}
