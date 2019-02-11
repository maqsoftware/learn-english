package com.maqautocognita.section.Math;

import com.maqautocognita.Config;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.DragObject;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.constant.TextFontSizeEnum;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.CustomCamera;
import com.maqautocognita.graphics.NumberScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.IStoryModeActor;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractAutoCognitaSection;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StageUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractMathSection extends AbstractAutoCognitaSection implements IMathSection {

    public final static IconPosition TRASH_ICON_POSITION = new IconPosition(1750, 35, 100, 100);
    protected static final int BIG_NUMBER_START_Y_POSITION = 800;
    protected static final int NUMBER_BLOCK_GAP = 2;
    private static final int NUMBER_BLOCK_START_X_POSITION_IN_BOTTOM_SCREEN = 390;
    private static final int NUMBER_BLOCK_START_Y_POSITION_IN_BOTTOM_SCREEN = 295;
    private final static int HIGHLIGHTED_BORDER_WIDTH = 5;
    protected final MathAudioScriptWithElementCode mathAudioScriptWithElementCode;
    protected AutoCognitaTextureRegion numberBlocksAutoCognitaTextureRegions[];
    protected Texture numberTrayTexture;
    protected Texture smallBlockTrayTexture;
    protected ImageActor<Integer> numberBlocks[];
    protected ImageActor<Integer> draggingNumberBlock;
    protected Stage stage;
    protected CustomCamera camera;
    private AutoCognitaTextureRegion trashAutoCognitaTextureRegion;
    private Texture numberBlocksHorizontalTexture;
    private NumberScreenObject bigNumberScreenObject;
    private ShapeRenderer highlightBorder;


    public AbstractMathSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener);
        this.mathAudioScriptWithElementCode = mathAudioScriptWithElementCode;
        camera = new CustomCamera();
        camera.setWorldWidth(Config.TABLET_SCREEN_WIDTH);
        stage = new Stage(new ScreenViewport(camera), batch);
        reloadCamera();
    }

    protected void reloadCamera() {
        camera.setToOrtho(false, Config.TABLET_SCREEN_WIDTH, Config.TABLET_SCREEN_HEIGHT);
        camera.update();
    }

    @Override
    public void reloadVoScript() {
        mathAudioScriptWithElementCode.loadVoScript();
    }

    @Override
    public MathAudioScriptWithElementCode getMathAudioScriptWithElementCode() {
        return mathAudioScriptWithElementCode;
    }

    @Override
    protected float getExpectedScreenWidth() {
        return Config.TABLET_SCREEN_WIDTH;
    }

    @Override
    protected float getExpectedScreenHeight() {
        return Config.TABLET_SCREEN_HEIGHT;
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        resetScreen();
        numberBlocks = null;
        stage = new Stage(new ScreenViewport(camera), batch);
        reloadCamera();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (null != Gdx.input.getInputProcessor()) {
            inputMultiplexer.addProcessor(0, Gdx.input.getInputProcessor());
        }
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    @Override
    protected void render() {

        if (isTrashRequired()) {
            if (null == trashAutoCognitaTextureRegion) {
                trashAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.ICONS), 0, 300, (int) TRASH_ICON_POSITION.width, (int) TRASH_ICON_POSITION.height);
            }
            batch.begin();
            batch.draw(trashAutoCognitaTextureRegion, TRASH_ICON_POSITION.x, TRASH_ICON_POSITION.y);
            batch.end();
        }

        if (isNumberBlocksRequired() && null == numberBlocks) {
            initNumberBlocks();
        }

        if (null != draggingNumberBlock) {
            draggingNumberBlock.toFront();
        }
        if (null != stage) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

    }

    @Override
    protected void onHide() {
        super.onHide();
        if (null != Gdx.input.getInputProcessor() && Gdx.input.getInputProcessor() instanceof InputMultiplexer) {
            ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
        }
        StageUtils.dispose(stage);
        stage = null;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL, AssetManagerUtils.NUMBER_TRAY, AssetManagerUtils.SMALL_BLOCK};
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return mathAudioScriptWithElementCode;
    }

    @Override
    public void dispose() {
        super.dispose();
        trashAutoCognitaTextureRegion = null;
        numberBlocksHorizontalTexture = null;
        if (ArrayUtils.isNotEmpty(numberBlocks)) {
            for (ImageActor numberBlock : numberBlocks) {
                if (numberBlock instanceof IStoryModeActor) {
                    numberBlock.dispose();
                }
            }
        }
        numberBlocks = null;
        if (null != draggingNumberBlock) {
            draggingNumberBlock.dispose();
            draggingNumberBlock = null;
        }

        bigNumberScreenObject = null;

        StageUtils.dispose(stage);
        stage = null;
    }

    @Override
    public void onResize() {
        super.onResize();
        reloadCamera();
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (null != draggingNumberBlock) {
            draggingNumberBlock.setPosition(screenX, screenY);
        }
    }

    @Override
    protected void singleTap(int screenX, int screenY) {
        if (isTrashRequired() && TouchUtils.isTouched(TRASH_ICON_POSITION, screenX, screenY)) {
            resetScreen();
        }
    }

    protected boolean isTrashRequired() {
        return false;
    }

    protected boolean isNumberBlocksRequired() {
        return true;
    }

    protected void initNumberBlocks() {
        initNumberBlocksHorizontalTexture();
        int startXPosition = NUMBER_BLOCK_START_X_POSITION_IN_BOTTOM_SCREEN;
        int startYPosition = NUMBER_BLOCK_START_Y_POSITION_IN_BOTTOM_SCREEN;
        numberBlocks = new ImageActor[10];
        for (int i = 0; i < 10; i++) {

            IconPosition iconPosition = getNumberBlockIconPositionInTexture(i);
            numberBlocks[i] = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL, iconPosition);
            numberBlocks[i].setId(i + 1);
            numberBlocks[i].setOrigin(startXPosition, startYPosition);
            numberBlocks[i].setPosition(numberBlocks[i].getOriginX(), numberBlocks[i].getOriginY());
            numberBlocks[i].setSize(iconPosition.width, iconPosition.height);
            stage.addActor(numberBlocks[i]);
            startXPosition += iconPosition.width + 50;

            if (i + 1 < 10) {
                //check if the next block is wider than the screen width
                if (startXPosition + iconPosition.width > Config.TABLET_SCREEN_WIDTH) {
                    //if yes, reset the start x position
                    startXPosition = NUMBER_BLOCK_START_X_POSITION_IN_BOTTOM_SCREEN;
                    //and decrease the y position
                    startYPosition -= 100;
                }
            }
        }
    }

    protected void initNumberBlocksHorizontalTexture() {
        if (null == numberBlocksHorizontalTexture) {

            numberBlocksHorizontalTexture = AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL);
            numberBlocksAutoCognitaTextureRegions = new AutoCognitaTextureRegion[10];


            for (int i = 0; i < 10; i++) {
                numberBlocksAutoCognitaTextureRegions[i] = new AutoCognitaTextureRegion(numberBlocksHorizontalTexture, 0, i * 60, 48 + i * 50, 55);
            }

        }
    }

    protected IconPosition getNumberBlockIconPositionInTexture(int numberIndex) {
        return new IconPosition(0, numberIndex * 60, 48 + numberIndex * 50, 55);
    }

    /**
     * It will be call when the trash button is pressed
     */
    protected void resetScreen() {
    }

    protected void drawDragObject(DragObject dragObject) {
        float x, y;

        if (null == dragObject.getDraggingPosition()) {
            x = dragObject.getOriginalPosition().x;
            y = dragObject.getOriginalPosition().y;
        } else {
            x = dragObject.getDraggingPosition().x;
            y = dragObject.getDraggingPosition().y;
        }

        if (dragObject.isHighlighted()) {
            batch.end();
            highlightScreenObject(x, y,
                    dragObject.getAutoCognitaTextureRegion().getRegionWidth(), dragObject.getAutoCognitaTextureRegion().getRegionHeight());
            batch.begin();
        }
        batch.draw(dragObject.getAutoCognitaTextureRegion(), x, y);
    }

    protected void highlightScreenObject(float x, float y, float width, float height) {
        if (null == highlightBorder) {
            highlightBorder = new ShapeRenderer();
        }

        highlightBorder.setProjectionMatrix(batch.getProjectionMatrix());
        highlightBorder.begin(ShapeRenderer.ShapeType.Filled);
        highlightBorder.setColor(Color.RED);
        highlightBorder.rect(x - HIGHLIGHTED_BORDER_WIDTH, y - HIGHLIGHTED_BORDER_WIDTH, width + HIGHLIGHTED_BORDER_WIDTH * 2, height + HIGHLIGHTED_BORDER_WIDTH * 2);
        highlightBorder.end();
    }


    protected void drawNumberInScreenCenter(int number) {

        if (null == bigNumberScreenObject) {
            bigNumberScreenObject = new NumberScreenObject(number, 0, BIG_NUMBER_START_Y_POSITION, TextFontSizeEnum.FONT_144, true);
            bigNumberScreenObject.xPositionInScreen = ScreenUtils.getXPositionForCenterObject(bigNumberScreenObject.width);
        } else if (!bigNumberScreenObject.displayText.equals(number)) {
            bigNumberScreenObject.setDisplayText(number);
            bigNumberScreenObject.xPositionInScreen = ScreenUtils.getXPositionForCenterObject(bigNumberScreenObject.width);
        }

        ScreenObjectUtils.draw(batch, bigNumberScreenObject);
    }

    protected void drawBigNumber(int number, int startXPositionForSingleDigit, int startXPositionForDoubleDigit,
                                 int startYPosition) {
        drawNumber(number, startXPositionForSingleDigit, startXPositionForDoubleDigit,
                startYPosition, ColorProperties.TEXT, TextFontSizeEnum.FONT_144);
    }

    private int drawNumber(int number, int startXPositionForSingleDigit, int startXPositionForDoubleDigit,
                           int startYPosition, Color textColor, TextFontSizeEnum textFontSizeEnum) {

        NumberScreenObject numberScreenObject = new NumberScreenObject(number, number >= 10 ? startXPositionForDoubleDigit : startXPositionForSingleDigit, startYPosition, textFontSizeEnum, true);

        numberScreenObject.setColor(textColor);

        ScreenObjectUtils.draw(batch, numberScreenObject);

        int numberOfDigit = String.valueOf(number).length();

        return numberOfDigit > 1 ? startXPositionForDoubleDigit : startXPositionForSingleDigit;
    }

    protected boolean isTouchingNumberBlock(int screenX, int screenY) {

        if (ArrayUtils.isNotEmpty(numberBlocks)) {
            draggingNumberBlock = ScreenObjectUtils.getTouchingActor(numberBlocks, screenX, screenY);

            if (null != draggingNumberBlock) {
                draggingNumberBlock.setTouchingPosition(screenX, screenY);
                return true;
            }
        }

        return false;
    }

    protected void rollbackDraggingNumberBlock() {
        rollbackDraggingNumberBlock(draggingNumberBlock);
        draggingNumberBlock = null;
    }

    protected void rollbackDraggingNumberBlock(ImageActor<Integer> numberBlocks) {
        if (null != numberBlocks) {
            numberBlocks.removeTouch();
            numberBlocks.setPosition(numberBlocks.getOriginX(), numberBlocks.getOriginY());
        }

    }

    protected void removeDraggingNumberBlock() {
        if (null != draggingNumberBlock) {
            rollbackDraggingNumberBlock(draggingNumberBlock);
            draggingNumberBlock = null;
        }
    }

    protected void playNumberAudio(int number) {
        abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getNumberAudioFileList(number));
    }

    protected void playNumberAudio(int number, AbstractSoundPlayListListener soundPlayListListener) {
        abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getNumberAudioFileList(number), soundPlayListListener);
    }
}
