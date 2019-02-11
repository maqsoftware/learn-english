package com.maqautocognita.section.Alphabet;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.screens.AbstractLetterScreen;
import com.maqautocognita.screens.AlphabetScreen;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ReviewUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetReviewMasteryReadingSection extends AbstractAutoCognitaSection implements IActivityChangeListener, ISoundPlayListener {


    private static final int KEYBOARD_PADDING_RIGHT = 6;
    private static final int KEYBOARD_PADDING_BOTTOM = 2;
    private static final int KEYBOARD_WIDTH = 104;
    private static final int KEYBOARD_HEIGHT = 108;

    private static final int SPACE_BETWEEN_KEYBOARD = 250;
    private static final int SPACE_BETWEEN_UPPER_AND_LOWER_CASE = 400;
    protected Texture keyboardTexture;
    protected List<TextureScreenObject<Character, ScreenObjectType>> screenObjectList;
    protected boolean isMasterTest;
    protected Character allLetters[];
    protected Activity selectedReview;
    private Character currentPlayingUppercaseLetters[];
    private Character currentPlayingLowercaseLetters[];
    /**
     * It will store the number of matched letter in the round, if the next round is play, the value will be reset to 0
     */
    private int numberOfMatchedLetter;
    private ScreenObject<Character, ScreenObjectType> pressedUpperCaseKey;
    private ScreenObject<Character, ScreenObjectType> pressedLowerCaseKey;
    private List<ScreenObject<Character, ScreenObjectType>> touchingKeyboardObjectList;

    public AlphabetReviewMasteryReadingSection(AbstractLetterScreen abstractLetterScreen) {
        super(abstractLetterScreen, abstractLetterScreen);
    }

    @Override
    public void setSelectedActivity(Activity review, boolean isMasterTest) {
        allLetters = isMasterTest ? ReviewUtils.getAllLetterForAlphabetMasteryTest() : ReviewUtils.getAllLettersInReview(review);

        if (ArrayUtils.isNotEmpty(allLetters)) {
            this.selectedReview = review;
            this.isMasterTest = isMasterTest;

            Collections.shuffle(Arrays.asList(allLetters));
            setPlayLetters();
        }


    }

    @Override
    protected float getExpectedScreenWidth() {
        if (ScreenUtils.isTablet) {
            return super.getExpectedScreenWidth();
        } else {
            return ScreenUtils.getScreenWidth() * 0.8f;
        }
    }

    protected void setPlayLetters() {

        //make sure the screen object for all showing letter are reinitialize
        screenObjectList = null;

        int numberOfShowingLetter;

        if (allLetters.length == 6) {
            //reach the max
            numberOfShowingLetter = 6;
        } else {
            numberOfShowingLetter = Math.min(isMasterTest ? 5 : 6, allLetters.length);
        }

        currentPlayingUppercaseLetters = new Character[numberOfShowingLetter];

        for (int i = 0; i < numberOfShowingLetter; i++) {
            //init all letter which is required to show in current screen
            currentPlayingUppercaseLetters[i] = Character.toUpperCase(allLetters[i]);
        }

        currentPlayingLowercaseLetters = new Character[currentPlayingUppercaseLetters.length];

        for (int i = 0; i < currentPlayingUppercaseLetters.length; i++) {
            //clone the upper case letter, suppose they are same, but in different case, it will be convert to lower later
            currentPlayingLowercaseLetters[i] = Character.toLowerCase(currentPlayingUppercaseLetters[i]);
        }


        //ramdom the position  fot eh lower case letter, make sure they are not in same y position below the upper case letter
        Collections.shuffle(Arrays.asList(currentPlayingLowercaseLetters));

        numberOfMatchedLetter = 0;
    }

    @Override
    protected float getExpectedScreenHeight() {
        if (ScreenUtils.isTablet) {
            return super.getExpectedScreenHeight();
        } else {
            return ScreenUtils.getScreenHeight() * 0.8f;
        }
    }

    @Override
    public void onPlay(int audioListIndex, long millisecond) {

    }

    @Override
    public void onComplete() {
        onMusicStop();
    }

    @Override
    public void onStop() {
        onMusicStop();
    }

    @Override
    public void render() {
        if (null == screenObjectList) {
            initScreenObjectList();
        }

        batch.begin();
        ScreenObjectUtils.draw(batch, screenObjectList);
        batch.end();
    }

    @Override
    public void onAudioFileMissing() {
        onMusicStop();
    }

    protected void initScreenObjectList() {
        if (null == keyboardTexture) {
            keyboardTexture = AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_KEYS);
        }

        if (null != currentPlayingUppercaseLetters && null != currentPlayingLowercaseLetters) {
            Character[] combined = new Character[currentPlayingUppercaseLetters.length + currentPlayingLowercaseLetters.length];

            for (int i = 0; i < combined.length; ++i) {
                combined[i] = i < currentPlayingUppercaseLetters.length ? currentPlayingUppercaseLetters[i] : currentPlayingLowercaseLetters[i - currentPlayingUppercaseLetters.length];
            }

            screenObjectList = new ArrayList<TextureScreenObject<Character, ScreenObjectType>>(combined.length);

            initKeyboardObjects(combined);
        }
    }

    @Override
    public void onWaveChanged(int level) {

    }

    protected void initKeyboardObjects(Character currentPlayingLetters[]) {


        int numberOfShowingLetterPerRow = currentPlayingLetters.length / 2;

        float startXPosition = 0;
        float startYPosition = 0;
        if (ScreenUtils.isLandscapeMode) {
            startXPosition =
                    ScreenUtils.getXPositionForCenterObject(KEYBOARD_WIDTH * numberOfShowingLetterPerRow + SPACE_BETWEEN_KEYBOARD *
                            (numberOfShowingLetterPerRow - 1), getExpectedScreenWidth());
            startYPosition =
                    ScreenUtils.getStartYPositionForCenterObject(KEYBOARD_HEIGHT * 2 + SPACE_BETWEEN_UPPER_AND_LOWER_CASE
                            , getExpectedScreenHeight()) - KEYBOARD_HEIGHT;
        } else {
            startXPosition = ScreenUtils.getXPositionForCenterObject(KEYBOARD_WIDTH * 2 + SPACE_BETWEEN_UPPER_AND_LOWER_CASE, getExpectedScreenWidth());
            startYPosition = ScreenUtils.getStartYPositionForCenterObject(KEYBOARD_HEIGHT *
                    numberOfShowingLetterPerRow + KEYBOARD_HEIGHT * (numberOfShowingLetterPerRow - 1), getExpectedScreenHeight()) - KEYBOARD_HEIGHT;
        }


        for (int i = 0; i < currentPlayingLetters.length; i++) {
            float letterXPosition = startXPosition;
            float letterYPosition = 0;

            if (ScreenUtils.isLandscapeMode) {
                letterXPosition +=
                        i % numberOfShowingLetterPerRow * SPACE_BETWEEN_KEYBOARD + i % numberOfShowingLetterPerRow * KEYBOARD_WIDTH;
                if (i < numberOfShowingLetterPerRow) {
                    letterYPosition = startYPosition;
                } else {
                    letterYPosition = startYPosition - SPACE_BETWEEN_UPPER_AND_LOWER_CASE - KEYBOARD_HEIGHT;
                }
            } else {
                if (i >= numberOfShowingLetterPerRow) {
                    letterXPosition += SPACE_BETWEEN_UPPER_AND_LOWER_CASE + KEYBOARD_WIDTH;
                }
                letterYPosition = startYPosition - i % numberOfShowingLetterPerRow * KEYBOARD_HEIGHT * 2;
            }


            char letter = currentPlayingLetters[i];

            screenObjectList.add(
                    new TextureScreenObject<Character, ScreenObjectType>(letter,
                            Character.isUpperCase(letter) ? ScreenObjectType.UPPERCASE : ScreenObjectType.LOWERCASE,
                            letterXPosition, letterYPosition,
                            Character.isUpperCase(letter) ? getLetterTextureRegionForUpperCase(letter, false) : getLetterTextureRegionForLowerCase(letter, false),
                            Character.isUpperCase(letter) ? getLetterTextureRegionForUpperCase(letter, true) : getLetterTextureRegionForLowerCase(letter, true)));

        }
    }

    @Override
    public boolean isWaveChangeListenerRequired() {
        return false;
    }

    private AutoCognitaTextureRegion getLetterTextureRegionForUpperCase(char letter, boolean isHighlightState) {
        return getLetterTextureRegion(letter, isHighlightState, false);
    }

    private void onMusicStop() {
        isHelpAudioPlaying = false;
        onHelpListener.onHelpComplete();
    }

    private AutoCognitaTextureRegion getLetterTextureRegionForLowerCase(char letter, boolean isHighlightState) {
        return getLetterTextureRegion(letter, isHighlightState, true);
    }

    protected AutoCognitaTextureRegion getLetterTextureRegion(char letter, boolean isHighlightState, boolean isLowerCase) {
        int position = Character.toLowerCase(letter) - 'a';

        int increaseY = isHighlightState ? (10 * KEYBOARD_HEIGHT) + (10 * KEYBOARD_PADDING_BOTTOM) : 0;

        increaseY += isLowerCase ? (3 * KEYBOARD_HEIGHT) + (3 * KEYBOARD_PADDING_BOTTOM) : 0;

        return new AutoCognitaTextureRegion(keyboardTexture, position % 10 * KEYBOARD_WIDTH + position % 10 * KEYBOARD_PADDING_RIGHT,
                position / 10 * KEYBOARD_HEIGHT + position / 10 * KEYBOARD_PADDING_BOTTOM + increaseY,
                KEYBOARD_WIDTH, KEYBOARD_HEIGHT);
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.GENERAL_KEYS};
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedReview;
    }

    @Override
    public void dispose() {
        super.dispose();
        keyboardTexture = null;
        pressedUpperCaseKey = null;
        pressedLowerCaseKey = null;
        reset();
    }

    @Override
    public void reset() {
        pressedUpperCaseKey = null;
        pressedLowerCaseKey = null;
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (CollectionUtils.isNotEmpty(touchingKeyboardObjectList)) {


            ScreenObject touchingKeyboardObject = ScreenObjectUtils.getTouchingScreenObject(touchingKeyboardObjectList, screenX, screenY);

            if (null != touchingKeyboardObject) {


                if (isCorrectMatch()) {
                    clearPressedLowerCase();
                    clearPressedUpperCase();
                } else {
                    if (null != pressedLowerCaseKey && null != pressedUpperCaseKey) {
                        //else if both upper and lower case are pressed but no match, here should clear all pressed key, prevent the user to touch both case again
                        clearPressedLowerCase();
                        clearPressedUpperCase();
                    }
                }

                if (null != touchingKeyboardObjectList) {
                    touchingKeyboardObjectList.remove(touchingKeyboardObject);
                }

            }
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        ScreenObject touchingKeyboardObject = ScreenObjectUtils.getTouchingScreenObject(screenObjectList, screenX, screenY);

        if (null != touchingKeyboardObject) {
            touchingKeyboardObject.isHighlighted = true;
            if (null == touchingKeyboardObjectList) {
                touchingKeyboardObjectList = new ArrayList<ScreenObject<Character, ScreenObjectType>>();
            }

            if (ScreenObjectType.UPPERCASE.equals(touchingKeyboardObject.objectType)) {

                boolean isSameKeyPressed = true;
                if (null == pressedUpperCaseKey) {
                    isSameKeyPressed = false;
                } else {
                    //if there already pressed the upper key before
                    if (!pressedUpperCaseKey.equals(touchingKeyboardObject)) {
                        //if the user is not press the same key again
                        isSameKeyPressed = false;
                    }
                    clearPressedUpperCase();
                }

                if (!isSameKeyPressed) {
                    //if the pressed key is not same to the previous key
                    pressedUpperCaseKey = touchingKeyboardObject;
                    pressedUpperCaseKey.isHighlighted = true;
                }

            } else {

                boolean isSameKeyPressed = true;
                if (null == pressedLowerCaseKey) {
                    isSameKeyPressed = false;
                } else {
                    //if there already pressed the upper key before
                    if (!pressedLowerCaseKey.equals(touchingKeyboardObject)) {
                        //if the user is not press the same key again
                        isSameKeyPressed = false;
                    }
                    clearPressedLowerCase();
                }

                if (!isSameKeyPressed) {
                    //if the pressed key is not same to the previous key
                    pressedLowerCaseKey = touchingKeyboardObject;
                    pressedLowerCaseKey.isHighlighted = true;
                }
            }

            touchingKeyboardObjectList.add(touchingKeyboardObject);
        }
    }

    private boolean isCorrectMatch() {
        if (null != pressedLowerCaseKey && null != pressedUpperCaseKey) {

            if (Character.toLowerCase(pressedLowerCaseKey.id) == (Character.toLowerCase(pressedUpperCaseKey.id))) {
                numberOfMatchedLetter++;
                removeLetter(pressedLowerCaseKey.id);
                screenObjectList.remove(pressedLowerCaseKey);
                screenObjectList.remove(pressedUpperCaseKey);
                pressedUpperCaseKey = null;
                pressedLowerCaseKey = null;
                abstractAutoCognitaScreen.playCorrectSound(new AlphabetScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {
                        //play next round
                        if (0 == allLetters.length) {
                            //which mean the review is already done
                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                        } else if (numberOfMatchedLetter == currentPlayingUppercaseLetters.length) {
                            //set next round letters
                            setPlayLetters();
                        }
                    }
                });
                return true;
            } else {
                addNumberOfFails();
                abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {

                    }
                });
                return false;
            }
        }

        return false;
    }

    private void clearPressedLowerCase() {
        if (null != pressedLowerCaseKey) {
            pressedLowerCaseKey.isHighlighted = false;
            pressedLowerCaseKey = null;
        }
    }

    private void clearPressedUpperCase() {
        if (null != pressedUpperCaseKey) {
            pressedUpperCaseKey.isHighlighted = false;
            pressedUpperCaseKey = null;
        }
    }

    private void removeLetter(Character toBeRemoveLetter) {
        List<Character> list = new ArrayList<Character>(Arrays.asList(allLetters));
        list.remove(toBeRemoveLetter);
        allLetters = list.toArray(new Character[]{});
    }


}
