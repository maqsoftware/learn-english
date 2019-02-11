package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.storyMode.StoryModeImage;
import com.maqautocognita.prototype.storyMode.StoryModeLesson;
import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.maqautocognita.prototype.storyMode.StoryModeUserGestureEnum;
import com.maqautocognita.prototype.storyMode.StoryModeUserGestureResult;
import com.maqautocognita.scene2d.actions.CameraMoveXAction;
import com.maqautocognita.scene2d.actions.CameraMoveYAction;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actions.ZoomAction;
import com.maqautocognita.scene2d.actors.StoryModeObjectActor;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StoryModeBackgroundMusicFileNameUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class StoryModeScreen extends AbstractAdvanceStoryModeScreen {

    private final static float TARGET_ZOOM_SIZE = 0.3f;

    /**
     * There is no way to check if the user is double tap or sing tap on the object,
     * below second is used to check if the user has another tap after the single tap
     */
    private final static float SECOND_TO_BE_WAIT_FOR_DOUBLE_TAP = 0.1f;

    private StoryModeLesson currentLesson;

    //store the actor which is focusing and zoom
    private StoryModeObjectActor zoomingActor;
    //store the group which is zooming, for example toy group, it will be empty if the user zoom out again
    private String zoomingGroup = "";
    private int currentPlayingLessonIndex;

    private TextureScreenObject previousLessonImage;
    private TextureScreenObject nextLessonImage;

    private StoryModeUserGestureResult storyModeUserGestureResult;

    public StoryModeScreen(Database storyModeDatabase, AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(storyModeDatabase, game, menuScreenListener, analyticSpotService);
        currentPlayingLessonIndex = storyModeLogic.getCurrentLesson(UserPreferenceUtils.getInstance().getLanguage());
    }

    @Override
    public void show() {
        super.show();
        playLessonStartScript();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();

    }

    @Override
    public void dispose() {
        super.dispose();
        previousLessonImage = null;
        nextLessonImage = null;
        clearHintsImage();
    }

    @Override
    protected void afterZoomOut() {
        super.afterZoomOut();
        zoomingActor = null;
        zoomingGroup = "";
    }

    protected void whenAllSceneObjectCreate() {
        if (CollectionUtils.isNotEmpty(currentLesson.getRemovalScene().getSceneImageObject())) {
            for (final StoryModeImage storyModeImage : currentLesson.getRemovalScene().getSceneImageObject()) {
                StoryModeObjectActor storyModeObjectActor = getStoryModeObjectActorByObjectName(storyModeImage.vObjectName);
                if (null != storyModeObjectActor) {
                    Gdx.app.log(getClass().getName(), "The object " + storyModeImage.vObjectName + " removed");
                    storyModeObjectActor.remove();
                }
            }
        }
    }

    protected List<StoryModeImage> getAdditionalStoryModeImageList() {
        Gdx.app.log(getClass().getName(), "going to add additional images");
        return currentLesson.getAdditionalScene().getSceneImageObject();
    }

    @Override
    protected void afterScenePreloaded() {
        super.afterScenePreloaded();
        Gdx.app.log(getClass().getName(), "afterScenePreloaded");
    }

    /**
     * Mainly used to add the default user gesture response the given storyModeObjectActor
     *
     * @param storyModeObjectActor
     */
    protected void afterStoryModeObjectActorCreated(final StoryModeObjectActor storyModeObjectActor) {
        super.afterStoryModeObjectActorCreated(storyModeObjectActor);

        storyModeObjectActor.addListener(new ActorGestureListener() {
            private boolean isDoubleTap;

            //this is mainly used to check if the tap is double tap, when the single tap is tapped, here need to wait a time, to make sure the user is not double tap
            private TimerService timerService;

            //this is the used for single tap or double tap action
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (1 == count) {
                    //final StoryModeUserGestureResult storyModeUserGestureResult =
                    storyModeUserGestureResult =
                            currentLesson.getGestureResult(StoryModeUserGestureEnum.TOUCH, storyModeObjectActor.getStoryModeImage().vObjectName, zoomingGroup);
                    Gdx.app.log(getClass().getName(), "The gesture result audio file is " + storyModeUserGestureResult.audioFile + ",message = " + storyModeUserGestureResult.message);
                    if (isUserGestureResultSuccess(storyModeUserGestureResult)) {
                        if (null == timerService) {
                            //because they is no way to check if the tap is double tap, so here make a timer to check if the user is going to double tap,
                            // if not double tap, the sound of the touched object will be play and do action if required
                            timerService = new TimerService(new TimerService.ITimerListener() {
                                @Override
                                public void beforeStartTimer() {

                                }

                                @Override
                                public void onTimerComplete(Object threadIndicator) {
                                    if (!isDoubleTap) {
                                        //make sure it is calling the render thread
                                        Gdx.app.postRunnable(new Runnable() {
                                            @Override
                                            public void run() {
                                                Gdx.app.log(getClass().getName(), "going to action " + storyModeUserGestureResult.expectedAction);
                                                String soundFile = "sound_" + storyModeObjectActor.getStoryModeImage().vObjectName;
                                                Gdx.app.log(getClass().getName(), "going to show message, audio = " + storyModeUserGestureResult.audioFile + ", message = " + storyModeUserGestureResult.message);

                                                if (isSoundFileExists(soundFile)) {
                                                    playSound(soundFile);
                                                }

                                                if (StringUtils.isBlank(zoomingGroup) &&
                                                        StoryModeUserGestureEnum.ZOOM_IN.toString().equals(storyModeUserGestureResult.expectedAction)) {

                                                    ParallelAction parallelAction = new ParallelAction();
                                                    parallelAction.addAction(new CameraMoveXAction(
                                                            storyModeObjectActor.getX() +
                                                                    //move the x-position to the center of the image
                                                                    (storyModeObjectActor.getWidthAfterScale() / 2), mainCamera, true));

                                                    parallelAction.addAction(new CameraMoveYAction(
                                                            mainCamera.position.y * (1 - TARGET_ZOOM_SIZE), mainCamera));
                                                    parallelAction.addAction(new ZoomAction(mainCamera, TARGET_ZOOM_SIZE));
                                                    storyModeObjectActor.addAction(parallelAction);

                                                    zoomingGroup = storyModeUserGestureResult.gestureLocation;
                                                    Gdx.app.log(getClass().getName(), "zooming group =  " + zoomingGroup);
                                                } else {
                                                    showMessage();
                                                }

                                            }
                                        });

                                    }

                                    isDoubleTap = false;
                                }
                            });
                        }

                        timerService.clearTimer();
                        timerService.startTimer(null, SECOND_TO_BE_WAIT_FOR_DOUBLE_TAP);

                    }
                } else if (2 == count) {
                    doDoubleClickOrLongPress();
                    isDoubleTap = true;
                }
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                doDoubleClickOrLongPress();
                return true;
            }

            private void doDoubleClickOrLongPress() {


                if (null != storyModeObjectActor) {
                    final StoryModeUserGestureResult storyModeUserGestureResult =
                            currentLesson.getGestureResult(StoryModeUserGestureEnum.DOUBLE_CLICK, storyModeObjectActor.getStoryModeImage().vObjectName);
                    if (isUserGestureResultSuccess(storyModeUserGestureResult)) {
                        if (null != zoomingActor && zoomingActor.equals(storyModeObjectActor)
                                && Math.round((1 - currentScale) * 10) / 10.0f == TARGET_ZOOM_SIZE) {
                            //which mean the actor is zoom, it will be zoom out again
                            SequenceAction sequenceAction = new SequenceAction();
                            sequenceAction.addAction(new ZoomAction(mainCamera, -TARGET_ZOOM_SIZE));
                            RunnableAction runnableAction = new RunnableAction();
                            runnableAction.setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    afterZoom(1);
                                    afterZoomOut();
                                }
                            });
                            sequenceAction.addAction(runnableAction);
                            storyModeObjectActor.addAction(sequenceAction);

                        } else {
                            ZoomAction zoomAction = new ZoomAction(mainCamera, TARGET_ZOOM_SIZE);
                            zoomAction.setActionListener(new IActionListener() {
                                @Override
                                public void onComplete() {
                                    //after zoom complete show the message
                                    showMessage();
                                }
                            });
                            if (null != storyModeObjectActor) {

                                storyModeObjectActor.addAction(new CameraMoveXAction(
                                        storyModeObjectActor.getX() +
                                                //move the x-position to the center of the image
                                                (storyModeObjectActor.getWidthAfterScale() / 2), mainCamera));

                                zoomingActor = storyModeObjectActor;
                            }
                        }
                    }
                }
            }
        });

        storyModeObjectActor.addListener(new DragListener() {

            /**
             * This is used to store the touch position in the actor
             */
            private float startTouchXPosition = -1;
            private float startTouchYPosition = -1;

            /**
             * It is used to store the drop target of the drag object.
             * It is also act as a flag to indicate if the actor is allow to drag
             * if it is stored as null, which mean the actor is not allow to drag
             */
            private StoryModeObjectActor dropTarget;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                StoryModeUserGestureResult storyModeUserGestureResult =
                        currentLesson.getGestureResult(StoryModeUserGestureEnum.DRAG, storyModeObjectActor.getStoryModeImage().vObjectName);
                if (isUserGestureResultSuccess(storyModeUserGestureResult)) {
                    dropTarget = getStoryModeObjectActorByObjectName(storyModeUserGestureResult.message);
                    if (null != dropTarget) {
                        startTouchXPosition = event.getStageX() - storyModeObjectActor.getX();
                        startTouchYPosition = event.getStageY() - storyModeObjectActor.getY();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (null != dropTarget) {
                    mainCamera.setLocked(true);
                    mainCamera.setFocusTarget(storyModeObjectActor);
                    storyModeObjectActor.setPosition(event.getStageX() - startTouchXPosition, event.getStageY() - startTouchYPosition);
                }
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (null != dropTarget) {
                    storyModeUserGestureResult =
                            currentLesson.getGestureResult(StoryModeUserGestureEnum.DROP,
                                    storyModeObjectActor.getStoryModeImage().vObjectName);

                    if (storyModeObjectActor.isOverlap(dropTarget)) {
                        //check if the actor is dropped in the valid target
                        showMessage();
                    }
                    mainCamera.setLocked(false);
                    mainCamera.setFocusTarget(null);
                    startTouchXPosition = -1;
                    startTouchYPosition = -1;
                    dropTarget = null;
                }
            }
        });
    }

    @Override
    protected String getSceneName() {
        return null;
    }

    @Override
    public void doRender() {
        super.doRender();

        addLessonNavigation();

        if (null != mainBatch) {
            mainBatch.begin();
            ScreenObjectUtils.draw(mainBatch, previousLessonImage);
            ScreenObjectUtils.draw(mainBatch, nextLessonImage);
            mainBatch.end();
        }
    }

    @Override
    protected String getBackgroundMusicFileName() {
        if (null != currentLesson) {
            return StoryModeBackgroundMusicFileNameUtils.getMusicFileName(currentLesson.getScene());
        }

        return "music1.wav";
    }

    @Override
    protected void afterCameraSetup() {
        super.afterCameraSetup();

        moveToPlayLocation();
    }

    private void moveToPlayLocation() {
        if (null != currentLesson && null != currentStoryModeScene) {

            StoryModeObjectActor storyModeObjectActor = getStoryModeObjectActorByObjectName(getNextNonPlayedObjectName());

            if (null != storyModeObjectActor) {
                mainCamera.updateCameraXPosition(storyModeObjectActor.getX());
            }
        }
    }

    private String getNextNonPlayedObjectName() {
        String object = currentLesson.getRandomPicWord();
        if (StringUtils.isNotBlank(object)) {
            return object.split(",")[0];
        }

        return null;
    }

    private void addLessonNavigation() {
        if (null == previousLessonImage) {
            Vector2 previousLessonArrowScreenPosition = getPreviousLessonArrowScreenPosition();
            previousLessonImage = new TextureScreenObject(null, null, NavigationSection.LEFT_ARROW_POSITION, previousLessonArrowScreenPosition.x,
                    previousLessonArrowScreenPosition.y, AssetManagerUtils.getTexture(AssetManagerUtils.ICONS));
        }

        if (null == nextLessonImage) {
            nextLessonImage = new TextureScreenObject(null, null, NavigationSection.RIGHT_ARROW_POSITION, ScreenUtils.getNavigationRightArrowStartXPosition(),
                    ScreenUtils.getNavigationBarStartYPosition(), AssetManagerUtils.getTexture(AssetManagerUtils.ICONS));
        }
    }

    private Vector2 getPreviousLessonArrowScreenPosition() {
        Vector2 clothesScreenPosition = getClothesScreenPosition();
        return new Vector2(clothesScreenPosition.x + CLOTHES_ICON_POSITION.width + SPACE_BETWEEN_TOP_ICON - 29, clothesScreenPosition.y);
    }

    @Override
    protected StoryModeScene getCurrentStoryModeScene() {
        currentLesson = storyModeLogic.getStoryModeLesson(String.valueOf(currentPlayingLessonIndex), UserPreferenceUtils.getInstance().getLanguage());
        return currentLesson.getScene();
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        highlightObject(getNextNonPlayedObjectName(), true);
    }

    private void playLessonStartScript() {
        playLessonStartScript(0, new IActionListener() {
            @Override
            public void onComplete() {
                startShowHintTimer();
            }
        });
    }

    private void playLessonStartScript(int index, IActionListener actionListener) {
        playLessonScript(index, true, actionListener);
    }

    private void playLessonEndScript(int index, IActionListener actionListener) {
        playLessonScript(index, false, actionListener);
    }

    private void playLessonScript(final int index, final boolean isStartScript, final IActionListener actionListener) {
        if (null != currentLesson) {
            setTouchAllow(false);
            setActorTouchable(Touchable.disabled);
            List<String> vAudioScript = isStartScript ? currentLesson.vAutoStartScript : currentLesson.vAutoEndScript;
            List<String> vAudioScriptSwahili = isStartScript ? currentLesson.vAutoStartScriptSwahili : currentLesson.vAutoEndScriptSwahili;
            List<String> vAutoVoice = isStartScript ? currentLesson.vAutoStartVoice : currentLesson.vAutoEndVoice;
            if (CollectionUtils.isNotEmpty(vAudioScript) && vAudioScript.size() > index) {
                String message = "";
                if (UserPreferenceUtils.getInstance().isEnglish()) {
                    message = vAudioScript.get(index);
                } else {
                    message = vAudioScriptSwahili.get(index);
                }
                String audio = null;
                if (CollectionUtils.isNotEmpty(vAudioScript) && vAutoVoice.size() > index) {
                    audio = vAutoVoice.get(index);
                }
                Gdx.app.log(getClass().getName(), "going to play the lesson script " + message + " and audio = " + audio);
                getMessageActor().setText(message, audio, new IActionListener() {
                    @Override
                    public void onComplete() {
                        playLessonScript(index + 1, isStartScript, actionListener);
                    }
                });
            } else {
                if (null != actionListener) {
                    actionListener.onComplete();
                }
                setActorTouchable(Touchable.enabled);
                setTouchAllow(true);
            }
        }
    }

    private void setActorTouchable(Touchable touchable) {
        if (null != getStages()) {
            for (Stage stage : getStages()) {
                if (null != stage && null != stage.getActors()) {
                    for (Actor actor : stage.getActors()) {
                        actor.setTouchable(touchable);
                    }
                }
            }
        }
    }

    /**
     * Check the given storyModeUserGestureResult is valid, which mean if the object given to the
     *
     * @param storyModeUserGestureResult
     * @return
     */
    private boolean isUserGestureResultSuccess(StoryModeUserGestureResult storyModeUserGestureResult) {
        return null != storyModeUserGestureResult && storyModeUserGestureResult.findAction;
    }

    private void showMessage() {

        hideHintTouch();

        if (null != storyModeUserGestureResult) {
            getMessageActor().setText(storyModeUserGestureResult.message.replace(". ", ".\n"), storyModeUserGestureResult.audioFile, new IActionListener() {
                @Override
                public void onComplete() {
                    if (storyModeUserGestureResult.lessonCompleted) {
                        Gdx.app.log(getClass().getName(), "The lesson is completed");

                        goNextLesson();

                    } else {
                        //after show the message,
                        startShowHintTimer();
                    }
                }
            });
        }
    }

    private void whenLessonComplete() {

        storyModeLogic.saveCurrentLesson(String.valueOf(currentPlayingLessonIndex), UserPreferenceUtils.getInstance().getLanguage());
        hideHintTouch();

        playLessonEndScript(0, new IActionListener() {
            @Override
            public void onComplete() {
                changeLesson();
            }
        });
    }

    private void changeLesson() {
        Gdx.app.log(getClass().getName(), "going to show lesson for index " + currentPlayingLessonIndex);
        if (null != currentLesson) {
            //get next currentLesson
            StoryModeLesson nextLesson = storyModeLogic.getStoryModeLesson(String.valueOf(currentPlayingLessonIndex), UserPreferenceUtils.getInstance().getLanguage());
            Gdx.app.log(getClass().getName(), "The scene in next lesson is " + nextLesson.getScene().getSceneName());

            //check if the scene in the next currentLesson is same to the current currentLesson
            if (currentLesson.getScene().getSceneName().equals(nextLesson.getScene().getSceneName())) {
                List<StoryModeImage> storyModeImageList = currentLesson.getAdditionalScene().getSceneImageObject();
                if (CollectionUtils.isNotEmpty(storyModeImageList)) {
                    //dispose all addition images
                    for (StoryModeImage additionalStoryModeImage : storyModeImageList) {
                        StoryModeObjectActor storyModeObjectActor = getStoryModeObjectActorByObjectName(additionalStoryModeImage.vObjectName);
                        Gdx.app.log(getClass().getName(), "The object " + additionalStoryModeImage.vObjectName + " which is not required to use in lesson " + currentPlayingLessonIndex);
                        if (null != storyModeObjectActor) {
                            storyModeObjectActor.remove();
                            Gdx.app.log(getClass().getName(), "The object " + additionalStoryModeImage.vObjectName + " removed");
                        }
                    }
                }
                currentLesson = nextLesson;
                addActorByStoryModeImageList(getAdditionalStoryModeImageList());


            } else {
                //if the next lesson is in another scene
                clearHintsImage();
                currentLesson = nextLesson;
                changeScene(nextLesson.getScene());
            }

            //make sure the character is bring to front after add new images
            addCharacter();

            moveToPlayLocation();

            playLessonStartScript();

        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (isTouchAllow()) {
            int screenX = (int) ScreenUtils.toViewPosition(x);
            int screenY = (int) ScreenUtils.getExactYPositionOnScreen(y);
            if (TouchUtils.isTouched(previousLessonImage, screenX, screenY)) {
                if (currentPlayingLessonIndex - 1 > 0) {
                    currentPlayingLessonIndex--;
                    whenLessonComplete();
                }
            } else if (TouchUtils.isTouched(nextLessonImage, screenX, screenY)) {

                goNextLesson();
            }
        }
        return super.tap(x, y, count, button);

    }

    private void goNextLesson() {
        if (currentPlayingLessonIndex + 1 < 35) {
            currentPlayingLessonIndex++;
            whenLessonComplete();
        }
    }
}
