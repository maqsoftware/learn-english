package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


/**
 * Start with 0.  Play introduction script.
 * <p>
 * After that, show the +1 apple icon.  User presses that.
 * Apple turns from opaque to solid and becomes larger while moving to the correct position.
 * Pressing the number plays the sound of the number.
 * <p>
 * 1 apple is in the correct position.   The white number 1 label shows up.  Play script.
 * <p>
 * After that, white label disappears.  Show the +1 apple icon again.
 * <p>
 * Same pattern continues.  With each number,
 * first show the white labels on the apples, then white label disappears.
 * Then the +1 apple icon shows up.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingAddAppleSection extends AbstractMathSection {


    private static final int NUMBER_OF_OBJECTS_PER_ROW = 5;
    private static final String ADD_ONE_BUTTON_PICTURE_PATH = Config.LESSON_IMAGE_FOLDER_NAME + "/apple.png";
    private static final Vector2 ADD_ONE_BUTTON_POSITION = new Vector2(1600, 828);
    /**
     * The gap (px) between the objects store in {@link #screenObjectList}
     */
    private static final int GAP_BETWEEN_OBJECT = 100;
    private static final int OBJECT_START_Y_POSITION = 500;
    private static final Vector2 OBJECT_SIZE = new Vector2(200, 200);
    /**
     * Store the maximum number of object will shown in the screen after the user click the +1 button
     */
    private final int maximumNumberOfObjects;
    private TextScreenObject numberScreenObject;
    private List<ScreenObject> buttonScreenObjectList;
    /**
     * Mainly store the object which is added by click the add one button
     */
    private List<AnimateTextureScreenObject> screenObjectList;
    private TextureScreenObject addOneButton;

    private int countingNumber;


    public CountingAddAppleSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int maximumNumberOfObjects, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.maximumNumberOfObjects = maximumNumberOfObjects;
    }

    @Override
    public void render() {

        super.render();

        initButton();

        batch.begin();

        ScreenObjectUtils.draw(batch, numberScreenObject);

        ScreenObjectUtils.draw(batch, buttonScreenObjectList);

        ScreenObjectUtils.draw(batch, screenObjectList);

        batch.end();

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{ADD_ONE_BUTTON_PICTURE_PATH}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
    }

    @Override
    protected void singleTap(int screenX, int screenY) {
        if (null != ScreenObjectUtils.getTouchingScreenObject(buttonScreenObjectList, screenX, screenY)) {
            //if add one button is tapped

            //invisible the add one button
            hideScreenObjectList(buttonScreenObjectList);
            hideNumberInObject();

            if (null == screenObjectList) {
                screenObjectList = new ArrayList<AnimateTextureScreenObject>();
            }

            if (countingNumber < maximumNumberOfObjects) {
                //make sure only maximumNumberOfObjects object can be add into the screen
                countingNumber++;

                if (countingNumber < maximumNumberOfObjects) {
                    //the last number will be play later
                    playNumberAudio(countingNumber);
                }

                numberScreenObject.setDisplayText(String.valueOf(countingNumber));

                int indexInTheRow = countingNumber > NUMBER_OF_OBJECTS_PER_ROW ? countingNumber - NUMBER_OF_OBJECTS_PER_ROW : countingNumber;

                final float startXPosition = ScreenUtils.getXPositionForCenterObject(
                        //total width of the objects
                        indexInTheRow * OBJECT_SIZE.x +
                                //total gap between each object
                                (indexInTheRow - 1) * GAP_BETWEEN_OBJECT
                );

                float xPosition = startXPosition;
                //re-position the previous object list
                for (int i = 0; i < screenObjectList.size(); i++) {
                    //if the number object is in the first line
                    if (countingNumber <= NUMBER_OF_OBJECTS_PER_ROW ||
                            //or if the number object is in the next line
                            i >= NUMBER_OF_OBJECTS_PER_ROW) {
                        AnimateTextureScreenObject screenObject = screenObjectList.get(i);
                        screenObject.setTargetPosition(xPosition, screenObject.yPositionInScreen);
                        xPosition += screenObject.width + GAP_BETWEEN_OBJECT;
                    }
                }

                AnimateTextureScreenObject animateTextureScreenObject = new AnimateTextureScreenObject(countingNumber, addOneButton, xPosition,
                        //if the number of object is larger the maximum of the row, append the object to next row
                        (countingNumber <= NUMBER_OF_OBJECTS_PER_ROW ? OBJECT_START_Y_POSITION : OBJECT_START_Y_POSITION - OBJECT_SIZE.y - GAP_BETWEEN_OBJECT),
                        OBJECT_SIZE.x, OBJECT_SIZE.y);

                animateTextureScreenObject.setAnimationListener(new AnimateTextureScreenObject.IAnimationListener() {
                    @Override
                    public void onComplete() {

                        //TODO play the script and hide the number after the script and show the add number button
                        //show the number which is located in the center of the object
                        showNumberInObject();

                        if (countingNumber < maximumNumberOfObjects) {
                            //make the add one button visible after animation
                            showScreenObjectList(buttonScreenObjectList);

                            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                @Override
                                public void onCorrectSoundPlayed() {

                                }
                            });
                        } else {

                            playNumberAudio(countingNumber, new AbstractSoundPlayListListener() {
                                @Override
                                public void onComplete() {
                                    //which mean maximumNumberOfObjects objects are shown already, play next section
                                    abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                        @Override
                                        public void onCorrectSoundPlayed() {
                                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                                        }
                                    });
                                }
                            });

                        }

                    }
                });
                screenObjectList.add(animateTextureScreenObject);

            }
        }

    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        countingNumber = 0;
        numberScreenObject = null;
        if (null != buttonScreenObjectList) {
            buttonScreenObjectList.clear();
            buttonScreenObjectList = null;
        }

        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }

        if (null != addOneButton) {
            addOneButton = null;
        }

    }

    private <S extends ScreenObject> void hideScreenObjectList(List<S> screenObjectList) {
        for (S screenObject : screenObjectList) {
            screenObject.isVisible = false;
        }
    }

    private void hideNumberInObject() {
        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            //show the number which is located in the center of the object
            for (AnimateTextureScreenObject screenObject : screenObjectList) {
                screenObject.hideNumber();
            }
        }
    }

    private void showNumberInObject() {
        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            //show the number which is located in the center of the object
            for (AnimateTextureScreenObject screenObject : screenObjectList) {
                screenObject.showNumber();
            }
        }
    }

    private <S extends ScreenObject> void showScreenObjectList(List<S> screenObjectList) {
        for (S screenObject : screenObjectList) {
            screenObject.isVisible = true;
        }
    }

    private void initButton() {

        if (null == addOneButton) {

            addOneButton = new TextureScreenObject(null, ScreenObjectType.BUTTON, AssetManagerUtils.getTexture(ADD_ONE_BUTTON_PICTURE_PATH), ADD_ONE_BUTTON_POSITION.x, ADD_ONE_BUTTON_POSITION.y, 100, 102);

            addOneButton.setAlpha(0.5f);

            if (null == buttonScreenObjectList) {
                buttonScreenObjectList = new ArrayList<ScreenObject>();
            }

            buttonScreenObjectList.add(addOneButton);

            //add the text in the add one button
            buttonScreenObjectList.add(new TextScreenObject(null, ScreenObjectType.BUTTON,
                    "+1", ADD_ONE_BUTTON_POSITION.x, ADD_ONE_BUTTON_POSITION.y
                    //the margin button to the add one button
                    + 15,
                    TextFontSizeEnum.FONT_72, true));
        }

        if (null == numberScreenObject) {
            float xPosition = ScreenUtils.getXPositionForCenterObject(LetterUtils.getTotalWidthOfWord(String.valueOf(countingNumber), TextFontSizeEnum.FONT_144));
            numberScreenObject = new TextScreenObject(null, null, String.valueOf(countingNumber), xPosition, ADD_ONE_BUTTON_POSITION.y, TextFontSizeEnum.FONT_144, true);
        }
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

}
