package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.entities.configs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that contains data on all the entities for a given game area
 */
public class AreaEntityConfig {
    public List<AsteroidConfig> asteroids = new ArrayList<>();
    public List<BaseEntityConfig> baseEntities = new ArrayList<>();
    public BotanistConfig botanist = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaEntityConfig that = (AreaEntityConfig) o;

        if (!Objects.equals(asteroids, that.asteroids)) return false;
        if (!Objects.equals(baseEntities, that.baseEntities)) return false;
        if (!Objects.equals(botanist, that.botanist)) return false;
        if (!Objects.equals(bullets, that.bullets)) return false;
        if (!Objects.equals(enemies, that.enemies)) return false;
        if (!Objects.equals(extractors, that.extractors)) return false;
        if (!Objects.equals(gates, that.gates)) return false;
        if (!Objects.equals(powerups, that.powerups)) return false;
        if (!Objects.equals(ship, that.ship)) return false;
        if (!Objects.equals(turrets, that.turrets)) return false;
        if (!Objects.equals(upgradeBench, that.upgradeBench)) return false;
        if (!Objects.equals(walls, that.walls)) return false;
        return Objects.equals(weapons, that.weapons);
    }

    @Override
    public int hashCode() {
        int result = asteroids != null ? asteroids.hashCode() : 0;
        result = 31 * result + (baseEntities != null ? baseEntities.hashCode() : 0);
        result = 31 * result + (botanist != null ? botanist.hashCode() : 0);
        result = 31 * result + (bullets != null ? bullets.hashCode() : 0);
        result = 31 * result + (enemies != null ? enemies.hashCode() : 0);
        result = 31 * result + (extractors != null ? extractors.hashCode() : 0);
        result = 31 * result + (gates != null ? gates.hashCode() : 0);
        result = 31 * result + (powerups != null ? powerups.hashCode() : 0);
        result = 31 * result + (ship != null ? ship.hashCode() : 0);
        result = 31 * result + (turrets != null ? turrets.hashCode() : 0);
        result = 31 * result + (upgradeBench != null ? upgradeBench.hashCode() : 0);
        result = 31 * result + (walls != null ? walls.hashCode() : 0);
        result = 31 * result + (weapons != null ? weapons.hashCode() : 0);
        return result;
    }
}
