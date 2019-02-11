package com.maqautocognita.scene2d.actors;

import com.maqautocognita.Config;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class BackgroundRepeatActor extends BackgroundActor {

    private TiledDrawable tiledDrawable;

    public BackgroundRepeatActor(String imagePath, float x, float y) {
        super(imagePath, x, y);
        tiledDrawable = new TiledDrawable(objectTextureRegion);
    }


    @Override
    public float getWidthAfterScale() {
        return camera.getWorldWidth();
    }

    @Override
    protected void drawActor(Batch batch, TextureRegion textureRegion, float x, float y, float width, float height) {
        tiledDrawable.draw(batch, x, y, width, height);
    }

    @Override
    public float getHeightAfterScale() {
        return Config.TABLET_SCREEN_HEIGHT;
    }
}
