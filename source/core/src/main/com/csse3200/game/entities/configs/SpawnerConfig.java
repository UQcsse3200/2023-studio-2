package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.enemies.EnemyName;

import java.util.LinkedHashMap;

public class SpawnerConfig extends BaseEntityConfig {
    public LinkedHashMap<String, Integer> wave1 = new LinkedHashMap<>();
    public LinkedHashMap<String, Integer> wave2 = new LinkedHashMap<>();
    public LinkedHashMap<String, Integer> wave3 = new LinkedHashMap<>();
    SoundsConfig sounds;

    public LinkedHashMap<String, Integer> getWave1() {
        return wave1;
    }

    public LinkedHashMap<String, Integer> getWave2() {
        return wave2;
    }

    public LinkedHashMap<String, Integer> getWave3() {
        return wave3;
    }
}
