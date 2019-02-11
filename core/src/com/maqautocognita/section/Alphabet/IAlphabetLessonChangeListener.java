package com.maqautocognita.section.Alphabet;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.bo.LessonWithReview;
import com.maqautocognita.section.NavigationSection;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IAlphabetLessonChangeListener extends NavigationSection.ILessonChangeListener<LessonWithReview> {
    void onReviewChanged(Activity selectedReview);

    void onMasteryTestSelected(Activity selectedMasteryTest);
}
