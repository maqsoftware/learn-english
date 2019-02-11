package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
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
public class SubtractionUsingColorBlockSection extends AbstractAdvanceMathSection implements TimerService.ITimerListener {

    protected static final int NUMBER_TRAY_TO_BE_SUBTRACT_START_Y_POSITION = 593;
    protected static final int NUMBER_TRAY_SUBTRACTED_START_Y_POSITION = 663;
    protected static final int NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY = 210;
    private static final int NUMBER_TRAY_RESULT_START_Y_POSITION = 503;
    private static final int NUMBER_BLOCK_GAP = 2;
    private static final int MAXIMUM_NUMBER = 30;
    private static final Rectangle NUMBER_TRAY_SUBTRACTED_FROM_TEXTURE_AREA = new Rectangle(0, 100, 1510, 60);
    private static final IconPosition NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION = new IconPosition(205, 590, 1510, 60);
    private static final IconPosition NUMBER_TRAY_RESULT_ICON_POSITION = new IconPosition(205, 500, 1510, 60);
    private final TimerService timerService;
    protected int result;
    //which is the value store in the number tray subtracted from
    protected int leftValue;
    //which is the value store in the number tray which to be subtract
    protected int rightValue;
    protected List<ImageActor<Integer>> numberBlockListInResultNumberTray;
    private ImageActor<Integer> draggingNumberBlockInSubtractedNumberTray;
    private ImageActor<Integer> draggingNumberBlockInToBeSubtractNumberTray;
    private List<ImageActor<Integer>> numberBlockListInSubtractedNumberTray;
    private List<ImageActor<Integer>> numberBlockListInToBeSubtractNumberTray;

    public SubtractionUsingColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        timerService = new TimerService(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        draggingNumberBlockInSubtractedNumberTray = null;
        draggingNumberBlockInToBeSubtractNumberTray = null;
        resetScreen();
    }

    private void clearNumberTray(List<ImageActor<Integer>> numberBlockListInNumberTray) {
        if (CollectionUtils.isNotEmpty(numberBlockListInNumberTray)) {
            for (ImageActor imageActor : numberBlockListInNumberTray) {
                imageActor.remove();
            }
            numberBlockListInNumberTray.clear();
        }
    }

    @Override
    public void render() {
        initNumberBlocksHorizontalTexture();


        batch.begin();

        if (leftValue > 0) {
            //draw the formula
            StringBuilder formulaBuilder = new StringBuilder();
            formulaBuilder.append(leftValue);
            if (rightValue > 0) {
                formulaBuilder.append(" - ");
                formulaBuilder.append(rightValue);
                if (result > 0) {
                    formulaBuilder.append(" = ");
                    formulaBuilder.append(result);
                }
            }

            drawFormula(formulaBuilder.toString());

        } else {

            //draw the result number in the center of the screen
            drawNumberInScreenCenter(result);

        }

        if (null != numberTrayTexture) {
            //draw the number tray for the number to be subtract
            batch.draw(new AutoCognitaTextureRegion(numberTrayTexture, 0, 200, (int) NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION.width, (int) NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION.height),
                    NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION.x, NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION.y);

            //draw the - icon
            drawMinusScreenObject(110, 670);

            //draw divider line
            batch.draw(getDividerAutoCognitaTextureRegion(), 125, 574);

            //draw the = icon
            drawEqualsScreenObject(105, 575);
            //batch.draw(getEqualsAutoCognitaTextureRegion(), 125, 482, 48, 98);

            //draw the number tray for display the result
            batch.draw(new AutoCognitaTextureRegion(numberTrayTexture, 0, 300, (int) NUMBER_TRAY_RESULT_ICON_POSITION.width, (int) NUMBER_TRAY_RESULT_ICON_POSITION.height),
                    NUMBER_TRAY_RESULT_ICON_POSITION.x, NUMBER_TRAY_RESULT_ICON_POSITION.y);
        }

        batch.end();

        super.render();
    }

    @Override
    protected Rectangle getNumberTrayTextureRegionArea() {
        return NUMBER_TRAY_SUBTRACTED_FROM_TEXTURE_AREA;
    }

    @Override
    protected int getMaximumNumberAboveTheTray() {
        return MAXIMUM_NUMBER;
    }

    @Override
    protected void beforeDrawNumber(NumberScreenObject numberScreenObject) {
        if (numberScreenObject.displayText <= result) {
            numberScreenObject.setColor(ColorProperties.TEXT);
        } else if (result > 0 && numberScreenObject.displayText > result && numberScreenObject.displayText <= leftValue) {
            numberScreenObject.setColor(ColorProperties.HIGHLIGHT);
        } else {
            numberScreenObject.setColor(ColorProperties.DISABLE_TEXT);
        }
    }

    protected void addDraggingNumberBlockToSubtractedNumberTray(ImageActor<Integer> draggingNumberBlock) {
        IconPosition iconPosition = getNumberBlockIconPositionInNumberTray(draggingNumberBlock.getId() - 1, NUMBER_TRAY_SUBTRACTED_START_Y_POSITION,
                numberBlockListInSubtractedNumberTray);

        ImageActor<Integer> numberBlockInSubtractedNumberTray = draggingNumberBlock.clone(iconPosition.x, iconPosition.y);
        numberBlockInSubtractedNumberTray.setId(draggingNumberBlock.getId());

        if (null == numberBlockListInSubtractedNumberTray) {
            numberBlockListInSubtractedNumberTray = new ArrayList<ImageActor<Integer>>();
        }

        numberBlockListInSubtractedNumberTray.add(numberBlockInSubtractedNumberTray);
        stage.addActor(numberBlockInSubtractedNumberTray);
        //if there is a new number block dragged in the subtracted number tray, recaculate the position in the to be subtract number tray
        rearrangeNumberBlockListInToBeSubtractNumberTray();
    }

    protected void addDraggingNumberBlockToToBeSubtractedNumberTray(ImageActor<Integer> draggingNumberBlock) {
        IconPosition iconPosition = getNumberBlockIconPositionInToBeSubtractNumberTray(draggingNumberBlock.getId() - 1);
        ImageActor dragObject = draggingNumberBlock.clone(iconPosition.x, iconPosition.y);
        if (null == numberBlockListInToBeSubtractNumberTray) {
            numberBlockListInToBeSubtractNumberTray = new ArrayList<ImageActor<Integer>>();
        }

        numberBlockListInToBeSubtractNumberTray.add(dragObject);
        stage.addActor(dragObject);
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

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlock) {

            //check if the number block is drop in the subtracted from number tray
            if (isTouchingNumberTray(screenX, screenY)
                    //make sure the result is smaller than the maximum number
                    && draggingNumberBlock.getId() + result <= MAXIMUM_NUMBER && isLeftValueNumberBlockAllowToDrop(
                    draggingNumberBlock.getId())) {
                leftValue += draggingNumberBlock.getId();

                result = leftValue - rightValue;

                addDraggingNumberBlockToSubtractedNumberTray(draggingNumberBlock);

                whenLeftValueDroppedInTheNumberTray();
            } else if (TouchUtils.isTouched(NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION, screenX, screenY) && leftValue > 0 &&
                    draggingNumberBlock.getId() + rightValue <= leftValue &&
                    isRightValueNumberBlockAllowToDrop(draggingNumberBlock.getId())) {
                rightValue += draggingNumberBlock.getId();

                result = leftValue - rightValue;

                addDraggingNumberBlockToToBeSubtractedNumberTray(draggingNumberBlock);

                whenRightValueDroppedInTheNumberTray();
            }


            rollbackDraggingNumberBlock(draggingNumberBlock);
            draggingNumberBlock = null;

            if (leftValue > 0 && rightValue > 0) {
                //the result should only appear when both left and right value have value
                //calculated the number of block required show in the result number tray
                numberBlockListInResultNumberTray = getNumberBlockList(result, 503);
            }

            if (leftValue > 0 || rightValue > 0) {
                timerService.startTimer(null);
            }
        }

        if (null != draggingNumberBlockInSubtractedNumberTray) {
            //if the number block in the subtracted number tray is dragging
            if (isTouchingNumberTray(screenX, screenY)) {
                rollbackDraggingNumberBlock(draggingNumberBlockInSubtractedNumberTray);
                draggingNumberBlockInSubtractedNumberTray = null;
            } else {
                //if the number block is drop outside the number tray
                leftValue -= draggingNumberBlockInSubtractedNumberTray.getId();
                rightValue = 0;
                result = 0;
                clearNumberTray(numberBlockListInSubtractedNumberTray);
                clearNumberTray(numberBlockListInToBeSubtractNumberTray);
                draggingNumberBlockInSubtractedNumberTray.remove();
                draggingNumberBlockInSubtractedNumberTray = null;
                reloadResultInResultNumberTray();
            }
        }

        if (null != draggingNumberBlockInToBeSubtractNumberTray) {
            //if the number block in the to be subtract number tray is dragging
            if (TouchUtils.isTouched(NUMBER_TRAY_TO_BE_SUBTRACT_ICON_POSITION, screenX, screenY)) {
                rollbackDraggingNumberBlock(draggingNumberBlockInToBeSubtractNumberTray);
                draggingNumberBlockInToBeSubtractNumberTray = null;
            } else {
                //if the number block is drop outside the number tray
                rightValue = 0;
                result = 0;
                draggingNumberBlockInToBeSubtractNumberTray.remove();
                draggingNumberBlockInToBeSubtractNumberTray = null;
                clearNumberTray(numberBlockListInToBeSubtractNumberTray);
                reloadResultInResultNumberTray();
            }

        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (!isTouchingNumberBlock(screenX, screenY)) {
            if (null != numberBlockListInSubtractedNumberTray) {
                draggingNumberBlockInSubtractedNumberTray =
                        ScreenObjectUtils.getTouchingActor(numberBlockListInSubtractedNumberTray, screenX, screenY);
                if (null != draggingNumberBlockInSubtractedNumberTray) {
                    draggingNumberBlockInSubtractedNumberTray.setTouchingPosition(screenX, screenY);
                    return;
                }
            }

            if (null != numberBlockListInToBeSubtractNumberTray) {
                draggingNumberBlockInToBeSubtractNumberTray =
                        ScreenObjectUtils.getTouchingActor(numberBlockListInToBeSubtractNumberTray, screenX, screenY);
                if (null != draggingNumberBlockInToBeSubtractNumberTray) {
                    draggingNumberBlockInToBeSubtractNumberTray.setTouchingPosition(screenX, screenY);
                    return;
                }
            }
        }
    }

    protected void reloadResultInResultNumberTray() {
        rearrangeNumberBlockInTray(result, numberBlockListInResultNumberTray, NUMBER_TRAY_RESULT_START_Y_POSITION);
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != draggingNumberBlockInSubtractedNumberTray) {
            draggingNumberBlockInSubtractedNumberTray.setPosition(screenX, screenY);
        }

        if (null != draggingNumberBlockInToBeSubtractNumberTray) {
            draggingNumberBlockInToBeSubtractNumberTray.setPosition(screenX, screenY);
        }
    }

    @Override
    protected void resetScreen() {
        result = 0;
        leftValue = 0;
        rightValue = 0;
        clearNumberTray(numberBlockListInResultNumberTray);
        clearNumberTray(numberBlockListInSubtractedNumberTray);
        clearNumberTray(numberBlockListInToBeSubtractNumberTray);
    }

    @Override
    public void beforeStartTimer() {

    }

    @Override
    public void onTimerComplete(Object threadIndicator) {
        if (leftValue > 0) {
            rearrangeNumberBlockInTray(leftValue, numberBlockListInSubtractedNumberTray, NUMBER_TRAY_SUBTRACTED_START_Y_POSITION);
        }

        if (rightValue > 0) {
            //rearrangeNumberBlockInTray(rightValue, numberBlockListInToBeSubtractNumberTray, NUMBER_TRAY_TO_BE_SUBTRACT_START_Y_POSITION);
            rearrangeNumberBlockListInToBeSubtractNumberTray();
        }


    }

    private void rearrangeNumberBlockInTray(int displayValue, List<ImageActor<Integer>> dragObjectList, int startYPosition) {
        clearNumberTray(dragObjectList);
        if (null == dragObjectList) {
            dragObjectList = new ArrayList<ImageActor<Integer>>();
        }

        dragObjectList.addAll(getNumberBlockList(displayValue, startYPosition));
    }

    /**
     * rearrange all number block which are stayed in the to be number tray
     */
    private void rearrangeNumberBlockListInToBeSubtractNumberTray() {

        if (null == numberBlockListInToBeSubtractNumberTray) {
            numberBlockListInToBeSubtractNumberTray = new ArrayList<ImageActor<Integer>>();
        } else {
            clearNumberTray(numberBlockListInToBeSubtractNumberTray);
        }

        int number = rightValue;

        while (number > 0) {
            ImageActor<Integer> dragObject = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL);

            if (number > 10) {
                dragObject.setId(10);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[9]);
                IconPosition iconPosition = getNumberBlockIconPositionInToBeSubtractNumberTray(9);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                number -= 10;
            } else {
                dragObject.setId(number);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[number - 1]);
                IconPosition iconPosition = getNumberBlockIconPositionInToBeSubtractNumberTray(number - 1);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                number -= number;
            }

            numberBlockListInToBeSubtractNumberTray.add(dragObject);
            stage.addActor(dragObject);
        }

    }

    private List<ImageActor<Integer>> getNumberBlockList(int number, int startYPosition) {

        List<ImageActor<Integer>> dragObjectList = new ArrayList<ImageActor<Integer>>();

        while (number > 0) {
            ImageActor<Integer> dragObject = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL);

            if (number > 10) {
                dragObject.setId(10);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[9]);
                IconPosition iconPosition = getNumberBlockIconPositionInNumberTray(9, startYPosition, dragObjectList);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                number -= 10;
            } else {
                dragObject.setId(number);
                dragObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[number - 1]);
                IconPosition iconPosition = getNumberBlockIconPositionInNumberTray(number - 1, startYPosition, dragObjectList);
                dragObject.setOrigin(iconPosition.x, iconPosition.y);
                dragObject.setPosition(dragObject.getOriginX(), dragObject.getOriginY());
                dragObject.setSize(iconPosition.width, iconPosition.height);
                number -= number;
            }
            dragObjectList.add(dragObject);
            stage.addActor(dragObject);
        }

        return dragObjectList;
    }

    /**
     * Get the icon position which is specify the number block position in the to be subtract number tray, which will get the total width of the number block(s) in the subtracted number tray,
     * make sure that the blocks in the to be subtracted number tray are always aligned with the right-most edge of the blocks in the green row(subtracted number tray)
     *
     * @param number
     * @return
     */
    private IconPosition getNumberBlockIconPositionInToBeSubtractNumberTray(int number) {

        int currentNumberBlockWidth = numberBlocksAutoCognitaTextureRegions[number].getRegionWidth();


        return new IconPosition(
                NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY +
                        //get the total width of number block in Subtracted Number Tray
                        (getTotalBlockWidth(numberBlockListInSubtractedNumberTray) -
                                currentNumberBlockWidth -
                                getTotalBlockWidth(numberBlockListInToBeSubtractNumberTray) -
                                NUMBER_BLOCK_GAP * numberBlockListInToBeSubtractNumberTray.size()),
                NUMBER_TRAY_TO_BE_SUBTRACT_START_Y_POSITION,
                numberBlocksAutoCognitaTextureRegions[number].getRegionWidth(),
                numberBlocksAutoCognitaTextureRegions[number].getRegionHeight());
    }

    protected IconPosition getNumberBlockIconPositionInNumberTray(int number, int startYPosition,
                                                                  List<ImageActor<Integer>> droppedNumberBlockList) {
        int numberOfDroppedObjectInTheNumberTray = null != droppedNumberBlockList ? droppedNumberBlockList.size() : 0;
        return new IconPosition(NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY + getTotalBlockWidth(droppedNumberBlockList) +
                NUMBER_BLOCK_GAP * numberOfDroppedObjectInTheNumberTray, startYPosition,
                numberBlocksAutoCognitaTextureRegions[number].getRegionWidth(), numberBlocksAutoCognitaTextureRegions[number].getRegionHeight());
    }

    private int getTotalBlockWidth(List<ImageActor<Integer>> dragObjectList) {
        int width = 0;
        if (null != dragObjectList) {
            for (ImageActor numberBlockInNumberTray : dragObjectList) {
                width += numberBlockInNumberTray.getWidth();
            }
        }

        return width;
    }

}
