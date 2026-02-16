package com.jamtemplate.audio;

public interface MusicEngine {
    void init();
    void play(String patternId);
    void stop();
    void setVolume(float volume);
    void setParameter(String name, float value);
    void crossfade(String patternId, float durationSeconds);
    void dispose();
}
