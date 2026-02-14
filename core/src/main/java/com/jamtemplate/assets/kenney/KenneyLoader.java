package com.jamtemplate.assets.kenney;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Loader and cache for Kenney asset packs.
 * 
 * Usage:
 *   KenneyLoader loader = new KenneyLoader();
 *   Texture barrel = loader.get(IsoDungeon.BARREL_E);
 *   TextureRegion region = loader.getRegion(IsoDungeon.BARREL_E);
 *   
 *   // Don't forget to dispose when done
 *   loader.dispose();
 */
public class KenneyLoader implements Disposable {
    
    private final ObjectMap<String, Texture> cache = new ObjectMap<>();
    
    /**
     * Get a texture for any Kenney asset enum.
     * Textures are cached and reused.
     */
    public Texture get(KenneyAsset asset) {
        String path = asset.getFullPath();
        Texture tex = cache.get(path);
        if (tex == null) {
            tex = new Texture(Gdx.files.internal(path));
            cache.put(path, tex);
        }
        return tex;
    }
    
    /**
     * Get a TextureRegion for any Kenney asset enum.
     * Useful for SpriteBatch drawing.
     */
    public TextureRegion getRegion(KenneyAsset asset) {
        return new TextureRegion(get(asset));
    }
    
    /**
     * Preload multiple assets (e.g., all dungeon tiles).
     */
    public void preload(KenneyAsset... assets) {
        for (KenneyAsset asset : assets) {
            get(asset);
        }
    }
    
    /**
     * Preload all values from an enum class.
     */
    public <E extends Enum<E> & KenneyAsset> void preloadAll(Class<E> enumClass) {
        for (E asset : enumClass.getEnumConstants()) {
            get(asset);
        }
    }
    
    /**
     * Clear a specific texture from cache.
     */
    public void unload(KenneyAsset asset) {
        String path = asset.getFullPath();
        Texture tex = cache.remove(path);
        if (tex != null) {
            tex.dispose();
        }
    }
    
    /**
     * Check if an asset is currently cached.
     */
    public boolean isLoaded(KenneyAsset asset) {
        return cache.containsKey(asset.getFullPath());
    }
    
    /**
     * Get number of cached textures.
     */
    public int getCacheSize() {
        return cache.size;
    }
    
    @Override
    public void dispose() {
        for (Texture tex : cache.values()) {
            tex.dispose();
        }
        cache.clear();
    }
}
