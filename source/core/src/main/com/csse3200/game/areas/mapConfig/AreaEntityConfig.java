package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.entities.configs.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains data on all the entities for a given game area
 */
public class AreaEntityConfig {
    public List<EnemyConfig> asteroids = new ArrayList<>();
    public List<BaseEntityConfig> baseEntities = new ArrayList<>();
    public BotanistConfig botanist = null;
    public CompanionConfig companion = null;
    public List<EnemyBulletConfig> bullets = new ArrayList<>();
    public List<EnemyConfig> enemies = new ArrayList<>();
    public List<ExtractorConfig> extractors = new ArrayList<>();
    public List<GateConfig> gates = new ArrayList<>();
    public List<PowerupConfig> powerups = new ArrayList<>();
    public ShipConfig ship = null;
    public List<TurretConfig> turrets = new ArrayList<>();
    public UpgradeBenchConfig upgradeBench = null;
    public List<WallConfig> walls = new ArrayList<>();
    public List<WeaponConfig> weapons = new ArrayList<>();

    /**
     * Returns a list of all config entities in the game area
     */
    public List<BaseEntityConfig> getAllConfigs() {
        List<BaseEntityConfig> entities = new ArrayList<>();
        //Ensure this list contains all property lists
        entities.addAll(asteroids);
        entities.addAll(baseEntities);
        if (botanist != null) entities.add(botanist);
        if (companion != null) entities.add(companion);
        entities.addAll(bullets);
        entities.addAll(enemies);
        entities.addAll(extractors);
        entities.addAll(gates);
        entities.addAll(powerups);
        if (ship != null) entities.add(ship);
        entities.addAll(turrets);
        if (upgradeBench != null) entities.add(upgradeBench);
        entities.addAll(walls);
        entities.addAll(weapons);
        return entities;
    }
}
