package com.maqautocognita.section.Alphabet;

import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.service.LetterPointsSequenceService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.DotTracingUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public final class AlphabetDotTracingSection {

    private int dotSize = 20;
    private float letterStartYPosition = 720;
    private float letterStartXPosition;
    private SpriteBatch batch;
    private AutoCognitaTextureRegion dot;
    private int[] minMaxYPositionOfLowerCaseA;
    private float ratio;
    private float minYPositionOfLetter;
    private boolean isUpperCaseLetterTracingDotDrew;
    private boolean isLowerCaseLetterTracingDotDrew;

    private List<IconPosition> currentShowingUpperLetterPositionList;
    private List<IconPosition> currentShowingLowerLetterPositionList;

    public AlphabetDotTracingSection(int ratio) {
        minMaxYPositionOfLowerCaseA = LetterPointsSequenceService.getInstance().getMaxAndMinYPosition("a");

        this.ratio = (ratio + dotSize) / (minMaxYPositionOfLowerCaseA[1] - minMaxYPositionOfLowerCaseA[0]);

        minYPositionOfLetter = minMaxYPositionOfLowerCaseA[0] * this.ratio;

    }

    public float getRatio() {
        return ratio;
    }

    public void setLetterStartXPosition(float letterStartXPosition) {
        this.letterStartXPosition = letterStartXPosition;
    }

    public void setLetterStartYPosition(float letterStartYPosition) {
        this.letterStartYPosition = letterStartYPosition;
    }

    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    public int getDotSize() {
        return dotSize;
    }

    public void reset() {
        DotTracingUtils.restart();
        currentShowingUpperLetterPositionList = null;
        currentShowingLowerLetterPositionList = null;
        isLowerCaseLetterTracingDotDrew = false;
        isUpperCaseLetterTracingDotDrew = false;
    }

    public void drawBigLetter(String letter) {
        getBatch().begin();
        drawLetterPoints(letter.toUpperCase(), letterStartXPosition, true);
        getBatch().end();
    }

    private Batch getBatch() {
        if (null == batch) {
            batch = new SpriteBatch();
            batch.getProjectionMatrix().setToOrtho2D(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        }

        return batch;
    }

    private void drawLetterPoints(String letter, float startX, boolean isUpperCase) {

        List<IconPosition> currentShowingLetterPositionList;

        if (isUpperCase) {
            letter = letter.toUpperCase();
            if (null == currentShowingUpperLetterPositionList) {
                currentShowingUpperLetterPositionList = new ArrayList<IconPosition>();
            }

            if (currentShowingUpperLetterPositionList.size() == 0) {
                currentShowingUpperLetterPositionList = LetterPointsSequenceService.getInstance().
                        getDrawLetterPoints(letter, startX, letterStartYPosition, minMaxYPositionOfLowerCaseA[2], minYPositionOfLetter, ratio, dotSize);
            }

            currentShowingLetterPositionList = currentShowingUpperLetterPositionList;

        } else {
            letter = letter.toLowerCase();
            if (null == currentShowingLowerLetterPositionList) {
                currentShowingLowerLetterPositionList = new ArrayList<IconPosition>();
            }

            if (currentShowingLowerLetterPositionList.size() == 0) {
                currentShowingLowerLetterPositionList = LetterPointsSequenceService.getInstance().
                        getDrawLetterPoints(letter, startX, letterStartYPosition, minMaxYPositionOfLowerCaseA[2], minYPositionOfLetter, ratio, dotSize);
            }

            currentShowingLetterPositionList = currentShowingLowerLetterPositionList;
        }

        for (IconPosition point : currentShowingLetterPositionList) {
            if (null == dot) {
                dot = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 921, 111, 30, 30);
            }
            DotTracingUtils.drawDot(getBatch(), dot, point, dotSize);
        }

    }

    public void drawSmallLetter(String letter) {
        getBatch().begin();
        drawLetterPoints(letter.toLowerCase(), letterStartXPosition, false);
        getBatch().end();
    }

    public void drawRedDotTracingForBigLetter() {
        if (!isUpperCaseLetterTracingDotDrew) {
            getBatch().begin();
            isUpperCaseLetterTracingDotDrew = DotTracingUtils.drawDotTracingLetter(getBatch(), currentShowingUpperLetterPositionList, dotSize);
            getBatch().end();
        }

    }

    public void drawRedDotTracingForSmallLetter() {
        if (!isLowerCaseLetterTracingDotDrew) {
            getBatch().begin();
            isLowerCaseLetterTracingDotDrew = DotTracingUtils.drawDotTracingLetter(getBatch(), currentShowingLowerLetterPositionList, dotSize);
            getBatch().end();
        }
    }

    public void dispose() {
        if (null != batch) {
            batch.dispose();
            batch = null;
        }

        dot = null;
    }


}
