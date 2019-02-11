package com.maqautocognita.scene2d.actors;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.scene2d.actions.IActionListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MessageActor extends Actor {

    private static final TextFontSizeEnum TEXT_FONT_SIZE = TextFontSizeEnum.FONT_72;
    /**
     * The padding of the border in the left right side, the space between the text and the border in left and right side
     */
    private static final float PADDING_LEFT_RIGHT = 50;
    /**
     * The padding of the border in the top bottom side, the space between the text and the border in top and bottom side
     */
    private static final float PADDING_TOP_BOTTOM = 20;

    /**
     * How long will the message show in second
     */
    private static final float SHOW_SECOND = 2;
    //it is mainly used to play the sound
    private final AbstractAutoCognitaScreen abstractAutoCognitaScreen;
    private String text;
    private float startXPosition;
    private float[] textSize;
    private BitmapFont bitmapFont;
    private Color color = Color.WHITE;
    /**
     * It is used to draw the background layer of the text
     */
    private ShapeRenderer shapeRenderer;
    /**
     * store the second displayed for the {@link #text}, if the given animatedSecond is reach {@link #SHOW_SECOND}, the text will be disappear
     */
    private float animatedSecond;
    private IActionListener actionListener;
    private boolean isAudioPlayed;

    public MessageActor(AbstractAutoCognitaScreen abstractAutoCognitaScreen) {
        addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

            }
        });

        this.abstractAutoCognitaScreen = abstractAutoCognitaScreen;
    }


    public void setText(String text, String audioFileName, IActionListener actionListener) {
        this.actionListener = null;
        this.text = text;
        if (StringUtils.isNotBlank(text)) {

            this.actionListener = actionListener;
            textSize = LetterUtils.getSizeOfWord(text, TEXT_FONT_SIZE);
            startXPosition = ScreenUtils.getXPositionForCenterObject(textSize[0]);
            float height = textSize[1] + PADDING_TOP_BOTTOM * 2;
            setSize(textSize[0] + PADDING_LEFT_RIGHT * 2, height + PADDING_TOP_BOTTOM * 2);
            //set the message background position
            setPosition(startXPosition - PADDING_LEFT_RIGHT,
                    PADDING_TOP_BOTTOM);

            if (StringUtils.isNotBlank(audioFileName)) {
                isAudioPlayed = false;
                abstractAutoCognitaScreen.playSound(audioFileName, new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isAudioPlayed = true;
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        isAudioPlayed = true;
                    }
                });
            } else {
                isAudioPlayed = true;
            }


        }
        animatedSecond = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (StringUtils.isNotBlank(text)) {

            batch.end();

            if (null == shapeRenderer) {
                shapeRenderer = new ShapeRenderer();
            }

            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
            shapeRenderer.setColor(ColorProperties.MESSAGE_BACKGROUND);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


            batch.begin();
            if (null == bitmapFont) {
                bitmapFont = FontGeneratorManager.getFont(TEXT_FONT_SIZE);
            }
            bitmapFont.setColor(color);
            bitmapFont.draw(batch, text, startXPosition, getHeight() - PADDING_TOP_BOTTOM);
            batch.end();

            batch.begin();
            batch.setColor(1.0f, 1.0f, 1.0f, 1);


        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (StringUtils.isNotBlank(text) && isAudioPlayed) {
            animatedSecond += delta;
            if (animatedSecond >= SHOW_SECOND) {
                text = null;
                setSize(0, 0);
                animatedSecond = 0;

                if (null != actionListener) {
                    actionListener.onComplete();
                }
            }
        }
    }
}
