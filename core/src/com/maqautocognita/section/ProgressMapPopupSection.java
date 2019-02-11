package com.maqautocognita.section;

import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actors.ProgressMapPopupActor;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * This class is mainly show all lesson progress include alphabet,phonic and Math
 * <p>
 * Created by siu-chun.chi on 5/18/2017.
 */
public class ProgressMapPopupSection {

    private final IActionListener onCloseListener;
    private final IMenuScreenListener menuScreenListener;
    private final LessonUnitCode defaultDisplayUnit;
    private final AbstractLessonService lessonService;
    private Stage stage;
    private CustomCamera camera;
    private boolean showing;

    public ProgressMapPopupSection(
            AbstractLessonService lessonService,
            LessonUnitCode defaultDisplayUnit,
            IActionListener onCloseListener, IMenuScreenListener menuScreenListener) {
        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

        this.lessonService = lessonService;
        this.defaultDisplayUnit = defaultDisplayUnit;

        this.onCloseListener = onCloseListener;
        this.menuScreenListener = menuScreenListener;
    }

    public void show() {


        camera.update();
        if (null == stage) {
            ScreenViewport screenViewport = new ScreenViewport(camera);
            screenViewport.setUnitsPerPixel(ScreenUtils.widthRatio);
            stage = new Stage(screenViewport);
        }


        stage.addActor(new ProgressMapPopupActor(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight(),
                defaultDisplayUnit, lessonService, onCloseListener, menuScreenListener
        ));


        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        showing = true;
    }


    public void hide() {
        showing = false;


        StageUtils.dispose(stage);
        stage = null;

    }

    public boolean isShowing() {
        return showing;
    }

    public void render() {
        if (null != stage) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

    }


}
