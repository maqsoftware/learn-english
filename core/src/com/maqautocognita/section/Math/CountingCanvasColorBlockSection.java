package com.maqautocognita.section.Math;

import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.TextureScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.listener.AbstractSoundPlayListListener;
import com.maqautocognita.scene2d.actors.ImageActor;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.utils.AnimationUtils;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.RandomUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * It automatically perform these actions the first time so the user can see what to do.
 * When the instructor says, " Move a {@link #x} block and a {@link #y} block to the green space." the app should automatically move those blocks to the green space.
 * One block should be on top of the other. The left edges of each block should be lined up.
 * Then, when the instructor says, "Touch the block that has more" the app should highlight the longer block. Then, when the instructor says, "How many more", the app should highlight the correct answer in the numeric keyboard.
 * <p>
 * After the example, then repeat {@link #MAXIMUM_NUMBER_OF_ROUND} -1
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class CountingCanvasColorBlockSection extends AbstractNumberKeyboardSection {

    //store the space between the number block and the number pad
    protected static final Vector2 START_POSITION_IN_NUMBER_PAD = new Vector2(7, 10);
    protected static final float BLOCK_SIZE_IN_NUMBER_PAD = 50;
    private static final Vector2 NUMBER_PAD_SIZE = new Vector2(1010, 775);
    private static final float GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_PAD = 85;
    /**
     * Number of round should be played
     */
    protected final int MAXIMUM_NUMBER_OF_ROUND = 5;
    private final boolean isMore;
    protected TextureScreenObject numberPadScreenObject;
    protected List<ImageActor<Integer>> numberBlockListInNumberPad;
    protected ImageActor<Integer> draggingNumberBlockInNumberPad;
    /**
     * the number required to drag to the number pad
     */
    protected int x;
    /**
     * the number required to drag to the number pad, it must be different to the {@link #x}
     */
    protected int y;
    /**
     * a flag to indicate if the number block with correct {@link #x} value is dropped on the number pad
     */
    protected boolean isXNumberBlockDragged;
    /**
     * a flag to indicate if the number block with correct {@link #y} value is dropped on the number pad
     */
    protected boolean isYNumberBlockDragged;
    /**
     * number of round played
     */
    protected int numberOfRoundPlayed;
    /**
     * Store the list of x and y value which will be used to play, it will use the "_" to separate the x and y value
     */
    protected List<String> xyList;
    /**
     * the application will be ask the user to select which number block is more or less depends on the {@link #isMore}, and this is a flag to indicate if the number block is selected
     */
    private boolean isNumberBlockSelected;

    public CountingCanvasColorBlockSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, boolean isMore, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(mathAudioScriptWithElementCode, abstractAutoCognitaScreen, onHelpListener);
        this.isMore = isMore;
    }

    private void afterCorrectNumberBlockSelected() {
        isNumberBlockSelected = true;
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                onHelp();
            }
        });
    }

    protected boolean isCorrectNumberBlockDropped() {
        if (!isXNumberBlockDragged && draggingNumberBlock.getId() != x) {
            return false;
        } else if (isXNumberBlockDragged && !isYNumberBlockDragged && draggingNumberBlock.getId() != y) {
            return false;
        }

        return true;
    }

    private boolean isCorrectNumberBlockSelected(ImageActor<Integer> draggingNumberBlockInNumberPad) {
        if (isMore) {
            if (x > y && x == draggingNumberBlockInNumberPad.getId()) {
                return true;
            } else if (y > x && y == draggingNumberBlockInNumberPad.getId()) {
                return true;
            }
        } else {
            if (x < y && x == draggingNumberBlockInNumberPad.getId()) {
                return true;
            } else if (y < x && y == draggingNumberBlockInNumberPad.getId()) {
                return true;
            }
        }

        return false;
    }

    protected boolean isNumberBlockOverlapOtherNumberBlockInNumberPad(float x, float y, float width, float height, ImageActor excludeCheckScreenObject) {
        if (CollectionUtils.isNotEmpty(numberBlockListInNumberPad)) {
            for (ImageActor screenObject : numberBlockListInNumberPad) {
                if (!screenObject.equals(excludeCheckScreenObject) && isOverLap(screenObject, x, y, width, height)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean isOverLap(ImageActor screenObject, float x, float y, float width, float height) {
        //check if the given screenobject is overlap the given position and size, here the height will be using {@link #BLOCK_SIZE_IN_NUMBER_PAD},
        // because the screen object height is include the shadow, the overlap caculation should include it
        return screenObject.getX() < x + width
                && screenObject.getX() + screenObject.getWidth() > x &&
                screenObject.getY() < y + height
                && screenObject.getY() + BLOCK_SIZE_IN_NUMBER_PAD > y;
    }

    private float getDropXPositionInNumberPad(float dropXPosition) {

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
    protected void onShowAgain() {
        numberOfRoundPlayed = 0;
        generateXYList();
        resetNumber();
        if (null == numberPadScreenObject) {
            Texture numberPadGreenTexture = AssetManagerUtils.getTexture(AssetManagerUtils.NUMBER_PAD_GREEN);
            numberPadScreenObject = new TextureScreenObject(numberPadGreenTexture, getScreenObjectStartXPosition(), getStartYPositionOfNumberBlocks(),
                    NUMBER_PAD_SIZE.x, NUMBER_PAD_SIZE.y);
        }
        super.onShowAgain();
    }


    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);

        if (null != draggingNumberBlockInNumberPad) {
            draggingNumberBlockInNumberPad.setPosition(screenX, screenY);
        }
    }

    @Override
    protected void initNumberBlocks() {

        initNumberBlocksHorizontalTexture();

        numberBlocks = new ImageActor[numberBlocksAutoCognitaTextureRegions.length];

        //init number blocks from 10 to 1
        for (int i = 0; i < numberBlocksAutoCognitaTextureRegions.length; i++) {

            IconPosition iconPosition = getNumberBlockIconPositionInTexture(10 - i - 1);
            numberBlocks[i] = new ImageActor(AssetManagerUtils.NUMBER_BLOCKS_HORIZONTAL, iconPosition);
            numberBlocks[i].setId(10 - i);
            numberBlocks[i].setOrigin(getNumberBlockStartXPosition(), getStartYPositionOfNumberBlocks() + i * iconPosition.height);
            numberBlocks[i].setPosition(numberBlocks[i].getOriginX(), numberBlocks[i].getOriginY());
            numberBlocks[i].setSize(iconPosition.width, iconPosition.height);
            stage.addActor(numberBlocks[i]);
        }
    }

    @Override
    protected void resetScreen() {
        super.resetScreen();
        if (null != numberBlockListInNumberPad) {
            for (ImageActor numberBlock : numberBlockListInNumberPad) {
                numberBlock.remove();
            }
            numberBlockListInNumberPad.clear();
            numberBlockListInNumberPad = null;
        }
        isYNumberBlockDragged = false;
        isXNumberBlockDragged = false;
        isNumberBlockSelected = false;
    }

    private float getNumberBlockStartXPosition() {
        return getScreenObjectStartXPosition() + GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_PAD + NUMBER_PAD_SIZE.x;
    }

    protected void generateXYList() {
        if (null == xyList) {
            List<Integer> xList = new ArrayList<Integer>(MAXIMUM_NUMBER_OF_ROUND);
            for (int i = 0; i < MAXIMUM_NUMBER_OF_ROUND; i++) {
                xList.add(RandomUtils.getRandomWithExclusion(1, 10, ArrayUtils.toArray(xList)));
            }
            xyList = new ArrayList<String>(MAXIMUM_NUMBER_OF_ROUND);
            for (int i = 0; i < MAXIMUM_NUMBER_OF_ROUND; i++) {
                int xValue = xList.get(i);
                xyList.add(xValue + "_" + RandomUtils.getRandomWithExclusion(1, 10, xValue));
            }
        }
    }

    private void resetNumber() {
        x = 0;
        y = 0;
        getXY();
    }

    private float getScreenObjectStartXPosition() {
        float contentWidth = 498
                + GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_PAD + NUMBER_PAD_SIZE.x;

        return ScreenUtils.getXPositionForCenterObject(contentWidth);
    }

    private float getStartYPositionOfNumberBlocks() {
        return getStartYPositionOfNumberKeyboard() + NUMBER_BLOCK_SIZE.y + GAP_BETWEEN_NUMBER_BLOCK_AND_NUMBER_PAD;
    }

    private void getXY() {
        String xy = xyList.get(numberOfRoundPlayed);
        x = Integer.valueOf(xy.split("_")[0]);
        y = Integer.valueOf(xy.split("_")[1]);
        Gdx.app.log(getClass().getName(), "generate x = " + x);
        Gdx.app.log(getClass().getName(), "generate y = " + y);
    }

    protected void nextRound() {
        resetScreen();
        resetNumber();
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        onHelp();
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        super.onNoIntroductionAudioPlay();
        onHelp();
    }

    @Override
    public void onHelp() {
        if (isHelpAudioAllowToPlay()) {
            if (0 == numberOfRoundPlayed) {
                abstractAutoCognitaScreen.setTouchAllow(false);
                if (isNumberBlockSelected) {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.instructionScript2AudioFileNameList, new AbstractSoundPlayListListener() {
                        @Override
                        public void onComplete() {
                            numberKeyBlockScreenObjectList.get(Math.abs(x - y) - 1).isHighlighted = true;
                            abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
                                @Override
                                public void onCorrectSoundPlayed() {
                                    numberOfRoundPlayed++;
                                    numberKeyBlockScreenObjectList.get(Math.abs(x - y) - 1).isHighlighted = false;
                                    rollbackDraggingNumberBlock(numberBlocks[10 - x]);
                                    rollbackDraggingNumberBlock(numberBlocks[10 - y]);
                                    nextRound();
                                    abstractAutoCognitaScreen.setTouchAllow(true);
                                    onHelp();
                                }
                            });
                        }
                    });
                } else if (null != numberPadScreenObject) {
                    abstractAutoCognitaScreen.playSound(mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(x, y), new AbstractSoundPlayListListener() {

                        @Override
                        public void beforePlaySound(int index) {
                            if (1 == index) {
                                MoveToAction moveAction = new MoveToAction();
                                moveAction.setPosition(numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x, numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y);
                                moveAction.setDuration(1.5f);
                                numberBlocks[10 - x].setZIndex(numberBlocks.length - 1);
                                numberBlocks[10 - x].addAction(moveAction);
                            } else if (3 == index) {
                                MoveToAction moveAction = new MoveToAction();
                                moveAction.setPosition(numberPadScreenObject.xPositionInScreen + START_POSITION_IN_NUMBER_PAD.x,
                                        numberPadScreenObject.yPositionInScreen + START_POSITION_IN_NUMBER_PAD.y + BLOCK_SIZE_IN_NUMBER_PAD);
                                moveAction.setDuration(1.5f);
                                numberBlocks[10 - y].setZIndex(numberBlocks.length - 2);
                                numberBlocks[10 - y].addAction(moveAction);
                            }
                        }

                        @Override
                        public void onComplete() {
                            int numberBlockIndex = isMore ? 10 - Math.max(x, y) : 10 - Math.min(x, y);
                            AnimationUtils.doFlash(numberBlocks[numberBlockIndex]);
                            afterCorrectNumberBlockSelected();
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
        if (isNumberBlockSelected) {
            return mathAudioScriptWithElementCode.instructionScript2AudioFileNameList;
        } else {
            return mathAudioScriptWithElementCode.getInstructionScriptAudioFileNameList(x, y);
        }
    }

    @Override
    protected void render() {
        batch.begin();
        ScreenObjectUtils.draw(batch, numberPadScreenObject);
        batch.end();
        super.render();
        if (null != draggingNumberBlockInNumberPad) {
            draggingNumberBlockInNumberPad.toFront();
        }

    }

    @Override
    protected int getMaximumNumberOfNumberBlockRequiredToShowInKeyboard() {
        return 10;
    }

    @Override
    protected float getStartXPositionOfNumberKeyboard(float totalWidthOfNumberKeyboard) {
        return getScreenObjectStartXPosition();
    }

    @Override
    protected float getStartYPositionOfNumberKeyboard() {
        return TRASH_ICON_POSITION.y;
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{AssetManagerUtils.NUMBER_PAD_GREEN}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        resetScreen();
        numberOfRoundPlayed = 0;
        numberPadScreenObject = null;
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchUp(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        doTouchUp(screenX, screenY);
        rollbackDraggingNumberBlock();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        if (!isTouchingNumberBlock(screenX, screenY)) {
            //check if it is touching the number pad
            if (TouchUtils.isTouched(numberPadScreenObject, screenX, screenY)) {
                //check if touching the blocks in the number pad
                ImageActor touchedScreenObject = ScreenObjectUtils.getTouchingActor(numberBlockListInNumberPad, screenX, screenY);

                if (null != touchedScreenObject) {
                    draggingNumberBlockInNumberPad = touchedScreenObject;
                    draggingNumberBlockInNumberPad.setTouchingPosition(screenX, screenY);
                    stage.addActor(draggingNumberBlockInNumberPad);
                    //draggingNumberBlockInNumberPad.touchingPosition(screenX, screenY);
                }
            }
        }
    }

    @Override
    protected void afterNumberKeyboardSelected(int number) {
        if (isNumberBlockSelected) {
            //check if the user pressed the correct different number
            if (number == Math.abs(x - y)) {
                afterPlayCorrected();
            }

        }
    }

    protected void doTouchUp(int screenX, int screenY) {
        //if the user dragging the number block and dropped on the number pad
        if (null != draggingNumberBlock && TouchUtils.isTouched(numberPadScreenObject, screenX, screenY)) {
            //make sure the number block is only can be dragged when the x and y number are not dragged
            if (!isXNumberBlockDragged || !isYNumberBlockDragged) {

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

                float targetX = getDropXPositionInNumberPad(draggingNumberBlock.getX());
                float targetY = getDropYPositionInNumberPad(draggingNumberBlock.getY());

                if (isNumberBlockOverlapOtherNumberBlockInNumberPad(targetX, targetY,
                        draggingNumberBlock.getWidth(), BLOCK_SIZE_IN_NUMBER_PAD, null)) {
                    for (ImageActor numberBlock : numberBlockListInNumberPad) {
                        targetY += numberBlock.getHeight();
                    }
                }
                ImageActor numberBlock = draggingNumberBlock.clone(targetX, targetY);
                removeDraggingNumberBlock();
                numberBlockListInNumberPad.add(numberBlock);
                stage.addActor(numberBlock);
                doZIndexNumberBlockListInNumberPad();
            }
        } else if (null != draggingNumberBlockInNumberPad) {
            //if the user is dragging the number block in the number pad
            if (TouchUtils.isTouched(numberPadScreenObject, screenX, screenY)) {
                //make sure the number block is dragged inside in the number pad
                float targetX = getDropXPositionInNumberPad(draggingNumberBlockInNumberPad.getX());
                float targetY = getDropYPositionInNumberPad(draggingNumberBlockInNumberPad.getY());

                if (isNumberBlockOverlapOtherNumberBlockInNumberPad(targetX, targetY,
                        draggingNumberBlockInNumberPad.getWidth(), BLOCK_SIZE_IN_NUMBER_PAD, draggingNumberBlockInNumberPad)) {
                    rollbackDraggingNumberBlock(draggingNumberBlockInNumberPad);
                } else {
                    //make sure no overlap to other number block in the pad,change to new position
                    draggingNumberBlockInNumberPad.removeTouch();
                    draggingNumberBlockInNumberPad.setPosition(targetX, targetY);
                    draggingNumberBlockInNumberPad.setOrigin(targetX, targetY);
                    doZIndexNumberBlockListInNumberPad();
                    if (isXNumberBlockDragged && isYNumberBlockDragged && !isNumberBlockSelected &&
                            isCorrectNumberBlockSelected(draggingNumberBlockInNumberPad)) {
                        afterCorrectNumberBlockSelected();
                    }
                }

            } else {
                rollbackDraggingNumberBlock(draggingNumberBlockInNumberPad);
            }

            draggingNumberBlockInNumberPad = null;
        }
    }

    protected void afterPlayCorrected() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                numberOfRoundPlayed++;
                if (numberOfRoundPlayed == MAXIMUM_NUMBER_OF_ROUND) {
                    abstractAutoCognitaScreen.showNextSection(numberOfFails);
                } else {
                    nextRound();
                    onHelp();
                }
            }
        });
    }

    protected void doZIndexNumberBlockListInNumberPad() {
        if (CollectionUtils.isNotEmpty(numberBlockListInNumberPad)) {
            //sort the number block by the y position, the lower y position will be in the first index of the list
            Collections.sort(numberBlockListInNumberPad, new Comparator<ImageActor>() {
                @Override
                public int compare(ImageActor imageActor, ImageActor t1) {
                    return (int) (imageActor.getY() - t1.getY());
                }
            });
            for (int i = 0; i < numberBlockListInNumberPad.size(); i++) {
                //in order to do the 3D effect, the number block which is in the near bottom will be overlap the top one
                numberBlockListInNumberPad.get(i).setZIndex(stage.getActors().size - i - 1);
            }
        }
    }


}
