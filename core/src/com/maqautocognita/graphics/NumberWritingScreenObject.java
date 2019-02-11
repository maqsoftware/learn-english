package com.maqautocognita.graphics;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sc.chi on 31/7/16.
 */
public class NumberWritingScreenObject extends NumberScreenObject {

    private List<Vector2> drawingPointsForNumber;

    private boolean clearWritingAfterCheckCorrect = true;

    public NumberWritingScreenObject(Integer displayText, float targetWidth, float xPositionInScreen, float yPositionInScreen, TextFontSizeEnum textFontSizeEnum, boolean isNotIncludeHeight) {
        super(displayText, xPositionInScreen, yPositionInScreen, textFontSizeEnum, isNotIncludeHeight);
        setTargetWidth(targetWidth);
    }

    public NumberWritingScreenObject(Integer displayText, float xPositionInScreen, float yPositionInScreen, float width, float height) {
        super();
        super.displayText = displayText;
        super.xPositionInScreen = xPositionInScreen;
        super.yPositionInScreen = yPositionInScreen;
        super.width = width;
        super.height = height;
    }

    public void addDrawingPoints(int touchingXPosition, int touchingYPosition) {


        if (null == drawingPointsForNumber) {
            drawingPointsForNumber = new ArrayList<Vector2>();
        }

        drawingPointsForNumber.add(new Vector2(touchingXPosition, touchingYPosition));
    }

    public void setClearWritingAfterCheckCorrect(boolean clearWritingAfterCheckCorrect) {
        this.clearWritingAfterCheckCorrect = clearWritingAfterCheckCorrect;
    }

    public boolean isNumberWritingCorrect() {
        boolean isCorrect = false;
        if (CollectionUtils.isNotEmpty(drawingPointsForNumber)) {
            AutoCognitaGame.handWritingRecognizeService.clearPoints();
            for (Vector2 vector2 : drawingPointsForNumber) {
                AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
            }

            isCorrect = AutoCognitaGame.handWritingRecognizeService.isCorrect(String.valueOf(displayText));

            if (clearWritingAfterCheckCorrect) {
                clearDrawingPoints();
            }
        }

        return isCorrect;
    }

    public void clearDrawingPoints() {
        if (null != drawingPointsForNumber) {
            drawingPointsForNumber.clear();
        }
    }

    public boolean hasDrawingPoints() {
        return CollectionUtils.isNotEmpty(drawingPointsForNumber);
    }
}
