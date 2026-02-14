package com.jamtemplate.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.jamtemplate.JamGame;

/**
 * WebGL/GWT launcher.
 * 
 * Build with: ./gradlew html:dist
 * Output in: html/build/dist/
 */
public class GwtLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
        config.padVertical = 0;
        config.padHorizontal = 0;
        config.fullscreenOrientation = GwtApplicationConfiguration.OrientationLockType.LANDSCAPE;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new JamGame();
    }
}
