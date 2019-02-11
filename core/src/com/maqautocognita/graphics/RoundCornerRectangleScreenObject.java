package com.maqautocognita.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class RoundCornerRectangleScreenObject<ID> extends ScreenObject<ID, Object> {


    private static final int RADIUS = 15;
    private final boolean isRoundCornerRequired;
    private int borderWidth;
    private Color color;
    private Texture rectangle;
    //store the previous status of the highlighted, it is mainly used to prevent init the rectangle continously in the render method
    private boolean previousIsHighLighted;
    /**
     * it will be draw on the top of the @{link #rectangle} in white color, just used to make the effect of show border only, if the caller using the constructor
     * {@link #RoundCornerRectangleScreenObject(float, float, int, int, int)}
     */
    private Texture innerRectangle;

    public RoundCornerRectangleScreenObject(float x, float y, int width, int height, Color color) {
        this(x, y, width, height, color, 0, true);
    }


    public RoundCornerRectangleScreenObject(float x, float y, int width, int height, Color color, int borderWidth, boolean isRoundCornerRequired) {
        this.xPositionInScreen = x;
        this.yPositionInScreen = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderWidth = borderWidth;
        this.isRoundCornerRequired = isRoundCornerRequired;

        Pixmap pixmap = getPixmapRoundedRectangle(width, height, color);
        rectangle = new Texture(pixmap);
        rectangle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();

        if (borderWidth > 0) {
            pixmap = getPixmapRoundedRectangle(width - borderWidth * 2, height - borderWidth * 2, Color.WHITE);
            innerRectangle = new Texture(pixmap);
            innerRectangle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            pixmap.dispose();
        }

    }

    private Pixmap getPixmapRoundedRectangle(int rectangleWidth, int rectangleHeight, Color color) {

        Pixmap pixmap = new Pixmap(rectangleWidth, rectangleHeight, Pixmap.Format.RGBA8888);

        pixmap.setColor(color);

        if (isRoundCornerRequired) {
            // Pink rectangle
            pixmap.fillRectangle(0, RADIUS, pixmap.getWidth(), pixmap.getHeight() - 2 * RADIUS);

            // Green rectangle
            pixmap.fillRectangle(RADIUS, 0, pixmap.getWidth() - 2 * RADIUS, pixmap.getHeight());

            // Bottom-left circle
            pixmap.fillCircle(RADIUS, RADIUS, RADIUS);

            // Top-left circle
            pixmap.fillCircle(RADIUS, pixmap.getHeight() - RADIUS, RADIUS);

            // Bottom-right circle
            pixmap.fillCircle(pixmap.getWidth() - RADIUS, RADIUS, RADIUS);

            // Top-right circle
            pixmap.fillCircle(pixmap.getWidth() - RADIUS, pixmap.getHeight() - RADIUS, RADIUS);
        } else {
            pixmap.fillRectangle(0, 0, rectangleWidth, rectangleHeight);
        }
        return pixmap;
    }

    public RoundCornerRectangleScreenObject(ID id, float x, float y, int width, int height, int borderWidth) {
        this(x, y, width, height, ColorProperties.BORDER, borderWidth, true);
        super.id = id;

    }

    public RoundCornerRectangleScreenObject(float x, float y, int width, int height, int borderWidth) {

        this(x, y, width, height, ColorProperties.BORDER, borderWidth, true);

    }


    public RoundCornerRectangleScreenObject(float x, float y, int width, int height, int borderWidth, Color color, boolean isRoundCornerRequired) {

        this(x, y, width, height, color, borderWidth, isRoundCornerRequired);

    }

    @Override
    public void onDraw(Batch batch) {
        if (isDisabled) {
            redrawRectangle(ColorProperties.DISABLE_TEXT);
        } else {
            if (previousIsHighLighted != isHighlighted) {
                previousIsHighLighted = isHighlighted;
                if (isHighlighted) {
                    redrawRectangle(ColorProperties.HIGHLIGHT);
                } else {
                    redrawRectangle(color);
                }
            }
        }

        batch.draw(rectangle, xPositionInScreen, yPositionInScreen);

        if (null != innerRectangle) {
            batch.draw(innerRectangle, xPositionInScreen + borderWidth, yPositionInScreen + borderWidth);
        }
    }

    private void redrawRectangle(Color color) {
        disposalRectangle();
        Pixmap pixmap = getPixmapRoundedRectangle((int) width, (int) height, color);
        rectangle = new Texture(pixmap);
        pixmap.dispose();
    }

    private void disposalRectangle() {
        if (null != rectangle) {
            rectangle.dispose();
            rectangle = null;
        }
    }

    public void dispose() {
        disposalRectangle();
        disposalInnerRectangle();
    }

    private void disposalInnerRectangle() {
        if (null != innerRectangle) {
            innerRectangle.dispose();
            innerRectangle = null;
        }
    }

}
