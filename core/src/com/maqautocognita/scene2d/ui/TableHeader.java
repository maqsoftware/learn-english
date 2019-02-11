package com.maqautocognita.scene2d.ui;

import com.maqautocognita.graphics.ColorProperties;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TableHeader extends TableCell {

    private static final int LINE_HEIGHT = 5;
    private final int headerLineStartWidth;
    private ShapeRenderer shapeRenderer;

    public TableHeader(String text, int width, int height, int headerLineStartWidth) {
        super(text, width, height, ColorProperties.TABLE_HEADER_TEXT);
        this.headerLineStartWidth = headerLineStartWidth;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        if (null == shapeRenderer) {
            shapeRenderer = new ShapeRenderer();
        }
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(ColorProperties.TABLE_HEADER_TEXT);
        shapeRenderer.rect(getX() + headerLineStartWidth, getY(), getWidth(), LINE_HEIGHT);
        shapeRenderer.end();

        batch.begin();
    }
}
