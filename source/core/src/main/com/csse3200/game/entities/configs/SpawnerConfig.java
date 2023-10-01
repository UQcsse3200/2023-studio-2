package com.csse3200.game.entities.configs;

public class SpawnerConfig extends BaseEntityConfig {
    public int[] wave1;
    public int[] wave2;
    public int[] wave3;

    public int[] getWave1() {
        return wave1;
    }

    public int[] getWave2() {
        return wave2;
    }

    public int[] getWave3() {
        return wave3;
    }

    SoundsConfig sounds;
}
