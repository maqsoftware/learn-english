package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.AbstractLearningGame;
import com.maqautocognita.Config;
import com.maqautocognita.adapter.IDeviceCameraService;
import com.maqautocognita.bo.CheatSheetBox;
import com.maqautocognita.bo.OCRRecognizedObject;
import com.maqautocognita.bo.OCRResult;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.listener.ICheatSheetForLifeMessageListener;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.ISoundPlayListener;
import com.maqautocognita.scene2d.actions.IAdvanceActionListener;
import com.maqautocognita.scene2d.actors.CheatSheetBoxActor;
import com.maqautocognita.scene2d.actors.CheatSheetForLifeMessageActor;
import com.maqautocognita.scene2d.actors.CheatSheetImageActor;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.CheatSheetForLifeService;
import com.maqautocognita.service.IBMVisualRecognizeService;
import com.maqautocognita.service.IBMWatonTranslateAndSpeechService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CheatSheetForLifeScreen extends AbstractAutoCognitaScreen implements ICheatSheetForLifeMessageListener {

    private static final String PICTURE_RECOGNITION_PATH = Gdx.files.getLocalStoragePath() + "/recognition.jpg";
    private static final float IMAGE_SCREEN_RATIO = 0.7f;
    private static final float MESSAGE_SCREEN_RATIO = 1 - IMAGE_SCREEN_RATIO;
    private final IDeviceCameraService deviceCameraService;
    private Stage stage;
    private CustomCamera camera;
    private Table container;
    private CheatSheetForLifeMessageActor cheatSheetForLifeMessageActor;
    private List<CheatSheetBoxActor> ocrRecognizedObjectList;

    private CheatSheetImageActor cheatSheetImageActor;

    private boolean isRecognitionMode = false;
    private boolean isVisualRecognitionSelected = true;

    private Image cameraPreviewImage;

    private boolean isPreviewing;

    private Texture takePictureBackground;

    private Texture takeOcrBackground;

    public CheatSheetForLifeScreen(AbstractGame game, IMenuScreenListener menuScreenListener, IDeviceCameraService deviceCameraService) {
        super(game, menuScreenListener);
        this.deviceCameraService = deviceCameraService;

        camera = new CustomCamera();
        camera.setWorldWidth(ScreenUtils.getScreenWidth());
        camera.setToOrtho(false, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
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
    public void onHomeClick() {
        menuScreenListener.onHomeSelected();
    }

    @Override
    protected List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    protected String getAudioPath() {
        return super.getAudioPath() + "/cheat_sheet";
    }

    @Override
    public void show() {
        super.show();

        camera.update();
        if (null == stage) {
            ScreenViewport screenViewport = new ScreenViewport(camera);
            screenViewport.setUnitsPerPixel(ScreenUtils.widthRatio);
            stage = new Stage(screenViewport);
        }
        if (null == cheatSheetForLifeMessageActor) {
            cheatSheetForLifeMessageActor = new CheatSheetForLifeMessageActor(this);
        }

        cheatSheetForLifeMessageActor.addTextTapListener();

        if (null == container) {
            container = new Table();
            container.setFillParent(true);
            stage.addActor(container);
        }

        updateScreen();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void updateScreen() {
        if (null != container) {
            container.clearChildren();
        }
        if (null != cheatSheetForLifeMessageActor) {
            cheatSheetForLifeMessageActor.clearText();
        }
        if (isRecognitionMode) {
            addCamera();
            if (null != cheatSheetImageActor) {
                cheatSheetImageActor.setVisible(false);
            }
        } else {
            addLifeSkillBuiltInContent();
            if (null != cheatSheetImageActor) {
                cheatSheetImageActor.setVisible(true);
            }
        }

        if (ScreenUtils.isTablet) {
            container.add(cheatSheetForLifeMessageActor).height(ScreenUtils.getScreenHeight()).width(ScreenUtils.getScreenWidth() / 2).fill();
        } else {
            container.row();
            container.add(cheatSheetForLifeMessageActor).height(ScreenUtils.getScreenHeight() * MESSAGE_SCREEN_RATIO).fill();
        }
    }

    private void addCamera() {
        if (null == cameraPreviewImage) {
            cameraPreviewImage = new Image();
            cameraPreviewImage.setScaling(Scaling.fit);
        }

        changeTakePictureBackground();
        addNonMessagePanel(cameraPreviewImage);
        cheatSheetForLifeMessageActor.changeToRecognitionMode();

    }

    private void addLifeSkillBuiltInContent() {
        deviceCameraService.stopPreview();
        if (null == cheatSheetImageActor) {
            float width = 0;
            float height = 0;
            if (ScreenUtils.isTablet) {
                width = ScreenUtils.getScreenWidth() / 2;
                height = ScreenUtils.getScreenHeight();
            } else {
                width = ScreenUtils.getScreenWidth();
                height = ScreenUtils.getScreenHeight() * IMAGE_SCREEN_RATIO;
            }

            cheatSheetImageActor =
                    new CheatSheetImageActor(width, height,
                            ScreenUtils.isTablet ? 0 : ScreenUtils.getScreenHeight() - height,
                            CheatSheetForLifeService.getInstance().getAllCheatSheet(), cheatSheetForLifeMessageActor);
        }

        addNonMessagePanel(cheatSheetImageActor);

        cheatSheetForLifeMessageActor.changeToLeftSkillBuiltInContentMode(cheatSheetImageActor);

    }

    private void changeTakePictureBackground() {
        if (null == takePictureBackground) {
            takePictureBackground = AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "take_picture_background.png");
        }
        cameraPreviewImage.setDrawable(new SpriteDrawable(new Sprite(takePictureBackground)));
    }

    private void addNonMessagePanel(Actor actor) {
        if (ScreenUtils.isTablet) {
            container.add(actor).height(ScreenUtils.getScreenHeight()).width(ScreenUtils.getScreenWidth() / 2).expand().fill();
        } else {
            container.add(actor).height(ScreenUtils.getScreenHeight() * IMAGE_SCREEN_RATIO).expand().fill();
        }
    }

    @Override
    protected boolean isRequiredToWaitImageLoading() {
        return false;
    }

    @Override
    public void doRender() {
        if (isRecognitionMode) {
            if (!deviceCameraService.isReady() || !isPreviewing) {
                deviceCameraService.startPreviewAsync();
                isPreviewing = true;
            }
            Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        }

        if (null != stage) {
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
        isRecognitionMode = false;
        isPreviewing = false;

        IBMWatonTranslateAndSpeechService.getInstance().stopPlayTextToSpeech();

        if (null != stage) {
            StageUtils.dispose(stage);
            stage = null;
        }

        if (null != container) {
            container.remove();
            container = null;
        }

        if (null != takePictureBackground) {
            takePictureBackground.dispose();
            takePictureBackground = null;
        }

        if (null != cameraPreviewImage) {
            cameraPreviewImage.remove();
            cameraPreviewImage = null;
        }

        if (null != takeOcrBackground) {
            takeOcrBackground.dispose();
            takeOcrBackground = null;
        }

        if (null != takePictureBackground) {
            takePictureBackground.dispose();
            takePictureBackground = null;
        }

        cheatSheetForLifeMessageActor = null;

        deviceCameraService.stopPreview();

        AbstractGame.audioService.stopMusic();


        if (null != cheatSheetImageActor) {
            cheatSheetImageActor.remove();
            cheatSheetImageActor = null;
        }
    }

    public void setRecognitionMode(boolean recognitionMode) {
        isRecognitionMode = recognitionMode;
    }

    @Override
    public void onPlayAudioPath(String audioFilePath, AbstractSoundPlayListener soundPlayListListener) {
        playSound(audioFilePath, soundPlayListListener);
    }

    @Override
    public void onTranslateLanguageSelected(String text, Language fromLanguage, Language toLanguage, IAdvanceActionListener translationListener) {
        if (StringUtils.isNotBlank(text)) {
            IBMWatonTranslateAndSpeechService.getInstance().translateTextToLanguage(text, fromLanguage, toLanguage, translationListener);
        }
    }

    @Override
    public void onTextToSpeech(String text, ISoundPlayListener soundPlayListener) {
        if (StringUtils.isNotBlank(text)) {
            IBMWatonTranslateAndSpeechService.getInstance().textToSpeech(text, soundPlayListener);
        }
    }

    @Override
    public void onCameraClick() {
        IBMWatonTranslateAndSpeechService.getInstance().stopPlayTextToSpeech();
        isRecognitionMode = true;
        updateScreen();
        showCameraPreviewImage();
    }

    private void showCameraPreviewImage() {
        cameraPreviewImage.setVisible(true);
    }

    @Override
    public void onLifeSkillBuiltInContentClick() {
        clearOCRBox();
        IBMWatonTranslateAndSpeechService.getInstance().stopPlayTextToSpeech();
        isRecognitionMode = false;
        isPreviewing = false;
        updateScreen();
    }

    @Override
    public void onVisualRecognitionSelected() {
        isVisualRecognitionSelected = true;
        clearOCRBox();
        changeTakePictureBackground();
    }

    @Override
    public void onTextRecognitionSelected() {
        isVisualRecognitionSelected = false;
        changeTakeOCRBackground();
    }

    private void changeTakeOCRBackground() {
        if (null == takeOcrBackground) {
            takeOcrBackground = AssetManagerUtils.getTextureWithWait(Config.COMMON_IMAGE_XDPI_PATH + "take_ocr_background.png");
        }
        cameraPreviewImage.setDrawable(new SpriteDrawable(new Sprite(takeOcrBackground)));
    }

    @Override
    public void onTakePictureClick() {
        if (deviceCameraService.isReady()) {
            cheatSheetForLifeMessageActor.showLoadingIcon();
            final int compressQuality = isVisualRecognitionSelected ? 90 : 100;
            final IAdvanceActionListener<Boolean> actionListener = new IAdvanceActionListener<Boolean>() {
                @Override
                public void onComplete(Boolean isSuccess) {
                    if (isSuccess) {
                        if (isVisualRecognitionSelected) {
                            doImageRecognize();
                        } else {
                            doOCR();
                        }
                    } else {
                        cheatSheetForLifeMessageActor.hideLoadingIcon();
                    }
                }
            };
            if (ScreenUtils.isTablet) {
                deviceCameraService.
                        takePictureFromTablet(0.5f,
                                PICTURE_RECOGNITION_PATH, actionListener, compressQuality);
            } else {
                new Runnable() {

                    @Override
                    public void run() {
                        deviceCameraService.
                                takePicture(IMAGE_SCREEN_RATIO,
                                        PICTURE_RECOGNITION_PATH, actionListener, compressQuality);
                    }
                }.run();

            }
            hideCameraPreviewImage();
        }
    }

    private void doImageRecognize() {
        IBMVisualRecognizeService.getInstance().doRecognize(PICTURE_RECOGNITION_PATH, new IAdvanceActionListener<String>() {
            @Override
            public void onComplete(String information) {
                if (StringUtils.isNotBlank(information)) {
                    cheatSheetForLifeMessageActor.setText(information);
                    cheatSheetForLifeMessageActor.showReturnIcon();
                    cheatSheetForLifeMessageActor.showAskIcon();
                } else {
                    isPreviewing = false;
                    showCameraPreviewImage();
                }
                cheatSheetForLifeMessageActor.hideLoadingIcon();
            }
        });


    }

    private void doOCR() {
        AbstractLearningGame.ocrService.processImage(PICTURE_RECOGNITION_PATH, new IAdvanceActionListener<OCRResult>() {
            @Override
            public void onComplete(OCRResult ocrResult) {
                if (null != ocrResult && CollectionUtils.isNotEmpty(ocrResult.getOCRRecognizedObjectList())) {
                    if (null == ocrRecognizedObjectList) {
                        ocrRecognizedObjectList = new ArrayList<CheatSheetBoxActor>();
                    }

                    int index = 0;

                    final float heightRatio = (ScreenUtils.isTablet ? ScreenUtils.getScreenHeight() :
                            ScreenUtils.getScreenHeight() * IMAGE_SCREEN_RATIO) / ocrResult.imageHeight;
                    final float widthRatio = (float) (ScreenUtils.isTablet ? ScreenUtils.getScreenWidth() / 2 : ScreenUtils.getScreenWidth()) / ocrResult.imageWidth;

                    for (OCRRecognizedObject recognizedObject : ocrResult.getOCRRecognizedObjectList()) {

                        CheatSheetBox cheatSheetBox = new CheatSheetBox();
                        cheatSheetBox.xPositionFromLeft = (int) recognizedObject.bound.x;
                        cheatSheetBox.yPositionFromTop = (int) recognizedObject.bound.y;
                        cheatSheetBox.width = (int) recognizedObject.bound.width;
                        cheatSheetBox.height = (int) recognizedObject.bound.height;
                        cheatSheetBox.text = recognizedObject.text;
                        CheatSheetBoxActor cheatSheetBoxActor = createCheatSheetBoxActor(cheatSheetBox, ocrRecognizedObjectList, widthRatio, heightRatio);
                        ocrRecognizedObjectList.add(cheatSheetBoxActor);
                        stage.addActor(cheatSheetBoxActor);

                        if (0 == index) {
                            cheatSheetBoxActor.setHighlighted(true);
                        }

                        index++;
                    }
                }

                if (null != cheatSheetForLifeMessageActor) {
                    cheatSheetForLifeMessageActor.hideLoadingIcon();
                    cheatSheetForLifeMessageActor.showReturnIcon();
                    cheatSheetForLifeMessageActor.showAskIcon();
                }
            }
        });
    }

    private void hideCameraPreviewImage() {
        cameraPreviewImage.setVisible(false);
    }

    private CheatSheetBoxActor createCheatSheetBoxActor(CheatSheetBox cheatSheetBox, List<CheatSheetBoxActor> cheatSheetBoxActorList,
                                                        float widthRatio, float heightRatio) {
        CheatSheetBoxActor cheatSheetBoxActor =
                new CheatSheetBoxActor(cheatSheetBox, cheatSheetForLifeMessageActor, cheatSheetBoxActorList);
        final float cheatSheetBoxHeight = cheatSheetBox.height * heightRatio;
        cheatSheetBoxActor.setPosition(cheatSheetBox.xPositionFromLeft * widthRatio,
                ScreenUtils.getScreenHeight() - cheatSheetBox.yPositionFromTop * heightRatio -
                        cheatSheetBoxHeight);
        cheatSheetBoxActor.setSize(cheatSheetBox.width * widthRatio, cheatSheetBoxHeight);
        cheatSheetBoxActor.toFront();
        return cheatSheetBoxActor;
    }

    @Override
    public void onLoading() {
        setTouchAllow(false);
    }

    @Override
    public void afterLoading() {
        setTouchAllow(true);
    }

    @Override
    public void onReturnToPreviousScreen() {
        IBMWatonTranslateAndSpeechService.getInstance().stopPlayTextToSpeech();

        clearOCRBox();

        if (isRecognitionMode) {
            //make the camera preview is on
            isPreviewing = false;
            cheatSheetForLifeMessageActor.changeToRecognitionMode();
            showCameraPreviewImage();
        } else {
            cheatSheetForLifeMessageActor.changeToLeftSkillBuiltInContentMode(cheatSheetImageActor);
        }
    }

    private void clearOCRBox() {
        if (CollectionUtils.isNotEmpty(ocrRecognizedObjectList)) {
            for (CheatSheetBoxActor cheatSheetBoxActor : ocrRecognizedObjectList) {
                cheatSheetBoxActor.remove();
            }
            ocrRecognizedObjectList.clear();
            ocrRecognizedObjectList = null;
        }
    }
}
