package com.maqautocognita.screens;

import com.maqautocognita.AutoCognitaStoryLibraryGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.StoryLibraryBook;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.scene2d.actors.StoryLibraryBookActor;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.StoryLibraryService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

/**
 * Created by siu-chun.chi on 4/24/2017.
 */

public class StoryLibraryHomeScreen extends AbstractAutoCognitaScreen {

    private static final int ARROW_SCALE = 5;
    private final AutoCognitaStoryLibraryGame game;
    private Stage stage;
    private CustomCamera camera;
    private Table storyBookTable;
    private Image hintLeft;
    private Image hintRight;
    private ScrollPane scrollPane;
    private float lastScrollX;
    private boolean isScrolledToPreviousPosition;

    public StoryLibraryHomeScreen(AutoCognitaStoryLibraryGame game) {
        super(game, null);
        ScreenUtils.isTablet = true;
        this.game = game;
        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    protected void onHomeClick() {
        Gdx.app.exit();
        FontGeneratorManager.clear();
    }

    @Override
    protected List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    protected String getAudioPath() {
        return "audios";
    }

    @Override
    public void show() {
        super.show();

        camera.update();
        if (null == stage) {
            stage = new Stage(new ScreenViewport(camera));
        }


        List<StoryLibraryBook> storyLibraryBookList = StoryLibraryService.getInstance().getAllStoryLibraryBook();

        if (null == storyBookTable) {
            storyBookTable = new Table();
            for (StoryLibraryBook storyLibraryBook : storyLibraryBookList) {
                Cell cell = storyBookTable.
                        add(new StoryLibraryBookActor(storyLibraryBook, this, game)).top()
                        .padLeft(getHomeIconScreenPosition().x + HOME_ICON_POSITION.width);
                cell.width((ScreenUtils.getScreenWidth() - cell.getPadLeft()) / 3);
            }

            scrollPane = new ScrollPane(storyBookTable);
            scrollPane.setScrollingDisabled(false, true);
            scrollPane.setFillParent(true);

            scrollPane.addListener(new EventListener() {

                @Override
                public boolean handle(Event event) {

                    if (null != storyBookTable) {
                        Cell lastCell = storyBookTable.getCells().get(storyBookTable.getCells().size - 1);

                        Gdx.app.log(getClass().getName(), "scroll x= " + scrollPane.getScrollX() + ", max x = " + scrollPane.getMaxX());

                        if (scrollPane.getScrollX() <= storyBookTable.getCells().get(0).getActorX()) {
                            hintLeft.setVisible(false);
                        } else if (scrollPane.getScrollX() >= scrollPane.getMaxX() - 100) {
                            hintRight.setVisible(false);
                        } else {
                            hintLeft.setVisible(true);
                            hintRight.setVisible(true);
                        }

                        if (isScrolledToPreviousPosition) {
                            lastScrollX = scrollPane.getScrollX();
                        }
                    }

                    return false;
                }
            });

            stage.addActor(scrollPane);


        }

        if (null == hintLeft) {
            hintLeft = new Image(new Texture(Config.IMAGE_FOLDER_NAME + "hint_left.png"));

            hintLeft.setScale((Gdx.graphics.getHeight() / ARROW_SCALE) / hintLeft.getHeight());

            hintLeft.setPosition(10,
                    ScreenUtils.getBottomYPositionForCenterObject(hintLeft.getHeight() * hintLeft.getScaleY(), Gdx.graphics.getHeight()));

            hintLeft.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    scrollPane.setScrollX(scrollPane.getScrollX() - storyBookTable.getCells().get(0).getActorWidth());
                }
            });

            hintLeft.setVisible(false);

            stage.addActor(hintLeft);
        }

        if (null == hintRight) {
            hintRight = new Image(new Texture(Config.IMAGE_FOLDER_NAME + "hint_right.png"));
            hintRight.setPosition(ScreenUtils.getScreenWidth() - 10 - hintRight.getWidth(),
                    ScreenUtils.getStartYPositionForCenterObject(hintRight.getHeight()));
            hintRight.setScale((Gdx.graphics.getHeight() / ARROW_SCALE) / hintRight.getHeight());
            hintRight.setPosition(Gdx.graphics.getWidth() - hintRight.getWidth() * hintRight.getScaleX() - 10,
                    ScreenUtils.getBottomYPositionForCenterObject(hintRight.getHeight() * hintRight.getScaleY(), Gdx.graphics.getHeight()));
            hintRight.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    scrollPane.setScrollX(scrollPane.getScrollX() + storyBookTable.getCells().get(0).getActorWidth());
                }
            });
            stage.addActor(hintRight);
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

            if (!isScrolledToPreviousPosition && null != scrollPane) {
                Gdx.app.log(getClass().getName(), "last scroll x = " + lastScrollX);
                scrollPane.setScrollX(lastScrollX);
                isScrolledToPreviousPosition = true;
            }
        }
    }

    @Override
    protected String getIconPath() {
        return Config.IMAGE_FOLDER_NAME + "icons.png";
    }

    @Override
    public void hide() {
        super.hide();
        StageUtils.dispose(stage);
        storyBookTable = null;
        scrollPane = null;
        hintRight = null;
        hintRight = null;
        isScrolledToPreviousPosition = false;
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
        }
    }
}
