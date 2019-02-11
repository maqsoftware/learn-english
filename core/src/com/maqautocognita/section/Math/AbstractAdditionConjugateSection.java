package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.LetterUtils;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.scene2d.ui.TextCell;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractAdditionConjugateSection extends AbstractMathSection {

    protected static final IconPosition NUMBER_TRAY_SINGLE_BLOCK = new IconPosition(0, 0, 60, 60);
    protected static final float GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY = 5;
    private static final float START_X_POSITION_FORMULA = 1400;
    private static final float GAP_BETWEEN_NUMBER_TRAY = 20;
    private static final float Y_GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_TRAY = 1;
    private static final IconPosition NUMBER_TRAY_HEAD_BLOCK = new IconPosition(118, 0, 55, NUMBER_TRAY_SINGLE_BLOCK.height);
    private static final IconPosition NUMBER_TRAY_CONTENT_BLOCK = new IconPosition(173, 0, 50, NUMBER_TRAY_HEAD_BLOCK.height);
    private static final IconPosition NUMBER_TRAY_TAIL_BLOCK =
            new IconPosition(223, 0, NUMBER_TRAY_HEAD_BLOCK.width, NUMBER_TRAY_HEAD_BLOCK.height);


    private static final IconPosition NUMBER_TRAY_DISABLE_SINGLE_BLOCK = new IconPosition(NUMBER_TRAY_SINGLE_BLOCK.x,
            76, NUMBER_TRAY_SINGLE_BLOCK.width, NUMBER_TRAY_SINGLE_BLOCK.height);
    private static final IconPosition NUMBER_TRAY_DISABLE_HEAD_BLOCK =
            new IconPosition(NUMBER_TRAY_HEAD_BLOCK.x, NUMBER_TRAY_DISABLE_SINGLE_BLOCK.y, NUMBER_TRAY_HEAD_BLOCK.width, NUMBER_TRAY_SINGLE_BLOCK.height);
    private static final IconPosition NUMBER_TRAY_DISABLE_CONTENT_BLOCK =
            new IconPosition(NUMBER_TRAY_CONTENT_BLOCK.x, NUMBER_TRAY_DISABLE_SINGLE_BLOCK.y, NUMBER_TRAY_CONTENT_BLOCK.width, NUMBER_TRAY_HEAD_BLOCK.height);
    private static final IconPosition NUMBER_TRAY_DISABLE_TAIL_BLOCK =
            new IconPosition(NUMBER_TRAY_TAIL_BLOCK.x, NUMBER_TRAY_DISABLE_SINGLE_BLOCK.y, NUMBER_TRAY_HEAD_BLOCK.width, NUMBER_TRAY_HEAD_BLOCK.height);

    /**
     * store the start x position for all number tray for the coming drop number blocks
     */
    protected float[] startXPositionNumberTrays;
    /**
     * store all number blocks which are shown on the number tray
     */
    protected List<TextureScreenObject> numberBlockScreenObjectList;
    private List<TextureScreenObject<Integer, Object>> numberTrayScreenObjectList;
    /**
     * store the small counting number which is shown on the first number tray
     */
    private List<NumberScreenObject> countingNumberTextScreenObjectList;


    /**
     * store all formula
     */
    private List<TextCell<Integer>> formulaScreenObjectList;

    public AbstractAdditionConjugateSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void render() {
        initScreenObject();
        batch.begin();
        ScreenObjectUtils.draw(batch, numberTrayScreenObjectList);
        ScreenObjectUtils.draw(batch, countingNumberTextScreenObjectList);
        ScreenObjectUtils.draw(batch, numberBlockScreenObjectList);
        batch.end();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
        if (null != countingNumberTextScreenObjectList) {
            countingNumberTextScreenObjectList.clear();
            countingNumberTextScreenObjectList = null;
        }

    }

    @Override
    protected void resetScreen() {
        super.resetScreen();

        if (null != formulaScreenObjectList) {
            for (TextCell textCell : formulaScreenObjectList) {
                textCell.clearActions();
                textCell.remove();
            }
            formulaScreenObjectList.clear();
            formulaScreenObjectList = null;
        }

        if (null != numberTrayScreenObjectList) {
            numberTrayScreenObjectList.clear();
            numberTrayScreenObjectList = null;
        }

        if (null != numberBlockScreenObjectList) {
            numberBlockScreenObjectList.clear();
            numberBlockScreenObjectList = null;
        }
    }

    private void initScreenObject() {
        initNumberBlocksHorizontalTexture();
        if (null == numberTrayScreenObjectList) {

            Texture numberTrayTexture = AssetManagerUtils.getTexture(MathImagePathUtils.NUMBER_TRAY_IMAGE_PATH);

            numberTrayScreenObjectList = new ArrayList<TextureScreenObject<Integer, Object>>(getNumberOfNumberTray());


            startXPositionNumberTrays = new float[getNumberOfNumberTray()];

            float currentNumberTrayStartYPosition = getNumberTrayStartYPosition();
            for (int i = 0; i < getNumberOfNumberTray(); i++) {
                int number = i + getStartNumber();

                if (getPlayingNumber() <= 1) {
                    AutoCognitaTextureRegion singleNumberBlock = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_SINGLE_BLOCK);
                    AutoCognitaTextureRegion singleNumberBlockDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_SINGLE_BLOCK);
                    numberTrayScreenObjectList.add(
                            new TextureScreenObject<Integer, Object>(i, getNumberTrayStartXPosition(), currentNumberTrayStartYPosition, singleNumberBlock,
                                    singleNumberBlockDisable));
                } else {
                    AutoCognitaTextureRegion head = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_HEAD_BLOCK);
                    AutoCognitaTextureRegion headDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_HEAD_BLOCK);
                    AutoCognitaTextureRegion content = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_CONTENT_BLOCK);
                    AutoCognitaTextureRegion contentDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_CONTENT_BLOCK);
                    AutoCognitaTextureRegion tail = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_TAIL_BLOCK);
                    AutoCognitaTextureRegion tailDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_TAIL_BLOCK);


                    TextureScreenObject headNumberBlock =
                            new TextureScreenObject(number, getNumberTrayStartXPosition(), currentNumberTrayStartYPosition, head, headDisable);

                    numberTrayScreenObjectList.add(headNumberBlock);

                    float startXPosition = headNumberBlock.xPositionInScreen + headNumberBlock.width;

                    for (int c = 0; c < getPlayingNumber() - 2; c++) {
                        numberTrayScreenObjectList.add(new TextureScreenObject(number, startXPosition, currentNumberTrayStartYPosition, content, contentDisable));
                        startXPosition += NUMBER_TRAY_CONTENT_BLOCK.width;
                    }

                    numberTrayScreenObjectList.add(new TextureScreenObject(number, startXPosition, currentNumberTrayStartYPosition, tail, tailDisable));
                }

                drawShortFormula(getPlayingNumber(), number, currentNumberTrayStartYPosition);

                if (null == numberBlockScreenObjectList) {
                    numberBlockScreenObjectList = new ArrayList<TextureScreenObject>();
                }

                float numberBlockStartXPosition = getNumberTrayStartXPosition() + GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY;

                startXPositionNumberTrays[i] = numberBlockStartXPosition;
                if (number > 0) {
                    numberBlockScreenObjectList.add(
                            new TextureScreenObject(numberBlockStartXPosition,
                                    currentNumberTrayStartYPosition + Y_GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_TRAY, numberBlocksAutoCognitaTextureRegions[number - 1]));

                    startXPositionNumberTrays[i] += numberBlocksAutoCognitaTextureRegions[number - 1].getRegionWidth();
                }

                currentNumberTrayStartYPosition -= NUMBER_TRAY_SINGLE_BLOCK.height + GAP_BETWEEN_NUMBER_TRAY;
            }

            countingNumberTextScreenObjectList = new ArrayList<NumberScreenObject>(getPlayingNumber() + 1);

            for (int i = 0; i <= getPlayingNumber(); i++) {
                NumberScreenObject numberBlock = new NumberScreenObject(i,
                        getNumberTrayStartXPosition() + NUMBER_TRAY_CONTENT_BLOCK.width * i
                                //make the number move to left side little bit
                                - GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY,
                        getNumberTrayStartYPosition() + NUMBER_TRAY_SINGLE_BLOCK.height
                                //the space between number tray and number
                                + GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY, TextFontSizeEnum.FONT_36, true);
                numberBlock.isDisabled = true;
                countingNumberTextScreenObjectList.add(numberBlock);
            }

            refreshNumberTrayState();
        }
    }

    protected abstract int getNumberOfNumberTray();

    protected abstract float getNumberTrayStartYPosition();

    protected abstract int getStartNumber();

    protected abstract int getPlayingNumber();

    protected abstract float getNumberTrayStartXPosition();

    /**
     * Draw the give formula in the right hand side in the given yPosition
     *
     * @param playingNumber
     * @param leftNumber
     * @param yPosition
     */
    private void drawShortFormula(int playingNumber, int leftNumber, float yPosition) {

        if (null == formulaScreenObjectList) {
            formulaScreenObjectList = new ArrayList<TextCell<Integer>>();
        }

        float maxWidth = LetterUtils.getTotalWidthOfWord(String.valueOf(10), TextFontSizeEnum.FONT_72);

        final int id = playingNumber - leftNumber;

        float startXPosition = START_X_POSITION_FORMULA;
        formulaScreenObjectList.add(new TextCell<Integer>(id, String.valueOf(leftNumber), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;
        formulaScreenObjectList.add(new TextCell<Integer>(id, "+", TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;
        TextCell answer = new TextCell<Integer>(id, String.valueOf(playingNumber - leftNumber), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition);
        answer.setVisible(false);
        formulaScreenObjectList.add(answer);

        startXPosition += maxWidth;
        formulaScreenObjectList.add(new TextCell<Integer>(id, "=", TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;
        formulaScreenObjectList.add(new TextCell<Integer>(id, String.valueOf(playingNumber), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        for (TextCell textCell : formulaScreenObjectList) {
            textCell.setTextFlip(true);
            stage.addActor(textCell);
        }
    }

    /**
     * the number tray {@link #numberTrayScreenObjectList}
     * will be changed to disabled if the number in the number tray( {@link TextureScreenObject#id}) is smaller than current playing number {@link #getCurrentPlayingNumber()} is
     */
    protected void refreshNumberTrayState() {
        if (CollectionUtils.isNotEmpty(numberTrayScreenObjectList)) {
            for (TextureScreenObject<Integer, Object> numberTrayScreenObject : numberTrayScreenObjectList) {
                numberTrayScreenObject.isDisabled = numberTrayScreenObject.id > getCurrentPlayingNumber();
                numberTrayScreenObject.isTouchAllow = !numberTrayScreenObject.isDisabled;
            }
        }
    }

    protected abstract int getCurrentPlayingNumber();

    @Override
    public void onHelp() {
        //show to the user that the number block is required to drag to the number tray
        //check current required drag number block
        if (isHelpAudioAllowToPlay() && CollectionUtils.isNotEmpty(numberTrayScreenObjectList) && null == draggingNumberBlock) {

            int numberRequired = getPlayingNumber() - getCurrentPlayingNumber();
            moveNumberBlock(numberRequired);
        }
        super.onHelp();
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != draggingNumberBlock) {
            TextureScreenObject<Integer, Object> touchedNumberTray = ScreenObjectUtils.getTouchingScreenObject(numberTrayScreenObjectList, screenX, screenY);
            if (null != touchedNumberTray) {
                if (draggingNumberBlock.getId() + touchedNumberTray.id == getPlayingNumber()) {
                    float startXPosition = startXPositionNumberTrays[getCurrentPlayingNumber() - getStartNumber()];
                    if (getCurrentPlayingNumber() > 0) {
                        startXPosition += GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY;
                    }
                    //if the dragged number block is correct, draw a new number block on the number tray with correct position
                    numberBlockScreenObjectList.add(new TextureScreenObject(startXPosition,
                            touchedNumberTray.yPositionInScreen + Y_GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_TRAY, draggingNumberBlock.getAutoCognitaTextureRegion()));

                    doWhenAnswerIsCorrected();
                }
            }
            abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                @Override
                public void onCorrectSoundPlayed() {

                }
            });
            rollbackDraggingNumberBlock();
        }
    }

    protected void doWhenAnswerIsCorrected() {

        //redraw the short formula
        for (final TextCell<Integer> formula : formulaScreenObjectList) {
            int answer = getPlayingNumber() - getCurrentPlayingNumber();
            if (null != formula.id && formula.id == answer) {
                formula.setVisible(true);
                formula.setHighlighted(true);
                AnimationUtils.doFlash(formula, 2, new Runnable() {
                    @Override
                    public void run() {
                        formula.setHighlighted(false);
                    }
                });
            }
        }

        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                doAfterCorrectSoundPlayed();
            }
        });

    }

    protected abstract void doAfterCorrectSoundPlayed();

    private void moveNumberBlock(final int number) {

        final ImageActor<Integer> numberBlock = numberBlocks[number - 1];

        RunnableAction moveCompleteAction = new RunnableAction();
        moveCompleteAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                rollbackDraggingNumberBlock(numberBlock);
            }
        });

        numberBlock.addAction(new SequenceAction(getNextMovement(), moveCompleteAction));
    }

    protected MoveToAction getNextMovement() {
        float numberTrayYPosition = 0;
        for (TextureScreenObject<Integer, Object> textureScreenObject : numberTrayScreenObjectList) {
            if (textureScreenObject.id == getCurrentPlayingNumber()) {
                numberTrayYPosition = textureScreenObject.yPositionInScreen;
                break;
            }
        }
        MoveToAction moveToAction = new MoveToAction();
        float startXPosition = getMovingBlockDestinationXPosition();
        if (getCurrentPlayingNumber() > 0) {
            startXPosition += GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY;
        }
        moveToAction.setPosition(startXPosition, numberTrayYPosition);
        moveToAction.setDuration(1);
        return moveToAction;
    }

    protected float getMovingBlockDestinationXPosition() {
        return startXPositionNumberTrays[getCurrentPlayingNumber()];
    }
}
