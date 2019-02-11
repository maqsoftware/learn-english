package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.storyMode.MissionListStoryMode;
import com.maqautocognita.prototype.storyMode.MissionStoryMode;
import com.maqautocognita.prototype.storyMode.MissionTaskStoryMode;
import com.maqautocognita.prototype.storyMode.StoryModeImage;
import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.maqautocognita.prototype.storyMode.StoryModeScenePath;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actors.AbstractStoryModeObjectActor;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.actors.ImageStoryModeActor;
import com.maqautocognita.scene2d.actors.StoryModeObjectActor;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StoryModeBackgroundMusicFileNameUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryMissionScreen extends AbstractStoryModeScreen {

    private static final int SECOND_TO_FADE_OUT_THE_MISSION_TITLE = 3;

    private static final String CHARACTER_SPEAKER_INDICATOR = "me";

    private static final float CHARACTER_PADDING_LEFT_SPACE = 20;

    private static final float INVENTORY_BOX_PADDING_LEFT_RIGHT_SPACE = 10;

    private static final TextFontSizeEnum MISSION_TITLE_FONT_SIZE = TextFontSizeEnum.FONT_144;

    private MissionListStoryMode missionListStoryMode;
    private MissionStoryMode currentMissionStoryMode;
    private MissionTaskStoryMode currentMissionTaskStoryMode;

    private ImageStoryModeActor speechBubble;

    private int currentTaskIndex;

    private TextCell missionTitle;
    private TextCell instructionTitle;
    private Image inventoryBox;

    private ImageActor rightArrow;
    private ImageActor leftArrow;

    private List<StoryModeObjectActor> inventoryItemList;

    private Database storyModeDatabase;

    public StoryMissionScreen(Database storyModeDatabase,
                              AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(storyModeDatabase, game, menuScreenListener, analyticSpotService);
        this.storyModeDatabase = storyModeDatabase;
        missionListStoryMode = new MissionListStoryMode(storyModeDatabase);
        initCurrentMission();
    }

    private boolean initCurrentMission() {
        currentTaskIndex = 0;
        currentMissionStoryMode = null;
        for (MissionStoryMode missionStoryMode : missionListStoryMode.getEnabledMission()) {
            if (!missionStoryMode.getCompleted()) {
                currentMissionStoryMode = missionStoryMode;
                Gdx.app.log(getClass().getName(), "Next mission is " + getMissionTitle());
            }
        }

        if (null == currentMissionStoryMode) {
            //which mean the game of the story mission is completed
            UserPreferenceUtils.getInstance().setStoryMissionCompleted();
            missionListStoryMode.restartGame();

            new TimerService(new TimerService.ITimerListener() {
                @Override
                public void beforeStartTimer() {

                }

                @Override
                public void onTimerComplete(Object threadIndicator) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            onHomeClick();
                        }
                    });

                }
            }).startTimer(null, 10);
            return false;
        }

        return true;
    }

    private String getMissionTitle() {
        return UserPreferenceUtils.getInstance().isEnglish() ?
                currentMissionStoryMode.vEnglishText : currentMissionStoryMode.vSwahiliText;
    }

    @Override
    public void show() {
        if (null == currentMissionStoryMode) {
            missionListStoryMode = new MissionListStoryMode(storyModeDatabase);
            initCurrentMission();
        }
        super.show();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != currentMissionStoryMode) {
            currentMissionStoryMode.isTitleShown = false;
        }
        missionTitle = null;
        inventoryBox = null;
        rightArrow = null;
        leftArrow = null;
        if (null != inventoryItemList) {
            inventoryItemList.clear();
            inventoryItemList = null;
        }
    }

    @Override
    protected void clearListOfActors() {
        super.clearListOfActors();
        speechBubble = null;
    }

    @Override
    protected StoryModeScene getCurrentStoryModeScene() {
        if (null != currentMissionStoryMode && null == currentStoryModeScene) {
            return currentMissionStoryMode.vStoryModeScene;
        }
        return currentStoryModeScene;
    }

    @Override
    protected List<StoryModeImage> getAdditionalStoryModeImageList() {
        if (isMissionRequiredInTheCurrentScene()) {
            Gdx.app.log(getClass().getName(), "add additional images");
            return currentMissionStoryMode.vAdditionalSceneImage.getSceneImageObject();
        }
        return null;
    }

    @Override
    protected void afterScenePreloaded() {
        super.afterScenePreloaded();
        showTask();
    }

    @Override
    protected String getSceneName() {
        return null;
    }

    @Override
    protected void afterSceneChanged() {
        super.afterSceneChanged();
        startShowHintTimer();
    }

    @Override
    public void onMove(CustomCamera customCamera) {
        showMissionTitle();
    }

    private void showMissionTitle() {
        initMissionTitle();
        if (null != missionTitle && null != currentMissionStoryMode && !currentMissionStoryMode.isTitleShown && isMissionRequiredInTheCurrentScene() && isMissionLocationReached()) {
            Gdx.app.log(getClass().getName(), "reach the location, show mission title");
            missionTitle.setText(getMissionTitle());
            currentMissionStoryMode.isTitleShown = true;
            clearInstructionTitle();
            AnimationUtils.doFadeOut(missionTitle, SECOND_TO_FADE_OUT_THE_MISSION_TITLE);
        }
    }

    private void initMissionTitle() {
        if (null != fixedStage) {
            if (null == missionTitle) {
                missionTitle = new TextCell("", MISSION_TITLE_FONT_SIZE, ScreenUtils.getScreenWidth());
                missionTitle.setY(ScreenUtils.getBottomYPositionForCenterObject(LetterUtils.getMaximumHeight(MISSION_TITLE_FONT_SIZE)));
                missionTitle.setTextAlign(Align.center | Align.center);
                missionTitle.setTextColor(ColorProperties.STORY_MISSION_TITLE);
                fixedStage.addActor(missionTitle);
            }
        }
    }

    private boolean isMissionRequiredInTheCurrentScene() {
        if (null == currentStoryModeScene || null == currentMissionStoryMode) {
            return false;
        }

        return currentStoryModeScene.getSceneName().equals(currentMissionStoryMode.vStoryModeScene.getSceneName());
    }

    private boolean isMissionLocationReached() {
        return mainCamera.isObjectInsideCamera(getCurrentMissionLocationCenterXPosition(), 1);
    }

    private void clearInstructionTitle() {
        if (null != instructionTitle) {
            instructionTitle.setText(null);
        }
    }

    private float getCurrentMissionLocationCenterXPosition() {
        return currentMissionStoryMode.vStoryModeScene.getSceneCenterLocation(currentMissionStoryMode.vSceneLocation);
    }

    private void showNextMission() {
        Gdx.app.log(getClass().getName(), "Go to next mission");
        if (initCurrentMission()) {
            addActorByStoryModeImageList(getAdditionalStoryModeImageList());
            showTask();
        }
    }

    @Override
    protected String getAudioPath() {
        return "storyMission" + File.separator + UserPreferenceUtils.getInstance().getLanguage().toLowerCase();
    }

    @Override
    protected boolean isCharacterRequiredToShow() {
        return true;
    }

    @Override
    protected float getCharacterStartXPosition() {
        return CHARACTER_PADDING_LEFT_SPACE;
    }

    @Override
    public void doRender() {
        super.doRender();
        if (null != fixedStage) {


            if (null == inventoryBox) {
                inventoryBox = new Image(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.STORY_MISSION_INVENTORY_BOX_IMAGE_PATH));
                inventoryBox.setX(ScreenUtils.getScreenWidth() - inventoryBox.getWidth() - 10);
                inventoryBox.setY(ScreenUtils.getScreenHeight() - inventoryBox.getHeight() - 10);
                fixedStage.addActor(inventoryBox);

                if (CollectionUtils.isNotEmpty(missionListStoryMode.getPocketList())) {
                    for (String item : missionListStoryMode.getPocketList()) {
                        StoryModeImage storyModeImage = null;
                        StoryModeObjectActor storyModeObjectActor = getStoryModeObjectActorByObjectName(item);
                        if (null == storyModeObjectActor) {
                            storyModeImage = storyModeLogic.getStoryModeImage(item);
                        } else {
                            storyModeImage = storyModeObjectActor.getStoryModeImage();
                        }
                        addItemToInventoryBox(storyModeImage);
                    }
                }
            }

            if (null == instructionTitle) {
                float startXPosition = CLOTHES_ICON_POSITION.x + CLOTHES_ICON_POSITION.width * 2;
                instructionTitle = new TextCell("", TextFontSizeEnum.FONT_72,
                        ScreenUtils.getScreenWidth() - (ScreenUtils.getScreenWidth() - inventoryBox.getX()) - startXPosition - 10,
                        startXPosition,
                        ScreenUtils.getScreenHeight() - 30 - LetterUtils.getMaximumHeight(TextFontSizeEnum.FONT_72));
                instructionTitle.setWrap(true);
                instructionTitle.setTextFlip(false);
                instructionTitle.setTextAlign(Align.left);
                instructionTitle.setTextColor(ColorProperties.STORY_INSTRUCTION_TEXT);
                fixedStage.addActor(instructionTitle);
            }
        }
    }

    private StoryModeObjectActor addItemToInventoryBox(StoryModeImage storyModeImage) {

        float startXPosition = inventoryBox.getX() + INVENTORY_BOX_PADDING_LEFT_RIGHT_SPACE;
        if (null != inventoryItemList) {
            for (StoryModeObjectActor item : inventoryItemList) {
                startXPosition += item.getWidthAfterScale() + INVENTORY_BOX_PADDING_LEFT_RIGHT_SPACE;
            }
        }

        StoryModeObjectActor storyModeObjectActor = new StoryModeObjectActor(storyModeImage, startXPosition, inventoryBox.getY());
        storyModeObjectActor.setScale(inventoryBox.getHeight() / storyModeImage.vImageArea.vImageHeight);

        storyModeObjectActor.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Gdx.app.log(getClass().getName(), "click the inventory item");
                return;
            }
        });

        fixedStage.addActor(storyModeObjectActor);
        storyModeObjectActor.toFront();
        if (null == inventoryItemList) {
            inventoryItemList = new ArrayList<StoryModeObjectActor>();
        }
        inventoryItemList.add(storyModeObjectActor);
        return storyModeObjectActor;
    }

    @Override
    protected String getBackgroundMusicFileName() {
        if (null != currentMissionStoryMode) {
            return StoryModeBackgroundMusicFileNameUtils.getMusicFileName(currentMissionStoryMode.vStoryModeScene);
        }
        return null;
    }

    @Override
    protected void afterCameraSetup() {
        super.afterCameraSetup();
        setupCamera();
        showMissionTitle();
    }

    private void setupCamera() {
        if (null != mainCamera) {
            mainCamera.updateCameraXPosition(getCurrentMissionLocationCenterXPosition());
        }
    }

    private void showTask() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {


                addCharacter();
                if (isMissionRequiredInTheCurrentScene()) {
                    showMissionTitle();
                    //make sure the previous listener is removed
                    character.clearListeners();
                    if (null != speechBubble) {
                        speechBubble.setVisible(false);
                    }
                    float xPosition = getCurrentMissionLocationStartXPosition() + CHARACTER_PADDING_LEFT_SPACE;
                    character.setX(xPosition);
                    character.setOriginalXPosition(xPosition);
                    List<MissionTaskStoryMode> taskList = currentMissionStoryMode.vMissionTaskList;
                    if (taskList.size() >= currentTaskIndex + 1) {
                        currentMissionTaskStoryMode = taskList.get(currentTaskIndex);

                        Gdx.app.log(getClass().getName(), "missionTaskStoryMode = " + currentMissionTaskStoryMode);

                        if ((StringUtils.isNotBlank(currentMissionTaskStoryMode.vDragObject)
                                || StringUtils.isNotBlank(currentMissionTaskStoryMode.vInventoryDrop)
                        ) && StringUtils.isNotBlank(currentMissionTaskStoryMode.vDropToObject)) {

                            final AbstractStoryModeObjectActor dropTarget =
                                    CHARACTER_SPEAKER_INDICATOR.equals(currentMissionTaskStoryMode.vDropToObject) ?
                                            character : getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vDropToObject);

                            if (null != dropTarget) {
                                StoryModeObjectActor dragActor = null;

                                if (StringUtils.isNotBlank(currentMissionTaskStoryMode.vDragObject)) {
                                    dragActor = getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vDragObject);
                                    if (null == dragActor) {
                                        dragActor = getItemFromInventoryBox(currentMissionTaskStoryMode.vDragObject);
                                    }
                                }

                                addDragListener(dragActor, dropTarget, currentMissionTaskStoryMode);

                            }
                        } else {
                            if (StringUtils.isNotBlank(currentMissionTaskStoryMode.vClickObject)) {
                                addSpeakAction(getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vClickObject), currentMissionTaskStoryMode);
                            }
                            addSpeaker(currentMissionTaskStoryMode);
                        }

                    } else {
                        //which mean the task is complete
                        missionListStoryMode.setMissionCompleted(currentMissionStoryMode.vMissionCode);
                        showNextMission();
                    }
                }

                startShowHintTimer();
            }
        });
    }

    private void addDragListener(final StoryModeObjectActor dragActor, final AbstractStoryModeObjectActor dropTarget,
                                 final MissionTaskStoryMode missionTaskStoryMode) {
        if (null != dragActor) {

            Gdx.app.log(getClass().getName(), "going to add drag listener for the object " + dragActor.getStoryModeImage().vObjectName + " and drop target = " + dropTarget.getImagePath());

            dragActor.addListener(new DragListener() {
                /**
                 * This is used to store the touch position in the actor
                 */
                private float startTouchXPosition = -1;
                private float startTouchYPosition = -1;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    startTouchXPosition = event.getStageX() - dragActor.getX();
                    startTouchYPosition = event.getStageY() - dragActor.getY();
                    dragActor.toFront();
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    mainCamera.setLocked(true);
                    mainCamera.setFocusTarget(dragActor);
                    dragActor.setPosition(event.getStageX() - startTouchXPosition, event.getStageY() - startTouchYPosition);
                }


                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);


                    if (isOverlap(event)) {
                        //if the actor is dropped in the valid target
                        showMessageAndPlayAudio(missionTaskStoryMode);
                        if (StringUtils.isNotBlank(missionTaskStoryMode.vInventoryAdd)) {
                            addItemToInventoryBox(dragActor);
                        } else if (StringUtils.isNotBlank(missionTaskStoryMode.vInventoryDrop)) {
                            removeItemFromInventoryBox(dragActor);
                        } else {
                            dragActor.rollbackToOriginalPosition();
                        }
                    } else {
                        dragActor.rollbackToOriginalPosition();
                    }

                    mainCamera.setLocked(false);
                    mainCamera.setFocusTarget(null);
                    startTouchXPosition = -1;
                    startTouchYPosition = -1;
                }

                private boolean isOverlap(InputEvent event) {
                    if (dragActor.getCamera().equals(dropTarget.getCamera())) {
                        return dragActor.isOverlap(dropTarget);
                    } else {
                        float dropTargetXPosition = dropTarget.getX() - (dropTarget.getCamera().position.x - dropTarget.getCamera().viewportWidth / 2);

                        return TouchUtils.isTouched(dropTargetXPosition, dropTarget.getY(),
                                dropTarget.getWidthAfterScale(), dropTarget.getHeightAfterScale(), event.getStageX(), event.getStageY());
                    }
                }

            });
        }
    }

    private void addSpeaker(MissionTaskStoryMode missionTaskStoryMode) {
        //check if required speak
        if (StringUtils.isNotBlank(missionTaskStoryMode.vSpeaker)) {

            AbstractStoryModeObjectActor speaker;
            if (CHARACTER_SPEAKER_INDICATOR.equals(missionTaskStoryMode.vSpeaker)) {
                //if it is for character speaker
                speaker = character;
            } else {
                //for other speaker
                speaker = getStoryModeObjectActorByObjectName(missionTaskStoryMode.vSpeaker);
            }

            if (null != speaker) {
                if (null == speechBubble) {
                    speechBubble = new ImageStoryModeActor(Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/speech_bubble.png");
                    mainStage.addActor(speechBubble);
                }
                float centerXLocation =
                        speaker.getWidth() > speechBubble.getWidth() ?
                                ScreenUtils.getXPositionForCenterObject(speechBubble.getWidth(), speaker.getWidth()) :
                                0;
                speechBubble.setPosition(speaker.getX() + Math.abs(centerXLocation),
                        speaker.getY() + speaker.getHeight() + 10);
                speechBubble.setOriginalXPosition(speechBubble.getX());
                speechBubble.setVisible(true);
                addSpeakAction(speechBubble, missionTaskStoryMode);
                addSpeakAction(speaker, missionTaskStoryMode);
                speechBubble.toFront();
            }
        }
    }

    private void addSpeakAction(final Actor actor, final MissionTaskStoryMode missionTaskStoryMode) {
        if (null != actor) {
            actor.clearListeners();
            actor.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (missionTaskStoryMode.equals(currentMissionTaskStoryMode)) {
                        //make sure the message on show when the current mission is equals to the given mission
                        showMessageAndPlayAudio(missionTaskStoryMode);
                    }
                    //if the user presses again, it should not say the last sentence again and again
                    actor.clearListeners();
                }
            });
        }
    }

    private void removeItemFromInventoryBox(StoryModeObjectActor actor) {
        if (null != actor) {
            missionListStoryMode.removeItemFromPocketList(actor.getStoryModeImage().vImageName);
            inventoryItemList.remove(actor);
            actor.remove();
            actor = null;
            reorderItemPositionInInventoryBox();
        }
    }

    private void reorderItemPositionInInventoryBox() {
        if (null != inventoryBox && CollectionUtils.isNotEmpty(inventoryItemList)) {
            float startXPosition = inventoryBox.getX();
            for (StoryModeObjectActor item : inventoryItemList) {
                item.setX(startXPosition);
                startXPosition += item.getWidthAfterScale() + INVENTORY_BOX_PADDING_LEFT_RIGHT_SPACE;
            }
        }
    }

    private void addItemToInventoryBox(StoryModeObjectActor actor) {
        if (null != actor) {
            missionListStoryMode.addItemToPocketList(actor.getStoryModeImage().vImageName);
            addItemToInventoryBox(actor.getStoryModeImage());
            actor.remove();
            actor = null;
        }
    }

    private void showMessageAndPlayAudio(final MissionTaskStoryMode missionTaskStoryMode) {
        clearShowHintTimer();
        hideHintTouch();
        showMessageAndPlayAudio(isEnglish() ? missionTaskStoryMode.vEnglishText : missionTaskStoryMode.vSwahiliText,
                missionTaskStoryMode.vAudioFile, new IActionListener() {
                    @Override
                    public void onComplete() {

                        final boolean isGoToNextTask = missionTaskStoryMode.equals(currentMissionTaskStoryMode);

                        if (StringUtils.isNotBlank(missionTaskStoryMode.vInstructionAudioFile)) {
                            instructionTitle.setText(isEnglish() ? missionTaskStoryMode.vEnglishInstruction : missionTaskStoryMode.vSwahiliInstruction);
                            playSound(missionTaskStoryMode.vInstructionAudioFile, new AbstractSoundPlayListener() {
                                @Override
                                public void onComplete() {
                                    super.onComplete();
                                    doComplete();
                                }

                                @Override
                                public void onStop() {
                                    super.onStop();
                                    doComplete();
                                }

                                private void doComplete() {
                                    if (isGoToNextTask) {
                                        nextTask();
                                    }
                                }
                            });
                        } else if (isGoToNextTask) {
                            nextTask();
                        }
                    }
                });
    }

    private void nextTask() {
        currentTaskIndex++;
        showTask();
    }

    private boolean isEnglish() {
        return UserPreferenceUtils.getInstance().isEnglish();
    }

    private void showMessageAndPlayAudio(String text, String audioFile, IActionListener completeListener) {
        clearInstructionTitle();
        getMessageActor().setText(text,
                audioFile, completeListener);
    }

    private float getCurrentMissionLocationStartXPosition() {
        Gdx.app.log(getClass().getName(), "going to check the start x of the location : " + currentMissionStoryMode.vSceneLocation);
        return currentMissionStoryMode.vStoryModeScene.getSceneStartXLocation(currentMissionStoryMode.vSceneLocation);
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (null != currentMissionTaskStoryMode && null != currentMissionStoryMode) {
            if (isMissionRequiredInTheCurrentScene()) {
                AbstractStoryModeObjectActor object = null;
                if (StringUtils.isNotBlank(currentMissionTaskStoryMode.vClickObject)) {
                    object = getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vClickObject);
                }
                if (StringUtils.isNotBlank(currentMissionTaskStoryMode.vSpeaker)) {
                    object = speechBubble;
                }

                if (StringUtils.isNotBlank(currentMissionTaskStoryMode.vDragObject)) {
                    StoryModeObjectActor dragObject = getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vDragObject);
                    if (null == dragObject) {
                        dragObject = getItemFromInventoryBox(currentMissionTaskStoryMode.vDragObject);
                    }
                    if (null != dragObject) {
                        Gdx.app.log(getClass().getName(), "flash the drag object " + currentMissionTaskStoryMode.vDragObject);
                    }
                    highlightObject(dragObject, false);
                    AnimationUtils.doFlash(dragObject);
                    object = getStoryModeObjectActorByObjectName(currentMissionTaskStoryMode.vDropToObject);
                }

                if (null != object) {
                    //check if the object is inside the camera
                    if (mainCamera.isObjectInsideCamera(object.getX(), object.getWidthAfterScale())) {
                        if (object instanceof StoryModeObjectActor) {
                            highlightObject((StoryModeObjectActor) object, false);
                        } else {
                            AnimationUtils.doFlash(object);
                        }
                    } else {
                        showIndicator(object);
                    }
                }

            } else {

                Gdx.app.log(getClass().getName(), "get the scene path from " + currentStoryModeScene.getSceneName() + " to " + currentMissionStoryMode.vStoryModeScene.getSceneName());

                //the mission scene may need to pass via 1 scene
                StoryModeScenePath storyModeScenePath = missionListStoryMode.getScenePath(
                        currentStoryModeScene.getSceneName(),
                        currentMissionStoryMode.vStoryModeScene.getSceneName());

                Gdx.app.log(getClass().getName(), storyModeScenePath.toString());

                StoryModeObjectActor sceneEntryActor = getSceneEntryActor(storyModeScenePath.vStep1);

                if (null != sceneEntryActor && mainCamera.isObjectInsideCamera(sceneEntryActor.getX(), sceneEntryActor.getWidthAfterScale())) {
                    highlightObject(sceneEntryActor, false);
                    Gdx.app.log(getClass().getName(), "flash the scene entry " + sceneEntryActor.getStoryModeImage().vObjectName);
                } else {
                    showIndicator(sceneEntryActor);
                }
            }

            startShowHintTimer();
        }
    }

    private StoryModeObjectActor getItemFromInventoryBox(String itemName) {
        if (CollectionUtils.isNotEmpty(inventoryItemList)) {
            for (StoryModeObjectActor item : inventoryItemList) {
                if (item.getStoryModeImage().vObjectName.equals(itemName)) {
                    return item;
                }
            }
        }

        return null;
    }

    private void showIndicator(AbstractStoryModeObjectActor actor) {

        if (null != actor) {

            Gdx.app.log(getClass().getName(), "camera x = " + mainCamera.position.x +
                    ", actor x = " + actor.getX() + ", actor Original x = " + actor.getOriginalXPosition() + " worldwidth = " + mainCamera.getWorldWidth());

            boolean isMovingRight = mainCamera.position.x + mainCamera.viewportWidth / 2 > 0;

            float actorX = 0;

            if (actor.getX() == actor.getOriginalXPosition() && "02".equals(currentMissionStoryMode.vMissionCode)) {
                //TODO find a way to detect the nearest entry  instead of hardcode
                actorX = actor.getX() - mainCamera.getWorldWidth();
            } else {
                actorX = Math.abs(mainCamera.getWorldWidth() - actor.getX()) > Math.abs(mainCamera.getWorldWidth() - actor.getOriginalXPosition()) ?
                        actor.getOriginalXPosition() : actor.getX();
            }

            //in right hand side
            if (Math.abs(mainCamera.position.x) - Math.abs(actorX) > 0) {
                if (isMovingRight) {
                    showLeftArrow();
                } else {
                    showRightArrow();
                }
            } else {
                if (isMovingRight) {
                    showRightArrow();
                } else {
                    showLeftArrow();
                }
            }
        }

    }

    private void showLeftArrow() {
        Gdx.app.log(getClass().getName(), "flash the left arrow");
        if (null == leftArrow) {
            leftArrow =
                    new ImageActor(AssetManagerUtils.ICONS, NavigationSection.LEFT_ARROW_POSITION,
                            0,
                            ScreenUtils.getBottomYPositionForCenterObject(NavigationSection.LEFT_ARROW_POSITION.height));
            fixedStage.addActor(leftArrow);
        }
        leftArrow.setX(fixedStageCamera.position.x - fixedStageCamera.viewportWidth / 2 + NavigationSection.LEFT_ARROW_POSITION.width);

        AnimationUtils.doFlashAndFadeOut(leftArrow);
    }

    private void showRightArrow() {
        Gdx.app.log(getClass().getName(), "flash the right arrow");
        if (null == rightArrow) {
            rightArrow =
                    new ImageActor(AssetManagerUtils.ICONS, NavigationSection.RIGHT_ARROW_POSITION,
                            0,
                            ScreenUtils.getBottomYPositionForCenterObject(NavigationSection.RIGHT_ARROW_POSITION.height));
            fixedStage.addActor(rightArrow);
        }
        rightArrow.setX(fixedStageCamera.position.x + fixedStageCamera.viewportWidth / 2 - NavigationSection.RIGHT_ARROW_POSITION.width);

        AnimationUtils.doFlashAndFadeOut(rightArrow);
    }
}
