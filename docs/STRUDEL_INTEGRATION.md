# Strudel Integration Plan

Dynamic music composition for jam-template using Strudel patterns.

## Goal

Procedural/adaptive music that:
- Reacts to game state (tension, location, events)
- Works on both **web** (GWT) and **desktop** (LWJGL)
- Is code-first (patterns defined in code, not audio files)
- Stays lightweight for jam scope

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     Game Code                           │
│                                                         │
│   MusicEngine.setIntensity(0.8)                        │
│   MusicEngine.setPattern("combat")                     │
│   MusicEngine.setParameter("tempo", 140)               │
└─────────────────────┬───────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────┐
│                  MusicEngine (Interface)                │
│                                                         │
│   void play(String patternName)                        │
│   void stop()                                          │
│   void setParameter(String key, float value)           │
│   void crossfade(String patternName, float duration)   │
└─────────────────────┬───────────────────────────────────┘
                      │
          ┌───────────┴───────────┐
          ▼                       ▼
┌─────────────────┐     ┌─────────────────┐
│  WebMusicEngine │     │DesktopMusicEngine│
│  (GWT/JsInterop)│     │   (GraalJS)      │
│                 │     │                  │
│  Calls Strudel  │     │ Embeds Strudel   │
│  via JS bridge  │     │ via GraalVM JS   │
└─────────────────┘     └──────────────────┘
```

## Implementation Phases

### Phase 1: Interface + Web Implementation

**Files:**
```
core/src/main/java/com/jamtemplate/audio/
├── MusicEngine.java          # Interface
├── MusicPattern.java         # Pattern definition
└── MusicParameter.java       # Reactive parameters

html/src/main/java/com/jamtemplate/gwt/
└── GwtMusicEngine.java       # JsInterop to Strudel
```

**MusicEngine.java:**
```java
public interface MusicEngine {
    void init();
    void play(String patternId);
    void stop();
    void setParameter(String name, float value);
    void crossfade(String patternId, float durationSeconds);
    void dispose();
}
```

**Web integration (JsInterop):**
```java
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Strudel")
public class Strudel {
    public static native void evaluate(String code);
    public static native void hush();
    public static native void setParam(String name, float value);
}
```

**HTML template addition:**
```html
<script src="https://unpkg.com/@strudel/embed"></script>
```

### Phase 2: Desktop Implementation

**Option A: GraalJS (Recommended)**
- GraalJS is a pure-Java JavaScript engine
- ~15MB added to jar
- Runs Strudel patterns natively
- Audio output via libGDX AudioDevice

**build.gradle addition:**
```gradle
implementation 'org.graalvm.js:js:23.0.0'
implementation 'org.graalvm.js:js-scriptengine:23.0.0'
```

**DesktopMusicEngine.java:**
```java
public class DesktopMusicEngine implements MusicEngine {
    private Context jsContext;
    private Value strudel;
    
    public void init() {
        jsContext = Context.newBuilder("js")
            .allowAllAccess(true)
            .build();
        // Load Strudel bundle
        jsContext.eval("js", strudelBundleCode);
        strudel = jsContext.getBindings("js").getMember("Strudel");
    }
    
    public void play(String patternId) {
        String pattern = patterns.get(patternId);
        strudel.invokeMember("evaluate", pattern);
    }
}
```

**Audio bridge:**
- Strudel outputs to Web Audio API
- Need to bridge to libGDX AudioDevice
- Options:
  1. Custom AudioWorklet → Java callback
  2. Render to buffer, play via libGDX
  3. Port just the pattern scheduler, use libGDX synths

### Phase 3: Pattern Library

**patterns/music.js:**
```javascript
const patterns = {
    menu: `
        stack(
            note("<[d3,fs3,a3] [g2,b2,d3] [e3,g3,b3] [a2,cs3,e3]>")
                .sound("gm_pad_choir").slow(4),
            note("d4 fs4 a4 ~ a4 fs4 ~ ~")
                .sound("gm_music_box").slow(2)
        ).cpm(45)
    `,
    
    combat: `
        stack(
            s("bd sd bd sd").fast(2),
            note("<e3 e3 g3 a3>").sound("sawtooth").lpf(800)
        ).cpm(70)
    `,
    
    victory: `
        note("c4 e4 g4 c5").sound("gm_trumpet").slow(2)
    `
};
```

**Java-side pattern switching:**
```java
// In game code
if (enemiesNearby) {
    musicEngine.crossfade("combat", 1.0f);
    musicEngine.setParameter("intensity", enemyCount / 10f);
} else {
    musicEngine.crossfade("explore", 2.0f);
}
```

### Phase 4: Reactive Parameters

Expose game state to patterns:

```javascript
// Pattern that reacts to "intensity" parameter
note("c3 e3 g3 c4".fast(getParam("intensity") * 2))
    .sound("sawtooth")
    .lpf(400 + getParam("intensity") * 1000)
```

```java
// Game code
musicEngine.setParameter("intensity", player.health / 100f);
musicEngine.setParameter("danger", nearestEnemy.distance);
```

## Complexity Estimate

| Phase | Effort | Jam-viable? |
|-------|--------|-------------|
| Phase 1 (Web only) | 1-2 hours | ✅ Yes |
| Phase 2 (Desktop) | 4-6 hours | ⚠️ Maybe |
| Phase 3 (Patterns) | 2-3 hours | ✅ Yes |
| Phase 4 (Reactive) | 2-3 hours | ✅ Yes |

**For a jam:** Do Phase 1 + 3, skip desktop (web-first anyway).

**For jam-template:** Full implementation makes sense as reusable infra.

## Alternative: Minimal Viable Integration

If full Strudel is too heavy, extract just the pattern concepts:

```java
// Pure Java mini-pattern system
Pattern combat = Pattern.stack(
    Pattern.note("e3", "e3", "g3", "a3").sound(Synth.SAW).slow(4),
    Pattern.sample("bd", "sd", "bd", "sd").fast(2)
);

musicEngine.play(combat);
```

This loses Strudel's JS ecosystem but keeps the compositional model.

## Next Steps

1. [ ] Prototype web integration (1 hour)
2. [ ] Test with existing Strudel patterns
3. [ ] Decide on desktop approach (GraalJS vs pure Java port)
4. [ ] Build pattern library for common game states
5. [ ] Add to jam-template as optional module

## Resources

- Strudel source: https://codeberg.org/uzu/strudel
- @strudel/embed: https://www.npmjs.com/package/@strudel/embed
- GraalJS: https://www.graalvm.org/javascript/
- JsInterop: https://www.gwtproject.org/doc/latest/DevGuideCodingBasicsJsInterop.html
