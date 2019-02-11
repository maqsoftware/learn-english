package com.maqautocognita.scene2d.actors;

import com.maqautocognita.bo.storyMode.Clothes;
import com.maqautocognita.bo.storyMode.Face;
import com.maqautocognita.screens.storyMode.CharacterSetupScreen;
import com.maqautocognita.service.CharacterService;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * It will auto add the {@link FaceGroup} to the group , and search the character profile {@link UserPreferenceUtils} in the constructor and add the clothing if user has setup the character clothing
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class BodyGroup extends AbstractGroup {

    public static final Vector2 BODY_SIZE = new Vector2(248, 586);
    FaceGroup faceGroup;
    private CharacterClothesActor characterTopClothesActor;
    private CharacterClothesActor characterBottomClothesActor;
    private CharacterClothesActor characterFootwearClothesActor;
    private ImageActor body;


    private ImageActor rearCharacterTopClothesActor;
    private ImageActor rearCharacterBottomClothesActor;
    private ImageActor rearCharacterFootwearClothesActor;

    public BodyGroup(float xPositionInScreen, float yPositionInScreen) {
        setPosition(xPositionInScreen, yPositionInScreen);
        setSize(BODY_SIZE.x, BODY_SIZE.y);

        faceGroup = new FaceGroup(24, BODY_SIZE.y - 200);
        faceGroup.setScale(0.5f);
        faceGroup.setIsRequiredToCheckCameraBeforeDraw(false);

        reloadBodyColor(faceGroup.selectedFace);

        addActor(faceGroup);
        
        addTopClothes(CharacterService.getInstance().getSelectedTopClothes());
        addBottomClothes(CharacterService.getInstance().getSelectedBottomClothes());
        addFootwearClothes(CharacterService.getInstance().getSelectedFootwear());

        faceGroup.toFront();
    }

    void reloadBodyColor(Face selectedFace) {
        if (null != selectedFace) {
            IconPosition bodyIconPosition = new IconPosition(selectedFace.bodyXPositionToImage, selectedFace.bodyYPositionToImage, BODY_SIZE.x, BODY_SIZE.y);
            if (null == body) {
                body = new ImageActor(CharacterSetupScreen.CHARACTER_IMAGE_FILE_NAME, bodyIconPosition);
                body.setIsRequiredToCheckCameraBeforeDraw(false);
                addActor(body);
                body.setZIndex(1);
            } else {
                body.setIconPosition(bodyIconPosition);
            }
        }
    }

    public void addTopClothes(Clothes clothes) {

        if (null == characterTopClothesActor) {
            characterTopClothesActor = new CharacterClothesActor(false);
            addActor(characterTopClothesActor);
        }

        setClothesPosition(characterTopClothesActor, clothes);

        if (StringUtils.isNotBlank(characterTopClothesActor.getRearImagePath())) {
            if (null == rearCharacterTopClothesActor) {
                rearCharacterTopClothesActor = new ImageActor(characterTopClothesActor.getRearImagePath());
                addRearClothes(rearCharacterTopClothesActor, clothes);
            } else {
                rearCharacterTopClothesActor.setImagePath(characterTopClothesActor.getRearImagePath());
            }
        } else {
            removeRearCharacterTopClothesActor();
        }
    }

    public void addBottomClothes(Clothes clothes) {

        if (null == characterBottomClothesActor) {
            characterBottomClothesActor = new CharacterClothesActor(false);
            addActor(characterBottomClothesActor);
        }

        setClothesPosition(characterBottomClothesActor, clothes);

        if (StringUtils.isNotBlank(characterBottomClothesActor.getRearImagePath())) {
            if (null == rearCharacterBottomClothesActor) {
                rearCharacterBottomClothesActor = new ImageActor(characterBottomClothesActor.getRearImagePath());
                addRearClothes(rearCharacterBottomClothesActor, clothes);
            } else {
                rearCharacterBottomClothesActor.setImagePath(characterBottomClothesActor.getRearImagePath());
            }
        } else {
            removeRearCharacterBottomClothesActor();
        }
    }

    public void addFootwearClothes(Clothes clothes) {

        if (null == characterFootwearClothesActor) {
            characterFootwearClothesActor = new CharacterClothesActor(false);
            addActor(characterFootwearClothesActor);
        }

        setClothesPosition(characterFootwearClothesActor, clothes);

        removeRearCharacterFootwearClothesActor();
        if (StringUtils.isNotBlank(characterFootwearClothesActor.getRearImagePath())) {
            if (null == rearCharacterFootwearClothesActor) {
                rearCharacterFootwearClothesActor = new ImageActor(characterFootwearClothesActor.getRearImagePath());
                addRearClothes(rearCharacterFootwearClothesActor, clothes);
            } else {
                rearCharacterFootwearClothesActor.setImagePath(characterFootwearClothesActor.getRearImagePath());
            }

        }
    }

    private void setClothesPosition(CharacterClothesActor characterClothesActor, Clothes clothes) {

        if (null != clothes) {
            characterClothesActor.setZIndex(clothes.level + 1);
            characterClothesActor.setIsRequiredToCheckCameraBeforeDraw(false);
            characterClothesActor.setClothes(clothes);
            characterClothesActor.setPosition(clothes.xPositionToBody,
                    clothes.yPositionToBody);
            faceGroup.toFront();
        }
    }

    private void addRearClothes(ImageActor imageActor, Clothes clothes) {
        imageActor.setSize(clothes.imageWidth, clothes.imageHeight);
        imageActor.setIsRequiredToCheckCameraBeforeDraw(false);
        imageActor.setPosition(clothes.xPositionToBody,
                clothes.yPositionToBody);
        addActor(imageActor);
        imageActor.setZIndex(0);
    }

    private void removeRearCharacterTopClothesActor() {
        if (null != rearCharacterTopClothesActor) {
            rearCharacterTopClothesActor.remove();
            rearCharacterTopClothesActor = null;
        }
    }

    private void removeRearCharacterBottomClothesActor() {
        if (null != rearCharacterBottomClothesActor) {
            rearCharacterBottomClothesActor.remove();
            rearCharacterBottomClothesActor = null;
        }
    }

    private void removeRearCharacterFootwearClothesActor() {
        if (null != rearCharacterFootwearClothesActor) {
            rearCharacterFootwearClothesActor.remove();
            rearCharacterFootwearClothesActor = null;
        }
    }

    public void removeTopClothes() {
        removeActor(characterTopClothesActor);
        characterTopClothesActor = null;
        removeRearCharacterTopClothesActor();
    }

    public void removeBottomClothes() {
        removeActor(characterBottomClothesActor);
        characterBottomClothesActor = null;
        removeRearCharacterBottomClothesActor();
    }

    public void removeFootwearClothes() {
        removeActor(characterFootwearClothesActor);
        characterFootwearClothesActor = null;
        removeRearCharacterFootwearClothesActor();
    }

    public CharacterClothesActor getCharacterBottomClothesActor() {
        return characterBottomClothesActor;
    }

    public CharacterClothesActor getCharacterTopClothesActor() {
        return characterTopClothesActor;
    }

}
