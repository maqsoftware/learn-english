package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.AbstractLearningGame;
import com.maqautocognita.Config;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class HomeMenuMapScreen extends ProgressMapScreen implements TimerService.ITimerListener {

    public static final float DEFAULT_UNIT_ALPHA = 0.3f;
    private static final boolean IS_SWAHILI_ENABLED = false;
    private static final int MENU_ICON_PADDING = 10;
    private TimerService timerService2;

    /**
     * it is mainly for the menu table, for the space between each row
     */
    private static final int MENU_ROW_SPACE_BOTTOM = ScreenUtils.isTablet ? 20 : 10;

    private static final IconPosition SW_ICON_POSITION = new IconPosition(0, 100, 100, 100);
    private static final Vector2 LANGUAGE_SCREEN_POSITION = new Vector2(Config.TABLET_SCREEN_WIDTH - ARROW_ICON_SIZE - SW_ICON_POSITION.width, Config.TABLET_SCREEN_HEIGHT - TOP_MENU_MARGIN_TOP - SW_ICON_POSITION.height);
    private static final IconPosition EN_ICON_POSITION = new IconPosition(100, 100, 100, 100);
    private static final int MENU_ICON_SIZE = 150;
    private static final int SUB_MENU_ICON_SIZE_IN_PICTURE = 100;
    public static final IconPosition ALPHABET_ICON_POSITION = new IconPosition(0, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition PHONICS_LEVEL1_ICON_POSITION = new IconPosition(125, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition PHONICS_LEVEL2_ICON_POSITION = new IconPosition(250, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition PHONICS_LEVEL3_ICON_POSITION = new IconPosition(375, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition PHONICS_LEVEL4_ICON_POSITION = new IconPosition(500, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition SENTENCE_LEVEL1_ICON_POSITION= new IconPosition(625, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition SENTENCE_LEVEL2_ICON_POSITION= new IconPosition(0, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition SENTENCE_LEVEL3_ICON_POSITION= new IconPosition(125, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition SENTENCE_LEVEL4_ICON_POSITION= new IconPosition(250, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition COMPREHENSION_LEVEL1_ICON_POSITION= new IconPosition(375, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition COMPREHENSION_LEVEL2_ICON_POSITION= new IconPosition(500, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition COMPREHENSION_LEVEL3_ICON_POSITION= new IconPosition(625, 125, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition COUNT_ICON_POSITION = new IconPosition(0, 250, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition ADDITION_SUBTRACT_ICON_POSITION = new IconPosition(125, 250, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition PLACE_VALUE_ICON_POSITION = new IconPosition(250, 250, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition MULTIPLY_ICON_POSITION = new IconPosition(375, 250, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition MATH_OTHER_TOPICS_ICON_POSITION = new IconPosition(500, 250, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    public static final IconPosition DEMO_ICON_POSTION = new IconPosition(0, 0, SUB_MENU_ICON_SIZE_IN_PICTURE, SUB_MENU_ICON_SIZE_IN_PICTURE);
    private static final TextFontSizeEnum MENU_TEXT_FONT_SIZE = TextFontSizeEnum.FONT_48;
    private static final TextFontSizeEnum MENU_TITLE_FONT_SIZE = TextFontSizeEnum.FONT_72;
    private static final float LOGO_ICON_SIZE_RATIO = 0.8f;

    private final int textureTimes;
    private List<ImageActor<ModuleId>> screenObjectList;
    private List<TextCell<ModuleId>> menuTextScreenObjectList;
    private ImageActor englishIcon;
    private ImageActor swahiliIcon;
    private Image logoIcon;
    private Image demoIcon;
    private ImageActor Actor;

    private RoundCornerRectangleScreenObject readingMenuBorder;
    private RoundCornerRectangleScreenObject mathMenuBorder;
    private RoundCornerRectangleScreenObject lifeMenuBorder;
    private float originalLineHeight;

    private ImageActor demoActor;
    public HomeMenuMapScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener, AssetManagerUtils.LANGUAGE_MENU_ICONS);
        textureTimes = 2;
    }

    @Override
    public void show() {
        super.show();

        if (ScreenUtils.isTablet && IS_SWAHILI_ENABLED) {

            if (null == englishIcon) {
                englishIcon = new ImageActor(
                        AssetManagerUtils.LANGUAGE_MENU_ICONS, EN_ICON_POSITION,
                        LANGUAGE_SCREEN_POSITION.x, LANGUAGE_SCREEN_POSITION.y);

                stage.addActor(englishIcon);
            }

            if (null == swahiliIcon) {
                swahiliIcon = new ImageActor(
                        AssetManagerUtils.LANGUAGE_MENU_ICONS, SW_ICON_POSITION,
                        LANGUAGE_SCREEN_POSITION.x, LANGUAGE_SCREEN_POSITION.y);

                stage.addActor(swahiliIcon);
            }
        }

        initScreenObject();
    }

    @Override
    public void hide() {
        super.hide();

        clearScreenObject();

        if (null != swahiliIcon) {
            swahiliIcon.dispose();
            swahiliIcon = null;
        }

        if (null != englishIcon) {
            englishIcon.dispose();
            englishIcon = null;
        }

        if (null != logoIcon) {
            logoIcon.remove();
            logoIcon = null;
        }

        if (null != readingMenuBorder) {
            readingMenuBorder.dispose();
            readingMenuBorder = null;
        }

        if (null != mathMenuBorder) {
            mathMenuBorder.dispose();
            mathMenuBorder = null;
        }

        if (null != lifeMenuBorder) {
            lifeMenuBorder.dispose();
            lifeMenuBorder = null;
        }

        if (originalLineHeight > 0) {
            FontGeneratorManager.getFont(MENU_TEXT_FONT_SIZE).getData().setLineHeight(originalLineHeight);
        }

        AssetManagerUtils.unloadAllTexture();

    }

    private void clearScreenObject() {
        if (null != screenObjectList) {
            for (ImageActor actor : screenObjectList) {
                actor.dispose();
            }

            if (CollectionUtils.isNotEmpty(menuTextScreenObjectList)) {
                for (Actor actor : menuTextScreenObjectList) {
                    actor.remove();
                }

                menuTextScreenObjectList.clear();
            }

            screenObjectList.clear();

            screenObjectList = null;

            menuTextScreenObjectList = null;
        }

    }

    private void initScreenObject() {
        clearScreenObject();

        initLogoIcon(ScreenUtils.isTablet ? LOGO_ICON_SIZE_RATIO : 0.6f);

        if (ScreenUtils.isTablet) {

            MobileMenuGroup menuGroup = new MobileMenuGroup();
            menuGroup.setSize(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

            float menuTableLeftRightMargin = 100;
            float menuTableStartYPosition = 50;


            int tablePadding = 20;

            int titlePaddingBottom = 20;

            Table readingMenuTable = getReadingMenu(menuTableLeftRightMargin, menuTableStartYPosition,
                    (ScreenUtils.getScreenWidth() - menuTableLeftRightMargin * 3) * 3 / 5,
                    ScreenUtils.getScreenHeight() - menuTableStartYPosition - menuTableLeftRightMargin
            );

            menuGroup.addActor(readingMenuTable);

            readingMenuBorder = getMenuBorder(readingMenuTable, tablePadding);
            Label menuTitle = new Label("ए बी सी", getMenuTitleStyle());
            menuTitle.setPosition(readingMenuTable.getX(), readingMenuBorder.yPositionInScreen + readingMenuBorder.height - titlePaddingBottom);
            menuGroup.addActor(menuTitle);


            Table mathLifeMenuTable = getMathLifeMenu(readingMenuTable.getX() + readingMenuTable.getWidth() + menuTableLeftRightMargin,
                    readingMenuTable.getY(),
                    ScreenUtils.getScreenWidth() - readingMenuBorder.xPositionInScreen - readingMenuBorder.width - menuTableLeftRightMargin * 2,
                    readingMenuTable.getHeight()
            );

            mathMenuBorder = getMenuBorder(mathLifeMenuTable, tablePadding);

            float titleYPosition = mathMenuBorder.yPositionInScreen + mathMenuBorder.height - titlePaddingBottom;
            menuTitle = new Label("१२३", getMenuTitleStyle());
            menuTitle.setPosition(mathLifeMenuTable.getX(), titleYPosition);
            menuGroup.addActor(menuTitle);

            menuTitle = new Label("Life", getMenuTitleStyle());
            menuTitle.setPosition(mathLifeMenuTable.getX() + mathLifeMenuTable.getWidth() - menuTitle.getWidth(), titleYPosition);
            menuGroup.addActor(menuTitle);

            menuGroup.addActor(mathLifeMenuTable);

            stage.addActor(menuGroup);

        } else {

            logoIcon.setY(ScreenUtils.getScreenHeight() - logoIcon.getHeight() * logoIcon.getScaleY());

            MobileMenuGroup menuGroup = new MobileMenuGroup();

            float menuTableLeftRightMargin = 50;

            int tablePadding = 20;

            int titlePaddingBottom = 20;

            float screenMenuHeight = logoIcon.getY() - titlePaddingBottom;

            float readingMenuTableHeight = screenMenuHeight * 3 / 5;
            float mathMenuTableHeight = readingMenuTableHeight * 3 / 5;


            Label menuTitle = new Label("ए बी सी", getMenuTitleStyle());

            float totalHeight = readingMenuTableHeight + mathMenuTableHeight + menuTitle.getHeight() * 3 + menuTableLeftRightMargin;

            Table readingMenuTable = getReadingMenu(menuTableLeftRightMargin, totalHeight - readingMenuTableHeight - menuTitle.getHeight(),
                    ScreenUtils.getScreenWidth() - menuTableLeftRightMargin * 2, readingMenuTableHeight);

            menuGroup.addActor(readingMenuTable);

            readingMenuBorder =
                    getMenuBorder(readingMenuTable, tablePadding);


            menuTitle.setPosition(readingMenuTable.getX(), readingMenuBorder.yPositionInScreen + readingMenuBorder.height - titlePaddingBottom);
            menuGroup.addActor(menuTitle);


            Table mathMenuTable = getMathMenu(readingMenuTable.getX(),
                    readingMenuTable.getY() - menuTitle.getHeight() - mathMenuTableHeight,
                    readingMenuTable.getWidth(),
                    mathMenuTableHeight);

            menuGroup.addActor(mathMenuTable);
            mathMenuBorder = getMenuBorder(mathMenuTable, tablePadding);
            float titleYPosition = mathMenuBorder.yPositionInScreen + mathMenuBorder.height - titlePaddingBottom;


            menuTitle = new Label("१२३", getMenuTitleStyle());
            menuTitle.setPosition(mathMenuTable.getX(), titleYPosition);
            menuGroup.addActor(menuTitle);


            menuGroup.setSize(ScreenUtils.getScreenWidth(), totalHeight);
            menuGroup.setPosition(0, 0);
            ScrollPane scrollPane = new ScrollPane(menuGroup);
            scrollPane.setSize(ScreenUtils.getScreenWidth(), logoIcon.getY());
            scrollPane.setPosition(0, 0);

            stage.addActor(scrollPane);

        }


        if (null != englishIcon) {
            englishIcon.toFront();
        }

        if (null != swahiliIcon) {
            swahiliIcon.toFront();
        }

    }

    private Image getPersonalIcon(RoundCornerRectangleScreenObject mathMenuBorder, float padding) {
        Image personIcon = new Image(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.ICON_PERSON));
        personIcon.setPosition(mathMenuBorder.xPositionInScreen +
                        mathMenuBorder.width - personIcon.getWidth() - padding,
                mathMenuBorder.yPositionInScreen + mathMenuBorder.height - personIcon.getHeight() - padding);

        return personIcon;

    }

    private RoundCornerRectangleScreenObject getMenuBorder(Table menu, int padding) {
        return new RoundCornerRectangleScreenObject(menu.getX() - padding, menu.getY(),
                (int) menu.getWidth() + padding * 2,
                (int) menu.getHeight(), ColorProperties.DISABLE_TEXT, 10, true);
    }

    private void initLogoIcon(float logoIconScale) {
        if (null == logoIcon) {
            logoIcon = new Image(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.ICON_LOGO));
            logoIcon.setScaling(Scaling.fit);
            logoIcon.setAlign(Align.center);
        }

        logoIcon.setScale(logoIconScale);
        logoIcon.setX(ScreenUtils.getXPositionForCenterObject(logoIcon.getWidth() * logoIconScale));

    }

    private void addEmptyMenuIcon(Table table) {
        ImageActor emptyActor = new ImageActor(MENU_ICON_SIZE, MENU_ICON_SIZE);
        addMenuIcon(table, emptyActor, 1, Align.bottom);
    }

    private Table getReadingMenu(float x, float y, float width, float height) {

        Table menuTable = new Table();
        menuTable.setWidth(width);
        if (height > 0) {
            menuTable.setHeight(height);
        }
        menuTable.setPosition(x, y);

        int menuTextAlignment = Align.center | Align.left;

       menuTable.add(addMenuText("वर्णमाला", "alphabet")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.ALPHABET, AssetManagerUtils.getMenuIcon(), ALPHABET_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK1, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK2, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK3, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);

        menuTable.add(addMenuText("नाद"+"िवद"+"्या", "phonics")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.PHONICU1, AssetManagerUtils.getMenuIcon(), PHONICS_LEVEL1_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.PHONICU2, AssetManagerUtils.getMenuIcon(), PHONICS_LEVEL2_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.PHONICU3, AssetManagerUtils.getMenuIcon(), PHONICS_LEVEL3_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.PHONICU4, AssetManagerUtils.getMenuIcon(), PHONICS_LEVEL4_ICON_POSITION), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);


        menuTable.add(addMenuText("शब्द", "word")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_1, AssetManagerUtils.getMenuIcon(), SENTENCE_LEVEL1_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_2, AssetManagerUtils.getMenuIcon(), SENTENCE_LEVEL2_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_3, AssetManagerUtils.getMenuIcon(), SENTENCE_LEVEL3_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_4, AssetManagerUtils.getMenuIcon(), SENTENCE_LEVEL4_ICON_POSITION), 1, Align.bottom);
        /*
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_2, AssetManagerUtils.ICON_SENTENCE_2), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_3, AssetManagerUtils.ICON_SENTENCE_3), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SENTENCE_4, AssetManagerUtils.ICON_SENTENCE_4), 1, Align.bottom);
        */
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);

        menuTable.add(addMenuText("समझ", "comprehension")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_1, AssetManagerUtils.getMenuIcon(), COMPREHENSION_LEVEL1_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_2, AssetManagerUtils.getMenuIcon(), COMPREHENSION_LEVEL2_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_3, AssetManagerUtils.getMenuIcon(), COMPREHENSION_LEVEL3_ICON_POSITION), 1, Align.bottom);
        /*
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_1, AssetManagerUtils.ICON_COMPREHENSION_1), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_2, AssetManagerUtils.ICON_COMPREHENSION_2), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.COMPREHENSION_3, AssetManagerUtils.ICON_COMPREHENSION_3), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK4, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        */
        if (AbstractLearningGame.deviceService.isStoryModeEnable()) {
            menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);

            menuTable.add(addMenuText("story", "story")).align(menuTextAlignment);
            addMenuIcon(menuTable, getIconImageActor(ModuleId.STORY_MODE, AssetManagerUtils.ICON_STORY_1), 1, Align.bottom);
            addMenuIcon(menuTable, getIconImageActor(ModuleId.STORY_MISSION, AssetManagerUtils.ICON_STORY_2), 1, Align.bottom);
        }

        return menuTable;
    }

    private Table getMathLifeMenu(float x, float y, float width, float height) {

        Table menuTable = new Table();
        menuTable.setWidth(width);
        menuTable.setHeight(height);

        menuTable.setPosition(x, y);

        int menuTextAlignment = Align.center | Align.left;
        menuTable.add(addMenuText("िगनती", "counting")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_COUNT, AssetManagerUtils.getMenuIcon(), COUNT_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK5, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK6, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK7, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);

        menuTable.add(addMenuText("जोड़ना/\n" + "घटाना", "add")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_ADDITION_SUBSTRACTION, AssetManagerUtils.getMenuIcon(), ADDITION_SUBTRACT_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_PLACE_VALUE, AssetManagerUtils.getMenuIcon(), PLACE_VALUE_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK8, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK9, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);

        menuTable.add(addMenuText("गुणा /\n" + "अन्य", "multiply")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_MULTIPLY, AssetManagerUtils.getMenuIcon(), MULTIPLY_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_OTHER_TOPICS, AssetManagerUtils.getMenuIcon(), MATH_OTHER_TOPICS_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK10, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK11, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);

        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);
        return menuTable;
    }

    private Label addMenuText(String text, final String audioFileName) {
        final Label label = new Label(text, getMenuTextStyle());
        label.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

                label.getStyle().fontColor = ColorProperties.HIGHLIGHT;

                super.tap(event, x, y, count, button);
                playSound(audioFileName, new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        label.getStyle().fontColor = ColorProperties.TEXT;
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        label.getStyle().fontColor = ColorProperties.TEXT;
                    }
                });
            }
        });
        return label;
    }

    protected String getAudioPath() {
        return (AbstractLearningGame.deviceService.isSpanishLocale() ? "spanish" : "english") + File.separator + "menu";
    }

    private Table getMathMenu(float x, float y, float width, float height) {

        Table menuTable = new Table();
        menuTable.setWidth(width);
        menuTable.setHeight(height);

        menuTable.setPosition(x, y);

        int menuTextAlignment = Align.center | Align.left;

        menuTable.add(addMenuText("िगनती", "counting")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_COUNT, AssetManagerUtils.getMenuIcon(), COUNT_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK5, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK6, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK7, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);


       menuTable.add(addMenuText("जोड़ना/\n" + "घटाना", "add")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_ADDITION_SUBSTRACTION, AssetManagerUtils.getMenuIcon(), ADDITION_SUBTRACT_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_PLACE_VALUE, AssetManagerUtils.getMenuIcon(), PLACE_VALUE_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK8, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK9, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        menuTable.row().spaceTop(MENU_ROW_SPACE_BOTTOM);


        menuTable.add(addMenuText("गुणा /\n" + "अन्य", "multiply")).align(menuTextAlignment);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_MULTIPLY, AssetManagerUtils.getMenuIcon(), MULTIPLY_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.MATH_OTHER_TOPICS, AssetManagerUtils.getMenuIcon(), MATH_OTHER_TOPICS_ICON_POSITION), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK10, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK11, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);
        return menuTable;
    }

    private Table getLifeMenu(float x, float y, float width, float height, float firstColumnWidth) {

        Table menuTable = new Table();
        menuTable.setWidth(width);
        menuTable.setHeight(height);

        menuTable.setPosition(x, y);

        addEmptyMenuText(menuTable);

        addMenuIcon(menuTable, getIconImageActor(ModuleId.LIFE_SKILL, AssetManagerUtils.ICON_INFO), 1, Align.bottom);

///        addMenuIcon(menuTable, getIconImageActor(ModuleId.SNAP, AssetManagerUtils.ICON_SNAP), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.CONVERSATION, AssetManagerUtils.ICON_TALK), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.SAY_SOMETHING, AssetManagerUtils.ICON_SHARE), 1, Align.bottom);
        addMenuIcon(menuTable, getIconImageActor(ModuleId.BLANK12, AssetManagerUtils.ICON_DEMO), 1, Align.bottom);

        menuTable.row();

        addEmptyMenuText(menuTable).width(firstColumnWidth);

//        menuTable.add(addMenuText("info", "info"));
        menuTable.add(addMenuText("जानकारी", "info"));
//        menuTable.add(addMenuText("snap", "snap"));
//        menuTable.add(addMenuText("talk", "talk"));
        menuTable.add(addMenuText("बातचीत", "talk"));
//        menuTable.add(addMenuText("share", "share"));
        menuTable.add(addMenuText("शेयर", "share"));
        return menuTable;
    }

    private Label.LabelStyle getMenuTextStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(MENU_TEXT_FONT_SIZE);
        labelStyle.fontColor = ColorProperties.TEXT;

        if (originalLineHeight == 0) {
            originalLineHeight = labelStyle.font.getData().lineHeight;
        }
        labelStyle.font.getData().setLineHeight(labelStyle.font.getCapHeight() + 10);

        return labelStyle;
    }

    private Label.LabelStyle getMenuTitleStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(MENU_TITLE_FONT_SIZE);
        labelStyle.fontColor = ColorProperties.DISABLE_TEXT;

        return labelStyle;
    }

    private ImageActor getIconImageActor(ModuleId moduleId, String imagePath) {
        return getIconImageActor(moduleId, imagePath, null);
    }

    private ImageActor getIconImageActor(ModuleId moduleId, String imagePath, IconPosition iconPosition) {
        ImageActor imageActor = null;
        //System.out.println(imagePath);
        if (null == iconPosition) {
            imageActor = new ImageActor(moduleId, imagePath);
        } else {
            //System.out.println("IconPosition");
            //System.out.println(iconPosition);
            imageActor = new ImageActor(moduleId, imagePath, iconPosition, textureTimes);
        }
        imageActor.setSize(MENU_ICON_SIZE, MENU_ICON_SIZE);
        return imageActor;
    }

    private Cell addMenuIcon(Table table, ImageActor imageActor, int colspan, int align) {
        return addMenuIcon(table, imageActor, colspan, align, MENU_ICON_PADDING);
    }

    private Cell addMenuIcon(Table table, final ImageActor<ModuleId> imageActor, int colspan, int align, int padding) {

        if (null == screenObjectList) {
            screenObjectList = new ArrayList<ImageActor<ModuleId>>();
        }

        if (null != imageActor) {
            screenObjectList.add(imageActor);
            imageActor.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    onModuleSelected(imageActor.getId());
                }
            });
        }

        return table.add(imageActor).pad(padding).colspan(colspan).expandX().align(align);
    }

    private Cell addEmptyMenuText(Table table) {
        return addMenuText(table, null, null, null, null);
    }

    private Cell addMenuText(Table table, ModuleId moduleId, String menuText, TextFontSizeEnum textFontSizeEnum, Color textColor) {
        return addMenuText(table, moduleId, menuText, textFontSizeEnum, textColor, Align.top | Align.center, true, 10, 0);
    }

    private Cell addMenuText(Table table, final ModuleId moduleId, String menuText, TextFontSizeEnum textFontSizeEnum, Color textColor,
                             int align, boolean isTextFlip, int paddingTop, float cellWidth) {
        TextCell textCell = new TextCell(moduleId, menuText, textFontSizeEnum, cellWidth, isTextFlip);
        textCell.setWrap(true);
        textCell.adjustNarrowLineHeight();
        if (null != textColor) {
            textCell.setTextColor(textColor);
        }
        addMenuTextScreenObject(textCell);

        if (StringUtils.isNotBlank(menuText)) {
            textCell.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    //onModuleSelected(moduleId);
                    onModuleSelected(moduleId.Demo);
                }
            });
        }

        Cell cell = table.add(textCell).height(textCell.getHeight()).padTop(paddingTop).expandX().align(align);
        textCell.setWidth(cell.getMaxWidth());
        return cell;
    }

    private void addMenuTextScreenObject(TextCell textCell) {
        if (null == menuTextScreenObjectList) {
            menuTextScreenObjectList = new ArrayList<TextCell<ModuleId>>();
        }

        textCell.setAlpha(1f);
        menuTextScreenObjectList.add(textCell);
    }

    private void onModuleSelected(ModuleId moduleId) {
        if (null != moduleId) {
            switch (moduleId) {

                case MATH_COUNT:
                    menuScreenListener.onMathCountSelected();
                    break;
                case ALPHABET:
                    menuScreenListener.onAlphabetSelected();
                    break;
                case PHONICU1:
                    menuScreenListener.onPhonicU1Selected();
                    break;
                case PHONICU2:
                    menuScreenListener.onPhonicU2Selected();
                    break;
                case PHONICU3:
                    menuScreenListener.onPhonicU3Selected();
                    break;
                case PHONICU4:
                    menuScreenListener.onPhonicU4Selected();
                    break;
                case LIFE_SKILL:
                    menuScreenListener.onLifeSkillSelected();
                    break;
                case SNAP:
                    menuScreenListener.onSnapSelected();
                    //menuScreenListener.onDemoSelected2();
                    break;
                case MATH_ADDITION_SUBSTRACTION:
                    menuScreenListener.onMathAdditionSubtractionSelected();
                    break;
                case MATH_PLACE_VALUE:
                    menuScreenListener.onMathPlaceValueSelected();
                    break;
                case MATH_OTHER_TOPICS:
                    menuScreenListener.onMathOtherTopicSelected();
                    break;
                case MATH_MULTIPLY:
                    menuScreenListener.onMathMultiplySelected();
                    break;
                case STORY_MODE:
                    menuScreenListener.onStoryModeSelected();
                    break;
                case STORY_MISSION:
                    menuScreenListener.onStoryMissionSelected();
                    break;
                case LIBRARY_SCENE:
                    menuScreenListener.onLibraryStoryModeSelected();
                    break;
                case SCHOOL_SCENE:
                    menuScreenListener.onSchoolStoryModeSelected();
                    break;
                case HOME_SCENE:
                    menuScreenListener.onHomeStoryModeSelected();
                    break;
                case VILLAGE_SCENE:
                    menuScreenListener.onVillageStoryModeSelected();
                    break;
                case MARKET_SCENE:
                    menuScreenListener.onMarketStoryModeSelected();
                    break;
                case CAFE_SCENE:
                    menuScreenListener.onCafeStoryModeSelected();
                    break;
                case STORE_SCENE:
                    menuScreenListener.onStoreStoryModeSelected();
                    break;
                case CITY_OR_COUNTRY_SCENE:
                    menuScreenListener.onCityStoryModeSelected();
                    break;
                case SAY_SOMETHING:
                    menuScreenListener.onSaySomethingSelected();
                    break;
                case SENTENCE_1:
                    menuScreenListener.onSentenceUnit1Selected();
                    break;
                case SENTENCE_2:
                    if (UserPreferenceUtils.getInstance().isEnglish())
                        menuScreenListener.onSentenceUnit2Selected();
                    break;
                case SENTENCE_3:
                    if (UserPreferenceUtils.getInstance().isEnglish())
                        menuScreenListener.onSentenceUnit3Selected();
                    break;
                case SENTENCE_4:
                    if (UserPreferenceUtils.getInstance().isEnglish())
                        menuScreenListener.onSentenceUnit4Selected();
                    break;
                case SENTENCE_5:
                    menuScreenListener.onSentenceUnit5Selected();
                    break;
                case COMPREHENSION_1:
                    menuScreenListener.onReadingComprehensionUnit1Selected();
                    break;
                case COMPREHENSION_2:
                    menuScreenListener.onReadingComprehensionUnit2Selected();
                    break;
                case COMPREHENSION_3:
                    menuScreenListener.onReadingComprehensionUnit3Selected();
                    break;
                case CONVERSATION:
                    menuScreenListener.onConversationSelected();
                    break;
            }
        }
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {

        buttonTouch();

    }

    private void buttonTouch(){
        InputEvent event2 = new InputEvent();
        event2.setType(InputEvent.Type.touchDown);
        //demoActor.fire(event2);
        screenObjectList.get(15).fire(event2);
    }

    private void addLogo(){
        if (null != logoIcon) {
            logoIcon.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    onModuleSelected(ModuleId.Demo);
                }
            });
        }
    }

    private enum ModuleId {
        EN_LANGUAGE, KS_LANGUAGE,
        MATH_COUNT, ALPHABET, PHONICU1, PHONICU2, PHONICU3, PHONICU4, TOPIC, MATH_ADDITION_SUBSTRACTION,
        MATH_PLACE_VALUE, MATH_MULTIPLY, MATH_OTHER_TOPICS, STORY_MISSION, STORY_MODE, LIFE_SKILL, SNAP, SAY_SOMETHING, LOGO, Demo,
        HOME_SCENE, VILLAGE_SCENE, LIBRARY_SCENE, SCHOOL_SCENE, MARKET_SCENE, CAFE_SCENE, STORE_SCENE, CITY_OR_COUNTRY_SCENE,
        SENTENCE_1, SENTENCE_2, SENTENCE_3, SENTENCE_4, SENTENCE_5, COMPREHENSION_1, COMPREHENSION_2, COMPREHENSION_3, CONVERSATION,
        BLANK1, BLANK2, BLANK3, BLANK4, BLANK5, BLANK6, BLANK7, BLANK8, BLANK9, BLANK10, BLANK11, BLANK12
    }

    private class MobileMenuGroup extends Group {
        @Override
        public void drawChildren(Batch batch, float parentAlpha) {
            ScreenObjectUtils.draw(batch, readingMenuBorder);
            ScreenObjectUtils.draw(batch, mathMenuBorder);
            ScreenObjectUtils.draw(batch, lifeMenuBorder);
            super.drawChildren(batch, parentAlpha);
            super.drawChildren(batch, parentAlpha);
        }
    }
}
