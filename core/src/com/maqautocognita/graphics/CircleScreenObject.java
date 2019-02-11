package com.maqautocognita.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CircleScreenObject<ID> extends ScreenObject<ID, Object> {


    private Texture circle;

    public CircleScreenObject(ID id, float x, float y, int radius, Color color, int borderWidth) {
        this.id = id;
        this.xPositionInScreen = x;
        this.yPositionInScreen = y;
        this.width = radius * 2;
        this.height = radius * 2;

        Pixmap pixmap = getPixmapRoundedRectangle(radius, color, borderWidth);
        circle = new Texture(pixmap);
        circle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();

    }

    private Pixmap getPixmapRoundedRectangle(int radius, Color color, int borderWidth) {

        int size = radius * 2;

        Pixmap pixmap = new Pixmap(size + 1, size + 1, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(color);

        pixmap.fillCircle(radius, radius, radius);
        if (borderWidth > 0) {
            pixmap.setColor(Color.WHITE);
            pixmap.fillCircle(radius, radius, radius - borderWidth);
        }

        return pixmap;
    }

    @Override
    public void onDraw(Batch batch) {

        batch.draw(circle, xPositionInScreen, yPositionInScreen);

    }
}
