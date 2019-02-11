package com.maqautocognita.section;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Lesson;
import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * This is mainly show in the top side of the alphabet and phonic screen, act as a navigation bar
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class NavigationSection<T extends Lesson, I extends NavigationSection.ILessonChangeListener> extends AbstractAutoCognitaSection implements AnimateTextureScreenObject.IAnimationListener {

    public static final IconPosition LEFT_ARROW_POSITION = new IconPosition(500, 0, 100, 100);
    public static final IconPosition RIGHT_ARROW_POSITION = new IconPosition(600, 0, 100, 100);
    public static final IconPosition RIGHT_HIGHLIGHTED_ARROW_POSITION = new IconPosition(600, 200, 100, 100);
    protected static final boolean IS_MENU_DOT_REQUIRED = true;
    protected static final int DOT_SIZE = 30;
    public static final IconPosition DISABLE_DOT_ICON_POSITION = new IconPosition(985, 111, DOT_SIZE, DOT_SIZE);
    public static final IconPosition SELECTED_DOT_ICON_POSITION = new IconPosition(952, 111, DOT_SIZE, DOT_SIZE);
    protected static final int GAP_BETWEEN_DOT = 30;
    private static final IconPosition ENABLE_DOT_ICON_POSITION = new IconPosition(921, 111, DOT_SIZE, DOT_SIZE);
    private static final IconPosition LEFT_DISABLE_ARROW_POSITION = new IconPosition(500, 100, 100, 100);
    private static final IconPosition RIGHT_DISABLE_ARROW_POSITION = new IconPosition(600, 100, 100, 100);
    private final IMenuScreenListener menuScreenListener;
    protected List<T> lessonList;
    protected AutoCognitaTextureRegion enabledLeftArrow;
    protected AutoCognitaTextureRegion enabledRightArrow;
    protected AutoCognitaTextureRegion disabledLeftArrow;
    protected AutoCognitaTextureRegion disabledRightArrow;
    protected AutoCognitaTextureRegion disabledDotTexture;
    protected AutoCognitaTextureRegion enabledDotTexture;
    protected AutoCognitaTextureRegion selectedDotTexture;
    protected Texture generalIconTexture;
    protected I lessonSelectListener;
    protected boolean isEnableLeftArrow;
    protected boolean isEnableRightArrow;
    /**
     * it is include the dot, menu text and the border enclose the menu text, it is not include the left and right arrow
     */
    protected List<ScreenObject<Object, ScreenObjectType>> navigationObjectList;
    protected T selectedLesson;
    protected int selectedLessonIndex;
    private AnimateTextureScreenObject leftArrowScreenObject;
    private AnimateTextureScreenObject rightArrowScreenObject;
    private int numberOfTimeRightArrowFlashed;

    public NavigationSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IMenuScreenListener menuScreenListener, List<T> lessonList) {
        super(abstractAutoCognitaScreen, null);
        //it is always show
        isShowing = true;
        setLessonList(lessonList);
        this.menuScreenListener = menuScreenListener;
    }

    public void setLessonList(List<T> lessonList) {
        selectedLesson = null;
        this.lessonList = lessonList;
    }

    protected float getDotYPosition() {
        return ScreenUtils.getNavigationBarStartYPosition() + (LEFT_ARROW_POSITION.height - DOT_SIZE) / 2;
    }

    public void setLessonSelectListener(I lessonSelectListener) {
        this.lessonSelectListener = lessonSelectListener;
    }

    public void setSelectedLesson(T lesson) {
        if (!lesson.equals(selectedLesson) && lesson.isPassed()) {
            lesson.setSelected(true);
            if (null != selectedLesson) {
                selectedLesson.setSelected(false);
            }

            selectedLesson = lesson;
            if (null != lessonSelectListener) {
                lessonSelectListener.onLessonChanged(selectedLesson);
            }

            initMenuAndTexture();
        }
    }


    protected void initTexture() {

        if (null == enabledLeftArrow) {
            Texture menuIconTexture = AssetManagerUtils.getTexture(AssetManagerUtils.ICONS);

            enabledLeftArrow = new AutoCognitaTextureRegion(menuIconTexture, LEFT_ARROW_POSITION);
            enabledRightArrow = new AutoCognitaTextureRegion(menuIconTexture, RIGHT_ARROW_POSITION);

            disabledLeftArrow = new AutoCognitaTextureRegion(menuIconTexture, LEFT_DISABLE_ARROW_POSITION);
            disabledRightArrow = new AutoCognitaTextureRegion(menuIconTexture, RIGHT_DISABLE_ARROW_POSITION);

            if (IS_MENU_DOT_REQUIRED) {
                Texture dotTexture = AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS);

                disabledDotTexture = new AutoCognitaTextureRegion(dotTexture, DISABLE_DOT_ICON_POSITION);
                enabledDotTexture = new AutoCognitaTextureRegion(dotTexture, ENABLE_DOT_ICON_POSITION);
                selectedDotTexture = new AutoCognitaTextureRegion(dotTexture, SELECTED_DOT_ICON_POSITION);
            }

            leftArrowScreenObject = new AnimateTextureScreenObject(enabledLeftArrow, null, disabledLeftArrow, ScreenUtils.getNavigationBarStartXPosition(), ScreenUtils.getNavigationBarStartYPosition());
            rightArrowScreenObject = new AnimateTextureScreenObject(enabledRightArrow,
                    new AutoCognitaTextureRegion(menuIconTexture, RIGHT_HIGHLIGHTED_ARROW_POSITION), disabledRightArrow,
                    ScreenUtils.getNavigationRightArrowStartXPosition(), ScreenUtils.getNavigationBarStartYPosition());
            rightArrowScreenObject.setAnimationListener(this);
        }
    }

    private void initMenuAndTexture() {

        initTexture();

        initMenu();
    }

    private void initMenu() {
        float startDotXPosition = 0;
        float originalStartDotXPosition = 0;
        float startYPosition = 0;

        if (IS_MENU_DOT_REQUIRED) {
            originalStartDotXPosition = ScreenUtils.getNavigationBarStartXPosition() + LEFT_ARROW_POSITION.width + 20;

            startDotXPosition = originalStartDotXPosition;

            if (null == navigationObjectList) {
                navigationObjectList = new ArrayList<ScreenObject<Object, ScreenObjectType>>();
            } else {
                navigationObjectList.clear();
            }

            startYPosition = getDotYPosition();
        }
        for (T lesson : lessonList) {

            TextureScreenObject dot = null;

            if (IS_MENU_DOT_REQUIRED) {
                if (startDotXPosition + enabledDotTexture.getRegionWidth() > ScreenUtils.getNavigationRightArrowStartXPosition()) {
                    startDotXPosition = originalStartDotXPosition;
                    startYPosition -= enabledDotTexture.getRegionHeight() + 20;
                }

                dot = new TextureScreenObject(startDotXPosition, startYPosition, enabledDotTexture, selectedDotTexture, disabledDotTexture);
                dot.id = lesson;
            }
            if (lesson.isSelected()) {
                selectedLessonIndex = lessonList.indexOf(lesson);
                if (null == selectedLesson) {
                    //for the first init, in order to ask listeners
                    setSelectedLesson(lesson);
                } else {
                    selectedLesson = lesson;
                }
                if (null != dot) {
                    dot.isHighlighted = true;
                }

            } else if (lesson.isPassed() && null != dot) {
                dot.isHighlighted = false;
                dot.isDisabled = false;
            } else if (null != dot) {
                dot.isDisabled = true;
            }

            if (IS_MENU_DOT_REQUIRED) {
                IconPosition dotIconPosition = new IconPosition(startDotXPosition, startYPosition, DOT_SIZE, DOT_SIZE);

                lesson.setIconPosition(dotIconPosition);

                navigationObjectList.add(dot);

                startDotXPosition += (dot.width + GAP_BETWEEN_DOT);
            }

        }
    }

    @Override
    public void render() {
        if (null == navigationObjectList) {
            initMenuAndTexture();
        }

        batch.begin();

        ScreenObjectUtils.draw(batch, navigationObjectList);

        drawArrow();

        batch.end();
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.GENERAL_ICONS, AssetManagerUtils.ICONS, AssetManagerUtils.GENERAL_ICONS_FOR_NINE_PATCH};
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != navigationObjectList) {
            navigationObjectList.clear();
            navigationObjectList = null;
        }

        enabledLeftArrow = null;
    }

    @Override
    public void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (!isTouchingMenuNavigator()) {

            ScreenObject<?, ScreenObjectType> touchedScreenObject =
                    isMenuTouched(navigationObjectList, GAP_BETWEEN_DOT, screenX, screenY);

            if (null != touchedScreenObject) {
                setSelectedLesson((T) touchedScreenObject.id);
            }
        }
    }

    private void drawArrow() {
        if (isEnableLeftArrow()) {
            leftArrowScreenObject.isDisabled = false;
            isEnableLeftArrow = true;

        } else {
            leftArrowScreenObject.isDisabled = true;
            isEnableLeftArrow = false;
        }
        ScreenObjectUtils.draw(batch, leftArrowScreenObject);

        if (isEnableRightArrow()) {
            rightArrowScreenObject.isDisabled = false;
            isEnableRightArrow = true;
        } else {
            rightArrowScreenObject.isDisabled = true;
            isEnableRightArrow = false;
        }
        ScreenObjectUtils.draw(batch, rightArrowScreenObject);
    }

    protected boolean isEnableLeftArrow() {
        return selectedLessonIndex > 0;
    }

    protected boolean isEnableRightArrow() {
        return null != selectedLesson &&
                selectedLesson.isPassed() && selectedLessonIndex + 1 < lessonList.size();
    }

    protected boolean isTouchingMenuNavigator() {

        if (isEnableLeftArrow && TouchUtils.isTouched(ScreenUtils.getNavigationBarStartXPosition(), ScreenUtils.getNavigationBarStartYPosition(), LEFT_ARROW_POSITION.width, LEFT_ARROW_POSITION.height)) {
            onSelectPreviousSection();
            return true;
        }

        if (isEnableRightArrow && TouchUtils.isTouched(ScreenUtils.getNavigationRightArrowStartXPosition(), ScreenUtils.getNavigationBarStartYPosition(), RIGHT_ARROW_POSITION.width, RIGHT_ARROW_POSITION.height)) {
            rightArrowScreenObject.isHighlighted = false;
            numberOfTimeRightArrowFlashed = 0;
            onSelectNextSection();
            return true;
        }

        return false;
    }

    protected <O, S> ScreenObject<O, S> isMenuTouched(List<ScreenObject<O, S>> screenObjectList, int gapBetween, int screenX, int screenY) {
        ScreenObject touchedObject = null;
        if (CollectionUtils.isNotEmpty(screenObjectList)) {
            //in order to make the navigation dot more sensitive, the touch area for each menu text will be expand to the half of the GAP_BETWEEN_DOT
            float expandSpace = gapBetween / 2;
            for (ScreenObject screenObject : screenObjectList) {
                if (screenObject.isVisible && screenObject.isTouchAllow && !screenObject.isDisabled) {
                    float yPosition = screenObject.yPositionInScreen;
                    if (screenObject instanceof TextScreenObject) {
                        yPosition = screenObject.yPositionInScreen - screenObject.height;
                    }
                    if (TouchUtils.isTouched(screenObject.xPositionInScreen - expandSpace, yPosition - expandSpace,
                            screenObject.width + expandSpace * 2, screenObject.height + expandSpace * 2, screenX, screenY)) {
                        touchedObject = screenObject;
                        break;
                    }
                }
            }
        }

        return touchedObject;
    }

    public void onSelectPreviousSection() {
        if (selectedLessonIndex - 1 >= 0) {
            selectedLessonIndex--;
            setSelectedLesson(lessonList.get(selectedLessonIndex));
        }
    }

    /**
     * It will be call when the user touch the right arrow to show the next lesson
     */
    protected void onSelectNextSection() {
        if (selectedLessonIndex + 1 < lessonList.size()) {
            selectedLessonIndex++;
            setSelectedLesson(lessonList.get(selectedLessonIndex));
            rightArrowScreenObject.isHighlighted = false;
        }
    }

    /**
     * It is mainly called when the lesson is complete, and going to show next lesson
     * <p>
     * The right arrow will be flash in a red color
     */
    public void onLessonComplete() {

        if (isAllLessonCompleted()) {
            restartLessonAndGoToHomeScreen();
        } else {
            numberOfTimeRightArrowFlashed = 0;
            if (null != rightArrowScreenObject) {
                rightArrowScreenObject.isHighlighted = true;
                rightArrowScreenObject.setAlpha(0);
                rightArrowScreenObject.isAnimationCompleted = false;
            }

        }
    }

    protected boolean isAllLessonCompleted() {
        return selectedLessonIndex + 1 >= lessonList.size();
    }

    protected void restartLessonAndGoToHomeScreen() {
        //if all lesson finish, jump to home
        //make sure it is calling the render thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                restart();
                //in order to make sure the user will see the first lesson selected if she/he is come again
                if (CollectionUtils.isNotEmpty(lessonList)) {
                    selectedLesson = null;
                    //make sure the selected lesson is deselected
                    lessonList.get(selectedLessonIndex).setSelected(false);
                    lessonList.get(0).setSelected(true);
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        menuScreenListener.onHomeSelected();
                    }
                });
            }
        }).start();
    }

    /**
     * in order to reset everything after all test are done, so next time the user enter the screen again, it will be start from begining
     */
    protected void restart() {
        //in order to reset everything after all test are done, so next time the user enter the screen again, it will be start from begining
        abstractAutoCognitaScreen.restart();
    }

    @Override
    public void onComplete() {
        if (rightArrowScreenObject.isHighlighted) {
            if (numberOfTimeRightArrowFlashed < 5) {
                rightArrowScreenObject.setAlpha(0);
                rightArrowScreenObject.isAnimationCompleted = false;
                numberOfTimeRightArrowFlashed++;
            } else {
                rightArrowScreenObject.isHighlighted = false;
            }
        }
    }


    public interface ILessonChangeListener<T> {
        void onLessonChanged(T selectedLesson);

    }
}
