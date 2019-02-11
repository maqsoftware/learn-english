package com.maqautocognita.screens.storyMode;

import com.maqautocognita.AbstractGame;
import com.maqautocognita.adapter.IAnalyticSpotService;
import com.maqautocognita.listener.IMenuScreenListener;
import com.maqautocognita.prototype.databases.Database;
import com.maqautocognita.prototype.storyMode.StoryModeScene;
import com.maqautocognita.utils.ScreenUtils;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public abstract class AbstractAdvanceStoryModeScreen extends AbstractStoryModeScreen {


    public AbstractAdvanceStoryModeScreen(Database storyModeDatabase, AbstractGame game, IMenuScreenListener menuScreenListener, IAnalyticSpotService analyticSpotService) {
        super(storyModeDatabase, game, menuScreenListener, analyticSpotService);
    }

    protected StoryModeScene getCurrentStoryModeScene() {
        return storyModeLogic.getStoryModeScene(getSceneName(), ScreenUtils.getSceneRatio());
    }

    @Override
    public void onTimerComplete(Object threadIndicator) {

    }
}
