package com.csse3200.game.entities.configs;

import com.csse3200.game.components.PotionType;

public class PotionConfigs {

    public PotionConfig DEATH_POTION = new PotionConfig();
    public PotionConfig HEALTH_POTION = new PotionConfig();
    public PotionConfig SPEED_POTION = new PotionConfig();
    public PotionConfig INVINCIBILITY_POTION = new PotionConfig();
    public PotionConfig EXTRA_LIFE = new PotionConfig();
    public PotionConfig DOUBLE_CROSS = new PotionConfig();
    public PotionConfig DOUBLE_DAMAGE = new PotionConfig();
    public PotionConfig SNAP = new PotionConfig();


    public PotionConfig GetPotionConfig(PotionType type) {
        return switch (type) {
            case DEATH_POTION -> DEATH_POTION;
            case HEALTH_POTION  -> HEALTH_POTION;
            case SPEED_POTION -> SPEED_POTION;
            case INVINCIBILITY_POTION  -> INVINCIBILITY_POTION;
            case EXTRA_LIFE  -> EXTRA_LIFE;
            case DOUBLE_CROSS  -> DOUBLE_CROSS;
            case DOUBLE_DAMAGE  -> DOUBLE_DAMAGE;
            case SNAP  -> SNAP;
        };
    }

}
