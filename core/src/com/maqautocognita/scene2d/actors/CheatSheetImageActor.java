package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.bo.CheatSheetBox;
import com.maqautocognita.bo.CheatSheetLesson;
import com.maqautocognita.bo.storyMode.CheatSheetImage;
import com.maqautocognita.listener.DirectionGestureDetector;
import com.maqautocognita.listener.IDirectionGestureListener;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class CheatSheetImageActor extends WidgetGroup implements IDirectionGestureListener {

    private static final int IMAGE_WIDTH = 1080;
    private static final int IMAGE_HEIGHT = 1298;

    private static final int SPACE_BETWEEN_DOT = 20;

    private final List<CheatSheetLesson> cheatSheetLessonList;
    private final DirectionGestureDetector directionGestureDetector = new DirectionGestureDetector(this);
    private CheatSheetLesson selectedCheatSheetLesson;
    private int dotSize;
    private Image downArrowImage;
    private List<HighlightImageActor<CheatSheetLesson>> lessonDotList;
    private List<CheatSheetBoxActor> cheatSheetBoxActorList;
    private HighlightImageActor<CheatSheetLesson> selectedLessonDot;
    private ImageActor currentImage;
    private CheatSheetForLifeMessageActor cheatSheetForLifeMessageActor;

    public CheatSheetImageActor(float width, float height, float yPosition, List<CheatSheetLesson> cheatSheetLessonList, CheatSheetForLifeMessageActor cheatSheetForLifeMessageActor) {
        this.cheatSheetForLifeMessageActor = cheatSheetForLifeMessageActor;
        setSize(width, height);
        this.cheatSheetLessonList = cheatSheetLessonList;
        dotSize = (int) width / 30;
        init();
    }

    private void init() {

        if (null == lessonDotList) {
            initShowImage();
            if (CollectionUtils.isNotEmpty(cheatSheetLessonList)) {
                int lessonIndex = 0;
                lessonDotList = new ArrayList<HighlightImageActor<CheatSheetLesson>>(cheatSheetLessonList.size());
                for (CheatSheetLesson cheatSheetLesson : cheatSheetLessonList) {
                    int imageIndex = 0;
                    for (CheatSheetImage cheatSheetImage : cheatSheetLesson.imageList) {
                        cheatSheetImage.index = imageIndex;
                        imageIndex++;
                    }

                    HighlightImageActor<CheatSheetLesson> dot = new HighlightImageActor(cheatSheetLesson,
                            AssetManagerUtils.GENERAL_ICONS, NavigationSection.SELECTED_DOT_ICON_POSITION,
                            AssetManagerUtils.GENERAL_ICONS, NavigationSection.DISABLE_DOT_ICON_POSITION);
                    if (0 == lessonIndex) {
                        selectedLessonDot = dot;
                    }
                    lessonDotList.add(dot);

                    cheatSheetLesson.index = lessonIndex;

                    if (0 == lessonIndex) {
                        selectedCheatSheetLesson = cheatSheetLesson;
                    }

                    lessonIndex++;
                }

                float startDotXPosition = ScreenUtils.getXPositionForCenterObject(dotSize * lessonDotList.size() + SPACE_BETWEEN_DOT * (lessonDotList.size() - 1),
                        getWidth());

                for (int i = 0; i < lessonDotList.size(); i++) {
                    final HighlightImageActor<CheatSheetLesson> dot = lessonDotList.get(i);
                    dot.setSize(dotSize, dotSize);
                    dot.setPosition(startDotXPosition, SPACE_BETWEEN_DOT);
                    startDotXPosition += dotSize + SPACE_BETWEEN_DOT;
                    dot.addListener(new ActorGestureListener() {
                        public void tap(InputEvent event, float x, float y, int count, int button) {
                            setSelectedLessonDot(dot);
                        }
                    });
                    addActor(dot);
                }
            }
            if (null == downArrowImage) {
                downArrowImage = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "down_arrow.png"));
                downArrowImage.setPosition(
                        ScreenUtils.getXPositionForCenterObject(downArrowImage.getWidth(), getWidth()),
                        dotSize);
                downArrowImage.setTouchable(Touchable.childrenOnly);
                addActor(downArrowImage);
            }

        }


    }

    private void initShowImage() {
        if (null == currentImage) {
            currentImage = new ImageActor("");
            currentImage.setSize(getWidth(), IMAGE_HEIGHT * (getHeight() / IMAGE_HEIGHT));
            currentImage.setTouchable(Touchable.childrenOnly);
            currentImage.toBack();
            addActor(currentImage);
        }
    }

    private void setSelectedLessonDot(HighlightImageActor<CheatSheetLesson> dot) {

        if (null != selectedLessonDot) {
            selectedLessonDot.setHighlighted(false);
        }

        selectedLessonDot = dot;
        selectedLessonDot.setHighlighted(true);

        selectedCheatSheetLesson = selectedLessonDot.getId();

        if (null == selectedCheatSheetLesson.selectedImage) {
            selectedCheatSheetLesson.selectedImage = selectedCheatSheetLesson.imageList.get(0);
        }

        showImage(selectedCheatSheetLesson.selectedImage);

    }

    private void showImage(CheatSheetImage cheatSheetImage) {
        initShowImage();
        Gdx.app.log(getClass().getName(), "showing image = " + cheatSheetImage.index);
        currentImage.setImagePath(Config.CHEAT_SHEET_IMAGE_PATH + cheatSheetImage.imageName);

        if (CollectionUtils.isNotEmpty(cheatSheetBoxActorList)) {
            for (CheatSheetBoxActor cheatSheetBoxActor : cheatSheetBoxActorList) {
                cheatSheetBoxActor.remove();
            }
            cheatSheetBoxActorList.clear();
            cheatSheetBoxActorList = null;
        }

        selectedCheatSheetLesson.selectedImage = cheatSheetImage;

        showBoxWithDefaultHighlight(cheatSheetImage.cheatSheetBoxList);

        if (0 == cheatSheetImage.index) {
            showDownArrow();
        } else {
            hideDownArrowWithoutAnimation();
        }
    }

    private void showBoxWithDefaultHighlight(List<CheatSheetBox> cheatSheetBoxList) {
        List<CheatSheetBoxActor> cheatSheetBoxActorList = showBox(cheatSheetBoxList);
        if (CollectionUtils.isNotEmpty(cheatSheetBoxActorList)) {
            //default highlight the first one
            cheatSheetBoxActorList.get(0).setHighlighted(true);
        }
    }

    private void showDownArrow() {
        showDownArrowWithoutAnimation();
        AnimationUtils.doFlash(downArrowImage);
    }

    private void hideDownArrowWithoutAnimation() {
        downArrowImage.setVisible(false);
    }

    //
    private List<CheatSheetBoxActor> showBox(List<CheatSheetBox> cheatSheetBoxList) {
        if (CollectionUtils.isNotEmpty(cheatSheetBoxList)) {
            cheatSheetBoxActorList = new ArrayList<CheatSheetBoxActor>(cheatSheetBoxList.size());
            for (CheatSheetBox cheatSheetBox : cheatSheetBoxList) {
                CheatSheetBoxActor cheatSheetBoxActor = createCheatSheetBoxActor(cheatSheetBox, cheatSheetBoxActorList);
                cheatSheetBoxActorList.add(cheatSheetBoxActor);
                addActor(cheatSheetBoxActor);
            }
        }

        return cheatSheetBoxActorList;
    }

    private void showDownArrowWithoutAnimation() {
        if (null != downArrowImage) {
            downArrowImage.setVisible(true);
        }
    }

    private CheatSheetBoxActor createCheatSheetBoxActor(CheatSheetBox cheatSheetBox, List<CheatSheetBoxActor> cheatSheetBoxActorList) {
        final float heightRatio = getHeight() / IMAGE_HEIGHT;
        final float widthRatio = getWidth() / IMAGE_WIDTH;
        return createCheatSheetBoxActor(cheatSheetBox, cheatSheetBoxActorList, widthRatio, heightRatio);
    }

    private CheatSheetBoxActor createCheatSheetBoxActor(CheatSheetBox cheatSheetBox, List<CheatSheetBoxActor> cheatSheetBoxActorList,
                                                        float widthRatio, float heightRatio) {
        CheatSheetBoxActor cheatSheetBoxActor =
                new CheatSheetBoxActor(cheatSheetBox, cheatSheetForLifeMessageActor, cheatSheetBoxActorList);
        final float cheatSheetBoxHeight = cheatSheetBox.height * heightRatio;
        cheatSheetBoxActor.setPosition(cheatSheetBox.xPositionFromLeft * widthRatio,
                getHeight() - cheatSheetBox.yPositionFromTop * heightRatio -
                        cheatSheetBoxHeight);
        cheatSheetBoxActor.setSize(cheatSheetBox.width * widthRatio, cheatSheetBoxHeight);
        cheatSheetBoxActor.toFront();
        return cheatSheetBoxActor;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            addGestureListener();
            setSelectedLessonDot(selectedLessonDot);
        } else {
            removeGestureListener();
        }
    }

    private void addGestureListener() {
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(0, directionGestureDetector);
        }
    }

    private void removeGestureListener() {
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(0);
        }
    }

    @Override
    public void onLeft() {
        if (isTouchable() && selectedCheatSheetLesson.index + 1 < lessonDotList.size()) {
            setSelectedLessonDot(lessonDotList.get(selectedCheatSheetLesson.index + 1));
        }
    }

    @Override
    public void onRight() {
        if (isTouchable() && selectedCheatSheetLesson.index - 1 >= 0) {
            setSelectedLessonDot(lessonDotList.get(selectedCheatSheetLesson.index - 1));
        }
    }

    @Override
    public void onUp() {
        if (isTouchable() && selectedCheatSheetLesson.selectedImage.index + 1 < selectedCheatSheetLesson.imageList.size()) {
            showImage(selectedCheatSheetLesson.imageList.get(selectedCheatSheetLesson.selectedImage.index + 1));
        }
    }

    @Override
    public void onDown() {
        if (isTouchable() && selectedCheatSheetLesson.selectedImage.index - 1 >= 0) {
            showImage(selectedCheatSheetLesson.imageList.get(selectedCheatSheetLesson.selectedImage.index - 1));
        }
    }


}
