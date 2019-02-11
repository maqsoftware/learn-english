package com.maqautocognita.section;

import com.maqautocognita.bo.AbstractSentence;
import com.maqautocognita.bo.SentenceLesson;
import com.maqautocognita.bo.SentenceWithActivityCode;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.screens.AbstractAutoCognitaScreen;

import java.util.List;

/**
 * Created by siu-chun.chi on 5/8/2017.
 */

public class SentenceNavigationSection<
        T extends AbstractSentence,
        I extends NavigationSection.ILessonChangeListener> extends NavigationSection<SentenceLesson<T>, I> {

    private int selectedSentenceIndex;
    private ISentenceSelectListener sentenceSelectListener;

    public SentenceNavigationSection(AbstractAutoCognitaScreen abstractAutoCognitaScreen, IMenuScreenListener menuScreenListener, List lessonList) {
        super(abstractAutoCognitaScreen, menuScreenListener, lessonList);
    }

    public void setSentenceSelectListener(ISentenceSelectListener sentenceSelectListener) {
        this.sentenceSelectListener = sentenceSelectListener;
    }

    public interface ISentenceSelectListener<I extends AbstractSentence> {
        void onSentenceSelected(SentenceWithActivityCode<I> selectedSentenceWithActivityCode);
    }

    @Override
    public void onSelectPreviousSection() {

        boolean isRequiredTriggerSelectSentenceListener = false;

        if (selectedSentenceIndex - 1 >= 0) {
            selectedSentenceIndex--;
            isRequiredTriggerSelectSentenceListener = true;
        } else {
            //select previous lesson
            if (selectedLessonIndex - 1 >= 0) {
                selectedLessonIndex--;
                selectedSentenceIndex = lessonList.get(selectedLessonIndex).sentenceWithActivityCodeList.size() - 1;
                setSelectedLesson(lessonList.get(selectedLessonIndex));
                isRequiredTriggerSelectSentenceListener = true;
            }
        }
        if (isRequiredTriggerSelectSentenceListener && null != sentenceSelectListener) {
            sentenceSelectListener.onSentenceSelected(getSelectedSentenceWithLessonCode());
        }
    }


    @Override
    protected void onSelectNextSection() {

        boolean isRequiredTriggerSelectSentenceListener = false;

        if (selectedSentenceIndex + 1 < selectedLesson.sentenceWithActivityCodeList.size()) {
            selectedSentenceIndex++;
            isRequiredTriggerSelectSentenceListener = true;
        } else {
            //select next lesson
            if (selectedLessonIndex + 1 < lessonList.size()) {
                selectedLessonIndex++;
                setSelectedLesson(lessonList.get(selectedLessonIndex));
                isRequiredTriggerSelectSentenceListener = true;
            }
        }
        if (isRequiredTriggerSelectSentenceListener) {
            triggerListener();
        }
    }

    @Override
    protected boolean isEnableLeftArrow() {
        if (selectedSentenceIndex - 1 >= 0) {
            return true;
        }

        return super.isEnableLeftArrow();
    }

    @Override
    protected boolean isEnableRightArrow() {
        if (selectedSentenceIndex + 1 < lessonList.get(selectedLessonIndex).sentenceWithActivityCodeList.size()) {
            return true;
        }
        return super.isEnableRightArrow();
    }

    @Override
    public void setSelectedLesson(SentenceLesson lesson) {
        super.setSelectedLesson(lesson);
        //every time call this method, which means always the lesson is changed, so the sentence index should be reset to 0
        selectedSentenceIndex = 0;
        triggerListener();
    }

    private void triggerListener() {
        if (null != sentenceSelectListener) {
            sentenceSelectListener.onSentenceSelected(getSelectedSentenceWithLessonCode());
        }
    }

    private SentenceWithActivityCode<T> getSelectedSentenceWithLessonCode() {
        return lessonList.get(selectedLessonIndex).sentenceWithActivityCodeList.get(selectedSentenceIndex);
    }

    protected boolean isAllLessonCompleted() {
        return selectedLessonIndex + 1 >= lessonList.size() &&
                selectedSentenceIndex + 1 >= selectedLesson.sentenceWithActivityCodeList.size();
    }

}
