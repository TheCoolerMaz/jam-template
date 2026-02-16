package com.jamtemplate.gwt;

import com.jamtemplate.audio.MusicEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class GwtMusicEngine implements MusicEngine {
    @Override
    public void init() {
        // Strudel is expected to be loaded via the script tag in index.html
        // No specific initialization needed here other than ensuring the JS is present.
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            // Optional: You could call a JavaScript function here to signal Strudel is ready
            // For now, assume it's ready once the script is loaded.
        }
    }

    @Override
    public void play(String patternCode) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Strudel.evaluate(patternCode);
        }
    }

    @Override
    public void stop() {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Strudel.hush();
        }
    }

    @Override
    public void setParameter(String name, float value) {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Strudel.setParam(name, value);
        }
    }

    @Override
    public void crossfade(String patternCode, float durationSeconds) {
        // For simplicity in Phase 1, crossfade just plays the new pattern immediately.
        // A proper crossfade would involve more complex Strudel scripting or timing managed here.
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Strudel.evaluate(patternCode);
        }
    }

    @Override
    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            Strudel.hush();
        }
    }
}
