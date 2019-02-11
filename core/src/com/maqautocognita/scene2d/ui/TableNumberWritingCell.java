package com.maqautocognita.scene2d.ui;

import com.maqautocognita.graphics.NumberWritingScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class TableNumberWritingCell extends TableBorderCell {

    private List<NumberWritingScreenObject> numberWritingScreenObjectList;

    public TableNumberWritingCell(String number, int width, int height) {
        super(number, width, height);
    }

    @Override
    protected void drawText(Batch batch) {
        if (null == numberWritingScreenObjectList) {
            float startXPosition = getX();
            float width = getWidth() / text.length();
            for (int i = 0; i < text.length(); i++) {
                NumberWritingScreenObject numberScreenObject = new NumberWritingScreenObject(Integer.valueOf(text.substring(i, i + 1)),
                        startXPosition, getY(), width, getHeight());

                if (null == numberWritingScreenObjectList) {
                    numberWritingScreenObjectList = new ArrayList<NumberWritingScreenObject>();
                }
                numberScreenObject.setClearWritingAfterCheckCorrect(false);
                numberWritingScreenObjectList.add(numberScreenObject);

                startXPosition += width;
            }
        }
    }

    public void addDrawPoint(int screenX, int screenY, int drawXPoint, int drawYPoint) {
        if (CollectionUtils.isNotEmpty(numberWritingScreenObjectList)) {
            NumberWritingScreenObject touchingNumberWritingScreenObject = ScreenObjectUtils.getTouchingScreenObject(numberWritingScreenObjectList, screenX, screenY);
            if (null != touchingNumberWritingScreenObject) {
                touchingNumberWritingScreenObject.addDrawingPoints(drawXPoint, drawYPoint);
            }
        }
    }

    public boolean isWriteCorrect() {
        if (CollectionUtils.isNotEmpty(numberWritingScreenObjectList)) {
            for (NumberWritingScreenObject numberWritingScreenObject : numberWritingScreenObjectList) {
                if (!numberWritingScreenObject.isNumberWritingCorrect()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isAnswerWrote() {
        for (NumberWritingScreenObject numberWritingScreenObject : numberWritingScreenObjectList) {
            if (!numberWritingScreenObject.hasDrawingPoints()) {
                return false;
            }
        }
        return true;
    }

    public void clearDrawPoints() {
        for (NumberWritingScreenObject numberWritingScreenObject : numberWritingScreenObjectList) {
            numberWritingScreenObject.clearDrawingPoints();
        }
    }
}
