package com.maqautocognita.service;

import com.maqautocognita.constant.LessonUnitCode;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicU2LessonService extends PhonicLessonService {

    private static PhonicU2LessonService instance = null;

    public static PhonicU2LessonService getInstance() {
        if (instance == null) {
            instance = new PhonicU2LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_2.code;
    }
}
