package org.anomek.omegasim.scenarios.partysynergy;


public class Timing {
    // timeline
    public static final long PARTY_SYNERGY_CAST_TIME = 2700;
    public static final long PARTY_SYNERGY_EXECUTION = 6000;  // mf disappear, eye shows, markers appear, debuff appear
    public static final long ATTACK_MF_APPEAR = 8500;
    public static final long ATTACK_AOE_SHOW = 12300;              // markers disappear
    public static final long ATTACK_AOE_DAMAGE = 13300;            // fakes disappear, 4 m force appear
    public static final long ATTACK_MF_DISAPPEAR = 16900;
    public static final long EYE_AOE_DAMAGE = 19800;    // splash damage, final m force and f appear
    public static final long STACK_MARKER_APPEARS = 21000;
    public static final long EYE_DISAPPEAR = 25700;
    public static final long KNOCKBACK_RESOLVES = 27600;
    public static final long M_FORCE_AOE_APPEAR = 30400;
    public static final long M_FORCE_AOE_DAMAGE = 31300; // stack damage
    public static final long END_OF_SIM = 35000; // stack damage


    public static final long KNOCKBACK_DURATION = 500;
    public static final long AOE_FADE_IN = 330;
    public static final long MARKERS_FADE_IN = 330;
    public static final long EYE_AOE_LINGER = 2200;
    public static final long MF_FADE_OUT = 1000;
    public static final long MF_FADE_IN = 600;
}
