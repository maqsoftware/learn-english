package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.badlogic.gdx.Gdx;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ExampleSubtractionStorySection extends SubtractionStorySection {

    private final static int NUMBER_OF_MILLISECOND_FOR_ANIMATION = 1200;

    private boolean startMoveFirstZebra;
    private boolean startMoveSecondZebra;

    public ExampleSubtractionStorySection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        startMoveFirstZebra = false;
        startMoveSecondZebra = false;
    }

    @Override
    protected boolean isTrashRequired() {
        return false;
    }

    @Override
    protected void onHelpAudioPlay(int audioListIndex, long millisecond) {
        super.onHelpAudioPlay(audioListIndex, millisecond);

        if (millisecond >= 5000 && millisecond < 6000 && !startMoveFirstZebra) {
            moveAwayZebra(0);
            startMoveFirstZebra = true;
        } else if (millisecond >= 6000 && !startMoveSecondZebra) {
            moveAwayZebra(1);
            startMoveSecondZebra = true;
        }
    }

    @Override
    protected void onHelpAudioComplete() {
        super.onHelpAudioComplete();
        numberKeyBlockScreenObjectList.get(2).isHighlighted = true;
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                numberKeyBlockScreenObjectList.get(2).isHighlighted = false;
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                abstractAutoCognitaScreen.setTouchAllow(true);
            }
        });

    }

    private void moveAwayZebra(final int listIndex) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long millisecond = 0;
                boolean isExecute = true;
                if (null != dropObjectScreenObjectList && dropObjectScreenObjectList.size() > listIndex) {
                    TextureScreenObject TextureScreenObject = dropObjectScreenObjectList.get(listIndex);
                    final float startXPosition = TextureScreenObject.xPositionInScreen;
                    final float startYPosition = TextureScreenObject.yPositionInScreen;
                    while (isExecute) {
                        try {
                            Thread.sleep(1);
                            millisecond++;
                            moveAwayZebra(listIndex, millisecond, startXPosition, startYPosition, TextureScreenObject);
                            if (millisecond == NUMBER_OF_MILLISECOND_FOR_ANIMATION) {
                                isExecute = false;
                                dropObjectScreenObjectList.remove(TextureScreenObject);
                            }
                        } catch (InterruptedException e) {
                            Gdx.app.log(getClass().getName(), "", e);
                        }
                    }
                }
            }
        }).start();


    }

    private void moveAwayZebra(int listIndex, long millisecond, float startXPosition, float startYPosition, TextureScreenObject zebraTextureScreenObject) {
        if (null != backgroundScreenObject) {
            float destinationXPosition = backgroundScreenObject.xPositionInScreen - zebraTextureScreenObject.width;
            float destinationYPosition = backgroundScreenObject.yPositionInScreen + zebraTextureScreenObject.height;
            zebraTextureScreenObject.xPositionInScreen = startXPosition + (destinationXPosition - startXPosition) * millisecond / NUMBER_OF_MILLISECOND_FOR_ANIMATION;
            zebraTextureScreenObject.yPositionInScreen = startYPosition + (destinationYPosition - startYPosition) * millisecond / NUMBER_OF_MILLISECOND_FOR_ANIMATION;
        }
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        abstractAutoCognitaScreen.setTouchAllow(false);
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        initDropObjectAtBeginning(5);
        onHelp();
    }

}
