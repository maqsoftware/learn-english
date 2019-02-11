package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * User is asked to make X groups with Y pencils in each group.  Then press the number corresponding to the total number of pencils.  Repeat with number pairs of X=2,Y=3; X=2,Y=4; X=3,Y=4
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class MultiplicationStorySection extends AbstractAdditionSubtractionStorySection {

    private static final int PADDING_BETWEEN_PLUS_ICON_AND_BOTTOM_RECTANGLE = 10;
    private static final int[] NUMBER_OF_GROUP_PAIR = new int[]{2, 2, 3};
    private static final int[] NUMBER_OF_OBJECT_ALLOW_IN_GROUP_PAIR = new int[]{3, 4, 4};
    private int numberOfBoatRequiredToAddInFirstTime;
    private int numberOfBoatRequiredToAddInSecondTime;
    private TextureScreenObject plusTextureScreenObject;
    private int playingIndex;
    private int maximumNumberOfObjectInRowInGroup;

    private List<TextureScreenObject> objectListDroppedOnTheScreen;

    public MultiplicationStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void render() {
        super.render();

        if (null == plusTextureScreenObject) {
            //draw the plus icon"+" in the top right corner
            Texture plusTexture = AssetManagerUtils.getTexture(MathImagePathUtils.PLUS);
            plusTextureScreenObject = new TextureScreenObject(plusTexture,
                    bottomRectangle.xPositionInScreen + bottomRectangle.width - plusTexture.getWidth() - PADDING_BETWEEN_PLUS_ICON_AND_BOTTOM_RECTANGLE,
                    bottomRectangle.yPositionInScreen + bottomRectangle.height - plusTexture.getHeight() - PADDING_BETWEEN_PLUS_ICON_AND_BOTTOM_RECTANGLE);
        }
        batch.begin();
        ScreenObjectUtils.draw(batch, plusTextureScreenObject);
        ScreenObjectUtils.draw(batch, objectListDroppedOnTheScreen);
        batch.end();

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{MathImagePathUtils.PLUS}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        plusTextureScreenObject = null;
    }

    @Override
    protected boolean isDragAllow() {
        return false;
    }

    @Override
    protected boolean isNumberKeyboardPressCorrectly(int number) {
        int totalNumberOfBoatRequiredToDrop = numberOfBoatRequiredToAddInFirstTime + numberOfBoatRequiredToAddInSecondTime;
        return CollectionUtils.isNotEmpty(dropObjectScreenObjectList) && dropObjectScreenObjectList.size() == totalNumberOfBoatRequiredToDrop &&
                number == totalNumberOfBoatRequiredToDrop;
    }

    @Override
    protected void beforePlayNextRound() {
        numberOfBoatRequiredToAddInFirstTime = 0;
        numberOfBoatRequiredToAddInSecondTime = 0;
        generateNumberOfBoatRequiredToDrop();
    }

    private void generateNumberOfBoatRequiredToDrop() {
        if (numberOfBoatRequiredToAddInFirstTime == 0) {
            numberOfBoatRequiredToAddInFirstTime = RandomUtils.getRandomWithExclusion(1, 9);
            numberOfBoatRequiredToAddInSecondTime = RandomUtils.getRandomWithExclusion(1, 10 - numberOfBoatRequiredToAddInFirstTime, numberOfBoatRequiredToAddInFirstTime);
        }
    }

    @Override
    protected void whenObjectIDroppedInTheBackground() {
        //duplicate the object
        dropObjectScreenObjectList.add(touchedDraggableScreenObject);
    }

    @Override
    protected boolean isNumberKeyboardRequired() {
        return false;
    }

    @Override
    protected String getScreenObjectImagePath() {
        return MathImagePathUtils.PENCIL;
    }

    @Override
    protected boolean isBackgroundRequired() {
        return false;
    }

    @Override
    protected String getBackgroundScreenObjectImagePath() {
        return null;
    }

    @Override
    protected Rectangle getAllowDropArea() {
        return null;
    }

    protected int getBottomRectangleWidth() {
        return (int) (objectScreenObject.width + PADDING_LEFT_RIGHT_IN_RECTANGLE * 3);
    }

    protected int getBottomRectangleHeight() {
        return (int) (objectScreenObject.height + PADDING_TOP_BOTTOM_IN_RECTANGLE * 3);
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        if (null != objectListDroppedOnTheScreen) {
            objectListDroppedOnTheScreen.clear();
        }
        maximumNumberOfObjectInRowInGroup = 0;
    }

    @Override
    protected void singleTap(int screenX, int screenY) {
        if (TouchUtils.isTouched(objectScreenObject, screenX, screenY) || TouchUtils.isTouched(plusTextureScreenObject, screenX, screenY)) {
            AnimateTextureScreenObject.IAnimationListener animationListener = null;
            if ((CollectionUtils.isNotEmpty(objectListDroppedOnTheScreen) ? objectListDroppedOnTheScreen.size() : 0) + 1 == getTotalNumberOfObjectRequiredToAdd()) {
                animationListener = new AnimateTextureScreenObject.IAnimationListener() {
                    @Override
                    public void onComplete() {
                        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                            @Override
                            public void onCorrectSoundPlayed() {
                                abstractAutoCognitaScreen.setTouchAllow(false);
                                abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getInstruction2ScriptAudioFileNameList(getNumberOfGroupPair()[playingIndex],
                                        getNumberOfObjectAllowInGroupPair()[playingIndex],
                                        getTotalNumberOfObjectRequiredToAdd(), getNumberOfGroupPair()[playingIndex], getNumberOfObjectAllowInGroupPair()[playingIndex],
                                        getTotalNumberOfObjectRequiredToAdd()), new AbstractSoundPlayListListener() {
                                    @Override
                                    public void onComplete() {
                                        super.onComplete();
                                        playingIndex++;
                                        abstractAutoCognitaScreen.setTouchAllow(true);
                                        if (playingIndex < NUMBER_OF_GROUP_PAIR.length) {
                                            resetScreen();
                                            onHelp();
                                        } else {
                                            abstractAutoCognitaScreen.showNextSection(numberOfFails);
                                        }
                                    }
                                });
                            }
                        });
                    }
                };
            }
            addObject(animationListener);
        }
        else{
            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {

                }
            });
        }
    }

    protected int getTotalNumberOfObjectRequiredToAdd() {
        int index = playingIndex;
        if (playingIndex >= getNumberOfGroupPair().length) {
            index = getNumberOfGroupPair().length - 1;
        }
        return getNumberOfGroupPair()[index] * getNumberOfObjectAllowInGroupPair()[index];
    }

    protected int[] getNumberOfGroupPair() {
        return NUMBER_OF_GROUP_PAIR;
    }

    protected int[] getNumberOfObjectAllowInGroupPair() {
        return NUMBER_OF_OBJECT_ALLOW_IN_GROUP_PAIR;
    }

    protected void addObject(AnimateTextureScreenObject.IAnimationListener animationListener) {
        //move the object
        if (null == objectListDroppedOnTheScreen) {
            objectListDroppedOnTheScreen = new ArrayList<TextureScreenObject>();
        }

        if (objectListDroppedOnTheScreen.size() < getTotalNumberOfObjectRequiredToAdd()) {

            final float groupWidth = Config.SCREEN_CENTER_WIDTH / getNumberOfGroupPair()[playingIndex];
            final float startGroupIndex = objectListDroppedOnTheScreen.size() / getNumberOfObjectAllowInGroupPair()[playingIndex];
            final float numberOfObjectInGroup = objectListDroppedOnTheScreen.size() % getNumberOfObjectAllowInGroupPair()[playingIndex];

            float destinationXPosition = Config.SCREEN_CENTER_START_X_POSITION + startGroupIndex * groupWidth + numberOfObjectInGroup * objectScreenObject.width;
            float destinationYPosition = ScreenUtils.getNavigationBarStartYPosition() - objectScreenObject.height * 2;

            if (maximumNumberOfObjectInRowInGroup == 0) {
                boolean isRetrieved = false;
                int numberOfObject = 1;
                while (!isRetrieved) {
                    if (numberOfObject * objectScreenObject.width > groupWidth) {
                        maximumNumberOfObjectInRowInGroup = numberOfObject - 1;
                        isRetrieved = true;
                    }
                    numberOfObject++;
                }
            }

            if (numberOfObjectInGroup + 1 > maximumNumberOfObjectInRowInGroup) {
                destinationXPosition = Config.SCREEN_CENTER_START_X_POSITION + startGroupIndex * groupWidth + (numberOfObjectInGroup - maximumNumberOfObjectInRowInGroup) * objectScreenObject.width;
                destinationYPosition -= objectScreenObject.height;
            }


            objectListDroppedOnTheScreen.add(duplicateAndMoveObject(destinationXPosition, destinationYPosition, 1000, animationListener));
        }
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        if (playingIndex < NUMBER_OF_GROUP_PAIR.length) {
            return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(getNumberOfGroupPair()[playingIndex], getNumberOfObjectAllowInGroupPair()[playingIndex]);
        }
        return null;
    }
}
