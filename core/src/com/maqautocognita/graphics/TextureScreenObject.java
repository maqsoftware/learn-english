package com.maqautocognita.graphics;

import com.maqautocognita.Config;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TextureScreenObject<ID, T> extends ScreenObject<ID, T> {

    /**
     * It is mainly used to store the textureFileName, and the {@link #texture} will be using the given fileName
     */
    public String textureFileName;
    public AutoCognitaTextureRegion textureRegion;
    public Texture textureInHighlightState;
    public AutoCognitaTextureRegion textureRegionInHighlightState;
    public AutoCognitaTextureRegion textureRegionInDisableState;
    public Texture textureInDisableState;
    public IconPosition iconPosition;
    /**
     * This is used to indicate the alpha of this screen object when showing on the screen
     */
    protected float alpha = 1f;
    private float textureTimes = Config.DEFAULT_IMAGE_SIZE_TIMES;
    private Texture texture;
    private Sprite sprite;

    public TextureScreenObject(Texture texture, float xPositionInScreen, float yPositionInScreen) {
        this(texture, xPositionInScreen, yPositionInScreen, texture.getWidth(), texture.getHeight());
    }

    public TextureScreenObject(Texture texture, float xPositionInScreen, float yPositionInScreen, float width, float height) {
        this.width = width;
        this.height = height;
        this.xPositionInScreen = xPositionInScreen;
        this.yPositionInScreen = yPositionInScreen;
        this.texture = texture;
        originalPosition = new Vector2(xPositionInScreen, yPositionInScreen);
    }

    public TextureScreenObject(ID id, Texture texture, float xPositionInScreen, float yPositionInScreen) {
        this(texture, xPositionInScreen, yPositionInScreen, texture.getWidth(), texture.getHeight());
        this.id = id;
    }

    public TextureScreenObject(ID id, T objectType, Texture texture, float xPositionInScreen, float yPositionInScreen, float width, float height) {
        this(texture, xPositionInScreen, yPositionInScreen, width, height);
        this.id = id;
        this.objectType = objectType;
    }

    public TextureScreenObject(ID id, T objectType, IconPosition iconPosition, float xPositionInScreen, float yPositionInScreen, Texture texture, int textureTimes) {
        this(iconPosition, xPositionInScreen, yPositionInScreen, texture);
        this.id = id;
        this.objectType = objectType;
        this.textureTimes = textureTimes;

    }

    public TextureScreenObject(IconPosition iconPosition, float xPositionInScreen, float yPositionInScreen, Texture texture) {
        this(texture, xPositionInScreen, yPositionInScreen, 0, 0);
        if (null != iconPosition) {
            this.iconPosition = iconPosition;
            this.width = iconPosition.width;
            this.height = iconPosition.height;
        }
    }

    public TextureScreenObject(ID id, T objectType, IconPosition iconPosition, float xPositionInScreen, float yPositionInScreen, Texture texture, Texture textureInHighlightState) {
        this(id, objectType, iconPosition, xPositionInScreen, yPositionInScreen, texture);
        this.textureInHighlightState = textureInHighlightState;
    }


    public TextureScreenObject(ID id, T objectType, IconPosition iconPosition, float xPositionInScreen, float yPositionInScreen, Texture texture) {
        this(iconPosition, xPositionInScreen, yPositionInScreen, texture);
        this.id = id;
        this.objectType = objectType;

    }


    public TextureScreenObject(ID id, T objectType, float xPositionInScreen, float yPositionInScreen, float width, float height, String textureFileName) {
        this(id, objectType, null, xPositionInScreen, yPositionInScreen, null);
        this.textureFileName = textureFileName;
        AssetManagerUtils.loadTexture(textureFileName);
        this.width = width;
        this.height = height;
    }

    public TextureScreenObject(ID id, T objectType, float xPositionInScreen, float yPositionInScreen, float width, float height, Texture texture) {
        this(id, objectType, null, xPositionInScreen, yPositionInScreen, texture);
        this.width = width;
        this.height = height;
    }


    public TextureScreenObject(float xPositionInScreen, float yPositionInScreen, AutoCognitaTextureRegion autoCognitaTextureRegion) {
        this(null, xPositionInScreen, yPositionInScreen, autoCognitaTextureRegion, null);
    }

    public TextureScreenObject(ID id, float xPositionInScreen, float yPositionInScreen, AutoCognitaTextureRegion autoCognitaTextureRegion, AutoCognitaTextureRegion autoCognitaTextureRegionInDisabledState) {
        this(id, null, null, xPositionInScreen, yPositionInScreen, null);
        this.textureRegion = autoCognitaTextureRegion;
        this.textureRegionInDisableState = autoCognitaTextureRegionInDisabledState;
        if (null != textureRegion) {
            this.width = textureRegion.getRegionWidth();
            this.height = textureRegion.getRegionHeight();
        }
    }

    public TextureScreenObject(ID id, T objectType, float xPositionInScreen, float yPositionInScreen, AutoCognitaTextureRegion autoCognitaTextureRegion,
                               AutoCognitaTextureRegion textureRegionInHighlightState, AutoCognitaTextureRegion textureRegionInDisabledState) {
        this(id, objectType, xPositionInScreen, yPositionInScreen, autoCognitaTextureRegion, textureRegionInHighlightState);
        this.textureRegionInDisableState = textureRegionInDisabledState;
    }

    public TextureScreenObject(ID id, T objectType, float xPositionInScreen, float yPositionInScreen, AutoCognitaTextureRegion autoCognitaTextureRegion,
                               AutoCognitaTextureRegion textureRegionInHighlightState) {
        this(id, objectType, null, xPositionInScreen, yPositionInScreen, null);
        this.textureRegion = autoCognitaTextureRegion;
        this.textureRegionInHighlightState = textureRegionInHighlightState;
        if (null != textureRegion) {
            this.width = textureRegion.getRegionWidth();
            this.height = textureRegion.getRegionHeight();
        }
    }

    public TextureScreenObject(float xPositionInScreen, float yPositionInScreen,
                               AutoCognitaTextureRegion autoCognitaTextureRegion,
                               AutoCognitaTextureRegion textureRegionInHighlightState, AutoCognitaTextureRegion textureRegionInDisabledState) {
        this(null, null, xPositionInScreen, yPositionInScreen, autoCognitaTextureRegion, textureRegionInHighlightState);
        this.textureRegionInDisableState = textureRegionInDisabledState;
    }

    public Sprite getSprite() {
        if (null == sprite) {
            if (null != getTexture()) {
                sprite = new Sprite(texture);
            } else if (null != getTextureRegion()) {
                sprite = new Sprite(getTextureRegion());
            }

            if (null != sprite) {
                sprite.setSize(width, height);
                sprite.setPosition(xPositionInScreen, yPositionInScreen);
            }
        }

        return sprite;
    }

    public Texture getTexture() {
        if (null == texture) {
            if (null != textureFileName) {
                texture = AssetManagerUtils.getTexture(textureFileName);
            }
        }
        return texture;
    }

    public AutoCognitaTextureRegion getTextureRegion() {
        if (null == textureRegion) {
            if (null != iconPosition && null != texture) {
                textureRegion = new AutoCognitaTextureRegion(texture, iconPosition, textureTimes);
            }
        }
        return textureRegion;
    }

    public void resetTextureRegion(IconPosition iconPosition) {
        textureRegion = new AutoCognitaTextureRegion(texture, iconPosition, textureTimes);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void onDraw(Batch batch) {
        Texture texture;

        TextureRegion textureRegion;

        if (isDisabled) {
            textureRegion = getTextureRegionInDisableState();
            texture = textureInDisableState;
        } else if (isHighlighted) {
            textureRegion = getTextureRegionInHighlightState();
            texture = textureInHighlightState;
        } else {
            textureRegion = getTextureRegion();
            texture = getTexture();
        }

        if (1 > alpha) {
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        }

        if (null != textureRegion) {
            batch.draw(textureRegion, xPositionInScreen, yPositionInScreen);
        } else if (null != texture) {
            batch.draw(texture,
                    xPositionInScreen, yPositionInScreen, width, height);
        }

        if (1 > alpha) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1f);
        }

    }

    public AutoCognitaTextureRegion getTextureRegionInDisableState() {
        if (null == textureRegionInDisableState) {
            if (null != iconPosition && null != textureInDisableState) {
                textureRegionInDisableState = new AutoCognitaTextureRegion(textureInDisableState, (int) iconPosition.x, (int) iconPosition.y, (int) iconPosition.width, (int) iconPosition.height, textureTimes);
            }
        }
        return textureRegionInDisableState;
    }

    public AutoCognitaTextureRegion getTextureRegionInHighlightState() {
        if (null == textureRegionInHighlightState) {
            if (null != iconPosition && null != textureInHighlightState) {
                textureRegionInHighlightState = new AutoCognitaTextureRegion(textureInHighlightState, (int) iconPosition.x, (int) iconPosition.y, (int) iconPosition.width, (int) iconPosition.height, textureTimes);
            }
        }
        return textureRegionInHighlightState;
    }
}
