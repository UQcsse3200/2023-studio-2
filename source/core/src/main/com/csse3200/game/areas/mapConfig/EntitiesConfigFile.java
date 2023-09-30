package com.csse3200.game.areas.mapConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntitiesConfigFile {
    public String entityType;
    public List<Object> entities;

    public Map.Entry<String, List<Object>> getMapEntry() {
        return Map.entry(entityType, entities);
    }
}
