package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IDirectionGestureListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actors.DemoActor;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.TimerService;
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

public class DemoScreen extends AbstractAutoCognitaScreen implements IDirectionGestureListener {

    private Stage stage;
    private CustomCamera camera;

    private TimerService timerService;
    private DemoActor demoActor;

    private int currentImageIndex = 0;

    public DemoScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);

        camera = new CustomCamera();
        camera.setWorldWidth(Gdx.graphics.getWidth());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //this.timerService = new TimerService(this);
        //timerService.startTimer(null,15);
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

        if (null == demoActor) {
            demoActor = new DemoActor();
            demoActor.setSize(stage.getWidth(), stage.getHeight());
            stage.addActor(demoActor);
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

        StageUtils.dispose(stage);
        stage = null;


        demoActor = null;
    }


//    @Override
    public void beforeStartTimer() {

    }

//    @Override
    public void onTimerComplete(Object threadIndicator) {
        addIndex();
            //timerService.startTimer(null, 5);
    }

    private void addIndex(){
        currentImageIndex++;
        switch (currentImageIndex) {
            case 1:
                demoActor.setBackgroundVisible("DemoScreenAddSubtract");
                break;
            case 2:
                demoActor.setBackgroundVisible("DemoScreenAlphabet");
                break;
            case 3:
                demoActor.setBackgroundVisible("DemoScreenComprehension1");
                break;
            case 4:
                demoActor.setBackgroundVisible("DemoScreenComprehension2");
                break;
            case 5:
                demoActor.setBackgroundVisible("DemoScreenComprehension3");
                break;
            case 6:
                demoActor.setBackgroundVisible("DemoScreenCount");
                break;
            case 7:
                demoActor.setBackgroundVisible("DemoScreenMultiply");
                break;
            case 8:
                demoActor.setBackgroundVisible("DemoScreenPhonics1");
                break;
            case 9:
                demoActor.setBackgroundVisible("DemoScreenPhonics2");
                break;
            case 10:
                demoActor.setBackgroundVisible("DemoScreenPhonics3");
                break;
            case 11:
                demoActor.setBackgroundVisible("DemoScreenPhonics4");
                break;
            case 12:
                demoActor.setBackgroundVisible("DemoScreenWord1");
                break;
            case 13:
                demoActor.setBackgroundVisible("DemoScreenWord2");
                break;
            case 14:
                demoActor.setBackgroundVisible("DemoScreenWord3");
                break;
            case 15:
                demoActor.setBackgroundVisible("DemoScreenWord4");
                break;
            default:
                currentImageIndex = 0;
        }
    }

    private void descIndex(){
        currentImageIndex--;
        switch (currentImageIndex) {
            case 1:
                demoActor.setBackgroundVisible("DemoScreenAddSubtract");
                break;
            case 2:
                demoActor.setBackgroundVisible("DemoScreenAlphabet");
                break;
            case 3:
                demoActor.setBackgroundVisible("DemoScreenComprehension1");
                break;
            case 4:
                demoActor.setBackgroundVisible("DemoScreenComprehension2");
                break;
            case 5:
                demoActor.setBackgroundVisible("DemoScreenComprehension3");
                break;
            case 6:
                demoActor.setBackgroundVisible("DemoScreenCount");
                break;
            case 7:
                demoActor.setBackgroundVisible("DemoScreenMultiply");
                break;
            case 8:
                demoActor.setBackgroundVisible("DemoScreenPhonics1");
                break;
            case 9:
                demoActor.setBackgroundVisible("DemoScreenPhonics2");
                break;
            case 10:
                demoActor.setBackgroundVisible("DemoScreenPhonics3");
                break;
            case 11:
                demoActor.setBackgroundVisible("DemoScreenPhonics4");
                break;
            case 12:
                demoActor.setBackgroundVisible("DemoScreenWord1");
                break;
            case 13:
                demoActor.setBackgroundVisible("DemoScreenWord2");
                break;
            case 14:
                demoActor.setBackgroundVisible("DemoScreenWord3");
                break;
            case 15:
                demoActor.setBackgroundVisible("DemoScreenWord4");
                break;
            default:
                currentImageIndex = 0;
        }
    }

    @Override
    public void onLeft() {
        addIndex();
    }

    @Override
    public void onRight() {
        descIndex();
    }

    @Override
    public void onUp() {

    }

    @Override
    public void onDown() {

    }
}
