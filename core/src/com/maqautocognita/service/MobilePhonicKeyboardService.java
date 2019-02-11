package com.maqautocognita.service;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MobilePhonicKeyboardService implements com.maqautocognita.adapter.IPhonicKeyboardService {

    private static final int LARGE_KEY_WIDTH = 211;
    private static final int KEY_WIDTH = 175;
    private static final int KEY_HEIGHT = 115;
    private static final int MAXIMUM_NUMBER_OF_KEY_PER_ROW = 6;
    private static final int NUMBER_OF_ROW = 8;
    private static final String KEYBOARDS[] = new String[]{
            "p", "t", "k", "s", "f", "r",
            "b", "d", "g", "z", "v", "l",
            "m", "th", "j", "sh", "w", "n",
            "h", "_th", "y", "ch", "wh", "ng",
            "a", "e", "i", "o", "u",
            "_a", "_e", "_i", "_o", "_u",
            "ar", "er", "", "or", "oo",
            "aw", "", "oi", "ow", "_oo"};
    /**
     * below will be the position for each phonic key in the image,
     * the x position in the Vector2 means the column position in the image,
     * the y position in the Vector2 means the row position in the Vector2
     */
    private static final Vector2 P_POSITION = new Vector2(1, 1);
    private static final Vector2 T_POSITION = new Vector2(2, 1);
    private static final Vector2 C_POSITION = new Vector2(3, 1);
    private static final Vector2 K_POSITION = new Vector2(3, 1);
    private static final Vector2 S_POSITION = new Vector2(4, 1);
    private static final Vector2 F_POSITION = new Vector2(5, 1);
    private static final Vector2 R_POSITION = new Vector2(6, 1);
    private static final Vector2 B_POSITION = new Vector2(1, 2);
    private static final Vector2 D_POSITION = new Vector2(2, 2);
    private static final Vector2 G_POSITION = new Vector2(3, 2);
    private static final Vector2 Z_POSITION = new Vector2(4, 2);
    private static final Vector2 V_POSITION = new Vector2(5, 2);
    private static final Vector2 L_POSITION = new Vector2(6, 2);
    private static final Vector2 M_POSITION = new Vector2(1, 3);
    private static final Vector2 TH_POSITION = new Vector2(2, 3);
    private static final Vector2 J_POSITION = new Vector2(3, 3);
    private static final Vector2 SH_POSITION = new Vector2(4, 3);
    private static final Vector2 W_POSITION = new Vector2(5, 3);
    private static final Vector2 N_POSITION = new Vector2(6, 3);
    private static final Vector2 H_POSITION = new Vector2(1, 4);
    private static final Vector2 _TH_POSITION = new Vector2(2, 4);
    private static final Vector2 Y_POSITION = new Vector2(3, 4);
    private static final Vector2 CH_POSITION = new Vector2(4, 4);
    private static final Vector2 ZH_POSITION = new Vector2(4, 4);
    private static final Vector2 WH_POSITION = new Vector2(5, 4);
    private static final Vector2 NG_POSITION = new Vector2(6, 4);
    private static final Vector2 A_POSITION = new Vector2(1, 5);
    private static final Vector2 E_POSITION = new Vector2(2, 5);
    private static final Vector2 I_POSITION = new Vector2(3, 5);
    private static final Vector2 O_POSITION = new Vector2(4, 5);
    private static final Vector2 U_POSITION = new Vector2(5, 5);
    private static final Vector2 _A_POSITION = new Vector2(1, 6);
    private static final Vector2 _E_POSITION = new Vector2(2, 6);
    private static final Vector2 _I_POSITION = new Vector2(3, 6);
    private static final Vector2 _O_POSITION = new Vector2(4, 6);
    private static final Vector2 _U_POSITION = new Vector2(5, 6);
    private static final Vector2 AR_POSITION = new Vector2(1, 7);
    private static final Vector2 ER_POSITION = new Vector2(2, 7);
    private static final Vector2 OR_POSITION = new Vector2(4, 7);
    private static final Vector2 OO_POSITION = new Vector2(5, 7);
    private static final Vector2 AW_POSITION = new Vector2(1, 8);
    private static final Vector2 OI_POSITION = new Vector2(3, 8);
    private static final Vector2 OW_POSITION = new Vector2(4, 8);
    private static final Vector2 _OO_POSITION = new Vector2(5, 8);
    private static MobilePhonicKeyboardService instance = null;


    public static MobilePhonicKeyboardService getInstance() {
        if (instance == null) {
            instance = new MobilePhonicKeyboardService();
        }
        return instance;
    }

    @Override
    public int getKeyHeight() {
        return KEY_HEIGHT;
    }

    @Override
    public float getKeyboardHeight() {
        return NUMBER_OF_ROW * getKeyHeight();
    }

    @Override
    public float getKeyboardWidth() {
        return MAXIMUM_NUMBER_OF_KEY_PER_ROW * KEY_WIDTH;
    }

    public String[] getAllKeys() {
        return KEYBOARDS;
    }


    private AutoCognitaTextureRegion getKeyTextureRegion(String key, String textureFileName) {
        Vector2 keyPosition = getKeyPosition(key);

        if (null != keyPosition) {

            float keyWidth = keyPosition.y <= 4 ? KEY_WIDTH : LARGE_KEY_WIDTH;
            float startYPosition = keyPosition.y <= 4 ? 0 : 20;

            return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(textureFileName),
                    new IconPosition(2 + (keyPosition.x - 1) * (keyWidth + 5), startYPosition + (keyPosition.y - 1) * KEY_HEIGHT, keyWidth, KEY_HEIGHT));
        }

        return null;
    }

    @Override
    public Vector2 getKeyPosition(String key) {
        Vector2 keyPosition = null;

        try {
            keyPosition = (Vector2) MobilePhonicKeyboardService.class.getDeclaredField(key.toUpperCase() + "_POSITION").get(null);
        } catch (NoSuchFieldException e) {
            Gdx.app.error(getInstance().getClass().getName(), e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Gdx.app.error(getInstance().getClass().getName(), e.getMessage(), e);
        }

        return keyPosition;
    }

    public AutoCognitaTextureRegion getKeyTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.MOBILE_PHONICS_KEYBOARD);
    }

    public AutoCognitaTextureRegion getKeyHighLightedTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_HIGHLIGHTED);
    }

    public AutoCognitaTextureRegion getKeyDisabledTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.MOBILE_PHONICS_KEYBOARD_DISABLED);
    }
}
