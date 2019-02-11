package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
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
public class SubtractionConjugateAcrossTenSection extends AbstractSubtractionConjugateSection {

    private static final IconPosition NUMBER_BLOCK_HORIZONTAL_BACKGROUND = new IconPosition(521, 0, 860, 370);

    private static final float GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN = 15;

    private TextureScreenObject numberBlockBackgroundScreenObject;

    private int playingNumber = 11;

    /**
     * indicate the current playing number
     * if the {@link #playingNumber} is 5 and the current playing number is 1, the user is required to drag the number block 4 to the first number tray
     */
    private int currentPlayingNumber;
    private int playingRound;

    public SubtractionConjugateAcrossTenSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        resetScreen();
    }

    private int getStartNumber() {
        return playingNumber % 10;
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

        final float startYPosition = TRASH_ICON_POSITION.y + NUMBER_BLOCK_HORIZONTAL_BACKGROUND.height - GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN;

        float currentStartYPosition = startYPosition;

        float startXPosition = Config.SCREEN_CENTER_START_X_POSITION;

        float maximumBlockWidth = 0;

        //init number blocks from 1 to 10
        for (int i = 0; i < numberBlocksAutoCognitaTextureRegions.length; i++) {

            if (i == 5) {
                currentStartYPosition = startYPosition;
                startXPosition += maximumBlockWidth + GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN;
            }
            IconPosition iconPosition = getNumberBlockIconPositionInTexture(i);
            currentStartYPosition -= iconPosition.height;


            numberBlocks[i] = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL, iconPosition);
            numberBlocks[i].setId(i + 1);
            numberBlocks[i].setOrigin(startXPosition + GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN,
                    currentStartYPosition);
            numberBlocks[i].setPosition(numberBlocks[i].getOriginX(), numberBlocks[i].getOriginY());
            numberBlocks[i].setSize(iconPosition.width, iconPosition.height);
            numberBlocks[i].setVisible(false);
            stage.addActor(numberBlocks[i]);

            currentStartYPosition -= GAP_BETWEEN_NUMBER_BLOCK_IN_LEFT_SCREEN;

            if (i < 5 &&
                    maximumBlockWidth < iconPosition.width) {
                maximumBlockWidth = iconPosition.width;
            }
        }
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected List<String> getHelpAudioFileNameList() {
        return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(playingNumber);
    }

    @Override
    protected void render() {
        initNumberBlocksBackground();
        batch.begin();
        ScreenObjectUtils.draw(batch, numberBlockBackgroundScreenObject);
        batch.end();
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        numberBlockBackgroundScreenObject = null;
        resetScreen();
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        currentPlayingNumber = getNumberOfNumberTray();
    }

    @Override
    protected int getNumberOfNumberTray() {
        return playingNumber / 2 - getStartNumber() + 1;
    }

    @Override
    protected float getNumberTrayStartYPosition() {
        return ScreenUtils.getNavigationBarStartYPosition() - 100;
    }

    @Override
    protected int getPlayingNumber() {
        return playingNumber;
    }

    @Override
    protected float getNumberTrayStartXPosition() {
        return Config.SCREEN_CENTER_START_X_POSITION;
    }

    @Override
    protected int getSecondNumberInNumberTray(int indexOfNumberTray) {
        return getPlayingNumber() - getFirstNumberInNumberTray(indexOfNumberTray);
    }

    @Override
    protected int getFirstNumberInNumberTray(int indexOfNumberTray) {
        return getPlayingNumber() - indexOfNumberTray - getStartNumber() + 1;
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        isTouchingNumberBlock(screenX, screenY);
    }

    @Override
    protected void doAfterCorrectSoundPlayed() {
        playingRound++;
        if (playingRound == getNumberOfNumberTray()) {
            playingRound = 0;
            playingNumber++;
            if (playingNumber < 20) {
                resetScreen();
                onHelp();
            } else {
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
            }
        } else {
            currentPlayingNumber--;
        }
    }

    @Override
    protected int getCurrentPlayingNumber() {
        return getSecondNumberInNumberTray(currentPlayingNumber);
    }

    /**
     * init the background of the number blocks in the bottom  side of the screen, the number blocks will be init in the {@link #initNumberBlocks()}
     */
    private void initNumberBlocksBackground() {
        if (null == numberBlockBackgroundScreenObject) {
            Texture numberBlockBackground = AssetManagerUtils.getTexture(MathImagePathUtils.NUMBER_BLOCK_BACKGROUND_IMAGE_PATH);

            numberBlockBackgroundScreenObject = new TextureScreenObject(Config.SCREEN_CENTER_START_X_POSITION,
                    TRASH_ICON_POSITION.y,
                    new AutoCognitaTextureRegion(numberBlockBackground, NUMBER_BLOCK_HORIZONTAL_BACKGROUND));
        }
    }

}
