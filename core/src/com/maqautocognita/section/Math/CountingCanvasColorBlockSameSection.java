package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingCanvasColorBlockSameSection extends CountingCanvasColorBlockSection {

    private boolean isExampleNumberBlockXAndYPlayed;

    private ImageActor<Integer> thirdDroppedNumberBlock;

    public CountingCanvasColorBlockSameSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, false, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void generateXYList() {
        if (null == xyList) {
            List<Integer> xList = new ArrayList<Integer>(MAXIMUM_NUMBER_OF_ROUND);
            for (int i = 0; i < MAXIMUM_NUMBER_OF_ROUND; i++) {
                xList.add(RandomUtils.getRandomWithExclusion(1, 9, ArrayUtils.toArray(xList)));
            }
            xyList = new ArrayList<String>(MAXIMUM_NUMBER_OF_ROUND);
            for (int i = 0; i < MAXIMUM_NUMBER_OF_ROUND; i++) {
                int xValue = xList.get(i);
                xyList.add(xValue + "_" + RandomUtils.getRandomWithExclusion(1, 10 - xValue, xValue));
            }
        }
    }

    @Override
    public void onHelp() {
        if (isHelpAudioAllowToPlay()) {
            if (0 == numberOfRoundPlayed && null != numberPadScreenObject) {
                abstractAutoCognitaScreen.setTouchAllow(false);
                if (isExampleNumberBlockXAndYPlayed) {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScript2AudioFileNameList, new AbstractSoundPlayListListener() {

                        @Override
                        public void onComplete() {

                            MoveToAction moveAction = new MoveToAction();
                            moveAction.setPosition(numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x,
                                    numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y + BLOCK_SIZE_IN_NUMBER_PAD);
                            moveAction.setDuration(1.5f);
                            numberBlocks[10 - x - y].setZIndex(numberBlocks.length - 1);
                            RunnableAction runnableAction = new RunnableAction();
                            runnableAction.setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    numberBlocks[10 - x - y].toBack();
                                    abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                        @Override
                                        public void onCorrectSoundPlayed() {
                                            numberOfRoundPlayed++;
                                            isExampleNumberBlockXAndYPlayed = false;
                                            rollbackDraggingNumberBlock(numberBlocks[10 - x]);
                                            rollbackDraggingNumberBlock(numberBlocks[10 - y]);
                                            rollbackDraggingNumberBlock(numberBlocks[10 - x - y]);
                                            nextRound();

                                            abstractAutoCognitaScreen.setTouchAllow(true);
                                            onHelp();
                                        }
                                    });
                                }
                            });
                            numberBlocks[10 - x - y].addAction(new SequenceAction(moveAction, runnableAction));
                        }
                    });
                } else {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(x, y), new AbstractSoundPlayListListener() {

                        @Override
                        public void beforePlaySound(int index) {
                            if (1 == index) {
                                MoveToAction moveAction = new MoveToAction();
                                moveAction.setPosition(numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x,
                                        numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y);
                                moveAction.setDuration(1.5f);
                                numberBlocks[10 - x].addAction(moveAction);
                            } else if (3 == index) {
                                MoveToAction moveAction = new MoveToAction();
                                moveAction.setPosition(numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x + numberBlocks[10 - x].getWidth(),
                                        numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y);
                                moveAction.setDuration(1.5f);
                                numberBlocks[10 - y].addAction(moveAction);
                            }
                        }


                        @Override
                        public void onComplete() {
                            isExampleNumberBlockXAndYPlayed = true;
                            onHelp();
                        }


                    });
                }
            } else {
                super.onHelp();
            }
        }
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        if (isXNumberBlockDragged && isYNumberBlockDragged) {
            return mathAudioScriptWithElementCode.instructionScript2AudioFileNameList;
        } else {
            return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(x, y);
        }
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{AssetManagerUtils.NUMBER_PAD_GREEN}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        thirdDroppedNumberBlock = null;
    }

    @Override
    protected void doTouchUp(int screenX, int screenY) {

        if (null != draggingNumberBlock && TouchUtils.isTouched(numberPadScreenObject, screenX, screenY)) {
            //if the user dragging the number block and dropped on the number pad
            if (isXNumberBlockDragged && isYNumberBlockDragged) {

                float targetXPosition = getNumberBlockStartXPositionInScreen();

                float targetYPosition = numberBlockListInNumberPad.get(0).getY() + BLOCK_SIZE_IN_NUMBER_PAD;

                if (targetYPosition + draggingNumberBlock.getHeight() > numberPadScreenObject.yPositionInScreen + numberPadScreenObject.height) {
                    //make it below the dragged number block
                    targetYPosition -= (numberBlockListInNumberPad.get(0).getHeight() + draggingNumberBlock.getHeight());
                }

                if (null != thirdDroppedNumberBlock) {
                    numberBlockListInNumberPad.remove(thirdDroppedNumberBlock);
                    thirdDroppedNumberBlock.remove();
                }
                thirdDroppedNumberBlock = draggingNumberBlock.clone(targetXPosition, targetYPosition);
                removeDraggingNumberBlock();
                //make sure no overlap to other number block in the pad
                numberBlockListInNumberPad.add(thirdDroppedNumberBlock);
                stage.addActor(thirdDroppedNumberBlock);
                doZIndexNumberBlockListInNumberPad();

                if (thirdDroppedNumberBlock.getId() == x + y) {
                    afterPlayCorrected();
                }
            } else {
                //make sure the number block is only can be dragged when the x and y number are not dragged
                //check if the number block dropped is correctly
                if (isCorrectNumberBlockDropped()) {
                    if (isXNumberBlockDragged) {
                        isYNumberBlockDragged = true;
                    } else {
                        isXNumberBlockDragged = true;
                    }

                } else {
                    rollbackDraggingNumberBlock();
                    return;
                }

                if (null == numberBlockListInNumberPad) {
                    numberBlockListInNumberPad = new ArrayList<ImageActor<Integer>>();
                }

                float targetYPosition = getDropYPositionInNumberPad(draggingNumberBlock.getY());

                //make the dragged block number to the head of number pad
                float targetXPosition = getNumberBlockStartXPositionInScreen();

                //check if already drop x number block
                if (isYNumberBlockDragged) {
                    targetXPosition = numberBlockListInNumberPad.get(0).getX() +
                            numberBlockListInNumberPad.get(0).getWidth();
                    targetYPosition = numberBlockListInNumberPad.get(0).getY();
                }

                ImageActor numberBlock = draggingNumberBlock.clone(targetXPosition, targetYPosition);

                //make sure no overlap to other number block in the pad
                numberBlockListInNumberPad.add(numberBlock);
                doZIndexNumberBlockListInNumberPad();
                stage.addActor(numberBlock);
                removeDraggingNumberBlock();

                //if both number block are dropped, please the audio
                if (isXNumberBlockDragged && isYNumberBlockDragged) {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScript2AudioFileNameList);
                }
            }
        } else if (null != draggingNumberBlockInNumberPad) {

            if (TouchUtils.isTouched(numberPadScreenObject, screenX, screenY)) {
                //if the user is dragging the number block in the number pad
                rollbackDraggingNumberBlock(draggingNumberBlockInNumberPad);

            } else {
                //if the user drag the number block outside the green number pad, remove it from the number pad
                draggingNumberBlockInNumberPad.remove();
            }

            draggingNumberBlockInNumberPad = null;

        }
    }

    private float getNumberBlockStartXPositionInScreen() {
        return numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x;
    }

    private float getDropYPositionInNumberPad(float dropYPosition) {

        float startYPositionInNumberPad = numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y;

        return startYPositionInNumberPad +
                //get the number of row in the number pad which the number block dragged
                (Math.round((dropYPosition - startYPositionInNumberPad) / BLOCK_SIZE_IN_NUMBER_PAD))
                        * BLOCK_SIZE_IN_NUMBER_PAD;
    }

    @Override
    protected boolean isKeyboardRequired() {
        return false;
    }
}
