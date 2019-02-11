package com.maqautocognita.service;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionU3Service extends AbstractReadingComprehensionService {

    private static ReadingComprehensionU3Service instance = null;

    private ReadingComprehensionU3Service() {

    }

    public static ReadingComprehensionU3Service getInstance() {
        if (instance == null) {
            instance = new ReadingComprehensionU3Service();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "3";
    }
}
