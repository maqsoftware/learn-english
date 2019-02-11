package com.maqautocognita.section.Alphabet;

import com.maqautocognita.bo.AbstractAudioFile;
import com.maqautocognita.bo.Activity;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.screens.AbstractLetterScreen;
import com.maqautocognita.section.AbstractHandWritingRecognizeSection;
import com.maqautocognita.section.IActivityChangeListener;
import com.maqautocognita.service.LetterPointsSequenceService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.CollectionUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AlphabetWriteSection extends AbstractHandWritingRecognizeSection implements IActivityChangeListener {

    public static final IconPosition WRITING_PAD_ICON_POSITION = new IconPosition(440, 700, 320, 420);
    private static final IconPosition HIGHLIGHTED_WRITING_PAD_ICON_POSITION = new IconPosition(760, 700, 320, 420);

    private final Vector2 WRITING_PAD_SIZE;

    /**
     * store the current showing letter
     */
    private String letter;
    private Activity selectedActivity;
    private WritingStage currentWritingStage;

    private float writingPadXPosition, writingPadYPosition;

    public AlphabetWriteSection(AbstractLetterScreen abstractLetterScreen) {
        super(abstractLetterScreen, abstractLetterScreen, 150);
        float widthRatio = 1;

        WRITING_PAD_SIZE = new Vector2(WRITING_PAD_ICON_POSITION.width * widthRatio, WRITING_PAD_ICON_POSITION.height * widthRatio);

        writingPadXPosition = ScreenUtils.getXPositionForCenterObject(WRITING_PAD_SIZE.x);
        writingPadYPosition = ScreenUtils.getBottomYPositionForCenterObject(WRITING_PAD_SIZE.y);
    }

    @Override
    protected void onIntroductionAudioPlayed() {
        if (CollectionUtils.isNotEmpty(selectedActivity.getShortInstructionAudioFilenameList())) {
            abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList().get(0));
        }
    }

    @Override
    protected AbstractAudioFile getAudioFile() {
        return selectedActivity;
    }

    @Override
    public void setSelectedActivity(Activity selectedActivity, boolean isMasterTest) {
        this.selectedActivity = selectedActivity;
        if (null != selectedActivity) {
            this.letter = selectedActivity.getLetter();
            currentWritingStage = WritingStage.BIG_LETTER_WRITING_WITH_RED_DOT_TRACING;
        }
    }

    private enum WritingStage {
        BIG_LETTER_WRITING_WITH_RED_DOT_TRACING, BIG_LETTER_WRITING_WITHOUT_RED_DOT_TRACING, SMALL_LETTER_WRITING_WITH_RED_DOT_TRACING, SMALL_LETTER_WRITING_WITHOUT_RED_DOT_TRACING
    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.GENERAL_ICONS, AssetManagerUtils.ICONS};
    }


    @Override
    protected void initWritingPadTexture() {
        if (null == writingPadAutoCognitaTextureRegion) {
            writingPadAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), WRITING_PAD_ICON_POSITION);
            highlightedWritingPadAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), HIGHLIGHTED_WRITING_PAD_ICON_POSITION);

            alphabetDotTracingSection.setLetterStartXPosition(writingPadXPosition + (WRITING_PAD_SIZE.x -
                    LetterPointsSequenceService.getInstance().getWidthOfTheLetter('A', alphabetDotTracingSection.getRatio())) / 2 + alphabetDotTracingSection.getDotSize() / 2);
            alphabetDotTracingSection.setLetterStartYPosition(writingPadYPosition + WRITING_PAD_SIZE.y - WRITING_PAD_SIZE.y / 3 + alphabetDotTracingSection.getDotSize());
        }
    }


    @Override
    public void render() {

        initWritingPadTexture();

        batch.begin();

        //draw writing pad
        batch.draw(writingPadAutoCognitaTextureRegion,
                writingPadXPosition, writingPadYPosition, WRITING_PAD_SIZE.x, WRITING_PAD_SIZE.y
        );
        batch.end();

        switch (currentWritingStage) {
            case BIG_LETTER_WRITING_WITH_RED_DOT_TRACING:
                alphabetDotTracingSection.drawBigLetter(letter);
                if (WritingStage.BIG_LETTER_WRITING_WITH_RED_DOT_TRACING.equals(currentWritingStage)) {
                    alphabetDotTracingSection.drawRedDotTracingForBigLetter();
                }
                break;
            case SMALL_LETTER_WRITING_WITH_RED_DOT_TRACING:
                alphabetDotTracingSection.drawSmallLetter(letter);
                if (WritingStage.SMALL_LETTER_WRITING_WITH_RED_DOT_TRACING.equals(currentWritingStage)) {
                    alphabetDotTracingSection.drawRedDotTracingForSmallLetter();
                }
                break;
        }


        super.render();

    }


    @Override
    protected String getLetter() {
        return (WritingStage.BIG_LETTER_WRITING_WITHOUT_RED_DOT_TRACING.equals(currentWritingStage) || WritingStage.BIG_LETTER_WRITING_WITH_RED_DOT_TRACING.equals(currentWritingStage)) ? letter.toUpperCase() : letter.toLowerCase();
    }

    @Override
    protected void doWhenLetterWriteCorrect() {

        switch (currentWritingStage) {
            case BIG_LETTER_WRITING_WITH_RED_DOT_TRACING:
                currentWritingStage = WritingStage.BIG_LETTER_WRITING_WITHOUT_RED_DOT_TRACING;
                if (isShortInstructionAudioFilenameListExists() && selectedActivity.getShortInstructionAudioFilenameList().size() > 1) {
                    abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList().get(1));
                }
                break;
            case BIG_LETTER_WRITING_WITHOUT_RED_DOT_TRACING:
                currentWritingStage = WritingStage.SMALL_LETTER_WRITING_WITH_RED_DOT_TRACING;
                if (isShortInstructionAudioFilenameListExists()) {
                    abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList().get(0));
                }

                break;
            case SMALL_LETTER_WRITING_WITH_RED_DOT_TRACING:
                currentWritingStage = WritingStage.SMALL_LETTER_WRITING_WITHOUT_RED_DOT_TRACING;
                if (isShortInstructionAudioFilenameListExists() && selectedActivity.getShortInstructionAudioFilenameList().size() > 1) {
                    abstractAutoCognitaScreen.playSound(selectedActivity.getShortInstructionAudioFilenameList().get(1));
                }
                break;
            case SMALL_LETTER_WRITING_WITHOUT_RED_DOT_TRACING:
                //if both letter write success without showing dot, then jump to next letter
                abstractAutoCognitaScreen.showNextSection(numberOfFails);
                break;
        }


    }

    private boolean isShortInstructionAudioFilenameListExists() {
        return CollectionUtils.isNotEmpty(selectedActivity.getShortInstructionAudioFilenameList());
    }


}
