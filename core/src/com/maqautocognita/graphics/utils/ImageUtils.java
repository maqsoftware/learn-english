package com.maqautocognita.graphics.utils;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ImageProperties;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;

/**
 * @author sc.chi csc19840914@gmail.com
 *         <p/>
 *         It is mainly used to get the {@link AutoCognitaTextureRegion} for element, but please make sure the image file name is already loaded in the {@link AssetManagerUtils}
 */
public class ImageUtils {

    public static final IconPosition BLUE_ROUND_RECTANGLE = new IconPosition(0, 1150, 1000, 150);

    public static final IconPosition LARGE_PHONIC_SYMBOL_OPEN = new IconPosition(0, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_OPEN_WAVE_25_PERCENT = new IconPosition(600, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_OPEN_WAVE_50_PERCENT = new IconPosition(400, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_OPEN_WAVE_75_PERCENT = new IconPosition(200, 0, 100, 200);

    public static final IconPosition LARGE_PHONIC_SYMBOL_CLOSE = new IconPosition(100, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_CLOSE_WAVE_25_PERCENT = new IconPosition(700, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_CLOSE_WAVE_50_PERCENT = new IconPosition(500, 0, 100, 200);
    public static final IconPosition LARGE_PHONIC_SYMBOL_CLOSE_WAVE_75_PERCENT = new IconPosition(300, 0, 100, 200);


    public static final IconPosition MEDIUM_PHONIC_SYMBOL_OPEN = new IconPosition(0, 200, 75, 150);

    public static final IconPosition SMALL_PHONIC_SYMBOL_OPEN = new IconPosition(0, 350, 50, 100);
    public static final IconPosition SMALL_PHONIC_SYMBOL_OPEN_WAVE_25_PERCENT = new IconPosition(300, 350, 50, 100);
    public static final IconPosition SMALL_PHONIC_SYMBOL_OPEN_WAVE_50_PERCENT = new IconPosition(200, 350, 50, 100);
    public static final IconPosition SMALL_PHONIC_SYMBOL_OPEN_WAVE_75_PERCENT = new IconPosition(100, 350, 50, 100);

    public static final IconPosition SMALL_PHONIC_SYMBOL_CLOSE_WAVE_25_PERCENT = new IconPosition(350, 350, 50, 100);
    public static final IconPosition SMALL_PHONIC_SYMBOL_CLOSE_WAVE_50_PERCENT = new IconPosition(250, 350, 50, 100);
    public static final IconPosition SMALL_PHONIC_SYMBOL_CLOSE_WAVE_75_PERCENT = new IconPosition(150, 350, 50, 100);


    public static final IconPosition MEDIUM_PHONIC_SYMBOL_CLOSE = new IconPosition(75, 200, 75, 150);
    public static final IconPosition SMALL_PHONIC_SYMBOL_CLOSE = new IconPosition(50, 350, 50, 100);
    public static final IconPosition SPEECH_ICON_POSITION = new IconPosition(100, 300, 100, 100);
    private static final IconPosition SMALL_GREY_BORDER_POSITION = new IconPosition(810, 0, 110, 110);
    private static final IconPosition SMALL_RED_BORDER_POSITION = new IconPosition(920, 0, 110, 110);
    private static final IconPosition SMALL_YELLOW_BORDER_POSITION = new IconPosition(810, 110, 110, 110);
    private static final IconPosition SPEECH_ICON_POSITION_IN_HIGHLIGHT_STATE = new IconPosition(100, 500, 100, 100);

    public static AutoCognitaTextureRegion getBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), 270, 0, ImageProperties.BORDER_IMAGE_SIZE, ImageProperties.BORDER_IMAGE_SIZE);
    }

    public static AutoCognitaTextureRegion getSelectedBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), 540, 0, ImageProperties.BORDER_IMAGE_SIZE, ImageProperties.BORDER_IMAGE_SIZE);
    }

    public static AutoCognitaTextureRegion getDisabledBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), 0, 0, ImageProperties.BORDER_IMAGE_SIZE, ImageProperties.BORDER_IMAGE_SIZE);
    }

    public static AutoCognitaTextureRegion getSmallGreyBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), SMALL_GREY_BORDER_POSITION);
    }

    public static AutoCognitaTextureRegion getSmallRedBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), SMALL_RED_BORDER_POSITION);
    }

    public static AutoCognitaTextureRegion getSmallYellowBorder() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), SMALL_YELLOW_BORDER_POSITION);
    }

    public static AutoCognitaTextureRegion getBlueRoundRectangle() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.GENERAL_ICONS), BLUE_ROUND_RECTANGLE);
    }

    public static AutoCognitaTextureRegion getSpeechIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.ICONS), SPEECH_ICON_POSITION);
    }

    public static AutoCognitaTextureRegion getSpeechIconInHighlightState() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.ICONS), SPEECH_ICON_POSITION_IN_HIGHLIGHT_STATE);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolOpenIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_OPEN);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolOpenIconWave25Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_OPEN_WAVE_25_PERCENT);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolOpenIconWave50Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_OPEN_WAVE_50_PERCENT);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolOpenIconWave75Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_OPEN_WAVE_75_PERCENT);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolCloseIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_CLOSE);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolCloseIconWave25Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_CLOSE_WAVE_25_PERCENT);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolCloseIconWave50Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_CLOSE_WAVE_50_PERCENT);
    }

    public static AutoCognitaTextureRegion getLargePhonicSymbolCloseIconWave75Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), LARGE_PHONIC_SYMBOL_CLOSE_WAVE_75_PERCENT);
    }


    public static AutoCognitaTextureRegion getMediumPhonicSymbolOpenIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), MEDIUM_PHONIC_SYMBOL_OPEN);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolOpenIcon() {

        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_OPEN);
    }


    public static AutoCognitaTextureRegion getMediumPhonicSymbolCloseIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), MEDIUM_PHONIC_SYMBOL_CLOSE);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolCloseIcon() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_CLOSE);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolOpenIconWave25Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_OPEN_WAVE_25_PERCENT);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolOpenIconWave50Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_OPEN_WAVE_50_PERCENT);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolOpenIconWave75Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_OPEN_WAVE_75_PERCENT);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolCloseIconWave25Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_CLOSE_WAVE_25_PERCENT);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolCloseIconWave50Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_CLOSE_WAVE_50_PERCENT);
    }

    public static AutoCognitaTextureRegion getSmallPhonicSymbolCloseIconWave75Percent() {
        return new AutoCognitaTextureRegion(AssetManagerUtils.getTextureWithWait(AssetManagerUtils.PHONICS_SYMBOL), SMALL_PHONIC_SYMBOL_CLOSE_WAVE_75_PERCENT);
    }


}
