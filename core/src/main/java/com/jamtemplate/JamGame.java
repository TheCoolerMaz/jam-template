package com.jamtemplate;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jamtemplate.assets.Assets;
import com.jamtemplate.graphics.ShaderPipeline;
import com.jamtemplate.screens.ScreenManager;
import com.jamtemplate.screens.SplashScreen;
import com.jamtemplate.util.Prefs;

/**
 * Main game entry point.
 * 
 * Initializes core systems and manages the game lifecycle.
 */
public class JamGame extends Game {

    public static JamGame INSTANCE;
    
    public Assets assets;
    public ScreenManager screens;
    public ShaderPipeline shaders;
    public Prefs prefs;

    @Override
    public void create() {
        INSTANCE = this;
        
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        
        // Initialize core systems
        prefs = new Prefs();
        assets = new Assets();
        assets.loadAll();
        assets.finishLoading();
        
        shaders = new ShaderPipeline();
        shaders.rebuildFromPrefs(prefs);
        
        screens = new ScreenManager(this);
        screens.push(new SplashScreen());
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render through shader pipeline
        if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
            shaders.begin();
        }
        
        // Update and render screens
        screens.update(Gdx.graphics.getDeltaTime());
        super.render();
        
        if (Gdx.app.getType() != Application.ApplicationType.HeadlessDesktop) {
            shaders.end();
            shaders.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        shaders.resize(width, height);
        screens.resize(width, height);
    }

    @Override
    public void dispose() {
        screens.dispose();
        shaders.dispose();
        assets.dispose();
    }
}
