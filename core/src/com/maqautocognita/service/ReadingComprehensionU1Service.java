package com.maqautocognita.service;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionU1Service extends AbstractReadingComprehensionService {

    private static ReadingComprehensionU1Service instance = null;

    private ReadingComprehensionU1Service() {

    }

    public static ReadingComprehensionU1Service getInstance() {
        if (instance == null) {
            instance = new ReadingComprehensionU1Service();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "1";
    }
}
