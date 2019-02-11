package com.maqautocognita;

import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.adapter.IDeviceCameraService;
import com.maqautocognita.adapter.IDeviceService;
import com.maqautocognita.adapter.IHandWritingRecognizeService;
import com.maqautocognita.adapter.IOCR;
import com.maqautocognita.adapter.IShareService;
import com.maqautocognita.adapter.ISpeechRecognizeService;
import com.maqautocognita.adapter.NotificationHandler;
import com.maqautocognita.constant.Language;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.screens.AlphabetScreen;
import com.maqautocognita.screens.CheatSheetForLifeScreen;
import com.maqautocognita.screens.ConversationScreen;
import com.maqautocognita.screens.DemoScreen;
import com.maqautocognita.screens.DemoScreen2;
import com.maqautocognita.screens.HomeMenuMapScreen;
import com.maqautocognita.screens.LoadingScreen;
import com.maqautocognita.screens.MathAdditionSubtractionScreen;
import com.maqautocognita.screens.MathCountingScreen;
import com.maqautocognita.screens.MathLargeNumberScreen;
import com.maqautocognita.screens.MathMultiplicationScreen;
import com.maqautocognita.screens.MathOtherTopicScreen;
import com.maqautocognita.screens.PhonicU1Screen;
import com.maqautocognita.screens.PhonicU2Screen;
import com.maqautocognita.screens.PhonicU3Screen;
import com.maqautocognita.screens.PhonicU4Screen;
import com.maqautocognita.screens.ReadingComprehensionU1Screen;
import com.maqautocognita.screens.ReadingComprehensionU2Screen;
import com.maqautocognita.screens.ReadingComprehensionU3Screen;
import com.maqautocognita.screens.SaySomethingScreen;
import com.maqautocognita.screens.SentenceU1Screen;
import com.maqautocognita.screens.SentenceU2Screen;
import com.maqautocognita.screens.SentenceU3Screen;
import com.maqautocognita.screens.SentenceU4Screen;
import com.maqautocognita.screens.SentenceU5Screen;
import com.maqautocognita.screens.storyMode.AbstractStoryScreen;
import com.maqautocognita.screens.storyMode.CafeStoryModeScreen;
import com.maqautocognita.screens.storyMode.CharacterSetupScreen;
import com.maqautocognita.screens.storyMode.CityStoryModeScreen;
import com.maqautocognita.screens.storyMode.HomeStoryModeScreen;
import com.maqautocognita.screens.storyMode.LibraryStoryModeScreen;
import com.maqautocognita.screens.storyMode.MarketStoryModeScreen;
import com.maqautocognita.screens.storyMode.SchoolStoryModeScreen;
import com.maqautocognita.screens.storyMode.StoreStoryModeScreen;
import com.maqautocognita.screens.storyMode.StoryMissionScreen;
import com.maqautocognita.screens.storyMode.StoryModeScreen;
import com.maqautocognita.screens.storyMode.VillageStoryModeScreen;
import com.maqautocognita.service.DemoService;
import com.maqautocognita.service.DemoService2;
import com.maqautocognita.service.SaySomethingService;
import com.maqautocognita.utils.ScreenUtils;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AutoCognitaGame extends AbstractLearningGame implements IMenuScreenListener {

    private static final boolean IS_GLOBAL_LEARNING = false;
//    private static final boolean IS_GLOBAL_LEARNING = true;

    private static final String DEFAULT_LANGUAGE = IS_GLOBAL_LEARNING ?
            Language.SWAHILI :
            Language.ENGLISH;

    public static Database storyModeDatabase;
    // This is the notification handler
    public NotificationHandler notificationHandler;
    private SaySomethingScreen saySomethingScreen;
    private ConversationScreen conversationScreen;
    private CharacterSetupScreen characterSetupScreen;
    private MathOtherTopicScreen mathOtherTopicScreen;
    private HomeMenuMapScreen menuScreen;
    private MathCountingScreen mathCountingScreen;
    private MathAdditionSubtractionScreen mathAdditionSubtractionScreen;
    private MathLargeNumberScreen mathLargeNumberScreen;
    private MathMultiplicationScreen mathMultiplicationScreen;
    private HomeStoryModeScreen homeStoryModeScreen;
    private StoryMissionScreen storyMissionScreen;
    private VillageStoryModeScreen villageStoryModeScreen;
    private SchoolStoryModeScreen schoolStoryModeScreen;
    private LibraryStoryModeScreen libraryStoryModeScreen;
    private CafeStoryModeScreen cafeStoryModeScreen;
    private MarketStoryModeScreen marketStoryModeScreen;
    private StoreStoryModeScreen storeStoryModeScreen;
    private CityStoryModeScreen cityStoryModeScreen;
    private PhonicU1Screen phonicU1Screen;
    private PhonicU2Screen phonicU2Screen;
    private PhonicU3Screen phonicU3Screen;
    private PhonicU4Screen phonicU4Screen;
    private StoryModeScreen storyModeScreen;
    private AlphabetScreen alphabetScreen;
    private SentenceU1Screen sentenceU1Screen;
    private SentenceU2Screen sentenceU2Screen;
    private SentenceU3Screen sentenceU3Screen;
    private SentenceU4Screen sentenceU4Screen;
    private SentenceU5Screen sentenceU5Screen;
    private CheatSheetForLifeScreen cheatSheetForLifeScreen;
    private ReadingComprehensionU1Screen readingComprehensionU1Screen;
    private ReadingComprehensionU2Screen readingComprehensionU2Screen;
    private ReadingComprehensionU3Screen readingComprehensionU3Screen;
    private LoadingScreen loadingScreen;
    private DemoScreen demoScreen;
    private DemoScreen2 demoScreen2;

    public AutoCognitaGame(boolean isTablet, int miniSDKversion) {
        ScreenUtils.isLandscapeMode = isTablet;
        ScreenUtils.isTablet = isTablet;
        ScreenUtils.miniSDKVersion = miniSDKversion;

    }

    public void showLoadingScreen() {
        if (null == loadingScreen) {
            loadingScreen = new LoadingScreen();
        }

        showScreen(loadingScreen);
    }


    public void start(String lessonJdbcUrl,
                      String dictionaryJdbcUrl,
                      IAudioService audioService,
                      IHandWritingRecognizeService handWritingRecognizeService,
                      ISpeechRecognizeService speechRecognizeService, IDeviceService deviceService,
                      IDeviceCameraService deviceCameraService,
                      Database storyModeDatabase, IOCR ocrService, IShareService shareService, IAnalyticSpotService analyticSpotService) {
        super.start(lessonJdbcUrl, dictionaryJdbcUrl, audioService,
                handWritingRecognizeService,
                speechRecognizeService, deviceService, deviceCameraService, ocrService, shareService, analyticSpotService);

        this.storyModeDatabase = storyModeDatabase;

        SaySomethingService.getInstance(this);
        DemoService.getInstance(this);
        DemoService2.getInstance(this);

        //onConversationSelected();
        onHomeSelected();

        //onSaySomethingSelected();

    }

    @Override
    public void onSentenceUnit1Selected() {
        if (null == sentenceU1Screen) {
            sentenceU1Screen = new SentenceU1Screen(this, this);
        }
        showScreen(sentenceU1Screen);
    }

    @Override
    public void onSentenceUnit2Selected() {
        if (null == sentenceU2Screen) {
            sentenceU2Screen = new SentenceU2Screen(this, this);
        }
        showScreen(sentenceU2Screen);
    }

    @Override
    public void onSentenceUnit3Selected() {
        if (null == sentenceU3Screen) {
            sentenceU3Screen = new SentenceU3Screen(this, this);
        }
        showScreen(sentenceU3Screen);
    }

    @Override
    public void onSentenceUnit4Selected() {
        if (null == sentenceU4Screen) {
            sentenceU4Screen = new SentenceU4Screen(this, this);
        }
        showScreen(sentenceU4Screen);
    }

    @Override
    public void onSentenceUnit5Selected() {
        if (null == sentenceU5Screen) {
            sentenceU5Screen = new SentenceU5Screen(this, this);
        }
        showScreen(sentenceU5Screen);
    }


    @Override
    public void onReadingComprehensionUnit1Selected() {
        if (null == readingComprehensionU1Screen) {
            readingComprehensionU1Screen = new ReadingComprehensionU1Screen(this, this);
        }
        showScreen(readingComprehensionU1Screen);
    }

    @Override
    public void onReadingComprehensionUnit2Selected() {
        if (null == readingComprehensionU2Screen) {
            readingComprehensionU2Screen = new ReadingComprehensionU2Screen(this, this);
        }
        showScreen(readingComprehensionU2Screen);
    }

    @Override
    public void onReadingComprehensionUnit3Selected() {
        if (null == readingComprehensionU3Screen) {
            readingComprehensionU3Screen = new ReadingComprehensionU3Screen(this, this);
        }
        showScreen(readingComprehensionU3Screen);
    }

    @Override
    public void onMathCountSelected() {

        if (null == mathCountingScreen) {
            mathCountingScreen = new MathCountingScreen(this, this);
        }
        showScreen(mathCountingScreen);
    }


    @Override
    public void onAlphabetSelected() {

        if (null == alphabetScreen) {
            alphabetScreen = new AlphabetScreen(this, this);
        }

        showScreen(alphabetScreen);
    }

    @Override
    public void onPhonicU1Selected() {

        if (null == phonicU1Screen) {
            phonicU1Screen = new PhonicU1Screen(this, this);
        }

        showScreen(phonicU1Screen);
    }

    @Override
    public void onPhonicU2Selected() {

        if (null == phonicU2Screen) {
            phonicU2Screen = new PhonicU2Screen(this, this);
        }

        showScreen(phonicU2Screen);
    }

    @Override
    public void onPhonicU3Selected() {

        if (null == phonicU3Screen) {
            phonicU3Screen = new PhonicU3Screen(this, this);
        }

        showScreen(phonicU3Screen);
    }

    @Override
    public void onPhonicU4Selected() {

        if (null == phonicU4Screen) {
            phonicU4Screen = new PhonicU4Screen(this, this);
        }

        showScreen(phonicU4Screen);
    }

    @Override
    public void onMathAdditionSubtractionSelected() {

        if (null == mathAdditionSubtractionScreen) {
            mathAdditionSubtractionScreen = new MathAdditionSubtractionScreen(this, this);
        }

        showScreen(mathAdditionSubtractionScreen);
    }

    @Override
    public void onMathPlaceValueSelected() {

        if (null == mathLargeNumberScreen) {
            mathLargeNumberScreen = new MathLargeNumberScreen(this, this);
        }

        showScreen(mathLargeNumberScreen);
    }

    @Override
    public void onMathMultiplySelected() {

        if (null == mathMultiplicationScreen) {
            mathMultiplicationScreen = new MathMultiplicationScreen(this, this);
        }
        showScreen(mathMultiplicationScreen);
    }

    @Override
    public void onMathOtherTopicSelected() {

        if (null == mathOtherTopicScreen) {
            mathOtherTopicScreen = new MathOtherTopicScreen(this, this);
        }
        showScreen(mathOtherTopicScreen);
    }

    @Override
    public void onStoryModeSelected() {

        if (null == storyModeScreen) {
            storyModeScreen = new StoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(storyModeScreen);
    }

    @Override
    public void onStoryMissionSelected() {

        if (null == storyMissionScreen) {
            storyMissionScreen = new StoryMissionScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(storyMissionScreen);
    }

    @Override
    public void preloadVillageStoryMode() {
        if (null == villageStoryModeScreen) {
            villageStoryModeScreen = new VillageStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }
        villageStoryModeScreen.preload();
    }

    @Override
    public void loadPreloadStoryMode() {
        showScreen(villageStoryModeScreen);
    }

    @Override
    public void onHomeSelected() {

        if (null == menuScreen) {
            menuScreen = new HomeMenuMapScreen(this, this);
        }

        showScreen(menuScreen);
    }

    @Override
    public void onPreviousScreenSelected() {
        if (previousScreen instanceof AbstractStoryScreen) {
            showScreenWithPreloadAndCharacterSetup((AbstractStoryScreen) previousScreen);
        } else {
            showScreen(previousScreen);
        }

    }

    @Override
    public void onCharacterSetupSelected() {
        if (null == characterSetupScreen) {
            characterSetupScreen = new CharacterSetupScreen(this, this);
        }

        showScreen(characterSetupScreen);
    }

    @Override
    public void onHomeStoryModeSelected() {

        if (null == homeStoryModeScreen) {
            homeStoryModeScreen = new HomeStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(homeStoryModeScreen);
    }

    @Override
    public void onVillageStoryModeSelected() {
        preloadVillageStoryMode();
        loadPreloadStoryMode();
    }

    @Override
    public void onLibraryStoryModeSelected() {
        if (null == libraryStoryModeScreen) {
            libraryStoryModeScreen = new LibraryStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(libraryStoryModeScreen);
    }

    @Override
    public void onMarketStoryModeSelected() {
        if (null == marketStoryModeScreen) {
            marketStoryModeScreen = new MarketStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(marketStoryModeScreen);
    }

    @Override
    public void onCafeStoryModeSelected() {
        if (null == cafeStoryModeScreen) {
            cafeStoryModeScreen = new CafeStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(cafeStoryModeScreen);
    }

    @Override
    public void onSchoolStoryModeSelected() {
        if (null == schoolStoryModeScreen) {
            schoolStoryModeScreen = new SchoolStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(schoolStoryModeScreen);
    }

    @Override
    public void onStoreStoryModeSelected() {
        if (null == storeStoryModeScreen) {
            storeStoryModeScreen = new StoreStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(storeStoryModeScreen);
    }

    @Override
    public void onCityStoryModeSelected() {
        if (null == cityStoryModeScreen) {
            cityStoryModeScreen = new CityStoryModeScreen(storyModeDatabase, this, this, analyticSpotService);
        }

        showScreenWithPreloadAndCharacterSetup(cityStoryModeScreen);
    }

    @Override
    public void onEnglishSelected() {
        UserPreferenceUtils.getInstance().setLanguage(Language.ENGLISH);
    }

    @Override
    public void onSwahiliSelected() {
        UserPreferenceUtils.getInstance().setLanguage(Language.SWAHILI);
    }

    @Override
    public void onLifeSkillSelected() {
        if (null == cheatSheetForLifeScreen) {
            cheatSheetForLifeScreen = new CheatSheetForLifeScreen(this, this, deviceCameraService);
        }

        cheatSheetForLifeScreen.setRecognitionMode(false);

        showScreen(cheatSheetForLifeScreen);

    }

    @Override
    public void onSnapSelected() {
        if (null == cheatSheetForLifeScreen) {
            cheatSheetForLifeScreen = new CheatSheetForLifeScreen(this, this, deviceCameraService);

        }

        cheatSheetForLifeScreen.setRecognitionMode(true);

        showScreen(cheatSheetForLifeScreen);

    }
    @Override
    public void onDemoSelected(){
        if (null == demoScreen) {
            demoScreen = new DemoScreen(this, this);
        }
        showScreen(demoScreen);
    }

    @Override
    public void onSaySomethingSelected() {
        if (null == saySomethingScreen) {
            saySomethingScreen = new SaySomethingScreen(this, this);
        }

        showScreen(saySomethingScreen);
    }

    @Override
    public void onConversationSelected() {
        if (null == conversationScreen) {
            conversationScreen = new ConversationScreen(this, this);
        }

        showScreen(conversationScreen);
    }

    private void showScreenWithPreloadAndCharacterSetup(AbstractStoryScreen newScreen) {
        //check if the user has setup the character, if no, go to the character screen
        if (!UserPreferenceUtils.getInstance().isClothingConfigReady()) {
            onCharacterSetupSelected();
            previousScreen = newScreen;
            return;
        }

        showScreenWithPreload(newScreen);
    }

    private void showScreenWithPreload(AbstractStoryScreen newScreen) {
        previousScreen = screen;

        newScreen.preload();

        //make sure no audio is playing
        audioService.stopMusic();
        //currentScreen = screen;
        setScreen(newScreen);
    }

    @Override
    public void create() {
        super.create();
        if (null == screen) {
            if (IS_GLOBAL_LEARNING) {
                //which mean it is the first time to enter the app
                if (StringUtils.isBlank(UserPreferenceUtils.getInstance().getLanguage())) {
                    UserPreferenceUtils.getInstance().setLanguage(DEFAULT_LANGUAGE);
                }
            } else {
                //always set it in english, even their user profile is in other language
                UserPreferenceUtils.getInstance().setLanguage(DEFAULT_LANGUAGE);
            }


        }

    }

    // This is the method we created to set the notifications handler
    public void setNotificationHandler(NotificationHandler handler) {
        this.notificationHandler = handler;
    }

}
