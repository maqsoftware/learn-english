package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingStoryForPlayingBackgroundSection extends CountingStorySection {

    public CountingStoryForPlayingBackgroundSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int minimumNumberOfObjectRequiredToDrop, int maximumNumberOfObjectRequiredToDrop, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, minimumNumberOfObjectRequiredToDrop, maximumNumberOfObjectRequiredToDrop, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected String[] getAllRequiredTextureNameForStoryMode() {
        return new String[]{MathImagePathUtils.COUNTING_BACKGROUND_PLAYGROUND_IMAGE_PATH,
                MathImagePathUtils.BALL, MathImagePathUtils.BICYCLE, MathImagePathUtils.MONKEY};
    }

    protected Texture getBackgroundTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.COUNTING_BACKGROUND_PLAYGROUND_IMAGE_PATH);
    }


    @Override
    protected Texture getFirstObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.MONKEY);
    }

    @Override
    protected Texture getSecondObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.BICYCLE);
    }

    @Override
    protected Texture getThirdObjectTexture() {
        return AssetManagerUtils.getTexture(MathImagePathUtils.BALL);
    }

    @Override
    protected Rectangle getFirstObjectAllowDropArea() {
        return new Rectangle(backgroundScreenObject.xPositionInScreen, backgroundScreenObject.yPositionInScreen, backgroundScreenObject.width, backgroundScreenObject.height);
    }

    @Override
    protected Rectangle getSecondObjectAllowDropArea() {
        return getFirstObjectAllowDropArea();
    }

    @Override
    protected Rectangle getThirdObjectAllowDropArea() {
        return getFirstObjectAllowDropArea();
    }

    protected int getAudioIndexForTheDropObject(int objectId) {
        if (getFirstAllowDragObjectId() == objectId) {
            return 2;
        } else if (getSecondAllowDragObjectId() == objectId) {
            return 3;
        } else if (getThirdAllowDragObjectId() == objectId) {
            return 4;
        }
        return 0;
    }

    protected String getPutObjectAudioNameForSwahili(int objectIndex) {
        if (2 == objectIndex) {
            return "ctst2_i1_1";
        } else if (3 == objectIndex) {
            return "ctst2_i1_2";
        } else if (4 == objectIndex) {
            return "ctst2_i1_3";
        }

        return null;
    }

    protected String getPutObjectDestinationAudioNameForSwahili(int objectIndex) {
        if (2 == objectIndex) {
            return "ctst2_i1_5";
        } else if (3 == objectIndex) {
            return "ctst2_i1_6";
        } else if (4 == objectIndex) {
            return "ctst2_i1_7";
        }

        return null;
    }
}
