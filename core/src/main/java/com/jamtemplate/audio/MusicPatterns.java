package com.jamtemplate.audio;

public class MusicPatterns {
    public static final String MENU_MUSIC =
        "        stack(\n" +
        "            note(\"<[d3,fs3,a3] [g2,b2,d3] [e3,g3,b3] [a2,cs3,e3]>\")\n" +
        "                .sound(\"gm_pad_choir\").slow(4),\n" +
        "            note(\"d4 fs4 a4 ~ a4 fs4 ~ ~\")\n" +
        "                .sound(\"gm_music_box\").slow(2)\n" +
        "        ).cpm(45)\n" +
        "        ";

    public static final String COMBAT_MUSIC =
        "        stack(\n" +
        "            s(\"bd sd bd sd\").fast(2),\n" +
        "            note(\"<e3 e3 g3 a3>\").sound(\"sawtooth\").lpf(800)\n" +
        "        ).cpm(70)\n" +
        "        ";

    public static final String VICTORY_MUSIC =
        "        note(\"c4 e4 g4 c5\").sound(\"gm_trumpet\").slow(2)\n" +
        "        ";
}
