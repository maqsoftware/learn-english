package com.maqautocognita.listener;

import com.badlogic.gdx.input.GestureDetector;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class DirectionGestureDetector extends GestureDetector {

    private static final int FLING_DISTANCE = 800;

    public DirectionGestureDetector(IDirectionGestureListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter {
        IDirectionGestureListener directionListener;

        public DirectionGestureListener(IDirectionGestureListener directionListener) {
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityX) > FLING_DISTANCE) {
                if (velocityX > 0) {
                    directionListener.onRight();
                } else {
                    directionListener.onLeft();
                }
            } else if (Math.abs(velocityY) > FLING_DISTANCE) {
                if (velocityY > 0) {
                    directionListener.onDown();
                } else {
                    directionListener.onUp();
                }
            }
            return super.fling(velocityX, velocityY, button);
        }

    }

}
