package com.maqautocognita.screens;

import com.maqautocognita.AutoCognitaStoryLibraryGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.StoryLibraryBook;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.scene2d.actors.HighlightImageActor;
import com.maqautocognita.scene2d.actors.StoryBookActor;
import com.maqautocognita.scene2d.ui.PagedScrollPane;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.StoryLibraryService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 4/24/2017.
 */

public class StoryLibraryBookScreen extends AbstractAutoCognitaScreen implements IScrollPaneListener {

    private static final int ICON_PADDING = 50;
    private static final int DOT_PADDING_TO_RIGHT = 20;
    private static final int ARROW_SCALE = 5;
    private final AutoCognitaStoryLibraryGame game;
    private Stage stage;
    private CustomCamera camera;
    private int storyBookNumber;
    private PagedScrollPane bookScrollPane;
    private Image hintRight;
    private Image hintLeft;
    private Image homeIcon;
    private int currentPageIndex;
    private List<HighlightImageActor> navigationDotList;
    private List<StoryLibraryBook> storyLibraryBookList;

    public StoryLibraryBookScreen(AutoCognitaStoryLibraryGame game, int storyBookNumber) {
        super(game, null);
        this.game = game;
        ScreenUtils.isTablet = true;
        setStoryBookNumber(storyBookNumber);
        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

    }

    public void setStoryBookNumber(int storyBookNumber) {
        this.storyBookNumber = storyBookNumber;
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

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

        if (null == homeIcon) {
            homeIcon = new Image(new Texture(Config.IMAGE_FOLDER_NAME + "library_bookshelf.png"));
            homeIcon.setPosition(ICON_PADDING, Gdx.graphics.getHeight() - homeIcon.getHeight() - ICON_PADDING);

            homeIcon.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    game.showStoryLibraryHomeScreen();
                }
            });
            stage.addActor(homeIcon);
        }

        if (null == storyLibraryBookList) {
            storyLibraryBookList = StoryLibraryService.getInstance().getAllStoryBookPage(storyBookNumber);
        }

        if (null == bookScrollPane) {
            bookScrollPane = new PagedScrollPane(this);
            bookScrollPane.setFillParent(true);
            stage.addActor(bookScrollPane);
        }

        if (null == hintLeft) {
            hintLeft = new Image(new Texture(Config.IMAGE_FOLDER_NAME + "hint_left.png"));

            hintLeft.setScale((Gdx.graphics.getHeight() / ARROW_SCALE) / hintLeft.getHeight());

            hintLeft.setPosition(10,
                    ScreenUtils.getBottomYPositionForCenterObject(hintLeft.getHeight() * hintLeft.getScaleY(), Gdx.graphics.getHeight()));

            hintLeft.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (currentPageIndex > 0) {
                        currentPageIndex--;
                        showNextPage();
                    }
                }
            });

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
                    if (currentPageIndex < storyLibraryBookList.size()) {
                        currentPageIndex++;
                        showNextPage();
                    }
                }
            });
            stage.addActor(hintRight);
        }


        int index = 0;
        float startDotXPosition = homeIcon.getX() + homeIcon.getWidth() + ICON_PADDING * 2;
        String generalIconPath = Config.IMAGE_FOLDER_NAME + "General Icons.png";

        for (StoryLibraryBook storyLibraryBook : storyLibraryBookList) {
            StoryBookActor storyBookActor = new StoryBookActor(storyLibraryBook, this, hintLeft.getWidth() + 100);
            storyBookActor.setWidth(ScreenUtils.getScreenWidth());
            bookScrollPane.addPage(storyBookActor, null);

            final HighlightImageActor<Integer> dot = new HighlightImageActor(index,
                    generalIconPath, NavigationSection.SELECTED_DOT_ICON_POSITION,
                    generalIconPath, NavigationSection.DISABLE_DOT_ICON_POSITION);

            if (null == navigationDotList) {
                navigationDotList = new ArrayList<HighlightImageActor>();
            }

            navigationDotList.add(dot);

            dot.setPosition(startDotXPosition, homeIcon.getY() + (homeIcon.getHeight() - dot.getHeight()) / 2);

            dot.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    currentPageIndex = dot.getId();
                    showNextPage();
                }
            });

            stage.addActor(dot);

            startDotXPosition += dot.getWidth() + DOT_PADDING_TO_RIGHT;

            index++;
        }


        homeIcon.toFront();

        showNextPage();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }

        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    private void showNextPage() {
        //deselected all navigation dot
        for (HighlightImageActor dot : navigationDotList) {
            dot.setHighlighted(false);
        }

        navigationDotList.get(currentPageIndex).setHighlighted(true);
        if (0 == currentPageIndex) {
            hintLeft.setVisible(false);
            hintRight.setVisible(true);
        } else if (currentPageIndex == storyLibraryBookList.size() - 1) {
            hintRight.setVisible(false);
            hintLeft.setVisible(true);
        } else {
            hintRight.setVisible(true);
            hintLeft.setVisible(true);
        }

        bookScrollPane.scrollToPageWithoutTriggerListener(currentPageIndex);
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
        bookScrollPane = null;
        hintRight = null;
        hintLeft = null;
        homeIcon = null;
        storyLibraryBookList = null;
        currentPageIndex = 0;
        if (null != navigationDotList) {
            navigationDotList.clear();
            navigationDotList = null;
        }
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
        }
        StageUtils.dispose(stage);
        stage = null;
    }

    @Override
    public void onPaneChanged(Object object, int paneIndex) {
        currentPageIndex = paneIndex;
        showNextPage();
    }
}
