package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.DirectionGestureDetector;
import com.maqautocognita.listener.IDirectionGestureListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actions.CameraMoveXAction;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.actors.ProgressMapPopupActor;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.service.MathLessonService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;
import java.util.Timer;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class ProgressMapScreen extends AbstractAutoCognitaScreen implements IDirectionGestureListener {


    protected static final int TOP_MENU_MARGIN_TOP = 20;
    protected static final int ARROW_ICON_SIZE = 100;

    protected Stage stage;
    protected Stage fixedStage;
    protected CustomCamera camera;
    protected ProgressMapPopupActor readingProgressActor;
    protected ProgressMapPopupActor mathProgressActor;
    private CustomCamera fixedStageCamera;
    private ImageActor rightArrow;
    private ImageActor leftArrow;
    private CameraMoveXAction cameraMoveXAction;
    private float previousCameraX;
    private boolean isShowOneProgressMapOnly;
    private Timer timer;

    public ProgressMapScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
        timer = new Timer();
        //scale the image to fit to the container after 1 seconds
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//
//                drop(source, payload, x, y, pointer);
//            }
//        }, 1000);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return AlphabetLessonService.getInstance();
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        float screenX = ScreenUtils.toViewPosition(x);
        float screenY = ScreenUtils.getExactYPositionOnScreen(y);
        if (TouchUtils.isTouched(rightArrow, (int) screenX, (int) screenY)) {
            CameraMoveXAction cameraMoveXAction = new CameraMoveXAction(ScreenUtils.getScreenWidth(), camera, 1.5f, new IActionListener() {
                @Override
                public void onComplete() {
                    whenMoveRightComplete(camera.position.x);
                }
            });

            beforeCameraMovedToRight();
            stage.addAction(cameraMoveXAction);
        } else if (TouchUtils.isTouched(leftArrow, (int) screenX, (int) screenY) &&
                !onLeftArrowTap()) {
            CameraMoveXAction cameraMoveXAction = new CameraMoveXAction(-ScreenUtils.getScreenWidth(), camera, 1.5f, new IActionListener() {
                @Override
                public void onComplete() {
                    whenMoveLeftComplete(camera.position.x);
                }
            });
            beforeCameraMovedToLeft();
            stage.addAction(cameraMoveXAction);
        }

        return false;
    }

    @Override
    protected boolean isRequiredToShowHomeButton() {
        return false;
    }

    @Override
    protected List<AbstractAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    public void show() {
        super.show();
        if (null == stage) {
            camera = new CustomCamera();
            camera.setWorldWidth(getTotalScreenWidth());
            stage = new Stage(new ScreenViewport(camera));
            camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            camera.update();

            stage.addListener(new ActorGestureListener() {

                @Override
                public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                    super.fling(event, velocityX, velocityY, button);
                    if (isAllowMoveToProgressMap()) {
                        if (null != cameraMoveXAction) {
                            cameraMoveXAction.reset();
                        }
                        float movedDistance;

                        if (velocityX > 0) {
                            //move back
                            movedDistance = -(ScreenUtils.getScreenWidth() + (camera.position.x - previousCameraX));
                        } else {
                            //move forward
                            float startScreenPosition = ScreenUtils.getScreenWidth();
                            if (camera.position.x > ScreenUtils.getScreenWidth()) {
                                startScreenPosition = getTotalScreenWidth() - startScreenPosition;
                            }
                            movedDistance = startScreenPosition - (camera.position.x - camera.viewportWidth / 2);
                        }

                        if (movedDistance > 0 && camera.position.x + movedDistance > getTotalScreenWidth()) {
                            //move right
                            movedDistance = getTotalScreenWidth() - camera.position.x;
                        } else if (movedDistance < 0 && camera.position.x + movedDistance < camera.viewportWidth / 2) {
                            //move left
                            movedDistance = camera.viewportWidth / 2 - camera.position.x;
                        }

                        Gdx.app.log(getClass().getName(), "movedDistance = " + movedDistance +
                                " camera x = " + camera.position.x + " previous camera x =" + previousCameraX);
                        final boolean isMoveLeft = movedDistance < 0;

                        if (isMoveLeft) {
                            beforeCameraMovedToLeft();
                        } else {
                            beforeCameraMovedToRight();
                        }
                        moveCamera(movedDistance);
                    }

                }

                @Override
                public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                    super.pan(event, x, y, deltaX, deltaY);
                    if (isAllowMoveToProgressMap()) {
                        if (deltaX < 0) {
                            camera.translateCameraXPosition(20);
                        } else {
                            camera.translateCameraXPosition(-20);
                        }
                    }
                }

            });

            InputMultiplexer inputMultiplexer = new InputMultiplexer();
            inputMultiplexer.addProcessor(new AutoCognitaGestureDetector(this));
            inputMultiplexer.addProcessor(stage);
            Gdx.input.setInputProcessor(inputMultiplexer);

            addGestureListener();
        }

        if (null == fixedStage) {
            fixedStageCamera = new CustomCamera();
            fixedStageCamera.setWorldWidth(ScreenUtils.getScreenWidth());
            fixedStage = new Stage(new ScreenViewport(fixedStageCamera), mainBatch);
            fixedStageCamera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            fixedStageCamera.update();
        }

        if (null == readingProgressActor) {
            readingProgressActor = new ProgressMapPopupActor(ScreenUtils.getScreenWidth(),
                    ScreenUtils.getScreenHeight() - ARROW_ICON_SIZE,
                    LessonUnitCode.ALPHABET, AlphabetLessonService.getInstance(), null, menuScreenListener);
            readingProgressActor.hideCloseButton();
            stage.addActor(readingProgressActor);
        }

        if (null == mathProgressActor) {
//            mathProgressActor = new ProgressMapPopupActor(readingProgressActor.getWidth(), readingProgressActor.getHeight(),
//                    LessonUnitCode.MATH_1, MathLessonService.getInstance(), null, menuScreenListener);
            mathProgressActor = new ProgressMapPopupActor(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight() - ARROW_ICON_SIZE,
                    LessonUnitCode.MATH_1, MathLessonService.getInstance(), null, menuScreenListener);
            mathProgressActor.hideCloseButton();
            stage.addActor(mathProgressActor);
        }

        showAllProgressMapOnly();
    }

    private float getTotalScreenWidth() {
        return ScreenUtils.getScreenWidth() * (isShowOneProgressMapOnly ? 2 : 3);
    }

    protected boolean isAllowMoveToProgressMap() {
        return true;
    }

    private void moveCamera(float movedDistance) {

        cameraMoveXAction = new CameraMoveXAction(movedDistance,
                camera, 1f);

        cameraMoveXAction.setActionListener(new IActionListener() {

            private final int cameraMoveXHashCode = cameraMoveXAction.hashCode();

            @Override
            public void onComplete() {
                if (cameraMoveXHashCode == cameraMoveXAction.hashCode()) {
                    if (camera.position.x <= camera.viewportWidth) {
                        whenMoveLeftComplete(camera.position.x);
                    } else {
                        whenMoveRightComplete(camera.position.x);
                    }
                    previousCameraX = camera.position.x;
                }
            }
        });

        stage.addAction(cameraMoveXAction);
    }

    private void addGestureListener() {
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(0, new DirectionGestureDetector(this));
        }
    }

    protected void showAllProgressMapOnly() {
        readingProgressActor.setX(ScreenUtils.getScreenWidth());
        mathProgressActor.setX(readingProgressActor.getX() + readingProgressActor.getWidth());
        readingProgressActor.setVisible(true);
        mathProgressActor.setVisible(true);
        isShowOneProgressMapOnly = false;
    }

    @Override
    public void doRender() {
        if (null != stage) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        if (null != fixedStage) {
            fixedStage.act(Gdx.graphics.getDeltaTime());
            fixedStage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (null != stage) {
            stage.getViewport().update(width, height);
            camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            camera.update();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (null != rightArrow) {
            rightArrow.dispose();
            rightArrow = null;
        }

        if (null != leftArrow) {
            leftArrow.dispose();
            leftArrow = null;
        }

        StageUtils.dispose(stage);
        stage = null;
        StageUtils.dispose(fixedStage);
        fixedStage = null;

        camera = null;
        fixedStageCamera = null;

        readingProgressActor = null;

        mathProgressActor = null;
    }

    private void whenMoveRightComplete(float cameraPositionX) {
        if (cameraPositionX <
                (isShowOneProgressMapOnly ? ScreenUtils.getScreenWidth() : ScreenUtils.getScreenWidth() * 2)) {
            showRightArrow();
        }
        showLeftArrow();
        afterCameraMovedToRight();
    }

    protected void beforeCameraMovedToRight() {
        rightArrow.setVisible(false);
    }

    protected boolean onLeftArrowTap() {
        return false;
    }

    private void whenMoveLeftComplete(float cameraPositionX) {
        if (cameraPositionX > ScreenUtils.getScreenWidth()) {
            showLeftArrow();
        }
        showRightArrow();
        afterCameraMovedToLeft();
    }

    protected void beforeCameraMovedToLeft() {
        leftArrow.setVisible(false);
    }

    protected void showRightArrow() {
        rightArrow.setVisible(true);
    }

    protected void showLeftArrow() {
        leftArrow.setVisible(true);
    }

    protected void afterCameraMovedToRight() {

    }

    protected void afterCameraMovedToLeft() {

    }

    @Override
    public void onLeft() {
    }

    @Override
    public void onRight() {
        onLeftArrowTap();
    }

    @Override
    public void onUp() {

    }

    @Override
    public void onDown() {

    }
}
