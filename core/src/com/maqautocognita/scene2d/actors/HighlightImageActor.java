package com.maqautocognita.scene2d.actors;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * It is used to listen when the user is tap the object, it will render another image which should be the highligthed version of the parent image.
 * <p/>
 * And it also provide a method {@link #setHighlighted(boolean)} to change the highlight state of the image
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class HighlightImageActor<T> extends ImageActor<T> {

    private String highlightImagePath;

    private IconPosition highLightImageIconPosition;

    private boolean isHighlighted;

    private AutoCognitaTextureRegion autoCognitaTextureRegion;

    private IActionListener actionListener;

    public HighlightImageActor(T id,
                               String highlightImagePath, IconPosition highLightImageIconPosition,
                               String imagePath, IconPosition iconPosition) {
        this(highlightImagePath, highLightImageIconPosition, imagePath, iconPosition, 0, 0);
        setId(id);
    }

    public HighlightImageActor(
            String highlightImagePath, IconPosition highLightImageIconPosition,
            String imagePath, IconPosition iconPosition, float screenX, float screenY) {
        super(imagePath, iconPosition, screenX, screenY);

        this.highLightImageIconPosition = highLightImageIconPosition;

        this.highlightImagePath = highlightImagePath;

        loadImage(highlightImagePath);

        addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {
                isHighlighted = true;
                if (null != actionListener) {
                    actionListener.onComplete();
                }
            }

        });
    }

    public HighlightImageActor(T id,
                               String highlightImagePath, IconPosition highLightImageIconPosition,
                               String imagePath, IconPosition iconPosition, float screenX, float screenY) {
        this(highlightImagePath, highLightImageIconPosition, imagePath, iconPosition, screenX, screenY);
        setId(id);
    }

    /**
     * the given listener will be used to listen if the image is taped
     *
     * @param actionListener
     */
    public void setSingleTapListener(IActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public AutoCognitaTextureRegion getAutoCognitaTextureRegion() {
        if (isHighlighted) {
            if (null == autoCognitaTextureRegion) {
                if (null != AssetManagerUtils.getTexture(highlightImagePath)) {
                    autoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(highlightImagePath),
                            highLightImageIconPosition);
                }
            }

            return autoCognitaTextureRegion;
        }

        return super.getAutoCognitaTextureRegion();

    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != autoCognitaTextureRegion) {
            autoCognitaTextureRegion = null;
            AssetManagerUtils.unloadTexture(highlightImagePath);

        }
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }
}
