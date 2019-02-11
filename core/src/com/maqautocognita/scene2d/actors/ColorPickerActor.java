package com.maqautocognita.scene2d.actors;

import com.maqautocognita.scene2d.actions.IOptionSelectListener;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ColorPickerActor extends AbstractCameraActor {


    private static final int RADIUS = 25;

    private static final int GAP_BETWEEN_CIRCLE = 10;

    private Texture circle;

    private ShapeRenderer sRenderer = new ShapeRenderer();

    private Color[] colors;

    private Rectangle[] colorPosition;

    private IOptionSelectListener<Color> optionSelectListener;

    public ColorPickerActor(float x, float y, Color... colorsArgu) {
        this.colors = colorsArgu;
        setPosition(x, y);

        if (colors.length > 0) {
            reloadColorPosition();
            setSize(colors.length * RADIUS * 2 + (colors.length - 1) * GAP_BETWEEN_CIRCLE, RADIUS * 2);
        }
        addListener(new ActorGestureListener() {

            public void tap(InputEvent event, float x, float y, int count, int button) {
                if (null != optionSelectListener && ArrayUtils.isNotEmpty(colors)) {
                    for (int i = 0; i < colorPosition.length; i++) {
                        if (TouchUtils.isTouched(colorPosition[i], (int) event.getStageX(), (int) event.getStageY())) {
                            optionSelectListener.onTap(colors[i]);
                            break;
                        }
                    }
                }
            }

        });

    }

    private void reloadColorPosition() {
        colorPosition = new Rectangle[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colorPosition[i] = new Rectangle(getX() + i * GAP_BETWEEN_CIRCLE + i * RADIUS * 2, getY(), RADIUS * 2, RADIUS * 2);
        }
    }

    public void setOptionSelectListener(IOptionSelectListener<Color> optionSelectListener) {
        this.optionSelectListener = optionSelectListener;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        sRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        sRenderer.begin(ShapeRenderer.ShapeType.Filled);
        int i = 0;
        for (Color color : colors) {
            if (null != color) {
                sRenderer.setColor(color);
                sRenderer.circle(colorPosition[i].x + RADIUS, colorPosition[i].y + RADIUS, RADIUS, 100);
            }
            i++;
        }
        sRenderer.end();

        batch.begin();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        reloadColorPosition();
    }

    @Override
    public void dispose() {
        if (null != sRenderer) {
            sRenderer.dispose();
        }
    }
}
