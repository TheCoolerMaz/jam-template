package com.jamtemplate.graphics;

import com.jamtemplate.util.Prefs;

public class GwtShaderPipeline implements IShaderPipeline {
    @Override
    public void rebuildFromPrefs(Prefs prefs) {
        // No-op for GWT
    }

    @Override
    public void clear() {
        // No-op for GWT
    }

    @Override
    public void begin() {
        // No-op for GWT
    }

    @Override
    public void end() {
        // No-op for GWT
    }

    @Override
    public void render() {
        // No-op for GWT
    }

    @Override
    public void resize(int width, int height) {
        // No-op for GWT
    }

    @Override
    public void dispose() {
        // No-op for GWT
    }
}
