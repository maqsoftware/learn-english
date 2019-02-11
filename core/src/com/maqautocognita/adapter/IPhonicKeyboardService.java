package com.maqautocognita.adapter;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface IPhonicKeyboardService {


    int getKeyHeight();


    float getKeyboardHeight();

    float getKeyboardWidth();

    String[] getAllKeys();

    Vector2 getKeyPosition(String key);

    AutoCognitaTextureRegion getKeyTextureRegion(String key);

    AutoCognitaTextureRegion getKeyHighLightedTextureRegion(String key);

    AutoCognitaTextureRegion getKeyDisabledTextureRegion(String key);
}
