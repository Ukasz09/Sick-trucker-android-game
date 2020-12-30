package com.github.ukasz09.activities;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.ukasz09.GameApp;
import com.github.ukasz09.GameWonPageBridge;
import com.github.ukasz09.fragments.MenuStart;

public class GameLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//        config.useImmersiveMode = true;
        MenuStart.stopBackgroundSound();
        initialize(new GameApp(new GameWonPageBridge(this)), config);
    }


}
