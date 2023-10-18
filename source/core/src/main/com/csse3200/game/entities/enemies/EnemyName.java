package com.csse3200.game.entities.enemies;

public enum EnemyName {
    redGhost,
    chain,
    roboMan,
    necromancer,
    Knight,
    rangeBossPTE;

    public static EnemyName getEnemyName(String name) {
        return switch (name) {
            case "redGhost" -> redGhost;
            case "chain" -> chain;
            case "necromancer" -> necromancer;
            case "roboMan" -> roboMan;
            case "Knight" -> Knight;
            case "rangeBossPTE" -> rangeBossPTE;
            default -> null;
        };
    }
}


