package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.ProgressMapSkillResult;
import com.maqautocognita.bo.ProgressMapTopicResult;
import com.maqautocognita.constant.LessonProgressType;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.service.AlphabetLessonService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TextPropertiesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class MathProgressActor extends ProgressActor {


    protected static final int MOBILE_MENU_TEXT_WIDTH = 300;
    private TextCell countTextCell;
    private TextCell compareTextCell;
    private TextCell addTextCell;
    private TextCell subtractTextCell;
    private ImageActor countIcon;
    private ImageActor compareIcon;
    private ImageActor addIcon;
    private ImageActor subtractIcon;
    private TextCell multiplyTextCell;
    private TextCell placeValueTextCell;
    private TextCell numberPatternTextCell;
    private TextCell shapeTextCell;
    private ImageActor multiplyIcon;
    private ImageActor placeValueIcon;
    private ImageActor numberPatternIcon;
    private ImageActor shapeIcon;
    private TextCell clickTextCell;
    private TextCell typeTextCell;
    private TextCell writeTextCell;
    private TextCell speakTextCell;
    private TextCell makePictureTextCell;
    private TextCell matchTextCell;
    private TextCell colorBlockTextCell;
    private TextCell longFormTextCell;
    private ImageActor clickIcon;
    private ImageActor typeIcon;
    private ImageActor writeIcon;
    private ImageActor speakIcon;
    private ImageActor makePictureIcon;
    private ImageActor matchIcon;
    private ImageActor colorBlocksIcon;
    private ImageActor longFormIcon;

    public MathProgressActor(
            int topMenuMarginTop, int arrowIconSize) {
        super(topMenuMarginTop, arrowIconSize);
    }

    @Override
    protected TextCell drawTopicProgress(TextCell topicsTextCell) {
        if (null == countTextCell) {
            countTextCell = createMenuText(DUMMY_TEXT, topicsTextCell);
            addActor(countTextCell);
            countIcon = createIcon(countTextCell, 100, 0);
            addActor(countIcon);
        }

        if (null == compareTextCell) {
            compareTextCell = createMenuText(DUMMY_TEXT, countTextCell);
            addActor(compareTextCell);
            compareIcon = createIcon(compareTextCell, 100, 60);
            addActor(compareIcon);
        }

        if (null == addTextCell) {
            addTextCell = createMenuText(DUMMY_TEXT, compareTextCell);
            addActor(addTextCell);
            addIcon = createIcon(addTextCell, 100, 120);
            addActor(addIcon);
        }

        if (null == subtractTextCell) {
            subtractTextCell = createMenuText(DUMMY_TEXT, addTextCell);
            addActor(subtractTextCell);
            subtractIcon = createIcon(subtractTextCell, 100, 180);
            addActor(subtractIcon);
        }

        if (null == multiplyTextCell) {
            multiplyTextCell = ScreenUtils.isLandscapeMode ? createMenuTextInRightSide(DUMMY_TEXT, topicsTextCell) : createMenuText(DUMMY_TEXT, subtractTextCell);
            addActor(multiplyTextCell);
            multiplyIcon = createIcon(multiplyTextCell, 200, 0);
            addActor(multiplyIcon);
        }

        if (null == placeValueTextCell) {
            placeValueTextCell = ScreenUtils.isLandscapeMode ? createMenuTextInRightSide(DUMMY_TEXT, multiplyTextCell) : createMenuText(DUMMY_TEXT, multiplyTextCell);
            addActor(placeValueTextCell);
            placeValueIcon = createIcon(placeValueTextCell, 200, 60);
            addActor(placeValueIcon);
        }

        if (null == numberPatternTextCell) {
            numberPatternTextCell = ScreenUtils.isLandscapeMode ? createMenuTextInRightSide(DUMMY_TEXT, placeValueTextCell) : createMenuText(DUMMY_TEXT, placeValueTextCell);
            addActor(numberPatternTextCell);
            numberPatternIcon = createIcon(numberPatternTextCell, 200, 120);
            addActor(numberPatternIcon);
        }

        if (null == shapeTextCell) {
            shapeTextCell = ScreenUtils.isLandscapeMode ? createMenuTextInRightSide(DUMMY_TEXT, numberPatternTextCell) :
                    createMenuText(DUMMY_TEXT, numberPatternTextCell);
            addActor(shapeTextCell);
            shapeIcon = createIcon(shapeTextCell, 200, 180);
            addActor(shapeIcon);
        }

        return shapeTextCell;
    }

    @Override
    protected void drawActivityProgress(TextCell activitiesTextCell) {
        if (null == clickTextCell) {
            clickTextCell = createMenuText(DUMMY_TEXT, activitiesTextCell);
            addActor(clickTextCell);
            clickIcon = createActivityIcon(clickTextCell, 200, 500);
            addActor(clickIcon);
        }

        if (null == typeTextCell) {
            typeTextCell = createMenuText(DUMMY_TEXT, clickTextCell);
            addActor(typeTextCell);
            typeIcon = createActivityIcon(typeTextCell, 200, 560);
            addActor(typeIcon);
        }

        if (null == writeTextCell) {
            writeTextCell = createMenuText(DUMMY_TEXT, typeTextCell);
            addActor(writeTextCell);
            writeIcon = createActivityIcon(writeTextCell, 200, 620);
            addActor(writeIcon);
        }

        if (null == speakTextCell) {
            speakTextCell = createMenuText(DUMMY_TEXT, writeTextCell);
            addActor(speakTextCell);
            speakIcon = createActivityIcon(speakTextCell, 200, 680);
            addActor(speakIcon);
        }

        if (null == makePictureTextCell) {
            makePictureTextCell = createMenuText(DUMMY_TEXT, speakTextCell);
            addActor(makePictureTextCell);
            makePictureIcon = createActivityIcon(makePictureTextCell, 200, 740);
            addActor(makePictureIcon);
        }

        if (null == matchTextCell) {
            matchTextCell = createMenuText(DUMMY_TEXT, makePictureTextCell);
            addActor(matchTextCell);
            colorBlocksIcon = createActivityIcon(matchTextCell, 200, 800);
            addActor(colorBlocksIcon);
        }

        if (null == colorBlockTextCell) {
            colorBlockTextCell = createMenuText(DUMMY_TEXT, matchTextCell);
            addActor(colorBlockTextCell);
            matchIcon = createActivityIcon(colorBlockTextCell, 200, 860);
            addActor(matchIcon);
        }

        if (null == longFormTextCell) {
            longFormTextCell = createMenuText(DUMMY_TEXT, colorBlockTextCell);
            addActor(longFormTextCell);
            longFormIcon = createActivityIcon(longFormTextCell, 200, 920);
            addActor(longFormIcon);
        }
    }

    @Override
    protected float getMenuTextWidth() {
        return ScreenUtils.isLandscapeMode ? MENU_TEXT_WIDTH : MOBILE_MENU_TEXT_WIDTH;
    }

    @Override
    protected void drawProgress() {

        if (null == progressIconList) {
            progressIconList = new ArrayList<ImageActor>();
        } else {
            for (ImageActor imageActor : progressIconList) {
                imageActor.remove();
            }
        }

        List<ProgressMapTopicResult> progressMapTopicResultList =
                AlphabetLessonService.getInstance().getMathProgressTopic();

        String progressType = null;
        float startXPosition = 0;
        float startYPosition = 0;
        for (ProgressMapTopicResult progressMapTopicResult : progressMapTopicResultList) {
            if (!progressMapTopicResult.progressType.equals(progressType)) {
                progressType = progressMapTopicResult.progressType;
                if (LessonProgressType.COUNT.type.equals(progressType)) {
                    startYPosition = countIcon.getY();
                    startXPosition = getProgressBarStartXPosition(countIcon);
                } else if (LessonProgressType.COMPARE.type.equals(progressType)) {
                    startYPosition = compareIcon.getY();
                    startXPosition = getProgressBarStartXPosition(compareIcon);
                } else if (LessonProgressType.ADD.type.equals(progressType)) {
                    startYPosition = addIcon.getY();
                    startXPosition = getProgressBarStartXPosition(addIcon);
                } else if (LessonProgressType.SUBTRACT.type.equals(progressType)) {
                    startYPosition = subtractIcon.getY();
                    startXPosition = getProgressBarStartXPosition(subtractIcon);
                } else if (LessonProgressType.MULTIPLY.type.equals(progressType)) {
                    startYPosition = multiplyIcon.getY();
                    startXPosition = getProgressBarStartXPosition(multiplyIcon);
                } else if (LessonProgressType.PLACE_VALUE.type.equals(progressType)) {
                    startYPosition = placeValueIcon.getY();
                    startXPosition = getProgressBarStartXPosition(placeValueIcon);
                } else if (LessonProgressType.NUMBER_PATTERN.type.equals(progressType)) {
                    startYPosition = numberPatternIcon.getY();
                    startXPosition = getProgressBarStartXPosition(numberPatternIcon);
                } else if (LessonProgressType.SHAPE.type.equals(progressType)) {
                    startYPosition = shapeIcon.getY();
                    startXPosition = getProgressBarStartXPosition(shapeIcon);
                }
            }

            final float originalStartXPosition = startXPosition;

            IconPosition iconPosition = null;
            switch (progressMapTopicResult.completeCount) {
                case 1:
                    iconPosition = READING_PROGRESS_ICON_1;
                    break;
                case 2:
                    iconPosition = READING_PROGRESS_ICON_2;
                    break;
                case 3:
                    iconPosition = READING_PROGRESS_ICON_3;
                    break;
                default:
                    iconPosition = DISABLE_PROGRESS_ICON;
            }

            ImageActor imageActor = new ImageActor(AssetManagerUtils.PROGRESS_MAP,
                    iconPosition,
                    startXPosition, startYPosition);
            imageActor.setIsRequiredToCheckCameraBeforeDraw(false);
            progressIconList.add(imageActor);
            addActor(imageActor);
            startXPosition += PROGRESS_ICON_WIDTH;
            if (startXPosition + PROGRESS_ICON_WIDTH >=
                    getWidth()) {

                startXPosition = originalStartXPosition;
                startYPosition -= PROGRESS_ICON_HEIGHT;
            }
        }


        List<ProgressMapSkillResult> progressMapSkillResultList =
                AlphabetLessonService.getInstance().getMathProgressSkill();
        for (ProgressMapSkillResult progressMapSkillResult : progressMapSkillResultList) {
            startXPosition = getProgressBarStartXPosition();
            if (!progressMapSkillResult.progressType.equals(progressType)) {
                progressType = progressMapSkillResult.progressType;
                if (LessonProgressType.CLICK.type.equals(progressType)) {
                    startYPosition = clickIcon.getY();
                } else if (LessonProgressType.MATH_TYPE.type.equals(progressType)) {
                    startYPosition = typeIcon.getY();
                } else if (LessonProgressType.WRITE.type.equals(progressType)) {
                    startYPosition = writeIcon.getY();
                } else if (LessonProgressType.SPEAK.type.equals(progressType)) {
                    startYPosition = speakIcon.getY();
                } else if (LessonProgressType.MAKE_PICTURE.type.equals(progressType)) {
                    startYPosition = makePictureIcon.getY();
                } else if (LessonProgressType.COLOR_BLOCKS.type.equals(progressType)) {
                    startYPosition = colorBlocksIcon.getY();
                } else if (LessonProgressType.MATCH.type.equals(progressType)) {
                    startYPosition = matchIcon.getY();
                } else if (LessonProgressType.LONG_FORM.type.equals(progressType)) {
                    startYPosition = longFormIcon.getY();
                }
            }

            for (int i = 1; i <= progressMapSkillResult.fullMark; i++) {
                ImageActor imageActor = new ImageActor(AssetManagerUtils.PROGRESS_MAP,
                        i < progressMapSkillResult.currentMark ? ACTIVITY_PROGRESS_ICON : ACTIVITY_DISABLE_PROGRESS_ICON,
                        startXPosition, startYPosition);
                imageActor.setIsRequiredToCheckCameraBeforeDraw(false);
                addActor(imageActor);
                progressIconList.add(imageActor);
                startXPosition += ACTIVITY_PROGRESS_ICON.width;
            }
        }
    }

    private float getProgressBarStartXPosition(ImageActor icon) {
        return icon.getX() + icon.getWidth() + SPACE_BETWEEN_ICON_AND_PROGRESS;
    }

    @Override
    protected String getTitleText() {
        return TextPropertiesUtils.getTitleMath();
    }

    @Override
    protected void changeLanguage() {
        countTextCell.setText(TextPropertiesUtils.getTitleCount());
        compareTextCell.setText(TextPropertiesUtils.getTitleCompare());
        addTextCell.setText(TextPropertiesUtils.getTitleAdd());
        subtractTextCell.setText(TextPropertiesUtils.getTitleSubtract());

        multiplyTextCell.setText(TextPropertiesUtils.getTitleMultiply());
        placeValueTextCell.setText(TextPropertiesUtils.getTitlePlaceValue());
        numberPatternTextCell.setText(TextPropertiesUtils.getTitleNumberPattern());
        shapeTextCell.setText(TextPropertiesUtils.getTitleShape());

        clickTextCell.setText(TextPropertiesUtils.getTitleClick());
        typeTextCell.setText(TextPropertiesUtils.getTitleType());
        writeTextCell.setText(TextPropertiesUtils.getTitleWrite());
        speakTextCell.setText(TextPropertiesUtils.getTitleSpeak());
        makePictureTextCell.setText(TextPropertiesUtils.getTitleMakePicture());
        matchTextCell.setText(TextPropertiesUtils.getTitleMatch());
        colorBlockTextCell.setText(TextPropertiesUtils.getTitleColorBlocks());
        longFormTextCell.setText(TextPropertiesUtils.getTitleLongForm());
    }

    @Override
    public boolean remove() {
        countTextCell = null;
        compareTextCell = null;
        addTextCell = null;
        subtractTextCell = null;
        clickTextCell = null;
        typeTextCell = null;
        writeTextCell = null;
        speakTextCell = null;
        makePictureTextCell = null;
        matchTextCell = null;
        colorBlockTextCell = null;
        longFormTextCell = null;

        multiplyTextCell = null;
        placeValueTextCell = null;
        numberPatternTextCell = null;
        shapeTextCell = null;

        if (null != multiplyIcon) {
            multiplyIcon.dispose();
            multiplyIcon = null;
        }
        if (null != placeValueIcon) {
            placeValueIcon.dispose();
            placeValueIcon = null;
        }
        if (null != numberPatternIcon) {
            numberPatternIcon.dispose();
            numberPatternIcon = null;
        }
        if (null != shapeIcon) {
            shapeIcon.dispose();
            shapeIcon = null;
        }

        if (null != countIcon) {
            countIcon.dispose();
            countIcon = null;
        }

        if (null != compareIcon) {
            compareIcon.dispose();
            compareIcon = null;
        }
        if (null != addIcon) {
            addIcon.dispose();
            addIcon = null;
        }
        if (null != subtractIcon) {
            subtractIcon.dispose();
            subtractIcon = null;
        }
        if (null != clickIcon) {
            clickIcon.dispose();
            clickIcon = null;
        }
        if (null != typeIcon) {
            typeIcon.dispose();
            typeIcon = null;
        }
        if (null != writeIcon) {
            writeIcon.dispose();
            writeIcon = null;
        }
        if (null != speakIcon) {
            speakIcon.dispose();
            speakIcon = null;
        }
        if (null != makePictureIcon) {
            makePictureIcon.dispose();
            makePictureIcon = null;
        }
        if (null != colorBlocksIcon) {
            colorBlocksIcon.dispose();
            colorBlocksIcon = null;
        }
        if (null != matchIcon) {
            matchIcon.dispose();
            matchIcon = null;
        }
        if (null != longFormIcon) {
            longFormIcon.dispose();
            longFormIcon = null;
        }
        return super.remove();
    }

    private ImageActor createActivityIcon(TextCell forTextCell, int iconXPositionInImage, int iconYPositionInImage) {
        return createIcon(forTextCell, iconXPositionInImage, iconYPositionInImage, ACTIVITY_ICON_WIDTH, ICON_HEIGHT);
    }

    private ImageActor createIcon(TextCell forTextCell, int iconXPositionInImage, int iconYPositionInImage) {
        return createIcon(forTextCell, iconXPositionInImage, iconYPositionInImage, ICON_WIDTH, ICON_HEIGHT);
    }

    private ImageActor createIcon(TextCell forTextCell, int iconXPositionInImage, int iconYPositionInImage, int iconWidth, int iconHeight) {
        ImageActor imageActor = new ImageActor(AssetManagerUtils.PROGRESS_MAP, new IconPosition(iconXPositionInImage, iconYPositionInImage, iconWidth, iconHeight),
                forTextCell.getX() +
                        (ScreenUtils.isLandscapeMode ? MENU_TEXT_WIDTH : MOBILE_MENU_TEXT_WIDTH)
                        + (ACTIVITY_ICON_WIDTH - iconWidth) / 2, forTextCell.getY() - ((forTextCell.getHeight() + ICON_HEIGHT) / 2));
        imageActor.setIsRequiredToCheckCameraBeforeDraw(false);
        return imageActor;
    }

}
