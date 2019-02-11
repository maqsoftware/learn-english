package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.service.TimerService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAdditionUsingColorBlockSection extends AbstractAdvanceMathSection implements TimerService.ITimerListener {

    private static final int NUMBER_BLOCK_GAP = 5;
    private static final Rectangle NUMBER_TRAY_TEXTURE_AREA = new Rectangle(0, 100, 1510, 60);
    private static final int MAXIMUM_NUMBER = 30;
    private final TimerService timerService;
    protected int result;
    //store the number of the left side in the formula
    protected int leftValue;
    //store the number of the right side in the formula
    protected int rightValue;
    private ImageActor<Integer> draggingNumberBlockInNumberTray;
    private List<ImageActor<Integer>> numberBlockListInNumberTray;

    public AbstractAdditionUsingColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
        draggingNumberBlockInNumberTray = null;
    }

    private void clearNumberBlockListInTray() {
        if (CollectionUtils.isNotEmpty(numberBlockListInNumberTray)) {
            for (ImageActor numberBlock : numberBlockListInNumberTray) {
                numberBlock.remove();
            }
            numberBlockListInNumberTray.clear();
        }
    }

    @Override
    public void render() {
        initNumberBlocksHorizontalTexture();

        super.render();

        batch.begin();

        if (leftValue > 0 || rightValue > 0) {
            //draw the formula
            StringBuilder formulaBuilder = new StringBuilder();

            if (leftValue > 0) {
                formulaBuilder.append(leftValue);
            }
            if (rightValue > 0) {
                formulaBuilder.append(" + ");
                formulaBuilder.append(rightValue);
            }
            if (result > 0) {
                formulaBuilder.append(" = ");
                formulaBuilder.append(result);
            }

            drawFormula(formulaBuilder.toString());
        } else {

            //draw the result number in the center of the screen
            drawNumberInScreenCenter(result);

        }

        batch.end();

    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return NUMBER_TRAY_TEXTURE_AREA;
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return MAXIMUM_NUMBER;
    }

    @Override
    protected void beforeDrawNumber(NumberScreenObject numberScreenObject) {
        if (numberScreenObject.displayText <= leftValue + rightValue) {
            numberScreenObject.setColor(ColorProperties.TEXT);
        } else {
            numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
        }
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        leftValue = result;
        rightValue = 0;
        rearrangeNumberBlockInTray();
    }

    private void rearrangeNumberBlockInTray() {
        clearNumberBlockListInTray();

        int remainValue = leftValue + rightValue;

        while (remainValue > 0) {
            ImageActor<Integer> dragObject = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL);

            if (remainValue > 10) {
                IconPosition iconPosition = getNumberBlockIconPositionInNumberTray(9);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[9]);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                dragObject.setId(10);
                remainValue -= 10;
            } else {
                IconPosition iconPosition = getNumberBlockIconPositionInNumberTray(remainValue - 1);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[remainValue - 1]);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                dragObject.setId(remainValue);
                remainValue -= remainValue;
            }
            numberBlockListInNumberTray.add(dragObject);
            stage.addActor(dragObject);

        }
    }

    private IconPosition getNumberBlockIconPositionInNumberTray(int number) {
        return new IconPosition(numberTrayStartXPosition + getWidthOfBlockListInNumberTray() + NUMBER_BLOCK_GAP *
                (null == numberBlockListInNumberTray ? 0 : numberBlockListInNumberTray.size()),
                663,
                numberBlocksAutoCognitaTextureRegions[number].getRegionWidth(), numberBlocksAutoCognitaTextureRegions[number].getRegionHeight());
    }

    protected int getWidthOfBlockListInNumberTray() {
        int width = 0;
        if (null != numberBlockListInNumberTray) {
            for (ImageActor numberBlockInNumberTray : numberBlockListInNumberTray) {
                width += numberBlockInNumberTray.getWidth();
            }
        }

        return width;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlock) {
            //check if the number block is drop in the number tray
            if (isTouchingNumberTray(screenX, screenY)
                    //make sure the result is smaller than the maximum number
                    && draggingNumberBlock.getId() + result <= MAXIMUM_NUMBER
                    ) {
                if (leftValue == 0 && isLeftValueNumberBlockAllowToDrop(draggingNumberBlock.getId())) {
                    //if there is no value in the number tray yet
                    leftValue = draggingNumberBlock.getId();
                    addNumberBlockInTheTray(draggingNumberBlock);
                    whenLeftValueDroppedInTheNumberTray();
                } else if (
                    //if the left value number block is dropped
                        leftValue > 0 &&
                                //and the right value number block is not dropped yet
                                rightValue == 0 && isRightValueNumberBlockAllowToDrop(draggingNumberBlock.getId())) {
                    rightValue = draggingNumberBlock.getId();
                    addNumberBlockInTheTray(draggingNumberBlock);
                    whenRightValueDroppedInTheNumberTray();
                }

                if (leftValue > 0 && rightValue > 0) {
                    //the result will be shown only when both left and right value are dropped
                    result = leftValue + rightValue;
                }

            }
            rollbackDraggingNumberBlock();
        }

        if (null != draggingNumberBlockInNumberTray) {
            //if the number block in the number tray is dragged
            if (TouchUtils.isTouched(NUMBER_TRAY_TEXTURE_AREA, screenX, screenY)) {
                rollbackDraggingNumberBlock(draggingNumberBlockInNumberTray);
            } else {
                // if the number block is drop outside the number tray
                if (leftValue == draggingNumberBlockInNumberTray.getId()) {
                    leftValue = rightValue;
                    rightValue = 0;
                }
                if (rightValue == draggingNumberBlockInNumberTray.getId()) {
                    rightValue = 0;
                }

                draggingNumberBlockInNumberTray.remove();
                numberBlockListInNumberTray.remove(draggingNumberBlockInNumberTray);
                rearrangeNumberBlockInTray();
                result = 0;
            }

            draggingNumberBlockInNumberTray = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (!isTouchingNumberBlock(screenX, screenY)) {
            if (null != numberBlockListInNumberTray) {
                for (ImageActor numberBlockInTray : numberBlockListInNumberTray) {
                    if (TouchUtils.isTouched(numberBlockInTray, screenX, screenY)) {
                        draggingNumberBlockInNumberTray = numberBlockInTray;
                        draggingNumberBlockInNumberTray.setTouchingPosition(screenX, screenY);
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (null != draggingNumberBlockInNumberTray) {
            draggingNumberBlockInNumberTray.setPosition(screenX, screenY);
        }
    }

    @Override
    protected void resetScreen() {
        result = 0;
        leftValue = 0;
        rightValue = 0;
        clearNumberBlockListInTray();
    }

    protected boolean isLeftValueNumberBlockAllowToDrop(int leftValue) {
        return true;
    }

    protected boolean isRightValueNumberBlockAllowToDrop(int rightValue) {
        return true;
    }

    protected void whenLeftValueDroppedInTheNumberTray() {

    }

    protected void whenRightValueDroppedInTheNumberTray() {

    }

    protected void addNumberBlockInTheTray(ImageActor<Integer> numberBlock) {
        if (null == numberBlockListInNumberTray) {
            numberBlockListInNumberTray = new ArrayList<ImageActor<Integer>>();
        }
        IconPosition position = getNumberBlockIconPositionInNumberTray(numberBlock.getId() - 1);
        ImageActor<Integer> numberBlockInTheTray = numberBlock.clone(position.x, position.y);
        stage.addActor(numberBlockInTheTray);
        numberBlockListInNumberTray.add(numberBlockInTheTray);
    }

}
