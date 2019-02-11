package com.maqautocognita.service;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         This class is mainly used to get the image of the phonic keyboard key in type {@link AutoCognitaTextureRegion}
 *         <p/>
 *         Those key in the state normal (unpressed), highlighted(pressing) and disabled (non-press) are able by this class.
 */
public class TabletPhonicKeyboardService implements com.maqautocognita.adapter.IPhonicKeyboardService {

    private static final int KEY_WIDTH = 110;
    private static final int KEY_HEIGHT = 110;
    private static final int MAXIMUM_NUMBER_OF_KEY_PER_ROW = 11;
    private static final int NUMBER_OF_ROW = 4;
    private static final String KEYBOARDS[] = new String[]{"p", "t", "c", "k", "s", "f", "r", "a", "e", "i", "o", "u",
            "b", "d", "g", "z", "v", "l", "_a", "_e", "_i", "_o", "_u",
            "m", "th", "j", "sh", "w", "n", "ar", "er", "", "or", "oo",
            "h", "_th", "y", "ch", "wh", "ng", "aw", "", "oi", "ow", "_oo"};
    /**
     * below will be the position for each phonic key in the image,
     * the x position in the Vector2 means the column position in the image,
     * the y position in the Vector2 means the row position in the Vector2
     */
    private static final Vector2 P_POSITION = new Vector2(1, 1);
    private static final Vector2 T_POSITION = new Vector2(2, 1);
    private static final Vector2 C_POSITION = new Vector2(1, 6);
    private static final Vector2 K_POSITION = new Vector2(3, 1);
    private static final Vector2 S_POSITION = new Vector2(4, 1);
    private static final Vector2 F_POSITION = new Vector2(5, 1);
    private static final Vector2 R_POSITION = new Vector2(6, 1);
    private static final Vector2 A_POSITION = new Vector2(7, 1);
    private static final Vector2 E_POSITION = new Vector2(8, 1);
    private static final Vector2 I_POSITION = new Vector2(9, 1);
    private static final Vector2 O_POSITION = new Vector2(10, 1);
    private static final Vector2 U_POSITION = new Vector2(11, 1);
    private static final Vector2 B_POSITION = new Vector2(1, 2);
    private static final Vector2 D_POSITION = new Vector2(2, 2);
    private static final Vector2 G_POSITION = new Vector2(3, 2);
    private static final Vector2 Z_POSITION = new Vector2(4, 2);
    private static final Vector2 V_POSITION = new Vector2(5, 2);
    private static final Vector2 L_POSITION = new Vector2(6, 2);
    private static final Vector2 _A_POSITION = new Vector2(7, 2);
    private static final Vector2 _E_POSITION = new Vector2(8, 2);
    private static final Vector2 _I_POSITION = new Vector2(9, 2);
    private static final Vector2 _O_POSITION = new Vector2(10, 2);
    private static final Vector2 _U_POSITION = new Vector2(11, 2);
    private static final Vector2 M_POSITION = new Vector2(1, 3);
    private static final Vector2 TH_POSITION = new Vector2(2, 3);
    private static final Vector2 J_POSITION = new Vector2(3, 3);
    private static final Vector2 SH_POSITION = new Vector2(4, 3);
    private static final Vector2 W_POSITION = new Vector2(5, 3);
    private static final Vector2 N_POSITION = new Vector2(6, 3);
    private static final Vector2 AR_POSITION = new Vector2(7, 3);
    private static final Vector2 ER_POSITION = new Vector2(8, 3);
    private static final Vector2 OR_POSITION = new Vector2(10, 3);
    private static final Vector2 OO_POSITION = new Vector2(11, 3);
    private static final Vector2 _TH_POSITION = new Vector2(2, 4);
    private static final Vector2 Y_POSITION = new Vector2(3, 4);
    private static final Vector2 ZH_POSITION = new Vector2(4, 4);
    private static final Vector2 WH_POSITION = new Vector2(5, 4);
    private static final Vector2 NG_POSITION = new Vector2(6, 4);
    private static final Vector2 AW_POSITION = new Vector2(7, 4);
    private static final Vector2 OI_POSITION = new Vector2(9, 4);
    private static final Vector2 OW_POSITION = new Vector2(10, 4);
    private static final Vector2 _OO_POSITION = new Vector2(11, 4);
    private static final Vector2 H_POSITION = new Vector2(1, 4);
    private static final Vector2 CH_POSITION = new Vector2(4, 4);
    private static TabletPhonicKeyboardService instance = null;

    public static TabletPhonicKeyboardService getInstance() {
        if (instance == null) {
            instance = new TabletPhonicKeyboardService();
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

    public AutoCognitaTextureRegion getKeyTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.PHONICS_KEYBOARD);
    }

    private AutoCognitaTextureRegion getKeyTextureRegion(String key, String textureFileName) {
        Vector2 keyPosition = getKeyPosition(key);

        if (null != keyPosition) {
            return new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(textureFileName),
                    new IconPosition((keyPosition.x - 1) * KEY_WIDTH, (keyPosition.y - 1) * KEY_HEIGHT, KEY_WIDTH, KEY_HEIGHT));
        }

        return null;
    }

    @Override
    public Vector2 getKeyPosition(String key) {
        Vector2 keyPosition = null;

        try {
            keyPosition = (Vector2) TabletPhonicKeyboardService.class.getDeclaredField(key.toUpperCase() + "_POSITION").get(null);
        } catch (NoSuchFieldException e) {
            Gdx.app.error(getInstance().getClass().getName(), e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Gdx.app.error(getInstance().getClass().getName(), e.getMessage(), e);
        }

        return keyPosition;
    }

    public AutoCognitaTextureRegion getKeyHighLightedTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.PHONICS_KEYBOARD_HIGHLIGHTED);
    }

    public AutoCognitaTextureRegion getKeyDisabledTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.PHONICS_KEYBOARD_DISABLED);
    }
}
