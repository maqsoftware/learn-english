package com.maqautocognita.service;

import com.maqautocognita.constant.LessonUnitCode;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class PhonicU3LessonService extends PhonicLessonService {

    private static PhonicU3LessonService instance = null;

    public static PhonicU3LessonService getInstance() {
        if (instance == null) {
            instance = new PhonicU3LessonService();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return LessonUnitCode.PHONIC_UNIT_3.code;
    }
}
