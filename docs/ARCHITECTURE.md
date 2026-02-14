# Architecture

## Overview

This template follows a layered architecture optimized for rapid game jam development.

```
┌─────────────────────────────────────┐
│           JamGame (main)            │
├─────────────────────────────────────┤
│  ScreenManager  │  ShaderPipeline   │
├─────────────────────────────────────┤
│  GameScreen     │  Effects          │
│  Transitions    │  (CRT, Dither...) │
├─────────────────────────────────────┤
│  ECS World      │  Assets           │
│  Components     │  UI Kit           │
│  Systems        │  Prefs            │
└─────────────────────────────────────┘
```

## Core Systems

### Screen Manager

Stack-based screen management with transition support.

```java
// Push new screen
screens.push(new GameplayScreen());

// Push with transition
screens.push(new MenuScreen(), new FadeTransition(0.5f));

// Pop to previous
screens.pop();

// Replace current
screens.swap(new GameOverScreen());
```

### Shader Pipeline

Post-processing chain using gdx-vfx.

Effects are toggled via `Prefs` and rebuilt on change:

```java
// In your settings screen
prefs.setCrtEnabled(true);
JamGame.INSTANCE.shaders.rebuildFromPrefs(prefs);
```

Available effects:
- **Bloom** - Glow around bright areas
- **Vignette** - Darkened edges
- **CRT** - Scanlines, curvature, chromatic aberration
- **Dither** - Ordered Bayer dithering

### ECS (Ashley)

Standard Ashley ECS pattern:

```java
// Create entity
Entity player = engine.createEntity();
player.add(new PositionComponent(100, 100));
player.add(new SpriteComponent(texture));
engine.addEntity(player);

// Process in system
public class MovementSystem extends IteratingSystem {
    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }
    
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // Update position
    }
}
```

### Asset Loading

Two modes: async (loading screen) or direct (jam shortcuts).

```java
// Async (proper)
assets.loadAll();
assets.finishLoading();
Texture tex = assets.get("sprites/player.png", Texture.class);

// Direct (quick and dirty for jams)
Texture tex = assets.loadTexture("sprites/player.png");
```

## Screen Lifecycle

```
show()    → Called when screen becomes active
render()  → Called every frame
resize()  → Called on window resize
hide()    → Called when screen becomes inactive
dispose() → Called when screen is removed from stack
```

## WebGL Considerations

1. **GLES 1.00 shaders only** - No array constructors, limited features
2. **Asset loading is async** - Plan for loading screens
3. **Keep assets small** - Compress, use atlases
4. **Test in browser** - Some behaviors differ from desktop
