package com.jamtemplate.graphics;

import com.badlogic.gdx.utils.Disposable;
import com.jamtemplate.util.Prefs;

public interface IShaderPipeline extends Disposable {
    void rebuildFromPrefs(Prefs prefs);
    void clear();
    void begin();
    void end();
    void render();
    void resize(int width, int height);
}
