package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.bo.storyMode.Hair;
import com.maqautocognita.utils.StringUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CharacterHairActor extends ImageActor {

    private static final String HAIR_STYLE_IMAGE_FILE_PATH = Config.HAIR_STYLE_IMAGE_FOLDER_NAME + "/";

    private final Hair hair;

    public CharacterHairActor(Hair hair) {
        super(getImagePathByImageName(hair.imageName));
        this.hair = hair;
        setWidth(Math.max(hair.frontImageWidth, hair.rearImageWidth));
        setHeight(Math.max(hair.frontImageHeight, hair.rearImageHeight));
    }

    public static final String getImagePathByImageName(String imageName) {
        if (StringUtils.isNotBlank(imageName)) {
            return HAIR_STYLE_IMAGE_FILE_PATH + imageName + ".png";
        }
        return null;
    }

    public Hair getHair() {
        return hair;
    }
}
