package com.maqautocognita.section.sentence;

import com.maqautocognita.Config;
import com.maqautocognita.bo.SentenceNounConjugation;
import com.maqautocognita.constant.ActivityCodeEnum;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.scene2d.ui.ConjugationLabel;
import com.maqautocognita.scene2d.ui.NounConjugationSelectBox;
import com.maqautocognita.screens.AbstractSentenceScreen;
import com.maqautocognita.service.SentenceNounConjugationService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import java.io.File;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class NounConjugationSection extends AbstractSentenceSection {

    private static final int CONJUGATION_SELECT_BOX_WIDTH = 400;
    private static final int CONJUGATION_SELECT_BOX_HEIGHT = 100;

    private static final float EXPECTED_SELECTED_ITEM_IMAGE_HEIGHT = 400;

    private static final int SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX = 30;

    private static final int MARGIN_LEFT_RIGHT_RIGHT_ARROW = 60;

    private static final int SPACE_BETWEEN_SELECTED_ITEM_IMAGE_AND_WORD_TRAY = 100;

    private static final int PADDING_LEFT_TEXT = 10;

    private Texture wordTrayBackground;

    private float selectItemImageTopYPosition;

    public NounConjugationSection(AbstractSentenceScreen abstractSentenceScreen) {
        super(abstractSentenceScreen);
    }

    @Override
    public void onShow(ActivityCodeEnum activityCodeEnum) {

        wordTrayBackground = getTextureFromSentenceFolder("verb conjugation tray.png");

        Texture nounTrayBackgroundTexture = getTextureFromSentenceFolder("noun prefix tray.png");
        final Image nounTrayBackground = new Image(nounTrayBackgroundTexture);
        nounTrayBackground.setSize(nounTrayBackgroundTexture.getWidth(), nounTrayBackgroundTexture.getHeight());
        stage.addActor(nounTrayBackground);

        final Label nounSingular = new Label(null, getNounTrayStyle());
        nounSingular.setWidth(nounTrayBackground.getWidth());
        nounSingular.setHeight(nounSingular.getStyle().font.getLineHeight());
        stage.addActor(nounSingular);

        final Label nounPlural = new Label(null, getNounTrayStyle());
        nounPlural.setWidth(nounTrayBackground.getWidth());
        nounPlural.setHeight(nounPlural.getStyle().font.getLineHeight());
        stage.addActor(nounPlural);


        final Image selectedItemImage = new Image();
        selectedItemImage.setY(500);
        selectedItemImage.setScaling(Scaling.fit);
        stage.addActor(selectedItemImage);

        final ConjugationLabel wordSingular = new ConjugationLabel("", getWordTrayStyle());
        stage.addActor(wordSingular);
        final ConjugationLabel wordPlural = new ConjugationLabel("", getWordTrayStyle());
        stage.addActor(wordPlural);

        NounConjugationSelectBox nounConjugationSelectBox =
                new NounConjugationSelectBox(CONJUGATION_SELECT_BOX_WIDTH, CONJUGATION_SELECT_BOX_HEIGHT,
                        "verb stem tray.png", new IScrollPaneListener<SentenceNounConjugation>() {
                    @Override
                    public void onPaneChanged(SentenceNounConjugation sentenceNounConjugation, int paneIndex) {
                        Gdx.app.log(getClass().getName(), "selected english = " + sentenceNounConjugation.english);
                        nounSingular.setText(sentenceNounConjugation.prefixSingular);
                        nounPlural.setText(sentenceNounConjugation.prefixPlural);

                        wordPlural.setObject(sentenceNounConjugation.prefixPlural);
                        wordPlural.setText(sentenceNounConjugation.root);

                        wordSingular.setObject(sentenceNounConjugation.prefixSingular);
                        wordSingular.setText(sentenceNounConjugation.root);

                        if (StringUtils.isNotBlank(sentenceNounConjugation.english) &&
                                Gdx.files.internal(Config.LESSON_IMAGE_FOLDER_NAME + File.separator + sentenceNounConjugation.english + ".png").exists()) {

                            Texture image = new Texture(Config.LESSON_IMAGE_FOLDER_NAME + File.separator + sentenceNounConjugation.english + ".png");
                            selectedItemImage.setDrawable(
                                    new TextureRegionDrawable(new TextureRegion(image)));

                            selectedItemImage.setSize(image.getWidth(), image.getHeight());
                            if (image.getHeight() > EXPECTED_SELECTED_ITEM_IMAGE_HEIGHT) {
                                float scale = EXPECTED_SELECTED_ITEM_IMAGE_HEIGHT / image.getHeight();
                                selectedItemImage.setSize(image.getWidth() * scale, EXPECTED_SELECTED_ITEM_IMAGE_HEIGHT);
                            }
                            selectedItemImage.setPosition(ScreenUtils.getXPositionForCenterObject(selectedItemImage.getWidth()),
                                    selectItemImageTopYPosition - selectedItemImage.getHeight()
                            );
                        } else {
                            selectedItemImage.setDrawable(null);
                        }

                    }
                });

        nounConjugationSelectBox.setItems(SentenceNounConjugationService.getInstance().getAllSentenceNounConjugation());
        stage.addActor(nounConjugationSelectBox);


        Image rightArrow = new Image(getTextureFromSentenceFolder("right arrow.png"));
        rightArrow.setSize(rightArrow.getDrawable().getMinWidth(), rightArrow.getDrawable().getMinHeight());
        stage.addActor(rightArrow);

        Image thirdSingularIcon = new Image(getTextureFromSentenceFolder("3S new.png"));
        thirdSingularIcon.setSize(thirdSingularIcon.getDrawable().getMinWidth(), thirdSingularIcon.getDrawable().getMinHeight());
        stage.addActor(thirdSingularIcon);
        Image thirdPluralIcon = new Image(getTextureFromSentenceFolder("3P new.png"));
        thirdPluralIcon.setSize(thirdPluralIcon.getDrawable().getMinWidth(), thirdPluralIcon.getDrawable().getMinHeight());
        stage.addActor(thirdPluralIcon);


        float totalWidth = nounTrayBackground.getWidth() +
                SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX + nounConjugationSelectBox.getWidth() + MARGIN_LEFT_RIGHT_RIGHT_ARROW
                + rightArrow.getWidth() + MARGIN_LEFT_RIGHT_RIGHT_ARROW + wordSingular.getWidth() + SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX
                + thirdSingularIcon.getWidth();
        float startX = ScreenUtils.getXPositionForCenterObject(totalWidth);

        nounTrayBackground.setPosition(startX, getScreenStartYPosition() - nounTrayBackground.getHeight());
        nounSingular.setPosition(nounTrayBackground.getX() + PADDING_LEFT_TEXT, nounTrayBackground.getY() + nounTrayBackground.getHeight() - nounSingular.getHeight());
        nounPlural.setPosition(nounTrayBackground.getX() + PADDING_LEFT_TEXT, nounTrayBackground.getY());

        nounConjugationSelectBox.
                setPosition(nounTrayBackground.getX() + nounTrayBackground.getWidth() + SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX,
                        //center the select box to the noun tray
                        nounTrayBackground.getY() +
                                ScreenUtils.getBottomYPositionForCenterObject(nounConjugationSelectBox.getHeight(),
                                        nounTrayBackground.getHeight())
                );

        rightArrow.
                setPosition(nounConjugationSelectBox.getX() + nounConjugationSelectBox.getWidth() + MARGIN_LEFT_RIGHT_RIGHT_ARROW,
                        //center the select box to the noun tray
                        nounTrayBackground.getY() +
                                ScreenUtils.getBottomYPositionForCenterObject(rightArrow.getHeight(), nounTrayBackground.getHeight())
                );

        wordSingular.setPosition(rightArrow.getX() + rightArrow.getWidth() + MARGIN_LEFT_RIGHT_RIGHT_ARROW,
                nounTrayBackground.getY() + nounTrayBackground.getHeight() - wordSingular.getHeight()
        );

        wordPlural.setPosition(rightArrow.getX() + rightArrow.getWidth() + MARGIN_LEFT_RIGHT_RIGHT_ARROW,
                nounTrayBackground.getY());

        thirdSingularIcon.setPosition(wordSingular.getX() + wordSingular.getWidth() + SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX,
                wordSingular.getY());

        thirdPluralIcon.setPosition(wordPlural.getX() + wordPlural.getWidth() + SPACE_BETWEEN_NOUN_TRAY_AND_NOUN_CONJUGATION_SELECT_BOX,
                wordPlural.getY());

        selectItemImageTopYPosition = wordPlural.getY() - SPACE_BETWEEN_SELECTED_ITEM_IMAGE_AND_WORD_TRAY;

        nounConjugationSelectBox.setSelectedItem(SentenceNounConjugationService.getInstance().getAllSentenceNounConjugation().get(0));

    }

    @Override
    public void hide() {
        wordTrayBackground = null;
        super.hide();
    }

    private Label.LabelStyle getNounTrayStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(ConjugationLabel.NORMAL_FONT_SIZE);
        labelStyle.fontColor = Color.valueOf(ConjugationLabel.OBJECT_TEXT_COLOR_HEX_CODE);
        return labelStyle;
    }

    private Label.LabelStyle getWordTrayStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(ConjugationLabel.NORMAL_FONT_SIZE);
        labelStyle.background = new TextureRegionDrawable(new TextureRegion(wordTrayBackground));
        labelStyle.background.setLeftWidth(PADDING_LEFT_TEXT);
        return labelStyle;
    }
}
