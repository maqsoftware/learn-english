package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.ISaySomethingListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.SaySomethingService;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * @author noel ramirez noeleramirez@yahoo.com
 */
public class SayWhatsappScreen extends AbstractAutoCognitaScreen {
    public enum Category {
        TIMES,
        REPLY,
        MEETING,
        CONGRATS,
        FEELINGS
    }

    private Stage stage;
    private CustomCamera camera;

    private ImageButton imageButtonHome, imageButtonWhatsapp, imageButtonTrash,
            imageButtonTime, imageButtonReply, imageButtonMeeting, imageButtonCongrats, imageButtonFeeling;

    private AssetManager assetManager;
    private Skin skin;

    private float gameWidth;
    private float gameHeight;

    private ScrollPane scrollPane;
    private List<String> list;
    private ArrayMap<String, String> sounds;
    private Table buttonsTable;

    private TextArea textAreaMessage;

    private Sound selectionSound;

    private final ISaySomethingListener saySomethingListener = SaySomethingService.getInstance(null);

    private final TextFontSizeEnum textFontSize;

    private static final String UI_SKIN = "ui/uiskin.json";

    private Viewport viewport;

    private String imagesDirectory = "images/whatsapp_messaging/";

    private ArrayList<WhatsappItem> listWA;

    private int[] totalCategories;

    private class WhatsappItem {
        public String category;
        public String name;
        public String soundFile;

        public WhatsappItem(String categoryWA, String nameWA, String soundFileWA) {
            this.category = categoryWA;
            this.name = nameWA;
            this.soundFile = soundFileWA;
        }
    }

    public SayWhatsappScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);

        camera = new CustomCamera();
        camera.setWorldWidth(Gdx.graphics.getWidth());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        textFontSize = TextFontSizeEnum.FONT_48;

        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);
        assetManager.load(UI_SKIN, Skin.class);
        fillListWA();
        fillSounds();
        assetManager.finishLoading();
        skin = assetManager.get(UI_SKIN);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    protected boolean isRequiredToShowHomeButton() {
        return false;
    }

    @Override
    protected java.util.List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    public void show() {
        super.show();

        camera.update();
        if (null == stage) {
            gameWidth = Gdx.graphics.getWidth();
            gameHeight = Gdx.graphics.getHeight();
            viewport = new FitViewport(gameWidth,gameHeight);
            stage = new Stage(viewport);
        }

        iniUI();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void doRender() {
        if (null != stage) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
        }

        StageUtils.dispose(stage);
        stage = null;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

// Non-override methods ////////////////////////////////////////////////////////////////////////////

    private void fillListWA() {
        listWA = new ArrayList<WhatsappItem>();
        totalCategories = new int[]{0,0,0,0,0};
        // Time
        addItem(Category.TIMES, "Be there soon", "betheresoon.m4a");
        addItem(Category.TIMES, "Good afternoon", "goodafternoon.m4a");
        addItem(Category.TIMES, "Good morning", "goodmorning.m4a");
        addItem(Category.TIMES, "Good night", "goodnight.m4a");
        addItem(Category.TIMES, "Hello!", "hello.m4a");
        addItem(Category.TIMES, "Hi there!", "hithere.m4a");
        addItem(Category.TIMES, "Hi!", "hi.m4a");
        addItem(Category.TIMES, "Running late", "runninglate.m4a");
        addItem(Category.TIMES, "See you in 10 minutes", "seeyouin10minutes.m4a");
        addItem(Category.TIMES, "See you in 5 minutes", "seeyouin5minutes.m4a");
        addItem(Category.TIMES, "See you soon", "seeyousoon.m4a");
        addItem(Category.TIMES, "See you then", "seeyouthen.m4a");
        addItem(Category.TIMES, "See you tomorrow", "seeyoutomorrow.m4a");
        addItem(Category.TIMES, "Talk to you soon", "talktoyousoon.m4a");
        addItem(Category.TIMES, "Talk to you tomorrow", "talktoyoutomorrow.m4a");
        // Reply
        addItem(Category.REPLY, "Arrived home","arrivedhome.m4a");
        addItem(Category.REPLY, "Can't talk now","canttalknow.m4a");
        addItem(Category.REPLY, "Gotta go","gottago.m4a");
        addItem(Category.REPLY, "Great!", "great.m4a");
        addItem(Category.REPLY, "Have a great day!", "haveagreatday.m4a");
        addItem(Category.REPLY, "Have a great weekend!", "haveagreatweekend.m4a");
        addItem(Category.REPLY, "Have fun!", "havefun.m4a");
        addItem(Category.REPLY, "I don't know", "idontknow.m4a");
        addItem(Category.REPLY, "Maybe", "maybe.m4a");
        addItem(Category.REPLY, "No problem", "noproblem.m4a");
        addItem(Category.REPLY, "No", "no.m4a");
        addItem(Category.REPLY, "Okay", "okay.m4a");
        addItem(Category.REPLY, "Really!?", "really.m4a");
        addItem(Category.REPLY, "Sure", "sure.m4a");
        addItem(Category.REPLY, "Thank you", "thankyou.m4a");
        addItem(Category.REPLY, "Thanks", "thanks.m4a");
        addItem(Category.REPLY, "Works for me", "worksforme.m4a");
        addItem(Category.REPLY, "Yes", "yes.m4a");
        // Meeting
        addItem(Category.MEETING, "Almost there", "almostthere.m4a");
        addItem(Category.MEETING, "Are you free tomorrow?", "areyoufreetomorrow.m4a");
        addItem(Category.MEETING, "Call me","callme.m4a");
        addItem(Category.MEETING, "Can you pick up the kids?", "canyoupickupthekids.m4a");
        addItem(Category.MEETING, "Meet for coffee?", "meetforcoffee.m4a");
        addItem(Category.MEETING, "Meet for dinner?", "meetfordinner.m4a");
        addItem(Category.MEETING, "Meet for drinks?", "meetfordrinks.m4a");
        addItem(Category.MEETING, "Meet for lunch?", "meetforlunch.m4a");
        addItem(Category.MEETING, "Meet to talk?", "meettotalk.m4a");
        addItem(Category.MEETING, "Talk?", "talk.m4a");
        addItem(Category.MEETING, "Where shall we meet?", "whereshallwemeet.m4a");
        // Congrats
        addItem(Category.CONGRATS, "Congratulations!","congratulations.m4a");
        addItem(Category.CONGRATS, "Fingers crossed!", "fingerscrossed.m4a");
        addItem(Category.CONGRATS, "Good job!", "goodjob.m4a");
        addItem(Category.CONGRATS, "Happy birthday!", "happybirthday.m4a");
        addItem(Category.CONGRATS, "I'm proud of you.", "improudofyou.m4a");
        addItem(Category.CONGRATS, "That's amazing!", "thatsamazing.m4a");
        addItem(Category.CONGRATS, "Way to go!", "waytogo.m4a");
        // Feelings
        addItem(Category.FEELINGS, "Ha ha","haha.m4a");
        addItem(Category.FEELINGS, "Hugs and kisses","hugsandkisses.m4a");
        addItem(Category.FEELINGS, "Hugs","hugs.m4a");
        addItem(Category.FEELINGS, "Just kidding","justkidding.m4a");
        addItem(Category.FEELINGS, "Laugh out loud","laughoutloud.m4a");
        addItem(Category.FEELINGS, "Love you","loveyou.m4a");
        addItem(Category.FEELINGS, "Miss you!","missyou.m4a");
        addItem(Category.FEELINGS, "No way!","noway.m4a");
        addItem(Category.FEELINGS, "Omigod!","omigod.m4a");
        addItem(Category.FEELINGS, "Sorry","sorry.m4a");
        addItem(Category.FEELINGS, "Wow!","wow.m4a");
    }

    private void addItem(Category category, String name, String fileSound) {
        listWA.add(new WhatsappItem(category.toString(), name, fileSound));
        totalCategories[category.ordinal()]++;
    }

    private void iniUI() {

        Table table = new Table();
        table.top();

        Create1stRow(table);         // 1st row with home icon and whatsapp button

        Create2ndRow(table);         // 2nd row with editable text field and trash icon

        Create3rdRow(table);         // 3rd row with buttons Time, Reply, Meeting, Congrats and Feelings

        Create4thRow(table);        // 4th row with list

        table.setFillParent(true);
        table.pack();
        stage.addActor(table);
    }

    private void Create1stRow(Table table) {
        table.row().padBottom(15f).padTop(10f);

        // Home icon
        imageButtonHome = createImageButton(imagesDirectory+"home_icon.png",imagesDirectory+"home_icon.png");
        imageButtonHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onHomeClick();
            }
        });

        table.add(imageButtonHome).width(100f).height(100f).left();

        // whatsapp button
        imageButtonWhatsapp = createImageButton(imagesDirectory+"whatsapp_icon.png", imagesDirectory+"whatsapp_icon.png");
        imageButtonWhatsapp.align(Align.center);
        imageButtonWhatsapp.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                saySomethingListener.onWhatsappClick(getShareText());
            }
        });
        table.add(imageButtonWhatsapp).width(120f).height(100f).center().colspan(3);

        // Trash icon button
        imageButtonTrash = createImageButton(imagesDirectory+"trash_icon_small.png", imagesDirectory+"trash_icon_small.png");
        imageButtonTrash.setBounds(gameWidth-imageButtonTrash.getWidth(),0,imageButtonTrash.getWidth(),imageButtonTrash.getHeight());
        imageButtonTrash.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                textAreaMessage.setText("");
            }
        });
        imageButtonTrash.right().bottom();
        table.add(imageButtonTrash).width(50f).height(57f).right();
    }

    private String getShareText() {
        return textAreaMessage.getText();
    }

    private void Create2ndRow(Table table) {
        table.row().height(100f).padBottom(15f);

        Stack stack = new Stack();
        stack.setColor(Color.WHITE);
        stack.setSize(10,10);

        // Editable area
        textAreaMessage = new TextArea("", getTextAreaTextStyle());
        textAreaMessage.setSize(10,10);
        textAreaMessage.setColor(Color.WHITE);
        textAreaMessage.setAlignment(Align.left);
        textAreaMessage.setPrefRows(10);
        textAreaMessage.setMessageText("Select messages below");
        stack.add(textAreaMessage);

        table.add(stack).width(gameWidth).height(gameHeight/4).colspan(5);
    }

    private TextField.TextFieldStyle getTextAreaTextStyle() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = FontGeneratorManager.getFont(textFontSize);
        textFieldStyle.fontColor = ColorProperties.TEXT;

        Pixmap pm1 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm1.setColor(Color.WHITE);
        pm1.fill();
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(pm1)));

        Pixmap pm2 = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pm2.setColor(Color.BLACK);
        pm2.fill();
        textFieldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(pm2)));

        return textFieldStyle;
    }

    private void Create3rdRow(Table table) {
        table.row();

        // Time button
        imageButtonTime = createImageButton(imagesDirectory+"time_icon.png", imagesDirectory+"time_highlight_icon.png");
        imageButtonTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FillMessages(Category.TIMES);
                FillButtonsTable();
            }
        });
        table.add(imageButtonTime).width(120).uniform();

        // Reply button
        imageButtonReply = createImageButton(imagesDirectory+"reply_icon.png", imagesDirectory+"reply_highlight_icon.png");
        imageButtonReply.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FillMessages(Category.REPLY);
                FillButtonsTable();
            }
        });
        table.add(imageButtonReply).width(120);

        // Meeting button
        imageButtonMeeting = createImageButton(imagesDirectory+"meeting_icon.png", imagesDirectory+"meeting_highlight_icon.png");
        imageButtonMeeting.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FillMessages(Category.MEETING);
                FillButtonsTable();
            }
        });
        table.add(imageButtonMeeting).width(120);

        // Congrats button
        imageButtonCongrats = createImageButton(imagesDirectory+"congrats_icon.png", imagesDirectory+"congrats_highlight_icon.png");
        imageButtonCongrats.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FillMessages(Category.CONGRATS);
                FillButtonsTable();
            }
        });
        table.add(imageButtonCongrats).width(120);

        // Feeling button
        imageButtonFeeling = createImageButton(imagesDirectory+"feelings_icon.png", imagesDirectory+"feelings_highlight_icon.png");
        imageButtonFeeling.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FillMessages(Category.FEELINGS);
                FillButtonsTable();
            }
        });
        imageButtonFeeling.align(Align.right);
        table.add(imageButtonFeeling).width(120);
    }

    private ImageButton createImageButton(String nonHighlightImage, String highlightImage) {
        ImageButton imageButton = new ImageButton(
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal(nonHighlightImage)))),
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(Gdx.files.internal(highlightImage))))
        );
        imageButton.setSize(10,10);
        return imageButton;
    }

    private void FillMessages(Category category) {
        list.clear();
        String[] strings = new String[totalCategories[category.ordinal()]];
        int j = 0;
        for (int i = 0; i < listWA.size() ; i++) {
            if (listWA.get(i).category == category.toString()) {
                strings[j++] = listWA.get(i).name;
            }
        }
        list.setItems(strings);
    }

    private void FillButtonsTable() {
        buttonsTable.clear();
        buttonsTable.setWidth(gameWidth);
        for (String item: list.getItems()) {
            final TextButton button = new TextButton(item.toString(), getTextButtonStyle());
            button.addListener(new ClickListener() {
                public void clicked (InputEvent event, float x, float y) {
                    textAreaMessage.setText(String.format("%s %s ",
                            textAreaMessage.getText(),
                            button.getText()));
                    String fileSound = String.format("sounds/%s",sounds.get(button.getText().toString()));
                    selectionSound = assetManager.get(fileSound, Sound.class);
                    selectionSound.play();
                }
            });
            buttonsTable.add(button).fillX().expand();
            buttonsTable.row();
        }
    }

    private TextButton.TextButtonStyle getTextButtonStyle() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = FontGeneratorManager.getFont(textFontSize);
        textButtonStyle.fontColor = Color.WHITE;

        return textButtonStyle;
    }

    private void Create4thRow(Table table) {
        table.row();
        list = new List<String>(skin);
        buttonsTable = new Table();
        buttonsTable.defaults().left();
        buttonsTable.left();

        scrollPane = new ScrollPane(buttonsTable, skin);
        scrollPane.setBounds(0, 0, gameWidth, gameHeight + 200);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setTransform(true);
        scrollPane.setScale(1f);
        scrollPane.setScrollPercentY(100f);
        table.add(scrollPane).width(gameWidth).left().colspan(5);
    }

    private List.ListStyle getListStyle() {
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = FontGeneratorManager.getFont(textFontSize);
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.fontColorUnselected = Color.YELLOW;
        return listStyle;
    }

    private void fillSounds() {
        sounds = new ArrayMap<String, String>();
        // Time
//        loadSound("Be there soon", "betheresoon.m4a");
//        loadSound("Good afternoon", "goodafternoon.m4a");
//        loadSound("Good morning", "goodmorning.m4a");
//        loadSound("Good night", "goodnight.m4a");
//        loadSound("Hello!", "hello.m4a");
//        loadSound("Hi there!", "hithere.m4a");
//        loadSound("Hi!", "hi.m4a");
//        loadSound("Running late", "runninglate.m4a");
//        loadSound("See you in 10 minutes", "seeyouin10minutes.m4a");
//        loadSound("See you in 5 minutes", "seeyouin5minutes.m4a");
//        loadSound("See you soon", "seeyousoon.m4a");
//        loadSound("See you then", "seeyouthen.m4a");
//        loadSound("See you tomorrow", "seeyoutomorrow.m4a");
//        loadSound("Talk to you soon", "talktoyousoon.m4a");
//        loadSound("Talk to you tomorrow", "talktoyoutomorrow.m4a");
//        // Reply
//        loadSound("Arrived home","arrivedhome.m4a");
//        loadSound("Can't talk now","canttalknow.m4a");
//        loadSound("Gotta go","gottago.m4a");
//        loadSound("Great!", "great.m4a");
//        loadSound("Have a great day!", "haveagreatday.m4a");
//        loadSound("Have a great weekend!", "haveagreatweekend.m4a");
//        loadSound("Have fun!", "havefun.m4a");
//        loadSound("I don't know", "idontknow.m4a");
//        loadSound("Maybe", "maybe.m4a");
//        loadSound("No problem", "noproblem.m4a");
//        loadSound("No", "no.m4a");
//        loadSound("Okay", "okay.m4a");
//        loadSound("Really!?", "really.m4a");
//        loadSound("Sure", "sure.m4a");
//        loadSound("Thank you", "thankyou.m4a");
//        loadSound("Thanks", "thanks.m4a");
//        loadSound("Works for me", "worksforme.m4a");
//        loadSound("Yes", "yes.m4a");
//        // Meeting
//        loadSound("Almost there", "almostthere.m4a");
//        loadSound("Are you free tomorrow?", "areyoufreetomorrow.m4a");
//        loadSound("Call me","callme.m4a");
//        loadSound("Can you pick up the kids?", "canyoupickupthekids.m4a");
//        loadSound("Meet for coffee?", "meetforcoffee.m4a");
//        loadSound("Meet for dinner?", "meetfordinner.m4a");
//        loadSound("Meet for drinks?", "meetfordrinks.m4a");
//        loadSound("Meet for lunch?", "meetforlunch.m4a");
//        loadSound("Meet to talk?", "meettotalk.m4a");
//        loadSound("Talk?", "talk.m4a");
//        loadSound("Where shall we meet?", "whereshallwemeet.m4a");
//        // Congrats
//        loadSound("Congratulations!","congratulations.m4a");
//        loadSound("Fingers crossed!", "fingerscrossed.m4a");
//        loadSound("Good job!", "goodjob.m4a");
//        loadSound("Happy birthday!", "happybirthday.m4a");
//        loadSound("I'm proud of you.", "improudofyou.m4a");
//        loadSound("That's amazing!", "thatsamazing.m4a");
//        loadSound("Way to go!", "waytogo.m4a");
//        // Feelings
//        loadSound("Ha ha","haha.m4a");
//        loadSound("Hugs and kisses","hugsandkisses.m4a");
//        loadSound("Hugs","hugs.m4a");
//        loadSound("Just kidding","justkidding.m4a");
//        loadSound("Laugh out loud","laughoutloud.m4a");
//        loadSound("Love you","loveyou.m4a");
//        loadSound("Miss you!","missyou.m4a");
//        loadSound("No way!","noway.m4a");
//        loadSound("Omigod!","omigod.m4a");
//        loadSound("Sorry","sorry.m4a");
//        loadSound("Wow!","wow.m4a");
        for (WhatsappItem item: listWA) {
            loadSound(item.name, item.soundFile);
        }
    }

    private void loadSound(String label, String soundName) {
        sounds.put(label, soundName);
        assetManager.load("sounds/"+soundName, Sound.class);
    }

}
