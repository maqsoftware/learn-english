package com.maqautocognita.africanstorybookslibrary;

import android.os.Bundle;

import com.maqautocognita.AutoCognitaStoryLibraryGame;
import com.maqautocognita.africanstorybookslibrary.service.AudioService;
import com.maqautocognita.africanstorybookslibrary.utils.FileUtils;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 *
 */
public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Class.forName("org.sqldroid.SQLDroidDriver");
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }

        final String resultPath = FileUtils.copyFileFromAsset(getContext(), "autocognita_storybook.db", false);

        final AutoCognitaStoryLibraryGame game = new AutoCognitaStoryLibraryGame("jdbc:sqldroid:" + resultPath,
                new AudioService(getContext(), null));

        initialize(game, new AndroidApplicationConfiguration());
    }
}
