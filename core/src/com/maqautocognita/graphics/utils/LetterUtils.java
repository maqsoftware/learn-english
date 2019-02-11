package com.maqautocognita.graphics.utils;

import com.maqautocognita.constant.ScreenObjectType;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.FontGeneratorManager;
import com.maqautocognita.graphics.ScreenObject;
import com.maqautocognita.graphics.TextScreenObject;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class LetterUtils {

    public static final String UNDERSCORE = "_";


    public static float getTotalWidthOfWord(String word, TextFontSizeEnum textFontSizeEnum) {

        return getSizeOfWord(word, textFontSizeEnum)[0];
    }

    public static float[] getSizeOfWord(String word, TextFontSizeEnum textFontSizeEnum) {

        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(FontGeneratorManager.getFont(textFontSizeEnum), word);
        return new float[]{layout.width, layout.height};
    }

    public static float getMaximumHeight(TextFontSizeEnum textFontSizeEnum) {

        switch (textFontSizeEnum) {
            case FONT_36:
                return 25;
            case FONT_48:
                return 34;
            case FONT_54:
                return 37;
            case FONT_72:
                return 50;
            case FONT_108:
                return 75;
            case FONT_144:
                return 99;
            case FONT_288:
                return 200;
        }
        return 0;
    }

    public static float getHeightOfWord(String word, TextFontSizeEnum textFontSizeEnum) {

        GlyphLayout layout = new GlyphLayout();
        layout.setText(FontGeneratorManager.getFont(textFontSizeEnum), word);
        return layout.height;
    }

    public static float getHeightOfWordWithWrap(String word, TextFontSizeEnum textFontSizeEnum, float targetWidth) {

        GlyphLayout layout = new GlyphLayout();
        layout.setText(FontGeneratorManager.getFont(textFontSizeEnum), word, Color.WHITE, targetWidth, Align.topLeft, true);
        return layout.height;
    }

    public static float getHeightOfWordWithWrap(String word, BitmapFont font, float targetWidth) {

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, word, Color.WHITE, targetWidth, Align.topLeft, true);
        return layout.height;
    }


    public static <ID> List<ScreenObject<ID, ScreenObjectType>> getTextScreenObjectListSeparately(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                                                                  TextFontSizeEnum textFontSizeEnum
    ) {

        return getTextScreenObjectListSeparately(id, objectType, startXPosition, startYPosition, displayText, textFontSizeEnum, false);
    }

    private static <ID> List<ScreenObject<ID, ScreenObjectType>> getTextScreenObjectListSeparately(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                                                                   TextFontSizeEnum textFontSizeEnum, boolean isYPositionNotIncludeHeight
    ) {

        List<ScreenObject<ID, ScreenObjectType>> screenObjectList = new ArrayList<ScreenObject<ID, ScreenObjectType>>(displayText.length());
        for (int i = 0; i < displayText.length(); i++) {
            ScreenObject textScreenObject = getTextScreenObject(id, objectType, startXPosition, startYPosition, String.valueOf(displayText.charAt(i)), textFontSizeEnum, isYPositionNotIncludeHeight);
            screenObjectList.add(textScreenObject);
            startXPosition += textScreenObject.width;
        }
        return screenObjectList;
    }

    private static <ID> TextScreenObject getTextScreenObject(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                             TextFontSizeEnum textFontSizeEnum, boolean isYPositionNotIncludeHeight
    ) {
        return new TextScreenObject(id, objectType, displayText, startXPosition, startYPosition, textFontSizeEnum, isYPositionNotIncludeHeight);
    }

    public static <ID> List<ScreenObject<ID, ScreenObjectType>> getTextScreenObjectListSeparatelyWithYPositionNotIncludeHeight(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                                                                                               TextFontSizeEnum textFontSizeEnum
    ) {

        return getTextScreenObjectListSeparately(id, objectType, startXPosition, startYPosition, displayText, textFontSizeEnum, true);
    }

    public static List<ScreenObject> getTextScreenObjectList(float startXPosition, float startYPosition, String displayText,
                                                             TextFontSizeEnum textFontSizeEnum
    ) {

        List<ScreenObject> screenObjectList = new ArrayList<ScreenObject>();
        screenObjectList.add(getTextScreenObject(null, null, startXPosition, startYPosition, displayText, textFontSizeEnum));

        return screenObjectList;
    }

    public static <ID> TextScreenObject getTextScreenObject(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                            TextFontSizeEnum textFontSizeEnum
    ) {
        return getTextScreenObject(id, objectType, startXPosition, startYPosition, displayText,
                textFontSizeEnum, false);
    }

    public static <ID> List<ScreenObject<ID, Object>> getTextScreenObjectList(ID id, float startXPosition, float startYPosition, String displayText,
                                                                              TextFontSizeEnum textFontSizeEnum
    ) {

        List<ScreenObject<ID, Object>> screenObjectList = new ArrayList<ScreenObject<ID, Object>>();
        screenObjectList.add(getTextScreenObject(id, null, startXPosition, startYPosition, displayText, textFontSizeEnum));

        return screenObjectList;
    }

    public static <ID> List<ScreenObject<ID, ScreenObjectType>> getTextScreenObjectList(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                                                        TextFontSizeEnum textFontSizeEnum
    ) {

        List<ScreenObject<ID, ScreenObjectType>> screenObjectList = new ArrayList<ScreenObject<ID, ScreenObjectType>>();
        screenObjectList.add(getTextScreenObject(id, objectType, startXPosition, startYPosition, displayText, textFontSizeEnum));

        return screenObjectList;
    }

    public static <ID> TextScreenObject getTextScreenObject(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                            TextFontSizeEnum textFontSizeEnum, float targetWidth
    ) {
        TextScreenObject textScreenObject = getTextScreenObject(id, objectType, startXPosition, startYPosition, displayText,
                textFontSizeEnum, false);
        textScreenObject.setTargetWidth(targetWidth);

        return textScreenObject;
    }

    public static <ID> TextScreenObject getTextScreenObjectWithYPositionNotIncludeHeight(ID id, ScreenObjectType objectType, float startXPosition, float startYPosition, String displayText,
                                                                                         TextFontSizeEnum textFontSizeEnum
    ) {
        return getTextScreenObject(id, objectType, startXPosition, startYPosition, displayText,
                textFontSizeEnum, true);
    }

    public static TextScreenObject getTextScreenObjectWithYPositionNotIncludeHeight(float startXPosition, float startYPosition, String displayText,
                                                                                    TextFontSizeEnum textFontSizeEnum
    ) {
        return getTextScreenObject(null, null, startXPosition, startYPosition, displayText,
                textFontSizeEnum, true);
    }

    public static List<ScreenObject<String, ScreenObjectType>> getTHSound(ScreenObjectType screenObjectType, float startXPosition, float startYPosition, TextFontSizeEnum fontSizeEnum, float targetWidth) {
        List<ScreenObject<String, ScreenObjectType>> thSounds = new ArrayList<ScreenObject<String, ScreenObjectType>>(2);

        TextScreenObject phonicSoundScreenObject = new TextScreenObject("_th", screenObjectType, "th",
                startXPosition,
                startYPosition, fontSizeEnum, targetWidth, true);
        thSounds.add(phonicSoundScreenObject);

        TextScreenObject underlineScreenObject = new TextScreenObject("_th", screenObjectType, "_",
                startXPosition,
                startYPosition, fontSizeEnum, phonicSoundScreenObject.width, true);
        thSounds.add(underlineScreenObject);

        return thSounds;

    }

}
