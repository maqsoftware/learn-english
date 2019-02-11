package com.maqautocognita.scene2d.ui;

import com.maqautocognita.prototype.sentence.SentenceModule;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by siu-chun.chi on 5/29/2017.
 */

public abstract class AbstractSentenceChangeButton extends Image {

    protected final SentenceModule sentenceModule;

    private final Texture texture;

    private final Texture selectedTexture;
    private IActionListener onButtonSelectedListener;
    private boolean isSelected;

    public AbstractSentenceChangeButton(SentenceModule sentenceModule, Texture texture, Texture selectedTexture) {
        super(texture);
        this.texture = texture;
        this.selectedTexture = selectedTexture;
        this.sentenceModule = sentenceModule;
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (!isSelected) {
                    onClick();
                    if (null != onButtonSelectedListener) {
                        onButtonSelectedListener.onComplete();
                    }
                }
            }
        });
    }

    protected abstract void onClick();

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            setDrawable(new TextureRegionDrawable(new TextureRegion(selectedTexture)));
        } else {
            setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        }
    }

    public void setOnButtonSelectedListener(IActionListener onButtonSelectedListener) {
        this.onButtonSelectedListener = onButtonSelectedListener;
    }

}
