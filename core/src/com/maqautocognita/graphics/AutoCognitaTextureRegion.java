package com.maqautocognita.graphics;

import com.maqautocognita.Config;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AutoCognitaTextureRegion extends TextureRegion {

    public AutoCognitaTextureRegion(Texture texture) {
        super(texture);
    }


    public AutoCognitaTextureRegion(Texture texture, IconPosition iconPosition) {
        this(texture, iconPosition.x, iconPosition.y, iconPosition.width, iconPosition.height);
    }

    public AutoCognitaTextureRegion(Texture texture, float x, float y, float width, float height) {
        this(texture, x, y, width, height, Config.DEFAULT_IMAGE_SIZE_TIMES);
    }

    public AutoCognitaTextureRegion(Texture texture, float x, float y, float width, float height, float times) {
        super(texture, Math.round(x * times), Math.round(y * times), Math.round(width * times), Math.round(height * times));
    }

    public AutoCognitaTextureRegion(Texture texture, IconPosition iconPosition, float times) {
        this(texture, iconPosition.x, iconPosition.y, iconPosition.width, iconPosition.height, times);
    }
}
