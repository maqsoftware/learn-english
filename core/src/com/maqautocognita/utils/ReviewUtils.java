package com.maqautocognita.utils;

import com.maqautocognita.bo.Activity;
import com.maqautocognita.service.AlphabetLessonService;

import java.util.List;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class ReviewUtils {

    public static Character[] getAllLettersInReview(Activity review) {
        if (null == review || null == review.getParent() || CollectionUtils.isEmpty(review.getParent().getLetterList())) {
            return null;
        }
        List<String> alphabetList = review.getParent().getLetterList();
        Character letters[] = new Character[alphabetList.size()];
        for (int i = 0; i < alphabetList.size(); i++) {
            letters[i] = alphabetList.get(i).charAt(0);
        }

        return letters;
    }

    public static Character[] getAllLetterForAlphabetMasteryTest() {
        List<String> alphabetList = AlphabetLessonService.getInstance().getAlphabetList();
        Character letters[] = new Character[alphabetList.size()];
        for (int i = 0; i < alphabetList.size(); i++) {
            letters[i] = alphabetList.get(i).charAt(0);
        }

        return letters;
    }
}
