package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.bo.MathLesson;
import com.maqautocognita.bo.ProgressMapElementResult;
import com.maqautocognita.constant.LessonType;
import com.maqautocognita.constant.LessonUnitCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.ui.VerticalPagedScrollPane;
import com.maqautocognita.screens.HomeMenuMapScreen;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.service.MathLessonService;
import com.maqautocognita.service.PhonicLessonService;
import com.maqautocognita.service.PhonicU2LessonService;
import com.maqautocognita.service.PhonicU3LessonService;
import com.maqautocognita.service.PhonicU4LessonService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TextPropertiesUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siu-chun.chi on 7/8/2017.
 */

public class ProgressMapPopupActor extends Group {

    private static final Color SCORE_LOW_THAN_25 = Color.valueOf("#EEEEEE");
    private static final Color SCORE_LOW_THAN_50 = Color.valueOf("#9E9E9E");
    private static final Color SCORE_LOW_THAN_75 = Color.valueOf("#424242");
    private static final Color SCORE_HIGHER_THAN_75 = Color.valueOf("#212121");

    private static final Color UNDERLINE_COLOR = ColorProperties.DISABLE_TEXT;
    private static final TextFontSizeEnum TITLE_FONT_SIZE = TextFontSizeEnum.FONT_48;
    private static final int SCREEN_MARGIN = 20;
    private static final int ICON_SIZE = 100;

    private static final int READING_MAXIMUM_NUMBER_OF_COLUMN = 8;
    private static final int MATH_MAXIMUM_NUMBER_OF_COLUMN = ScreenUtils.isTablet ? 4 : 2;

    private final LessonUnitCode defaultDisplayUnit;
    private final IMenuScreenListener menuScreenListener;
    private Image closeIcon;
    private Map<LessonUnitCode, List<ProgressMapElementResult>> lessonTypeProgressListMap;

    public ProgressMapPopupActor(float stageWidth, float stageHeight,
                                 LessonUnitCode defaultDisplayUnit,
                                 AbstractLessonService lessonService,
                                 final IActionListener onCloseListener, IMenuScreenListener menuScreenListener) {

        this.menuScreenListener = menuScreenListener;
        this.defaultDisplayUnit = defaultDisplayUnit;

        setSize(stageWidth, stageHeight);

        int unitIndex = 0;
        int index = 0;

        for (LessonUnitCode lessonUnitCode : LessonType.getLessonType(defaultDisplayUnit).lessonUnitCodes) {
            if (null == lessonTypeProgressListMap) {
                lessonTypeProgressListMap = new LinkedHashMap<LessonUnitCode, List<ProgressMapElementResult>>();
            }
            if (lessonUnitCode.equals(defaultDisplayUnit)) {
                unitIndex = index;
            }

            if (lessonUnitCode.equals(defaultDisplayUnit) ||
                    !lessonTypeProgressListMap.containsKey(lessonUnitCode)) {
                lessonTypeProgressListMap.put(lessonUnitCode,
                        lessonService.getProgressMapStatusForPopup(lessonUnitCode.code));
            }

            index++;

        }

        VerticalPagedScrollPane verticalPagedScrollPane = new VerticalPagedScrollPane(null);
        verticalPagedScrollPane.setSize(getWidth(), getHeight());

        for (LessonUnitCode lessonUnitCode : lessonTypeProgressListMap.keySet()) {
            AutoCognitaTextureRegion iconTextureRegion = null;
            String titleText = null;

            switch (lessonUnitCode) {
                case ALPHABET:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.ALPHABET_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getTitleAlphabet();
                    break;
                case PHONIC_UNIT_1:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.PHONICS_LEVEL1_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getPhonics1();
                    break;
                case PHONIC_UNIT_2:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.PHONICS_LEVEL2_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getPhonics2();
                    break;
                case PHONIC_UNIT_3:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.PHONICS_LEVEL3_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getPhonics3();
                    break;
                case PHONIC_UNIT_4:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.PHONICS_LEVEL4_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getPhonics4();
                    break;
                case MATH_1:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.COUNT_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getMathCount();
                    break;
                case MATH_2:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.ADDITION_SUBTRACT_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getMathAddition();
                    break;
                case MATH_3:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.PLACE_VALUE_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getMathPlaceValue();
                    break;
                case MATH_4:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.MULTIPLY_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getMathMultiplication();
                    break;
                case MATH_5:
                    iconTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.getMenuIcon()),
                            HomeMenuMapScreen.MATH_OTHER_TOPICS_ICON_POSITION, 2);
                    titleText = TextPropertiesUtils.getMathOtherTopics();
                    break;
            }

            verticalPagedScrollPane.addPage(new ProgressMapGroup(
                    iconTextureRegion, titleText, lessonTypeProgressListMap.get(lessonUnitCode),
                    getWidth(), getHeight()
            ), null);
        }

        verticalPagedScrollPane.setSelectedPage(unitIndex);

        addActor(verticalPagedScrollPane);

        closeIcon = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_HDPI_PATH + "close.png"));
        closeIcon.setY(getHeight() - closeIcon.getHeight() - SCREEN_MARGIN);
        closeIcon.setX(getWidth() - closeIcon.getWidth() - SCREEN_MARGIN);
        addActor(closeIcon);

        closeIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != onCloseListener) {
                    onCloseListener.onComplete();
                }
            }
        });

    }

    @Override
    public boolean remove() {
        AssetManagerUtils.unloadAllTexture();
        return super.remove();
    }

    public void hideCloseButton() {
        closeIcon.setVisible(false);
    }

    private void addTable(List<ProgressMapElementResult> progressMapElementResultList, Table table) {
        int i = 0;

        int maxNumberOfColumn = LessonType.READING.equals(LessonType.getLessonType(defaultDisplayUnit)) ?
                READING_MAXIMUM_NUMBER_OF_COLUMN : MATH_MAXIMUM_NUMBER_OF_COLUMN;

        for (final ProgressMapElementResult progressMapElementResult : progressMapElementResultList) {
            if (i % maxNumberOfColumn == 0) {
                table.row();
            }
            Label.LabelStyle topicStyle = new Label.LabelStyle();
            topicStyle.font = FontGeneratorManager.getFont(TITLE_FONT_SIZE);
            double score = progressMapElementResult.currentMark / progressMapElementResult.fullMark;
            if (score <= 0.25) {
                topicStyle.fontColor = SCORE_LOW_THAN_25;
            } else if (score <= 0.5) {
                topicStyle.fontColor = SCORE_LOW_THAN_50;
            } else if (score <= 0.75) {
                topicStyle.fontColor = SCORE_LOW_THAN_75;
            } else {
                topicStyle.fontColor = SCORE_HIGHER_THAN_75;
            }


            Label topic = new Label(progressMapElementResult.topic, topicStyle);
            topic.setHeight(topicStyle.font.getLineHeight());
            topic.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {

                    if (LessonUnitCode.ALPHABET.code.equals(progressMapElementResult.unitCode)) {
                        selectLesson(AlphabetLessonService.getInstance(), progressMapElementResult);
                        menuScreenListener.onAlphabetSelected();
                    } else if (LessonUnitCode.PHONIC_UNIT_1.code.equals(progressMapElementResult.unitCode)) {
                        selectLesson(PhonicLessonService.getInstance(), progressMapElementResult);
                        menuScreenListener.onPhonicU1Selected();
                    } else if (LessonUnitCode.PHONIC_UNIT_2.code.equals(progressMapElementResult.unitCode)) {
                        selectLesson(PhonicU2LessonService.getInstance(), progressMapElementResult);
                        menuScreenListener.onPhonicU2Selected();
                    } else if (LessonUnitCode.PHONIC_UNIT_3.code.equals(progressMapElementResult.unitCode)) {
                        selectLesson(PhonicU3LessonService.getInstance(), progressMapElementResult);
                        menuScreenListener.onPhonicU3Selected();
                    } else if (LessonUnitCode.PHONIC_UNIT_4.code.equals(progressMapElementResult.unitCode)) {
                        selectLesson(PhonicU4LessonService.getInstance(), progressMapElementResult);
                        menuScreenListener.onPhonicU4Selected();
                    } else if (LessonUnitCode.MATH_1.code.equals(progressMapElementResult.unitCode)) {
                        selectMathLesson(LessonUnitCode.MATH_1, progressMapElementResult);
                        menuScreenListener.onMathCountSelected();
                    } else if (LessonUnitCode.MATH_2.code.equals(progressMapElementResult.unitCode)) {
                        selectMathLesson(LessonUnitCode.MATH_2, progressMapElementResult);
                        menuScreenListener.onMathAdditionSubtractionSelected();
                    } else if (LessonUnitCode.MATH_3.code.equals(progressMapElementResult.unitCode)) {
                        selectMathLesson(LessonUnitCode.MATH_3, progressMapElementResult);
                        menuScreenListener.onMathPlaceValueSelected();
                    } else if (LessonUnitCode.MATH_4.code.equals(progressMapElementResult.unitCode)) {
                        selectMathLesson(LessonUnitCode.MATH_4, progressMapElementResult);
                        menuScreenListener.onMathMultiplySelected();
                    } else if (LessonUnitCode.MATH_5.code.equals(progressMapElementResult.unitCode)) {
                        selectMathLesson(LessonUnitCode.MATH_5, progressMapElementResult);
                        menuScreenListener.onMathOtherTopicSelected();
                    }

                }
            });
            table.add(topic).expandX().left().spaceBottom(20);
            i++;
        }
    }

    private void selectLesson(AbstractLessonService lessonService, ProgressMapElementResult progressMapElementResult) {
        for (LessonWithReview lesson : lessonService.getLessonList()) {
            boolean isSelected = false;
            if (CollectionUtils.isNotEmpty(lesson.getActivityList())) {
                for (Activity activity : lesson.getActivityList()) {
                    if (progressMapElementResult.elementCode.equals(activity.getLetter())
                            && progressMapElementResult.lessonCode.equals(activity.getLessonCode())) {
                        activity.setSelected(true);
                        isSelected = true;
                    }
                }
            }
            lesson.setSelected(isSelected);

        }
    }

    private void selectMathLesson(LessonUnitCode unitCode, ProgressMapElementResult progressMapElementResult) {
        for (MathLesson lesson : MathLessonService.getInstance().getAllMathLesson(unitCode.code)) {
            if (lesson.getElementCode().equals(progressMapElementResult.elementCode)) {
                lesson.setSelected(true);
            } else {
                lesson.setSelected(false);
            }
        }
    }

    private Label.LabelStyle getSentenceStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(TITLE_FONT_SIZE);
        labelStyle.fontColor = ColorProperties.TEXT;
        return labelStyle;
    }

    private class ProgressMapGroup extends WidgetGroup {

        /**
         * This is the line which draw under the {@link #title}
         */
        private ShapeRenderer titleUnderline;

        private Label title;
        private Table topicTable;

        public ProgressMapGroup(AutoCognitaTextureRegion iconTextureRegion, String titleText, List<ProgressMapElementResult>
                progressMapList, float width, float height) {

            setSize(width, height);

            Image icon = new Image(iconTextureRegion);
            icon.setSize(ICON_SIZE, ICON_SIZE);

            icon.setX(SCREEN_MARGIN);
            icon.setY(getHeight() - icon.getHeight() - SCREEN_MARGIN);

            title = new Label(titleText, getSentenceStyle());

            title.setX(icon.getWidth() + icon.getX() + 10);
            title.setY(icon.getY() + (icon.getHeight() - title.getHeight()) / 2);

            addActor(icon);
            addActor(title);

            topicTable = new Table();
            topicTable.setSize(getWidth() - title.getX() - 20, icon.getY());
            topicTable.setX(title.getX());

            topicTable.top().padTop(SCREEN_MARGIN);

            addTable(progressMapList, topicTable);
            addActor(topicTable);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {

            super.draw(batch, parentAlpha);
            batch.end();
            if (null == titleUnderline) {
                titleUnderline = new ShapeRenderer();
                titleUnderline.setColor(UNDERLINE_COLOR);
                titleUnderline.getProjectionMatrix().setToOrtho2D(0, 0,
                        ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            }

            titleUnderline.begin(ShapeRenderer.ShapeType.Filled);
            titleUnderline.rect(getX() + topicTable.getX(),
                    getY() + title.getY(), topicTable.getWidth(), 4f);
            titleUnderline.end();
            batch.begin();
        }

        @Override
        public boolean remove() {

            if (null != titleUnderline) {
                titleUnderline.dispose();
                titleUnderline = null;
            }
            return super.remove();
        }
    }
}
