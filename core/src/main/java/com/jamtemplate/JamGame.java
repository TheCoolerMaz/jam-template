package com.jamtemplate;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.jamtemplate.assets.Assets;
import com.jamtemplate.audio.MusicEngine;
import com.jamtemplate.audio.MusicPatterns;
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
    private MusicEngine musicEngine;

    public JamGame(MusicEngine musicEngine) {
        this.musicEngine = musicEngine;
    }

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

        musicEngine.init();
    }

    @Override
    public void render() {
        // Guard against early render calls
        if (screens == null || shaders == null) return;
        
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

        // --- Music Test Hook ---
        if (musicEngine != null) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                musicEngine.play(MusicPatterns.MENU_MUSIC);
                Gdx.app.debug("Music", "Playing Menu Music");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                musicEngine.play(MusicPatterns.COMBAT_MUSIC);
                Gdx.app.debug("Music", "Playing Combat Music");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                musicEngine.play(MusicPatterns.VICTORY_MUSIC);
                Gdx.app.debug("Music", "Playing Victory Music");
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
                musicEngine.stop();
                Gdx.app.debug("Music", "Stopping Music");
            }
        }
        // --- End Music Test Hook ---
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (shaders != null) shaders.resize(width, height);
        if (screens != null) screens.resize(width, height);
    }

    @Override
    public void dispose() {
        if (screens != null) screens.dispose();
        if (shaders != null) shaders.dispose();
        if (assets != null) assets.dispose();
        if (musicEngine != null) musicEngine.dispose();
    }
}
