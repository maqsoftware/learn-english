package com.maqautocognita.section.Alphabet;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.graphics.ColorProperties;
import com.maqautocognita.graphics.RoundCornerRectangleScreenObject;
import com.maqautocognita.graphics.utils.ScreenObjectUtils;
import com.maqautocognita.screens.AbstractLetterScreen;
import com.maqautocognita.section.AbstractHandWritingRecognizeSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.section.MenuSection;
import com.maqautocognita.service.SoundService;
import com.maqautocognita.utils.ArrayUtils;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ReviewUtils;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.TouchUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetReviewMasteryWritingSection extends AbstractHandWritingRecognizeSection implements IActivityChangeListener {


    protected static final int GAP_BETWEEN_WRITING_PAD = 30;
    private static final int WRITING_PAD_BORDER_WIDTH = 12;
    private final IconPosition playLetterIconPosition;
    private int currentHighLightingWritingPadIndex;
    private String currentPlayingLetter;
    /**
     * It will store those letter which required to play in both upper and lower case
     */
    private List<Character> audioLettersPlaySequenceList;
    private List<Vector2> writingPadPositionList;
    private int numberOfLetterNeedToShow;
    private AutoCognitaTextureRegion playLetterAutoCognitaTextureRegion;
    private Activity selectedReview;
    private boolean isMasterTest;
    private List<RoundCornerRectangleScreenObject> writingPadScreenObjectList;
    private ShapeRenderer dottedLineShapeRenderer;
    private int numberOfFailsForTheCurrentLetter;
    private boolean isDotTraceRequired;
    private int maxNumberOfWritingPadPerRow;

    private float writingPadWidth;
    private float writingPadHeight;

    public AlphabetReviewMasteryWritingSection(AbstractLetterScreen abstractLetterScreen) {
        super(abstractLetterScreen, abstractLetterScreen, ScreenUtils.isLandscapeMode ? 104 : 150);
        playLetterIconPosition = new IconPosition(0, 250, 100, 100);
        float xPositionOfPlayLetterIconPosition = 0;
        float yPositionOfPlayLetterIconPosition = 0;
        if (ScreenUtils.isLandscapeMode) {
            xPositionOfPlayLetterIconPosition = 1750;
            yPositionOfPlayLetterIconPosition = 250;
            maxNumberOfWritingPadPerRow = 5;
        } else {
            xPositionOfPlayLetterIconPosition = ScreenUtils.getXPositionForCenterObject(playLetterIconPosition.width);
            yPositionOfPlayLetterIconPosition = MenuSection.MenuItemEnum.HELP.iconPosition.y;
            maxNumberOfWritingPadPerRow = 2;
        }
        playLetterIconPosition.x = xPositionOfPlayLetterIconPosition;
        playLetterIconPosition.y = yPositionOfPlayLetterIconPosition;

        if (ScreenUtils.isLandscapeMode) {
            writingPadWidth = WRITING_PAD_WIDTH;
            writingPadHeight = WRITING_PAD_HEIGHT;
        } else {
            writingPadWidth = AlphabetWriteSection.WRITING_PAD_ICON_POSITION.width;
            writingPadHeight = AlphabetWriteSection.WRITING_PAD_ICON_POSITION.height;
        }
    }

    @Override
    public void setSelectedActivity(Activity selectedReview, boolean isMasterTest) {
        this.isMasterTest = isMasterTest;
        this.selectedReview = selectedReview;
        //make sure the previous paying list is empty, so that the method initAudioLettersPlaySequenceList will be work
        if (null != audioLettersPlaySequenceList) {
            audioLettersPlaySequenceList.clear();
            audioLettersPlaySequenceList = null;
        }
    }

    @Override
    public void render() {

        initAudioLettersPlaySequenceList();

        if (null == playLetterAutoCognitaTextureRegion) {
            playLetterAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.ICONS), 100, 300, (int) playLetterIconPosition.width, (int) playLetterIconPosition.height);
        }

        if (null != audioLettersPlaySequenceList && audioLettersPlaySequenceList.size() > 0) {


            if (null == writingPadPositionList || writingPadPositionList.size() == 0) {

                int numberOfWritingPad = (audioLettersPlaySequenceList.size() <= numberOfLetterNeedToShow ? audioLettersPlaySequenceList.size() : numberOfLetterNeedToShow * 2);

                if (null == writingPadPositionList) {
                    writingPadPositionList = new ArrayList<Vector2>(numberOfWritingPad);
                }

                if (null == writingPadScreenObjectList) {
                    writingPadScreenObjectList = new ArrayList<RoundCornerRectangleScreenObject>(numberOfWritingPad);
                }

                final float numberOfWritingPadPerRow = numberOfWritingPad <= maxNumberOfWritingPadPerRow ? numberOfWritingPad : maxNumberOfWritingPadPerRow;

                final float numberOfRow = numberOfWritingPad <= maxNumberOfWritingPadPerRow ? 1 : 2;

                final float startXPosition = ScreenUtils.getXPositionForCenterObject(writingPadWidth * numberOfWritingPadPerRow + GAP_BETWEEN_WRITING_PAD * (numberOfWritingPadPerRow - 1));

                final float startYPosition = ScreenUtils.getStartYPositionForCenterObject(writingPadHeight * numberOfRow + GAP_BETWEEN_WRITING_PAD * (numberOfRow - 1));

                for (int i = 0; i < numberOfWritingPad; i++) {
                    //draw the writing pad
                    float writingPadXPosition = startXPosition + (i % numberOfWritingPadPerRow) * GAP_BETWEEN_WRITING_PAD + (i % numberOfWritingPadPerRow) * writingPadWidth;

                    float writingPadYPosition = startYPosition - (i / maxNumberOfWritingPadPerRow) * GAP_BETWEEN_WRITING_PAD - (i / maxNumberOfWritingPadPerRow + 1) * writingPadHeight;

                    writingPadPositionList.add(new Vector2(writingPadXPosition, writingPadYPosition));

                    RoundCornerRectangleScreenObject roundCornerRectangleScreenObject =
                            new RoundCornerRectangleScreenObject(writingPadXPosition, writingPadYPosition, (int) writingPadWidth, (int) writingPadHeight, WRITING_PAD_BORDER_WIDTH, ColorProperties.DISABLE_TEXT,
                                    false);

                    if (i == currentHighLightingWritingPadIndex) {
                        roundCornerRectangleScreenObject.isHighlighted = true;
                    }

                    writingPadScreenObjectList.add(roundCornerRectangleScreenObject);

                }
            }


            batch.begin();

            batch.draw(playLetterAutoCognitaTextureRegion, playLetterIconPosition.x, playLetterIconPosition.y);

            ScreenObjectUtils.draw(batch, writingPadScreenObjectList);

            batch.end();


            if (null == dottedLineShapeRenderer) {
                dottedLineShapeRenderer = new ShapeRenderer();
            }

            dottedLineShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            dottedLineShapeRenderer.setColor(ColorProperties.DISABLE_TEXT);

            for (Vector2 writingPadPosition : writingPadPositionList) {
                float startYPosition = writingPadPosition.y + 69;
                drawDottedLine(dottedLineShapeRenderer, 10, 10, writingPadPosition.x + WRITING_PAD_BORDER_WIDTH, startYPosition,
                        writingPadPosition.x + writingPadWidth, startYPosition);

                startYPosition = writingPadPosition.y + writingPadHeight - 69;
                drawDottedLine(dottedLineShapeRenderer, 10, 10, writingPadPosition.x + WRITING_PAD_BORDER_WIDTH, startYPosition,
                        writingPadPosition.x + writingPadWidth, startYPosition);
            }

            if (isDotTraceRequired) {

                if (Character.isUpperCase(getLetter().charAt(0))) {
                    alphabetDotTracingSection.drawBigLetter(getLetter());
                    alphabetDotTracingSection.drawRedDotTracingForBigLetter();
                } else {
                    alphabetDotTracingSection.drawSmallLetter(getLetter());
                    alphabetDotTracingSection.drawRedDotTracingForSmallLetter();
                }

            }


        }

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        playLetterAutoCognitaTextureRegion = null;
        if (null != audioLettersPlaySequenceList) {
            audioLettersPlaySequenceList.clear();
            audioLettersPlaySequenceList = null;
        }
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        if (TouchUtils.isTouched(playLetterIconPosition, screenX, screenY)) {
            playLetterAudio();
        } else {
            super.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
        }
    }

    @Override
    protected void clearScreen() {
        super.clearScreen();
        if (null != writingPadPositionList) {
            writingPadPositionList.clear();
            writingPadPositionList = null;
        }

        if (null != writingPadScreenObjectList) {
            for (RoundCornerRectangleScreenObject writingPad : writingPadScreenObjectList) {
                writingPad.dispose();
            }
            writingPadScreenObjectList.clear();
            writingPadScreenObjectList = null;
        }

        if (null != dottedLineShapeRenderer) {
            dottedLineShapeRenderer.dispose();
            dottedLineShapeRenderer = null;
        }

        currentHighLightingWritingPadIndex = 0;

        numberOfFailsForTheCurrentLetter = 0;
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return null != writingPadPositionList && currentHighLightingWritingPadIndex < writingPadPositionList.size() && TouchUtils.isTouched(new IconPosition(writingPadPositionList.get(currentHighLightingWritingPadIndex).x,
                writingPadPositionList.get(currentHighLightingWritingPadIndex).y, writingPadWidth, writingPadHeight), screenX, screenY);
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return true;
    }

    @Override
    public void whenCorrectLetterWrite() {
        super.whenCorrectLetterWrite();
        resetNumberDotTracing();
    }

    @Override
    public void whenWrongLetterWrite() {
        super.whenWrongLetterWrite();
        resetNumberDotTracing();
    }

    @Override
    public void whenLetterWriteFails() {
        super.whenLetterWriteFails();
        numberOfFailsForTheCurrentLetter++;

        if (numberOfFailsForTheCurrentLetter == 3) {
            resetNumberDotTracing();
            isDotTraceRequired = true;
        }
    }

    @Override
    protected String getLetter() {
        if (null == currentPlayingLetter) {
            if (CollectionUtils.isNotEmpty(audioLettersPlaySequenceList)) {
                currentPlayingLetter = audioLettersPlaySequenceList.get(0).toString();
            }
        }
        return currentPlayingLetter;
    }

    @Override
    protected void doWhenLetterWriteCorrect() {
        if (CollectionUtils.isNotEmpty(audioLettersPlaySequenceList)) {
            audioLettersPlaySequenceList.remove(0);
            currentPlayingLetter = null;
            if (audioLettersPlaySequenceList.size() > 0) {
                if (currentHighLightingWritingPadIndex + 1 >= numberOfLetterNeedToShow * 2) {
                    currentHighLightingWritingPadIndex = 0;
                    //because here is another thread for the callback of the letter correct writing
                    // post a Runnable to the rendering thread that required to show next section
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            //which mean the current round is complete, clear the previous round drawing point
                            clearScreen();
                        }
                    });
                } else {
                    isDotTraceRequired = false;
                    alphabetDotTracingSection.reset();
                    writingPadScreenObjectList.get(currentHighLightingWritingPadIndex).isHighlighted = false;
                    currentHighLightingWritingPadIndex++;
                    writingPadScreenObjectList.get(currentHighLightingWritingPadIndex).isHighlighted = true;
                }
                //play next letter;
                playLetterAudio();
            } else {
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
            }
        }

    }

    private void resetNumberDotTracing() {
        alphabetDotTracingSection.reset();
        //show the dot tracing
        configDotTracing();
        isDotTraceRequired = false;
        numberOfFailsForTheCurrentLetter = 0;
    }

    private void configDotTracing() {
        alphabetDotTracingSection.setLetterStartXPosition(
                writingPadPositionList.get(currentHighLightingWritingPadIndex).x + writingPadWidth / 4);
        alphabetDotTracingSection.setLetterStartYPosition(writingPadPositionList.get(currentHighLightingWritingPadIndex).y + writingPadHeight * 0.63f);
        if (ScreenUtils.isLandscapeMode) {
            alphabetDotTracingSection.setDotSize(10);
        }
    }

    private void playLetterAudio() {
        if (StringUtils.isNotBlank(getLetter())) {
            abstractAutoCognitaScreen.playSound(getCurrentPlayingAudioFileName(), null);
        }
    }

    private String getCurrentPlayingAudioFileName() {
        return SoundService.getInstance().getAlphabetAudioFileName(getLetter().charAt(0));
    }

    private void initAudioLettersPlaySequenceList() {
        if (null == audioLettersPlaySequenceList) {
            Character letters[] = isMasterTest ? ReviewUtils.getAllLetterForAlphabetMasteryTest() : ReviewUtils.getAllLettersInReview(selectedReview);
            if (ArrayUtils.isNotEmpty(letters)) {
                int letterSize = letters.length * 2;
                audioLettersPlaySequenceList = new ArrayList(letterSize);
                for (int i = 0; i < letters.length; i++) {
                    audioLettersPlaySequenceList.add(Character.toUpperCase(letters[i]));
                }

                //for lower case
                for (int i = 0; i < letters.length; i++) {
                    audioLettersPlaySequenceList.add(Character.toLowerCase(letters[i]));
                }

                //random the play letter sequence
                Collections.shuffle(audioLettersPlaySequenceList);

                if (audioLettersPlaySequenceList.size() / 2 == maxNumberOfWritingPadPerRow) {
                    //reach the max
                    numberOfLetterNeedToShow = maxNumberOfWritingPadPerRow;
                } else {
                    numberOfLetterNeedToShow = Math.min(isMasterTest ?
                            (ScreenUtils.isLandscapeMode ? maxNumberOfWritingPadPerRow : maxNumberOfWritingPadPerRow) : maxNumberOfWritingPadPerRow, audioLettersPlaySequenceList.size() / 2);
                }
            }
        }
    }

    /**
     * Draws a dotted line between to points (x1,y1) and (x2,y2).
     *
     * @param shapeRenderer
     * @param dotLineWidth
     * @param dotLineGap
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    private void drawDottedLine(ShapeRenderer shapeRenderer, int dotLineWidth, int dotLineGap, float x1, float y1, float x2, float y2) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
        float length = vec2.len();
        for (int i = 0; i < length; i += dotLineGap + dotLineWidth) {
            vec2.clamp(length - i, length - i);
            float startXPosition = x1 + (i > 0 ? vec2.x : 0);
            shapeRenderer.line(startXPosition, y1 + vec2.y, startXPosition + dotLineWidth, y1 + vec2.y);
        }

        shapeRenderer.end();
    }

    /**
     * In this module, introduction audio file name will be store in the {@link AbstractAudioFile#shortInstructionAudioFilenameList}.
     * <p/>
     * And there is a audio named in the pattern "alphabet_***", which mean it is a dynamic audio file,
     * will be depends current playing letter, for example now is playing the big letter A, and the file name will be replaced by the audio big letter A.
     *
     * @return
     */
    @Override
    protected List<String> getIntroductionAudioFileName() {
        if (null != getAudioFile()) {
            List<String> shortInstructionAudioNameList = getAudioFile().getIntroductionAudioFilenameList();

            if (CollectionUtils.isNotEmpty(shortInstructionAudioNameList)) {

                List<String> newAudioFileNameList = new ArrayList<String>(shortInstructionAudioNameList.size());
                for (String audioFileName : shortInstructionAudioNameList) {

                    if ("alphabet_***".equals(audioFileName)) {
                        audioFileName = getCurrentPlayingAudioFileName();
                    }

                    newAudioFileNameList.add(audioFileName);
                }

                return newAudioFileNameList;
            }
        }

        return null;
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        super.onIntroductionAudioPlayed();
        playLetterAudio();
    }

    @Override
    protected void onNoIntroductionAudioPlay() {
        super.onNoIntroductionAudioPlay();
        playLetterAudio();
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedReview;
    }

    @Override
    protected void onHelpAudioComplete() {
        playLetterAudio();
    }


}
