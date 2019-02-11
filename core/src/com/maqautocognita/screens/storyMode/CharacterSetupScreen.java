package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.Config;
import com.maqautocognita.bo.storyMode.Clothes;
import com.maqautocognita.bo.storyMode.Face;
import com.maqautocognita.bo.storyMode.FacialFeatures;
import com.maqautocognita.bo.storyMode.Hair;
import com.maqautocognita.constant.HairColorEnum;
import com.maqautocognita.constant.UserPreferencesKeys;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.listener.IUserPreferenceValueChangeListener;
import com.maqautocognita.scene2d.actions.CameraMoveXAction;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.scene2d.actions.IOptionSelectListener;
import com.maqautocognita.scene2d.actors.AbstractCameraActor;
import com.maqautocognita.scene2d.actors.BodyGroup;
import com.maqautocognita.scene2d.actors.CharacterClothesActor;
import com.maqautocognita.scene2d.actors.CharacterHairActor;
import com.maqautocognita.scene2d.actors.ColorPickerActor;
import com.maqautocognita.scene2d.actors.FaceGroup;
import com.maqautocognita.scene2d.actors.HighlightImageActor;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.actors.MessageActor;
import com.maqautocognita.scene2d.actors.Selector;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.service.CharacterService;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CharacterSetupScreenMessageUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StoryModeBackgroundMusicFileNameUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CharacterSetupScreen extends AbstractStoryScreen implements IUserPreferenceValueChangeListener {

    public static final String CHARACTER_IMAGE_FILE_NAME = Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/character_body.png";
    public static final String FACIAL_FEATURES_IMAGE_FILE_NAME = Config.STORY_HDPI_IMAGE_FOLDER_NAME + "/facial_features.png";
    private static final float WORLD_WIDTH = Config.TABLET_SCREEN_WIDTH * 3;
    private CustomCamera camera;
    private Stage stage;
    private CustomCamera navigationCamera;
    private Stage navigationStage;
    private ImageActor rightArrow;
    private ImageActor leftArrow;
    private ImageActor resetButton;


    private HighlightImageActor boyCharacterObjectActor;
    private HighlightImageActor girlCharacterObjectActor;

    /**
     * it will include the face, nose, eye, hair and mouth,
     */
    private FaceGroup faceGroup;
    private BodyGroup bodyGroup;
    private Selector hairSelector;

    private Selector eyebrowSelector;
    private Selector eyeSelector;
    private Selector noseSelector;
    private Selector mouthSelector;
    private ColorPickerActor hairColorPickerActor;

    private Selector bottomClothesSelector;
    private Selector footwearClothesSelector;
    private Selector topClothesSelector;

    //the selected hair color, default is black
    private HairColorEnum selectedHairColor = HairColorEnum.BLACK;

    private CharacterService characterService;

    private UserPreferenceUtils userPreferenceUtils;

    private Color[] faceColors;
    /**
     * store the maximum width of the facial side which is displayed in the facial feature config screen
     */
    private float maximumWidthInFacialDisplay;

    private CustomCamera messageCamera;
    private Stage messageStage;
    private MessageActor messageActor;

    public CharacterSetupScreen(AbstractGame game, final IMenuScreenListener menuScreenListener) {
        super(game, menuScreenListener);
        characterService = CharacterService.getInstance();
        userPreferenceUtils = UserPreferenceUtils.getInstance();

        userPreferenceUtils.addValueChangeListener(this);

        faceColors = new Color[characterService.getAllFace().size()
                //because it will include the default face which is not able to select by the user
                - 1];
        for (int i = 0; i < faceColors.length; i++) {
            Face face = characterService.getAllFace().get(i);
            if (StringUtils.isNotBlank(face.color)) {
                faceColors[i] = Color.valueOf(face.color);
            }
        }
    }

    @Override
    protected boolean isRequiredToShowCharacterSetupButton() {
        return false;
    }

    @Override
    public void show() {
        super.show();
        preload();
    }

    @Override
    protected boolean isCharacterRequiredToShow() {
        return false;
    }

    @Override
    protected Stage getMainStage() {
        return stage;
    }

    @Override
    public void hide() {
        super.hide();
        userPreferenceUtils.save();
    }

    @Override
    public void dispose() {
        super.dispose();

        boyCharacterObjectActor = null;
        girlCharacterObjectActor = null;
        faceGroup = null;
        bodyGroup = null;
        hairSelector = null;

        eyebrowSelector = null;
        eyeSelector = null;
        noseSelector = null;
        mouthSelector = null;
        hairColorPickerActor = null;

        bottomClothesSelector = null;
        footwearClothesSelector = null;
        topClothesSelector = null;
        disposeStage(stage);
        stage = null;

        camera = null;
        navigationCamera = null;

        rightArrow = null;
        leftArrow = null;
        resetButton = null;
        disposeStage(navigationStage);
        navigationStage = null;

        messageCamera = null;
        messageActor = null;
        disposeStage(messageStage);
        messageStage = null;

    }

    @Override
    public void doRender() {
        super.doRender();
        if (null != messageStage) {
            messageStage.act(Gdx.graphics.getDeltaTime());
            messageStage.draw();
        }
    }

    @Override
    protected String getBackgroundMusicFileName() {
        return StoryModeBackgroundMusicFileNameUtils.CHARACTER_SCREEN;
    }

    @Override
    protected Stage[] getStages() {
        return new Stage[]{stage, navigationStage};
    }

    @Override
    protected void afterCameraSetup() {
        super.afterCameraSetup();
        initMessageActor();
        super.updateCamera(messageCamera);
    }

    @Override
    protected CustomCamera[] getCameras() {
        return new CustomCamera[]{camera, navigationCamera};
    }

    @Override
    protected void updateCamera(CustomCamera customCamera) {

        super.updateCamera(customCamera);

        if (null != customCamera && customCamera.equals(camera)) {

            leftArrow.setVisible(true);

            float cameraStartXPosition = 0;
            if (userPreferenceUtils.isClothingConfigReady() || userPreferenceUtils.isFacialFeaturesConfigReady()) {
                cameraStartXPosition += getCameraXPositionForClothesConfigScreen();
                rightArrow.setVisible(true);
            } else if (userPreferenceUtils.isFaceSelected()) {
                cameraStartXPosition += getCameraXPositionForFacialFeaturesConfigScreen();
                rightArrow.setVisible(true);
            } else if (userPreferenceUtils.isSexSelected()) {
                cameraStartXPosition += getCameraXPositionForFaceColorConfigScreen();
            } else {
                leftArrow.setVisible(false);
            }

            if (cameraStartXPosition > 0) {
                camera.position.x = cameraStartXPosition;
            }
        }

    }

    private float getCameraXPositionForClothesConfigScreen() {
        return Config.TABLET_SCREEN_WIDTH * 2 + camera.viewportWidth / 2;
    }

    private float getCameraXPositionForFacialFeaturesConfigScreen() {
        return Config.TABLET_SCREEN_WIDTH + camera.viewportWidth / 2;
    }

    private float getCameraXPositionForFaceColorConfigScreen() {
        return Config.TABLET_SCREEN_WIDTH + ScreenUtils.getXPositionForCenterObject(maximumWidthInFacialDisplay)
                //make sure the camera is focus to center of the facial display
                - maximumWidthInFacialDisplay / 2;
    }

    public void preload() {

        Hair selectedHair = characterService.getSelectedHair();
        if (null != selectedHair) {
            selectedHairColor = selectedHair.getColor();
        }

        //if the userPreferenceUtils sex is selected, visible the right arrow
        addRightArrow(false);
        addLeftArrow(false);
        addResetButton();

        addSexSelectionScreen();
        addFaceConfigScreen(Config.TABLET_SCREEN_WIDTH);
        addClothesConfigScreen(Config.TABLET_SCREEN_WIDTH * 2);
    }

    private void initMessageActor() {
        if (null == messageCamera) {
            messageCamera = new CustomCamera();
            messageCamera.setWorldWidth(Config.TABLET_SCREEN_WIDTH);
        }

        if (null == messageStage) {
            messageStage = new Stage(new ScreenViewport(messageCamera));
        }

        if (null == messageActor) {
            messageActor = new MessageActor(this);
            messageStage.addActor(messageActor);
        }
    }

    private void showMessage(String displayMessage, String audioFileName) {
        initMessageActor();
        messageActor.setText(displayMessage, audioFileName, null);
    }

    private float getContentStartXPosition() {
        return leftArrow.getX() + leftArrow.getWidth() + 100;
    }

    private void addSexSelectionScreen() {
        final float startXPosition = ScreenUtils.getXPositionForCenterObject(ImagePosition.BOY_CHARACTER_POSITION.width * 3);

        final float startYPosition = ScreenUtils.getBottomYPositionForCenterObject(ImagePosition.BOY_CHARACTER_POSITION.height);


        boyCharacterObjectActor =
                new HighlightImageActor(CHARACTER_IMAGE_FILE_NAME, ImagePosition.HIGHLIGHTED_BOY_CHARACTER_POSITION,
                        CHARACTER_IMAGE_FILE_NAME, ImagePosition.BOY_CHARACTER_POSITION, startXPosition, startYPosition);

        girlCharacterObjectActor =
                new HighlightImageActor(CHARACTER_IMAGE_FILE_NAME, ImagePosition.HIGHLIGHTED_GIRL_CHARACTER_POSITION,
                        CHARACTER_IMAGE_FILE_NAME, ImagePosition.GIRL_CHARACTER_POSITION,
                        //suppose the start x position will be on the right of the boy userPreferenceUtils and a gap which is in the same width of the userPreferenceUtils
                        startXPosition + ImagePosition.BOY_CHARACTER_POSITION.width * 2,
                        startYPosition);


        boyCharacterObjectActor.setSingleTapListener(new IActionListener() {
            @Override
            public void onComplete() {
                girlCharacterObjectActor.setHighlighted(false);
                highlightRightArrow();
                userPreferenceUtils.setIsGirl(false);
                showMessage(CharacterSetupScreenMessageUtils.getCharacterSexMessage("boy"), CharacterSetupScreenMessageUtils.getCharacterSexAudioFileName("boy"));
            }
        });

        girlCharacterObjectActor.setSingleTapListener(new IActionListener() {
            @Override
            public void onComplete() {
                boyCharacterObjectActor.setHighlighted(false);
                highlightRightArrow();
                userPreferenceUtils.setIsGirl(true);
                showMessage(CharacterSetupScreenMessageUtils.getCharacterSexMessage("girl"),
                        CharacterSetupScreenMessageUtils.getCharacterSexAudioFileName("girl")
                );
            }
        });


        Boolean isGirlSelected = userPreferenceUtils.isGirl();

        if (null != isGirlSelected) {
            if (isGirlSelected.booleanValue()) {
                girlCharacterObjectActor.setHighlighted(true);
            } else {
                boyCharacterObjectActor.setHighlighted(true);
            }
        }

        addActor(boyCharacterObjectActor);
        addActor(girlCharacterObjectActor);

    }

    private Face changeFaceColor(FaceGroup selectedFaceGroup, Color selectedColor) {
        Face selectedFace = characterService.getFaceByColor(selectedColor.toString().substring(0, 6));
        selectedFaceGroup.changeFace(selectedFace);
        userPreferenceUtils.setFaceId(selectedFace.id);
        return selectedFace;
    }

    private void addFaceConfigScreen(final float startXPosition) {


        faceGroup = new FaceGroup(0, ScreenUtils.getBottomYPositionForCenterObject(FaceGroup.FACE_SIZE));

        addActor(faceGroup);

        Color[] hairColors = new Color[HairColorEnum.values().length];

        for (int i = 0; i < HairColorEnum.values().length; i++) {
            hairColors[i] = HairColorEnum.values()[i].colorCode;
        }

        hairColorPickerActor = new ColorPickerActor(0, faceGroup.getY() + faceGroup.getHeight() + 150,
                hairColors);

        hairColorPickerActor.setOptionSelectListener(new IOptionSelectListener<Color>() {
            @Override
            public void onTap(Color selectedColor) {

                HairColorEnum selectedHairColorEnum = HairColorEnum.getHairColorEnumByColor(selectedColor);

                if (null != selectedHairColorEnum) {

                    reloadHairSelector(selectedHairColorEnum);
                    //change the current selected hair color
                    Hair selectedHair = faceGroup.changeHairColor(selectedHairColorEnum);

                    if (null != selectedHair) {
                        userPreferenceUtils.setHairId(selectedHair.id);
                    }
                }

            }
        });

        ColorPickerActor faceColorPickerActor = new ColorPickerActor(0, faceGroup.getY() - 150, faceColors);
        faceColorPickerActor.setOptionSelectListener(new IOptionSelectListener<Color>() {
            @Override
            public void onTap(Color selectedColor) {
                changeFaceColor(faceGroup, selectedColor);
                highlightRightArrow();
                showMessage(CharacterSetupScreenMessageUtils.getSkinToneMessage(),
                        CharacterSetupScreenMessageUtils.getSkinToneAudioFileName()
                );
            }
        });

        float faceStartXPosition = startXPosition + getContentStartXPosition();
        //check the max width
        maximumWidthInFacialDisplay = Math.max(faceGroup.getWidth(), Math.max(hairColorPickerActor.getWidth(), faceColorPickerActor.getWidth()));
        //make sure the color picker and the face is align in center
        faceGroup.setX(faceStartXPosition + (maximumWidthInFacialDisplay - faceGroup.getWidth()) / 2);
        hairColorPickerActor.setX(faceStartXPosition + (maximumWidthInFacialDisplay - hairColorPickerActor.getWidth()) / 2);
        faceColorPickerActor.setX(faceStartXPosition + (maximumWidthInFacialDisplay - faceColorPickerActor.getWidth()) / 2);


        addActor(hairColorPickerActor);
        addActor(faceColorPickerActor);

        //add hair border
        float selectorStartXPosition = faceGroup.getX() + faceGroup.getWidth() + 150;

        hairSelector = new Selector(new ImageActor(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH, ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE,
                selectorStartXPosition, 850), getSelectedHairColorList(selectedHairColor));

        hairSelector.setOptionSelectListener(new IOptionSelectListener<CharacterHairActor>() {
            @Override
            public void onTap(CharacterHairActor selectedActor) {
                addHair(selectedActor.getHair());
                showMessage(CharacterSetupScreenMessageUtils.getHairMessage(),
                        CharacterSetupScreenMessageUtils.getHairAudioFileName()
                );
            }
        });
        //add hair selection
        addActor(hairSelector);

        eyebrowSelector = addFacialFeatureSelector(characterService.getAllEyebrow(), selectorStartXPosition, 850 - ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE.height - 50, FacePart.EYEBROW);

        eyeSelector = addFacialFeatureSelector(characterService.getAllEye(), selectorStartXPosition, 850 - ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE.height * 2 - 50 * 2, FacePart.EYE);

        noseSelector = addFacialFeatureSelector(characterService.getAllNose(), selectorStartXPosition, 850 - ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE.height * 3 - 50 * 3, FacePart.NOSE);

        mouthSelector = addFacialFeatureSelector(characterService.getAllMouth(), selectorStartXPosition, 850 - ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE.height * 4 - 50 * 4, FacePart.MOUTH);

        if (userPreferenceUtils.isFaceSelected()) {
            showFacialFeaturesSelector();
        } else {
            hideFacialFeaturesSelector();
        }

    }

    private void hideFacialFeaturesSelector() {
        setFacialFeaturesSelectorVisible(false);
    }

    private void reloadHairSelector(HairColorEnum selectedHairColorEnum) {
        hairSelector.reloadOptionList(getSelectedHairColorList(selectedHairColorEnum));
    }

    private Selector addFacialFeatureSelector(List<FacialFeatures> facialFeaturesList, float startXPosition, float startYPosition,
                                              final FacePart facePart) {

        Selector selector = null;

        if (CollectionUtils.isNotEmpty(facialFeaturesList)) {

            List<ImageActor> facialFeaturesActorList = new ArrayList<ImageActor>(facialFeaturesList.size());
            float imageSize = FaceGroup.FACE_SIZE;
            for (FacialFeatures facialFeatures : facialFeaturesList) {
                ImageActor<FacialFeatures> facialFeaturesActor = new ImageActor<FacialFeatures>(FACIAL_FEATURES_IMAGE_FILE_NAME,
                        new IconPosition(facialFeatures.xPositionToImage, facialFeatures.yPositionToImage, imageSize, imageSize));

                facialFeaturesActor.setId(facialFeatures);

                facialFeaturesActorList.add(facialFeaturesActor);
            }

            selector = new Selector(new ImageActor(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH, ImagePosition.FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE,
                    startXPosition, startYPosition), facialFeaturesActorList, false);

            selector.setOptionSelectListener(new IOptionSelectListener<ImageActor<FacialFeatures>>() {
                @Override
                public void onTap(ImageActor<FacialFeatures> selectedActor) {
                    switch (facePart) {
                        case NOSE:
                            addNose(selectedActor.getId());
                            showMessage(CharacterSetupScreenMessageUtils.getNoseMessage(),
                                    CharacterSetupScreenMessageUtils.getNoseAudioFileName());
                            break;
                        case MOUTH:
                            addMouth(selectedActor.getId());
                            showMessage(CharacterSetupScreenMessageUtils.getMouthMessage(),
                                    CharacterSetupScreenMessageUtils.getMouthAudioFileName());
                            break;
                        case EYEBROW:
                            addEyeBrow(selectedActor.getId());
                            showMessage(CharacterSetupScreenMessageUtils.getEyeBrowsMessage(),
                                    CharacterSetupScreenMessageUtils.getEyeBrowsAudioFileName()
                            );
                            break;
                        case EYE:
                            addEye(selectedActor.getId());
                            showMessage(CharacterSetupScreenMessageUtils.getEyeMessage(),
                                    CharacterSetupScreenMessageUtils.getEyeAudioFileName());
                            break;
                    }
                }
            });

            addActor(selector);
        }

        return selector;
    }

    private void addNose(FacialFeatures nose) {
        faceGroup.addNose(nose);
        userPreferenceUtils.setNoseId(nose.id);
    }

    private void addMouth(FacialFeatures mouth) {
        faceGroup.addMouth(mouth);
        userPreferenceUtils.setMouthId(mouth.id);
    }

    private void addEyeBrow(FacialFeatures eyeBrow) {
        faceGroup.addEyeBrow(eyeBrow);
        userPreferenceUtils.setEyebrowId(eyeBrow.id);
    }

    private void addHair(Hair hair) {
        faceGroup.addHair(hair);
        userPreferenceUtils.setHairId(hair.id);
    }

    private void addEye(FacialFeatures eye) {
        faceGroup.addEye(eye);
        userPreferenceUtils.setEyeId(eye.id);
    }

    private void addRandomHair() {
        if (userPreferenceUtils.getHairId() == 0) {
            addHair(getRandomItem(getSelectedHairColorList(selectedHairColor)).getHair());
        }
    }

    private void addRandomEye() {
        if (userPreferenceUtils.getEyeId() == 0) {
            addEye(getRandomItem(characterService.getAllEye()));
        }
    }

    private void addRandomNose() {
        if (userPreferenceUtils.getNoseId() == 0) {
            addNose(getRandomItem(characterService.getAllNose()));
        }
    }

    private void addRandomMouth() {
        if (userPreferenceUtils.getMouthId() == 0) {
            addMouth(getRandomItem(characterService.getAllMouth()));
        }
    }

    private void addRandomEyebrow() {
        if (userPreferenceUtils.getEyebrowId() == 0) {
            addEyeBrow(getRandomItem(characterService.getAllEyebrow()));
        }
    }

    private <T> T getRandomItem(List<T> itemList) {
        return itemList.get(new Random().nextInt(itemList.size()));
    }

    /**
     * show and highlight the right arrow if the all required face feature have setup , checked by the method {@link UserPreferenceUtils#isFacialFeaturesConfigReady}
     */
    private void highLightRightArrowForFaceConfigScreen() {
        if (userPreferenceUtils.isFacialFeaturesConfigReady()) {
            highlightRightArrow();
        }
    }

    /**
     * highlight the right arrow {@link #rightArrow}, repeat the fade in and fade out action in 3 times, if the right arrow is already visible, no highlight behaviour will be do
     */
    private void highlightRightArrow() {
        if (null != rightArrow && !rightArrow.isVisible()) {
            rightArrow.setVisible(true);

            AnimationUtils.doFlash(rightArrow);
        }
    }

    private void highLightRightArrowForClothingScreen() {
        if (userPreferenceUtils.isClothingConfigReady()) {
            highlightRightArrow();
        }
    }

    private void addClothesConfigScreen(final float startXPosition) {

        float screenStartXPosition = startXPosition + getContentStartXPosition();

        bodyGroup = new BodyGroup(screenStartXPosition, ScreenUtils.getBottomYPositionForCenterObject(BodyGroup.BODY_SIZE.y));

        faceGroup.setBodyGroup(bodyGroup);


        //add top clothes selector
        float selectorStartXPosition = bodyGroup.getX() + bodyGroup.getWidth() + 150;

        topClothesSelector = new Selector(
                new ImageActor(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH,
                        ImagePosition.CLOTHES_BORDER_POSITION_TO_IMAGE, selectorStartXPosition, 750),
                getClotheList(1));

        topClothesSelector.setOptionSelectListener(new IOptionSelectListener<CharacterClothesActor>() {
            @Override
            public void onTap(CharacterClothesActor selectedActor) {

                addTopClothes(selectedActor.getClothes());

                highlightRightArrow();

                showMessage(CharacterSetupScreenMessageUtils.getCharacterClothesMessage(selectedActor.getClothes().word)
                        ,
                        CharacterSetupScreenMessageUtils.getCharacterClothesAudioFileName(selectedActor.getClothes().word)
                );
            }
        });

        bottomClothesSelector = new Selector(
                new ImageActor(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH, ImagePosition.CLOTHES_BORDER_POSITION_TO_IMAGE, selectorStartXPosition,
                        750 - ImagePosition.CLOTHES_BORDER_POSITION_TO_IMAGE.height - 50), getClotheList(2));

        bottomClothesSelector.setOptionSelectListener(new IOptionSelectListener<CharacterClothesActor>() {
            @Override
            public void onTap(CharacterClothesActor selectedActor) {

                addBottomClothes(selectedActor.getClothes());

                highlightRightArrow();

                showMessage(CharacterSetupScreenMessageUtils.getCharacterShortsMessage(selectedActor.getClothes().word),
                        CharacterSetupScreenMessageUtils.getCharacterShortsAudioFileName(selectedActor.getClothes().word)
                );
            }
        });

        footwearClothesSelector = new Selector(
                new ImageActor(AssetManagerUtils.STORY_MODE_ICON_IMAGE_PATH, ImagePosition.CLOTHES_BORDER_POSITION_TO_IMAGE, selectorStartXPosition,
                        750 - ImagePosition.CLOTHES_BORDER_POSITION_TO_IMAGE.height * 2 - 50 * 2), getClotheList(3));
        footwearClothesSelector.setOptionSelectListener(new IOptionSelectListener<CharacterClothesActor>() {
            @Override
            public void onTap(CharacterClothesActor selectedActor) {
                addFootwear(selectedActor.getClothes());
                highlightRightArrow();
                showMessage(CharacterSetupScreenMessageUtils.getCharacterShortsMessage(selectedActor.getClothes().word),
                        CharacterSetupScreenMessageUtils.getCharacterShortsAudioFileName(selectedActor.getClothes().word));
            }
        });

        addActor(topClothesSelector);
        addActor(bottomClothesSelector);
        addActor(footwearClothesSelector);
        addActor(bodyGroup);
    }

    private void addRandomClothes() {
        if (userPreferenceUtils.getTopClothesId() == 0) {
            addTopClothes(getRandomItem(getClotheList(1)).getClothes());
        }
        if (userPreferenceUtils.getBottomClothesId() == 0) {
            List<CharacterClothesActor> bottomClothesList = getClotheList(2);
            List<CharacterClothesActor> filterBottomClothesList = new ArrayList<CharacterClothesActor>();
            for (CharacterClothesActor bottomClothes : bottomClothesList) {
                Clothes topClothes = bodyGroup.getCharacterTopClothesActor().getClothes();
                if (topClothes.anotherSelectionLevel != bottomClothes.getClothes().selectionLevel) {
                    filterBottomClothesList.add(bottomClothes);
                }
            }
            if (CollectionUtils.isNotEmpty(filterBottomClothesList)) {
                addBottomClothes(getRandomItem(filterBottomClothesList).getClothes());
            }
        }
        if (userPreferenceUtils.getFootwearId() == 0) {
            addFootwear(getRandomItem(getClotheList(3)).getClothes());
        }
    }

    private void addTopClothes(Clothes topClothes) {
        bodyGroup.addTopClothes(topClothes);
        userPreferenceUtils.setTopClothesId(topClothes.id);

        if (null != bodyGroup.getCharacterBottomClothesActor()) {
            Clothes bottomClothes = bodyGroup.getCharacterBottomClothesActor().getClothes();
            if (null != bottomClothes && bottomClothes.selectionLevel == topClothes.anotherSelectionLevel) {
                userPreferenceUtils.removeByKey(UserPreferencesKeys.BOTTOM_CLOTHES_ID);
            }
        }
    }

    private void addBottomClothes(Clothes bottomClothes) {
        bodyGroup.addBottomClothes(bottomClothes);
        userPreferenceUtils.setBottomClothesId(bottomClothes.id);

        if (null != bodyGroup.getCharacterTopClothesActor()) {
            Clothes topClothes = bodyGroup.getCharacterTopClothesActor().getClothes();
            if (null != topClothes && topClothes.anotherSelectionLevel == bottomClothes.selectionLevel) {
                userPreferenceUtils.removeByKey(UserPreferencesKeys.TOP_CLOTHES_ID);
            }
        }
    }

    private void addFootwear(Clothes footwear) {
        bodyGroup.addFootwearClothes(footwear);
        userPreferenceUtils.setFootwearId(footwear.id);
    }

    private void addRightArrow(boolean isVisible) {
        final float rightArrowStartYPosition = ScreenUtils.getBottomYPositionForCenterObject(NavigationSection.RIGHT_ARROW_POSITION.height);

        rightArrow =
                new ImageActor(AssetManagerUtils.ICONS, NavigationSection.RIGHT_ARROW_POSITION,
                        ScreenUtils.getNavigationRightArrowStartXPosition(), rightArrowStartYPosition);

        rightArrow.setVisible(isVisible);

        rightArrow.addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {

                /**
                 * used to indicate if the right arrow is allow show in the next screen
                 */
                boolean isRightArrowCanShow;

                SetupStep comingSetupStep = getCurrentSetupStepByCameraXPosition(camera.position.x + Config.TABLET_SCREEN_WIDTH);

                if (null == comingSetupStep) {
                    //which mean reach the end of the screen, exit the setup screen, go to previous screen (in the clothes selection screen)

                    //make sure clothes are selected even the user has no selected
                    addRandomClothes();
                    menuScreenListener.onPreviousScreenSelected();
                } else {
                    switch (comingSetupStep) {
                        case CLOTHES_SELECTION:
                            //which mean it will be in clothing screen

                            //in order to let the user click the right arrow without select facial feature,
                            // here need make sure the feature is selected by random if the user has no selected
                            addRandomEye();
                            addRandomEyebrow();
                            addRandomMouth();
                            addRandomNose();
                            addRandomHair();
                            nextScreen(Config.TABLET_SCREEN_WIDTH, true);
                            break;
                        case FACIAL_FEATURE_SELECTION:
                            //which mean it will be  in facial feature screen
                            isRightArrowCanShow = userPreferenceUtils.isFacialFeaturesConfigReady();

                            float moveDistance = Config.TABLET_SCREEN_WIDTH;
                            if (!userPreferenceUtils.isFaceSelected()) {
                                //move to the facial color selector screen, make it center in the screen
                                moveDistance = getCameraXPositionForFaceColorConfigScreen() - camera.position.x;
                            }

                            nextScreen(moveDistance, isRightArrowCanShow);
                            break;
                    }
                }

            }

            private void nextScreen(float moveDistance, final boolean isRightArrowCanShow) {
                rightArrow.setVisible(false);
                leftArrow.setVisible(false);
                resetButton.setVisible(false);
                CameraMoveXAction cameraMoveXAction = new CameraMoveXAction(moveDistance, camera, 1.5f);
                cameraMoveXAction.setActionListener(new IActionListener() {
                    @Override
                    public void onComplete() {
                        leftArrow.setVisible(true);
                        resetButton.setVisible(true);
                        rightArrow.setVisible(isRightArrowCanShow);
                    }
                });

                stage.addAction(cameraMoveXAction);
            }

        });

        addActorInNavigation(rightArrow);
    }

    /**
     * get the current setup step screen by the given cameraXPosition, suppose that the camera x position will be start {@link javafx.scene.Camera#viewWidth}/2
     *
     * @param cameraXPosition
     * @return
     */
    private SetupStep getCurrentSetupStepByCameraXPosition(float cameraXPosition) {
        if (cameraXPosition <= Config.TABLET_SCREEN_WIDTH) {
            return SetupStep.CHARACTER_SEX_SELECTION;
        } else if (cameraXPosition <= getCameraXPositionForFaceColorConfigScreen()) {
            return SetupStep.FACE_COLOR_SELECTION;
        } else if (cameraXPosition <= Config.TABLET_SCREEN_WIDTH * 2) {
            return SetupStep.FACIAL_FEATURE_SELECTION;
        } else if (cameraXPosition <= WORLD_WIDTH) {
            return SetupStep.CLOTHES_SELECTION;
        }

        return null;

    }

    private void addLeftArrow(boolean isVisible) {
        final float leftArrowStartYPosition = ScreenUtils.getBottomYPositionForCenterObject(NavigationSection.LEFT_ARROW_POSITION.height);

        leftArrow =
                new ImageActor(AssetManagerUtils.ICONS, NavigationSection.LEFT_ARROW_POSITION,
                        getHomeIconScreenPosition().x, leftArrowStartYPosition);

        leftArrow.addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {


                leftArrow.setVisible(false);
                rightArrow.setVisible(false);
                resetButton.setVisible(false);

                float moveXPosition = -Config.TABLET_SCREEN_WIDTH;

                if (SetupStep.FACE_COLOR_SELECTION.equals(getCurrentSetupStepByCameraXPosition(camera.position.x))) {
                    moveXPosition = Config.TABLET_SCREEN_WIDTH - getCameraXPositionForFaceColorConfigScreen() - camera.viewportWidth / 2;
                }

                CameraMoveXAction cameraMoveXAction = new CameraMoveXAction(moveXPosition, camera, 1.5f);
                cameraMoveXAction.setActionListener(new IActionListener() {
                    @Override
                    public void onComplete() {
                        SetupStep currentSetupStep = getCurrentSetupStepByCameraXPosition(camera.position.x);


                        //if the current screen is not the first screen, show the left arrow
                        leftArrow.setVisible(!SetupStep.CHARACTER_SEX_SELECTION.equals(currentSetupStep));
                        resetButton.setVisible(true);

                        switch (currentSetupStep) {
                            case CHARACTER_SEX_SELECTION:
                                rightArrow.setVisible(userPreferenceUtils.isSexSelected());
                                break;
                            case FACIAL_FEATURE_SELECTION:
                                //if the face color is selected , allow the user click to next screen
                                rightArrow.setVisible(userPreferenceUtils.isFaceSelected());
                                break;
                            case CLOTHES_SELECTION:
                                //always show when the user stay in the last screen
                                rightArrow.setVisible(true);
                        }


                    }
                });

                stage.addAction(cameraMoveXAction);

            }
        });

        leftArrow.setVisible(isVisible);

        addActorInNavigation(leftArrow);
    }

    private void addResetButton() {

        resetButton =
                new ImageActor(AssetManagerUtils.ICONS, ImagePosition.TRASH_ICON_POSITION_TO_IMAGE,
                        ScreenUtils.getNavigationRightArrowStartXPosition(), 100);

        resetButton.addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {

                SetupStep currentSetupStep = getCurrentSetupStepByCameraXPosition(camera.position.x);
                if (null != currentSetupStep) {
                    switch (currentSetupStep) {
                        case CHARACTER_SEX_SELECTION:
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.IS_GIRL);
                        case FACIAL_FEATURE_SELECTION:
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.EYE_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.FACE_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.EYEBROW_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.HAIR_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.MOUTH_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.NOSE_ID);
                            hideFacialFeaturesSelector();
                        case CLOTHES_SELECTION:
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.TOP_CLOTHES_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.BOTTOM_CLOTHES_ID);
                            userPreferenceUtils.removeByKey(UserPreferencesKeys.FOOTWEAR_ID);
                    }

                    if (SetupStep.FACIAL_FEATURE_SELECTION.equals(currentSetupStep)) {
                        moveCameraToFacialColorSelectionScreen();
                    }
                }

            }
        });

        addActorInNavigation(resetButton);
    }

    private void moveCameraToFacialColorSelectionScreen() {
        leftArrow.setVisible(false);
        resetButton.setVisible(false);
        stage.addAction(new CameraMoveXAction(getCameraXPositionForFaceColorConfigScreen() - camera.position.x, camera, 1.5f,
                new IActionListener() {
                    @Override
                    public void onComplete() {
                        leftArrow.setVisible(true);
                        resetButton.setVisible(true);
                    }
                }));
    }

    private void addActor(Actor storyModeActor) {
        if (null != storyModeActor) {
            if (null == stage) {
                initStage();
            }
            stage.addActor(storyModeActor);
        }
    }

    private void addActorInNavigation(AbstractCameraActor storyModeActor) {
        if (null != storyModeActor) {
            if (null == navigationStage) {
                initNavigationStage();
            }
            navigationStage.addActor(storyModeActor);
        }
    }

    private void initStage() {
        if (null == camera) {
            camera = new CustomCamera();
            //because there are 3 screen for "userPreferenceUtils sex select","face setup","clothes setup"
            camera.setWorldWidth(WORLD_WIDTH);
        }
        ScreenViewport screenViewport = new ScreenViewport(camera);
        stage = new Stage(screenViewport);
        addInputProcessor(stage);
    }

    public void initNavigationStage() {
        if (null == navigationCamera) {
            navigationCamera = new CustomCamera();
            navigationCamera.setWorldWidth(Config.TABLET_SCREEN_WIDTH);
        }
        ScreenViewport screenViewport = new ScreenViewport(navigationCamera);
        navigationStage = new Stage(screenViewport);
        addInputProcessor(navigationStage);
    }

    @Override
    protected boolean isRequiredToWaitImageLoading() {
        return false;
    }

    @Override
    public void onValueChange(String key, Object value) {
        if (UserPreferencesKeys.IS_GIRL.equals(key)) {
            hairSelector.reloadOptionList(getSelectedHairColorList(selectedHairColor));
            topClothesSelector.reloadOptionList(getClotheList(1));
            bottomClothesSelector.reloadOptionList(getClotheList(2));
            footwearClothesSelector.reloadOptionList(getClotheList(3));
        } else if (UserPreferencesKeys.FACE_ID.equals(key)) {

            if (!isFacialFeaturesSelectorVisible()) {
                //make sure the camera is move to show the whole facial and facial feature selector
                CameraMoveXAction cameraMoveXAction = new CameraMoveXAction(camera.position.x - Config.TABLET_SCREEN_WIDTH, camera, 1f);
                cameraMoveXAction.setActionListener(new IActionListener() {
                    @Override
                    public void onComplete() {
                        //make sure the facial features selector is shown
                        showFacialFeaturesSelector();
                    }
                });
                stage.addAction(cameraMoveXAction);
            }
        }
    }

    private List<CharacterHairActor> getSelectedHairColorList(HairColorEnum selectedHairColor) {
        if (userPreferenceUtils.isSexSelected()) {

            this.selectedHairColor = selectedHairColor;
            List<Hair> hairList = characterService.getAllHair();
            if (CollectionUtils.isNotEmpty(hairList)) {
                boolean isGirlSelected = userPreferenceUtils.isGirl();
                //add selection border
                List<CharacterHairActor> hairStyleList = new ArrayList<CharacterHairActor>();

                for (Hair hair : hairList) {
                    if (selectedHairColor.equals(hair.getColor()) && isGirlSelected == hair.isGirl) {
                        hairStyleList.add(new CharacterHairActor(hair));
                    }
                }

                return hairStyleList;
            }
        }
        return null;
    }

    private List<CharacterClothesActor> getClotheList(int selectionLevel) {
        if (userPreferenceUtils.isSexSelected()) {
            List<Clothes> clothesList = characterService.getAllClothes();
            if (CollectionUtils.isNotEmpty(clothesList)) {

                boolean isGirlSelected = userPreferenceUtils.isGirl();

                //add selection border
                List<CharacterClothesActor> clothesActorList = new ArrayList<CharacterClothesActor>();

                for (Clothes clothes : clothesList) {
                    if (selectionLevel == clothes.selectionLevel &&
                            ((isGirlSelected && clothes.isGirl) || (!isGirlSelected && clothes.isBoy))
                            ) {
                        clothesActorList.add(new CharacterClothesActor(clothes));
                    }
                }

                return clothesActorList;
            }
        }
        return null;
    }

    private boolean isFacialFeaturesSelectorVisible() {
        return hairColorPickerActor.isVisible();
    }

    private void showFacialFeaturesSelector() {
        setFacialFeaturesSelectorVisible(true);
    }

    private void setFacialFeaturesSelectorVisible(boolean isVisible) {
        if (null != hairColorPickerActor) {
            hairColorPickerActor.setVisible(isVisible);
        }
        if (null != hairSelector) {
            hairSelector.setVisible(isVisible);
        }
        if (null != eyebrowSelector) {
            eyebrowSelector.setVisible(isVisible);
        }
        if (null != eyeSelector) {
            eyeSelector.setVisible(isVisible);
        }
        if (null != noseSelector) {
            noseSelector.setVisible(isVisible);
        }
        if (null != mouthSelector) {
            mouthSelector.setVisible(isVisible);
        }
    }

    @Override
    public void onRemove(String key) {
        if (UserPreferencesKeys.IS_GIRL.equals(key)) {
            boyCharacterObjectActor.setHighlighted(false);
            girlCharacterObjectActor.setHighlighted(false);
        } else if (UserPreferencesKeys.FACE_ID.equals(key)) {
            faceGroup.removeFace();
        } else if (UserPreferencesKeys.HAIR_ID.equals(key)) {
            faceGroup.removeHair();
        } else if (UserPreferencesKeys.NOSE_ID.equals(key)) {
            faceGroup.removeNose();
        } else if (UserPreferencesKeys.EYE_ID.equals(key)) {
            faceGroup.removeEye();
        } else if (UserPreferencesKeys.MOUTH_ID.equals(key)) {
            faceGroup.removeMouth();
        } else if (UserPreferencesKeys.EYEBROW_ID.equals(key)) {
            faceGroup.removeEyebrow();
        } else if (UserPreferencesKeys.TOP_CLOTHES_ID.equals(key)) {
            bodyGroup.removeTopClothes();
        } else if (UserPreferencesKeys.BOTTOM_CLOTHES_ID.equals(key)) {
            bodyGroup.removeBottomClothes();
        } else if (UserPreferencesKeys.FOOTWEAR_ID.equals(key)) {
            bodyGroup.removeFootwearClothes();
        }
    }

    private enum FacePart {
        EYEBROW, EYE, NOSE, MOUTH, HAIR
    }

    private enum SetupStep {
        CHARACTER_SEX_SELECTION, FACE_COLOR_SELECTION, FACIAL_FEATURE_SELECTION, CLOTHES_SELECTION
    }

    /**
     * Which is mainly used to store the object position in the image
     */
    private static class ImagePosition {


        static final IconPosition FACIAL_FEATURE_BORDER_POSITION_TO_IMAGE = new IconPosition(10, 10, 900, 100);

        static final IconPosition CLOTHES_BORDER_POSITION_TO_IMAGE = new IconPosition(10, 150, 1000, 200);

        static final IconPosition TRASH_ICON_POSITION_TO_IMAGE = new IconPosition(0, 300, 100, 100);

        private static final float CHARACTER_WIDTH = 251;
        private static final float CHARACTER_HEIGHT = 595;
        private static final float CHARACTER_Y_POSITION_IN_IMAGE = 1253;
        static final IconPosition BOY_CHARACTER_POSITION = new IconPosition(4, CHARACTER_Y_POSITION_IN_IMAGE, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        static final IconPosition GIRL_CHARACTER_POSITION = new IconPosition(274, CHARACTER_Y_POSITION_IN_IMAGE, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        static final IconPosition HIGHLIGHTED_BOY_CHARACTER_POSITION = new IconPosition(574, CHARACTER_Y_POSITION_IN_IMAGE, CHARACTER_WIDTH, CHARACTER_HEIGHT);
        static final IconPosition HIGHLIGHTED_GIRL_CHARACTER_POSITION = new IconPosition(851, CHARACTER_Y_POSITION_IN_IMAGE, CHARACTER_WIDTH, CHARACTER_HEIGHT);
    }
}
