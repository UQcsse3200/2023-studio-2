package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.buildables.TurretType;

public class TurretConfigs {

    public TurretConfig levelOne = new TurretConfig();
    public TurretConfig levelTwo = new TurretConfig();

    public TurretConfig GetTurretConfig(TurretType type) {
        return switch (type) {
            case levelOne -> levelOne;
            case levelTwo -> levelTwo;
        };

    }
}
