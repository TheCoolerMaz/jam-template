package com.jamtemplate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.jamtemplate.assets.Assets;
import com.jamtemplate.audio.MusicEngine;
import com.jamtemplate.audio.MusicPatterns;
import com.jamtemplate.graphics.IShaderPipeline;
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
    public IShaderPipeline shaders;
    public Prefs prefs;
    private MusicEngine musicEngine;

    public JamGame() {
        this(null, null); // Pass null for shaders, will be handled by platform specific launcher
    }

    public JamGame(MusicEngine musicEngine) {
        this(musicEngine, null); // Pass null for shaders
    }

    public JamGame(MusicEngine musicEngine, IShaderPipeline shaders) {
        this.musicEngine = musicEngine;
        this.shaders = shaders;
    }

    @Override
    public void create() {
        INSTANCE = this;

        Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG); // Fix: use Gdx.app.LOG_DEBUG

        // Initialize core systems
        prefs = new Prefs();
        assets = new Assets();
        assets.loadAll();
        assets.finishLoading();

        // Shader pipeline is passed in constructor now
        if (shaders != null) { // Only rebuild if shaders are provided
            shaders.rebuildFromPrefs(prefs);
        }

        screens = new ScreenManager(this);
        screens.push(new SplashScreen());

        if (musicEngine != null) {
            musicEngine.init();
            musicEngine.setVolume(prefs.getMasterVolume() * prefs.getMusicVolume());
        }
    }

    @Override
    public void render() {
        // Guard against early render calls
        if (screens == null) return; // shaders can be null if not provided

        // Clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render through shader pipeline if available
        if (shaders != null) {
            shaders.begin();
        }

        // Update and render screens
        screens.update(Gdx.graphics.getDeltaTime());
        super.render();

        if (shaders != null) {
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

    public MusicEngine getMusicEngine() {
        return musicEngine;
    }
}
