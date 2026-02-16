package com.jamtemplate.lwjgl3.graphics;

import com.jamtemplate.graphics.IShaderPipeline;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.VignettingEffect;
import com.jamtemplate.graphics.effects.CrtEffect;
import com.jamtemplate.graphics.effects.DitherEffect;
import com.jamtemplate.graphics.effects.Effect;
import com.jamtemplate.util.Prefs;

/**
 * Post-processing shader pipeline.
 * 
 * Manages a chain of shader effects applied to the final render.
 * Uses gdx-vfx for FBO ping-pong rendering.
 * 
 * Usage:
 *   shaders.begin();
 *   // ... render your game ...
 *   shaders.end();
 *   shaders.render();
 */
public class ShaderPipeline implements IShaderPipeline {

    private final VfxManager vfx;
    private final Array<Effect> customEffects = new Array<>();

    public ShaderPipeline() {
        vfx = new VfxManager(Pixmap.Format.RGBA8888);
    }

    /** Rebuild effects based on current preferences. */
    @Override
    public void rebuildFromPrefs(Prefs prefs) {
        // clear();
        vfx.removeAllEffects();
        for (Effect effect : customEffects) {
            effect.dispose();
        }
        customEffects.clear();
        
        // Bloom
        if (prefs.isBloomEnabled()) {
            BloomEffect bloom = new BloomEffect();
            bloom.setBaseIntensity(1.0f);
            bloom.setBloomIntensity(prefs.getBloomIntensity());
            vfx.addEffect(bloom);
        }
        
        // Vignette
        if (prefs.isVignetteEnabled()) {
            VignettingEffect vignette = new VignettingEffect(false);
            vignette.setIntensity(prefs.getVignetteIntensity());
            vfx.addEffect(vignette);
        }
        
        // Dither (before CRT for best results)
        if (prefs.isDitherEnabled()) {
            DitherEffect dither = new DitherEffect();
            dither.setStrength(prefs.getDitherStrength());
            vfx.addEffect(dither);
            customEffects.add(dither);
        }
        
        // CRT
        if (prefs.isCrtEnabled()) {
            CrtEffect crt = new CrtEffect();
            crt.setIntensity(prefs.getCrtIntensity());
            vfx.addEffect(crt);
            customEffects.add(crt);
        }
    }

    /** Clear all effects. */
    @Override
    public void clear() {
        vfx.removeAllEffects();
        for (Effect effect : customEffects) {
            effect.dispose();
        }
        customEffects.clear();
    }

    /** Begin capturing render output. */
    @Override
    public void begin() {
        vfx.cleanUpBuffers();
        vfx.beginInputCapture();
    }

    /** End capturing render output. */
    @Override
    public void end() {
        vfx.endInputCapture();
    }

    /** Apply effects and render to screen. */
    @Override
    public void render() {
        vfx.applyEffects();
        vfx.renderToScreen();
    }

    @Override
    public void resize(int width, int height) {
        vfx.resize(width, height);
    }

    @Override
    public void dispose() {
        clear();
        vfx.dispose();
    }
}
