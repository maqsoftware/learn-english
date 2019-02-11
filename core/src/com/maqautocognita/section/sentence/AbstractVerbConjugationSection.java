package com.maqautocognita.section.sentence;

import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.scene2d.ui.ConjugationLabel;
import com.maqautocognita.scene2d.ui.VerbConjugationSelectBox;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.service.SentenceVerbConjugationService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractVerbConjugationSection extends AbstractSentenceSection {

    private static final int SPACE_BETWEEN_COLUMN = 30;
    private static final int SPACE_BETWEEN_ROW = 20;

    private static final int SPACE_BETWEEN_TENSE_ICON_AND_TENSE_TEXT = 20;

    private static final int CONJUGATION_SELECT_BOX_WIDTH = 400;

    private static final float SUBJECT_LABEL_WIDTH = CONJUGATION_SELECT_BOX_WIDTH * 0.5f;

    private static final int START_Y_POSITION = ScreenUtils.getNavigationBarStartYPosition() - 100;

    private Texture verbTrayBackground;

    private List<ConjugationLabel> conjugationLabelList;

    public AbstractVerbConjugationSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        verbTrayBackground = getTextureFromSentenceFolder("verb conjugation tray.png");

        List<Actor> firstRowColumnList = addVerbRow("1S new.png", "mimi", getFirstSingleText());

        VerbConjugationSelectBox verbConjugationSelectBox =
                new VerbConjugationSelectBox(CONJUGATION_SELECT_BOX_WIDTH, 100,
                        getVerbStemTrayImageName(),
                        new IScrollPaneListener<String>() {
                            @Override
                            public void onPaneChanged(String item, int paneIndex) {
                                displayTense(item);
                            }
                        });
        verbConjugationSelectBox.setItems(SentenceVerbConjugationService.getInstance().getAllVerb());
        float yPosition = START_Y_POSITION - verbConjugationSelectBox.getHeight();
        verbConjugationSelectBox.setPosition(firstRowColumnList.get(1).getX(), yPosition);
        stage.addActor(verbConjugationSelectBox);

        yPosition = Math.max(Math.max(
                addColumnTitle(firstRowColumnList.get(3).getX(), yPosition, "past tense icon.png", "uliopita", getPastTenseText()),
                addColumnTitle(firstRowColumnList.get(4).getX(), yPosition, "present tense icon.png", "uliopo", getPresentTenseText())),
                addColumnTitle(firstRowColumnList.get(5).getX(), yPosition, "future tense icon.png", "ujao", getFutureTenseText()));

        yPosition = setRowYPosition(firstRowColumnList, yPosition);
        yPosition = addVerbRow(yPosition - SPACE_BETWEEN_ROW, "2S new.png", "wewe", getSecondSingleText());
        yPosition = addVerbRow(yPosition - SPACE_BETWEEN_ROW, "3S new.png", "yeye", getThirdSingleText());
        yPosition = addVerbRow(yPosition - SPACE_BETWEEN_ROW, "1P new.png", "sisi", getFirstPluralText());
        yPosition = addVerbRow(yPosition - SPACE_BETWEEN_ROW, "2P new.png", "ninyi", getSecondPluralText());
        addVerbRow(yPosition - SPACE_BETWEEN_ROW, "3P new.png", "wao", getThirdPluralText());

        verbConjugationSelectBox.setSelectedItem(SentenceVerbConjugationService.getInstance().getAllVerb().get(0));

    }

    private List<Actor> addVerbRow(String subjectImageName, String subjectText, String objectText) {
        float totalWidth = 0;
        List<Actor> columnActorList = new ArrayList<Actor>();
        Image subjectIcon = new Image(getTextureFromSentenceFolder(subjectImageName));
        addActor(columnActorList, subjectIcon);
        totalWidth += subjectIcon.getWidth() + SPACE_BETWEEN_COLUMN;
        Label subjectLabel = new Label(subjectText, getSentenceStyle());
        subjectLabel.setWidth(SUBJECT_LABEL_WIDTH);
        addActor(columnActorList, subjectLabel);
        totalWidth += subjectLabel.getWidth() + SPACE_BETWEEN_COLUMN;

        Label objectLabel = new Label(objectText, getSubjectStyle());
        addActor(columnActorList, objectLabel);
        objectLabel.setWidth(CONJUGATION_SELECT_BOX_WIDTH - subjectLabel.getWidth());
        totalWidth += objectLabel.getWidth() + SPACE_BETWEEN_COLUMN;


        ConjugationLabel pastVerbTray = new ConjugationLabel("", getVerbTrayStyle(), objectText, getPastTenseText());
        pastVerbTray.setSize(verbTrayBackground.getWidth(), verbTrayBackground.getHeight());
        addActor(columnActorList, pastVerbTray);
        addVerbConjugationLabelList(pastVerbTray);
        totalWidth += pastVerbTray.getWidth() + SPACE_BETWEEN_COLUMN;

        ConjugationLabel presentVerbTray = new ConjugationLabel("", pastVerbTray.getStyle(), objectText, getPresentTenseText());
        presentVerbTray.setSize(verbTrayBackground.getWidth(), verbTrayBackground.getHeight());
        addActor(columnActorList, presentVerbTray);
        addVerbConjugationLabelList(presentVerbTray);
        totalWidth += presentVerbTray.getWidth() + SPACE_BETWEEN_COLUMN;

        ConjugationLabel futureVerbTray = new ConjugationLabel("", pastVerbTray.getStyle(), objectText, getFutureTenseText());
        futureVerbTray.setSize(verbTrayBackground.getWidth(), verbTrayBackground.getHeight());
        addActor(columnActorList, futureVerbTray);
        addVerbConjugationLabelList(futureVerbTray);
        totalWidth += futureVerbTray.getWidth();

        float startXPosition = ScreenUtils.getXPositionForCenterObject(totalWidth);
        for (Actor actor : columnActorList) {
            actor.setX(startXPosition);
            startXPosition += actor.getWidth() + SPACE_BETWEEN_COLUMN;
            stage.addActor(actor);
        }

        return columnActorList;
    }

    protected abstract String getFirstSingleText();

    protected abstract String getVerbStemTrayImageName();

    private void displayTense(String selectedVerb) {
        if (CollectionUtils.isNotEmpty(conjugationLabelList)) {
            for (ConjugationLabel conjugationLabel : conjugationLabelList) {
                conjugationLabel.setText(selectedVerb);
            }
        }
    }

    private float addColumnTitle(float x, float y, String iconImageName, String tenseColumnName, String tenseContent) {
        Image tenseIcon = new Image(getTextureFromSentenceFolder(iconImageName));
        tenseIcon.setPosition(x, y);
        stage.addActor(tenseIcon);

        Label tenseColumnText = new Label(tenseColumnName, getTenseTitleStyle());
        tenseColumnText.setX(tenseIcon.getX() + tenseIcon.getWidth() + SPACE_BETWEEN_TENSE_ICON_AND_TENSE_TEXT);
        tenseColumnText.setY(tenseIcon.getY() + tenseIcon.getHeight() / 2);
        stage.addActor(tenseColumnText);

        if (StringUtils.isNotBlank(tenseContent)) {
            Label tenseContentText = new Label(tenseContent, getTensePrefixStyle());
            tenseContentText.setX(tenseColumnText.getX());
            tenseContentText.setY(tenseColumnText.getY() - tenseContentText.getHeight());
            stage.addActor(tenseContentText);
            return tenseContentText.getY();
        } else {
            return 0;
        }


    }

    protected abstract String getPastTenseText();

    protected abstract String getPresentTenseText();

    protected abstract String getFutureTenseText();

    private float setRowYPosition(List<Actor> columnActorList, float yPosition) {
        float nextYPosition = 0;
        for (Actor actor : columnActorList) {
            nextYPosition = yPosition - actor.getHeight();
            actor.setY(nextYPosition);
        }

        return nextYPosition;
    }

    private float addVerbRow(float yPosition, String subjectImageName, String subjectText, String objectText) {
        List<Actor> columnActorList = addVerbRow(subjectImageName, subjectText, objectText);
        return setRowYPosition(columnActorList, yPosition);
    }

    protected abstract String getSecondSingleText();

    protected abstract String getThirdSingleText();

    protected abstract String getFirstPluralText();

    protected abstract String getSecondPluralText();

    protected abstract String getThirdPluralText();

    private void addActor(List<Actor> columnActorList, Actor actor) {
        columnActorList.add(actor);
    }

    private Label.LabelStyle getSubjectStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(getSentenceFontSize());
        labelStyle.fontColor = Color.valueOf(ConjugationLabel.OBJECT_TEXT_COLOR_HEX_CODE);
        return labelStyle;
    }

    private Label.LabelStyle getVerbTrayStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(ConjugationLabel.NORMAL_FONT_SIZE);
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(verbTrayBackground));
        labelStyle.background.setLeftWidth(10);
        return labelStyle;
    }

    public void addVerbConjugationLabelList(ConjugationLabel conjugationLabel) {
        if (null == conjugationLabelList) {
            conjugationLabelList = new ArrayList<ConjugationLabel>();
        }
        conjugationLabelList.add(conjugationLabel);
    }

    private Label.LabelStyle getTenseTitleStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(TextFontSizeEnum.FONT_54);
        labelStyle.fontColor = ColorProperties.TEXT;
        return labelStyle;
    }

    private Label.LabelStyle getTensePrefixStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(getSentenceFontSize());
        labelStyle.fontColor = Color.valueOf(ConjugationLabel.TENSE_PREFIX_COLOR_HEX_CODE);
        return labelStyle;
    }

    @Override
    public void hide() {
        if (null != conjugationLabelList) {
            conjugationLabelList.clear();
            conjugationLabelList = null;
        }
        super.hide();
        if (null != verbTrayBackground) {
            verbTrayBackground.dispose();
            verbTrayBackground = null;
        }
    }
}
