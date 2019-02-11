package com.maqautocognita.listener;

import com.maqautocognita.screens.storyMode.AbstractStoryModeScreen;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public interface IMenuScreenListener {

    void onSentenceUnit1Selected();

    void onSentenceUnit2Selected();

    void onSentenceUnit3Selected();

    void onSentenceUnit4Selected();

    void onSentenceUnit5Selected();

    void onReadingComprehensionUnit1Selected();

    void onReadingComprehensionUnit2Selected();

    void onReadingComprehensionUnit3Selected();


    void onMathCountSelected();


    void onAlphabetSelected();

    void onPhonicU1Selected();

    void onPhonicU2Selected();

    void onPhonicU3Selected();

    void onPhonicU4Selected();

    void onMathAdditionSubtractionSelected();

    void onMathPlaceValueSelected();

    void onMathMultiplySelected();

    void onMathOtherTopicSelected();

    void onStoryModeSelected();

    void onStoryMissionSelected();

    void onDemoSelected();

    /**
     * Will preload all image in the village story mode, by calling the method {@link AbstractStoryModeScreen#preload()}
     */
    void preloadVillageStoryMode();


    /**
     * Will load the preloaded story mode which is loaded in those preload story mode method
     * <p/>
     * Please remember call it after preload the story mode, such as {@link #preloadVillageStoryMode()}
     */
    void loadPreloadStoryMode();

    void onHomeSelected();

    /**
     * It will show the preious Screen selected
     */
    void onPreviousScreenSelected();

    void onCharacterSetupSelected();

    void onHomeStoryModeSelected();

    void onVillageStoryModeSelected();

    void onLibraryStoryModeSelected();

    void onMarketStoryModeSelected();

    void onCafeStoryModeSelected();

    void onSchoolStoryModeSelected();

    void onStoreStoryModeSelected();

    void onCityStoryModeSelected();

    void onEnglishSelected();

    void onSwahiliSelected();

    void onLifeSkillSelected();

    void onSnapSelected();

    void onSaySomethingSelected();

    void onConversationSelected();
}
