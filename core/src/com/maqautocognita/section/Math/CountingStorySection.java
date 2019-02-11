package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
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
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Screen shows scenery background.
 * <p/>
 * User are required to put <number> ducks on the lake. or
 * Put <number> bird in the sky. or  Put <number> zebras on the land."
 * User drags objects to the right position.  If correct, move on.  Repeat five times.
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingStorySection extends AbstractMathSection {

    private static final float BOTTOM_RECTANGLE_START_Y_POSITION = 40;
    private static final float PADDING_TOP_BOTTOM_IN_RECTANGLE = 30;
    private static final float PADDING_LEFT_RIGHT_IN_RECTANGLE = 50;
    private static final float GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND = 30;
    private static final int NUMBER_OF_TIME_REQUIRED_TO_PLAY = 5;
    private static final int OBJECT_MAXIMUM_HEIGHT = 200;
    protected TextureScreenObject backgroundScreenObject;
    private RoundCornerRectangleScreenObject bottomRectangle;
    //store the screen object which is displaying in the bottom grey bar
    private List<TextureScreenObject<Integer, Object>> dragObjectScreenObjectList;
    //store the screen object which is dropped in the background
    private List<TextureScreenObject<Integer, Object>> dropObjectScreenObjectList;
    private TextureScreenObject<Integer, Object> touchedDraggableScreenObject;
    //which is used to store the list of number which will be play
    private List<Integer> playNumberList;
    private List<Integer> playObjectList;
    //it is the index of the playNumberList,  used to get the current playing numbers
    private int currentPlayingIndex;
    /**
     * This is the random number of object required to drop, it will be init {@link #initStoryModeCountingScreenObject()}
     */
    private int numberOfObjectRequiredToDrop;

    /**
     * the index of the audio in {@link MathAudioScriptWithElementCode#instructionScriptAudioFileNameList} for playing the instruction of the drop object
     * 2 = ducks on the lake.
     * 3 = birds in the sky.
     * 4 = zebras on the land.
     */
    private int audioIndexForTheDropObject;

    /**
     * This is the random value to ask user to drag specified object, it will be init {@link #initStoryModeCountingScreenObject()}
     */
    private int objectRequiredToDropId;

    /**
     * The area which allow to drop for first,second and third object
     */
    private Rectangle allowDropFirstArea, allowDropSecondArea, allowDropThirdArea;

    public CountingStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumNumberOfObjectRequiredToDrop, int maximumNumberOfObjectRequiredToDrop, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        if (null == playNumberList) {
            playNumberList = new ArrayList<Integer>(NUMBER_OF_TIME_REQUIRED_TO_PLAY);
            for (int i = minimumNumberOfObjectRequiredToDrop; i <= maximumNumberOfObjectRequiredToDrop; i++) {
                playNumberList.add(i);
            }
        }

        //make sure each object at least has 1 chance to play
        playObjectList = new ArrayList<Integer>(NUMBER_OF_TIME_REQUIRED_TO_PLAY);
        for (int i = 1; i <= NUMBER_OF_TIME_REQUIRED_TO_PLAY; i++) {
            if (i % 3 == 1) {
                playObjectList.add(getFirstAllowDragObjectId());
            } else if (i % 3 == 2) {
                playObjectList.add(getSecondAllowDragObjectId());
            } else if (i % 3 == 0) {
                playObjectList.add(getThirdAllowDragObjectId());
            }
        }
    }

    protected int getFirstAllowDragObjectId() {
        return 1;
    }

    protected int getSecondAllowDragObjectId() {
        return 2;
    }

    protected int getThirdAllowDragObjectId() {
        return 3;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        initStoryModeCountingScreenObject();
        Collections.shuffle(playObjectList);
        reRandomPlayingNumberList();
        nextRound();
    }

    @Override
    protected void render() {
        batch.begin();
        ScreenObjectUtils.draw(batch, backgroundScreenObject);
        ScreenObjectUtils.draw(batch, bottomRectangle);
        ScreenObjectUtils.draw(batch, dragObjectScreenObjectList);
        ScreenObjectUtils.draw(batch, dropObjectScreenObjectList);
        ScreenObjectUtils.draw(batch, touchedDraggableScreenObject);
        batch.end();
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(getAllRequiredTextureNameForStoryMode(), super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundScreenObject = null;
        if (null != bottomRectangle) {
            bottomRectangle.dispose();
            bottomRectangle = null;
        }
        resetScreen();


    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != touchedDraggableScreenObject) {
            touchedDraggableScreenObject.dragPosition(screenX, screenY);
        }
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        if (null != dragObjectScreenObjectList) {
            dragObjectScreenObjectList.clear();
            dragObjectScreenObjectList = null;
        }
        clearDroppedObjectList();
        numberOfObjectRequiredToDrop = 0;
        objectRequiredToDropId = 0;
    }

    protected String[] getAllRequiredTextureNameForStoryMode() {
        return new String[]{MathImagePathUtils.COUNTING_BACKGROUND_IMAGE_PATH,
                MathImagePathUtils.ZEBRA, MathImagePathUtils.DUCK, MathImagePathUtils.BIRD};
    }

    private void initStoryModeCountingScreenObject() {

        if (null == dragObjectScreenObjectList) {

            Texture backgroundTexture = getBackgroundTexture();

            dragObjectScreenObjectList = new ArrayList<TextureScreenObject<Integer, Object>>(3);

            float startYPosition = BOTTOM_RECTANGLE_START_Y_POSITION + PADDING_TOP_BOTTOM_IN_RECTANGLE;

            //init first screen object
            TextureScreenObject firstScreenObject = new TextureScreenObject(getFirstAllowDragObjectId(), getFirstObjectTexture(),
                    Config.SCREEN_CENTER_START_X_POSITION + PADDING_LEFT_RIGHT_IN_RECTANGLE,
                    startYPosition);
            adjustTextureSize(firstScreenObject);
            dragObjectScreenObjectList.add(firstScreenObject);

            //init the bottom rectangle
            bottomRectangle = new RoundCornerRectangleScreenObject(Config.SCREEN_CENTER_START_X_POSITION, BOTTOM_RECTANGLE_START_Y_POSITION,
                    backgroundTexture.getWidth(), (int) (firstScreenObject.height + PADDING_TOP_BOTTOM_IN_RECTANGLE * 2),
                    ColorProperties.OBJECT_CONTAINER);

            float startXPosition = (bottomRectangle.width - firstScreenObject.width) / 2;

            //init second screen object
            TextureScreenObject secondScreenObject = new TextureScreenObject(getSecondAllowDragObjectId(), getSecondObjectTexture(),
                    Config.SCREEN_CENTER_START_X_POSITION + startXPosition,
                    startYPosition);
            adjustTextureSize(secondScreenObject);
            dragObjectScreenObjectList.add(secondScreenObject);

            //init third screen object
            TextureScreenObject thirdScreenObject = new TextureScreenObject(getThirdAllowDragObjectId(), getThirdObjectTexture(),
                    bottomRectangle.yPositionInScreen + bottomRectangle.width - PADDING_LEFT_RIGHT_IN_RECTANGLE,
                    startYPosition);
            adjustTextureSize(thirdScreenObject);
            dragObjectScreenObjectList.add(thirdScreenObject);

            backgroundScreenObject =
                    new TextureScreenObject(backgroundTexture,
                            Config.SCREEN_CENTER_START_X_POSITION,
                            bottomRectangle.yPositionInScreen + bottomRectangle.height + GAP_BETWEEN_BOTTOM_RECTANGLE_BACKGROUND
                    );

            allowDropFirstArea = getFirstObjectAllowDropArea();
            allowDropSecondArea = getSecondObjectAllowDropArea();
            allowDropThirdArea = getThirdObjectAllowDropArea();

        }
    }

    private void reRandomPlayingNumberList() {
        Collections.shuffle(playNumberList);
        currentPlayingIndex = 0;
    }

    private void nextRound() {
        clearDroppedObjectList();
        numberOfObjectRequiredToDrop = playNumberList.get(currentPlayingIndex);
        objectRequiredToDropId = playObjectList.get(currentPlayingIndex);
        audioIndexForTheDropObject = getAudioIndexForTheDropObject(objectRequiredToDropId);
    }

    protected Texture getBackgroundTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.COUNTING_BACKGROUND_IMAGE_PATH);
    }

    protected Texture getFirstObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.ZEBRA);
    }

    private void adjustTextureSize(TextureScreenObject textureScreenObject) {
        if (textureScreenObject.height > OBJECT_MAXIMUM_HEIGHT) {
            float ratio = OBJECT_MAXIMUM_HEIGHT / textureScreenObject.height;
            textureScreenObject.width *= ratio;
            textureScreenObject.height = OBJECT_MAXIMUM_HEIGHT;
        }
    }

    protected Texture getSecondObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.DUCK);
    }

    protected Texture getThirdObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.BIRD);
    }

    protected Rectangle getFirstObjectAllowDropArea() {
        float dropAreaHeight = backgroundScreenObject.height / 3;
        float dropAreaWidth = backgroundScreenObject.width;
        return new Rectangle(backgroundScreenObject.xPositionInScreen, backgroundScreenObject.yPositionInScreen, dropAreaWidth, dropAreaHeight);
    }

    protected Rectangle getSecondObjectAllowDropArea() {
        float dropAreaHeight = backgroundScreenObject.height / 3;
        float dropAreaWidth = backgroundScreenObject.width;
        return new Rectangle(backgroundScreenObject.xPositionInScreen, backgroundScreenObject.yPositionInScreen + allowDropFirstArea.height, dropAreaWidth, dropAreaHeight);
    }

    protected Rectangle getThirdObjectAllowDropArea() {
        float dropAreaHeight = backgroundScreenObject.height / 3;
        float dropAreaWidth = backgroundScreenObject.width;
        return new Rectangle(backgroundScreenObject.xPositionInScreen, allowDropSecondArea.y + allowDropSecondArea.height, dropAreaWidth, dropAreaHeight);
    }

    private void clearDroppedObjectList() {
        if (null != dropObjectScreenObjectList) {
            dropObjectScreenObjectList.clear();
            dropObjectScreenObjectList = null;
        }
        touchedDraggableScreenObject = null;
    }

    protected int getAudioIndexForTheDropObject(int objectId) {
        if (getFirstAllowDragObjectId() == objectId) {
            //zebra
            return 4;
        } else if (getSecondAllowDragObjectId() == objectId) {
            //ducks
            return 2;
        } else if (getThirdAllowDragObjectId() == objectId) {
            //birds
            return 3;
        }
        return 0;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        abstractAutoCognitaScreen.playSound(getHelpAudioFileNameList());
    }

    protected List<String> getHelpAudioFileNameList() {

        if (null != mathAudioScriptWithElementCode.instructionScriptAudioFileNameList && mathAudioScriptWithElementCode.instructionScriptAudioFileNameList.size() == 5) {
            List<String> instructionScriptAudioFileNameList = new ArrayList<String>(3);
            if (UserPreferenceUtils.getInstance().isEnglish()) {
                for (int i = 0; i < 3; i++) {
                    if (i == 2) {
                        //the last audio in the list is used to tell the user which object type is required to drag, such as zebra or bird
                        instructionScriptAudioFileNameList.add(mathAudioScriptWithElementCode.instructionScriptAudioFileNameList.get(audioIndexForTheDropObject));
                    } else {
                        //for the first 2 audios, it is used to ask the user to "put" "<number>"
                        instructionScriptAudioFileNameList.add(mathAudioScriptWithElementCode.replaceNumberPattern(mathAudioScriptWithElementCode.instructionScriptAudioFileNameList.get(i), numberOfObjectRequiredToDrop));
                    }
                }
            } else {

                instructionScriptAudioFileNameList.add(getPutObjectAudioNameForSwahili(audioIndexForTheDropObject));

                instructionScriptAudioFileNameList.addAll(mathAudioScriptWithElementCode.getNumberAudioFileList(numberOfObjectRequiredToDrop));

                instructionScriptAudioFileNameList.add(getPutObjectDestinationAudioNameForSwahili(audioIndexForTheDropObject));

            }

            return instructionScriptAudioFileNameList;
        }
        return null;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != touchedDraggableScreenObject) {

            boolean dropped = false;

            if (touchedDraggableScreenObject.id == objectRequiredToDropId) {

                Rectangle requiredDopArea = null;
                if (objectRequiredToDropId == getFirstAllowDragObjectId()) {
                    requiredDopArea = allowDropFirstArea;
                } else if (objectRequiredToDropId == getSecondAllowDragObjectId()) {
                    requiredDopArea = allowDropSecondArea;
                } else if (objectRequiredToDropId == getThirdAllowDragObjectId()) {
                    requiredDopArea = allowDropThirdArea;
                }

                if (TouchUtils.isTouched(requiredDopArea, screenX, screenY)) {
                    dropped = true;
                }
            }

            if (dropped) {
                touchedDraggableScreenObject.setOriginalPositionToCurrent();

                //if allow drop, add it to the dropped list
                if (null == dropObjectScreenObjectList) {
                    dropObjectScreenObjectList = new ArrayList<TextureScreenObject<Integer, Object>>();
                }

                if (!dropObjectScreenObjectList.contains(touchedDraggableScreenObject)) {
                    //if the object which is not include in the list, which mean it is dragged from the bottom bar
                    //duplicate the object
                    dropObjectScreenObjectList.add(touchedDraggableScreenObject);

                    List droppedObjectList = ScreenObjectUtils.getScreenObjectListById(dropObjectScreenObjectList, objectRequiredToDropId);
                    if (CollectionUtils.isNotEmpty(droppedObjectList) && droppedObjectList.size() >= numberOfObjectRequiredToDrop) {
                        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                            @Override
                            public void onCorrectSoundPlayed() {
                                currentPlayingIndex++;
                                if (currentPlayingIndex < NUMBER_OF_TIME_REQUIRED_TO_PLAY) {
                                    nextRound();
                                    abstractAutoCognitaScreen.playSound(getHelpAudioFileNameList());
                                } else {
                                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
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
            } else {
                //check if the object is dragged outside
                if (TouchUtils.isTouched(backgroundScreenObject, screenX, screenY)) {
                    //roll back to the original position if not allow drop
                    touchedDraggableScreenObject.rollbackToOriginalPosition();
                } else {
                    if (CollectionUtils.isNotEmpty(dropObjectScreenObjectList) && dropObjectScreenObjectList.contains(touchedDraggableScreenObject)) {
                        dropObjectScreenObjectList.remove(touchedDraggableScreenObject);
                    }
                }

            }

            touchedDraggableScreenObject = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        //if the playing reached the number of time required to play, which mean the lesson is finished, no more touch will be execute
        if (currentPlayingIndex < NUMBER_OF_TIME_REQUIRED_TO_PLAY) {
            TextureScreenObject<Integer, Object> touchingTextureScreenObject = ScreenObjectUtils.getTouchingScreenObject(dragObjectScreenObjectList, screenX, screenY);
            if (null != touchingTextureScreenObject) {
                //duplicate the object, because the object in the bottom bar is required to keep in the position and not allow drag
                touchedDraggableScreenObject = new TextureScreenObject<Integer, Object>(touchingTextureScreenObject.id, touchingTextureScreenObject.getTexture(),
                        touchingTextureScreenObject.xPositionInScreen, touchingTextureScreenObject.yPositionInScreen);
            } else {
                //check if the user is dragging the object which is already dropped on the background image
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

    protected String getPutObjectAudioNameForSwahili(int objectIndex) {
        if (2 == objectIndex) {
            return "ctst1_i1_1";
        } else if (3 == objectIndex) {
            return "ctst1_i1_2";
        } else if (4 == objectIndex) {
            return "ctst1_i1_3";
        }

        return null;
    }

    protected String getPutObjectDestinationAudioNameForSwahili(int objectIndex) {
        if (2 == objectIndex) {
            return "ctst1_i1_5";
        } else if (3 == objectIndex) {
            return "ctst1_i1_6";
        } else if (4 == objectIndex) {
            return "ctst1_i1_7";
        }

        return null;
    }
}
