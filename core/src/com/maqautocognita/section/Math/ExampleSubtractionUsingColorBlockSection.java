package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleSubtractionUsingColorBlockSection extends SubtractionUsingColorBlockSection {

    private static final float NUMBER_OF_MILLISECOND_FOR_ANIMATION = 1.5f;

    private static final int LEFT_VALUE = 7;

    private static final int RIGHT_VALUE = 3;

    public ExampleSubtractionUsingColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);

    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        resetScreen();
        if (ArrayUtils.isNotEmpty(numberBlocks)) {
            for (ImageActor numberBlock : numberBlocks) {
                rollbackDraggingNumberBlock(numberBlock);
            }
        }
        abstractAutoCognitaScreen.setTouchAllow(false);
    }

    @Override
    protected void onHide() {
        super.onHide();
        abstractAutoCognitaScreen.setTouchAllow(true);
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScriptAudioFileNameList, new AbstractSoundPlayListListener() {
            @Override
            public void beforePlaySound(int index) {
                final ImageActor<Integer> leftNumberBlock = numberBlocks[LEFT_VALUE - 1];
                MoveToAction moveAction = new MoveToAction();
                moveAction.setPosition(NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY
                        , NUMBER_TRAY_SUBTRACTED_START_Y_POSITION);
                moveAction.setDuration(NUMBER_OF_MILLISECOND_FOR_ANIMATION);
                RunnableAction nextAction = new RunnableAction();
                nextAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        leftValue = LEFT_VALUE;
                        addDraggingNumberBlockToSubtractedNumberTray(leftNumberBlock);
                        rollbackDraggingNumberBlock(leftNumberBlock);

                        //move right value
                        final ImageActor<Integer> rightNumberBlock = numberBlocks[RIGHT_VALUE - 1];

                        MoveToAction moveAction = new MoveToAction();
                        moveAction.setPosition(NUMBER_BLOCK_START_X_POSITION_IN_NUMBER_TRAY,
                                NUMBER_TRAY_TO_BE_SUBTRACT_START_Y_POSITION);
                        moveAction.setDuration(NUMBER_OF_MILLISECOND_FOR_ANIMATION);

                        RunnableAction completeAction = new RunnableAction();
                        completeAction.setRunnable(new Runnable() {
                            @Override
                            public void run() {
                                addDraggingNumberBlockToToBeSubtractedNumberTray(rightNumberBlock);
                                rollbackDraggingNumberBlock(rightNumberBlock);
                                rightValue = RIGHT_VALUE;
                            }
                        });
                        rightNumberBlock.addAction(new SequenceAction(moveAction, completeAction));
                    }
                });
                leftNumberBlock.addAction(new SequenceAction(moveAction, nextAction));
            }

            @Override
            public void onComplete() {
                abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScript2AudioFileNameList, new AbstractSoundPlayListListener() {
                    @Override
                    public void onComplete() {
                        result = LEFT_VALUE - RIGHT_VALUE;
                        if (null == numberBlockListInResultNumberTray) {
                            numberBlockListInResultNumberTray = new ArrayList<ImageActor<Integer>>();
                        }
                        reloadResultInResultNumberTray();
                        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                            @Override
                            public void onCorrectSoundPlayed() {
                                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                                abstractAutoCognitaScreen.setTouchAllow(true);
                            }
                        });
                    }
                });
            }


        });
    }
}
