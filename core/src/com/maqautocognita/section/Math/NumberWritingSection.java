package com.maqautocognita.section.Math;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.MathAudioScriptWithElementCode;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.AbstractHandWritingRecognizeSection;
import com.maqautocognita.service.LetterPointsSequenceService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.DotTracingUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class NumberWritingSection extends AbstractHandWritingRecognizeSection implements IMathSection {

    private static final int GAP_BETWEEN_SMALL_BLOCK = 5;

    private static final int NUMBER_OF_FAILS_TIME_REQUIRED_TO_SHOW_DOT_TRACING = 3;

    private static final int DOT_SIZE = 10;

    private static final int SMALL_BLOCK_SIZE_WIDTH = 48;

    private static final int SMALL_BLOCK_SIZE_HEIGHT = 55;

    private static final float GAP_BETWEEN_WRITING_PAD = SMALL_BLOCK_SIZE_WIDTH;

    private static final float GAP_BETWEEN_WRITING_PAD_AND_SMALL_BLOCK = SMALL_BLOCK_SIZE_WIDTH / 2;
    private static final int NUMBER_HEIGHT = 232;
    private final MathAudioScriptWithElementCode mathAudioScriptWithElementCode;
    private int numberOfFailsForTheCurrentLetter;
    private boolean isDotTraceRequired;
    //store the small white block x position which is always render below each writing pad
    private int[] smallBlockStartXPosition = new int[11];
    private float minXPositionOfLetter;
    private float minYPositionOfLetter;
    private float ratio;
    private AutoCognitaTextureRegion dot;
    private AutoCognitaTextureRegion redDot;
    private AutoCognitaTextureRegion whiteSmallBlockAutoCognitaTextureRegion;
    private AutoCognitaTextureRegion redSmallBlockAutoCognitaTextureRegion;
    private Texture smallBlockTrayTexture;
    private int currentShowingNumber = 0;
    private boolean isAnimationDone;
    private Map<Integer, List<IconPosition>> integerIconPositionListMap;
    private List<Vector2> drawingPointsForNumber1;
    private List<Vector2> drawingPointsForNumber0;
    //store the list of writing pad screen object from 0  to 10
    private List<RoundCornerRectangleScreenObject<Integer>> numberWritingPadList;

    private boolean isDotRequiredForCurrentNumber;

    public NumberWritingSection(MathAudioScriptWithElementCode mathAudioScriptWithElementCode, boolean isDotTraceRequired, AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener) {
        super(abstractAutoCognitaScreen, onHelpListener, 0);
        this.mathAudioScriptWithElementCode = mathAudioScriptWithElementCode;

        this.isDotTraceRequired = isDotTraceRequired;

        int[] minMaxYPositionOfLowerCaseA = LetterPointsSequenceService.getInstance().getMaxAndMinYPosition("1");

        minYPositionOfLetter = minMaxYPositionOfLowerCaseA[0] * ratio;

        minXPositionOfLetter = minMaxYPositionOfLowerCaseA[2];

        ratio = (104f + DOT_SIZE) / (minMaxYPositionOfLowerCaseA[1] - minMaxYPositionOfLowerCaseA[0]);

        DotTracingUtils.restart();
    }

    @Override
    protected void onShowAgain() {
        super.onShowAgain();
        numberOfFailsForTheCurrentLetter = 0;
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return mathAudioScriptWithElementCode;
    }

    @Override
    public void render() {

        initNumberWritingPadList();

        if (null == dot) {

            dot = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 921, 111, 30, 30);
            redDot = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 952, 111, 30, 30);

            smallBlockTrayTexture = AssetManagerUtils.getTexture(AssetManagerUtils.SMALL_BLOCK);

        }

        if (null == whiteSmallBlockAutoCognitaTextureRegion) {
            whiteSmallBlockAutoCognitaTextureRegion = new AutoCognitaTextureRegion(smallBlockTrayTexture, 0, 0, SMALL_BLOCK_SIZE_WIDTH, SMALL_BLOCK_SIZE_HEIGHT);
            redSmallBlockAutoCognitaTextureRegion = new AutoCognitaTextureRegion(smallBlockTrayTexture, 0, 100, SMALL_BLOCK_SIZE_WIDTH, SMALL_BLOCK_SIZE_HEIGHT);
        }

        batch.begin();

        ScreenObjectUtils.draw(batch, numberWritingPadList);

        drawSmallBlocks();

        if (null == integerIconPositionListMap) {
            integerIconPositionListMap = new HashMap<Integer, List<IconPosition>>(10);
            for (int i = 0; i <= 10; i++) {

                String number = String.valueOf(i);

                integerIconPositionListMap.put(i, LetterPointsSequenceService.getInstance().
                        getDrawLetterPoints(String.valueOf(i), (smallBlockStartXPosition[i] + (Math.min(i - 1, 4 / number.length()) * SMALL_BLOCK_SIZE_WIDTH) / 2),
                                numberWritingPadList.get(i).yPositionInScreen + (WRITING_PAD_HEIGHT * 4 / 5), minXPositionOfLetter,
                                minYPositionOfLetter, ratio, DOT_SIZE));
            }
        }

        if (isDotTraceRequired) {
            for (int i = 0; i <= 10; i++) {
                renderNumberDot(i);
            }
        }

        if (isDotTraceRequired && !isAnimationDone) {
            isAnimationDone = DotTracingUtils.drawDotTracingLetter(batch, integerIconPositionListMap.get(currentShowingNumber), DOT_SIZE);
        }

        if (isDotRequiredForCurrentNumber && !isAnimationDone) {
            if (!isDotTraceRequired) {
                //make sure only the dot will not render duplicate
                renderNumberDot(currentShowingNumber);
            }
            isAnimationDone = DotTracingUtils.drawDotTracingLetter(batch, integerIconPositionListMap.get(currentShowingNumber), DOT_SIZE);
        }

        batch.end();

        super.render();
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return ArrayUtils.join(new String[]{AssetManagerUtils.SMALL_BLOCK}, super.getAllRequiredTextureName());
    }

    @Override
    public void dispose() {
        super.dispose();
        dot = null;
        redDot = null;
        whiteSmallBlockAutoCognitaTextureRegion = null;
        redSmallBlockAutoCognitaTextureRegion = null;
        smallBlockTrayTexture = null;
        currentShowingNumber = 0;

        if (null != drawingPointsForNumber1) {
            drawingPointsForNumber1.clear();
            drawingPointsForNumber1 = null;
        }

        if (null != drawingPointsForNumber0) {
            drawingPointsForNumber0.clear();
            drawingPointsForNumber0 = null;
        }

        if (null != numberWritingPadList) {
            for (RoundCornerRectangleScreenObject numberWritingPad : numberWritingPadList) {
                numberWritingPad.dispose();
            }
            numberWritingPadList.clear();
            numberWritingPadList = null;
        }

        DotTracingUtils.restart();

    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        if (currentShowingNumber < numberWritingPadList.size()) {
            return TouchUtils.isTouched(numberWritingPadList.get(currentShowingNumber), screenX, screenY);
        }
        return false;
    }

    public boolean isSaveCorrectDrawPointsRequired() {
        return true;
    }

    @Override
    public void whenLetterWriteFails() {
        super.whenLetterWriteFails();
        numberOfFailsForTheCurrentLetter++;

        if (numberOfFailsForTheCurrentLetter >= NUMBER_OF_FAILS_TIME_REQUIRED_TO_SHOW_DOT_TRACING) {
            isDotRequiredForCurrentNumber = true;
            numberOfFailsForTheCurrentLetter = 0;
            isAnimationDone = false;
        }
    }

    @Override
    public boolean isWriteCorrect() {
        if (null != AutoCognitaGame.handWritingRecognizeService && isWritingNumber10()) {
            boolean isCorrect = false;

            AutoCognitaGame.handWritingRecognizeService.clearPoints();
            //check if number 1 write correctly
            for (Vector2 vector2 : drawingPointsForNumber1) {
                AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
            }

            if (AutoCognitaGame.handWritingRecognizeService.isCorrect("1")) {
                AutoCognitaGame.handWritingRecognizeService.clearPoints();
                //check if number 0 write correctly
                for (Vector2 vector2 : drawingPointsForNumber0) {
                    AutoCognitaGame.handWritingRecognizeService.addPoint(vector2.x, vector2.y);
                }

                isCorrect = AutoCognitaGame.handWritingRecognizeService.isCorrect("0");
            }

            drawingPointsForNumber0.clear();
            drawingPointsForNumber1.clear();

            return isCorrect;

        } else {
            return super.isWriteCorrect();
        }
    }

    @Override
    protected String getLetter() {
        return String.valueOf(currentShowingNumber);
    }

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (isWritingNumber10()) {
            //check if the touching position is within the number "1" area
            if (TouchUtils.isTouched(smallBlockStartXPosition[10],
                    numberWritingPadList.get(10).yPositionInScreen,
                    SMALL_BLOCK_SIZE_WIDTH * 2, NUMBER_HEIGHT, screenX, screenY)) {
                if (null == drawingPointsForNumber1) {
                    drawingPointsForNumber1 = new ArrayList<Vector2>();
                }

                drawingPointsForNumber1.add(new Vector2(systemDetectXPosition, systemDetectYPosition));

            } else {
                if (null == drawingPointsForNumber0) {
                    drawingPointsForNumber0 = new ArrayList<Vector2>();
                }

                drawingPointsForNumber0.add(new Vector2(systemDetectXPosition, systemDetectYPosition));
            }
        }
    }

    @Override
    protected void doWhenLetterWriteCorrect() {
        numberOfFailsForTheCurrentLetter = 0;
        //dehighlight the current number writing pad
        setHighlightStatusForWritingPad(currentShowingNumber, false);
        currentShowingNumber++;
        //restart the tracing animation
        DotTracingUtils.restart();

        if (currentShowingNumber > 10) {
            //which mean all number are wrote correctly
            abstractAutoCognitaScreen.showNextSection(numberOfFails);
            isAnimationDone = true;
        } else {
            //highlight another writing pad
            setHighlightStatusForWritingPad(currentShowingNumber, true);
            numberOfFailsForTheCurrentLetter = 0;
        }
    }

    private void setHighlightStatusForWritingPad(int number, boolean isHighLight) {
        for (RoundCornerRectangleScreenObject<Integer> roundCornerRectangleScreenObject : numberWritingPadList) {
            if (roundCornerRectangleScreenObject.id == number) {
                roundCornerRectangleScreenObject.isHighlighted = isHighLight;
            }
        }
    }

    private boolean isWritingNumber10() {
        return "10".equals(getLetter());
    }

    private void initNumberWritingPadList() {
        if (null == numberWritingPadList) {

            numberWritingPadList = new ArrayList<RoundCornerRectangleScreenObject<Integer>>(11);

            final float writingPadWidth = SMALL_BLOCK_SIZE_WIDTH * 5;

            final float totalWritingPadWidth = writingPadWidth * 6 + GAP_BETWEEN_WRITING_PAD * 5;

            float startXPosition = ScreenUtils.getXPositionForCenterObject(totalWritingPadWidth);

            final float totalWritingPadHeight = WRITING_PAD_HEIGHT * 2 + SMALL_BLOCK_SIZE_HEIGHT * 3 + GAP_BETWEEN_WRITING_PAD_AND_SMALL_BLOCK * 3;

            final float startYPosition = ScreenUtils.getStartYPositionForCenterObject(totalWritingPadHeight) - WRITING_PAD_HEIGHT;

            for (int number = 0; number <= 10; number++) {
                RoundCornerRectangleScreenObject writingPad = new RoundCornerRectangleScreenObject<Integer>(number,
                        startXPosition,
                        //y position
                        (number < 6 ? startYPosition : startYPosition - WRITING_PAD_HEIGHT - GAP_BETWEEN_WRITING_PAD_AND_SMALL_BLOCK * 2 - SMALL_BLOCK_SIZE_HEIGHT),
                        //width
                        SMALL_BLOCK_SIZE_WIDTH * 5,
                        //height
                        WRITING_PAD_HEIGHT, 5);
                if (0 == number) {
                    writingPad.isHighlighted = true;
                }
                numberWritingPadList.add(writingPad);

                smallBlockStartXPosition[number] = (int) (startXPosition) + (SMALL_BLOCK_SIZE_WIDTH * 5 - SMALL_BLOCK_SIZE_WIDTH * Math.min(number, 5)) / 2;

                startXPosition += writingPadWidth + GAP_BETWEEN_WRITING_PAD;

                if (number == 5) {
                    //restart the x position for the next row, the number 6 should be align vertically same as number 1
                    startXPosition = numberWritingPadList.get(1).xPositionInScreen;
                }
            }

        }
    }

    private void drawSmallBlocks() {
        for (int number = 1; number <= 10; number++) {
            for (int smallBlockIndex = 0; smallBlockIndex < number; smallBlockIndex++) {
                int startXPosition = smallBlockStartXPosition[number] + smallBlockIndex * (whiteSmallBlockAutoCognitaTextureRegion.getRegionWidth() + GAP_BETWEEN_SMALL_BLOCK);

                float startYPosition = numberWritingPadList.get(number).yPositionInScreen - SMALL_BLOCK_SIZE_HEIGHT - GAP_BETWEEN_WRITING_PAD_AND_SMALL_BLOCK;

                if (number > 5) {
                    if (number >= 6 && smallBlockIndex < number - 5) {
                        startYPosition -= whiteSmallBlockAutoCognitaTextureRegion.getRegionHeight();
                    } else {
                        startXPosition = smallBlockStartXPosition[number] + (smallBlockIndex - (number - 5)) * (whiteSmallBlockAutoCognitaTextureRegion.getRegionWidth() + GAP_BETWEEN_SMALL_BLOCK);
                    }

                }
                batch.draw(isShowRed(number) ? redSmallBlockAutoCognitaTextureRegion : whiteSmallBlockAutoCognitaTextureRegion, startXPosition, startYPosition);
            }
        }
    }

    private void renderNumberDot(int number) {
        for (IconPosition iconPosition : integerIconPositionListMap.get(number)) {
            AutoCognitaTextureRegion drawingDot = isShowRed(number) ? redDot : dot;
            DotTracingUtils.drawDot(batch, drawingDot, iconPosition, DOT_SIZE);
        }
    }

    private boolean isShowRed(int number) {
        if (number < currentShowingNumber) {
            return true;
        } else {
            return number == currentShowingNumber && isAnimationDone;
        }
    }

    @Override
    public void reloadVoScript() {
        mathAudioScriptWithElementCode.loadVoScript();
    }

    @Override
    public MathAudioScriptWithElementCode getMathAudioScriptWithElementCode() {
        return mathAudioScriptWithElementCode;
    }
}
