package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actors.SaySomethingMessageActor;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SaySomethingScreen extends AbstractAutoCognitaScreen {

    private Stage stage;
    private CustomCamera camera;

    private SaySomethingMessageActor saySomethingMessageActor;

    public SaySomethingScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);

        camera = new CustomCamera();
        camera.setWorldWidth(Gdx.graphics.getWidth());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    protected boolean isRequiredToShowHomeButton() {
        return false;
    }

    @Override
    protected List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    public void show() {
        super.show();

        camera.update();
        if (null == stage) {
            ScreenViewport screenViewport = new ScreenViewport(camera);
            screenViewport.setUnitsPerPixel(ScreenUtils.widthRatio);
            stage = new Stage(screenViewport);
        }

        if (null == saySomethingMessageActor) {
            saySomethingMessageActor = new SaySomethingMessageActor();
            saySomethingMessageActor.setSize(stage.getWidth(), stage.getHeight());
            stage.addActor(saySomethingMessageActor);
        }

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void doRender() {

        if (null != stage) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
        }

        if (null != saySomethingMessageActor) {
            saySomethingMessageActor.stopMicrophoneListener();
        }

        StageUtils.dispose(stage);
        stage = null;


        saySomethingMessageActor = null;
    }


}
