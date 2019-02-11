package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.ICameraMoveListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.storyMode.StoryModeImage;
import com.maqautocognita.prototype.storyMode.StoryModeLogic;
import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.maqautocognita.prototype.storyMode.StoryModeSceneConjunction;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.actors.MainSceneStage;
import com.maqautocognita.scene2d.actors.StoryModeObjectActor;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * please see https://www.codeandweb.com/texturepacker/tutorials/libgdx-physics example
 *
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractStoryModeScreen extends AbstractStoryScreen implements TimerService.ITimerListener, ICameraMoveListener {

    /**
     * number of second need to wait for showing the hint, if there is no action from user,  the hint will be shown after the below second
     */
    private static float SECOND_TO_SHOW_HINT = 3;
    private final TimerService timerService;
    protected CustomCamera mainCamera;
    protected StoryModeScene currentStoryModeScene;
    protected Stage mainStage;
    /**
     * It is store the current zoom scale, when the user touch down the screen it will be assigned to the current zoom scale,
     * when the user is pinch the screen, it will be used to calculate the zoom ratio, which mean to calculate the object size after zoom
     */
    protected float currentScale = 1;
    protected StoryModeLogic storyModeLogic;
    private List<StoryModeObjectActor> storyModeObjectActorList;
    /**
     * The stage will is mainly used to store the background image, and it will render as the first layer, before everything
     */
    private Stage backgroundStage;
    private CustomCamera secondCamera;
    private Stage secondStage;
    private CustomCamera thirdCamera;
    private Stage thirdStage;
    private StoryModeObjectActor focusingActor;
    private ImageActor hintTouchImage;
    private Map<String, StoryModeObjectActor> sceneActorMap;

    public AbstractStoryModeScreen(Database storyModeDatabase, AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(game, menuScreenListener);
        storyModeLogic = new StoryModeLogic(storyModeDatabase, analyticSpotService);
        timerService = new TimerService(this);
    }

    @Override
    protected boolean isRequiredToShowCharacterSetupButton() {
        return true;
    }

    @Override
    public void show() {
        super.show();
        preload();
    }

    protected Stage getMainStage() {
        return mainStage;
    }

    @Override
    public void hide() {
        super.hide();
        currentStoryModeScene = null;
        clearShowHintTimer();
    }

    @Override
    public void dispose() {

        clearAllStageAndCamera();

        super.dispose();

        clearListOfActors();


    }

    protected void clearAllStageAndCamera() {
        disposeStage(backgroundStage);
        backgroundStage = null;

        disposeStage(mainStage);
        mainStage = null;

        disposeStage(secondStage);
        secondStage = null;

        disposeStage(thirdStage);
        thirdStage = null;

        mainCamera = null;
        secondCamera = null;
        thirdCamera = null;

        clearCharacterAndInputTouchDetector();
        clearHintsImage();
    }

    protected void clearListOfActors() {
        if (null != storyModeObjectActorList) {
            storyModeObjectActorList.clear();
        }
    }

    protected void clearHintsImage() {
        hintTouchImage = null;
    }

    protected Stage[] getStages() {
        return new Stage[]{backgroundStage, thirdStage, secondStage, mainStage};
    }

    protected CustomCamera[] getCameras() {
        return new CustomCamera[]{mainCamera, secondCamera, thirdCamera};
    }

    /**
     * This is used to preload some thing such as image to memory, it is useful when you want preload something before show the screen
     */
    public void preload() {
        if (null == currentStoryModeScene) {
            currentStoryModeScene = getCurrentStoryModeScene();
            if (null != currentStoryModeScene && null != currentStoryModeScene.getSceneImageObject()) {
                reloadScene(currentStoryModeScene);
            }
        }
    }

    protected void clearShowHintTimer() {
        timerService.clearTimer();
    }

    protected void hideHintTouch() {
        if (null != hintTouchImage) {
            hintTouchImage.setVisible(false);
        }
    }

    /**
     * this is method is mainly used to highlight the next object which the user will need to play,
     * It will  check if the given objectName is rendered on the screen, if yes, repeat the scale action of the actor
     *
     * @param objectName
     */
    protected void highlightObject(String objectName, boolean isRequiredCameraMove) {
        Gdx.app.log(getClass().getName(), "find the next play object  = " + objectName);
        if (StringUtils.isNotBlank(objectName)) {
            highlightObject(getStoryModeObjectActorByObjectName(objectName), isRequiredCameraMove);
        }
    }

    protected void highlightObject(StoryModeObjectActor object, boolean isRequiredCameraMove) {

        if (null != object) {
            if (currentScale != 1) {
                //restore the zoom scale, make sure it is in default state before highlight the new object
                mainCamera.zoom = 1;
                mainCamera.restoreYPosition();
                afterZoomOut();
            }

            if (null != focusingActor) {
                focusingActor.clearActions();
            }

            Gdx.app.log(getClass().getName(), "going to highlight object " + object.getStoryModeImage().vObjectName);
            focusingActor = object;


            final float focusingActorXPositionInTheScene = focusingActor.getStoryModeImage().vSceneLocationX;
            final float focusingActorYPositionInTheScene = focusingActor.getStoryModeImage().vSceneLocationY;

            Gdx.app.log(getClass().getName(), "The object " + object.getStoryModeImage().vObjectName + " is located in " + focusingActorXPositionInTheScene);
            Gdx.app.log(getClass().getName(), "The object " + object.getStoryModeImage().vObjectName + " with width = " + focusingActor.getWidthAfterScale() + " and height = " + focusingActor.getHeightAfterScale());


            if (isRequiredCameraMove) {
                mainCamera.updateCameraXPosition(focusingActorXPositionInTheScene);
            }

            if (null == hintTouchImage) {
                hintTouchImage = new ImageActor(Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/hint_touch.png", focusingActorXPositionInTheScene,
                        focusingActorYPositionInTheScene, 0, 0);
                mainStage.addActor(hintTouchImage);
            } else {
                hintTouchImage.setPosition(focusingActorXPositionInTheScene, focusingActorYPositionInTheScene);
            }

            hintTouchImage.toFront();

            AnimationUtils.doObjectIndication(hintTouchImage, focusingActor);

        }
    }

    protected StoryModeObjectActor getStoryModeObjectActorByObjectName(String objectName) {
        if (StringUtils.isNotBlank(objectName) && CollectionUtils.isNotEmpty(storyModeObjectActorList)) {
            for (StoryModeObjectActor storyModeObjectActor : storyModeObjectActorList) {
                if (storyModeObjectActor.getStoryModeImage().vObjectName.equals(objectName)) {
                    return storyModeObjectActor;
                }
            }
        }

        Gdx.app.log(getClass().getName(), "the object " + objectName + " is not found");

        return null;
    }

    protected void afterZoomOut() {

    }

    protected abstract StoryModeScene getCurrentStoryModeScene();

    protected void reloadScene(StoryModeScene storyModeScene) {
        addActorByStoryModeImageList(storyModeScene.getSceneImageObject());

        whenAllSceneObjectCreate();

        addActorByStoryModeImageList(getAdditionalStoryModeImageList());

        afterScenePreloaded();
    }

    protected void addActorByStoryModeImageList(List<StoryModeImage> storyModeImageList) {
        if (CollectionUtils.isNotEmpty(storyModeImageList)) {
            for (final StoryModeImage storyModeImage : storyModeImageList) {
                addActorByStoryModeImage(storyModeImage);
                Gdx.app.log(getClass().getName(), "The object " + storyModeImage.vObjectName + " added to the lesson  with x=" + storyModeImage.vSceneLocationX + " and y = " + storyModeImage.vSceneLocationY);
            }
        }
    }

    protected void whenAllSceneObjectCreate() {

    }

    protected List<StoryModeImage> getAdditionalStoryModeImageList() {
        return null;
    }

    /**
     * It will be call when the scene is preload, at the end of the {@link #preload()}, which mean every object are loaded and added to a stage
     */
    protected void afterScenePreloaded() {

    }

    protected void addActorByStoryModeImage(StoryModeImage storyModeImage) {
        if (null != storyModeImage) {
            /**
             * In here those image name prefix is "background" will be add into {@link #backgroundStage}
             *
             */
            StoryModeObjectActor actor = null;
            if (storyModeImage.vImageName.indexOf("background") >= 0) {
                addImageToBackgroundStage(storyModeImage);
            } else if (1 == storyModeImage.vLayer) {

                actor = addImageToMainStage(storyModeImage);

                final StoryModeSceneConjunction storyModeSceneConjunction = getCurrentStoryModeScene().checkSceneConjunction(storyModeImage.vObjectName);
                if (StringUtils.isNotBlank(storyModeSceneConjunction.vToScene)) {
                    Gdx.app.log(getClass().getName(), storyModeImage.vObjectName + " will enter the scene " +
                            storyModeSceneConjunction.vToScene);

                    if (null == sceneActorMap) {
                        sceneActorMap = new HashMap<String, StoryModeObjectActor>();
                    }
                    sceneActorMap.put(storyModeSceneConjunction.vToScene, actor);
                    actor.addListener(new ActorGestureListener() {
                        @Override
                        public void tap(InputEvent event, float x, float y, int count, int button) {
                            changeScene(storyModeLogic.getStoryModeScene(storyModeSceneConjunction.vToScene, ScreenUtils.getSceneRatio()));
                        }
                    });
                }

            } else if (2 == storyModeImage.vLayer) {
                actor = addImageToSecondStage(storyModeImage);
            } else {
                actor = addImageToThirdStage(storyModeImage);
            }

            if (null != actor) {
                afterStoryModeObjectActorCreated(actor);
            }
        }
    }

    private StoryModeObjectActor addImageToBackgroundStage(StoryModeImage storyModeImage) {
        if (null == backgroundStage) {
            initBackgroundStage();
        }
        StoryModeObjectActor actor = new StoryModeObjectActor(storyModeImage);
        backgroundStage.addActor(actor);
        return actor;
    }

    private StoryModeObjectActor addImageToMainStage(StoryModeImage storyModeImage) {
        if (null == mainStage) {
            initMainStage();
        }

        StoryModeObjectActor actor = new StoryModeObjectActor(storyModeImage);

        mainStage.addActor(actor);

        return actor;
    }

    private StoryModeObjectActor addImageToSecondStage(StoryModeImage storyModeImage) {
        if (null == secondStage) {
            initSecondStage();
        }
        StoryModeObjectActor actor = new StoryModeObjectActor(storyModeImage);
        secondStage.addActor(actor);
        return actor;
    }

    private StoryModeObjectActor addImageToThirdStage(StoryModeImage storyModeImage) {
        if (null == thirdStage) {
            initThirdStage();
        }
        StoryModeObjectActor actor = new StoryModeObjectActor(storyModeImage);
        thirdStage.addActor(actor);
        return actor;
    }

    /**
     * This method will be call when the given storyModeObjectActor is created and added to the stage such as {@link #mainStage}
     *
     * @param storyModeObjectActor
     */
    protected void afterStoryModeObjectActorCreated(StoryModeObjectActor storyModeObjectActor) {
        if (null == storyModeObjectActorList) {
            storyModeObjectActorList = new ArrayList<StoryModeObjectActor>();
        }

        storyModeObjectActorList.add(storyModeObjectActor);
    }

    private void initBackgroundStage() {
        initMainCamera();
        ScreenViewport screenViewport = new ScreenViewport(mainCamera);
        backgroundStage = new Stage(screenViewport);
    }

    private void initMainStage() {
        initMainCamera();

        ScreenViewport screenViewport = new ScreenViewport(mainCamera);
        //screenViewport.update(currentStoryModeScene.getvSceneArea().vImageWidth,Config.TABLET_SCREEN_HEIGHT);
        mainStage = new MainSceneStage(screenViewport);
        addInputProcessor(mainStage);
    }

    private void initSecondStage() {
        if (null == secondCamera) {
            secondCamera = new CustomCamera(0.5f);
            secondCamera.setWorldWidth(currentStoryModeScene.getvSceneArea().vImageWidth);
            mainCamera.addCamera(secondCamera);
        }
        ScreenViewport screenViewport = new ScreenViewport(secondCamera);
        secondStage = new Stage(screenViewport);
        addInputProcessor(secondStage);
    }

    private void initThirdStage() {
        if (null == thirdCamera) {
            thirdCamera = new CustomCamera(0.25f);
            thirdCamera.setWorldWidth(currentStoryModeScene.getvSceneArea().vImageWidth);

            Gdx.app.log(this.getClass().getName(), "width = " + currentStoryModeScene.getvSceneArea().vImageWidth);

            mainCamera.addCamera(thirdCamera);
        }

        ScreenViewport screenViewport = new ScreenViewport(thirdCamera);
        thirdStage = new Stage(screenViewport);
        addInputProcessor(thirdStage);
    }

    private void initMainCamera() {
        if (null == mainCamera) {
            mainCamera = new CustomCamera();
            mainCamera.setWorldWidth(currentStoryModeScene.getvSceneArea().vImageWidth);
            mainCamera.setCameraMoveListener(this);
        }
    }

    protected abstract String getSceneName();

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        super.touchDown(x, y, pointer, button);
        if (null != mainCamera) {
            currentScale = mainCamera.zoom;
        }
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        float ratio = initialPointer1.dst(initialPointer2) / pointer1.dst(pointer2);
        //the app is only allow zoom in, and zoom out if the zoom scale is larger than default 1
        float zoomScale = Math.min(1, Math.max(0.3f, currentScale * ratio));
        mainCamera.zoom = zoomScale;
        if (null != secondCamera) {
            secondCamera.zoom = zoomScale;
        }
        if (null != thirdCamera) {
            thirdCamera.zoom = zoomScale;
        }

        afterZoom(zoomScale);

        mainCamera.update();


        return false;
    }

    protected void afterZoom(float zoomScale) {
        if (1 == zoomScale ||
                //if the camera is out of the background bottom point
                mainCamera.frustum.pointInFrustum(mainCamera.position.x, -1, 0) ||
                //or the camera is out of the background top point
                mainCamera.frustum.pointInFrustum(mainCamera.position.x, Config.TABLET_SCREEN_HEIGHT + 1, 0)) {
            mainCamera.restoreYPosition();

            afterZoomOut();
        }
    }

    protected StoryModeObjectActor getSceneEntryActor(String sceneName) {
        if (null != sceneActorMap) {
            return sceneActorMap.get(sceneName);
        }

        return null;
    }

    protected void changeScene(StoryModeScene storyModeScene) {

        if (null != sceneActorMap) {
            sceneActorMap.clear();
        }

        currentStoryModeScene = storyModeScene;
        //dispose the current screen
        clearAllStageAndCamera();

        clearListOfActors();
        clearBackgroundMusic();

        //add the given scene images
        reloadScene(storyModeScene);
        //reload the input processor because of the previous scene are disposed
        reloadInputProcessor();
        //reload the cameras because of the previous scene are disposed
        reloadCamera();

        afterSceneChanged();
    }

    protected void afterSceneChanged() {

    }

    @Override
    public void beforeStartTimer() {

    }

    /***
     * Start to timer ,when the timer is complete, it will show the hint touch to a object which required to play
     */
    protected void startShowHintTimer() {
        clearShowHintTimer();
        timerService.startTimer(null, SECOND_TO_SHOW_HINT);
    }

    public void onMove(CustomCamera customCamera) {

    }
}
