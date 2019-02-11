package com.maqautocognita.section.sentence;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.Config;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.constant.SentenceClauseType;
import com.maqautocognita.constant.SentenceWordType;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.prototype.sentence.SentenceButton;
import com.maqautocognita.prototype.sentence.SentenceDisplayItem;
import com.maqautocognita.prototype.sentence.SentenceModule;
import com.maqautocognita.prototype.sentence.SentenceTenseEnum;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actors.AdvanceImage;
import com.maqautocognita.scene2d.ui.AbstractSentenceChangeButton;
import com.maqautocognita.scene2d.ui.SentenceChangeTenseButton;
import com.maqautocognita.scene2d.ui.SentenceSelectBox;
import com.maqautocognita.scene2d.ui.SentenceSinglePluralButton;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.SentenceWordTypeUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/19/2017.
 */

public class SentenceBuildSection extends AbstractSentenceSection {

    private static final int SELECT_BOX_SIZE = 200;

    private static final int SPACE_BETWEEN_TENSE_BUTTON_AND_OBJECT_BUTTON = 100;

    private static final int SPACE_BETWEEN_TENSE_BUTTONS = 20;

    private static final int MAXIMUM_NUMBER_OF_SELECT_BOX = 8;

    private static final int ADD_BUTTON_TOP_MARGIN = 20;

    private static final int SPACE_BETWEEN_SELECT_BOX = 20;

    private static final int SPACE_BETWEEN_CHANGE_OR_DELETE_BUTTON = 20;

    private static final String SUBJECT_CLAUSE_TYPE = "subject";

    private static final String OBJECT_CLAUSE_TYPE = "object";

    private static final String FIRST_SINGLE = "1S";

    private static final String SECOND_SINGLE = "2S";

    private static final String THIRD_SINGLE = "3S";

    private static final String FIRST_PLURAL = "1P";

    private static final String SECOND_PLURAL = "2P";

    private static final String THIRD_PLURAL = "3P";

    private List<SentenceSelectBox> selectBoxList;
    private List<SentenceSinglePluralButton> sentenceSinglePluralButtonList;
    private List<SentenceChangeTenseButton> sentenceChangeTenseButtonList;
    private List<AdvanceImage<SentenceButton>> patternFunctionButtonList;

    private SentenceModule sentenceModule;

    private float changeOrDeleteButtonAreaStartYPosition;

    private ShapeRenderer changeOrDeleteButtonAreaBackground;

    public SentenceBuildSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
        sentenceModule = new SentenceModule(AutoCognitaGame.storyModeDatabase);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        final float startX = 100;

        drawSentenceWithSpeaker("A", startX);

        float selectBoxYPosition = sentenceLabel.getY() - sentenceLabel.getHeight();
        float selectBoxXPosition = startX;

        selectBoxList = new ArrayList<SentenceSelectBox>(MAXIMUM_NUMBER_OF_SELECT_BOX);
        for (int i = 0; i < MAXIMUM_NUMBER_OF_SELECT_BOX; i++) {
            final SentenceSelectBox selectBox = createSelectBox();
            if (ScreenUtils.getScreenWidth() - selectBox.getWidth() + selectBoxXPosition <= startX) {
                selectBoxXPosition = startX;
                selectBoxYPosition -= selectBox.getHeight();
            }

            selectBox.setPosition(selectBoxXPosition, selectBoxYPosition - selectBox.getHeight());
            selectBoxXPosition += selectBox.getWidth() + SPACE_BETWEEN_SELECT_BOX;
            selectBoxList.add(selectBox);

            changeOrDeleteButtonAreaStartYPosition = selectBox.getY() - selectBox.getHeight();


        }

        addChangeButtonInBelow();

        showAndHideSelectBox();
    }

    @Override
    protected float getScreenStartYPosition() {
        return ScreenUtils.getNavigationBarStartYPosition() - 50;
    }

    @Override
    public void hide() {
        if (null != selectBoxList) {
            selectBoxList.clear();
            selectBoxList = null;
        }
        if (null != patternFunctionButtonList) {
            patternFunctionButtonList.clear();
            patternFunctionButtonList = null;
        }

        if (null != sentenceSinglePluralButtonList) {
            sentenceSinglePluralButtonList.clear();
            sentenceSinglePluralButtonList = null;
        }

        if (null != changeOrDeleteButtonAreaBackground) {
            changeOrDeleteButtonAreaBackground.dispose();
            changeOrDeleteButtonAreaBackground = null;
        }
        super.hide();
    }

    @Override
    public void render() {


        if (CollectionUtils.isNotEmpty(selectBoxList)) {
            if (null == changeOrDeleteButtonAreaBackground) {
                changeOrDeleteButtonAreaBackground = new ShapeRenderer();
                changeOrDeleteButtonAreaBackground.setColor(ColorProperties.DISABLE_TEXT);
                changeOrDeleteButtonAreaBackground.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            }
            changeOrDeleteButtonAreaBackground.begin(ShapeRenderer.ShapeType.Filled);
            for (SentenceSelectBox SentenceSelectBox : selectBoxList) {
                if (CollectionUtils.isNotEmpty(SentenceSelectBox.getChangeOrDeleteFunctionButtonList())) {
                    Image changeOrDeleteButton = SentenceSelectBox.getChangeOrDeleteFunctionButtonList().get(0);
                    changeOrDeleteButtonAreaBackground.rect(SentenceSelectBox.getX(),
                            changeOrDeleteButton.getY() - SPACE_BETWEEN_CHANGE_OR_DELETE_BUTTON,
                            SentenceSelectBox.getWidth(), changeOrDeleteButton.getHeight() + SPACE_BETWEEN_CHANGE_OR_DELETE_BUTTON * 2);

                }
            }

            changeOrDeleteButtonAreaBackground.end();
        }

        super.render();

    }

    private void addChangeButtonInBelow() {

        sentenceSinglePluralButtonList = new ArrayList<SentenceSinglePluralButton>();
        sentenceChangeTenseButtonList = new ArrayList<SentenceChangeTenseButton>();

        //the bottom row
        List<AbstractSentenceChangeButton> bottomRowList = new ArrayList<AbstractSentenceChangeButton>();

        SentenceSinglePluralButton selectedS1PButton =
                createChangeSinglePluralButtonImageWithYPosition("1P off new.png", "1P on new.png", SUBJECT_CLAUSE_TYPE,
                        FIRST_PLURAL);
        bottomRowList.add(selectedS1PButton);
        SentenceSinglePluralButton selectedS2PButton =
                createChangeSinglePluralButtonImageWithYPosition("2P off new.png", "2P on new.png", SUBJECT_CLAUSE_TYPE, SECOND_PLURAL);
        bottomRowList.add(selectedS2PButton);
        SentenceSinglePluralButton selectedS3PButton = createChangeSinglePluralButtonImageWithYPosition("3P off new.png", "3P on new.png", SUBJECT_CLAUSE_TYPE,
                THIRD_PLURAL);
        bottomRowList.add(selectedS3PButton);

        SentenceSinglePluralButton selectedO1PButton = createChangeSinglePluralButtonImageWithYPosition("1P off new.png", "1P on new.png", OBJECT_CLAUSE_TYPE,
                FIRST_PLURAL);
        bottomRowList.add(selectedO1PButton);
        SentenceSinglePluralButton selectedO2PButton = createChangeSinglePluralButtonImageWithYPosition("2P off new.png", "2P on new.png", OBJECT_CLAUSE_TYPE,
                SECOND_PLURAL);
        bottomRowList.add(selectedO2PButton);
        SentenceSinglePluralButton selectedO3PButton = createChangeSinglePluralButtonImageWithYPosition("3P off new.png", "3P on new.png", OBJECT_CLAUSE_TYPE,
                THIRD_PLURAL);
        bottomRowList.add(selectedO3PButton);

        SentenceChangeTenseButton selectedPastContinuousTenseButton =
                createChangeTenseButtonImageWithYPosition("past continuous tense off icon.png",
                        "past continuous tense on icon.png", SentenceTenseEnum.PASTCONTINUOUS);
        bottomRowList.add(selectedPastContinuousTenseButton);
        SentenceChangeTenseButton selectedPresentContinuousTenseButton = createChangeTenseButtonImageWithYPosition("present continuous tense off icon.png", "present continuous tense on icon.png", SentenceTenseEnum.PRESENTCONTINUOUS);
        bottomRowList.add(selectedPresentContinuousTenseButton);
        SentenceChangeTenseButton selectedFutureContinuousTenseButton = createChangeTenseButtonImageWithYPosition("future continuous tense off icon.png", "future continuous tense on icon.png", SentenceTenseEnum.FUTURECONTINUOUS);
        bottomRowList.add(selectedFutureContinuousTenseButton);


        float totalWidth = 0;
        for (Image tenseButton : bottomRowList) {
            totalWidth += tenseButton.getWidth() + SPACE_BETWEEN_TENSE_BUTTONS;
        }
        totalWidth += SPACE_BETWEEN_TENSE_BUTTON_AND_OBJECT_BUTTON;

        final float startX = ScreenUtils.getXPositionForCenterObject(totalWidth);

        float x = startX;
        for (int i = 0; i < bottomRowList.size(); i++) {
            bottomRowList.get(i).setX(x);
            x += bottomRowList.get(i).getWidth() + SPACE_BETWEEN_TENSE_BUTTONS;
            if ((i + 1) % 3 == 0) {
                x += SPACE_BETWEEN_TENSE_BUTTON_AND_OBJECT_BUTTON;
            }

            if (bottomRowList.get(i) instanceof SentenceChangeTenseButton) {
                sentenceChangeTenseButtonList.add((SentenceChangeTenseButton) bottomRowList.get(i));
            } else {
                sentenceSinglePluralButtonList.add((SentenceSinglePluralButton) bottomRowList.get(i));
            }

        }


        //the top row
        List<AbstractSentenceChangeButton> topRowList = new ArrayList<AbstractSentenceChangeButton>();

        SentenceSinglePluralButton selectedS1SButton =
                createChangeSinglePluralButtonImageWithYPosition("1S off new.png", "1S on new.png", SUBJECT_CLAUSE_TYPE,
                        FIRST_SINGLE);
        topRowList.add(selectedS1SButton);
        SentenceSinglePluralButton selectedS2SButton = createChangeSinglePluralButtonImageWithYPosition("2S off new.png", "2S on new.png", SUBJECT_CLAUSE_TYPE,
                SECOND_SINGLE);
        topRowList.add(selectedS2SButton);
        SentenceSinglePluralButton selectedS3SButton = createChangeSinglePluralButtonImageWithYPosition("3S off new.png", "3S on new.png", SUBJECT_CLAUSE_TYPE,
                THIRD_SINGLE);
        topRowList.add(selectedS3SButton);

        SentenceSinglePluralButton selectedO1SButton = createChangeSinglePluralButtonImageWithYPosition("1S off new.png", "1S on new.png", OBJECT_CLAUSE_TYPE,
                FIRST_SINGLE);
        topRowList.add(selectedO1SButton);
        SentenceSinglePluralButton selectedO2SButton = createChangeSinglePluralButtonImageWithYPosition("2S off new.png", "2S on new.png", OBJECT_CLAUSE_TYPE,
                SECOND_SINGLE);
        topRowList.add(selectedO2SButton);
        SentenceSinglePluralButton selectedO3SButton = createChangeSinglePluralButtonImageWithYPosition("3S off new.png", "3S on new.png", OBJECT_CLAUSE_TYPE,
                THIRD_SINGLE);
        topRowList.add(selectedO3SButton);


        SentenceChangeTenseButton selectedPastTenseButton = createChangeTenseButtonImage("past tense off icon.png", "past tense on icon.png",
                SentenceTenseEnum.PAST);
        topRowList.add(selectedPastTenseButton);
        SentenceChangeTenseButton selectedPresentTenseButton =
                createChangeTenseButtonImage("present tense off icon.png", "present tense on icon.png", SentenceTenseEnum.PRESENT);
        topRowList.add(selectedPresentTenseButton);
        SentenceChangeTenseButton selectedFutureTenseButton =
                createChangeTenseButtonImage("future tense off icon.png", "future tense on icon.png", SentenceTenseEnum.FUTURE);
        topRowList.add(selectedFutureTenseButton);


        x = startX;
        for (int i = 0; i < topRowList.size(); i++) {
            topRowList.get(i).setPosition(x, selectedPastContinuousTenseButton.getY() + selectedPastContinuousTenseButton.getHeight() + SPACE_BETWEEN_TENSE_BUTTONS);
            x += topRowList.get(i).getWidth() + SPACE_BETWEEN_TENSE_BUTTONS;
            if ((i + 1) % 3 == 0) {
                x += SPACE_BETWEEN_TENSE_BUTTON_AND_OBJECT_BUTTON;
            }

            if (topRowList.get(i) instanceof SentenceChangeTenseButton) {
                sentenceChangeTenseButtonList.add((SentenceChangeTenseButton) topRowList.get(i));
            } else {
                sentenceSinglePluralButtonList.add((SentenceSinglePluralButton) topRowList.get(i));
            }
        }

        reloadSentenceButtonSelectionState();

    }

    private SentenceChangeTenseButton createChangeTenseButtonImageWithYPosition(String imageName, String selectedImageName,
                                                                                SentenceTenseEnum sentenceTenseEnum) {
        SentenceChangeTenseButton image = createChangeTenseButtonImage(imageName, selectedImageName, sentenceTenseEnum);
        image.setY(100);
        return image;
    }

    private SentenceSinglePluralButton createChangeSinglePluralButtonImageWithYPosition(String imageName, String selectedImageName,
                                                                                        String clauseType, String count) {
        SentenceSinglePluralButton image = createChangeSinglePluralButtonImage(imageName, selectedImageName, clauseType, count);
        image.setY(100);
        return image;
    }

    private SentenceSinglePluralButton createChangeSinglePluralButtonImage(
            String imageName, String selectedImageName, String clauseType, String count) {
        final SentenceSinglePluralButton sentenceSinglePluralButton =
                new SentenceSinglePluralButton(clauseType, count, sentenceModule, getTextureFromSentenceFolder(imageName),
                        getTextureFromSentenceFolder(selectedImageName));
        sentenceSinglePluralButton.setOnButtonSelectedListener(new IActionListener() {
            @Override
            public void onComplete() {
                reloadSentenceButtonSelectionState();
            }
        });
        stage.addActor(sentenceSinglePluralButton);
        return sentenceSinglePluralButton;
    }


    private SentenceChangeTenseButton createChangeTenseButtonImage(String imageName, String selectedImageName,
                                                                   SentenceTenseEnum sentenceTenseEnum) {
        final SentenceChangeTenseButton sentenceChangeTenseButton =
                new SentenceChangeTenseButton(sentenceModule, sentenceTenseEnum, getTextureFromSentenceFolder(imageName),
                        getTextureFromSentenceFolder(selectedImageName));
        sentenceChangeTenseButton.setOnButtonSelectedListener(new IActionListener() {
            @Override
            public void onComplete() {
                reloadSentenceButtonSelectionState();
            }
        });
        stage.addActor(sentenceChangeTenseButton);
        return sentenceChangeTenseButton;
    }

    private void reloadSentenceButtonSelectionState() {
        showAndHideSelectBox();
        if (null != sentenceSinglePluralButtonList) {
            for (SentenceSinglePluralButton sentenceChangeButton : sentenceSinglePluralButtonList) {
                if (SUBJECT_CLAUSE_TYPE.equals(sentenceChangeButton.getClauseType())) {
                    if (FIRST_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS1SButton);
                    } else if (SECOND_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS2SButton);
                    } else if (THIRD_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS3SButton);
                    } else if (FIRST_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS1PButton);
                    } else if (SECOND_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS2PButton);
                    } else if (THIRD_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedS3PButton);
                    }
                } else if (OBJECT_CLAUSE_TYPE.equals(sentenceChangeButton.getClauseType())) {
                    if (FIRST_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO1SButton);
                    } else if (SECOND_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO2SButton);
                    } else if (THIRD_SINGLE.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO3SButton);
                    } else if (FIRST_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO1PButton);
                    } else if (SECOND_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO2PButton);
                    } else if (THIRD_PLURAL.equals(sentenceChangeButton.getCount())) {
                        sentenceChangeButton.setSelected(sentenceModule.selectedO3PButton);
                    }
                }
            }
        }

        if (null != sentenceChangeTenseButtonList) {
            for (SentenceChangeTenseButton sentenceChangeButton : sentenceChangeTenseButtonList) {
                switch (sentenceChangeButton.getSentenceTenseEnum()) {
                    case PAST:
                        sentenceChangeButton.setSelected(sentenceModule.selectedPastTenseButton);
                        break;
                    case PRESENT:
                        sentenceChangeButton.setSelected(sentenceModule.selectedPresentTenseButton);
                        break;
                    case FUTURE:
                        sentenceChangeButton.setSelected(sentenceModule.selectedFutureTenseButton);
                        break;
                    case PASTCONTINUOUS:
                        sentenceChangeButton.setSelected(sentenceModule.selectedPastContinuousTenseButton);
                        break;
                    case PRESENTCONTINUOUS:
                        sentenceChangeButton.setSelected(sentenceModule.selectedPresentContinuousTenseButton);
                        break;
                    case FUTURECONTINUOUS:
                        sentenceChangeButton.setSelected(sentenceModule.selectedFutureContinuousTenseButton);
                        break;
                }
            }
        }
    }

    private void showSentence() {
        StringBuilder sentence = new StringBuilder();
        if (CollectionUtils.isNotEmpty(selectBoxList)) {
            for (SentenceSelectBox sentenceSelectBox : selectBoxList) {
                if (sentenceSelectBox.isVisible()) {
                    SentenceDisplayItem otherSelectedSentenceDisplayItem = sentenceSelectBox.getSelectedItem();
                    if (null != otherSelectedSentenceDisplayItem) {
                        sentence.append(otherSelectedSentenceDisplayItem.getDisplay() + " ");
                    }
                }
            }
            if (sentence.length() > 0) {
                sentence.deleteCharAt(sentence.length - 1);
                sentence.append(".");
            }
        }
        setSentenceLabelText(sentence.toString());
    }

    private SentenceSelectBox createSelectBox() {

        SentenceSelectBox sentenceSelectBox = new SentenceSelectBox(SELECT_BOX_SIZE, SELECT_BOX_SIZE,
                new IScrollPaneListener<SentenceDisplayItem>() {
                    @Override
                    public void onPaneChanged(SentenceDisplayItem selectedSentenceDisplayItem, int paneIndex) {
                        if (null != selectedSentenceDisplayItem) {
                            Gdx.app.log(getClass().getName(), "selected stem = " + selectedSentenceDisplayItem.getStem() + ", display item = " + selectedSentenceDisplayItem.getDisplay());
                            sentenceModule.setcurrOAdjective(selectedSentenceDisplayItem.getStem(), selectedSentenceDisplayItem.getDisplay());
                            showSentence();
                        }
                    }
                });

        stage.addActor(sentenceSelectBox);

        return sentenceSelectBox;
    }

    private void showAndHideSelectBox() {

        for (SentenceSelectBox selectBox : selectBoxList) {
            selectBox.clearAndHide();
        }
        //hide all previous pattern function buttons
        if (CollectionUtils.isNotEmpty(patternFunctionButtonList)) {
            for (AdvanceImage addButton : patternFunctionButtonList) {
                addButton.setVisible(false);
            }
        }

        int index = 0;
        if (sentenceModule.enableSPN) {
            changeBackground(selectBoxList.get(index),
                    SentenceClauseType.SUBJECT, SentenceWordType.PRONOUN, sentenceModule.selSPN,
                    sentenceModule.getCurrSPN());
            index++;
        }

        if (sentenceModule.enableSArticle) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.SUBJECT, SentenceWordType.ARTICLE,
                    sentenceModule.selSArticle, sentenceModule.getcurrSArticle());
            index++;
        }

        if (sentenceModule.enableSPPN) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.SUBJECT, SentenceWordType.PRONOUN,
                    sentenceModule.selSPPN, sentenceModule.getcurrSPPN());
            index++;
        }

        if (sentenceModule.enableSAdjective) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.SUBJECT, SentenceWordType.ADJECTIVE,
                    sentenceModule.selSAdjective, sentenceModule.getcurrSAdjective());
            index++;
        }
        if (sentenceModule.enableSNoun) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.SUBJECT, SentenceWordType.NOUN,
                    sentenceModule.selSNoun, sentenceModule.getcurrSNoun());
            index++;
        }
        if (sentenceModule.enableAVerb) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.ACTION, SentenceWordType.VERB,
                    sentenceModule.selAVerb, sentenceModule.getcurrAVerb());
            index++;
        }
        if (sentenceModule.enableAAdverb) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.ACTION, SentenceWordType.ADVERB,
                    sentenceModule.selAAdverb, sentenceModule.getcurrAAdverb());
            index++;
        }

        if (sentenceModule.enableAPreposition) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.ACTION, SentenceWordType.PREPOSITION,
                    sentenceModule.selAPreposition, sentenceModule.getcurrAPreposition());
            index++;
        }

        if (sentenceModule.enableOPN) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.OBJECT, SentenceWordType.PRONOUN,
                    sentenceModule.selOPN, sentenceModule.getCurrOPN());
            index++;
        }

        if (sentenceModule.enableOArticle) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.OBJECT, SentenceWordType.ARTICLE,
                    sentenceModule.selOArticle, sentenceModule.getcurrOArticle());
            index++;
        }

        if (sentenceModule.enableOPPN) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.OBJECT, SentenceWordType.PRONOUN,
                    sentenceModule.selOPN, sentenceModule.getCurrOPN());
            index++;
        }

        if (sentenceModule.enableOAdjective) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.OBJECT, SentenceWordType.ADJECTIVE, sentenceModule.selOAdjective
                    , sentenceModule.getcurrOAdjective());
            index++;
        }

        if (sentenceModule.enableONoun) {
            changeBackground(selectBoxList.get(index), SentenceClauseType.OBJECT, SentenceWordType.NOUN, sentenceModule.selONoun
                    , sentenceModule.getcurrONoun());
            index++;
        }


        for (int i = 0; i < selectBoxList.size(); i++) {
            if (i < index) {
                selectBoxList.get(i).setVisible(true);
            }
        }

        if (CollectionUtils.isNotEmpty(sentenceModule.vSentenceButton)) {

            for (SentenceButton button : sentenceModule.vSentenceButton) {
                //check the button is related to which select box
                SentenceSelectBox selectBox = getSentenceSelectBox(button);
                if (null != selectBox) {

                    float x = 0, y = 0;
                    switch (button.vButtonName) {
                        case ADD_AFTER_1:
                        case ADD_AFTER_2:
                            AdvanceImage<SentenceButton> functionButton = getPatternFunctionButtonBySentenceButton(button, true);

                            x = selectBox.getX() + selectBox.getWidth() - functionButton.getWidth() / 2;

                            if (CollectionUtils.isNotEmpty(selectBox.getAddFunctionButtonList())) {
                                for (AdvanceImage existingAddButton : selectBox.getAddFunctionButtonList()) {
                                    x = Math.max(x, existingAddButton.getX() + existingAddButton.getWidth());
                                }
                            }

                            y = selectBox.getY() - functionButton.getHeight() - ADD_BUTTON_TOP_MARGIN;
                            selectBox.addAddFunctionButton(functionButton);
                            functionButton.setPosition(x, y);
                            break;
                        case ADD_BEFORE:
                            functionButton = getPatternFunctionButtonBySentenceButton(button, true);
                            x = selectBox.getX() - functionButton.getWidth() / 2;
                            y = selectBox.getY() - functionButton.getHeight() - ADD_BUTTON_TOP_MARGIN;
                            selectBox.addAddFunctionButton(functionButton);
                            functionButton.setPosition(x, y);
                            break;
                        case CHANGE_1:
                        case CHANGE_2:
                        case CHANGE_3:
                            functionButton = getPatternFunctionButtonBySentenceButton(button, false);
                            selectBox.addChangeOrDeleteFunctionButton(functionButton);
                            float startX = selectBox.getX() +
                                    ScreenUtils.getXPositionForCenterObject(selectBox.getTotalWidthOfChangeOrDeleteButton(SPACE_BETWEEN_CHANGE_OR_DELETE_BUTTON), selectBox.getWidth());
                            for (AdvanceImage changeOrDeleteButton : selectBox.getChangeOrDeleteFunctionButtonList()) {
                                changeOrDeleteButton.setPosition(startX, changeOrDeleteButtonAreaStartYPosition);
                                startX += changeOrDeleteButton.getWidth();
                            }
                            break;

                    }
                }
            }
        }

        showSentence();
    }

    private void changeBackground(SentenceSelectBox selectBox, SentenceClauseType sentenceClauseType, SentenceWordType sentenceWordType,
                                  List<SentenceDisplayItem> itemList, SentenceDisplayItem defaultDisplayItem) {

        selectBox.setSentenceClauseType(sentenceClauseType);
        selectBox.setSentenceWordType(sentenceWordType);

        selectBox.changeBackground();
        selectBox.setItems(itemList);

        if (CollectionUtils.isNotEmpty(itemList)) {
            if (null != defaultDisplayItem) {
                selectBox.setSelectedItem(defaultDisplayItem);
            }
        }

        selectBox.setVisible(true);

    }

    public SentenceSelectBox getSentenceSelectBox(SentenceButton button) {
        for (SentenceSelectBox SentenceSelectBox : selectBoxList) {
            if (SentenceSelectBox.isVisible() && SentenceSelectBox.getSentenceClauseType().isEquals(button.vButtonType)
                    && SentenceSelectBox.getSentenceWordType().isEquals(button.vP1)
                    ) {
                return SentenceSelectBox;
            }
        }

        return null;
    }

    private AdvanceImage<SentenceButton> getPatternFunctionButtonBySentenceButton(SentenceButton sentenceButton, boolean isAddButton) {
        AdvanceImage<SentenceButton> button = null;
        if (null != sentenceButton) {

            String imageName = isAddButton ? "triangle" : "circle";

            String imagePath = Config.SENTENCE_IMAGE_PATH +
                    SentenceWordTypeUtils.getImageNameByWordType(SentenceWordType.getByWordType(sentenceButton.vP2)) + " " + imageName + ".png";

            if (CollectionUtils.isNotEmpty(patternFunctionButtonList)) {
                for (AdvanceImage<SentenceButton> addButton : patternFunctionButtonList) {
                    if (addButton.getImagePath().equals(imagePath)) {
                        button = addButton;
                        button.setVisible(true);
                    }
                }
            }

            if (null == button) {
                button = new AdvanceImage<SentenceButton>(sentenceButton, imagePath);
                stage.addActor(button);

                if (null == patternFunctionButtonList) {

                    patternFunctionButtonList = new ArrayList<AdvanceImage<SentenceButton>>();
                }
                patternFunctionButtonList.add(button);

                final AdvanceImage<SentenceButton> functionButton = button;

                functionButton.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int buttonCount) {
                        sentenceModule.changePattern(functionButton.getId().vButtonType, functionButton.getId().vNewState);
                        showAndHideSelectBox();
                    }
                });


            } else {
                button.setId(sentenceButton);
            }

        }

        return button;
    }
}
