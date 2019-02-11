package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.scene2d.actors.AdvanceImage;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.RandomUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class ShapePracticeSection extends AbstractMathSection {

    private static int NUMBER_OF_SHAPE = 4;
    private static int SPACE_BETWEEN_SHARP = 20;
    private static int START_X_POSITION = 20;
    private final Shape playShape;
    private Rectangle shapes[];
    private List<AdvanceImage<Shape>> shapeImageActorList;

    public ShapePracticeSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener, Shape playShape) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.playShape = playShape;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        initShape();
    }

    @Override
    protected void render() {
        super.render();
        if (null == shapes && CollectionUtils.isNotEmpty(shapeImageActorList)) {
            shapes = new Rectangle[shapeImageActorList.size()];
            float startXPosition = START_X_POSITION;
            float startYPosition = MenuSection.MenuItemEnum.HELP.iconPosition.y + MenuSection.MenuItemEnum.HELP.iconPosition.height;
            float minimumHeight = 999;
            for (int i = 0; i < shapeImageActorList.size(); i++) {
                AdvanceImage imageActor = shapeImageActorList.get(i);
                if (startXPosition + imageActor.getWidth() > Config.TABLET_SCREEN_WIDTH) {
                    //start a new row
                    startXPosition = START_X_POSITION;
                    startYPosition += minimumHeight + SPACE_BETWEEN_SHARP;
                }
                minimumHeight = Math.min(minimumHeight, imageActor.getHeight());

                float imageStartYPosition = startYPosition;
                while (true) {
                    if (RandomUtils.isOverlapped(startXPosition, imageStartYPosition, imageActor.getWidth(), imageActor.getHeight(), shapes)) {
                        imageStartYPosition += SPACE_BETWEEN_SHARP;
                    } else {
                        imageStartYPosition += SPACE_BETWEEN_SHARP;
                        imageActor.setPosition(startXPosition, imageStartYPosition);
                        shapes[i] = new Rectangle(startXPosition, imageStartYPosition, imageActor.getWidth(), imageActor.getHeight());
                        startXPosition += imageActor.getWidth() + SPACE_BETWEEN_SHARP;
                        break;
                    }
                }


            }


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

    private void initShape() {

        if (null != shapeImageActorList) {
            shapeImageActorList.clear();
        }

        addShape(Shape.CIRCLE, ShapeCircleStaticSection.IMAGE_PATH);
        addShape(Shape.TRIANGLE, ShapeTriangleStaticSection.IMAGE_PATH);
        addShape(Shape.RECTANGLE, ShapeRectangleStaticSection.IMAGE_PATH);

        Collections.shuffle(shapeImageActorList);
        for (final AdvanceImage<Shape> shapeImageActor : shapeImageActorList) {
            stage.addActor(shapeImageActor);
        }

        shapes = null;
    }

    private void addShape(Shape shape, String[] shapePaths) {
        List<String> shapePathList = new ArrayList<String>(shapePaths.length);
        for (String shapePath : shapePaths) {
            shapePathList.add(shapePath);
        }

        Collections.shuffle(shapePathList);
        for (int i = 0; i < NUMBER_OF_SHAPE && NUMBER_OF_SHAPE < shapePathList.size(); i++) {
            if (null == shapeImageActorList) {
                shapeImageActorList = new ArrayList<AdvanceImage<Shape>>();
            }
            final AdvanceImage<Shape> imageActor = new AdvanceImage(shape, shapePathList.get(i));

            imageActor.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    if (playShape.equals(imageActor.getId())) {
                        imageActor.setTouchable(Touchable.disabled);
                        AlphaAction actionFadeOut = new AlphaAction();
                        actionFadeOut.setAlpha(0f);
                        actionFadeOut.setDuration(1f);
                        SequenceAction sequenceAction = new SequenceAction();
                        sequenceAction.addAction(actionFadeOut);
                        if (isAllPlayed()) {
                            RunnableAction runnableAction = new RunnableAction();
                            runnableAction.setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                                }
                            });
                            sequenceAction.addAction(runnableAction);
                        }
                        imageActor.addAction(sequenceAction);
                    }
                }
            });


            shapeImageActorList.add(imageActor);
        }
    }

    private boolean isAllPlayed() {
        if (CollectionUtils.isNotEmpty(shapeImageActorList)) {
            for (AdvanceImage<Shape> shape : shapeImageActorList) {
                if (shape.getId().equals(playShape) && shape.isTouchable()) {
                    return false;
                }
            }
        }
        return true;
    }

    public enum Shape {
        CIRCLE, TRIANGLE, RECTANGLE;
    }

}
