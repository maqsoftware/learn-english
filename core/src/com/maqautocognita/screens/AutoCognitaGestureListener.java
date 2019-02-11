package com.maqautocognita.screens;

import com.badlogic.gdx.input.GestureDetector;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface AutoCognitaGestureListener extends GestureDetector.GestureListener {
    boolean touchUp(float x, float y, int pointer, int button);
}
