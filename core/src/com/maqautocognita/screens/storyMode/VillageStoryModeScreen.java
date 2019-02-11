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
public class VillageStoryModeScreen extends AbstractAdvanceStoryModeScreen {


    public VillageStoryModeScreen(Database storyModeDatabase, AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(storyModeDatabase, game, menuScreenListener, analyticSpotService);
    }

    protected String getSceneName() {
        return StoryModeSceneNameUtils.VILLAGE_SCREEN;
    }

    @Override
    protected String getBackgroundMusicFileName() {
        return StoryModeBackgroundMusicFileNameUtils.VILLAGE_SCREEN;
    }

}
