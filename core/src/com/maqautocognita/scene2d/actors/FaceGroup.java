package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.storyMode.Face;
import com.maqautocognita.bo.storyMode.FacialFeatures;
import com.maqautocognita.bo.storyMode.Hair;
import com.maqautocognita.constant.HairColorEnum;
import com.maqautocognita.screens.storyMode.CharacterSetupScreen;
import com.maqautocognita.service.CharacterService;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.util.List;

/**
 * This group is mainly contain the facial features such as faceImageActor,mouth,nose,eye,eyebrow and hair
 * It will provide a method to add/update the image of the facial features
 * <p/>
 * It will search the character profile {@link UserPreferenceUtils} in the constructor and add the facical features if user has setup the character facical features
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class FaceGroup extends AbstractGroup {

    public static final float FACE_SIZE = 400;
    Face selectedFace;
    private ImageActor frontHairImageActor;
    private ImageActor rearHairImageActor;
    private ImageActor noseImageActor;
    private ImageActor eyeImageActor;
    private ImageActor mouthImageActor;
    private ImageActor eyebrowImageActor;
    private ImageActor faceImageActor;
    private Hair selectedHair;
    private BodyGroup bodyGroup;

    public FaceGroup(float xPositionInScreen, float yPositionInScreen) {

        selectedFace = CharacterService.getInstance().getSelectedFace();

        if (null == selectedFace) {
            selectedFace = CharacterService.getInstance().getDefaultFace();
        }
        changeFace(selectedFace);
        addNose(CharacterService.getInstance().getSelectedNose());
        addEye(CharacterService.getInstance().getSelectedEye());
        addMouth(CharacterService.getInstance().getSelectedMouth());
        addEyeBrow(CharacterService.getInstance().getSelectedEyebrow());
        addHair(CharacterService.getInstance().getSelectedHair());


        setPosition(xPositionInScreen, yPositionInScreen);
        setSize(faceImageActor.getWidth(), faceImageActor.getHeight());
    }

    public void removeHair() {
        removeActor(frontHairImageActor);
        frontHairImageActor = null;
        removeActor(rearHairImageActor);
        rearHairImageActor = null;
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeHair();
        }
    }

    public void removeFace() {
        changeFace(CharacterService.getInstance().getDefaultFace());
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeFace();
        }
    }

    public void removeNose() {
        removeActor(noseImageActor);
        noseImageActor = null;
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeNose();
        }
    }

    public void removeEye() {
        removeActor(eyeImageActor);
        eyeImageActor = null;
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeEye();
        }
    }

    public void removeMouth() {
        removeActor(mouthImageActor);
        mouthImageActor = null;
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeMouth();
        }
    }

    public void removeEyebrow() {
        removeActor(eyebrowImageActor);
        eyebrowImageActor = null;
        if (null != bodyGroup) {
            bodyGroup.faceGroup.removeEyebrow();
        }
    }


    public void changeFace(Face face) {

        selectedFace = face;

        if (null == faceImageActor) {
            faceImageActor = new ImageActor(CharacterSetupScreen.CHARACTER_IMAGE_FILE_NAME,
                    new IconPosition(selectedFace.xPositionToImage,
                            selectedFace.yPositionToImage, FACE_SIZE, FACE_SIZE));
            faceImageActor.setIsRequiredToCheckCameraBeforeDraw(false);
            addActor(faceImageActor);
        } else {
            faceImageActor.setIconPosition(new IconPosition(selectedFace.xPositionToImage, selectedFace.yPositionToImage, FACE_SIZE, FACE_SIZE));
        }
        if (null != bodyGroup) {
            bodyGroup.reloadBodyColor(selectedFace);
            bodyGroup.faceGroup.changeFace(face);
        }
    }

    public Hair changeHairColor(HairColorEnum hairColorEnum) {
        if (null != selectedHair && !hairColorEnum.equals(selectedHair.getColor())) {
            List<Hair> hairList = CharacterService.getInstance().getAllHair();
            if (CollectionUtils.isNotEmpty(hairList)) {
                for (Hair hair : hairList) {
                    if (selectedHair.groupId == hair.groupId && hairColorEnum.equals(hair.getColor())) {
                        addHair(hair);
                        break;
                    }
                }
            }
        }

        if (null != bodyGroup) {
            bodyGroup.faceGroup.changeHairColor(hairColorEnum);
        }

        return selectedHair;
    }

    public void addHair(Hair hair) {
        this.selectedHair = hair;
        if (null != hair) {
            String imagePath = CharacterHairActor.getImagePathByImageName(hair.frontImageName);
            if (StringUtils.isNotBlank(imagePath)) {
                if (null == frontHairImageActor) {
                    frontHairImageActor = new ImageActor(imagePath);
                    frontHairImageActor.setIsRequiredToCheckCameraBeforeDraw(false);
                    addActor(frontHairImageActor);
                } else {
                    frontHairImageActor.setImagePath(imagePath);
                }

                frontHairImageActor.setSize(hair.frontImageWidth * 2, hair.frontImageHeight * 2);
                frontHairImageActor.setPosition(hair.frontHairXPositionToHead * 2, hair.frontHairYPositionToHead * 2);
            }

            imagePath = CharacterHairActor.getImagePathByImageName(hair.rearImageName);

            if (null == rearHairImageActor) {
                if (StringUtils.isNotBlank(imagePath)) {
                    rearHairImageActor = new ImageActor(imagePath);
                    rearHairImageActor.setIsRequiredToCheckCameraBeforeDraw(false);
                    addActorAt(0, rearHairImageActor);
                }
            } else {
                rearHairImageActor.setImagePath(imagePath);
            }
            if (StringUtils.isNotBlank(imagePath)) {
                rearHairImageActor.setSize(hair.rearImageWidth * 2, hair.rearImageHeight * 2);
                rearHairImageActor.setPosition(hair.rearHairXPositionToHead * 2, hair.rearHairYPositionToHead * 2);
            }

            if (null != bodyGroup) {
                bodyGroup.faceGroup.addHair(hair);
            }
        }

    }

    public void addNose(FacialFeatures facialFeatures) {

        if (null == noseImageActor) {
            noseImageActor = new ImageActor(CharacterSetupScreen.FACIAL_FEATURES_IMAGE_FILE_NAME);
            addActor(noseImageActor);
        }

        addFacialFeatures(noseImageActor, facialFeatures);

        if (null != bodyGroup) {
            bodyGroup.faceGroup.addNose(facialFeatures);
        }
    }


    public void addEye(FacialFeatures facialFeatures) {
        if (null == eyeImageActor) {
            eyeImageActor = new ImageActor(CharacterSetupScreen.FACIAL_FEATURES_IMAGE_FILE_NAME);
            addActor(eyeImageActor);
        }

        addFacialFeatures(eyeImageActor, facialFeatures);

        if (null != bodyGroup) {
            bodyGroup.faceGroup.addEye(facialFeatures);
        }
    }

    public void addMouth(FacialFeatures facialFeatures) {

        if (null == mouthImageActor) {
            mouthImageActor = new ImageActor(CharacterSetupScreen.FACIAL_FEATURES_IMAGE_FILE_NAME);
            addActor(mouthImageActor);
        }

        addFacialFeatures(mouthImageActor, facialFeatures);

        if (null != bodyGroup) {
            bodyGroup.faceGroup.addMouth(facialFeatures);
        }
    }

    public void addEyeBrow(FacialFeatures facialFeatures) {

        if (null == eyebrowImageActor) {
            eyebrowImageActor = new ImageActor(CharacterSetupScreen.FACIAL_FEATURES_IMAGE_FILE_NAME);
            addActor(eyebrowImageActor);
        }

        addFacialFeatures(eyebrowImageActor, facialFeatures);

        if (null != bodyGroup) {
            bodyGroup.faceGroup.addEyeBrow(facialFeatures);
        }
    }

    private void addFacialFeatures(ImageActor imageActor, FacialFeatures facialFeatures) {
        if (null != facialFeatures) {
            IconPosition iconPosition = new IconPosition(facialFeatures.xPositionToImage, facialFeatures.yPositionToImage, FaceGroup.FACE_SIZE, FaceGroup.FACE_SIZE);
            imageActor.setIconPosition(iconPosition);
            imageActor.setIsRequiredToCheckCameraBeforeDraw(false);
        }
    }

    public void setBodyGroup(BodyGroup bodyGroup) {
        this.bodyGroup = bodyGroup;
    }

}
