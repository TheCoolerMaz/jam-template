# AGENTS.md - AI Coding Agent Instructions

This is a LibGDX game jam template. Use it to rapidly prototype games.

## Project Structure

```
jam-template/
├── core/                    # Main game code (platform-agnostic)
├── lwjgl3/                  # Desktop launcher
├── html/                    # WebGL launcher
├── assets/                  # Game assets (shared)
└── scripts/                 # Build/deploy scripts
```

## Key Packages

| Package | Purpose |
|---------|---------|
| `com.jamtemplate` | Main game entry (`JamGame.java`) |
| `com.jamtemplate.screens` | Screen management + transitions |
| `com.jamtemplate.ecs` | Entity-Component-System (Ashley-based) |
| `com.jamtemplate.graphics` | Shader pipeline + effects |
| `com.jamtemplate.assets` | Asset loading |
| `com.jamtemplate.ui` | UI kit widgets |
| `com.jamtemplate.util` | Preferences, helpers |

## Common Tasks

### Add a new screen
1. Create class extending `GameScreen`
2. Implement `render(float delta)`
3. Push via `JamGame.INSTANCE.screens.push(new YourScreen())`

### Add a shader effect
1. Create class implementing `Effect` in `graphics/effects/`
2. Follow `CrtEffect.java` pattern (GLES 1.00 compatible shaders)
3. Add toggle to `Prefs.java`
4. Wire in `ShaderPipeline.rebuildFromPrefs()`

### Add a component (ECS)
1. Create class in `ecs/components/` extending Ashley's `Component`
2. Use in systems via `ComponentMapper`

### Add a system (ECS)
1. Create class in `ecs/systems/` extending `EntitySystem` or `IteratingSystem`
2. Add to engine in your game screen

## Build Commands

```bash
./gradlew lwjgl3:run       # Run desktop
./gradlew html:dist        # Build WebGL
./scripts/deploy.sh        # Upload to itch.io (requires butler)
```

## Constraints

- **WebGL first**: All shaders must be GLES 1.00 compatible
- **No array constructors in GLSL**: Use `mat4` or procedural values
- **Keep assets small**: Itch.io voters prefer fast loads

## Testing

Run desktop build frequently. WebGL build for final testing before deploy.
