package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.listener.AbstractSoundPlayListener;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TextPropertiesUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShapeIntroductionSection extends AbstractMathSection {

    private static final float SPACE_BETWEEN_SHAPE_AND_TEXT = 100;
    private static final float SPACE_BETWEEN_SHAPE = 100;
    private float startPositionX;
    private Image circle;
    private Image triangle;
    private Image rectangle;
    private TextCell circleTextCell;
    private TextCell triangleTextCell;
    private TextCell rectangleTextCell;

    public ShapeIntroductionSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        initShape();
    }

    @Override
    protected void render() {
        super.render();
        if (0 == startPositionX && null != circle) {
            float totalWidth = circle.getWidth() + triangle.getWidth() + rectangle.getWidth() + SPACE_BETWEEN_SHAPE * 2;
            startPositionX = ScreenUtils.getXPositionForCenterObject(totalWidth);

            float maximumHeight = Math.max(circle.getHeight(), Math.max(rectangle.getHeight(), triangle.getHeight()));
            float startYPosition = ScreenUtils.getBottomYPositionForCenterObject(maximumHeight);

            circle.setPosition(startPositionX, startYPosition);
            startPositionX += circle.getWidth() + SPACE_BETWEEN_SHAPE;
            triangle.setPosition(startPositionX, startYPosition);
            startPositionX += triangle.getWidth() + SPACE_BETWEEN_SHAPE;
            rectangle.setPosition(startPositionX, startYPosition);

            circleTextCell = new TextCell(TextPropertiesUtils.getCircle(), TextFontSizeEnum.FONT_72, circle.getWidth(), circle.getX(), circle.getY() - SPACE_BETWEEN_SHAPE_AND_TEXT);
            circleTextCell.setTextFlip(true);
            stage.addActor(circleTextCell);

            triangleTextCell = new TextCell(TextPropertiesUtils.getTriangle(), TextFontSizeEnum.FONT_72, triangle.getWidth(), triangle.getX(), triangle.getY() - SPACE_BETWEEN_SHAPE_AND_TEXT);
            triangleTextCell.setTextFlip(true);
            stage.addActor(triangleTextCell);

            rectangleTextCell = new TextCell(TextPropertiesUtils.getRectangle(), TextFontSizeEnum.FONT_72, rectangle.getWidth(), rectangle.getX(), rectangle.getY() - SPACE_BETWEEN_SHAPE_AND_TEXT);
            rectangleTextCell.setTextFlip(true);
            stage.addActor(rectangleTextCell);

            addTouchEvent(circle, circleTextCell, "word_circle");
            addTouchEvent(triangle, triangleTextCell, "word_triangle");
            addTouchEvent(rectangle, rectangleTextCell, "word_rectangle");
            addTouchEvent(circleTextCell, circleTextCell, "word_circle");
            addTouchEvent(triangleTextCell, triangleTextCell, "word_triangle");
            addTouchEvent(rectangleTextCell, rectangleTextCell, "word_rectangle");
        }
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected boolean isNumberBlocksRequired() {
        return false;
    }

    private void addTouchEvent(Actor actor, final TextCell highlightTextCell, final String audioFileName) {
        actor.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                highlightTextCell.setHighlighted(true);
                abstractAutoCognitaScreen.playSound(audioFileName, new AbstractSoundPlayListener() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        deHighlightTextCell();
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        deHighlightTextCell();
                    }

                    private void deHighlightTextCell() {
                        highlightTextCell.setHighlighted(false);
                    }
                });
            }
        });
    }

    private void initShape() {

        circle = new Image(AssetManagerUtils.getTextureWithWait(MathImagePathUtils.CIRCLE1));
        triangle = new Image(AssetManagerUtils.getTextureWithWait(MathImagePathUtils.TRIANGLE1));
        rectangle = new Image(AssetManagerUtils.getTextureWithWait(MathImagePathUtils.RECTANGLE1));

        stage.addActor(circle);
        stage.addActor(triangle);
        stage.addActor(rectangle);
        startPositionX = 0;
    }
}
