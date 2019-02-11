package com.maqautocognita.section;

import com.maqautocognita.AutoCognitaGame;
import com.maqautocognita.graphics.AutoCognitaTextureRegion;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;
import com.maqautocognita.section.Alphabet.AlphabetDotTracingSection;
import com.maqautocognita.service.HandWritingRecognizeScreenService;
import com.maqautocognita.utils.AssetManagerUtils;
import com.maqautocognita.utils.IconPosition;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.TouchUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public abstract class AbstractHandWritingRecognizeSection extends AbstractAutoCognitaSection implements HandWritingRecognizeScreenService.IHandWritingRecognizeListener {

    protected static final int WRITING_PAD_WIDTH = 220;
    protected static final int WRITING_PAD_HEIGHT = 270;

    private static final int ERASE_SIZE = 100;
    private final IconPosition eraseScreenPosition;
    protected AutoCognitaTextureRegion writingPadAutoCognitaTextureRegion;
    protected AutoCognitaTextureRegion highlightedWritingPadAutoCognitaTextureRegion;
    protected AlphabetDotTracingSection alphabetDotTracingSection;
    private HandWritingRecognizeScreenService handWritingRecognizeScreenService;

    public AbstractHandWritingRecognizeSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IOnHelpListener onHelpListener, int letterDotTracingRatio) {
        super(abstractAutoCognitaScreen, onHelpListener);
        eraseScreenPosition = new IconPosition(ScreenUtils.getScreenWidth() - MenuSection.MenuItemEnum.HELP.iconPosition.x - ERASE_SIZE, MenuSection.MenuItemEnum.HELP.iconPosition.y, ERASE_SIZE, ERASE_SIZE);

        if (letterDotTracingRatio > 0) {
            alphabetDotTracingSection = new AlphabetDotTracingSection(letterDotTracingRatio);
        }
    }

    protected void initWritingPadTexture() {
        if (isWritingPadRequired() && null == writingPadAutoCognitaTextureRegion) {
            writingPadAutoCognitaTextureRegion = new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 0, 700, WRITING_PAD_WIDTH, WRITING_PAD_HEIGHT);
            highlightedWritingPadAutoCognitaTextureRegion =
                    new AutoCognitaTextureRegion(AssetManagerUtils.getTexture(AssetManagerUtils.GENERAL_ICONS), 220, 700, WRITING_PAD_WIDTH, WRITING_PAD_HEIGHT);
        }
    }

    protected boolean isWritingPadRequired() {
        return true;
    }

    @Override
    public void render() {

        if (null != handWritingRecognizeScreenService) {
            handWritingRecognizeScreenService.drawLine();
        }

    }

    @Override
    protected String[] getAllRequiredTextureName() {
        return new String[]{AssetManagerUtils.GENERAL_ICONS, AssetManagerUtils.ICONS};
    }

    @Override
    public void dispose() {
        super.dispose();
        if (null != alphabetDotTracingSection) {
            alphabetDotTracingSection.dispose();
        }
        highlightedWritingPadAutoCognitaTextureRegion = null;
        writingPadAutoCognitaTextureRegion = null;
        clearScreen();
    }

    @Override
    public void onResize() {
        super.onResize();
        if (null == handWritingRecognizeScreenService) {
            handWritingRecognizeScreenService = new HandWritingRecognizeScreenService(this);
        }
    }

    @Override
    protected void touchDragged(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDragged(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    @Override
    protected void touchUp(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchUp();
    }

    @Override
    protected void touchDown(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {
        handWritingRecognizeScreenService.touchDown(screenX, screenY, systemDetectXPosition, systemDetectYPosition);
    }

    protected void clearScreen() {
        if (null != handWritingRecognizeScreenService) {
            //reset everything
            handWritingRecognizeScreenService.clearDrawPoints();
            //make sure the previous saved draw points are clear
            handWritingRecognizeScreenService.clearCorrectDrawPoints();
        }

        if (null != alphabetDotTracingSection) {
            alphabetDotTracingSection.reset();
        }
    }

    @Override
    public boolean isDrawAllow(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean isSaveCorrectDrawPointsRequired() {
        return false;
//        return true;
    }

    @Override
    public void whenCorrectLetterWrite() {
        abstractAutoCognitaScreen.playCorrectSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {
                doWhenLetterWriteCorrect();
            }
        });
    }

    public void whenWrongLetterWrite(){
        abstractAutoCognitaScreen.playWrongSound(new AbstractAutoCognitaScreen.ICorrectSoundListener() {
            @Override
            public void onCorrectSoundPlayed() {

            }
        });
    }

    @Override
    public void whenLetterWriteFails() {
        addNumberOfFails();
    }

    @Override
    public boolean isWriteCorrect() {
        String letter = getLetter();
        if ("i".equalsIgnoreCase(letter)) {
            letter = "l";
        }
        return AutoCognitaGame.handWritingRecognizeService.isCorrect(letter);
    }

    protected abstract String getLetter();

    @Override
    public void afterDrawPointAdded(int screenX, int screenY, int systemDetectXPosition, int systemDetectYPosition) {

    }

    @Override
    public boolean isRequiredClearDrawPointsAfterTimesUp() {
        return true;
    }

    protected abstract void doWhenLetterWriteCorrect();

    public void reset() {
        clearScreen();
    }
}
