package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.maqautocognita.bo.storyMode.Clothes;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * This is mainly used to draw the clothes in the character {@link BodyGroup}
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CharacterClothesActor extends ImageActor {

    private static final String CLOTHES_IMAGE_FILE_PATH = Config.CLOTHES_IMAGE_FOLDER_NAME + "/";

    private Clothes clothes;

    private String rearImagePath;
    private AutoCognitaTextureRegion rearImageAutoCognitaTextureRegion;

    private boolean isRearImageRequiredToDraw;

    public CharacterClothesActor(Clothes clothes) {
        this();
        setClothes(clothes);
    }

    public CharacterClothesActor() {
        this(true);
    }


    public CharacterClothesActor(boolean isRearImageRequiredToDraw) {
        super("");
        this.isRearImageRequiredToDraw = isRearImageRequiredToDraw;
    }

    private String getImagePathByImageName(String imageName) {
        if (StringUtils.isNotBlank(imageName)) {
            return CLOTHES_IMAGE_FILE_PATH + imageName + ".png";
        }
        return null;
    }

    private void reloadSize() {
        setSize(clothes.imageWidth, clothes.imageHeight);
    }

    public Clothes getClothes() {
        return clothes;
    }

    public void setClothes(Clothes clothes) {
        this.clothes = clothes;

        if (StringUtils.isNotBlank(clothes.frontImageName) && StringUtils.isNotBlank(clothes.rearImageName)) {
            setImagePath(getImagePathByImageName(clothes.frontImageName));
            rearImagePath = getImagePathByImageName(clothes.rearImageName);
            if (isRearImageRequiredToDraw) {
                AssetManagerUtils.loadTexture(rearImagePath);
            }
        } else {
            setImagePath(getImagePathByImageName(clothes.imageName));
            rearImagePath = null;
            rearImageAutoCognitaTextureRegion = null;
        }
        reloadSize();
    }

    public String getRearImagePath() {
        return rearImagePath;
    }

    @Override
    protected void drawActor(Batch batch) {

        if (isRearImageRequiredToDraw) {
            if (StringUtils.isNotBlank(rearImagePath) && null == rearImageAutoCognitaTextureRegion) {
                //if there is rear image and not loaded to texture yet
                if (null == AssetManagerUtils.getTexture(rearImagePath)) {
                    AssetManagerUtils.isFinishLoading();
                } else {
                    rearImageAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(rearImagePath));

                }
            }

            if (null != rearImageAutoCognitaTextureRegion) {
                draw(batch, rearImageAutoCognitaTextureRegion);
            }
        }


        super.drawActor(batch);
    }
}
