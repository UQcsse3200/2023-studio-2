package com.csse3200.game.areas.mapConfig;

import com.csse3200.game.components.SaveableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that contains data on all the entities for a given game area
 */
public class AreaEntityConfig {

    public List<AsteroidConfig> asteroids = new ArrayList<>();
    public List<BaseEntityConfig> baseEntities = new ArrayList<>();
    public BotanistConfig botanist = null;
    public AstroConfig Astro = null;
    public AstronautConfig astronaut=null;
    public CompanionConfig companion = null;
    public JailConfig Jail = null;
    public List<EnemyBulletConfig> bullets = new ArrayList<>();
    public List<SpawnerConfig> spawners = new ArrayList<>();
    public List<PortalConfig> portals = new ArrayList<>();
    public List<ExtractorConfig> extractors = new ArrayList<>();
    public List<GateConfig> gates = new ArrayList<>();
    public List<PowerupConfig> powerups = new ArrayList<>();
    public ShipConfig ship = null;
    public List<TurretConfig> turrets = new ArrayList<>();
    public List<WallConfig> walls = new ArrayList<>();
    public List<WeaponConfig> weapons = new ArrayList<>();
    public TreeTopConfig treetop = null;

    private static final String DUPLICATE_ENTITY_KEY = "Duplicate entity type key in config files";

    public HashMap<String, List<Object>> entities = new HashMap<>();

    /**
     * Returns a list of the type of entity from the game area.
     * @param entityType Class of entity type to be returned
     * @param <T> Entity type
     * @return List of entities with type T
     */
    public <T extends BaseEntityConfig> List<T> getEntities(Class<T> entityType) {
        if (entities.get(entityType.getSimpleName()) == null) return new ArrayList<>();
        return entities.get(entityType.getSimpleName()).stream().map(o -> (T) o).collect(Collectors.toList());
    }

    /**
     * Returns an entity of type T from the game area.
     * @param entityType Class of entity type to be returned
     * @param <T> Entity type
     * @return The entity in the game area of type T or null if the number of entities of that type is not 1.
     */
    public <T extends BaseEntityConfig> T getEntity(Class<T> entityType) {
        if (entities.get(entityType.getSimpleName()) != null && entities.get(entityType.getSimpleName()).size() == 1)
            return (T) entities.get(entityType.getSimpleName()).get(0);
        else return null;
    }

    // Returns a given list casting the elements to the type provided
    private List<BaseEntityConfig> castList(List<Object> list) {
        return list.stream().map(o -> (BaseEntityConfig) o).collect(Collectors.toList());
    }


    /**
     * Returns a list of all config entities in the game area
     */
    public List<BaseEntityConfig> getAllConfigs() {

        List<BaseEntityConfig> configs = new ArrayList<>();
        return entities.values().stream()
                .map(this::castList)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Adds the entity to the config
     * Must have SaveableComponent
     * @param entity Entity to be added
     */
    public <T extends BaseEntityConfig> void addEntity(Entity entity) {
        T config = (T) entity.getComponent(SaveableComponent.class).save();
        Class entityType = entity.getComponent(SaveableComponent.class).getType();
        if (entities.get(entityType.getSimpleName()) == null) {
            List<Object> configs = new ArrayList<>();
            configs.add(config);
            entities.put(entityType.getSimpleName(), configs);
        } else {
            entities.get(entityType.getSimpleName()).add(config);
        }
    }

    /**
     * Adds the entities to the config
     * Must have SaveableComponent
     * @param entities Entities to be added
     */
    public <T extends BaseEntityConfig> void addEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            addEntity(entity);
        }
    }

    /**
     * Adds an entry to the entities hashmap
     * @throws InvalidConfigException if an entry with the same key already exists
     */
    public void addEntry(Map.Entry<String, List<Object>> entry) throws InvalidConfigException {
        if (entities.containsKey(entry.getKey())) throw new InvalidConfigException(DUPLICATE_ENTITY_KEY + entry.getKey());
        entities.put(entry.getKey(), entry.getValue());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AreaEntityConfig that = (AreaEntityConfig) o;


        return Objects.deepEquals(entities, that.entities);

    }

    @Override
    public int hashCode() {


        return entities.hashCode();

    }
}
