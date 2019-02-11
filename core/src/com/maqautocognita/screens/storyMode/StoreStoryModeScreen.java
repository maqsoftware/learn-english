package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.utils.StoryModeBackgroundMusicFileNameUtils;
import com.maqautocognita.utils.StoryModeSceneNameUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class StoreStoryModeScreen extends AbstractAdvanceStoryModeScreen {


    public StoreStoryModeScreen(Database storyModeDatabase, AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(storyModeDatabase, game, menuScreenListener, analyticSpotService);
    }

    protected String getSceneName() {
        return StoryModeSceneNameUtils.STORE_SCREEN;
    }

    @Override
    protected String getBackgroundMusicFileName() {
        return StoryModeBackgroundMusicFileNameUtils.STORE_SCREEN;
    }
}
