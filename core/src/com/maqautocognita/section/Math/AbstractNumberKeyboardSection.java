package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * This is mainly used to render the number keyboard in the center of the screen and display in the bottom of the screen
 *
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractNumberKeyboardSection extends AbstractMathSection {

    protected static final Vector2 NUMBER_BLOCK_SIZE = new Vector2(105, 105);

    private static final float GAP_BETWEEN_NUMBER_BLOCK_IN_IMAGE = 5;

    protected List<TextureScreenObject<Integer, Object>> numberKeyBlockScreenObjectList;

    //store the current touching screen object, it will be removed when tounchup
    private TextureScreenObject<Integer, Object> touchingNumberKeyboardScreenObject;


    public AbstractNumberKeyboardSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void render() {
        if (isKeyboardRequired()) {
            initNumberKeyboard();
            batch.begin();
            ScreenObjectUtils.draw(batch, numberKeyBlockScreenObjectList);
            batch.end();
        }
        super.render();
    }

    protected boolean isKeyboardRequired() {
        return true;
    }

    private void initNumberKeyboard() {
        if (null == numberKeyBlockScreenObjectList) {
            numberKeyBlockScreenObjectList = new ArrayList<TextureScreenObject<Integer, Object>>(getMaximumNumberOfNumberBlockRequiredToShowInKeyboard());

            float numberKeyboardStartXPosition = getStartXPositionOfNumberKeyboard(getWidthOfNumberKeyboard());
            float numberKeyboardStartYPosition = getStartYPositionOfNumberKeyboard();

            //init number block keyboard
            for (int i = 1; i <= getMaximumNumberOfNumberBlockRequiredToShowInKeyboard(); i++) {

                IconPosition iconPosition = new IconPosition(i * NUMBER_BLOCK_SIZE.x + i * GAP_BETWEEN_NUMBER_BLOCK_IN_IMAGE, 0, NUMBER_BLOCK_SIZE.x, NUMBER_BLOCK_SIZE.y);

                numberKeyBlockScreenObjectList.add(new TextureScreenObject(i, null, iconPosition,
                        numberKeyboardStartXPosition + (i - 1) * NUMBER_BLOCK_SIZE.x + (i - 1) * GAP_BETWEEN_NUMBER_BLOCK_IN_IMAGE,
                        numberKeyboardStartYPosition,
                        AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_KEYBOARD), AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_KEYBOARD_HIGHLIGHTED)));
            }
        }
    }

    /**
     * return the number of block start from 1 which are required to show in the number keyuboard
     *
     * @return
     */
    protected abstract int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard();

    protected float getStartXPositionOfNumberKeyboard(float totalWidthOfNumberKeyboard) {
        return ScreenUtils.getXPositionForCenterObject(totalWidthOfNumberKeyboard);
    }

    private float getWidthOfNumberKeyboard() {
        return NUMBER_BLOCK_SIZE.x * getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() + GAP_BETWEEN_NUMBER_BLOCK_IN_IMAGE * getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() - 1;
    }

    protected abstract float getStartYPositionOfNumberKeyboard();

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{AssetManagerUtils.NUMBER_KEYBOARD, AssetManagerUtils.NUMBER_KEYBOARD_HIGHLIGHTED}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != numberKeyBlockScreenObjectList) {
            numberKeyBlockScreenObjectList.clear();
            numberKeyBlockScreenObjectList = null;
        }

        touchingNumberKeyboardScreenObject = null;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != touchingNumberKeyboardScreenObject) {
            int number = touchingNumberKeyboardScreenObject.id;
            playNumberAudio(number);
            afterNumberKeyboardSelected(number);

            touchingNumberKeyboardScreenObject.isHighlighted = false;
            touchingNumberKeyboardScreenObject = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != touchingNumberKeyboardScreenObject) {
            //make sure the previous pressd key is unhighlight
            touchingNumberKeyboardScreenObject.isHighlighted = false;
        }


        touchingNumberKeyboardScreenObject = ScreenObjectUtils.getTouchingScreenObject(numberKeyBlockScreenObjectList, screenX, screenY);
        if (null != touchingNumberKeyboardScreenObject) {
            touchingNumberKeyboardScreenObject.isHighlighted = true;
        }
    }

    protected abstract void afterNumberKeyboardSelected(int number);

}
