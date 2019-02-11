package com.maqautocognita.bo;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DragObject<V> {

    private IconPosition originalPosition;

    private Vector2 draggingPosition;

    private AutoCognitaTextureRegion autoCognitaTextureRegion;

    private V value;

    private boolean visible = true;

    private boolean highlighted;

    public IconPosition getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(IconPosition originalPosition) {
        this.originalPosition = originalPosition;
    }

    public Vector2 getDraggingPosition() {
        return draggingPosition;
    }

    public void setDraggingPosition(Vector2 draggingPosition) {
        this.draggingPosition = draggingPosition;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public AutoCognitaTextureRegion getAutoCognitaTextureRegion() {
        return autoCognitaTextureRegion;
    }

    public void setAutoCognitaTextureRegion(AutoCognitaTextureRegion autoCognitaTextureRegion) {
        this.autoCognitaTextureRegion = autoCognitaTextureRegion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
