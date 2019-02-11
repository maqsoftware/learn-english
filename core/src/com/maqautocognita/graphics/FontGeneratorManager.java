package com.maqautocognita.graphics;

import com.maqautocognita.constant.TextFontSizeEnum;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class FontGeneratorManager {
    private static final String FONT_PATH = "fonts/NotoSans-Regular.ttf";
    private static AssetManager manager;

    static {
        init();
    }

    static void init() {
        manager = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        for (final TextFontSizeEnum textFontSizeEnum : TextFontSizeEnum.values()) {
            loadFreeTypeFont(textFontSizeEnum.getFontSize());
        }
    }

    private static String loadFreeTypeFont(int fontSizeEnum) {
        String name = getFileNameByFontSize(fontSizeEnum);
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = FONT_PATH;
        params.fontParameters = getFreeTypeFontParameter(fontSizeEnum);
        manager.load(name, BitmapFont.class, params);
        return name;
    }

    private static String getFileNameByFontSize(int fontSizeEnum) {
        return String.valueOf(fontSizeEnum) + ".ttf";
    }

    private static FreeTypeFontGenerator.FreeTypeFontParameter getFreeTypeFontParameter(int fontSize) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        String defaultCharacters = FreeTypeFontGenerator.DEFAULT_CHARS + "āēīōū";
        parameter.characters = defaultCharacters;
        parameter.incremental = true;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.size = fontSize;

        return parameter;
    }

    public static BitmapFont getFont(TextFontSizeEnum textFontSizeEnum) {
        return getBitmapFontByFontSize(textFontSizeEnum.getFontSize());
    }

    private static BitmapFont getBitmapFontByFontSize(int fontSize) {

        String name = getFileNameByFontSize(fontSize);

        if (null == manager) {
            init();
        }

        if (manager.isLoaded(name)) {
            return manager.get(name, BitmapFont.class);
        } else {
            manager.finishLoadingAsset(name);
            return getBitmapFontByFontSize(fontSize);
        }

    }

    public static void clear() {
        if (null != manager) {
            manager.dispose();
            manager = null;
        }
    }
}
