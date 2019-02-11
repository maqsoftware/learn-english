package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AnimateTextureScreenObject;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractLargeNumberBlockSection extends AbstractMathMicrophoneSection {

    /**
     * The space between number pad and small number
     */
    private static final int GAP_BETWEEN_NUMBER_PAD_AND_SMALL_NUMBER = 5;


    /**
     * The vertical space between result pag and the result digit below the number pad
     */
    private static final int GAP_BETWEEN_NUMBER_PAD_AND_RESULT_DIGIT = 50;
    private static final Vector2 NUMBER_PAD_SIZE = new Vector2(620, 540);
    /**
     * store the size of number block
     */
    private static final Vector2 NUMBER_BLOCK_SIZE = new Vector2(56, 59);
    /**
     * Store the width for those three part in  the number pad
     */
    private static final float PART_WIDTH_IN_NUMBER_PAD = NUMBER_PAD_SIZE.x / 3;
    private static final int MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN = 10;
    protected final int maximumValue;
    /**
     * store the number 1 block list which is show on the number pad
     */
    protected List<AnimateTextureScreenObject> number1BlockList;
    //store the result of the sum of the  number block which the user has dragged on the number pad
    protected int result;
    protected TextureScreenObject numberPadTextureScreenObject;
    /**
     * store the number 10 block list which is show on the number pad
     */
    protected List<AnimateTextureScreenObject> number10BlockList;
    /**
     * The small number text which are display above the number pad, there are mainly 3 numbers 100,10 and 1
     */
    private NumberScreenObject smallNumber100;
    private NumberScreenObject smallNumber10;
    private NumberScreenObject smallNumber1;
    /**
     * store the number 100 block list which is show on the number pad
     */
    private List<AnimateTextureScreenObject> number100BlockList;
    /**
     * store the list of number blocking which are currently touching
     */
    private List<AnimateTextureScreenObject> touchingNumberBlockList;
    /**
     * the number which is used to display under the number pad, number digit, tens digit and hundred digit which are showing the total number in the number pad
     */
    private NumberScreenObject numberDigitTextScreenObject;
    private NumberScreenObject tensDigitTextScreenObject;
    private NumberScreenObject hundredDigitTextScreenObject;
    /**
     * Below are the three parts vertically in the number pad, which are mainly used to store the start x position for each area.
     * They are mainly used to indicate their belongs digit, for example the {@link #startXPositionInFirstPartOfNumberPadArea} is mainly used to store the hundred digit number blocks.
     */
    private float startXPositionInFirstPartOfNumberPadArea;
    private float startXPositionInSecondPartOfNumberPadArea;
    private float startXPositionInThirdPartOfNumberPadArea;
    private float startYPositionNumberPad;

    public AbstractLargeNumberBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int maximumValue, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.maximumValue = maximumValue;
        startXPositionInFirstPartOfNumberPadArea = ScreenUtils.getXPositionForCenterObject(NUMBER_PAD_SIZE.x, getExpectedScreenWidth());
        startYPositionNumberPad = ScreenUtils.getBottomYPositionForCenterObject(NUMBER_PAD_SIZE.y, getExpectedScreenHeight());

        startXPositionInThirdPartOfNumberPadArea = startXPositionInFirstPartOfNumberPadArea + NUMBER_PAD_SIZE.x - PART_WIDTH_IN_NUMBER_PAD;
        startXPositionInSecondPartOfNumberPadArea = startXPositionInFirstPartOfNumberPadArea + PART_WIDTH_IN_NUMBER_PAD;

    }

    @Override
    protected void render() {
        if (null == numberPadTextureScreenObject) {
            numberPadTextureScreenObject = new TextureScreenObject(AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_PAD), startXPositionInFirstPartOfNumberPadArea,
                    startYPositionNumberPad, NUMBER_PAD_SIZE.x, NUMBER_PAD_SIZE.y);

            numberDigitTextScreenObject = new NumberScreenObject(null, 0, numberPadTextureScreenObject.yPositionInScreen - GAP_BETWEEN_NUMBER_PAD_AND_RESULT_DIGIT, TextFontSizeEnum.FONT_288, false);

            numberDigitTextScreenObject.xPositionInScreen = startXPositionInThirdPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - numberDigitTextScreenObject.width) / 2;

            tensDigitTextScreenObject = new NumberScreenObject(null, 0, numberPadTextureScreenObject.yPositionInScreen - GAP_BETWEEN_NUMBER_PAD_AND_RESULT_DIGIT, TextFontSizeEnum.FONT_288, false);
            tensDigitTextScreenObject.xPositionInScreen = numberPadTextureScreenObject.xPositionInScreen + (numberPadTextureScreenObject.width - tensDigitTextScreenObject.width) / 2;

            hundredDigitTextScreenObject = new NumberScreenObject(null, 0, numberPadTextureScreenObject.yPositionInScreen - GAP_BETWEEN_NUMBER_PAD_AND_RESULT_DIGIT, TextFontSizeEnum.FONT_288, false);

            hundredDigitTextScreenObject.xPositionInScreen = startXPositionInFirstPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - hundredDigitTextScreenObject.width) / 2;

        }
        if (null == smallNumber100) {

            float startYPosition = numberPadTextureScreenObject.yPositionInScreen + numberPadTextureScreenObject.height + GAP_BETWEEN_NUMBER_PAD_AND_SMALL_NUMBER;

            //draw the 100 text above the number pad
            smallNumber100 = new NumberScreenObject(100, 0, startYPosition, TextFontSizeEnum.FONT_48, true);
            smallNumber100.xPositionInScreen =
                    startXPositionInFirstPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - smallNumber100.width) / 2;


            //draw the 10 text above the number pad
            smallNumber10 = new NumberScreenObject(10, 0, startYPosition, TextFontSizeEnum.FONT_48, true);
            smallNumber10.xPositionInScreen =
                    startXPositionInSecondPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - smallNumber10.width) / 2;

            //draw the 1 text above the number pad
            smallNumber1 = new NumberScreenObject(1, 0, startYPosition, TextFontSizeEnum.FONT_48, true);
            smallNumber1.xPositionInScreen =
                    startXPositionInThirdPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - smallNumber1.width) / 2;


        }

        batch.begin();
        ScreenObjectUtils.draw(batch, numberPadTextureScreenObject);
        ScreenObjectUtils.draw(batch, smallNumber100);
        ScreenObjectUtils.draw(batch, smallNumber10);
        ScreenObjectUtils.draw(batch, smallNumber1);

        numberDigitTextScreenObject.setDisplayText(result % 10);
        numberDigitTextScreenObject.xPositionInScreen = startXPositionInThirdPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - numberDigitTextScreenObject.width) / 2;
        tensDigitTextScreenObject.setDisplayText(result < 10 ? null : result / 10 % 10);
        tensDigitTextScreenObject.xPositionInScreen = numberPadTextureScreenObject.xPositionInScreen + (numberPadTextureScreenObject.width - tensDigitTextScreenObject.width) / 2;
        hundredDigitTextScreenObject.setDisplayText(result < 100 ? null : result / 100 % 100);
        hundredDigitTextScreenObject.xPositionInScreen = startXPositionInFirstPartOfNumberPadArea + (PART_WIDTH_IN_NUMBER_PAD - hundredDigitTextScreenObject.width) / 2;

        ScreenObjectUtils.draw(batch, numberDigitTextScreenObject);
        ScreenObjectUtils.draw(batch, tensDigitTextScreenObject);
        ScreenObjectUtils.draw(batch, hundredDigitTextScreenObject);

        if (isNumberBlocksShowRequired()) {
            //put higher block behind lower block make  that the 3D effect look
            drawNumberBlockFromTopToBottom(number1BlockList);
            drawNumberBlockFromTopToBottom(number10BlockList);
            drawNumberBlockFromTopToBottom(number100BlockList);
        }

        batch.end();

        if (isMicrophoneRequired()) {
            super.render();
        }
    }

    protected boolean isNumberBlocksShowRequired() {
        return true;
    }

    private void drawNumberBlockFromTopToBottom(List<AnimateTextureScreenObject> numberBlockList) {
        if (CollectionUtils.isNotEmpty(numberBlockList)) {
            for (int i = numberBlockList.size() - 1; i >= 0; i--) {
                ScreenObjectUtils.draw(batch, numberBlockList.get(i));
            }
        }
    }

    protected boolean isMicrophoneRequired() {
        return true;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        touchingNumberBlockList = null;
    }

    @Override
    protected int getNumberRequiredForSpeechRecognize() {
        return result;
    }

    @Override
    protected String[] getAllRequiredTextureName() {

        return new String[]{AssetManagerUtils.NUMBER_1_BLOCK, AssetManagerUtils.NUMBER_PAD, AssetManagerUtils.NUMBER_10_BLOCK,
                AssetManagerUtils.NUMBER_100_BLOCK};
    }

    @Override
    public void dispose() {
        super.dispose();

        numberPadTextureScreenObject = null;
        smallNumber100 = null;
        smallNumber10 = null;
        smallNumber1 = null;

        numberDigitTextScreenObject = null;
        tensDigitTextScreenObject = null;
        hundredDigitTextScreenObject = null;

        resetScreen();
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (CollectionUtils.isNotEmpty(touchingNumberBlockList)) {
            int touchingNumber = touchingNumberBlockList.get(0).id;
            if (screenY > getNextBLockYPosition(touchingNumberBlockList)) {
                switch (touchingNumber) {
                    case 1:
                        addNumber1Block();
                        break;
                    case 10:
                        addNumber10Block();
                        break;
                    case 100:
                        addNumber100Block();
                        break;
                }
            } else if (screenY < getNextBLockYPosition(touchingNumberBlockList) - NUMBER_BLOCK_SIZE.y) {
                //if the touch y position is lower than the last block in the list
                removeLastNumberBlock(touchingNumberBlockList, touchingNumber);
            }
        }
    }

    protected void resetScreen() {


        if (null != number1BlockList) {
            number1BlockList.clear();
            number1BlockList = null;
        }

        if (null != number10BlockList) {
            number10BlockList.clear();
            number10BlockList = null;
        }

        if (null != number100BlockList) {
            number100BlockList.clear();
            number100BlockList = null;
        }

        if (null != touchingNumberBlockList) {
            touchingNumberBlockList.clear();
            touchingNumberBlockList = null;
        }

        result = 0;
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (!microphoneService.touchDown(screenX, screenY) && isAllowTouchNumberBlock()) {
            //check if the number pad is touching
            if (null != ScreenObjectUtils.getTouchingScreenObject(numberPadTextureScreenObject, screenX, screenY)) {
                if (isNumber1BlockTouching(screenX)) {
                    //if number 1 block list is touching
                    addNumber1Block();
                    touchingNumberBlockList = number1BlockList;
                } else if (isNumber10BlockTouching(screenX)) {
                    //if number 10 block list is touching
                    addNumber10Block();
                    touchingNumberBlockList = number10BlockList;
                } else if (isNumber100BlockTouching(screenX)) {
                    //if number 100 block list is touching
                    addNumber100Block();
                    touchingNumberBlockList = number100BlockList;
                }
            }
        }
    }

    protected boolean isAllowTouchNumberBlock() {
        return true;
    }

    private boolean isNumber1BlockTouching(int screenX) {
        return TouchUtils.isTouchedX(startXPositionInThirdPartOfNumberPadArea, PART_WIDTH_IN_NUMBER_PAD, screenX);
    }

    private boolean isNumber10BlockTouching(int screenX) {
        return TouchUtils.isTouchedX(startXPositionInSecondPartOfNumberPadArea, PART_WIDTH_IN_NUMBER_PAD, screenX);
    }

    private boolean isNumber100BlockTouching(int screenX) {
        return TouchUtils.isTouchedX(startXPositionInFirstPartOfNumberPadArea, PART_WIDTH_IN_NUMBER_PAD, screenX);
    }

    protected void addNumber1Block() {

        if (null == number1BlockList) {
            //the  maximum number of the number block is only 1
            number1BlockList = new ArrayList<AnimateTextureScreenObject>(MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN);
        }

        addNumberBlock(number1BlockList, startXPositionInThirdPartOfNumberPadArea, 1, AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_1_BLOCK));
    }

    protected void addNumber10Block() {

        if (null == number10BlockList) {
            //the  maximum number of the number block is only 10
            number10BlockList = new ArrayList<AnimateTextureScreenObject>(MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN);
        }

        addNumberBlock(number10BlockList, startXPositionInSecondPartOfNumberPadArea, 10, AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_10_BLOCK));
    }

    protected void addNumber100Block() {

        if (null == number100BlockList) {
            //the  maximum number of the number block is only 1
            number100BlockList = new ArrayList<AnimateTextureScreenObject>(MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN);
        }

        addNumberBlock(number100BlockList, startXPositionInFirstPartOfNumberPadArea, 100, AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_100_BLOCK));
    }

    private List<AnimateTextureScreenObject> addNumberBlock(List<AnimateTextureScreenObject> numberBlockList, float startXPosition, int value, Texture numberBlockTexture) {


        if (null == numberBlockList) {
            //the  maximum number of the number block is only 10
            numberBlockList = new ArrayList<AnimateTextureScreenObject>(MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN);
        }

        if (numberBlockList.size() < MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN) {

            if (result + value > maximumValue) {
                //if the result is larger than the maximum value, return the unchanged numberBlock list directly
                return numberBlockList;
            }

            result += value;

            numberBlockList.add(new AnimateTextureScreenObject(value, numberBlockTexture,
                    //make sure the number block is show vertically in center in that area
                    getNumberBlockCenterXPositionInThePad(startXPosition),
                    getNextBLockYPosition(numberBlockList), NUMBER_BLOCK_SIZE.x, NUMBER_BLOCK_SIZE.y));

            //check if the block is reach the maximum after 1 block added
            if (numberBlockList.size() >= MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN) {

                if (isAllowCarryValueToNextColumn(value)) {
                    //resize
                    resizeNumberBlockToNextColumn(numberBlockList);
                }

                return numberBlockList;
            }
        }

        afterNumberBlockAdded();

        return numberBlockList;
    }

    /**
     * it will be call when a new number block is added
     */
    protected void afterNumberBlockAdded() {

    }

    private boolean isAllowCarryValueToNextColumn(int value) {
        return !isReachMaximumValue() && (
                (100 == value && isNumberBlock100ReachMaximum()) ||
                        (10 == value && isNumberBlock10ReachMaximum()) || (1 == value && isNumberBlock1ReachMaximum()));
    }

    private boolean isNumberBlock100ReachMaximum() {
        return CollectionUtils.isNotEmpty(number100BlockList) && MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN == number100BlockList.size();
    }

    private boolean isNumberBlock10ReachMaximum() {
        return CollectionUtils.isNotEmpty(number10BlockList) && MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN == number10BlockList.size();
    }

    private boolean isNumberBlock1ReachMaximum() {
        return CollectionUtils.isNotEmpty(number1BlockList) && MAXIMUM_NUMBER_OF_BLOCK_IN_COLUMN == number1BlockList.size();
    }

    private boolean isReachMaximumValue() {
        return result >= maximumValue;
    }

    private float getNumberBlockCenterXPositionInThePad(float startXPosition) {
        return startXPosition + (PART_WIDTH_IN_NUMBER_PAD - NUMBER_BLOCK_SIZE.x) / 2;
    }

    /**
     * It will do a animation that reduce the size to 0 for the given numberBlockList, and move to the next column in the left, which mean the next digit will be +1
     * <p/>
     * For example, if the given numberBlockList is the number digit, it will be move to the column ten digit, and the given numberBlockList will become empty finally after the animation is done.
     *
     * @param numberBlockList
     */
    private void resizeNumberBlockToNextColumn(final List<AnimateTextureScreenObject> numberBlockList) {


        final int value = numberBlockList.get(0).id;

        float targetXPosition = 0;

        if (1 == value) {
            targetXPosition = getNumberBlockCenterXPositionInThePad(startXPositionInSecondPartOfNumberPadArea);
        } else if (10 == value) {
            targetXPosition = getNumberBlockCenterXPositionInThePad(startXPositionInFirstPartOfNumberPadArea);
        } else {
            //in the section, we only teach 3 digits, if the given numberBlockList is hundred digit, no more action will be do, because it already reach the max.
            return;
        }


        if (isShowing) {
            //disable the touch until all animation are done
            abstractAutoCognitaScreen.setTouchAllow(false);
        }
        for (final AnimateTextureScreenObject numberBlock : numberBlockList) {
            if (numberBlock.finalHeight > 0) {
                numberBlock.setTargetPosition(targetXPosition, startYPositionNumberPad);
                numberBlock.setSize(0, 0);
                numberBlock.setAnimationCompleteDependsAttribute(AnimateTextureScreenObject.AnimationCompleteDependsAttribute.SIZE);

                numberBlock.setAnimationListener(new AnimateTextureScreenObject.IAnimationListener() {
                    @Override
                    public void onComplete() {
                        numberBlockList.remove(numberBlock);
                        result -= value;

                        if (CollectionUtils.isEmpty(numberBlockList)) {
                            //which mean all animation in the number block list are done
                            if (1 == value) {
                                addNumber10Block();
                            } else if (10 == value) {
                                addNumber100Block();
                            }
                            abstractAutoCognitaScreen.setTouchAllow(true);
                        }
                    }
                });
            }

        }
    }

    private void removeLastNumberBlock(List<AnimateTextureScreenObject> numberBlockList, int value) {
        if (null != numberBlockList) {
            if (numberBlockList.size() > 0) {
                numberBlockList.remove(numberBlockList.size() - 1);
                result -= value;
                afterNumberBlockRemoved();
            }
        }
    }

    protected void afterNumberBlockRemoved() {

    }

    private float getNextBLockYPosition(List<AnimateTextureScreenObject> numberBlockList) {
        float startYPosition = startYPositionNumberPad
                //should be start little higher than the number pad
                + 10;
        if (CollectionUtils.isNotEmpty(numberBlockList)) {
            for (TextureScreenObject numberBlock : numberBlockList) {
                startYPosition += numberBlock.height
                        //no space between each block and even make them
                        - 9;
            }
        }

        return startYPosition;
    }


}
