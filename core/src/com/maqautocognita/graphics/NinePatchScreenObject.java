package com.maqautocognita.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class NinePatchScreenObject extends ScreenObject {

    public NinePatchDrawable ninePatch;

    public NinePatchScreenObject(TextureRegion textureRegion, float width, float height, float xPositionInScreen, float yPositionInScreen) {

        NinePatch patch = new NinePatch(textureRegion, 30, 10, 28 + 10, 10);
        ninePatch = new NinePatchDrawable(patch);

        this.xPositionInScreen = xPositionInScreen;
        this.yPositionInScreen = yPositionInScreen;

        this.width = width;// contains the width of the current set text
        this.height = height;

        isTouchAllow = false;
    }


    @Override
    public void onDraw(Batch batch) {
        ninePatch.draw(batch, xPositionInScreen, yPositionInScreen, width, height);
    }
}
