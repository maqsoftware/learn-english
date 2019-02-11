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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingMatchingSection extends AbstractNumberKeyboardSection {

    private static final int GAP_BETWEEN_BORDER = 10;
    private static final int GAP_BETWEEN_BORDER_AND_NUMBER_KEYBOARD = 50;
    private final int maximumNumberOfObject;
    private List<RoundCornerRectangleScreenObject<Integer>> borderScreenObjectList;
    private List<TextureScreenObject> screenObjectList;

    //store the current selected border
    private RoundCornerRectangleScreenObject<Integer> selectedBorderScreenObject;

    public CountingMatchingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int maximumNumberOfObject, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.maximumNumberOfObject = maximumNumberOfObject;
    }

    @Override
    protected void render() {
        super.render();

        initScreenObject();

        batch.begin();
        ScreenObjectUtils.draw(batch, borderScreenObjectList);
        ScreenObjectUtils.draw(batch, screenObjectList);
        batch.end();
    }

    @Override
    protected int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() {
        return maximumNumberOfObject;
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return TRASH_ICON_POSITION.y + TRASH_ICON_POSITION.height - GAP_BETWEEN_BORDER_AND_NUMBER_KEYBOARD;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{MathImagePathUtils.BOAT}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        RoundCornerRectangleScreenObject touchingScreenObject = ScreenObjectUtils.getTouchingScreenObject(borderScreenObjectList, screenX, screenY);
        if (null != touchingScreenObject) {
            if (null != selectedBorderScreenObject) {
                //make the previous selected border to unhighlighted, make sure only 1 border can be selected
                selectedBorderScreenObject.isHighlighted = false;
            }
            selectedBorderScreenObject = touchingScreenObject;
            selectedBorderScreenObject.isHighlighted = true;
        }
    }

    protected void afterNumberKeyboardSelected(int number) {
        //check if the number block is selected correctly
        if (null != selectedBorderScreenObject) {
            //if the touched the correct number block
            if (selectedBorderScreenObject.id == number) {
                selectedBorderScreenObject.isDisabled = true;
                selectedBorderScreenObject.isTouchAllow = false;

                abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                    @Override
                    public void onCorrectSoundPlayed() {
                        if (isAllBorderDisabled()) {
                            //if all border are disabled,jump to next section
                            //correct sound will be played when jump to next section
                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                        } else {
                            for (RoundCornerRectangleScreenObject borderScreenObject : borderScreenObjectList) {
                                if (!borderScreenObject.isDisabled) {
                                    selectedBorderScreenObject = borderScreenObject;
                                    //highlight the next  border
                                    selectedBorderScreenObject.isHighlighted = true;
                                    break;
                                }
                            }
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
    }

    private boolean isAllBorderDisabled() {
        //check if all border are disabled
        for (RoundCornerRectangleScreenObject borderScreenObject : borderScreenObjectList) {
            if (!borderScreenObject.isDisabled) {
                return false;
            }
        }

        return true;
    }

    private void initScreenObject() {
        if (null == borderScreenObjectList) {


            //there will be 3 border in each row
            int borderWidth = (Config.SCREEN_CENTER_WIDTH - GAP_BETWEEN_BORDER * 2) / 3;

            //there will be three rows of the border
            int borderHeight = (int) (ScreenUtils.getNavigationBarStartYPosition() - (getStartYPositionOfNumberKeyboard() + NUMBER_BLOCK_SIZE.y + GAP_BETWEEN_BORDER_AND_NUMBER_KEYBOARD) - GAP_BETWEEN_BORDER * 2) / 3;

            float borderStartXPosition = Config.SCREEN_CENTER_START_X_POSITION;
            float borderStartYPosition = ScreenUtils.getNavigationBarStartYPosition() - borderHeight;

            borderScreenObjectList = new ArrayList<RoundCornerRectangleScreenObject<Integer>>(maximumNumberOfObject);

            Texture texture = AssetManagerUtils.getTexture(MathImagePathUtils.BOAT);

            float width = borderWidth / 7;

            Vector2 textureSize = new Vector2(width, texture.getHeight() * width / texture.getWidth());

            //random generate a list of number which is within MAXIMUM_NUMBER_OF_OBJECT in sequence
            List<Integer> numberList = new ArrayList<Integer>(maximumNumberOfObject);
            for (int i = 1; i <= maximumNumberOfObject; i++) {
                numberList.add(i);
            }
            //and the random the position of these number, we dont want to fixed the position
            Collections.shuffle(numberList);


            for (int i = 0; i < Math.min(maximumNumberOfObject, 9); i++) {
                int numberOfExistsBorderInRow = i % 3;
                int numberOfExistsBorderRow = i / 3;

                RoundCornerRectangleScreenObject border = new RoundCornerRectangleScreenObject(numberList.get(i), borderStartXPosition + numberOfExistsBorderInRow * GAP_BETWEEN_BORDER + numberOfExistsBorderInRow * borderWidth,
                        borderStartYPosition - numberOfExistsBorderRow * GAP_BETWEEN_BORDER - numberOfExistsBorderRow * borderHeight,
                        borderWidth, borderHeight, 5);

                if (i == 0) {
                    //The first group should be highlighted in red so the user knows where to begin
                    border.isHighlighted = true;
                    selectedBorderScreenObject = border;
                }

                borderScreenObjectList.add(border);

                Vector2[] randomPositions = RandomUtils.getRandomPositions(numberList.get(i), new Rectangle(border.xPositionInScreen, border.yPositionInScreen, border.width, border.height), textureSize);

                if (ArrayUtils.isNotEmpty(randomPositions)) {
                    for (Vector2 randomPosition : randomPositions) {
                        if (null == screenObjectList) {
                            screenObjectList = new ArrayList<TextureScreenObject>();
                        }

                        screenObjectList.add(new TextureScreenObject(texture,
                                randomPosition.x, randomPosition.y, textureSize.x, textureSize.y));
                    }
                }

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
        if (null != borderScreenObjectList) {
            for (RoundCornerRectangleScreenObject roundCornerRectangleScreenObject : borderScreenObjectList) {
                roundCornerRectangleScreenObject.dispose();
            }
            borderScreenObjectList.clear();
            borderScreenObjectList = null;
        }
        if (null != screenObjectList) {
            screenObjectList.clear();
            screenObjectList = null;
        }
        selectedBorderScreenObject = null;
    }

}
