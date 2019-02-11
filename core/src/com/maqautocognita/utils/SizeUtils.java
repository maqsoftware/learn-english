package com.maqautocognita.utils;

import com.badlogic.gdx.graphics.Texture;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public final class SizeUtils {

    public static float[] getExpectSize(String picturePath, float maxWidth, float maxHeight) {

        float size[] = new float[]{maxWidth, maxHeight};

        AssetManagerUtils.loadTexture(picturePath);

        AssetManagerUtils.blockUtilsFinishLoading();

        Texture texture = AssetManagerUtils.getTexture(picturePath);

        if (null != texture) {
            float ratio = 0;

            if (texture.getWidth() > texture.getHeight()) {
                ratio = maxWidth / texture.getWidth();
            } else {
                ratio = maxHeight / texture.getHeight();
            }

            size[0] = texture.getWidth() * ratio;
            size[1] = texture.getHeight() * ratio;
        }

        return size;
    }
}
