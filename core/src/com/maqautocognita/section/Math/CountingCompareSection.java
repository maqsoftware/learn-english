package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingCompareSection extends AbstractNumberKeyboardSection {

    private final static int NUMBER_OF_ROUND_REQUIRED_TO_PLAY = 5;
    /**
     * the maximum number of object display in the border
     */
    private static final int MAXIMUM_NUMBER_DISPLAY_IN_THE_BORDER = 10;
    private static final int MAXIMUM_DIFFERENT_BETWEEN_LEFT_AND_RIGHT = 3;
    /**
     * Tbe gap between 2 borders
     */
    private static final int GAP_BETWEEN_BORDERS = 60;
    /**
     * Tbe gap between 2 border and number keyboard in the bottom of the screen
     */
    private static final int GAP_BETWEEN_BORDER_AND_NUMBER_KEYBOARD = 120;
    private int numberOfPlayedRound;
    private RoundCornerRectangleScreenObject<Integer> leftBorderScreenObject;
    private RoundCornerRectangleScreenObject<Integer> rightBorderScreenObject;

    private boolean isMore;

    /**
     * This is a flag to indicate if the group in left or right is selected correctly, the usser is required to select the number keyboard after the group is selected
     */
    private boolean isGroupSelected;

    private List<TextureScreenObject> screenObjectList;

    /**
     * stoire the correct border screen object which is required to select by the user
     */
    private RoundCornerRectangleScreenObject<Integer> correctBorderScreenObject;

    public CountingCompareSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener, boolean isMore) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.isMore = isMore;
    }

    @Override
    protected void render() {
        super.render();
        initScreenObject();
        batch.begin();
        ScreenObjectUtils.draw(batch, leftBorderScreenObject);
        ScreenObjectUtils.draw(batch, rightBorderScreenObject);
        ScreenObjectUtils.draw(batch, screenObjectList);
        batch.end();
    }

    @Override
    protected int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() {
        return 10;
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return TRASH_ICON_POSITION.y + TRASH_ICON_POSITION.height;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{MathImagePathUtils.BOAT}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != leftBorderScreenObject) {
            leftBorderScreenObject.dispose();
            leftBorderScreenObject = null;
        }
        if (null != rightBorderScreenObject) {
            rightBorderScreenObject.dispose();
            rightBorderScreenObject = null;
        }
        if (null != correctBorderScreenObject) {
            correctBorderScreenObject.dispose();
            correctBorderScreenObject = null;
        }
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }

        resetScreen();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        //check which side is more or less
        if (!isGroupSelected) {
            //check if border selected
            if (TouchUtils.isTouched(correctBorderScreenObject, screenX, screenY)) {
                abstractAutoCognitaScreen.playCorrectSound();
                isGroupSelected = true;
                correctBorderScreenObject.isHighlighted = true;
                onHelp();
            }
        }
    }

    @Override
    protected void afterNumberKeyboardSelected(int number) {
        if (isGroupSelected && Math.abs(leftBorderScreenObject.id - rightBorderScreenObject.id) == number) {
            abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {
                    numberOfPlayedRound++;
                    if (numberOfPlayedRound >= NUMBER_OF_ROUND_REQUIRED_TO_PLAY) {
                        abstractAutoCognitaScreen.showNextSection(numberOfFails);
                    } else {
                        resetScreen();
                        onHelp();
                    }
                }
            });
        }
        else{
            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {

                }
            });
        }
    }

    private void initScreenObject() {
        if (null == leftBorderScreenObject) {
            float borderStartYPosition = getStartYPositionOfNumberKeyboard() + NUMBER_BLOCK_SIZE.y + GAP_BETWEEN_BORDER_AND_NUMBER_KEYBOARD;

            int borderWidth = (Config.SCREEN_CENTER_WIDTH - GAP_BETWEEN_BORDERS) / 2;

            int borderHeight = (int) (ScreenUtils.getNavigationBarStartYPosition() - borderStartYPosition);

            Rectangle leftBorderArea = new Rectangle(Config.SCREEN_CENTER_START_X_POSITION, borderStartYPosition, borderWidth, borderHeight);
            leftBorderScreenObject = new RoundCornerRectangleScreenObject(null,
                    leftBorderArea.x, leftBorderArea.y, (int) leftBorderArea.width, (int) leftBorderArea.height, 5);

            Rectangle rightBorderArea = new Rectangle(Config.SCREEN_CENTER_START_X_POSITION + borderWidth + GAP_BETWEEN_BORDERS,
                    borderStartYPosition, borderWidth, borderHeight);
            rightBorderScreenObject =
                    new RoundCornerRectangleScreenObject(null,
                            rightBorderArea.x, rightBorderArea.y, (int) rightBorderArea.width, (int) rightBorderArea.height, 5);

            nextRound();
        }
    }

    private void nextRound() {
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }
        int leftNumberOfObject = RandomUtils.getRandomWithExclusion(1, MAXIMUM_NUMBER_DISPLAY_IN_THE_BORDER);
        leftBorderScreenObject.id = leftNumberOfObject;
        int rightNumberOfObject;
        if (new Random().nextBoolean()) {
            rightNumberOfObject = RandomUtils.getRandomWithExclusion(leftNumberOfObject + 1, leftNumberOfObject + MAXIMUM_DIFFERENT_BETWEEN_LEFT_AND_RIGHT);
        } else {
            rightNumberOfObject = RandomUtils.getRandomWithExclusion(leftNumberOfObject - MAXIMUM_DIFFERENT_BETWEEN_LEFT_AND_RIGHT, leftNumberOfObject - 1);
        }
        rightBorderScreenObject.id = rightNumberOfObject;

        Texture objectTexture = AssetManagerUtils.getTexture(MathImagePathUtils.BOAT);
        float objectWidth = leftBorderScreenObject.width / 7;
        Vector2 textureSize = new Vector2(objectWidth, objectTexture.getHeight() * objectWidth / objectTexture.getWidth());

        //init left border
        initObjectsInBorder(leftNumberOfObject, new Rectangle(leftBorderScreenObject.xPositionInScreen, leftBorderScreenObject.yPositionInScreen,
                leftBorderScreenObject.width, leftBorderScreenObject.height), textureSize, objectTexture);
        initObjectsInBorder(rightNumberOfObject, new Rectangle(rightBorderScreenObject.xPositionInScreen, rightBorderScreenObject.yPositionInScreen,
                rightBorderScreenObject.width, rightBorderScreenObject.height), textureSize, objectTexture);

        if (isMore) {
            if (leftNumberOfObject > rightNumberOfObject) {
                correctBorderScreenObject = leftBorderScreenObject;
            } else {
                correctBorderScreenObject = rightBorderScreenObject;
            }
        } else {
            if (leftNumberOfObject < rightNumberOfObject) {
                correctBorderScreenObject = leftBorderScreenObject;
            } else {
                correctBorderScreenObject = rightBorderScreenObject;
            }
        }
    }

    private void initObjectsInBorder(int numberOfObject, Rectangle borderArea, Vector2 objectSize, Texture objectTexture) {
        Vector2[] randomPositions =
                RandomUtils.getRandomPositions(numberOfObject, borderArea, objectSize);

        if (ArrayUtils.isNotEmpty(randomPositions)) {
            for (Vector2 randomPosition : randomPositions) {
                if (null == screenObjectList) {
                    screenObjectList = new ArrayList<TextureScreenObject>();
                }

                screenObjectList.add(new TextureScreenObject(objectTexture,
                        randomPosition.x, randomPosition.y, objectSize.x, objectSize.y));
            }
        }
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        if (numberOfPlayedRound >= NUMBER_OF_ROUND_REQUIRED_TO_PLAY) {
            numberOfPlayedRound = 0;
        }
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void resetScreen() {
        correctBorderScreenObject = null;
        isGroupSelected = false;
        if (null != leftBorderScreenObject) {
            leftBorderScreenObject.isHighlighted = false;
        }
        if (null != rightBorderScreenObject) {
            rightBorderScreenObject.isHighlighted = false;
        }
        if (null != leftBorderScreenObject && null != rightBorderScreenObject) {
            nextRound();
        }
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected void onIntroductionAudioStopped() {
        super.onIntroductionAudioStopped();
        onHelp();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        if (isGroupSelected) {
            return mathAudioScriptWithElementCode.instructionScript2AudioFileNameList;
        } else {
            return mathAudioScriptWithElementCode.instructionScriptAudioFileNameList;
        }
    }


}
