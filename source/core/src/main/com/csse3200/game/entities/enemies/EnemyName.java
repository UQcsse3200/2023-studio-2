package com.csse3200.game.entities.enemies;

public enum EnemyName {
    redGhost,
    chain,
    roboMan,
    necromancer,
    Knight,
    Mage,
    Guardian;

    public static EnemyName getEnemyName(String name) {
        return switch (name) {
            case "redGhost" -> redGhost;
            case "chain" -> chain;
            case "necromancer" -> necromancer;
            case "roboMan" -> roboMan;
            case "Knight" -> Knight;
            case "Mage" -> Mage;
            case "Guardian" -> Guardian;
            default -> null;
        };
    }
}


