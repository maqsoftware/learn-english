package com.maqautocognita;

import com.maqautocognita.adapter.IAudioService;
import com.maqautocognita.constant.Language;
import com.maqautocognita.screens.StoryLibraryBookScreen;
import com.maqautocognita.screens.StoryLibraryHomeScreen;
import com.maqautocognita.utils.StringUtils;
import com.maqautocognita.utils.UserPreferenceUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class AutoCognitaStoryLibraryGame extends AbstractGame {

    private static final String DEFAULT_LANGUAGE = Language.SWAHILI;

    private StoryLibraryBookScreen storyLibraryBookScreen;

    private StoryLibraryHomeScreen storyLibraryHomeScreen;

    public AutoCognitaStoryLibraryGame(String jdbcUrl, IAudioService audioService) {
        super();
        start(jdbcUrl, null, audioService);

    }

    @Override
    public void create() {
        super.create();
        if (null == screen) {
            //which mean it is the first time to enter the app
            if (StringUtils.isBlank(UserPreferenceUtils.getInstance().getLanguage())) {
                UserPreferenceUtils.getInstance().setLanguage(DEFAULT_LANGUAGE);
            }

            showStoryLibraryHomeScreen();

            //onLifeSkillSelected();
            //onChatSelected();
            //onHomeSelected();
        }
    }

    public void showStoryLibraryHomeScreen() {
        if (null == storyLibraryHomeScreen) {
            storyLibraryHomeScreen = new StoryLibraryHomeScreen(this);
        }
        showScreen(storyLibraryHomeScreen);
    }

    public void showStoryLibraryBookScreen(int storyBookNumber) {
        if (null == storyLibraryBookScreen) {
            storyLibraryBookScreen = new StoryLibraryBookScreen(this, storyBookNumber);
        } else {
            storyLibraryBookScreen.setStoryBookNumber(storyBookNumber);
        }
        showScreen(storyLibraryBookScreen);
    }

}
