package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.ICheatSheetForLifeMessageListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.utils.LoadingIconUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CheatSheetForLifeMessageActor extends WidgetGroup {

    private static final TextFontSizeEnum LARGE_TEXT_FONT_SIZE = TextFontSizeEnum.FONT_84;
    public static final TextFontSizeEnum TEXT_FONT_SIZE = LARGE_TEXT_FONT_SIZE;
    private static final TextFontSizeEnum TITLE_LARGE_TEXT_FONT_SIZE = TextFontSizeEnum.FONT_108;
    public static final TextFontSizeEnum TITLE_TEXT_FONT_SIZE = TITLE_LARGE_TEXT_FONT_SIZE;
    /**
     * The padding of the border in the top bottom side, the space between the text and the border in top and bottom side
     */
    private final float textPadding;

    private final float lineSpace;
    private final ICheatSheetForLifeMessageListener cheatSheetForLifeMessageListener;
    private SaySomethingMessageActor saySomethingMessageActor;
    private String text;
    private String translationText;
    private BitmapFont bitmapFont;
    private ShapeRenderer shapeRenderer;
    private Table iconTable;
    private Table loadingTable;
    private float textHeight;

    private boolean textHighlighted;
    private boolean translationTextHighlighted;

    private boolean isPictureRecognitionSelected = true;

    private CheatSheetImageActor cheatSheetImageActor;

    private Image homeIcon, returnIcon, languageIcon, selectedLanguageIcon, cameraIcon, imagesIcon, askIcon,
            pictureRecognitionIcon, textRecognitionIcon, pictureRecognitionSelectedIcon, textRecognitionSelectedIcon, takePictureIcon;

    private Map<Rectangle, String> textAreaMap;
    private DictionaryContainerActor dictionaryContainerActor;

    public CheatSheetForLifeMessageActor(final ICheatSheetForLifeMessageListener cheatSheetForLifeMessageListener) {
        this.cheatSheetForLifeMessageListener = cheatSheetForLifeMessageListener;
        initIcons();
//        if (ScreenUtils.isSmallResolution()) {
//            textPadding = 15;
//            lineSpace = 30;
//        } else {
            textPadding = 30;
            lineSpace = 60;
//        }
    }

    private void initIcons() {
        homeIcon = initIcon("home.png", Align.left);

        homeIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != cheatSheetForLifeMessageListener) {
                    cheatSheetForLifeMessageListener.onHomeClick();
                }
            }
        });

        returnIcon = initIcon("return.png", Align.left);
        returnIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != cheatSheetForLifeMessageListener) {
                    cheatSheetForLifeMessageListener.onReturnToPreviousScreen();
                }
            }
        });

        languageIcon = initIcon("language.png", Align.center);
        languageIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                languageIcon.setVisible(false);
                selectedLanguageIcon.setVisible(true);
                UserPreferenceUtils.getInstance().setCheatSheetSelectedLanguage(Language.SPANISH.toString());
                doTranslation(null);
            }
        });

        selectedLanguageIcon = initIcon("language_selected.png", Align.center);
        selectedLanguageIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                languageIcon.setVisible(true);
                selectedLanguageIcon.setVisible(false);
                UserPreferenceUtils.getInstance().setCheatSheetSelectedLanguage(Language.ENGLISH.toString());
                doTranslation(null);
            }
        });

        if (UserPreferenceUtils.getInstance().isCheatSheetEnglishSelected()) {
            selectedLanguageIcon.setVisible(false);
        } else {
            languageIcon.setVisible(false);
        }

        cameraIcon = initIcon("camera.png", Align.right);
        imagesIcon = initIcon("images.png", Align.right);
        askIcon = initIcon("ask.png", Align.right);

        pictureRecognitionIcon = initIcon("picture_recognition.png", Align.left);
        pictureRecognitionIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                isPictureRecognitionSelected = true;
                changeToRecognitionMode();
                cheatSheetForLifeMessageListener.onVisualRecognitionSelected();
            }
        });


        textRecognitionIcon = initIcon("text_recognition.png", Align.left);
        textRecognitionIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                isPictureRecognitionSelected = false;
                changeToRecognitionMode();
                cheatSheetForLifeMessageListener.onTextRecognitionSelected();
            }
        });

        pictureRecognitionSelectedIcon = initIcon("picture_recognition_selected.png", Align.left);
        textRecognitionSelectedIcon = initIcon("text_recognition_selected.png", Align.left);

        takePictureIcon = initIcon("take_picture.png", ScreenUtils.isTablet ? Align.right : Align.center);
        //if (ScreenUtils.isSmallResolution()) {
            takePictureIcon.setScale(0.5f);
        //}
        takePictureIcon.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != cheatSheetForLifeMessageListener) {
                    cheatSheetForLifeMessageListener.onTakePictureClick();
                }
            }
        });

    }

    private Image initIcon(String iconImageName, int alignment) {
        Image icon = new Image(new Texture(Config.COMMON_IMAGE_XDPI_PATH + iconImageName));
        icon.setScaling(Scaling.fit);
        icon.setAlign(alignment);
        return icon;
    }

    private void doTranslation(final IActionListener translationCompleteListener) {

        if (StringUtils.isNotBlank(text) && StringUtils.isBlank(translationText)) {
            showLoadingIcon();
            cheatSheetForLifeMessageListener.onTranslateLanguageSelected(text, Language.ENGLISH,
                    Language.SPANISH, new IAdvanceActionListener<String>() {

                        @Override
                        public void onComplete(String translatedText) {
                            translationText = translatedText;
                            hideLoadingIcon();
                            if (null != translationCompleteListener) {
                                translationCompleteListener.onComplete();
                            }
                        }
                    });
        } else {
            if (null != translationCompleteListener) {
                translationCompleteListener.onComplete();
            }
        }

    }

    /**
     * it is mainly called when the user enter visual recognition  mode
     * Show the images in the right bottom
     * It will auto hide the camera icon in the bottom right corner
     */
    public void changeToRecognitionMode() {

        clearText();

        if (null != imagesIcon) {
            imagesIcon.setVisible(true);
        }
        if (null != cameraIcon) {
            cameraIcon.setVisible(false);
        }

        pictureRecognitionIcon.setVisible(!isPictureRecognitionSelected);
        pictureRecognitionSelectedIcon.setVisible(isPictureRecognitionSelected);
        textRecognitionIcon.setVisible(isPictureRecognitionSelected);
        textRecognitionSelectedIcon.setVisible(!isPictureRecognitionSelected);
        takePictureIcon.setVisible(true);
        returnIcon.setVisible(false);
        askIcon.setVisible(false);
        homeIcon.setVisible(true);
    }

    public void showLoadingIcon() {
        if (null != cheatSheetForLifeMessageListener) {
            cheatSheetForLifeMessageListener.onLoading();
        }
        if (null == loadingTable) {
            loadingTable = LoadingIconUtils.getLoadingIcon();
            loadingTable.setSize(getWidth(), getHeight() / 4);
            loadingTable.setY(getHeight() - loadingTable.getHeight());
            loadingTable.center();
            loadingTable.toFront();
            addActor(loadingTable);
        }

        loadingTable.setVisible(true);
    }

    public void hideLoadingIcon() {
        if (null != loadingTable) {
            loadingTable.setVisible(false);
        }

        if (null != cheatSheetForLifeMessageListener) {
            cheatSheetForLifeMessageListener.afterLoading();
        }
    }

    public void clearText() {
        this.text = null;
        this.translationText = null;
        clearTextAreaMap();
    }

    private void clearTextAreaMap() {
        if (null != textAreaMap) {
            textAreaMap.clear();
            textAreaMap = null;
        }
    }


    public void addTextTapListener() {
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != textAreaMap) {
                    for (Rectangle rectangle : textAreaMap.keySet()) {
                        if (TouchUtils.isTouched(rectangle, (int) x, (int) y)) {
                            showDictionary(textAreaMap.get(rectangle));
                            break;
                        }
                    }
                }
            }
        });
    }

    private void showDictionary(String word) {
        if (null != cheatSheetImageActor) {
            cheatSheetImageActor.setTouchable(Touchable.disabled);
        }
        if (null == dictionaryContainerActor) {
            final float height = getStage().getHeight() - homeIcon.getHeight();
            dictionaryContainerActor = new DictionaryContainerActor(getWidth(), height,
                    TEXT_FONT_SIZE, TITLE_TEXT_FONT_SIZE, new IActionListener() {
                @Override
                public void onComplete() {
                    if (null != cheatSheetImageActor) {
                        cheatSheetImageActor.setTouchable(Touchable.enabled);
                    }
                    MoveToAction moveToAction = new MoveToAction();
                    moveToAction.setY(-height);
                    moveToAction.setDuration(1f);
                    dictionaryContainerActor.addAction(moveToAction);
                }
            });
            addActor(dictionaryContainerActor);
        }

        dictionaryContainerActor.setY(-getHeight());
        dictionaryContainerActor.setVisible(true);
        dictionaryContainerActor.toFront();
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setY(homeIcon.getHeight());
        moveToAction.setDuration(0.5f);
        dictionaryContainerActor.addAction(moveToAction);
        dictionaryContainerActor.setWord(word);

    }

    public void setTextWithTranslation(final String text, final String translationText, String textAudioFilePath) {
        this.translationText = translationText;
        setTextWithoutSpeak(text);
        setTextHighlighted(true);
        cheatSheetForLifeMessageListener.onPlayAudioPath(textAudioFilePath, new AbstractSoundPlayListener() {
            @Override
            public void onComplete() {
                super.onComplete();
                setTextHighlighted(false);
            }

            @Override
            public void onStop() {
                super.onComplete();
                setTextHighlighted(false);
            }
        });
    }

    private void setTextWithoutSpeak(String text) {
        setTextHighlighted(false);
        setTranslationTextHighlighted(false);
        clearTextAreaMap();
        this.text = text;
    }

    private void setTextHighlighted(boolean textHighlighted) {
        this.textHighlighted = textHighlighted;
    }

    private void setTranslationTextHighlighted(boolean belowTextHighlighted) {
        this.translationTextHighlighted = belowTextHighlighted;
    }

    public void setTextAndRemoveTranslation(final String text) {
        this.translationText = null;
        setText(text);
    }

    public void setText(final String text) {
        if (StringUtils.isNotBlank(text)) {
            setTextWithoutSpeak(text);
            speakText();
            doTranslation(null);
        }
    }

    private void speakText() {
        speakText(null);
    }

    private void speakText(final IActionListener actionListener) {
        if (null != cheatSheetForLifeMessageListener) {
            setTextHighlighted(true);
            cheatSheetForLifeMessageListener.onTextToSpeech(text, new AbstractSoundPlayListener() {
                @Override
                public void onComplete() {
                    super.onComplete();
                    setTextHighlighted(false);
                    if (null != actionListener) {
                        actionListener.onComplete();
                    }
                }
            });
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (isVisible()) {
            addIcons();
            batch.end();

            if (null == shapeRenderer) {
                shapeRenderer = new ShapeRenderer();
            }

            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
            shapeRenderer.setColor(ColorProperties.LIFE_SKILL_BOTTOM_BACKGROUND);
            shapeRenderer.end();

            if (StringUtils.isNotBlank(text) || StringUtils.isNotBlank(translationText)) {

                batch.begin();
                if (null == bitmapFont) {
                    bitmapFont = FontGeneratorManager.getFont(TEXT_FONT_SIZE);
                }

                if (StringUtils.isNotBlank(text)) {

                    bitmapFont.setColor(textHighlighted ? ColorProperties.HIGHLIGHT : Color.WHITE);
                    float startXPosition = textPadding;
                    final float startYPosition = getHeight() - textPadding;
                    boolean isMapInit = true;
                    if (null == textAreaMap) {
                        textAreaMap = new HashMap<Rectangle, String>();
                        isMapInit = false;
                    }
                    for (String drawText : text.split(" ")) {
                        GlyphLayout textLayout = drawText(batch, drawText + " ", startXPosition, startYPosition);
                        if (!isMapInit) {
                            textAreaMap.put(
                                    new Rectangle(startXPosition - textPadding,
                                            startYPosition - textLayout.height - textPadding,
                                            textLayout.width + 1.5f * textPadding, textLayout.height + 2 * textPadding), drawText);
                        }
                        textHeight = textLayout.height;
                        startXPosition += textLayout.width;
                    }

                }

                if (UserPreferenceUtils.getInstance().isCheatSheetSpanishSelected() && StringUtils.isNotBlank(translationText)) {
                    bitmapFont.setColor(translationTextHighlighted ? ColorProperties.HIGHLIGHT : ColorProperties.BORDER);
                    drawText(batch, translationText, getHeight() - textPadding - lineSpace
                            - textHeight);
                }


                batch.end();
            }
            batch.begin();
        }

        super.draw(batch, parentAlpha);

    }

    private void addIcons() {
        if (null == iconTable) {
            iconTable = new Table();

            iconTable.setSize(getWidth(), getHeight());

            Stack stack = new Stack();
            stack.add(pictureRecognitionIcon);
            stack.add(pictureRecognitionSelectedIcon);
            if (ScreenUtils.isTablet) {
                iconTable.add(new Actor());
                iconTable.add(stack).expand().center().top();
            } else {
                iconTable.add(stack).expand().left();
            }

            stack = new Stack();
            stack.add(textRecognitionIcon);
            stack.add(textRecognitionSelectedIcon);

            if (ScreenUtils.isTablet) {
                iconTable.add(stack).expand().right().top();
            } else {
                iconTable.row();
                iconTable.add(stack).expand().left();
            }
            iconTable.row();

            stack = new Stack();
            stack.add(homeIcon);
            stack.add(returnIcon);

            iconTable.add(stack).expand().left().bottom();


            stack = new Stack();
            stack.add(languageIcon);
            stack.add(selectedLanguageIcon);
            iconTable.add(stack).expand().bottom();

            stack = new Stack();
            stack.add(cameraIcon);
            stack.add(imagesIcon);
            stack.add(askIcon);

            iconTable.add(stack).expand().right().bottom();

            cameraIcon.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (null != cheatSheetForLifeMessageListener) {
                        cheatSheetForLifeMessageListener.onCameraClick();
                    }
                }
            });

            imagesIcon.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (null != cheatSheetForLifeMessageListener) {
                        cheatSheetForLifeMessageListener.onLifeSkillBuiltInContentClick();
                    }
                }
            });

            askIcon.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (null == saySomethingMessageActor) {
                        saySomethingMessageActor = new SaySomethingMessageActor();
                        getStage().addActor(saySomethingMessageActor);
                        saySomethingMessageActor.setSize(getStage().getWidth(), getStage().getHeight());
                        saySomethingMessageActor.setText(text, translationText, new IActionListener() {
                            @Override
                            public void onComplete() {
                                setVisible(true);
                                saySomethingMessageActor.remove();
                                cheatSheetForLifeMessageListener.onReturnToPreviousScreen();
                                saySomethingMessageActor = null;
                            }
                        });
                    }
                    setVisible(false);
                    saySomethingMessageActor.toFront();
                    saySomethingMessageActor.setVisible(true);

                }
            });

            addActor(iconTable);

            float xPosition = 0;
            if (ScreenUtils.isTablet) {
                xPosition = getWidth() - takePictureIcon.getWidth() - 30;
            } else {
                xPosition = ScreenUtils.getXPositionForCenterObject(takePictureIcon.getWidth() * takePictureIcon.getScaleX(), getWidth());
            }
            takePictureIcon.setPosition(xPosition,
                    ScreenUtils.getBottomYPositionForCenterObject(takePictureIcon.getHeight() * takePictureIcon.getScaleY(), getHeight()) + 30);
            addActor(takePictureIcon);
        }
    }

    private GlyphLayout drawText(Batch batch, String text, float xPosition, float yPosition) {
        return bitmapFont.draw(batch, text, getX() + xPosition, yPosition);
    }

    private float drawText(Batch batch, String text, float yPosition) {
        GlyphLayout layout = bitmapFont.draw(batch, text, getX() + textPadding, yPosition, getWidth() - textPadding * 2, Align.left, true);
        return layout.height;
    }

    /**
     * It is called when the user enter the life skill built-in content mode
     * Show the camera in the right bottom
     */
    public void changeToLeftSkillBuiltInContentMode(CheatSheetImageActor cheatSheetImageActor) {
        this.cheatSheetImageActor = cheatSheetImageActor;
        if (null != cameraIcon) {
            cameraIcon.setVisible(true);
        }
        if (null != imagesIcon) {
            imagesIcon.setVisible(false);
        }

        hideAllRecognitionIcon();
        returnIcon.setVisible(false);
        homeIcon.setVisible(true);
        askIcon.setVisible(false);
    }

    private void hideAllRecognitionIcon() {
        pictureRecognitionIcon.setVisible(false);
        pictureRecognitionSelectedIcon.setVisible(false);
        textRecognitionIcon.setVisible(false);
        textRecognitionSelectedIcon.setVisible(false);
        takePictureIcon.setVisible(false);
    }

    public void showReturnIcon() {
        hideAllRecognitionIcon();
        homeIcon.setVisible(false);
        returnIcon.setVisible(true);
    }

    public void showAskIcon() {
        askIcon.setVisible(true);
        imagesIcon.setVisible(false);
        cameraIcon.setVisible(false);
    }

}
