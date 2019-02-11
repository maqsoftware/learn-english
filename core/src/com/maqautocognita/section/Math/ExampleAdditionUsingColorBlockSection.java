package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.ArrayUtils;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleAdditionUsingColorBlockSection extends AbstractAdditionUsingColorBlockSection {

    private static final float NUMBER_OF_MILLISECOND_FOR_ANIMATION = 1.5f;

    private static final int LEFT_VALUE = 3;

    private static final int RIGHT_VALUE = 4;

    public ExampleAdditionUsingColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
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
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScriptAudioFileNameList, new AbstractSoundPlayListListener() {

            @Override
            public void beforePlaySound(int index) {
                MoveToAction moveAction = new MoveToAction();
                moveAction.setPosition(numberTrayStartXPosition + getWidthOfBlockListInNumberTray(), NUMBER_TRAY_START_Y_POSITION);
                moveAction.setDuration(NUMBER_OF_MILLISECOND_FOR_ANIMATION);

                RunnableAction nextAction = new RunnableAction();
                nextAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        leftValue = LEFT_VALUE;
                        //duplicate the number block in the number tray
                        addNumberBlockInTheTray(numberBlocks[LEFT_VALUE - 1]);
                        //rollback the moved number block to the bottom of the screen
                        rollbackDraggingNumberBlock(numberBlocks[LEFT_VALUE - 1]);
                        //move right value
                        MoveToAction moveAction = new MoveToAction();
                        moveAction.setPosition(numberTrayStartXPosition + getWidthOfBlockListInNumberTray(),
                                NUMBER_TRAY_START_Y_POSITION);
                        moveAction.setDuration(NUMBER_OF_MILLISECOND_FOR_ANIMATION);

                        RunnableAction completeAction = new RunnableAction();
                        completeAction.setRunnable(new Runnable() {
                            @Override
                            public void run() {
                                rightValue = RIGHT_VALUE;
                                //duplicate the number block in the number tray
                                addNumberBlockInTheTray(numberBlocks[RIGHT_VALUE - 1]);
                                //rollback the moved number block to the bottom of the screen
                                rollbackDraggingNumberBlock(numberBlocks[RIGHT_VALUE - 1]);
                                result = LEFT_VALUE + RIGHT_VALUE;
                            }
                        });


                        numberBlocks[RIGHT_VALUE - 1].addAction(new SequenceAction(moveAction, completeAction));


                    }
                });

                numberBlocks[LEFT_VALUE - 1].addAction(new SequenceAction(moveAction, nextAction));
            }


            @Override
            public void onComplete() {
                abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScript2AudioFileNameList, new AbstractSoundPlayListListener() {
                    @Override
                    public void onComplete() {
                        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                            @Override
                            public void onCorrectSoundPlayed() {
                                abstractAutoCognitaScreen.setTouchAllow(true);
                                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                            }
                        });
                    }
                });
            }


        });
    }
}
