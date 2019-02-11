package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAdditionSubtractionStorySection extends AbstractNumberKeyboardSection {

    protected static final float BOTTOM_RECTANGLE_START_Y_POSITION = 40;
    protected static final float PADDING_TOP_BOTTOM_IN_RECTANGLE = 30;
    protected static final float PADDING_LEFT_RIGHT_IN_RECTANGLE = 50;
    private static final float GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND = 30;
    private final static int MAXIMUM_NUMBER_OF_ROUND_REQUIRED_TO_PLAY = 5;

    protected TextureScreenObject objectScreenObject;
    //store the screen object which is dropped in the background
    protected List<TextureScreenObject> dropObjectScreenObjectList;
    protected TextureScreenObject touchedDraggableScreenObject;
    protected TextureScreenObject backgroundScreenObject;
    /**
     * which is act as a background of the  {@link #objectScreenObject}
     */
    protected RoundCornerRectangleScreenObject bottomRectangle;
    private int numberOfRoundPlayed;

    /**
     * The area which allow to drop for the boat
     */
    private Rectangle allowDropArea;

    public AbstractAdditionSubtractionStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void render() {
        initStoryModeCountingScreenObject();
        batch.begin();
        ScreenObjectUtils.draw(batch, backgroundScreenObject);
        ScreenObjectUtils.draw(batch, bottomRectangle);
        ScreenObjectUtils.draw(batch, objectScreenObject);
        ScreenObjectUtils.draw(batch, dropObjectScreenObjectList);
        ScreenObjectUtils.draw(batch, touchedDraggableScreenObject);
        batch.end();

        if (isNumberKeyboardRequired()) {
            super.render();
        }
    }

    @Override
    protected int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() {
        return 10;
    }

    @Override
    protected float getStartXPositionOfNumberKeyboard(float totalWidthOfNumberKeyboard) {
        return bottomRectangle.xPositionInScreen + bottomRectangle.width + GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND;
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return bottomRectangle.yPositionInScreen + (bottomRectangle.height - NUMBER_BLOCK_SIZE.y) / 2;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{getBackgroundScreenObjectImagePath(), getScreenObjectImagePath()}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundScreenObject = null;
        objectScreenObject = null;
        if (null != bottomRectangle) {
            bottomRectangle.dispose();
            bottomRectangle = null;
        }
        resetScreen();
        numberOfRoundPlayed = 0;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != touchedDraggableScreenObject) {

            boolean isAllowDrop = false;

            if (TouchUtils.isTouched(allowDropArea, screenX, screenY)) {
                //if the dragging object is allow drop into the area, set original position to current position
                touchedDraggableScreenObject.setOriginalPositionToCurrent();
                isAllowDrop = true;
            }

            if (isAllowDrop) {
                //if allow drop, add it to the dropped list
                if (null == dropObjectScreenObjectList) {
                    dropObjectScreenObjectList = new ArrayList<TextureScreenObject>();
                }

                if (!dropObjectScreenObjectList.contains(touchedDraggableScreenObject)) {
                    //if the object which is not include in the list, which mean it is dragged from the bottom bar
                    whenObjectIDroppedInTheBackground();
                }
            } else {
                //check if the object is dropped outside
                if (TouchUtils.isTouched(backgroundScreenObject, screenX, screenY)) {
                    //roll back to the original position if the object is still in the background
                    touchedDraggableScreenObject.rollbackToOriginalPosition();
                } else {
                    if (CollectionUtils.isNotEmpty(dropObjectScreenObjectList)
                            && dropObjectScreenObjectList.contains(touchedDraggableScreenObject)) {
                        if (isAllowDropToOutside()) {
                            dropObjectScreenObjectList.remove(touchedDraggableScreenObject);
                            whenObjectIDroppedOutsideTheBackground();
                        } else {
                            touchedDraggableScreenObject.rollbackToOriginalPosition();
                        }
                    }
                }
            }

            touchedDraggableScreenObject = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (isDragAllow()) {
            TextureScreenObject touchingTextureScreenObject = ScreenObjectUtils.getTouchingScreenObject(objectScreenObject, screenX, screenY);
            if (null != touchingTextureScreenObject) {
                //duplicate the object, because the boat in the bottom bar is required to keep in the position and not allow drag
                touchedDraggableScreenObject = new TextureScreenObject(touchingTextureScreenObject.id, touchingTextureScreenObject.getTexture(),
                        touchingTextureScreenObject.xPositionInScreen, touchingTextureScreenObject.yPositionInScreen);
            } else {
                //check if the user is dragging the boat which is already dropped on the background image
                TextureScreenObject touchingDroppableTextureScreenObject = ScreenObjectUtils.getTouchingScreenObject(dropObjectScreenObjectList, screenX, screenY);
                if (null != touchingDroppableTextureScreenObject) {
                    touchedDraggableScreenObject = touchingDroppableTextureScreenObject;
                }
            }

            if (null != touchedDraggableScreenObject) {
                touchedDraggableScreenObject.touchingPosition(screenX, screenY);
            }
        }
    }

    protected boolean isDragAllow() {
        return true;
    }

    @Override
    protected void afterNumberKeyboardSelected(int number) {
        if (isNumberKeyboardPressCorrectly(number)) {

            abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {
                    numberOfRoundPlayed++;
                    if (MAXIMUM_NUMBER_OF_ROUND_REQUIRED_TO_PLAY == numberOfRoundPlayed) {
                        abstractAutoCognitaScreen.showNextSection(numberOfFails);
                    } else {
                        nextRound();
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

    protected abstract boolean isNumberKeyboardPressCorrectly(int number);

    private void nextRound() {
        resetScreen();
        beforePlayNextRound();
        onHelp();
    }

    protected abstract void beforePlayNextRound();

    protected void whenObjectIDroppedInTheBackground() {

    }

    protected boolean isAllowDropToOutside() {
        return true;
    }

    protected void whenObjectIDroppedOutsideTheBackground() {

    }

    private void initStoryModeCountingScreenObject() {

        float startYPosition = BOTTOM_RECTANGLE_START_Y_POSITION + PADDING_TOP_BOTTOM_IN_RECTANGLE;

        if (isObjectRequiredToDrag()) {

            if (null == objectScreenObject) {
                //init  screen object
                objectScreenObject = new TextureScreenObject(AssetManagerUtils.getTexture(getScreenObjectImagePath()),
                        Config.SCREEN_CENTER_START_X_POSITION + PADDING_LEFT_RIGHT_IN_RECTANGLE,
                        startYPosition);
            }

            if (null == bottomRectangle) {
                //init the bottom rectangle
                bottomRectangle = getBottomRectangle();
            }
        }

        if (null == backgroundScreenObject && isBackgroundRequired()) {
            Texture backgroundTexture = AssetManagerUtils.getTexture(getBackgroundScreenObjectImagePath());
            float yPosition = 0;
            if (null == bottomRectangle) {
                yPosition = getStartYPositionOfNumberKeyboard() + NUMBER_BLOCK_SIZE.y + GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND;
            } else {
                yPosition = bottomRectangle.yPositionInScreen + bottomRectangle.height + GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND;
            }

            if (null != backgroundTexture) {
                backgroundScreenObject =
                        new TextureScreenObject(backgroundTexture,
                                Config.SCREEN_CENTER_START_X_POSITION, yPosition
                        );
            }

            allowDropArea = getAllowDropArea();
        }
    }

    protected boolean isNumberKeyboardRequired() {
        return true;
    }

    protected boolean isObjectRequiredToDrag() {
        return true;
    }

    protected abstract String getScreenObjectImagePath();

    private RoundCornerRectangleScreenObject getBottomRectangle() {
        return new RoundCornerRectangleScreenObject(Config.SCREEN_CENTER_START_X_POSITION, BOTTOM_RECTANGLE_START_Y_POSITION,
                getBottomRectangleWidth(),
                getBottomRectangleHeight(), ColorProperties.OBJECT_CONTAINER);
    }

    protected boolean isBackgroundRequired() {
        return true;
    }

    protected String getBackgroundScreenObjectImagePath() {
        return MathImagePathUtils.SEA_BACKGROUND_IMAGE_PATH;
    }

    protected abstract Rectangle getAllowDropArea();

    protected int getBottomRectangleWidth() {
        return (int) (objectScreenObject.width + PADDING_LEFT_RIGHT_IN_RECTANGLE * 2);
    }

    protected int getBottomRectangleHeight() {
        return (int) (objectScreenObject.height + PADDING_TOP_BOTTOM_IN_RECTANGLE * 2);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        resetScreen();
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != touchedDraggableScreenObject && isDragAllow()) {
            touchedDraggableScreenObject.dragPosition(screenX, screenY);
        }
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        touchedDraggableScreenObject = null;
        if (null != dropObjectScreenObjectList) {
            dropObjectScreenObjectList.clear();
            dropObjectScreenObjectList = null;
        }
    }

    protected TextureScreenObject duplicateAndMoveObject(final float destinationXPosition, final float destinationYPosition, final int numberOfMillisecondForAnimation) {
        return duplicateAndMoveObject(destinationXPosition, destinationYPosition, numberOfMillisecondForAnimation, null);
    }

    protected TextureScreenObject duplicateAndMoveObject(final float destinationXPosition, final float destinationYPosition, final int numberOfMillisecondForAnimation,
                                                         final AnimateTextureScreenObject.IAnimationListener animationListener) {
        final TextureScreenObject textureScreenObject = new TextureScreenObject(objectScreenObject.getTexture(), objectScreenObject.xPositionInScreen, objectScreenObject.yPositionInScreen);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long millisecond = 0;
                boolean isExecute = true;
                while (isExecute) {
                    try {
                        Thread.sleep(1);
                        millisecond++;
                        moveObject(textureScreenObject, destinationXPosition, destinationYPosition, millisecond, numberOfMillisecondForAnimation);
                        if (millisecond == numberOfMillisecondForAnimation) {
                            isExecute = false;
                            if (null != animationListener) {
                                animationListener.onComplete();
                            }
                        }
                    } catch (InterruptedException e) {
                        Gdx.app.log(getClass().getName(), "", e);
                    }
                }

            }
        }).start();
        return textureScreenObject;
    }

    private void moveObject(TextureScreenObject textureScreenObject, float destinationXPosition, float destinationYPosition, long millisecond, int numberOfMillisecondForAnimation) {
        if (null != objectScreenObject) {
            float startXPosition = objectScreenObject.xPositionInScreen;
            float startYPosition = objectScreenObject.yPositionInScreen;
            moveObject(textureScreenObject, startXPosition, startYPosition, destinationXPosition, destinationYPosition, millisecond, numberOfMillisecondForAnimation);
        }
    }

    private void moveObject(TextureScreenObject moveObject, float startXPosition, float startYPosition, float destinationXPosition, float destinationYPosition, long millisecond, int numberOfMillisecondForAnimation) {
        moveObject.xPositionInScreen = startXPosition + (destinationXPosition - startXPosition) * millisecond / numberOfMillisecondForAnimation;
        moveObject.yPositionInScreen = startYPosition + (destinationYPosition - startYPosition) * millisecond / numberOfMillisecondForAnimation;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        nextRound();
    }


    @Override
    protected void onNoIntroductionAudioPlay() {
        onIntroductionAudioPlayed();
    }


}
