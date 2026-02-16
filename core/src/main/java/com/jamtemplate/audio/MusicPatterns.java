package com.jamtemplate.audio;

public class MusicPatterns {
    // Using built-in synths (sawtooth, sine, square, triangle) that don't need sample loading
    
    public static final String MENU_MUSIC =
        "stack(\n" +
        "  note(\"<[d3,fs3,a3] [g2,b2,d3] [e3,g3,b3] [a2,cs3,e3]>\")\n" +
        "    .sound(\"sine\").gain(0.3).slow(4),\n" +
        "  note(\"d4 fs4 a4 ~ a4 fs4 ~ ~\")\n" +
        "    .sound(\"triangle\").gain(0.2).slow(2)\n" +
        ").cpm(45)";

    public static final String COMBAT_MUSIC =
        "stack(\n" +
        "  note(\"c2 c2 c2 c2\").sound(\"sawtooth\").lpf(200).gain(0.4).fast(2),\n" +
        "  note(\"<e3 e3 g3 a3>\").sound(\"square\").lpf(800).gain(0.2)\n" +
        ").cpm(70)";

    public static final String VICTORY_MUSIC =
        "note(\"c4 e4 g4 c5\").sound(\"sine\").gain(0.3).slow(2)";
}
