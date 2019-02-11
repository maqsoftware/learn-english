package com.maqautocognita.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class DrawLetter {

    private List<String> unTouchedPositionList;

    private List<String> touchedPositionList;

    public int getTotalNumberOfTouchedPoint() {
        return null == touchedPositionList ? 0 : touchedPositionList.size();
    }

    public int getTotalNumberOfUntouchedPoint() {
        return null == unTouchedPositionList ? 0 : unTouchedPositionList.size();
    }

    public void clearTouchPoints() {
        unTouchedPositionList = null;
        touchedPositionList = null;
    }


    /**
     * store the given untouched x and y position,
     * which mean if the user try to draw the alphabet in the writing pad, but their drawing point is not near the point of the letter, the drawing point will be stored in here.
     * <p>
     * The x position and the y position will be stored in unqiue, which mean if the given position is exists in the list, it will not be stored again.
     *
     * @param xPosition
     * @param yPosition
     */
    public void addUnTouchedPosition(float xPosition, float yPosition) {
        if (null == unTouchedPositionList) {
            unTouchedPositionList = new ArrayList<String>();
        }

        String positionText = xPosition + "_" + yPosition;

        if (!unTouchedPositionList.contains(positionText)) {
            unTouchedPositionList.add(positionText);
        }
    }

    public boolean addTouchedPosition(float xPosition, float yPosition) {
        if (null == touchedPositionList) {
            touchedPositionList = new ArrayList<String>();
        }

        String positionText = xPosition + "_" + yPosition;

        if (touchedPositionList.contains(positionText)) {
            return false;
        } else {
            touchedPositionList.add(positionText);
            return true;
        }
    }

}
