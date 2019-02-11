package com.maqautocognita.service;

/**
 * Created by siu-chun.chi on 7/2/2017.
 */

public class ReadingComprehensionU2Service extends AbstractReadingComprehensionService {

    private static ReadingComprehensionU2Service instance = null;

    private ReadingComprehensionU2Service() {

    }

    public static ReadingComprehensionU2Service getInstance() {
        if (instance == null) {
            instance = new ReadingComprehensionU2Service();
        }
        return instance;
    }

    @Override
    protected String getUnitCode() {
        return "2";
    }
}
