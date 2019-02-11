package com.maqautocognita.section.Math;

import com.maqautocognita.bo.DragObject;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class MultiplicationMatrixSection extends AbstractAdvanceMathSection {

    protected static final IconPosition NUMBER_PAD_POSITION = new IconPosition(390, 430, 520, 520);
    protected static final int FORMULA_START_Y_POSITION = 603;
    protected static final int FORMULA_START_X_POSITION = 927;
    private static final IconPosition TURN_BUTTON_ICON_POSITION = new IconPosition(1750, 200, 100, 100);
    protected int xValue;
    protected int yValue;
    /**
     * indicate if the “show counting white blocks” mode is on
     */
    protected boolean isCountingView;
    protected List<DragObject<Integer>> numberBlockListInNumberPad;
    protected int result;
    protected boolean isNumberBlockInHorizontalMode = true;
    private AutoCognitaTextureRegion numberPadAutoCognitaTextureRegion;
    private AutoCognitaTextureRegion turnButtonAutoCognitaTextureRegion;
    private AutoCognitaTextureRegion turnButtonHightedAutoCognitaTextureRegion;
    private AutoCognitaTextureRegion[] numberBlocksVerticalAutoCognitaTextureRegions;
    private AutoCognitaTextureRegion[] countingWhiteBlockAutoCognitaTextureRegions;
    private AutoCognitaTextureRegion[] countingWhiteBlockHighlightedAutoCognitaTextureRegions;
    private DragObject<Integer> draggingNumberBlockInNumberPad;
    private float maximumNumberBlockYPositionInNumberTray;
    private float maximumNumberBlockXPositionInNumberTray;
    private List<NumberScreenObject> xNumberScreenObjectList;
    private List<NumberScreenObject> yNumberScreenObjectList;


    public MultiplicationMatrixSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != xNumberScreenObjectList) {
            xNumberScreenObjectList.clear();
            xNumberScreenObjectList = null;
        }
        if (null != yNumberScreenObjectList) {
            yNumberScreenObjectList.clear();
            yNumberScreenObjectList = null;
        }

        draggingNumberBlockInNumberPad = null;
        numberPadAutoCognitaTextureRegion = null;
        turnButtonAutoCognitaTextureRegion = null;
        turnButtonHightedAutoCognitaTextureRegion = null;
        numberBlocksVerticalAutoCognitaTextureRegions = null;
        countingWhiteBlockAutoCognitaTextureRegions = null;
        countingWhiteBlockHighlightedAutoCognitaTextureRegions = null;

        resetScreen();

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{AssetManagerUtils.ICONS, AssetManagerUtils.NUMBER_BLOCKS_VERTICAL,
                AssetManagerUtils.COUNTING_WHITE_BLOCKS, AssetManagerUtils.COUNTING_WHITE_BLOCKS_HIGHLIGHTED}, super.getAllRequiredTextureName());
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != draggingNumberBlockInNumberPad && isNumberBlockInNumberPadAllowToDrag()) {
            if (isNumberBlockInHorizontalMode) {
                if (screenY > maximumNumberBlockYPositionInNumberTray + draggingNumberBlockInNumberPad.getOriginalPosition().height && yValue < 10) {
                    addNumberBlockUpInNumberPad(draggingNumberBlockInNumberPad.getValue());
                } else if (screenY < maximumNumberBlockYPositionInNumberTray && xValue > 0) {
                    //if the user is dragging down the number block
                    removeNumberBlockInNumberPad();
                }
            } else {
                //if the number block is in vertical mode
                if (screenX > maximumNumberBlockXPositionInNumberTray + draggingNumberBlockInNumberPad.getOriginalPosition().width && xValue < 10) {
                    //if the user dragging to the right side in the number block and the number block still not reach the max
                    addNumberBlockUpInNumberPad(draggingNumberBlockInNumberPad.getValue());
                } else if (screenX < maximumNumberBlockXPositionInNumberTray && yValue > 0) {
                    //if the user is dragging left the number block
                    removeNumberBlockInNumberPad();
                }
            }
        }
    }

    protected boolean isNumberBlockInNumberPadAllowToDrag() {
        return true;
    }

    protected void addNumberBlockUpInNumberPad(int number) {
        if (isNumberBlockInHorizontalMode) {
            yValue++;
            xValue = number;
        } else {
            xValue++;
            yValue = number;
        }

        initNumberBlocksHorizontalTexture();

        if (ArrayUtils.isNotEmpty(numberBlocksAutoCognitaTextureRegions) && numberBlocksAutoCognitaTextureRegions.length >= number) {
            if (null == numberBlockListInNumberPad) {
                numberBlockListInNumberPad = new ArrayList<DragObject<Integer>>(10);
            }
            numberBlockListInNumberPad.add(createNumberBlockInNumberPad(number,
                    isNumberBlockInHorizontalMode ?
                            numberBlocksAutoCognitaTextureRegions[number - 1] : numberBlocksVerticalAutoCognitaTextureRegions[number - 1]));
        }
        result = xValue * yValue;
    }

    protected void removeNumberBlockInNumberPad() {
        if (numberBlockListInNumberPad.size() > 0) {
            numberBlockListInNumberPad.remove(numberBlockListInNumberPad.size() - 1);
            if (isNumberBlockInHorizontalMode) {
                if (numberBlockListInNumberPad.size() > 0) {
                    maximumNumberBlockYPositionInNumberTray = numberBlockListInNumberPad.get(numberBlockListInNumberPad.size() - 1).getOriginalPosition().y;
                }
                yValue--;
                if (yValue == 0) {
                    xValue = 0;
                }
            } else {
                if (numberBlockListInNumberPad.size() > 0) {
                    maximumNumberBlockXPositionInNumberTray = numberBlockListInNumberPad.get(numberBlockListInNumberPad.size() - 1).getOriginalPosition().x;
                }
                xValue--;
                if (xValue == 0) {
                    yValue = 0;
                }
            }
        }
        result = xValue * yValue;
    }

    private DragObject<Integer> createNumberBlockInNumberPad
            (int value, AutoCognitaTextureRegion autoCognitaTextureRegion) {
        DragObject<Integer> dragObject = new DragObject<Integer>();
        dragObject.setValue(value);
        final int numberOfNumberBlockInNumberPad = CollectionUtils.isEmpty(numberBlockListInNumberPad) ? 0 : numberBlockListInNumberPad.size();
        if (isNumberBlockInHorizontalMode) {
            dragObject.
                    setOriginalPosition(getNumberBlockIconPositionForHorizontal(numberOfNumberBlockInNumberPad, autoCognitaTextureRegion));
        } else {
            dragObject.
                    setOriginalPosition(getNumberBlockIconPositionForVertical(numberOfNumberBlockInNumberPad, autoCognitaTextureRegion));
        }
        dragObject.setAutoCognitaTextureRegion(autoCognitaTextureRegion);

        maximumNumberBlockYPositionInNumberTray = dragObject.getOriginalPosition().y;
        maximumNumberBlockXPositionInNumberTray = dragObject.getOriginalPosition().x;

        return dragObject;
    }

    private IconPosition getNumberBlockIconPositionForHorizontal(int index, AutoCognitaTextureRegion numberBlockTexture) {
        return new IconPosition(NUMBER_PAD_POSITION.x + 10,
                440 + index * (numberBlockTexture.getRegionHeight() - 5),
                numberBlockTexture.getRegionWidth(), numberBlockTexture.getRegionHeight()
        );
    }

    private IconPosition getNumberBlockIconPositionForVertical(int index, AutoCognitaTextureRegion numberBlockTexture) {
        return new IconPosition(NUMBER_PAD_POSITION.x + 10 + index * (numberBlockTexture.getRegionWidth()),
                435,
                numberBlockTexture.getRegionWidth(), numberBlockTexture.getRegionHeight()
        );
    }

    @Override
    protected void singleTap(int screenX, int screenY) {
        super.singleTap(screenX, screenY);
        if (TouchUtils.isTouched(NUMBER_PAD_POSITION, screenX, screenY)) {
            //which mean the user is single tap in the number block in the number tray
            changeView();
        }
    }

    @Override
    protected void resetScreen() {
        result = 0;
        xValue = 0;
        yValue = 0;
        if (null != numberBlockListInNumberPad) {
            numberBlockListInNumberPad.clear();
        }
        //Whenever touch the erase icon, the “show counting white blocks” mode should be automatically switched back to the normal “show color blocks” mode
        isCountingView = false;
    }

    protected void changeView() {
        isCountingView = !isCountingView;
    }

    @Override
    public void whenSingleTap(int screenX, int screenY) {
        super.whenSingleTap(screenX, screenY);
        if (TouchUtils.isTouched(TURN_BUTTON_ICON_POSITION, screenX, screenY)) {
            //if the user touching the turn mode icon
            //switch the mode
            isNumberBlockInHorizontalMode = !isNumberBlockInHorizontalMode;
            int numberBlockToBeDisplay = isNumberBlockInHorizontalMode ? yValue : xValue;
            if (CollectionUtils.isNotEmpty(numberBlockListInNumberPad)) {
                numberBlockListInNumberPad.clear();
                int value = isNumberBlockInHorizontalMode ? xValue : yValue;
                for (int i = 0; i < numberBlockToBeDisplay; i++) {
                    numberBlockListInNumberPad.add(createNumberBlockInNumberPad(value, isNumberBlockInHorizontalMode ? numberBlocksAutoCognitaTextureRegions[value - 1] : numberBlocksVerticalAutoCognitaTextureRegions[value - 1]));
                }
            }
        }
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlock) {
            if (TouchUtils.isTouched(NUMBER_PAD_POSITION, screenX, screenY)) {
                //if the block is dragged into the tray
                if ((isNumberBlockInHorizontalMode && (xValue == 0 || xValue == draggingNumberBlock.getId()) && yValue < 10)
                        || (!isNumberBlockInHorizontalMode && (yValue == 0 || yValue == draggingNumberBlock.getId())) && xValue < 10) {
                    //make sure the dragging number block is same as the previous dragged one
                    addNumberBlockUpInNumberPad(draggingNumberBlock.getId());
                }
            }

            rollbackDraggingNumberBlock(draggingNumberBlock);
            draggingNumberBlock = null;
        } else if (null != draggingNumberBlockInNumberPad) {
            draggingNumberBlockInNumberPad = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (!isTouchingNumberBlock(screenX, screenY)) {
            if (null != numberBlockListInNumberPad) {
                for (DragObject numberBlockInNumberPad : numberBlockListInNumberPad) {
                    if (TouchUtils.isTouched(numberBlockInNumberPad.getOriginalPosition(), screenX, screenY)) {
                        draggingNumberBlockInNumberPad = numberBlockInNumberPad;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void render() {

        if (isMultiplicationMatrixRequired()) {
            batch.begin();
            initNumberBlocksHorizontalTexture();

            initTexture();

            //draw the number pad
            batch.draw(numberPadAutoCognitaTextureRegion, NUMBER_PAD_POSITION.x, NUMBER_PAD_POSITION.y);

            if (isSwitchModeButtonRequiredToShow()) {
                AutoCognitaTextureRegion autoCognitaTextureRegion = null;
                if (isNumberBlockInHorizontalMode) {
                    autoCognitaTextureRegion = turnButtonAutoCognitaTextureRegion;
                } else {
                    autoCognitaTextureRegion = turnButtonHightedAutoCognitaTextureRegion;
                }
                //draw the turn button
                batch.draw(autoCognitaTextureRegion, TURN_BUTTON_ICON_POSITION.x, TURN_BUTTON_ICON_POSITION.y);
            }

            if (null == yNumberScreenObjectList) {
                yNumberScreenObjectList = new ArrayList<NumberScreenObject>();
                for (int i = 0; i <= 10; i++) {
                    NumberScreenObject numberScreenObject = new NumberScreenObject(i, NUMBER_PAD_POSITION.x - 50,
                            416 + i * 51 + 5, TextFontSizeEnum.FONT_36, true);
                    yNumberScreenObjectList.add(numberScreenObject);
                }
            }

            if (null == xNumberScreenObjectList) {
                xNumberScreenObjectList = new ArrayList<NumberScreenObject>();
                for (int i = 0; i <= 10; i++) {
                    xNumberScreenObjectList.add(new NumberScreenObject(i, NUMBER_PAD_POSITION.x + i * 50, 381, TextFontSizeEnum.FONT_36, true));
                }
            }

            for (NumberScreenObject numberScreenObject : yNumberScreenObjectList) {
                if (numberScreenObject.displayText <= yValue) {
                    numberScreenObject.setColor(ColorProperties.TEXT);
                } else {
                    numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
                }
                ScreenObjectUtils.draw(batch, numberScreenObject);
            }

            for (NumberScreenObject numberScreenObject : xNumberScreenObjectList) {
                if (numberScreenObject.displayText <= xValue) {
                    numberScreenObject.setColor(ColorProperties.TEXT);
                } else {
                    numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
                }
                ScreenObjectUtils.draw(batch, numberScreenObject);
            }

            if (isCountingView) {

                if (isNumberBlockInHorizontalMode) {
                    for (int y = 0; y < yValue; y++) {
                        for (int x = 0; x < xValue; x++) {
                            drawCountingWhiteBlockAutoCognitaTextureRegions(x, y);
                        }
                    }
                } else {
                    for (int x = 0; x < xValue; x++) {
                        for (int y = 0; y < yValue; y++) {
                            drawCountingWhiteBlockAutoCognitaTextureRegions(x, y);
                        }
                    }
                }

            } else if (null != numberBlockListInNumberPad) {

                for (int i = numberBlockListInNumberPad.size() - 1; i >= 0; i--) {
                    drawDragObject(numberBlockListInNumberPad.get(i));
                }
            }
            batch.end();
            super.render();
        }

        batch.begin();
        if (result == 0) {
            drawBigNumber(result, 1350, 1350, FORMULA_START_Y_POSITION);
        } else {
            drawFormula();
        }
        batch.end();

    }

    @Override
    protected boolean isNumberTrayRequired() {
        return false;
    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return null;
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return 0;
    }

    @Override
    protected void beforeDrawNumber(NumberScreenObject numberScreenObject) {

    }

    protected boolean isMultiplicationMatrixRequired() {
        return true;
    }

    private void initTexture() {
        if (null == numberPadAutoCognitaTextureRegion) {
            numberPadAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_TRAY),
                    0, 400, (int) NUMBER_PAD_POSITION.width, (int) NUMBER_PAD_POSITION.height);

            turnButtonAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.ICONS),
                    200, 300, (int) TURN_BUTTON_ICON_POSITION.width, (int) TURN_BUTTON_ICON_POSITION.height);

            turnButtonHightedAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.ICONS),
                    200, 500, (int) TURN_BUTTON_ICON_POSITION.width, (int) TURN_BUTTON_ICON_POSITION.height);


            Texture numberBlocksVerticalTexture = AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_BLOCKS_VERTICAL);

            numberBlocksVerticalAutoCognitaTextureRegions = new AutoCognitaTextureRegion[10];

            for (int i = 0; i < 10; i++) {
                numberBlocksVerticalAutoCognitaTextureRegions[i] = new AutoCognitaTextureRegion(numberBlocksVerticalTexture, 0 + i * 55, 450 - i * 50, 50, 53 + i * 50);
            }

            Texture countingWhiteBlocksTexture = AssetManagerUtils.getTexture(AssetManagerUtils.COUNTING_WHITE_BLOCKS);
            Texture countingWhiteBlocksHighlightedTexture = AssetManagerUtils.getTexture(AssetManagerUtils.COUNTING_WHITE_BLOCKS_HIGHLIGHTED);
            countingWhiteBlockAutoCognitaTextureRegions = new AutoCognitaTextureRegion[100];
            countingWhiteBlockHighlightedAutoCognitaTextureRegions = new AutoCognitaTextureRegion[100];

            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 10; column++) {
                    countingWhiteBlockAutoCognitaTextureRegions[row * 10 + column] = new AutoCognitaTextureRegion(countingWhiteBlocksTexture, 0 + column * 50, 0 + row * 100, 48, 55);

                    countingWhiteBlockHighlightedAutoCognitaTextureRegions[row * 10 + column] = new AutoCognitaTextureRegion(countingWhiteBlocksHighlightedTexture, 0 + column * 50, 0 + row * 100, 48, 55);
                }
            }

        }
    }

    protected boolean isSwitchModeButtonRequiredToShow() {
        return true;
    }

    private void drawCountingWhiteBlockAutoCognitaTextureRegions(int x, int y) {
        AutoCognitaTextureRegion[] autoCognitaTextureRegions = null;
        int value;
        if (isNumberBlockInHorizontalMode) {
            value = y * xValue + x;
        } else {
            value = x * yValue + y;
        }

        if (value == result - 1) {
            autoCognitaTextureRegions = countingWhiteBlockHighlightedAutoCognitaTextureRegions;
        } else {
            autoCognitaTextureRegions = countingWhiteBlockAutoCognitaTextureRegions;
        }
        batch.draw(autoCognitaTextureRegions[value], 400 + x * 50, 440 + y * 50);
    }

    protected void drawFormula() {
        drawFormulaWithoutChangeExistingYPosition(getFormula(), FORMULA_START_X_POSITION, FORMULA_START_Y_POSITION, TextFontSizeEnum.FONT_144);
    }

    protected String getFormula() {
        //draw formula
        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append(isNumberBlockInHorizontalMode ? yValue : xValue);
        formulaBuilder.append(" x ");
        formulaBuilder.append(isNumberBlockInHorizontalMode ? xValue : yValue);
        formulaBuilder.append(" = ");
        formulaBuilder.append(result);
        return formulaBuilder.toString();
    }

}
