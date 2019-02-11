package com.maqautocognita.section;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAutoCognitaSection implements IAutoCognitaSection {

    protected AbstractAutoCognitaScreen abstractAutoCognitaScreen;
    protected IOnHelpListener onHelpListener;
    protected boolean isIntroductionAudioPlayed;
    protected SpriteBatch batch;
    protected boolean isShowing;
    protected boolean isHelpAudioPlaying;
    /**
     * Store the number of times which the user has answer  fails, it will be when the screen show again
     */
    protected int numberOfFails;

    /**
     * This is mainly used to indicate if the section is show again, some section is required
     */
    private boolean isShowAgain;

    private String[] images;

    public AbstractAutoCognitaSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        this.abstractAutoCognitaScreen = abstractAutoCognitaScreen;
        this.onHelpListener = onHelpListener;

        batch = new SpriteBatch();
        setProjection();
    }

    private void setProjection() {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, getExpectedScreenWidth(), getExpectedScreenHeight());
    }

    protected float getExpectedScreenWidth() {
        return ScreenUtils.getScreenWidth();
    }

    protected float getExpectedScreenHeight() {
        return ScreenUtils.getScreenHeight();
    }

    public void render(boolean isShowing) {

        this.isShowing = isShowing;
        if (isShowing) {
            loadAllRequiredImages();
            if (AssetManagerUtils.isFinishLoading()) {
                if (!isShowAgain) {
                    isShowAgain = true;
                    isHelpAudioPlaying = false;
                    resetNumberOfFails();
                    onShowAgain();
                }
                if (this.isShowing) {
                    render();
                }

            }
        } else {
            if (isShowAgain) {
                onHide();
                isShowAgain = false;
            }
        }
    }

    private void loadAllRequiredImages() {
        if (null == images) {
            images = getAllRequiredTextureName();

            if (ArrayUtils.isNotEmpty(images)) {
                for (String image : images) {
                    if (StringUtils.isNotBlank(image)) {
                        AssetManagerUtils.loadTexture(image);
                    }
                }
            }
        }
    }

    private void resetNumberOfFails() {
        numberOfFails = 0;
    }

    /**
     * It is mainly triggered when the section is show again,
     * some section may want to play again the introduction audio when the screen is show
     */
    protected void onShowAgain() {
        playIntroductionAudio();
    }

    protected abstract void render();

    protected void onHide() {
        Gdx.app.log(getClass().getName(), "onHide");
        abstractAutoCognitaScreen.setTouchAllow(true);
        isShowing = false;
    }

    protected abstract String[] getAllRequiredTextureName();

    protected void playIntroductionAudio() {
        if (CollectionUtils.isNotEmpty(getIntroductionAudioFileName())) {
            isIntroductionAudioPlayed = false;
            Gdx.app.log(getClass().getName(), "going to play introduction audio");
            abstractAutoCognitaScreen.playSound(getIntroductionAudioFileName(), new AbstractSoundPlayListListener() {
                @Override
                public void onPlay(int audioListIndex, long millisecond) {
                    onIntroductionAudioPlaying(millisecond);
                }

                @Override
                public void onComplete() {

                    isIntroductionAudioPlayed = true;
                    onIntroductionAudioPlayed();
                }

                @Override
                public void onStop() {
                    isIntroductionAudioPlayed = true;
                    onIntroductionAudioStopped();
                }
            });
        } else {
            isIntroductionAudioPlayed = true;
            onNoIntroductionAudioPlay();
        }
    }

    protected List<String> getIntroductionAudioFileName() {
        if (null != getAudioFile()) {
            return getAudioFile().getIntroductionAudioFilenameList();
        }

        return null;
    }

    /**
     * It will be call when the introduction audio is playing
     *
     * @param millisecond, the millisecond played for the introduction audio
     */
    protected void onIntroductionAudioPlaying(long millisecond) {

    }

    /**
     * It will be call when the introduction audio is played
     */
    protected void onIntroductionAudioPlayed() {

    }

    /**
     * It will be call when the introduction audio is stopped when playing
     */
    protected void onIntroductionAudioStopped() {

    }

    /**
     * It will be call when there is no introduction audio play for the section
     */
    protected void onNoIntroductionAudioPlay() {

    }

    protected abstract AbstractAudioFile getAudioFile();

    @Override
    public void whenSingleTap(int screenX, int screenY) {
        if (isShowing) {
            singleTap(screenX, screenY);
        }
    }

    @Override
    public void whenTouchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isShowing) {
            touchDown(getTouchingScreenX(screenX), getTouchingScreenY(screenY), systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    public void whenTouchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isShowing) {
            touchUp(getTouchingScreenX(screenX), getTouchingScreenY(screenY), systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    public void whenTouchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isShowing) {
            touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    public void setAutoCognitaScreen(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        this.abstractAutoCognitaScreen = abstractAutoCognitaScreen;
        this.onHelpListener = onHelpListener;
    }

    @Override
    public void dispose() {

        isShowing = false;

        if (ArrayUtils.isNotEmpty(images)) {
            for (String image : images) {
                AssetManagerUtils.unloadTexture(image);
            }
        }

        images = null;

        isShowAgain = false;

    }

    public void closeHelp() {
        if (isShowing) {
            doCloseHelp();
        }
    }

    public void onHelp() {
        if (isHelpAudioAllowToPlay()) {
            if (CollectionUtils.isNotEmpty(getHelpAudioFileNameList())) {

                isHelpAudioPlaying = true;

                if (null != AutoCognitaGame.audioService) {
                    abstractAutoCognitaScreen.playSound(getHelpAudioFileNameList(), new AbstractSoundPlayListListener() {

                        @Override
                        public void beforePlaySound(int index) {
                            beforePlayHelpAudio(index);
                        }

                        @Override
                        public void onPlay(int audioListIndex, long millisecond) {
                            onHelpAudioPlay(audioListIndex, millisecond);
                        }

                        @Override
                        public void onComplete() {
                            onAudioStop();
                            onHelpAudioComplete();
                        }


                        @Override
                        public void onStop() {
                            onAudioStop();
                            onHelpAudioStop();
                        }

                        private void onAudioStop() {
                            isHelpAudioPlaying = false;
                            onHelpAudioPlayComplete();
                        }
                    });
                }

            } else {
                if (null != AutoCognitaGame.audioService) {
                    AutoCognitaGame.audioService.stopMusic();
                }
                onHelpAudioPlayComplete();
            }
        }
    }

    @Override
    public void onResize() {
        setProjection();
    }

    protected boolean isHelpAudioAllowToPlay() {
        return isShowing && !isHelpAudioPlaying;
    }

    protected List<String> getHelpAudioFileNameList() {
        if (null != getAudioFile()) {
            return getAudioFile().getHelpAudioFilenameList();
        }

        return null;
    }

    protected void beforePlayHelpAudio(int index) {

    }

    protected void onHelpAudioPlay(int audioListIndex, long millisecond) {

    }

    protected void onHelpAudioComplete() {

    }

    protected void onHelpAudioStop() {

    }

    private void onHelpAudioPlayComplete() {
        if (null != onHelpListener) {
            onHelpListener.onHelpComplete();
        }
    }

    protected void doCloseHelp() {
        AutoCognitaGame.audioService.stopMusic();
    }

    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

    }

    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

    }

    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

    }

    private int getTouchingScreenX(int screenX) {
        float widthRatio = getExpectedScreenWidth() / ScreenUtils.viewport.width;
        return (int) (screenX * widthRatio - ScreenUtils.viewport.x * widthRatio);
    }

    private int getTouchingScreenY(int screenY) {
        float heightRatio = getExpectedScreenHeight() / ScreenUtils.viewport.height;
        return (int) (getExpectedScreenHeight() - screenY * heightRatio + ScreenUtils.viewport.y * heightRatio);
    }

    protected void singleTap(int screenX, int screenY) {
    }

    protected void addNumberOfFails() {
        numberOfFails++;
    }


    public interface IOnHelpListener {

        void onHelpComplete();
    }

}
