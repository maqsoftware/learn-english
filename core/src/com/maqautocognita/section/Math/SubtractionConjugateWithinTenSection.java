package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Math.Utils.MathImagePathUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class SubtractionConjugateWithinTenSection extends AbstractSubtractionConjugateSection {

    private static final float GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN = 15;

    private static final float GAP_BETWEEN_NUMBER_BLOCK_BACKGROUND_AND_NUMBER_TRAY = 100;

    private static final IconPosition NUMBER_BLOCK_VERTICAL_BACKGROUND = new IconPosition(0, 0, 520, 720);
    private final int startNumber;
    private final int endNumber;
    private TextureScreenObject numberBlockBackgroundScreenObject;
    private int playingNumber;
    /**
     * indicate the current playing number
     * if the {@link #playingNumber} is 5 and the current playing number is 1, the user is required to drag the number block 4 to the  number tray in left side
     */
    private int currentPlayingNumber = 0;


    public SubtractionConjugateWithinTenSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, int startNumber, int endNumber, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.startNumber = startNumber;
        this.endNumber = endNumber;
        playingNumber = startNumber;
        currentPlayingNumber = playingNumber;
    }

    @Override
    protected void render() {
        initNumberBlocksBackground();

        batch.begin();
        ScreenObjectUtils.draw(batch, numberBlockBackgroundScreenObject);
        batch.end();

        super.render();
    }

    /**
     * init the background of the number blocks in the left hand side of the screen, the number blocks will be init in the {@link #initNumberBlocks()}
     */
    private void initNumberBlocksBackground() {
        if (null == numberBlockBackgroundScreenObject) {
            Texture numberBlockBackground = AssetManagerUtils.getTexture(MathImagePathUtils.NUMBER_BLOCK_BACKGROUND_IMAGE_PATH);

            numberBlockBackgroundScreenObject = new TextureScreenObject(Config.SCREEN_CENTER_START_X_POSITION,
                    ScreenUtils.getBottomYPositionForCenterObject(NUMBER_BLOCK_VERTICAL_BACKGROUND.height),
                    new AutoCognitaTextureRegion(numberBlockBackground, NUMBER_BLOCK_VERTICAL_BACKGROUND));
        }
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        playingNumber = startNumber;
        currentPlayingNumber = playingNumber;
    }

    @Override
    public void dispose() {
        super.dispose();
        numberBlockBackgroundScreenObject = null;
        resetScreen();
        playingNumber = startNumber;
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        currentPlayingNumber = playingNumber;

    }

    @Override
    protected int getNumberOfNumberTray() {
        return playingNumber;
    }

    @Override
    protected float getNumberTrayStartYPosition() {
        return numberBlockBackgroundScreenObject.yPositionInScreen + numberBlockBackgroundScreenObject.height - NUMBER_TRAY_SINGLE_BLOCK.height;
    }

    @Override
    protected int getPlayingNumber() {
        return playingNumber;
    }

    @Override
    protected float getNumberTrayStartXPosition() {
        return numberBlockBackgroundScreenObject.xPositionInScreen + numberBlockBackgroundScreenObject.width + GAP_BETWEEN_NUMBER_BLOCK_BACKGROUND_AND_NUMBER_TRAY;
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        isTouchingNumberBlock(screenX, screenY);
    }

    @Override
    protected void doAfterCorrectSoundPlayed() {
        if (currentPlayingNumber == 1) {
            playingNumber++;
            if (playingNumber <= endNumber) {
                resetScreen();
                if (isAutoPlayRequired()) {
                    autoPlayWithHelpAudio();
                } else {
                    abstractAutoCognitaScreen.setTouchAllow(true);
                    onHelp();
                }
            } else {
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
            }
        } else {
            currentPlayingNumber--;
            if (isAutoPlayRequired()) {
                autoPlay();
            }
//            refreshNumberTrayState();
        }
    }

    @Override
    protected int getCurrentPlayingNumber() {
        return currentPlayingNumber;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{MathImagePathUtils.NUMBER_TRAY_IMAGE_PATH,
                MathImagePathUtils.NUMBER_BLOCK_BACKGROUND_IMAGE_PATH
        }, super.getAllRequiredTextureName());
    }

    @Override
    protected void initNumberBlocks() {

        numberBlocks = new ImageActor[numberBlocksAutoCognitaTextureRegions.length];

        float startYPosition = ScreenUtils.getStartYPositionForCenterObject(NUMBER_BLOCK_VERTICAL_BACKGROUND.height);

        //init number blocks from 1 to 10
        for (int i = 0; i < numberBlocksAutoCognitaTextureRegions.length; i++) {

            IconPosition iconPosition = getNumberBlockIconPositionInTexture(i);
            startYPosition -= GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN + iconPosition.height;

            numberBlocks[i] = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL, iconPosition);

            numberBlocks[i].setId(i + 1);
            numberBlocks[i].setOrigin(
                    Config.SCREEN_CENTER_START_X_POSITION + GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN,
                    startYPosition);
            numberBlocks[i].setPosition(numberBlocks[i].getOriginX(), numberBlocks[i].getOriginY());
            numberBlocks[i].setSize(iconPosition.width, iconPosition.height);
            numberBlocks[i].setVisible(false);
            stage.addActor(numberBlocks[i]);
        }

    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        if (isAutoPlayRequired()) {
            autoPlayWithHelpAudio();
        } else {
            onHelp();
        }
    }

    private boolean isAutoPlayRequired() {
        return playingNumber <= 4;
    }

    private void autoPlayWithHelpAudio() {
        abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(playingNumber), new AbstractSoundPlayListListener() {
            @Override
            public void onComplete() {
                autoPlay();
            }
        });
    }

    private void autoPlay() {
        final int number = getCurrentPlayingNumber();
        moveNumberBlock(getCurrentPlayingNumber(), new Runnable() {
            @Override
            public void run() {
                //if the number block is left the number tray
                numberBlocks[number - 1].setVisible(true);
                ImageActor removeNumberBlock = numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.get(0);
                removeNumberBlock.remove();
                numberBlockTextureScreenObjectListInNumberTrayAllowToDrag.remove(removeNumberBlock);
                doWhenAnswerIsCorrected(number);
            }
        });
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(playingNumber);
    }

}
