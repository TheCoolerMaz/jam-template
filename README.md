# Jam Template

A LibGDX game jam template with batteries included.

## Features

- **Screen Manager** - Stack-based with transitions (fade, slide)
- **ECS Framework** - Ashley integration ready
- **Shader Pipeline** - Post-processing effects (Bloom, CRT, Dither, Vignette)
- **Asset Manager** - Typed accessors, async loading
- **UI Kit** - Kenney skin, styled widgets
- **Web First** - Optimized for itch.io deployment

## Quick Start

```bash
# Run desktop
./gradlew lwjgl3:run

# Build WebGL
./gradlew html:dist

# Deploy to itch.io
./scripts/deploy.sh
```

## Project Structure

```
├── core/           # Game code (shared)
├── lwjgl3/         # Desktop launcher
├── html/           # WebGL launcher  
├── assets/         # Sprites, audio, UI
├── docs/           # Architecture docs
└── scripts/        # Build helpers
```

## Starting a New Jam

1. Clone this template
2. Update `appName` in `build.gradle`
3. Replace `SplashScreen` with your menu
4. Build your game!

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [AI Agent Instructions](AGENTS.md)

## License

MIT - Use freely for jams and beyond.
