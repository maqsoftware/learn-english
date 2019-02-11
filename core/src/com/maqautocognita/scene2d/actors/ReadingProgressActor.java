package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.ProgressMapSkillResult;
import com.maqautocognita.bo.ProgressMapTopicResult;
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

public class ReadingProgressActor extends ProgressActor {


    private TextCell alphabetTextCell;
    private TextCell basicPhonicTextCell;
    private TextCell digraphTextCell;
    private TextCell longVowelTextCell;
    private ImageActor alphabetIcon;
    private ImageActor basicPhonicIcon;
    private ImageActor digraphIcon;
    private ImageActor longVowelIcon;
    private TextCell readListenTextCell;
    private TextCell speakTextCell;
    private TextCell wordBlendTextCell;
    private TextCell listenTypeTextCell;
    private TextCell groupTextCell;
    private TextCell fillInBlanksTextCell;
    private TextCell matchTextCell;
    private TextCell listenWriteTextCell;
    private ImageActor readListenIcon;
    private ImageActor speakIcon;
    private ImageActor wordBlendIcon;
    private ImageActor listenTypeIcon;
    private ImageActor groupIcon;
    private ImageActor fillInBlanksIcon;
    private ImageActor matchIcon;
    private ImageActor listenWriteIcon;

    public ReadingProgressActor(
            int topMenuMarginTop, int arrowIconSize) {
        super(topMenuMarginTop, arrowIconSize);
    }

    @Override
    protected TextCell drawTopicProgress(TextCell topicsTextCell) {
        if (null == alphabetTextCell) {
            alphabetTextCell = createMenuText(DUMMY_TEXT, topicsTextCell);
            addActor(alphabetTextCell);
            alphabetIcon = createIcon(alphabetTextCell, 0, 0);
            addActor(alphabetIcon);
        }

        if (null == basicPhonicTextCell) {
            basicPhonicTextCell =
                    ScreenUtils.isLandscapeMode ?
                            createMenuText(DUMMY_TEXT, alphabetTextCell) :
                            createMenuTextForMultipleRow(DUMMY_TEXT, alphabetTextCell);
            addActor(basicPhonicTextCell);
            basicPhonicIcon = createIcon(basicPhonicTextCell, 0, 60);
            addActor(basicPhonicIcon);
        }

        if (null == digraphTextCell) {
            digraphTextCell = ScreenUtils.isLandscapeMode ? createMenuText(DUMMY_TEXT, basicPhonicTextCell) : createMenuTextForMultipleRow(DUMMY_TEXT, basicPhonicTextCell);
            addActor(digraphTextCell);
            digraphIcon = createIcon(digraphTextCell, 0, 120);
            addActor(digraphIcon);
        }

        if (null == longVowelTextCell) {
            longVowelTextCell = ScreenUtils.isLandscapeMode ? createMenuText(DUMMY_TEXT, digraphTextCell) : createMenuTextForMultipleRow(DUMMY_TEXT, digraphTextCell);
            addActor(longVowelTextCell);
            longVowelIcon = createIcon(longVowelTextCell, 0, 180);
            addActor(longVowelIcon);
        }

        return longVowelTextCell;
    }

    @Override
    protected void drawActivityProgress(TextCell activitiesTextCell) {
        if (null == readListenTextCell) {
            readListenTextCell = createMenuText(DUMMY_TEXT, activitiesTextCell);
            addActor(readListenTextCell);
            readListenIcon = createActivityIcon(readListenTextCell, 0, 500);
            addActor(readListenIcon);
        }

        if (null == speakTextCell) {
            speakTextCell = createMenuText(DUMMY_TEXT, readListenTextCell);
            addActor(speakTextCell);
            speakIcon = createActivityIcon(speakTextCell, 0, 560);
            addActor(speakIcon);
        }

        if (null == wordBlendTextCell) {
            wordBlendTextCell = createMenuText(DUMMY_TEXT, speakTextCell);
            addActor(wordBlendTextCell);
            wordBlendIcon = createActivityIcon(wordBlendTextCell, 0, 620);
            addActor(wordBlendIcon);
        }

        if (null == listenTypeTextCell) {
            listenTypeTextCell = createMenuText(DUMMY_TEXT, wordBlendTextCell);
            addActor(listenTypeTextCell);
            listenTypeIcon = createActivityIcon(listenTypeTextCell, 0, 680);
            addActor(listenTypeIcon);
        }

        if (null == groupTextCell) {
            groupTextCell = createMenuText(DUMMY_TEXT, listenTypeTextCell);
            addActor(groupTextCell);
            groupIcon = createActivityIcon(groupTextCell, 0, 740);
            addActor(groupIcon);
        }

        if (null == fillInBlanksTextCell) {
            fillInBlanksTextCell = createMenuText(DUMMY_TEXT, groupTextCell);
            addActor(fillInBlanksTextCell);
            fillInBlanksIcon = createActivityIcon(fillInBlanksTextCell, 0, 800);
            addActor(fillInBlanksIcon);
        }

        if (null == matchTextCell) {
            matchTextCell = createMenuText(DUMMY_TEXT, fillInBlanksTextCell);
            addActor(matchTextCell);
            matchIcon = createActivityIcon(matchTextCell, 0, 860);
            addActor(matchIcon);
        }

        if (null == listenWriteTextCell) {
            listenWriteTextCell = createMenuText(DUMMY_TEXT, matchTextCell);
            addActor(listenWriteTextCell);
            listenWriteIcon = createActivityIcon(listenWriteTextCell, 0, 920);
            addActor(listenWriteIcon);
        }
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
                AlphabetLessonService.getInstance().getReadingProgressTopic();

        String progressType = null;
        final float originalStartXPosition = getProgressBarStartXPosition();
        float startXPosition = 0;
        float startYPosition = 0;
        for (ProgressMapTopicResult progressMapTopicResult : progressMapTopicResultList) {
            if (!progressMapTopicResult.progressType.equals(progressType)) {
                progressType = progressMapTopicResult.progressType;
                startXPosition = originalStartXPosition;
                if ("AL".equals(progressType)) {
                    startYPosition = alphabetIcon.getY();
                } else if ("P1".equals(progressType)) {
                    startYPosition = basicPhonicIcon.getY();
                } else if ("P2".equals(progressType)) {
                    startYPosition = digraphIcon.getY();
                } else if ("P3".equals(progressType)) {
                    startYPosition = longVowelIcon.getY();
                }
            }

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
                AlphabetLessonService.getInstance().getReadingProgressSkill();
        for (ProgressMapSkillResult progressMapSkillResult : progressMapSkillResultList) {
            startXPosition = originalStartXPosition;
            if (!progressMapSkillResult.progressType.equals(progressType)) {
                progressType = progressMapSkillResult.progressType;
                if ("RL".equals(progressType)) {
                    startYPosition = readListenIcon.getY();
                } else if ("SP".equals(progressType)) {
                    startYPosition = speakIcon.getY();
                } else if ("WB".equals(progressType)) {
                    startYPosition = wordBlendIcon.getY();
                } else if ("LT".equals(progressType)) {
                    startYPosition = listenTypeIcon.getY();
                } else if ("GR".equals(progressType)) {
                    startYPosition = groupIcon.getY();
                } else if ("FB".equals(progressType)) {
                    startYPosition = fillInBlanksIcon.getY();
                } else if ("MA".equals(progressType)) {
                    startYPosition = matchIcon.getY();
                } else if ("LW".equals(progressType)) {
                    startYPosition = listenWriteIcon.getY();
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

    @Override
    protected String getTitleText() {
        return TextPropertiesUtils.getTitleReading();
    }

    @Override
    protected void changeLanguage() {
        alphabetTextCell.setText(TextPropertiesUtils.getTitleAlphabet());
        basicPhonicTextCell.setText(TextPropertiesUtils.getTitleBasicPhonics());
        digraphTextCell.setText(TextPropertiesUtils.getTitleDigraph());
        longVowelTextCell.setText(TextPropertiesUtils.getTitleLongVowel());
        readListenTextCell.setText(TextPropertiesUtils.getTitleReadAndListen());
        speakTextCell.setText(TextPropertiesUtils.getTitleSpeak());
        wordBlendTextCell.setText(TextPropertiesUtils.getTitleWordBlend());
        listenTypeTextCell.setText(TextPropertiesUtils.getTitleListenType());
        groupTextCell.setText(TextPropertiesUtils.getTitleGroup());
        fillInBlanksTextCell.setText(TextPropertiesUtils.getTitleFillInBlanks());
        matchTextCell.setText(TextPropertiesUtils.getTitleMatch());
        listenWriteTextCell.setText(TextPropertiesUtils.getTitleListenWrite());
    }

    @Override
    public boolean remove() {
        alphabetTextCell = null;
        basicPhonicTextCell = null;
        digraphTextCell = null;
        longVowelTextCell = null;
        readListenTextCell = null;
        speakTextCell = null;
        wordBlendTextCell = null;
        listenTypeTextCell = null;
        groupTextCell = null;
        fillInBlanksTextCell = null;
        matchTextCell = null;
        listenWriteTextCell = null;

        if (null != alphabetIcon) {
            alphabetIcon.dispose();
            alphabetIcon = null;
        }

        if (null != basicPhonicIcon) {
            basicPhonicIcon.dispose();
            basicPhonicIcon = null;
        }
        if (null != digraphIcon) {
            digraphIcon.dispose();
            digraphIcon = null;
        }
        if (null != longVowelIcon) {
            longVowelIcon.dispose();
            longVowelIcon = null;
        }
        if (null != readListenIcon) {
            readListenIcon.dispose();
            readListenIcon = null;
        }
        if (null != speakIcon) {
            speakIcon.dispose();
            speakIcon = null;
        }
        if (null != wordBlendIcon) {
            wordBlendIcon.dispose();
            wordBlendIcon = null;
        }
        if (null != listenTypeIcon) {
            listenTypeIcon.dispose();
            listenTypeIcon = null;
        }
        if (null != groupIcon) {
            groupIcon.dispose();
            groupIcon = null;
        }
        if (null != fillInBlanksIcon) {
            fillInBlanksIcon.dispose();
            fillInBlanksIcon = null;
        }
        if (null != matchIcon) {
            matchIcon.dispose();
            matchIcon = null;
        }
        if (null != listenWriteIcon) {
            listenWriteIcon.dispose();
            listenWriteIcon = null;
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
