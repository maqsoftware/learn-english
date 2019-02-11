package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.Conversation;
import com.maqautocognita.bo.ConversationLesson;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.IScrollPaneListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.scene2d.ui.PagedScrollPane;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.ConversationService;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siu-chun.chi on 7/3/2017.
 */

public class ConversationScreen extends AbstractAutoCognitaScreen implements IScrollPaneListener<ConversationLesson> {


    private static final Color BACKGROUND_COLOR = Color.valueOf("424242");
    private static final Color TITLE_BOTTOM_LINE_COLOR = Color.valueOf("979797");

    private static final float CONTROL_BUTTON_FLASH_DURATION_SECOND = 1f;

    private static final float SPEAKER_FLASH_DURATION_SECOND = 0.5f;

    private static final int ARROW_MARGIN = 40;

    private static final int SPACE_BETWEEN_SPEECH_AND_SPEAKER = 20;

    private static final int SPACE_BETWEEN_CONTROL_BUTTONS = 100;

    private static final int CONVERSATION_LABEL_PAD_LEFT = 20;

    private static final int SEPARATOR_LINE_HEIGHT = 2;

    private static final Color TITLE_COLOR = Color.valueOf("18FFFF");
    private static final TextFontSizeEnum TITLE_FONT_SIZE = TextFontSizeEnum.FONT_72;
    private static final TextFontSizeEnum CONVERSATION_TEXT_FONT_SIZE = TextFontSizeEnum.FONT_48;

    private static final float CONVERSATION_BOX_HEIGHT_SCALE = 1.5f;
    private CustomCamera camera;
    private Stage stage;

    private Label titleLabel;
    private Label leftSpeakerLabel;
    private Label rightSpeakerLabel;

    private SpeakerGroup leftSpeakerImage;
    private SpeakerGroup rightSpeakerImage;

    private ControlGroup playButton;
    private Image stopButton;
    private ControlGroup recordButton;

    private PagedScrollPane<ConversationLesson> pagedScrollPane;

    private List<ScrollPane> conversationTableList;

    private Map<Integer, List<LabelGroup>> lessonIndexConversationLabelListMap;

    private List<ConversationLesson> conversationLessonList;

    private Map<Integer, List<String>> lessonIndexRecordedAudioFileNameListMap;

    private int currentConversationLessonIndex;
    private int startConversationIndex;

    private State currentState;

    private State previousStateBeforePause;

    private boolean isLeftSpeakerSelected;
    private boolean firstTimeRender = true;

    public ConversationScreen(AbstractGame game, IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

        conversationLessonList = ConversationService.getInstance().getAllLesson();

    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {

    }

    @Override
    protected List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    protected String getAudioPath() {
        return "english" + File.separator + "conversation";
    }

    @Override
    public void show() {

        camera.update();
        if (null == stage) {
            ScreenViewport screenViewport = new ScreenViewport(camera);
            screenViewport.setUnitsPerPixel(ScreenUtils.widthRatio);
            stage = new Stage(screenViewport);
        }

        ConversationRowWithBottomLine titleRow = new ConversationRowWithBottomLine();

        titleLabel = new Label("", getTitleLabelStyle());
        titleLabel.setAlignment(Align.center);
        titleLabel.setTouchable(Touchable.disabled);
        titleRow.addActor(titleLabel);

        titleRow.setSize(ScreenUtils.getScreenWidth(), titleLabel.getHeight());

        ConversationRow speakerRow = new ConversationRow();
        leftSpeakerImage = new SpeakerGroup(true);
        leftSpeakerImage.setX(ARROW_MARGIN);

        rightSpeakerImage = new SpeakerGroup(false);
        rightSpeakerImage.setX(ScreenUtils.getScreenWidth() - ARROW_MARGIN -
                rightSpeakerImage.getWidth());

        leftSpeakerLabel = new Label("", getSpeakerLabelStyle());
        leftSpeakerLabel.setHeight(FontGeneratorManager.getFont(TITLE_FONT_SIZE).getLineHeight());
        leftSpeakerLabel.setAlignment(Align.center);
        // leftSpeakerLabel.setPosition(leftSpeakerImage.getX() + leftSpeakerImage.getWidth() + SPACE_BETWEEN_SPEECH_AND_SPEAKER, 0);
        rightSpeakerLabel = new Label("", getSpeakerLabelStyle());
        //rightSpeakerLabel.setPosition(rightSpeakerImage.getX() - rightSpeakerLabel.getWidth() - SPACE_BETWEEN_SPEECH_AND_SPEAKER, 0);
        rightSpeakerLabel.setHeight(FontGeneratorManager.getFont(TITLE_FONT_SIZE).getLineHeight());

        leftSpeakerImage.setY(ScreenUtils.getBottomYPositionForCenterObject(leftSpeakerImage.getHeight(),
                leftSpeakerLabel.getHeight()));

        rightSpeakerImage.setY(ScreenUtils.getBottomYPositionForCenterObject(rightSpeakerImage.getHeight(),
                rightSpeakerLabel.getHeight()));

        leftSpeakerImage.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onSpeakerPressed(true);
            }
        });
        leftSpeakerLabel.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onSpeakerPressed(true);
            }
        });

        rightSpeakerImage.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onSpeakerPressed(false);
            }
        });
        rightSpeakerLabel.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onSpeakerPressed(false);
            }
        });


        speakerRow.addActor(leftSpeakerImage);
        speakerRow.addActor(rightSpeakerImage);
        speakerRow.addActor(leftSpeakerLabel);
        speakerRow.addActor(rightSpeakerLabel);
        speakerRow.setSize(ScreenUtils.getScreenWidth(), leftSpeakerLabel.getHeight() + 20);


        titleRow.setPosition(0, ScreenUtils.getScreenHeight() - titleRow.getHeight());
        speakerRow.setPosition(0, titleRow.getY() - speakerRow.getHeight());

        stage.addActor(titleRow);
        stage.addActor(speakerRow);


        ConversationRowWithTopLine controlButtonRow = new ConversationRowWithTopLine();
        playButton = new ControlGroup("play.png", "play_highlight.png");
        playButton.setPosition(ScreenUtils.getXPositionForCenterObject(playButton.getWidth()), ARROW_MARGIN);
        recordButton = new ControlGroup("record.png", "record_highlight.png");
        recordButton.setPosition(playButton.getX() - SPACE_BETWEEN_CONTROL_BUTTONS - recordButton.getWidth(), ARROW_MARGIN);
        stopButton = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "pause.png"));
        stopButton.setPosition(playButton.getX() + playButton.getWidth() + SPACE_BETWEEN_CONTROL_BUTTONS,
                ARROW_MARGIN + ScreenUtils.getBottomYPositionForCenterObject(stopButton.getHeight(), playButton.getHeight()));


        Image homeButton = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "home.png"));
        homeButton.setPosition(ARROW_MARGIN,
                ARROW_MARGIN + ScreenUtils.getBottomYPositionForCenterObject(homeButton.getHeight(), playButton.getHeight()));
        homeButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                menuScreenListener.onHomeSelected();
            }
        });

        stopButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onPauseButtonPressed();
            }
        });
        playButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onPlayButtonPressed();
            }
        });
        recordButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                onRecordButtonPressed();
            }
        });


        controlButtonRow.addActor(recordButton);
        controlButtonRow.addActor(playButton);
        controlButtonRow.addActor(stopButton);
        controlButtonRow.addActor(homeButton);

        controlButtonRow.setSize(ScreenUtils.getScreenWidth(), recordButton.getHeight() + ARROW_MARGIN * 2);

        stage.addActor(controlButtonRow);

        pagedScrollPane = new PagedScrollPane(this);
        pagedScrollPane.setSize(ScreenUtils.getScreenWidth(), speakerRow.getY() - controlButtonRow.getHeight());

        pagedScrollPane.setY(controlButtonRow.getHeight());
        int i = 0;
        for (ConversationLesson conversationLesson : conversationLessonList) {
            ScrollPane conversationTableScrollPane = new ScrollPane(getConversationTable(conversationLesson, i));

            conversationTableScrollPane.getStyle().background = getBackgroundDrawable();

            conversationTableScrollPane.setScrollingDisabled(true, false);
            conversationTableScrollPane.setSize(ScreenUtils.getScreenWidth(), speakerRow.getY() - controlButtonRow.getHeight());

            if (null == conversationTableList) {
                conversationTableList = new ArrayList<ScrollPane>();
            }
            conversationTableList.add(conversationTableScrollPane);

            pagedScrollPane.addPage(conversationTableScrollPane, conversationLesson);


            i++;
        }

        pagedScrollPane.getStyle().background = getBackgroundDrawable();

        stage.addActor(pagedScrollPane);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor())

        {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        reloadConversationTitleAndSpeaker(conversationLessonList.get(currentConversationLessonIndex));

        changeToPlayState();

        startConversation();
    }

    @Override
    public void doRender() {
        if (null != stage) {

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();

            if (firstTimeRender) {
                pagedScrollPane.scrollToPage(currentConversationLessonIndex);
                firstTimeRender = false;
            }
        }
    }

    @Override
    public void hide() {
        super.hide();

        firstTimeRender = true;

        AbstractGame.audioService.stopMusic();
        AbstractGame.audioService.stopRecord();

        titleLabel = null;
        leftSpeakerLabel = null;
        rightSpeakerLabel = null;

        leftSpeakerImage = null;
        rightSpeakerImage = null;

        if (null != lessonIndexConversationLabelListMap) {
            lessonIndexConversationLabelListMap.clear();
            lessonIndexConversationLabelListMap = null;
        }

        lessonIndexConversationLabelListMap = null;

        //startConversationIndex = 0;

        stopButton = null;
        playButton = null;
        recordButton = null;

        StageUtils.dispose(stage);
        stage = null;

        changeToPauseState();
        //currentConversationLessonIndex = 0;

        if (null != conversationTableList) {
            conversationTableList.clear();
            conversationTableList = null;
        }

        if (null != lessonIndexRecordedAudioFileNameListMap) {
            lessonIndexRecordedAudioFileNameListMap.clear();
            lessonIndexRecordedAudioFileNameListMap = null;
        }

        pagedScrollPane = null;
    }

    private void changeToPauseState() {

        previousStateBeforePause = currentState;

        currentState = State.PAUSE;
        if (null != recordButton) {
            recordButton.setHighlight(false);
        }
        if (null != playButton) {
            playButton.setHighlight(false);
        }
    }

    private Drawable getBackgroundDrawable() {
        Pixmap background = new Pixmap(1, 1, Pixmap.Format.RGB565);
        background.setColor(BACKGROUND_COLOR);
        background.fill();

        return new TextureRegionDrawable(new TextureRegion(new Texture(background)));
    }

    private void reloadConversationTitleAndSpeaker(ConversationLesson conversationLesson) {

        titleLabel.setText(conversationLesson.topic);

        List<String> speakerNameList = getSpeakerName(conversationLesson);
        leftSpeakerLabel.setText(speakerNameList.get(0));
        leftSpeakerLabel.setWidth(leftSpeakerLabel.getPrefWidth());
        leftSpeakerLabel.setX(leftSpeakerImage.getX() + leftSpeakerImage.getWidth() + SPACE_BETWEEN_SPEECH_AND_SPEAKER);

        rightSpeakerLabel.setText(speakerNameList.get(1));
        rightSpeakerLabel.setWidth(rightSpeakerLabel.getPrefWidth());
        rightSpeakerLabel.setX(rightSpeakerImage.getX() - rightSpeakerLabel.getWidth() - SPACE_BETWEEN_SPEECH_AND_SPEAKER);
    }

    private void onSpeakerPressed(boolean isLeft) {
        if (State.SELECT_SPEAKER.equals(currentState)) {

            clearCurrentLessonRecordedAudios();

            changeToRecordingState();

            leftSpeakerImage.setHighlight(isLeft);
            rightSpeakerImage.setHighlight(!isLeft);

            isLeftSpeakerSelected = isLeft;

            startConversationIndex = 0;
            startConversation();
        }
    }

    private void onPauseButtonPressed() {
        if (!State.PAUSE.equals(currentState)) {
            changeToPauseState();
            AbstractGame.audioService.stopRecord();
            AbstractGame.audioService.stopMusic();
        }
    }

    private void onPlayButtonPressed() {
        if (!State.PLAY.equals(currentState)) {

            clearCurrentLessonRecordedAudios();

            changeToPlayState();

            if (!currentState.equals(previousStateBeforePause)) {
                startConversationIndex = 0;
            }

            startConversation();
        }

    }

    private void onRecordButtonPressed() {
        if (State.RECORDING.equals(previousStateBeforePause)) {
            clearCurrentLessonRecordedAudios();
            changeToRecordingState();
            startConversation();
        } else if (!State.RECORDING.equals(currentState) || !State.SELECT_SPEAKER.equals(currentState)) {
            changeToSelectSpeakerState();
            flashSpeaker();
        }
    }

    private void flashSpeaker() {
        if (State.SELECT_SPEAKER.equals(currentState)) {
            flashLeftSpeakerImage();
            flashLeftSpeakerLabel();
        }
    }

    private void flashRightSpeakerImage() {
        if (State.SELECT_SPEAKER.equals(currentState)) {
            AnimationUtils.doFlash(rightSpeakerImage, false, SPEAKER_FLASH_DURATION_SECOND, 1, new Runnable() {
                @Override
                public void run() {
                    flashLeftSpeakerImage();
                }
            });
        }
    }

    private void flashRightSpeakerLabel() {
        if (State.SELECT_SPEAKER.equals(currentState)) {
            AnimationUtils.doFlash(rightSpeakerLabel, false, SPEAKER_FLASH_DURATION_SECOND, 1, new Runnable() {
                @Override
                public void run() {
                    flashLeftSpeakerLabel();
                }
            });
        }
    }

    private void flashLeftSpeakerImage() {
        if (State.SELECT_SPEAKER.equals(currentState)) {
            AnimationUtils.doFlash(leftSpeakerImage, false, SPEAKER_FLASH_DURATION_SECOND, 1, new Runnable() {
                @Override
                public void run() {
                    flashRightSpeakerImage();
                }
            });
        }
    }

    private void flashLeftSpeakerLabel() {
        if (State.SELECT_SPEAKER.equals(currentState)) {
            AnimationUtils.doFlash(leftSpeakerLabel, false, SPEAKER_FLASH_DURATION_SECOND, 1, new Runnable() {
                @Override
                public void run() {
                    flashRightSpeakerLabel();
                }
            });
        }
    }

    private void clearCurrentLessonRecordedAudios() {
        if (null != lessonIndexRecordedAudioFileNameListMap) {
            lessonIndexRecordedAudioFileNameListMap.remove(currentConversationLessonIndex);
        }
    }

    /**
     * on lesson changed
     *
     * @param conversationLesson
     * @param paneIndex
     */
    @Override
    public void onPaneChanged(ConversationLesson conversationLesson, int paneIndex) {

        if (paneIndex != currentConversationLessonIndex) {
            if (State.RECORDING.equals(currentState)) {
                clearCurrentLessonRecordedAudios();
            }

            changeToPauseState();
            currentConversationLessonIndex = paneIndex;
            reloadConversationTitleAndSpeaker(conversationLesson);
            startConversationIndex = 0;
            changeToPlayState();
            startConversation();
        }
    }

    private Table getConversationTable(ConversationLesson conversationLesson, int lessonIndex) {

        //add conversation table
        Table table = new Table();
        table.setWidth(ScreenUtils.getScreenWidth());

        table.setBackground(getBackgroundDrawable());

        table.top();

        List<String> speakerNameList = getSpeakerName(conversationLesson);

        if (null == lessonIndexConversationLabelListMap) {
            lessonIndexConversationLabelListMap = new HashMap<Integer, List<LabelGroup>>();
        }

        List<LabelGroup> labelGroupList = new ArrayList<LabelGroup>(conversationLesson.conversationList.size());

        for (Conversation conversation : conversationLesson.conversationList) {

            if (conversation.speaker.equals(speakerNameList.get(0))) {
                LabelGroup leftLabel = new LabelGroup(conversation,
                        true);
                table.add(leftLabel).size(getLeftSpeakerConversationBox().getWidth(),
                        getLeftSpeakerConversationBox().getHeight() * CONVERSATION_BOX_HEIGHT_SCALE).left().expandX().pad(ARROW_MARGIN).row();
                labelGroupList.add(leftLabel);
            } else {
                LabelGroup rightLabel = new LabelGroup(conversation, false);
                table.add(rightLabel).size(getRightSpeakerConversationBox().getWidth(),
                        getRightSpeakerConversationBox().getHeight() * CONVERSATION_BOX_HEIGHT_SCALE).right().expandX().pad(ARROW_MARGIN).row();
                labelGroupList.add(rightLabel);
            }
        }

        lessonIndexConversationLabelListMap.put(lessonIndex, labelGroupList);

        return table;
    }

    private List<String> getSpeakerName(ConversationLesson conversationLesson) {
        List<String> speakerNameList = new ArrayList<String>(2);
        for (Conversation conversation : conversationLesson.conversationList) {
            if (!speakerNameList.contains(conversation.speaker)) {
                speakerNameList.add(conversation.speaker);
            }
        }

        return speakerNameList;
    }

    private List<LabelGroup> getCurrentTableLabelGroupList() {
        if (null != lessonIndexConversationLabelListMap) {
            return lessonIndexConversationLabelListMap.get(currentConversationLessonIndex);
        }

        return null;
    }

    private void startConversation() {

        if ((State.PLAY.equals(currentState) || State.RECORDING.equals(currentState))
                && CollectionUtils.isNotEmpty(getCurrentTableLabelGroupList()) &&
                startConversationIndex < getCurrentTableLabelGroupList().size()) {
            final LabelGroup label = getCurrentTableLabelGroupList().get(startConversationIndex);
            label.setHighLight(true);

            if (State.PLAY.equals(currentState)) {
                leftSpeakerImage.setHighlight(false);
                rightSpeakerImage.setHighlight(false);
            } else {
                leftSpeakerImage.setHighlight(isLeftSpeakerSelected);
                rightSpeakerImage.setHighlight(!isLeftSpeakerSelected);
            }

            if (null != conversationTableList) {
                if (0 != label.getY()) {
                    ScrollPane scrollPane = conversationTableList.get(currentConversationLessonIndex);
                    scrollPane
                            .setScrollY(scrollPane.getMaxY() - label.getY());
                }
            }

            final State state = currentState;

            if ((State.RECORDING.equals(currentState)) &&
                    isLeftSpeakerSelected == label.isLeft) {

                label.flashHighLightImage();

                final String recordAudioFileName = label.getConversation().audio;

                new TimerService(new TimerService.ITimerListener() {
                    @Override
                    public void beforeStartTimer() {
                        if (State.RECORDING.equals(state)) {
                            AbstractGame.audioService.startRecord(null, recordAudioFileName);
                        }
                    }

                    @Override
                    public void onTimerComplete(Object threadIndicator) {
                        if (state.equals(currentState) && State.RECORDING.equals(state)) {
                            AbstractGame.audioService.stopRecord();

                            if (null == lessonIndexRecordedAudioFileNameListMap) {
                                lessonIndexRecordedAudioFileNameListMap = new HashMap<Integer, List<String>>();
                            }

                            if (!lessonIndexRecordedAudioFileNameListMap.containsKey(currentConversationLessonIndex)) {
                                lessonIndexRecordedAudioFileNameListMap.put(currentConversationLessonIndex, new ArrayList<String>());
                            }

                            if (!lessonIndexRecordedAudioFileNameListMap.get(currentConversationLessonIndex).contains(recordAudioFileName)) {
                                lessonIndexRecordedAudioFileNameListMap.get(currentConversationLessonIndex).add(recordAudioFileName);
                            }
                        } else {
                            Gdx.app.log(getClass().getName(), "on practice mode finish audio");
                        }

                        whenFinishAudioPlaying(label, state);
                    }
                }).startTimer(null, label.getConversation().time);
            } else {
                playConversation(label, state);
            }

        }
    }

    private boolean isRecordAudioFileNameExist(String audioFileName) {
        if (null != lessonIndexRecordedAudioFileNameListMap && null != lessonIndexRecordedAudioFileNameListMap.get(currentConversationLessonIndex)) {
            if (lessonIndexRecordedAudioFileNameListMap.get(currentConversationLessonIndex).contains(audioFileName)) {
                return true;
            }
        }
        return false;
    }

    private void playConversation(final LabelGroup label, final State state) {

        ISoundPlayListener soundPlayListener = new AbstractSoundPlayListener() {
            @Override
            public void onComplete() {
                super.onComplete();
                whenFinishAudioPlaying(label, state);
            }

            @Override
            public void onStop() {
                super.onStop();
                whenFinishAudioPlaying(label, state);
            }
        };

        playAudio(label.getConversation(), soundPlayListener);


    }

    private void playAudio(Conversation conversation, ISoundPlayListener soundPlayListener) {
        if (isRecordAudioFileNameExist(conversation.audio)) {
            Gdx.app.log(getClass().getName(), "playing recorded audio");
            AbstractGame.audioService.playBackRecord(conversation.audio, soundPlayListener);
        } else {
            Gdx.app.log(getClass().getName(), "playing standard audio");
            playSound(conversation.audio, soundPlayListener);

        }
    }

    private void whenFinishAudioPlaying(final LabelGroup label, final State state) {

        if (null != label) {
            label.setHighLight(false);
        }

        if (State.PLAY.equals(state)) {
            if (null != leftSpeakerImage) {
                leftSpeakerImage.setHighlight(false);
            }
            if (null != rightSpeakerImage) {
                rightSpeakerImage.setHighlight(false);
            }
        }

        List<LabelGroup> labelGroupList = getCurrentTableLabelGroupList();

        if (null != labelGroupList && labelGroupList.contains(label) && state.equals(currentState)) {
            startConversationIndex++;
            if (startConversationIndex >= getCurrentTableLabelGroupList().size()) {
                startConversationIndex = 0;

                if (State.RECORDING.equals(state)) {
                    changeToPlayState();
                    startConversation();
                } else {
                    onRecordButtonPressed();
                }
            } else {
                startConversation();
            }
        }
    }

    private Label.LabelStyle getConversationLeftLabelStyle() {
        return getConversationLabelStyle(getLeftSpeakerConversationBox());
    }

    private Label.LabelStyle getConversationLabelStyle(Texture background) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        NinePatch ninePatch = new NinePatch(background);
        ninePatch.setPadLeft(CONVERSATION_LABEL_PAD_LEFT);
        labelStyle.background =
                new NinePatchDrawable(new NinePatchDrawable(ninePatch));
        labelStyle.font = FontGeneratorManager.getFont(CONVERSATION_TEXT_FONT_SIZE);
        labelStyle.fontColor = ColorProperties.TEXT;

        return labelStyle;
    }

    private Texture getLeftSpeakerConversationBox() {
        return AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "white_speech_box.png");
    }

    private Label.LabelStyle getConversationRightLabelStyle() {
        return getConversationLabelStyle(getRightSpeakerConversationBox());
    }

    private Texture getRightSpeakerConversationBox() {
        return AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "green_speech_box.png");
    }

    private Label.LabelStyle getTitleLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(TITLE_FONT_SIZE);
        labelStyle.fontColor = TITLE_COLOR;

        return labelStyle;
    }

    private Label.LabelStyle getSpeakerLabelStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontGeneratorManager.getFont(TITLE_FONT_SIZE);
        labelStyle.fontColor = Color.WHITE;
        return labelStyle;
    }

    private void changeToPlayState() {
        currentState = State.PLAY;

        if (null != recordButton) {
            recordButton.setHighlight(false);
        }

        if (null != playButton) {
            playButton.setHighlight(true);
        }
    }

    private void changeToSelectSpeakerState() {
        currentState = State.SELECT_SPEAKER;
        if (null != recordButton) {
            recordButton.setHighlight(true);
        }
        if (null != playButton) {
            playButton.setHighlight(false);
        }
    }

    private void changeToRecordingState() {
        currentState = State.RECORDING;
        if (null != recordButton) {
            recordButton.setHighlight(true);
        }
        if (null != playButton) {
            playButton.setHighlight(false);
        }
    }

    private Texture getHighlightSpeechBox() {
        return AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "white_speech_box_highlight.png");
    }

    private enum State {
        PLAY, PAUSE, SELECT_SPEAKER, RECORDING;
    }

    private class ControlGroup extends Group {
        private Image highlightControl;
        private Image control;

        public ControlGroup(String imageName, String highlightImageName) {
            control = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + imageName));
            highlightControl = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + highlightImageName));

            addActor(control);
            addActor(highlightControl);

            setSize(control.getWidth(), control.getHeight());

            setHighlight(false);
        }

        public void setHighlight(boolean isHighlight) {
            control.setVisible(!isHighlight);
            highlightControl.setVisible(isHighlight);
        }

    }

    private class SpeakerGroup extends Group {
        private Image highlightSpeaker;
        private Image speaker;

        public SpeakerGroup(boolean isLeft) {
            if (isLeft) {
                highlightSpeaker = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "left_speech_green.png"));
                speaker = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "left_speech_white.png"));
            } else {
                highlightSpeaker = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "right_speech_green.png"));
                speaker = new Image(AssetManagerUtils.getTextureWithWait(Config.CONVERSATION_IMAGE_PATH + "right_speech_white.png"));
            }

            addActor(speaker);
            addActor(highlightSpeaker);

            setSize(speaker.getWidth(), speaker.getHeight());

            setHighlight(false);
        }

        public void setHighlight(boolean isHighlight) {
            speaker.setVisible(!isHighlight);
            highlightSpeaker.setVisible(isHighlight);
        }

    }

    private class LabelGroup extends Group {

        private final boolean isLeft;
        private Label label;
        private Image highLightImage;
        private Conversation conversation;
        private boolean isHighLight;

        public LabelGroup(final Conversation conversation, boolean isLeft) {
            this.isLeft = isLeft;
            this.conversation = conversation;
            label = new Label(conversation.sentences, isLeft ? getConversationLeftLabelStyle() : getConversationRightLabelStyle());
            label.setWrap(true);
            Texture labelBackground = isLeft ? getLeftSpeakerConversationBox() : getRightSpeakerConversationBox();
            label.setSize(
                    labelBackground.getWidth(), labelBackground.getHeight() * CONVERSATION_BOX_HEIGHT_SCALE);

            label.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    if (State.PAUSE.equals(currentState)) {
                        setHighLight(true);
                        playAudio(conversation, new AbstractSoundPlayListener() {
                            @Override
                            public void onComplete() {
                                super.onComplete();
                                setHighLight(false);
                            }

                            @Override
                            public void onStop() {
                                super.onStop();
                                setHighLight(false);
                            }
                        });
                    }
                }
            });

            addActor(label);
            highLightImage = new Image(getHighlightSpeechBox());
            highLightImage.setHeight(highLightImage.getHeight() * CONVERSATION_BOX_HEIGHT_SCALE);
            addActor(highLightImage);

            setHighLight(false);
        }

        public void setHighLight(boolean isHighLight) {
            this.isHighLight = isHighLight;
            highLightImage.setVisible(isHighLight);
        }

        private void flashHighLightImage() {
            if (isHighLight) {
                AnimationUtils.doFlash(highLightImage, false, CONTROL_BUTTON_FLASH_DURATION_SECOND, 1, new Runnable() {

                    @Override
                    public void run() {
                        flashHighLightImage();
                    }
                });
            }
        }

        public boolean isLeft() {
            return isLeft;
        }


        public Conversation getConversation() {
            return conversation;
        }
    }

    private class ConversationRow extends Group {
        private ShapeRenderer background;

        @Override
        public void draw(Batch batch, float parentAlpha) {

            if (null == background) {
                background = new ShapeRenderer();
            }
            batch.end();
            background.setProjectionMatrix(batch.getProjectionMatrix());
            background.begin(ShapeRenderer.ShapeType.Filled);
            background.rect(getX(), getY(), getWidth(), getHeight());
            background.setColor(BACKGROUND_COLOR);
            background.end();

            batch.begin();
            super.draw(batch, parentAlpha);
        }

    }

    private class ConversationRowWithBottomLine extends ConversationRow {

        private ShapeRenderer bottomLine;

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.end();
            if (null == bottomLine) {
                bottomLine = new ShapeRenderer();
            }
            bottomLine.setProjectionMatrix(batch.getProjectionMatrix());
            bottomLine.begin(ShapeRenderer.ShapeType.Filled);
            bottomLine.rect(getX(), getY(), getWidth(), SEPARATOR_LINE_HEIGHT);
            bottomLine.setColor(TITLE_BOTTOM_LINE_COLOR);
            bottomLine.end();

            batch.begin();

        }
    }

    private class ConversationRowWithTopLine extends ConversationRow {

        private ShapeRenderer topLine;

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.end();
            if (null == topLine) {
                topLine = new ShapeRenderer();
            }
            topLine.setProjectionMatrix(batch.getProjectionMatrix());
            topLine.begin(ShapeRenderer.ShapeType.Filled);
            topLine.rect(getX(), getY() + getHeight() - SEPARATOR_LINE_HEIGHT, getWidth(), SEPARATOR_LINE_HEIGHT);
            topLine.setColor(TITLE_BOTTOM_LINE_COLOR);
            topLine.end();

            batch.begin();

        }
    }
}
