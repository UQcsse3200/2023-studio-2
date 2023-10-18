package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.buildables.TurretType;

public class TurretConfigs {

    public TurretConfig levelOne;
    public TurretConfig levelTwo;

    public TurretConfig getTurretConfig(TurretType type) {
        return switch (type) {
            case LEVEL_ONE -> levelOne;
            case LEVEL_TWO -> levelTwo;
        };

    }
}
