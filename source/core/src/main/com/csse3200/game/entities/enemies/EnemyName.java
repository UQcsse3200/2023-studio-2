package com.csse3200.game.entities.enemies;

import com.csse3200.game.entities.configs.EnemyConfig;

public enum EnemyName {
    redGhost,
    chain,
    roboMan,
    necromancer,
    Knight,
    rangeBossPTE,
    Guardian;

    public static EnemyName getEnemyName(String name) {
        return switch (name) {
            case "redGhost" -> redGhost;
            case "chain" -> chain;
            case "necromancer" -> necromancer;
            case "roboMan" -> roboMan;
            case "Knight" -> Knight;
            case "rangeBossPTE" -> rangeBossPTE;
            case "Guardian" -> Guardian;
            default -> null;
        };
    }
}


