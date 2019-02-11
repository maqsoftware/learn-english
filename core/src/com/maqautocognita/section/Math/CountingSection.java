package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Then, user sees random number of apples {@link #maximumNumberForCounting} in random non-overlapping locations,
 * and are asked to touch them one by one.  When the user touches each apple (in any order),
 * a number appears on the apple from 1, 2, and so on.
 * The number at the top changes automatically, and the sound of the number is played as each apple is counted.
 * <p>
 * <p>
 * If the given {@link #isKeyboardRequired} is true, the number keyboard in the bottom of the screen will be show, the object such as "apple" will not show the number when touch it,
 * and the user will asked to select the number in the keyboard for counting the number of object in the screen
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingSection extends AbstractNumberKeyboardSection {

    /**
     * The image path to represent the number , for example if the screen is playing the number 9, 9 images with below path will be shown
     */
    private static final Vector2 OBJECT_SIZE = new Vector2(200, 200);
    private static final float NUMBER_TEXT_START_Y_POSITION = 828;
    /**
     * Store the maximum number of time need to play
     */
    protected final int MAXIMUM_NUMBER_OF_ROUND_NEED_TO_PLAY = 5;
    private final int maximumNumberForCounting;
    private final boolean isKeyboardRequired;
    /**
     * Store the number of round played
     */
    protected int numberOfRoundPlayed;
    /**
     * The playing random number
     */
    protected int playingNumber;
    /**
     * The number display in the top center of the screen
     */
    private TextScreenObject numberScreenObject;
    /**
     * Mainly store the object which is added by click the add one button
     */
    private List<AnimateTextureScreenObject> screenObjectList;
    /**
     * Store the number of times clicked on the display objects, it will be reset to 0 after the round is played
     */
    private int countingNumber;
    //which is used to store the list of number which will be play
    private List<Integer> playNumberList;

    public CountingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int maximumNumberForCounting, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener, boolean isKeyboardRequired) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.maximumNumberForCounting = maximumNumberForCounting;
        this.isKeyboardRequired = isKeyboardRequired;


    }

    @Override
    public void render() {
        super.render();

        batch.begin();

        ScreenObjectUtils.draw(batch, numberScreenObject);

        ScreenObjectUtils.draw(batch, screenObjectList);

        batch.end();

    }

    @Override
    public boolean isKeyboardRequired() {
        return isKeyboardRequired;
    }

    @Override
    protected int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() {
        return maximumNumberForCounting;
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return TRASH_ICON_POSITION.y + TRASH_ICON_POSITION.height;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{MathImagePathUtils.APPLE}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        clear();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (isNumberScreenObjectRequired()) {
            AnimateTextureScreenObject touchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(screenObjectList, screenX, screenY);
            if (null != touchingScreenObject && 0 == touchingScreenObject.getNumber()) {
                countingNumber++;
                if (countingNumber < playingNumber) {
                    playNumberAudio(countingNumber);
                }
                touchingScreenObject.setNumber(countingNumber);
                touchingScreenObject.showNumber();
                if (countingNumber >= playingNumber) {
                    //if all object(s) are clicked, which mean the round is played
                    afterPlayCorrectly(countingNumber);
                    countingNumber = 0;
                }
            }
        }
    }

    @Override
    protected void afterNumberKeyboardSelected(int number) {
        if (isKeyboardRequired) {
            if (playingNumber == number) {
                afterPlayCorrectly();
            }
        }
    }

    protected void afterPlayCorrectly() {
        if (null != numberScreenObject) {
            numberScreenObject.isVisible = true;
        }
        playCorrectSound();
    }

    /**
     * mainly used to indicate if the apple need show the number after touch
     *
     * @return
     */
    protected boolean isNumberScreenObjectRequired() {
        return !isKeyboardRequired;
    }

    protected void afterPlayCorrectly(int pressedNumber) {
        if (null != numberScreenObject) {
            numberScreenObject.isVisible = true;
        }

        playNumberAudio(pressedNumber, new AbstractSoundPlayListListener() {
            @Override
            public void onComplete() {
                playCorrectSound();
            }
        });
    }

    private void playCorrectSound() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                numberOfRoundPlayed++;
                if (isSectionComplete()) {
                    //reset it, in order to let the user play again if the user enter the lesson again
                    //resetScreen();
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    nextRound();
                }
            }
        });
    }

    protected boolean isSectionComplete() {
        return numberOfRoundPlayed >= MAXIMUM_NUMBER_OF_ROUND_NEED_TO_PLAY;
    }

    private void nextRound() {

        if (isNumberScreenObjectRequired() && null == numberScreenObject) {
            float xPosition = ScreenUtils.getXPositionForCenterObject(LetterUtils.getTotalWidthOfWord(String.valueOf(countingNumber), TextFontSizeEnum.FONT_144));
            numberScreenObject = new TextScreenObject(null, null, String.valueOf(countingNumber), xPosition, NUMBER_TEXT_START_Y_POSITION, TextFontSizeEnum.FONT_144, true);
        }

        if (null == playNumberList) {
            playNumberList = new ArrayList<Integer>(maximumNumberForCounting * 2);
            for (int i = 1; i <= maximumNumberForCounting; i++) {
                //repeat this for all numbers within maximumNumberForCounting, twice each in random order
                playNumberList.add(i);
                playNumberList.add(i);
            }
            Collections.shuffle(playNumberList);
        }

        if (CollectionUtils.isNotEmpty(playNumberList)) {
            setPlayingNumber(playNumberList.get(0));
            playNumberList.remove(0);
        }

        //make sure it is calling the render thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                drawObjects();
            }
        }).start();
    }

    protected void setPlayingNumber(int playingNumber) {
        this.playingNumber = playingNumber;
        if (isNumberScreenObjectRequired()) {
            numberScreenObject.isVisible = false;
            numberScreenObject.setDisplayText(String.valueOf(playingNumber));
        }
    }

    private void drawObjects() {
        if (null == screenObjectList) {
            screenObjectList = new ArrayList<AnimateTextureScreenObject>();
        } else {
            screenObjectList.clear();
        }

        //This is store the start position for each object to move to the target position in animation
        TextureScreenObject sourceTextureScreenObject =
                new TextureScreenObject(null, null,
                        AssetManagerUtils.getTexture(getScreenObjectByRound(numberOfRoundPlayed)), ScreenUtils.getXPositionForCenterObject(OBJECT_SIZE.x),
                        ScreenUtils.getBottomYPositionForCenterObject(OBJECT_SIZE.y), OBJECT_SIZE.x, OBJECT_SIZE.y);


        Vector2[] randomPositions = RandomUtils.getRandomPositions(playingNumber, getObjectDrawWithinArea(), OBJECT_SIZE);

        if (ArrayUtils.isNotEmpty(randomPositions)) {
            for (int i = 0; i < randomPositions.length; i++) {

                screenObjectList.add(
                        new AnimateTextureScreenObject(0, sourceTextureScreenObject, randomPositions[i].x,
                                randomPositions[i].y,
                                OBJECT_SIZE.x, OBJECT_SIZE.y));
            }
        }
    }

    protected String getScreenObjectByRound(int currentRound) {
        return MathImagePathUtils.APPLE;
    }

    protected Rectangle getObjectDrawWithinArea() {
        float startYPosition = isKeyboardRequired ? getStartYPositionOfNumberKeyboard() + NUMBER_BLOCK_SIZE.y : 0;
        return new Rectangle(Config.SCREEN_CENTER_START_X_POSITION, startYPosition, Config.SCREEN_CENTER_WIDTH, NUMBER_TEXT_START_Y_POSITION - startYPosition);
    }

    private void clear() {
        numberScreenObject = null;
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }

        if (null != playNumberList) {
            playNumberList.clear();
            playNumberList = null;
        }
        numberOfRoundPlayed = 0;
        countingNumber = 0;
        playingNumber = 0;
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void resetScreen() {
        Gdx.app.log(getClass().getName(), "enter resetScreen");
        super.resetScreen();
        clear();
    }

    @Override
    protected void drawNumberInScreenCenter(int number) {
        super.drawNumberInScreenCenter(number);
    }

    @Override
    protected void playIntroductionAudio() {
        super.playIntroductionAudio();
    }

    @Override
    protected List<String> getIntroductionAudioFileName() {
        //TODO play the introduction screen
        return super.getIntroductionAudioFileName();
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();

        nextRound();
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        super.onNoIntroductionAudioPlay();
        nextRound();
    }

}
