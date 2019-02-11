package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.ColorProperties;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ImageWithBorder extends Image {

    private static final int BORDER_WIDTH = 10;

    private ShapeRenderer shapeRenderer;

    ImageWithBorder(Texture texture) {
        super(texture);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (null == shapeRenderer) {
            shapeRenderer = new ShapeRenderer();
        }
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(getX() - BORDER_WIDTH, getY() - BORDER_WIDTH, getWidth() + BORDER_WIDTH * 2, getHeight() + BORDER_WIDTH * 2);
        shapeRenderer.setColor(ColorProperties.DISABLE_TEXT);
        shapeRenderer.end();
        batch.begin();

        super.draw(batch, parentAlpha);
    }
}
