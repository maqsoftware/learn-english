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
import com.maqautocognita.utils.ArrayUtils;
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
public abstract class AbstractSubtractionConjugateSection extends AbstractMathSection {

    protected static final IconPosition NUMBER_TRAY_SINGLE_BLOCK = new IconPosition(0, 0, 60, 60);
    private static final float START_X_POSITION_FORMULA = 1400;
    private static final float GAP_BETWEEN_NUMBER_TRAY = 20;
    private static final float GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY = 5;
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
     * Store the number block which is located in the number tray and allow to drag
     */
    protected List<ImageActor<Integer>> numberBlockTextureScreenObjectListInNumberTrayAllowToDrag;
    /**
     * store the start x position for all number tray for the coming drop number blocks
     */
    private float[] startXPositionNumberTrays;
    private List<TextureScreenObject<Integer, Object>> numberTrayScreenObjectList;
    /**
     * store all number blocks which are shown on the number tray
     */
    private List<TextureScreenObject> numberBlockTextureScreenObjectListInNumberTray;
    /**
     * store the small counting number which is shown on the first number tray
     */
    private List<NumberScreenObject> countingNumberTextScreenObjectList;
    private ImageActor<Integer> draggingNumberBlockTextureScreenObject;


    /**
     * store all formula
     */
    private List<TextCell<Integer>> formulaScreenObjectList;

    public AbstractSubtractionConjugateSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        numberTrayScreenObjectList = null;
        numberBlockTextureScreenObjectListInNumberTray = null;
        countingNumberTextScreenObjectList = null;
        draggingNumberBlockTextureScreenObject = null;
    }

    @Override
    protected void render() {
        initScreenObject();
        batch.begin();
        ScreenObjectUtils.draw(batch, numberTrayScreenObjectList);
        ScreenObjectUtils.draw(batch, countingNumberTextScreenObjectList);
        ScreenObjectUtils.draw(batch, numberBlockTextureScreenObjectListInNumberTray);
        batch.end();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlockTextureScreenObject) {
            draggingNumberBlockTextureScreenObject.setPosition(screenX, screenY);
        }
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();

        if (null != numberBlockTextureScreenObjectListInNumberTrayAllowToDrag) {
            for (ImageActor numberBlock : numberBlockTextureScreenObjectListInNumberTrayAllowToDrag) {
                numberBlock.remove();
            }
            numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.clear();
            numberBlockTextureScreenObjectListInNumberTrayAllowToDrag = null;
        }

        if (null != formulaScreenObjectList) {
            for (TextCell formula : formulaScreenObjectList) {
                formula.remove();
            }
            formulaScreenObjectList.clear();
            formulaScreenObjectList = null;
        }

        if (null != numberTrayScreenObjectList) {
            numberTrayScreenObjectList.clear();
            numberTrayScreenObjectList = null;
        }


        if (null != numberBlockTextureScreenObjectListInNumberTray) {
            numberBlockTextureScreenObjectListInNumberTray.clear();
            numberBlockTextureScreenObjectListInNumberTray = null;
        }


        if (ArrayUtils.isNotEmpty(numberBlocks)) {
            for (ImageActor<Integer> numberBlock : numberBlocks) {
                numberBlock.setVisible(false);
            }
        }
    }

    private void initScreenObject() {
        if (null == numberTrayScreenObjectList) {
            initNumberBlocksHorizontalTexture();

            Texture numberTrayTexture = AssetManagerUtils.getTexture(MathImagePathUtils.NUMBER_TRAY_IMAGE_PATH);

            numberTrayScreenObjectList = new ArrayList<TextureScreenObject<Integer, Object>>(getNumberOfNumberTray());


            startXPositionNumberTrays = new float[getNumberOfNumberTray()];

            float currentNumberTrayStartYPosition = getNumberTrayStartYPosition();
            for (int indexOfNumberTray = getNumberOfNumberTray(); indexOfNumberTray > 0; indexOfNumberTray--) {

                if (getPlayingNumber() <= 1) {
                    AutoCognitaTextureRegion singleNumberBlock = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_SINGLE_BLOCK);
                    AutoCognitaTextureRegion singleNumberBlockDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_SINGLE_BLOCK);
                    numberTrayScreenObjectList.add(
                            new TextureScreenObject<Integer, Object>(indexOfNumberTray, getNumberTrayStartXPosition(), currentNumberTrayStartYPosition, singleNumberBlock,
                                    singleNumberBlockDisable));
                } else {
                    AutoCognitaTextureRegion head = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_HEAD_BLOCK);
                    AutoCognitaTextureRegion headDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_HEAD_BLOCK);
                    AutoCognitaTextureRegion content = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_CONTENT_BLOCK);
                    AutoCognitaTextureRegion contentDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_CONTENT_BLOCK);
                    AutoCognitaTextureRegion tail = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_TAIL_BLOCK);
                    AutoCognitaTextureRegion tailDisable = new AutoCognitaTextureRegion(numberTrayTexture, NUMBER_TRAY_DISABLE_TAIL_BLOCK);


                    //build the number tray by different tray block
                    TextureScreenObject headNumberBlock =
                            new TextureScreenObject(indexOfNumberTray, getNumberTrayStartXPosition(), currentNumberTrayStartYPosition, head, headDisable);

                    numberTrayScreenObjectList.add(headNumberBlock);

                    float startXPosition = headNumberBlock.xPositionInScreen + headNumberBlock.width;

                    for (int c = 0; c < getPlayingNumber() - 2; c++) {
                        numberTrayScreenObjectList.add(new TextureScreenObject(indexOfNumberTray, startXPosition, currentNumberTrayStartYPosition, content, contentDisable));
                        startXPosition += NUMBER_TRAY_CONTENT_BLOCK.width;
                    }

                    numberTrayScreenObjectList.add(new TextureScreenObject(indexOfNumberTray, startXPosition, currentNumberTrayStartYPosition, tail, tailDisable));
                }

                drawShortFormula(getPlayingNumber(), getSecondNumberInNumberTray(indexOfNumberTray), currentNumberTrayStartYPosition);

                if (null == numberBlockTextureScreenObjectListInNumberTray) {
                    numberBlockTextureScreenObjectListInNumberTray = new ArrayList<TextureScreenObject>();
                }

                float numberBlockStartXPosition = getNumberTrayStartXPosition() + GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY;

                startXPositionNumberTrays[indexOfNumberTray - 1] = numberBlockStartXPosition;
                if (indexOfNumberTray > 0) {
                    if (getPlayingNumber() - indexOfNumberTray > 0) {
                        TextureScreenObject firstNumberBlockTextureScreenObject = new TextureScreenObject(numberBlockStartXPosition,
                                currentNumberTrayStartYPosition + Y_GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_TRAY,
                                numberBlocksAutoCognitaTextureRegions[getFirstNumberInNumberTray(indexOfNumberTray) - 1]);
                        numberBlockStartXPosition += firstNumberBlockTextureScreenObject.width + GAP_BETWEEN_COUNTING_NUMBER_AND_NUMBER_TRAY;
                        numberBlockTextureScreenObjectListInNumberTray.add(firstNumberBlockTextureScreenObject);
                    }

                    ImageActor<Integer> secondNumberBlockTextureScreenObject = new ImageActor<Integer>(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL);
                    secondNumberBlockTextureScreenObject.setAutoCognitaTextureRegion(numberBlocksAutoCognitaTextureRegions[getSecondNumberInNumberTray(indexOfNumberTray) - 1]);
                    secondNumberBlockTextureScreenObject.setOrigin(numberBlockStartXPosition,
                            currentNumberTrayStartYPosition + Y_GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_TRAY);
                    secondNumberBlockTextureScreenObject.setPosition(secondNumberBlockTextureScreenObject.getOriginX(), secondNumberBlockTextureScreenObject.getOriginY());
                    secondNumberBlockTextureScreenObject.setId(getSecondNumberInNumberTray(indexOfNumberTray));


//                    numberBlockTextureScreenObjectListInNumberTray.add(
//                            secondNumberBlockTextureScreenObject);

                    if (null == numberBlockTextureScreenObjectListInNumberTrayAllowToDrag) {
                        numberBlockTextureScreenObjectListInNumberTrayAllowToDrag = new ArrayList<ImageActor<Integer>>();
                    }

                    numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.add(secondNumberBlockTextureScreenObject);
                    stage.addActor(secondNumberBlockTextureScreenObject);

                    startXPositionNumberTrays[indexOfNumberTray - 1] += numberBlocksAutoCognitaTextureRegions[indexOfNumberTray - 1].getRegionWidth();

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

        }
    }

    protected abstract int getNumberOfNumberTray();

    protected abstract float getNumberTrayStartYPosition();

    protected abstract int getPlayingNumber();

    protected abstract float getNumberTrayStartXPosition();

    /**
     * Draw the give formula in the right hand side in the given yPosition
     *
     * @param playingNumber
     * @param rightNumber
     * @param yPosition
     */
    private void drawShortFormula(int playingNumber, int rightNumber, float yPosition) {

        if (null == formulaScreenObjectList) {
            formulaScreenObjectList = new ArrayList<TextCell<Integer>>();
        }

        float maxWidth = LetterUtils.getTotalWidthOfWord(String.valueOf(10), TextFontSizeEnum.FONT_72);

        float startXPosition = START_X_POSITION_FORMULA;

        int id = playingNumber - rightNumber;

        formulaScreenObjectList.add(
                new TextCell<Integer>(id,
                        String.valueOf(playingNumber), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;
        formulaScreenObjectList.add(
                new TextCell<Integer>(id, String.valueOf("-"), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;

        TextCell answer = new TextCell<Integer>(id, String.valueOf(rightNumber), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition);
        answer.setVisible(false);
        formulaScreenObjectList.add(answer);

        startXPosition += maxWidth;
        formulaScreenObjectList.add(new TextCell<Integer>(id, String.valueOf("="), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        startXPosition += maxWidth;
        formulaScreenObjectList.add(new TextCell<Integer>(id, String.valueOf(id), TextFontSizeEnum.FONT_72, maxWidth, startXPosition, yPosition));

        for (TextCell textCell : formulaScreenObjectList) {
            textCell.setTextFlip(true);
            stage.addActor(textCell);
        }
    }

    protected int getSecondNumberInNumberTray(int indexOfNumberTray) {
        return indexOfNumberTray;
    }

    protected int getFirstNumberInNumberTray(int indexOfNumberTray) {
        return getPlayingNumber() - indexOfNumberTray;
    }

    @Override
    public void onHelp() {

        if (isHelpAudioAllowToPlay()) {
            //show to the user that the number block is required to drag to the grey number pad in the left
            //check current required drag number block
            if (CollectionUtils.isNotEmpty(numberBlockTextureScreenObjectListInNumberTrayAllowToDrag) && null == draggingNumberBlock) {
                moveCurrentPlayingNumberBlock(null);
            }
        }

        super.onHelp();
    }


    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

        if (null != draggingNumberBlockTextureScreenObject) {

            if (null == ScreenObjectUtils.getTouchingScreenObject(ScreenObjectUtils.getScreenObjectListById(numberTrayScreenObjectList, draggingNumberBlockTextureScreenObject.getId()), screenX, screenY)) {
                //if the number block is left the number tray
                numberBlocks[draggingNumberBlockTextureScreenObject.getId() - 1].setVisible(true);
                numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.remove(draggingNumberBlockTextureScreenObject);
                //numberBlockTextureScreenObjectListInNumberTray.remove(draggingNumberBlockTextureScreenObject);
                draggingNumberBlockTextureScreenObject.remove();
                doWhenAnswerIsCorrected(draggingNumberBlockTextureScreenObject.getId());
            } else {
                rollbackDraggingNumberBlock(draggingNumberBlockTextureScreenObject);
            }
            draggingNumberBlockTextureScreenObject = null;
        }

    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        draggingNumberBlockTextureScreenObject = ScreenObjectUtils.getTouchingActor(numberBlockTextureScreenObjectListInNumberTrayAllowToDrag, screenX, screenY);
        if (null != draggingNumberBlockTextureScreenObject) {
            draggingNumberBlockTextureScreenObject.setTouchingPosition(screenX, screenY);
        }
    }

    protected void doWhenAnswerIsCorrected(int dragNumber) {

        //redraw the short formula
        for (final TextCell<Integer> formula : formulaScreenObjectList) {
            int answer = getPlayingNumber() - dragNumber;
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

    protected void moveCurrentPlayingNumberBlock(Runnable completeListener) {
        moveNumberBlock(getCurrentPlayingNumber(), completeListener);
    }

    protected void moveNumberBlock(final int number, final Runnable completeListener) {
        final ImageActor<Integer> TextureScreenObject = numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.get(0);

        if (!TextureScreenObject.isMoved()) {
            TextureScreenObject.toFront();
            MoveToAction moveToAction = getMoveToAction(number);

            RunnableAction completeAction = new RunnableAction();

            Runnable runnable = completeListener;
            if (null == runnable) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        rollbackDraggingNumberBlock(TextureScreenObject);
                    }
                };
            }
            completeAction.setRunnable(runnable);

            TextureScreenObject.addAction(new SequenceAction(moveToAction, completeAction));
        }
    }

    protected abstract int getCurrentPlayingNumber();

    protected MoveToAction getMoveToAction(int number) {
        ImageActor<Integer> numberBlock = numberBlocks[number - 1];
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(numberBlock.getOriginX(), numberBlock.getOriginY());
        moveToAction.setDuration(1.5f);
        return moveToAction;
    }

}
