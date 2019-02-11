package com.maqautocognita.scene2d.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class AdvanceImage<T> extends Image {

    private final String imagePath;
    private T id;

    public AdvanceImage(T id, String imagePath) {
        super(new Texture(imagePath));
        this.imagePath = imagePath;
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
