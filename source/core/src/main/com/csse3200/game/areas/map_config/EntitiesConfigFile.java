package com.csse3200.game.areas.map_config;

import java.util.List;
import java.util.Map;

/**
 * Configuration class for storing information related to game entities.
 * Each instance represents a type of entity and its associated list of configurations or instances.
 */
public class EntitiesConfigFile {
    public String entityType;
    public List<Object> entities;

    /**
     * Constructs a key-value pair (Map.Entry) where the key is the entity type and the value is the list of entities.
     *
     * @return A Map.Entry with entityType as the key and entities as the value.
     */
    public Map.Entry<String, List<Object>> getMapEntry() {
        return Map.entry(entityType, entities);
    }
}
