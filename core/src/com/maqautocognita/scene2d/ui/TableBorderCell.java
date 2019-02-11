package com.maqautocognita.scene2d.ui;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class TableBorderCell extends TableCell {

    private ShapeRenderer shapeRenderer;

    private int rowIndex;

    public TableBorderCell(String text, int width, int height) {
        super(text, width, height, TextFontSizeEnum.FONT_72, Align.center);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        if (null == shapeRenderer) {
            shapeRenderer = new ShapeRenderer();
        }
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(highlighted ? ColorProperties.HIGHLIGHT : ColorProperties.TABLE_HEADER_TEXT);
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        batch.begin();
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

}
