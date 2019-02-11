package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.IDemoListener2;
import com.maqautocognita.scene2d.actions.AbstractAdvanceListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.service.DemoService2;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DemoActor2 extends Actor {


    private final IDemoListener2 demoListener;
    private String currentScreen = "DemoScreenAlphabet";
    private static final int SELECTED_TEXT_AREA_BORDER_WIDTH = 2;
    private static final float GAP_BETWEEN_TEXT_AREA = 10;
    private static final int TEXT_AREA_MARGIN = 20;
    private static final int INNER_TEXT_AREA_MARGIN = 10;
    private final TextFontSizeEnum textFontSize;
    private final int iconSize;
    private ShapeRenderer background, englishOuterTextAreaBackground, selectedTextAreaBackground;
    private TextArea englishTextArea;
    private Label englishTextAreaTips;
    private TextArea spainTextArea;
    private Label spainTextAreaTips;
    private boolean isLanguageSelected;
    private boolean isEnglishSelected = true;
    private Image homeIcon,
            imageDemoScreenAddSubtractBackground,
            imageDemoScreenAlphabetBackground,imageDemoScreenComprehension1Background,imageDemoScreenComprehension2Background,
            imageDemoScreenComprehension3Background, imageDemoScreenCountBackground,imageDemoScreenMultiplyBackground,
            imageDemoScreenPhonics1Background,imageDemoScreenPhonics2Background,imageDemoScreenPhonics3Background,imageDemoScreenPhonics4Background,
            imageDemoScreenWord1Background,imageDemoScreenWord2Background,imageDemoScreenWord3Background,imageDemoScreenWord4Background,
            imageDemoScreenOtherBackground,imageDemoScreenLargeNumberBackground;
    ;

    private String showText;
    private String showTranslatedText;
    private IActionListener onReturnClickListener;

    private String recognizedText;

    private String spanishRecognizedText;

    private BitmapFont recognizedTextBitmapFont;
    private boolean isTranslated = true;

    public DemoActor2() {
        this.demoListener = DemoService2.getInstance(null);
        iconSize = 200;
        textFontSize = TextFontSizeEnum.FONT_48;
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                boolean isChildrenTouch = false;
                if (isTouch(event.getStageX(), event.getStageY(),
                        homeIcon
                        , englishTextArea, spainTextArea)) {
                    isChildrenTouch = true;
                }

                if (!isChildrenTouch) {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
            }

            private boolean isTouch(float touchX, float touchY, Actor... actors) {
                for (Actor actor : actors) {
                    return TouchUtils.isTouched(actor, (int) touchX, (int) touchY);
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            super.draw(batch, parentAlpha);
            batch.end();

            if (null == background) {
                background = new ShapeRenderer();
            }

            if (null == englishOuterTextAreaBackground) {
                englishOuterTextAreaBackground = new ShapeRenderer();
            }

            if (null == selectedTextAreaBackground) {
                selectedTextAreaBackground = new ShapeRenderer();
            }

            background.setProjectionMatrix(batch.getProjectionMatrix());
            background.begin(ShapeRenderer.ShapeType.Filled);
            background.rect(getX(), getY(), getWidth(), getHeight());
            //background.setColor(ColorProperties.LIFE_SKILL_BOTTOM_BACKGROUND);
            background.setColor(Color.valueOf("FFFFFF"));
            background.end();

            englishOuterTextAreaBackground.setProjectionMatrix(batch.getProjectionMatrix());
            englishOuterTextAreaBackground.begin(ShapeRenderer.ShapeType.Filled);
            englishOuterTextAreaBackground.setColor(Color.WHITE);

            selectedTextAreaBackground.setProjectionMatrix(batch.getProjectionMatrix());
            selectedTextAreaBackground.begin(ShapeRenderer.ShapeType.Filled);
            //selectedTextAreaBackground.setColor(Color.YELLOW);
            selectedTextAreaBackground.setColor(Color.WHITE);

            float[] size = getEnglishOuterTextAreaSize();

            if (isLanguageSelected) {
                float height = size[1] / 2 - GAP_BETWEEN_TEXT_AREA;

                //draw the bottom text area
                englishOuterTextAreaBackground.rect(TEXT_AREA_MARGIN, getOuterTextAreaStartYPosition(), size[0], height);
                //draw the top text area
                englishOuterTextAreaBackground.rect(TEXT_AREA_MARGIN, getOuterTextAreaStartYPosition() + height + GAP_BETWEEN_TEXT_AREA, size[0], height);

                float bound[] = new float[4];
                bound[0] = TEXT_AREA_MARGIN - SELECTED_TEXT_AREA_BORDER_WIDTH;
                bound[2] = size[0] + SELECTED_TEXT_AREA_BORDER_WIDTH * 2;
                bound[3] = height + SELECTED_TEXT_AREA_BORDER_WIDTH * 2;
                if (isEnglishSelected) {
                    bound[1] = getOuterTextAreaStartYPosition() + height + GAP_BETWEEN_TEXT_AREA - SELECTED_TEXT_AREA_BORDER_WIDTH;
                } else {
                    bound[1] = getOuterTextAreaStartYPosition() - SELECTED_TEXT_AREA_BORDER_WIDTH;
                }
                selectedTextAreaBackground.rect(bound[0], bound[1], bound[2], bound[3]);

            } else {

                background.setProjectionMatrix(batch.getProjectionMatrix());
                background.begin(ShapeRenderer.ShapeType.Filled);
                background.rect(getX(), getY(), getWidth(), getHeight());
                background.setColor(ColorProperties.LIFE_SKILL_BOTTOM_BACKGROUND);
                background.end();

                selectedTextAreaBackground.rect(TEXT_AREA_MARGIN - SELECTED_TEXT_AREA_BORDER_WIDTH,
                        getOuterTextAreaStartYPosition() - SELECTED_TEXT_AREA_BORDER_WIDTH,
                        size[0] + SELECTED_TEXT_AREA_BORDER_WIDTH * 2, size[1] + SELECTED_TEXT_AREA_BORDER_WIDTH * 2);

                englishOuterTextAreaBackground.rect(TEXT_AREA_MARGIN, getOuterTextAreaStartYPosition(), size[0], size[1]);
            }

            selectedTextAreaBackground.end();
            englishOuterTextAreaBackground.end();

            batch.begin();

            addTextArea();

            if (StringUtils.isNotBlank(showText) && null != englishTextArea) {
                englishTextAreaTips.setVisible(false);
                englishTextArea.setText(showText);
                if (StringUtils.isNotBlank(showTranslatedText)) {
                    setSpanishText(showTranslatedText);
                } else {
                    doTranslation(showText, false, true);
                }
                showText = null;
                showTranslatedText = null;
                homeIcon.setVisible(false);
            }

            if (null == recognizedTextBitmapFont) {
                recognizedTextBitmapFont = FontGeneratorManager.getFont(textFontSize);
            }

            if (StringUtils.isNotBlank(recognizedText)) {
                englishTextAreaTips.setVisible(false);
                englishTextArea.setVisible(false);
                recognizedTextBitmapFont.setColor(ColorProperties.TEXT);
                recognizedTextBitmapFont.draw(batch, recognizedText,
                        englishTextArea.getX(), englishTextArea.getY() + englishTextArea.getHeight(),
                        englishTextArea.getWidth(), Align.left, true);
            }

            if (StringUtils.isNotBlank(spanishRecognizedText)) {
                spainTextAreaTips.setVisible(false);
                spainTextArea.setVisible(false);
                recognizedTextBitmapFont.setColor(ColorProperties.BORDER);
                recognizedTextBitmapFont.draw(batch, spanishRecognizedText,
                        spainTextArea.getX(), spainTextArea.getY() + spainTextArea.getHeight(),
                        spainTextArea.getWidth(), Align.left, true);
            }
        }

    }

    @Override
    public boolean remove() {
        stopMicrophoneListener();
        return super.remove();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (null != homeIcon) {
            homeIcon.setVisible(visible);

            englishTextArea.setVisible(visible);
            englishTextAreaTips.setVisible(visible);
            spainTextArea.setVisible(visible);
            spainTextAreaTips.setVisible(visible);
        }

    }

    public void stopMicrophoneListener() {

        demoListener.onSpeechToTextStop();
        try {
            if (!englishTextArea.isVisible()) {
                englishTextArea.setText(recognizedText);
                englishTextArea.setVisible(true);
                recognizedText = null;
            }

            if (!spainTextArea.isVisible()) {
                spainTextArea.setText(spanishRecognizedText);
                spainTextArea.setVisible(true);
                spanishRecognizedText = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private float[] getInnerTextAreaSize() {

        float[] englishOuterTextAreaSize = getEnglishOuterTextAreaSize();

        return new float[]{englishOuterTextAreaSize[0] - INNER_TEXT_AREA_MARGIN * 2, englishOuterTextAreaSize[1] - INNER_TEXT_AREA_MARGIN * 2};
    }

    private float[] getEnglishOuterTextAreaSize() {

        float width = 0, height = 0;

        if (ScreenUtils.isTablet) {
            width = getWidth() / 2 - TEXT_AREA_MARGIN * 2;
            height = getHeight() - TEXT_AREA_MARGIN * 2;
        } else {
            width = getWidth() - TEXT_AREA_MARGIN * 2 - iconSize;
            height = getHeight() - TEXT_AREA_MARGIN * 2 - iconSize;
        }

        return new float[]{width
                , height};
    }

    private TextField.TextFieldStyle getTextAreaTextStyle(Language language) {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = FontGeneratorManager.getFont(textFontSize);
        textFieldStyle.fontColor =
                Language.ENGLISH.equals(language) ?
                        ColorProperties.TEXT : ColorProperties.BORDER;

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(Color.WHITE);
        pm1.fill();
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pm1)));


        Pixmap pm2 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm2.setColor(Color.BLACK);
        pm2.fill();
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(pm2)));

        Pixmap pm3 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm3.setColor(Color.BLACK);
        pm3.fill();
        textFieldStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture(pm3)));

        return textFieldStyle;
    }

    private float getOuterTextAreaStartYPosition() {
        return ScreenUtils.isTablet ? TEXT_AREA_MARGIN : TEXT_AREA_MARGIN + iconSize;
    }

    private void addTextArea() {
        if (null == englishTextArea) {
            addIcons();

            float innerTextAreaSize[] = getInnerTextAreaSize();

            englishTextArea = new TextArea(null, getTextAreaTextStyle(Language.ENGLISH));
            //englishTextArea.setSize(innerTextAreaSize[0], innerTextAreaSize[1]);
            englishTextArea.setSize(1,1);
            englishTextArea.setPosition(INNER_TEXT_AREA_MARGIN + TEXT_AREA_MARGIN,
                    getOuterTextAreaStartYPosition() + INNER_TEXT_AREA_MARGIN);

            getStage().addActor(englishTextArea);

            spainTextArea = new TextArea(null, getTextAreaTextStyle(Language.SPANISH));
            //spainTextArea.setSize(innerTextAreaSize[0], innerTextAreaSize[1] / 2 - GAP_BETWEEN_TEXT_AREA * 2);
            spainTextArea.setSize(1,1);
            spainTextArea.setPosition(englishTextArea.getX(), englishTextArea.getY());

            spainTextArea.setVisible(false);

            getStage().addActor(spainTextArea);

            addEnglishTextAreaTips();

            addSpainTextAreaTips();

            addTextAreaListener(englishTextArea, englishTextAreaTips, false);
            addTextAreaListener(spainTextArea, spainTextAreaTips, true);

            if (UserPreferenceUtils.getInstance().isCheatSheetSpanishSelected()) {
                showSpainTextArea();
            }
        }
    }

    private void addTextAreaListener(final TextArea textArea, final Label textAreaTips, final boolean isTranslateToEnglish) {
        textArea.addCaptureListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {

                if (focused) {
                    //hide the tips
                    textAreaTips.setVisible(false);
                    isEnglishSelected = !isTranslateToEnglish;
                } else {
                    //check if the text area has text, if no, show the text
                    textAreaTips.setVisible(StringUtils.isBlank(textArea.getText()));
                }

                super.keyboardFocusChanged(event, actor, focused);
            }
        });

        textArea.addListener(new ActorGestureListener() {

            private DictionaryContainerActor dictionaryContainerActor;

            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

                if (isEnglishSelected && 2 == count && StringUtils.isNotBlank(textArea.getSelection())) {
                    setTouchable(Touchable.disabled);
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    if (null == dictionaryContainerActor) {
                        final float height = getStage().getHeight() - homeIcon.getHeight();
                        dictionaryContainerActor = new DictionaryContainerActor(getWidth(), height,
                                CheatSheetForLifeMessageActor.TEXT_FONT_SIZE,
                                CheatSheetForLifeMessageActor.TITLE_TEXT_FONT_SIZE, new IActionListener() {
                            @Override
                            public void onComplete() {
                                setTouchable(Touchable.enabled);
                                MoveToAction moveToAction = new MoveToAction();
                                moveToAction.setPosition(0, -height);
                                moveToAction.setDuration(1f);
                                dictionaryContainerActor.addAction(moveToAction);
                            }
                        });
                        getStage().addActor(dictionaryContainerActor);
                    }

                    dictionaryContainerActor.setY(-getStage().getHeight());
                    dictionaryContainerActor.setVisible(true);
                    dictionaryContainerActor.toFront();
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setPosition(0, homeIcon.getHeight());
                    moveToAction.setDuration(0.5f);
                    dictionaryContainerActor.addAction(moveToAction);
                    dictionaryContainerActor.setWord(textArea.getSelection());


                }
            }
        });

        textArea.setTextFieldListener(new TextField.TextFieldListener() {

            private TimerService timerService;

            @Override
            public void keyTyped(final TextField textField, char key) {


                //start the timer , until if no key typed again after 0.5s, the translation will be start
                if (null == timerService) {
                    timerService = new TimerService(new TimerService.ITimerListener() {
                        @Override
                        public void beforeStartTimer() {

                        }

                        @Override
                        public void onTimerComplete(Object threadIndicator) {
                            doTranslation(textField.getText(), isTranslateToEnglish, true);
                        }
                    });
                }
                timerService.startTimer(null, 0.5f);

            }
        });
    }

    private void setSpanishText(String text) {
        spainTextArea.toFront();
        if (StringUtils.isNotBlank(text)) {
            spainTextAreaTips.setVisible(false);
            spainTextArea.setText(text);
        } else {
            spainTextAreaTips.setVisible(true);
        }
    }

    private void doTranslation(String text, final boolean isTranslateToEnglish, final boolean isWriteToTextArea) {
        //check if the languageMode is open
        if (isLanguageSelected) {
            if (StringUtils.isNotBlank(text) && isTranslated) {
                isTranslated = false;
                demoListener.onTranslateLanguageSelected(text,
                        isTranslateToEnglish ? Language.SPANISH : Language.ENGLISH,
                        isTranslateToEnglish ? Language.ENGLISH : Language.SPANISH,
                        new IAdvanceActionListener<String>() {

                            @Override
                            public void onComplete(String information) {
                                if (isTranslateToEnglish) {
                                    if (isWriteToTextArea) {
                                        englishTextArea.setText(information);
                                    } else {
                                        recognizedText = information;
                                    }
                                } else {
                                    if (isWriteToTextArea) {
                                        setSpanishText(information);
                                    } else {
                                        spanishRecognizedText = information;
                                    }
                                }

                                isTranslated = true;
                            }
                        });
            } else {
                if (isTranslateToEnglish) {
                    englishTextAreaTips.setVisible(true);
                    englishTextArea.setText(null);
                } else {
                    spainTextAreaTips.setVisible(true);
                    spainTextArea.setText(null);
                }
            }
        }
    }

    private void addEnglishTextAreaTips() {
        if (null == englishTextAreaTips && null != englishTextArea) {
            englishTextAreaTips = new Label("", getTipsLabelStyle());

            englishTextAreaTips.setPosition(englishTextArea.getX(),
                    englishTextArea.getY() + englishTextArea.getHeight() - LetterUtils.getMaximumHeight(textFontSize) * 2);

            englishTextAreaTips.setTouchable(Touchable.disabled);
            getStage().addActor(englishTextAreaTips);
        }
    }

    private void addSpainTextAreaTips() {
        if (null == spainTextAreaTips && null != spainTextArea) {
            spainTextAreaTips = new Label("", getTipsLabelStyle());

            spainTextAreaTips.setPosition(spainTextArea.getX(),
                    spainTextArea.getY() + spainTextArea.getHeight() - LetterUtils.getMaximumHeight(textFontSize) * 2);

            spainTextAreaTips.setTouchable(Touchable.disabled);
            spainTextAreaTips.setVisible(false);
            getStage().addActor(spainTextAreaTips);
        }
    }

    private Label.LabelStyle getTipsLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(textFontSize);
        labelStyle.fontColor = ColorProperties.DISABLE_TEXT;

        return labelStyle;
    }

    public void setText(String text, String translationText, IActionListener onReturnClickListener) {
        showText = text;
        showTranslatedText = translationText;
        this.onReturnClickListener = onReturnClickListener;
    }

    private void showSpainTextArea() {
        spainTextArea.setVisible(true);
        englishTextArea.setHeight(spainTextArea.getHeight());
        englishTextArea.setY(spainTextArea.getY() + spainTextArea.getHeight() + GAP_BETWEEN_TEXT_AREA + INNER_TEXT_AREA_MARGIN * 2);
        spainTextAreaTips.setVisible(true);
        isLanguageSelected = true;
        doTranslation(englishTextArea.getText(), false, true);
    }

    private void hideSpainTextArea() {
        spainTextArea.setVisible(false);
        englishTextArea.setHeight(getInnerTextAreaSize()[1]);
        englishTextArea.setY(spainTextArea.getY());
        spainTextAreaTips.setVisible(false);
    }

    private void addIcons() {
        homeIcon = getIcon("home.png", 0, 0);

        imageDemoScreenAddSubtractBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenAddSubtract.png" : "DemoScreenAddSubtract.png"))));
        imageDemoScreenAlphabetBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenAlphabet.png" : "DemoScreenAlphabet.png"))));
        imageDemoScreenComprehension1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension1.png" : "DemoScreenComprehension1.png"))));
        imageDemoScreenComprehension2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension2.png" : "DemoScreenComprehension2.png"))));
        imageDemoScreenComprehension3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenComprehension3.png" : "DemoScreenComprehension3.png"))));
        imageDemoScreenCountBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenCount.png" : "DemoScreenCount.png"))));
        imageDemoScreenMultiplyBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenMultiply.png" : "DemoScreenMultiply.png"))));
        imageDemoScreenPhonics1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics1.png" : "DemoScreenPhonics1.png"))));
        imageDemoScreenPhonics2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics2.png" : "DemoScreenPhonics2.png"))));
        imageDemoScreenPhonics3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics3.png" : "DemoScreenPhonics3.png"))));
        imageDemoScreenPhonics4Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenPhonics4.png" : "DemoScreenPhonics4.png"))));
        imageDemoScreenWord1Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord1.png" : "DemoScreenWord1.png"))));
        imageDemoScreenWord2Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord2.png" : "DemoScreenWord2.png"))));
        imageDemoScreenWord3Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord3.png" : "DemoScreenWord3.png"))));
        imageDemoScreenWord4Background = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenWord4.png" : "DemoScreenWord4.png"))));
        imageDemoScreenOtherBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenOther.png" : "DemoScreenOther.png"))));
        imageDemoScreenLargeNumberBackground = new Image(new Texture(Gdx.files.internal(Config.IMAGE_FOLDER_NAME + "demo_screen/"+
                (ScreenUtils.isTablet ? "DemoScreenLargeNumber.png" : "DemoScreenLargeNumber.png"))));

        int dHeight = (int) (imageDemoScreenAddSubtractBackground.getHeight())-ScreenUtils.getScreenHeight();
        if (dHeight < 0){
            dHeight = 0;
        }
        dHeight=0;
        imageDemoScreenAddSubtractBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenAlphabetBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenComprehension1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenComprehension2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenComprehension3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenCountBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenMultiplyBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenPhonics1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenPhonics2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenPhonics3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenPhonics4Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenWord1Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenWord2Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenWord3Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenWord4Background.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenOtherBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);
        imageDemoScreenLargeNumberBackground.setSize(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()-dHeight);

        getStage().addActor(imageDemoScreenAddSubtractBackground);
        getStage().addActor(imageDemoScreenAlphabetBackground);
        getStage().addActor(imageDemoScreenComprehension1Background);
        getStage().addActor(imageDemoScreenComprehension2Background);
        getStage().addActor(imageDemoScreenComprehension3Background);
        getStage().addActor(imageDemoScreenCountBackground);
        getStage().addActor(imageDemoScreenMultiplyBackground);
        getStage().addActor(imageDemoScreenPhonics1Background);
        getStage().addActor(imageDemoScreenPhonics2Background);
        getStage().addActor(imageDemoScreenPhonics3Background);
        getStage().addActor(imageDemoScreenPhonics4Background);
        getStage().addActor(imageDemoScreenWord1Background);
        getStage().addActor(imageDemoScreenWord2Background);
        getStage().addActor(imageDemoScreenWord3Background);
        getStage().addActor(imageDemoScreenWord4Background);
        getStage().addActor(imageDemoScreenOtherBackground);
        getStage().addActor(imageDemoScreenLargeNumberBackground);

        setBackgroundVisible(currentScreen);


        Table bottomControlsTable = new Table();

        float width = getWidth();

        if (ScreenUtils.isTablet) {
            width = width / 2;
        }

        bottomControlsTable.setSize(width, iconSize);

        Stack stack = new Stack();
        stack.add(homeIcon);

        bottomControlsTable.add(stack).expand().left();
        stack = new Stack();

        bottomControlsTable.add(stack).expand();
        //bottomControlsTable.add(facebookIcon).expand();

        if (ScreenUtils.isTablet) {
            bottomControlsTable.setX(getStage().getWidth() / 2);
        }

        getStage().addActor(bottomControlsTable);

        addIconListener();
    }

    public void setBackgroundVisible(String pCurrentScreen){
        currentScreen = pCurrentScreen;
        imageDemoScreenAddSubtractBackground.setVisible(false);
        imageDemoScreenAlphabetBackground.setVisible(false);
        imageDemoScreenComprehension1Background.setVisible(false);
        imageDemoScreenComprehension2Background.setVisible(false);
        imageDemoScreenComprehension3Background.setVisible(false);
        imageDemoScreenCountBackground.setVisible(false);
        imageDemoScreenMultiplyBackground.setVisible(false);
        imageDemoScreenPhonics1Background.setVisible(false);
        imageDemoScreenPhonics2Background.setVisible(false);
        imageDemoScreenPhonics3Background.setVisible(false);
        imageDemoScreenPhonics4Background.setVisible(false);
        imageDemoScreenWord1Background.setVisible(false);
        imageDemoScreenWord2Background.setVisible(false);
        imageDemoScreenWord3Background.setVisible(false);
        imageDemoScreenWord4Background.setVisible(false);
        imageDemoScreenOtherBackground.setVisible(false);
        imageDemoScreenLargeNumberBackground.setVisible(false);

        if (currentScreen.equals("DemoScreenAddSubtract")) {
            imageDemoScreenAddSubtractBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenAlphabet")) {
            imageDemoScreenAlphabetBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension1")) {
            imageDemoScreenComprehension1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension2")) {
            imageDemoScreenComprehension2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenComprehension3")) {
            imageDemoScreenComprehension3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenCount")) {
            imageDemoScreenCountBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenMultiply")) {
            imageDemoScreenMultiplyBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics1")) {
            imageDemoScreenPhonics1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics2")) {
            imageDemoScreenPhonics2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics3")) {
            imageDemoScreenPhonics3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenPhonics4")) {
            imageDemoScreenPhonics4Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord1")) {
            imageDemoScreenWord1Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord2")) {
            imageDemoScreenWord2Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord3")) {
            imageDemoScreenWord3Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenWord4")) {
            imageDemoScreenWord4Background.setVisible(true);
        }else if (currentScreen.equals("DemoScreenOther")) {
            imageDemoScreenOtherBackground.setVisible(true);
        }else if (currentScreen.equals("DemoScreenLargeNumber")) {
            imageDemoScreenLargeNumberBackground.setVisible(true);
        }


    }


    private void disableIcon(Image icon) {
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.2f);
        icon.addAction(alphaAction);
        icon.setTouchable(Touchable.disabled);
    }

    private String getShareText() {
        return isEnglishSelected ? englishTextArea.getText() : spainTextArea.getText();
    }


    private void addIconListener() {
        homeIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onHomeClick();
            }
        });


        imageDemoScreenAddSubtractBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });

        imageDemoScreenAlphabetBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenComprehension3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenCountBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenMultiplyBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenPhonics4Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord1Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord2Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord3Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenWord4Background.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenOtherBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
        imageDemoScreenLargeNumberBackground.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                demoListener.onImageBackgroundClick(currentScreen);
            }
        });
    }

    private void addMicrophoneListener(final Image microphoneIcon,
                                       final Image selectedMicrophoneIcon,
                                       final boolean isEnglish) {
        microphoneIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

                //make sure all microphone listener is stopped
                stopMicrophoneListener();

                microphoneIcon.setVisible(false);
                selectedMicrophoneIcon.setVisible(true);
                //start listen
                demoListener.onSpeechToTextStart(new AbstractAdvanceListener<String>() {
                    @Override
                    public void onError() {
                        stopMicrophoneListener();
                    }

                    @Override
                    public void onComplete(String information) {
                        if (isEnglish) {
                            if (StringUtils.isNotBlank(information)) {
                                recognizedText = information;
                            }
                        } else {
                            spainTextArea.setText(information);
                            spainTextAreaTips.setVisible(false);
                        }

                        doTranslation(information, !isEnglish, false);
                    }
                }, isEnglish ? Language.ENGLISH : Language.SPANISH);
            }
        });

        selectedMicrophoneIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                stopMicrophoneListener();
                demoListener.onSpeechToTextStop();
            }
        });
    }

    private void addSpeakListener(final Image icon, final Image selectedIcon, final boolean isEnglish) {

        icon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                stopMicrophoneListener();
                selectedIcon.setVisible(true);
                icon.setVisible(false);
                String textToSpeech = isEnglish ? englishTextArea.getText() : spainTextArea.getText();
                demoListener.onTextToSpeech(textToSpeech, new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        selectedIcon.setVisible(false);
                        icon.setVisible(true);
                    }
                });
            }
        });
    }

    private Image getIcon(String iconImageName, float xPosition, float yPosition) {
        Image image = new Image(AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + iconImageName));
        image.setSize(iconSize, iconSize);
        image.setScaling(Scaling.fill);
        image.setPosition(xPosition, yPosition);
        return image;
    }

}
