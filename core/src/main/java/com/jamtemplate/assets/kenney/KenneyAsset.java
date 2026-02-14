package com.jamtemplate.assets.kenney;

/**
 * Common interface for all Kenney asset enums.
 * Allows generic loading via KenneyLoader.
 */
public interface KenneyAsset {
    
    /** Path relative to the pack folder (e.g., "Isometric/barrel_E.png") */
    String getPath();
    
    /** Full path from assets root (e.g., "kenney/isometric-miniature-dungeon/Isometric/barrel_E.png") */
    String getFullPath();
}
