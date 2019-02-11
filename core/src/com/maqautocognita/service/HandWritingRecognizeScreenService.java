package com.maqautocognita.service;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthTextAreaUI;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class HandWritingRecognizeScreenService {

    /**
     * define the time to check if the drawing alphabet is correct, the time will be start to calculate when the user touch up the screen
     */
    private static final int DEFAULT_CHECK_ALPHABET_DRAWING_STOP_TIME_IN_SECOND = 1;

    private final IHandWritingRecognizeListener handWritingRecognizeListener;
    /**
     * It is mainly used to store the correct draw points, example: if a is drawn correct, the drawing points "a" will be store in the list and keep on the screen, the save action will be depends on the method
     * {@link IHandWritingRecognizeListener#isSaveCorrectDrawPointsRequired()}
     */
    protected List<List<List<Vector3>>> correctDrawPointsList;

    /**
     * This is mainly used to count the time after touch up, if the time is over {@link #DEFAULT_CHECK_ALPHABET_DRAWING_STOP_TIME_IN_SECOND},
     * it will start to check if the user writing letter {@link IHandWritingRecognizeListener#isWriteCorrect}
     */
    private Timer timer;
    /**
     * It is used to draw the line which user has touch the screen
     */
    private ShapeRenderer drawLine;

    private List<List<Vector3>> drawPoints;

    private int checkAlphabetDrawingStopTimeInSecond = DEFAULT_CHECK_ALPHABET_DRAWING_STOP_TIME_IN_SECOND;

    public HandWritingRecognizeScreenService(IHandWritingRecognizeListener handWritingRecognizeListener) {
        this.handWritingRecognizeListener = handWritingRecognizeListener;
    }

    public void setDefaultCheckAlphabetDrawingStopTimeInSecond(int checkAlphabetDrawingStopTimeInSecond) {
        this.checkAlphabetDrawingStopTimeInSecond = checkAlphabetDrawingStopTimeInSecond;
    }

    public void drawLine() {

        if (CollectionUtils.isNotEmpty(drawPoints) || CollectionUtils.isNotEmpty(correctDrawPointsList)) {

            drawExtraLine(drawPoints);

            if (CollectionUtils.isNotEmpty(correctDrawPointsList)) {
                for (List<List<Vector3>> points : correctDrawPointsList) {
                    drawExtraLine(points);
                }
            }
        }

    }

    public void drawExtraLine(List<List<Vector3>> drawPoints) {
        getDrawLine().begin(ShapeRenderer.ShapeType.Filled);

        if (CollectionUtils.isNotEmpty(drawPoints)) {
            drawPoint(drawPoints);
        }

        getDrawLine().end();
    }

    private ShapeRenderer getDrawLine() {
        if (null == drawLine) {
            drawLine = new ShapeRenderer();
            drawLine.setColor(Color.RED);
            drawLine.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        }
        return drawLine;
    }

    private void drawPoint(List<List<Vector3>> points) {
        if (null != points) {
            for (List<Vector3> drawPoint : points) {
                if (drawPoint.size() > 1) {
                    for (int i = 0; i < drawPoint.size() - 1; i++) {
                        getDrawLine().rectLine(ScreenUtils.toViewPosition(drawPoint.get(i).x),
                                ScreenUtils.getExactYPositionOnScreen(drawPoint.get(i).y),
                                ScreenUtils.toViewPosition(drawPoint.get(i + 1).x),
                                ScreenUtils.getExactYPositionOnScreen(drawPoint.get(i + 1).y), 5);
                    }
                }
            }
        }
    }

    public void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (handWritingRecognizeListener.isDrawAllow(screenX, screenY)) {
            if (null != timer) {
                timer.clear();
            }

            if (null == drawPoints) {
                drawPoints = new ArrayList<List<Vector3>>();
            }

            drawPoints.add(new ArrayList<Vector3>());

            addDrawPoint(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }


    }

    private void addDrawPoint(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        addDrawPoint(drawPoints, screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    public void addDrawPoint(List<List<Vector3>> drawPoints, int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != drawPoints) {
            drawPoints.get(drawPoints.size() - 1).add(new Vector3(systemDetectXPosition, systemDetectYPosition, 0));
            handWritingRecognizeListener.afterDrawPointAdded(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
            if (null != AutoCognitaGame.handWritingRecognizeService) {
                AutoCognitaGame.handWritingRecognizeService.addPoint(systemDetectXPosition,
                        systemDetectYPosition);
            }
        }
    }

    public void touchUp() {
        if (null == timer) {
            timer = new Timer();
        } else {
            timer.stop();
        }

        if (null != AutoCognitaGame.handWritingRecognizeService) {
            Timer.schedule(new Timer.Task() {

                @Override
                public void run() {
                    if (null != drawPoints) {

                        if (handWritingRecognizeListener.isWriteCorrect()) {
                            handWritingRecognizeListener.whenCorrectLetterWrite();
                            if (handWritingRecognizeListener.isSaveCorrectDrawPointsRequired()) {
                                if (null == correctDrawPointsList) {
                                    correctDrawPointsList = new ArrayList<List<List<Vector3>>>();
                                }
                                correctDrawPointsList.add(drawPoints);
                            }

                        } else {
                            handWritingRecognizeListener.whenLetterWriteFails();
                            handWritingRecognizeListener.whenWrongLetterWrite();
                        }
                    }

                    if (handWritingRecognizeListener.isRequiredClearDrawPointsAfterTimesUp()) {
                        clearDrawPoints();
                    }
                }

            }, checkAlphabetDrawingStopTimeInSecond);
        }
    }

    public void clearDrawPoints() {
        if (null != AutoCognitaGame.handWritingRecognizeService) {
            AutoCognitaGame.handWritingRecognizeService.clearPoints();
        }
        drawPoints = null;
    }

    public void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (handWritingRecognizeListener.isDrawAllow(screenX, screenY)) {
            Timer.instance().clear();
            addDrawPoint(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    public void clearCorrectDrawPoints() {
        correctDrawPointsList = null;
    }

    public interface IHandWritingRecognizeListener {
        boolean isDrawAllow(int screenX, int screenY);

        boolean isSaveCorrectDrawPointsRequired();

        void whenCorrectLetterWrite();

        void whenWrongLetterWrite();

        void whenLetterWriteFails();

        boolean isWriteCorrect();

        void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition);

        boolean isRequiredClearDrawPointsAfterTimesUp();
    }


}
