package com.maqautocognita.utils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class IconPosition {
    public float x;
    public float y;
    public float width;
    public float height;

    public IconPosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updatePosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "IconPosition{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
