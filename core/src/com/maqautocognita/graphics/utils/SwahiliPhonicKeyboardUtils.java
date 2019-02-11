package com.maqautocognita.graphics.utils;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.utils.ArrayUtils;
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
public class SwahiliPhonicKeyboardUtils {

    public static final int VOWEL_KEY_WIDTH = 200;
    public static final int VOWEL_KEY_HEIGHT = 110;

    public static final int KEY_WIDTH = 110;
    public static final int KEY_HEIGHT = 110;

    public static final int LONG_KEY_WIDTH = 110;
    public static final int LONG_KEY_HEIGHT = 325;

    public static final String KEYBOARDS[] = new String[]{"p", "t", "k", "s", "f", "r", "y", "m", "n", "a",
            "b", "d", "g", "z", "v", "l", "j", "mb", "nd", "ny", "w", "e",
            "th", "kh", "sh", "vy", "mv", "ng", "nj", "i",
            "dh", "gh", "ch", "ng'", "nz", "o",
            "h", "u"};

    /**
     * below will be the position for each phonic key in the image,
     * the x position in the Vector2 means the column position in the image,
     * the y position in the Vector2 means the row position in the Vector2
     */
    private static final Vector2 P_POSITION = new Vector2(1, 1);
    private static final Vector2 T_POSITION = new Vector2(2, 1);
    private static final Vector2 K_POSITION = new Vector2(3, 1);
    private static final Vector2 S_POSITION = new Vector2(4, 1);
    private static final Vector2 F_POSITION = new Vector2(5, 1);
    private static final Vector2 R_POSITION = new Vector2(6, 1);
    private static final Vector2 Y_POSITION = new Vector2(7, 1);
    private static final Vector2 M_POSITION = new Vector2(8, 1);
    private static final Vector2 N_POSITION = new Vector2(9, 1);
    private static final Vector2 A_POSITION = new Vector2(13, 1);

    private static final Vector2 B_POSITION = new Vector2(1, 2);
    private static final Vector2 D_POSITION = new Vector2(2, 2);
    private static final Vector2 G_POSITION = new Vector2(3, 2);
    private static final Vector2 Z_POSITION = new Vector2(4, 2);
    private static final Vector2 V_POSITION = new Vector2(5, 2);
    private static final Vector2 L_POSITION = new Vector2(6, 2);
    private static final Vector2 J_POSITION = new Vector2(7, 2);
    private static final Vector2 MB_POSITION = new Vector2(8, 2);
    private static final Vector2 ND_POSITION = new Vector2(9, 2);
    private static final Vector2 NY_POSITION = new Vector2(10, 2);
    private static final Vector2 W_POSITION = new Vector2(11, 2);
    private static final Vector2 E_POSITION = new Vector2(13, 2);

    private static final Vector2 TH_POSITION = new Vector2(2, 3);
    private static final Vector2 KH_POSITION = new Vector2(3, 3);
    private static final Vector2 SH_POSITION = new Vector2(4, 3);
    private static final Vector2 VY_POSITION = new Vector2(5, 3);
    private static final Vector2 MV_POSITION = new Vector2(8, 3);
    private static final Vector2 NG_POSITION = new Vector2(9, 3);
    private static final Vector2 NJ_POSITION = new Vector2(10, 3);
    private static final Vector2 I_POSITION = new Vector2(13, 3);

    private static final Vector2 DH_POSITION = new Vector2(2, 4);
    private static final Vector2 GH_POSITION = new Vector2(3, 4);
    private static final Vector2 CH_POSITION = new Vector2(4, 4);
    private static final Vector2 NG__POSITION = new Vector2(9, 4);
    private static final Vector2 NZ_POSITION = new Vector2(10, 4);
    private static final Vector2 O_POSITION = new Vector2(13, 4);

    private static final Vector2 H_POSITION = new Vector2(3, 5);
    private static final Vector2 U_POSITION = new Vector2(13, 5);

    private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

    public static AutoCognitaTextureRegion getKeyTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD);
    }

    private static AutoCognitaTextureRegion getKeyTextureRegion(String key, String textureFileName) {
        Vector2 keyPosition = getKeyPosition(key);

        if (null != keyPosition) {
            float width = KEY_WIDTH;
            float height = KEY_HEIGHT;
            if ("w".equalsIgnoreCase(key)) {
                width = LONG_KEY_WIDTH;
                height = LONG_KEY_HEIGHT;
            } else if (isVowels(key)) {
                width = VOWEL_KEY_WIDTH;
                height = VOWEL_KEY_HEIGHT;
            }

            float startXPosition = (keyPosition.x - 1) * KEY_WIDTH;
            if (isVowels(key)) {
                startXPosition = 1252;
            }

            IconPosition iconPosition = new IconPosition(startXPosition, (keyPosition.y - 1) * KEY_HEIGHT, width, height);
            return new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(textureFileName), iconPosition);
        }

        return null;
    }

    public static Vector2 getKeyPosition(String key) {
        Vector2 keyPosition = null;

        if (key.equalsIgnoreCase("ng'")) {
            key = "ng_";
        }

        try {
            keyPosition = (Vector2) SwahiliPhonicKeyboardUtils.class.getDeclaredField(key.toUpperCase() + "_POSITION").get(null);
        } catch (NoSuchFieldException e) {
            Gdx.app.error("SwahiliPhonicKeyboardUtils", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Gdx.app.error("SwahiliPhonicKeyboardUtils", e.getMessage(), e);
        }

        return keyPosition;
    }

    public static boolean isVowels(String key) {
        return ArrayUtils.isContainIgnoreCase(VOWELS, key);
    }

    public static AutoCognitaTextureRegion getKeyHighLightedTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_HIGHLIGHTED);
    }

    public static AutoCognitaTextureRegion getKeyDisabledTextureRegion(String key) {
        return getKeyTextureRegion(key, AssetManagerUtils.SWAHILI_PHONICS_KEYBOARD_DISABLED);
    }
}
