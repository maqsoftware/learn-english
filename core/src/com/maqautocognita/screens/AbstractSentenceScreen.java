package com.maqautocognita.screens;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.section.IAutoCognitaSection;
import com.maqautocognita.section.NavigationSection;
import com.maqautocognita.section.SentenceNavigationSection;
import com.maqautocognita.section.sentence.AbstractSentenceSection;
import com.maqautocognita.section.sentence.NegatedVerbConjugationSection;
import com.maqautocognita.section.sentence.NounConjugationSection;
import com.maqautocognita.section.sentence.PositiveVerbConjugationSection;
import com.maqautocognita.section.sentence.SentenceArrangeSection;
import com.maqautocognita.section.sentence.SentenceBuildSection;
import com.maqautocognita.section.sentence.SentenceMultipleChoiceSection;
import com.maqautocognita.section.sentence.SentenceTypeSection;
import com.maqautocognita.section.sentence.SentenceWriteSection;
import com.maqautocognita.section.sentence.SwahiliSentenceArrangeSection;
import com.maqautocognita.section.sentence.SwahiliSentenceWriteSection;
import com.maqautocognita.service.AbstractLessonService;
import com.maqautocognita.service.AbstractSentenceLessonService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

import java.io.File;
import java.util.List;

/**
 * Created by siu-chun.chi on 5/5/2017.
 */

public abstract class AbstractSentenceScreen<T extends AbstractSentence> extends AbstractAutoCognitaScreen {

    protected SentenceNavigationSection<T, NavigationSection.ILessonChangeListener> navigationSection;
    protected AbstractSentenceSection selectedSentenceSection;
    private SentenceMultipleChoiceSection sentenceMultipleChoiceSection;
    private SentenceArrangeSection sentenceArrangeSection;
    private SwahiliSentenceArrangeSection swahiliSentenceArrangeSection;
    private SentenceWriteSection sentenceWriteSection;
    private SwahiliSentenceWriteSection swahiliSentenceWriteSection;
    private SentenceTypeSection sentenceTypeSection;
    private SentenceWithActivityCode selectedSentenceWithActivityCode;

    private PositiveVerbConjugationSection positiveVerbConjugationSection;
    private NegatedVerbConjugationSection negatedVerbConjugationSection;

    private NounConjugationSection nounConjugationSection;

    private SentenceBuildSection sentenceBuildSection;

    public AbstractSentenceScreen(AbstractGame game, IMenuScreenListener menuScreenListener, String... images) {
        super(game, menuScreenListener, images);
    }

    @Override
    public AbstractLessonService getLessonService() {
        return null;
    }

    @Override
    public void showNextSection(int numberOfFails) {
        playCorrectSound();
        navigationSection.onLessonComplete();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (null != navigationSection) {
            navigationSection.whenTouchDown((int) x, (int) y, (int) x, (int) y);
        }
        if (null != sentenceWriteSection) {
            sentenceWriteSection.touchDown((int) x, (int) y);
        }
        if (null != swahiliSentenceWriteSection) {
            swahiliSentenceWriteSection.touchDown((int) x, (int) y);
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        int screenX = (int) ScreenUtils.toViewPosition(x);
        int screenY = (int) ScreenUtils.getExactYPositionOnScreen(y);
        if (null != sentenceWriteSection) {
            sentenceWriteSection.touchDragged(screenX, screenY, (int) x, (int) y);
        }
        if (null != swahiliSentenceWriteSection) {
            swahiliSentenceWriteSection.touchDragged(screenX, screenY, (int) x, (int) y);
        }

        return super.pan(x, y, deltaX, deltaY);
    }

    @Override
    protected List<? extends IAutoCognitaSection> getAutoCognitaSectionList() {
        return null;
    }

    @Override
    public boolean touchUp(float x, float y, int pointer, int button) {

        if (null != sentenceWriteSection) {
            sentenceWriteSection.touchUp();
        }

        if (null != swahiliSentenceWriteSection) {
            swahiliSentenceWriteSection.touchUp();
        }
        return super.touchUp(x, y, pointer, button);
    }

    @Override
    protected String getAudioPath() {
        return (UserPreferenceUtils.getInstance().isEnglish() ? "english" : "swahili") + File.separator + "sentences" + File.separator;
    }

    @Override
    public void show() {
        super.show();
        navigationSection = new SentenceNavigationSection<T, NavigationSection.ILessonChangeListener>(this,
                menuScreenListener, getSentenceLessonService().getAllLesson());
        navigationSection.setSentenceSelectListener(new SentenceNavigationSection.ISentenceSelectListener() {

            @Override
            public void onSentenceSelected(SentenceWithActivityCode selectedSentenceWithActivityCode) {

                onLessonSelected(selectedSentenceWithActivityCode);

            }
        });

        navigationSection.onResize();
    }

    protected abstract AbstractSentenceLessonService getSentenceLessonService();

    protected void onLessonSelected(SentenceWithActivityCode<T> selectedSentenceWithActivityCode) {
        AbstractSentenceScreen.this.selectedSentenceWithActivityCode = selectedSentenceWithActivityCode;

        if (null != selectedSentenceSection) {
            selectedSentenceSection.hide();
            selectedSentenceSection = null;
        }

        switch (selectedSentenceWithActivityCode.activityCodeEnum) {
            case READ_AND_LISTEN:
            case MULTIPLE_CHOICE:
                if (null == sentenceMultipleChoiceSection) {
                    sentenceMultipleChoiceSection = new SentenceMultipleChoiceSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = sentenceMultipleChoiceSection;
                break;
            case READ_ARRANGE:
            case LISTEN_ARRANGE:
            case JUST_ARRANGE:
                if (UserPreferenceUtils.getInstance().isEnglish()) {
                    if (null == sentenceArrangeSection) {
                        sentenceArrangeSection = new SentenceArrangeSection(AbstractSentenceScreen.this);
                    }
                    selectedSentenceSection = sentenceArrangeSection;
                }

                if (UserPreferenceUtils.getInstance().isSwahili()) {
                    if (null == swahiliSentenceArrangeSection) {
                        swahiliSentenceArrangeSection = new SwahiliSentenceArrangeSection(AbstractSentenceScreen.this);
                    }
                    selectedSentenceSection = swahiliSentenceArrangeSection;
                }

                break;
            case READ_AND_WRITE:
            case WRITING:
                if (UserPreferenceUtils.getInstance().isEnglish()) {
                    if (null == sentenceWriteSection) {
                        sentenceWriteSection = new SentenceWriteSection(AbstractSentenceScreen.this);
                    }
                    selectedSentenceSection = sentenceWriteSection;
                } else {
                    if (null == swahiliSentenceWriteSection) {
                        swahiliSentenceWriteSection = new SwahiliSentenceWriteSection(AbstractSentenceScreen.this);
                    }
                    selectedSentenceSection = swahiliSentenceWriteSection;
                }
                break;
            case READ_AND_TYPE:
            case LISTEN_AND_TYPE:
                if (null == sentenceTypeSection) {
                    sentenceTypeSection = new SentenceTypeSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = sentenceTypeSection;
                break;

            case POSITIVE_VERB_CONJUGATION:
                if (null == positiveVerbConjugationSection) {
                    positiveVerbConjugationSection = new PositiveVerbConjugationSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = positiveVerbConjugationSection;
                break;

            case NEGATIVE_VERB_CONJUGATION:
                if (null == negatedVerbConjugationSection) {
                    negatedVerbConjugationSection = new NegatedVerbConjugationSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = negatedVerbConjugationSection;
                break;

            case NOUN_CONJUGATION:
                if (null == nounConjugationSection) {
                    nounConjugationSection = new NounConjugationSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = nounConjugationSection;
                break;

            case SENTENCE_BUILD:
                if (null == sentenceBuildSection) {
                    sentenceBuildSection = new SentenceBuildSection(AbstractSentenceScreen.this);
                }
                selectedSentenceSection = sentenceBuildSection;
                break;
        }

        if (null != selectedSentenceSection && null != selectedSentenceWithActivityCode) {
            selectedSentenceSection.show(selectedSentenceWithActivityCode.sentence, selectedSentenceWithActivityCode.activityCodeEnum);
        }
    }

    @Override
    public void doRender() {
        if (null != navigationSection) {
            navigationSection.render(true);
        }

        if (null != selectedSentenceSection) {
            selectedSentenceSection.render();
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (null != navigationSection) {
            navigationSection.dispose();
            navigationSection = null;
        }
        if (null != selectedSentenceSection) {
            selectedSentenceSection.hide();
        }
    }

    public abstract String getUnitCode();
}
