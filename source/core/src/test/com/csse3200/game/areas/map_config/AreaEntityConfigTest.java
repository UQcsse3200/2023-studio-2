package com.csse3200.game.areas.map_config;

import com.csse3200.game.components.SaveableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.EnemyConfig;
import com.csse3200.game.entities.configs.ExtractorConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class AreaEntityConfigTest {
    private static final PlayerConfig player = new PlayerConfig();
    private static final EnemyConfig enemy1 = new EnemyConfig();
    private static final EnemyConfig enemy2 = new EnemyConfig();
    private static final ExtractorConfig extractor1 = new ExtractorConfig();
    private static final ExtractorConfig extractor2 = new ExtractorConfig();

    private AreaEntityConfig areaEntityConfig;

    @BeforeEach
    void setup() {
        areaEntityConfig = new AreaEntityConfig();
    }

    @Test
    void getAllEntities() {
        areaEntityConfig.entities.put(PlayerConfig.class.toString(), List.of(player));
        areaEntityConfig.entities.put(EnemyConfig.class.toString(), List.of(enemy1, enemy2));
        areaEntityConfig.entities.put(ExtractorConfig.class.toString(), List.of(extractor1, extractor2));

        unorderedMatch(areaEntityConfig.getAllConfigs(), List.of(player, enemy1, enemy2, extractor1, extractor2));
    }

    @Test
    void getNoEntities() {
        assertEquals(areaEntityConfig.getAllConfigs(), new ArrayList<>());
    }

    @Test
    void addEntity() {
        SaveableComponent<PlayerConfig> saveableComponent = new SaveableComponent<>(e -> player, PlayerConfig.class);
        Entity p = new Entity().addComponent(saveableComponent);
        areaEntityConfig.addEntity(p);
        assertEquals(areaEntityConfig.getEntity(PlayerConfig.class), player);
        assertTrue(areaEntityConfig.getEntities(PlayerConfig.class).contains(player));
    }

    @Test
    void addDifferentClassEntities() {
        SaveableComponent<PlayerConfig> playerSaveable = new SaveableComponent<>(e -> player, PlayerConfig.class);
        Entity p = new Entity().addComponent(playerSaveable);

        SaveableComponent<EnemyConfig> enemySaveable = new SaveableComponent<>(e -> enemy1, EnemyConfig.class);
        Entity e = new Entity().addComponent(enemySaveable);

        areaEntityConfig.addEntity(p);
        areaEntityConfig.addEntity(e);

        assertEquals(areaEntityConfig.getEntity(PlayerConfig.class), player);
        assertTrue(areaEntityConfig.getEntities(PlayerConfig.class).contains(player));
        assertEquals(areaEntityConfig.getEntity(EnemyConfig.class), enemy1);
        assertTrue(areaEntityConfig.getEntities(EnemyConfig.class).contains(enemy1));
    }

    @Test
    void addSameClassEntities() {
        SaveableComponent<EnemyConfig> enemySaveable = new SaveableComponent<>(e -> enemy1, EnemyConfig.class);
        Entity e1 = new Entity().addComponent(enemySaveable);

        SaveableComponent<EnemyConfig> enemySaveable2 = new SaveableComponent<>(e -> enemy2, EnemyConfig.class);
        Entity e2 = new Entity().addComponent(enemySaveable2);

        areaEntityConfig.addEntity(e1);
        areaEntityConfig.addEntity(e2);

        assertTrue(areaEntityConfig.getEntities(EnemyConfig.class).contains(enemy1));
        assertTrue(areaEntityConfig.getEntities(EnemyConfig.class).contains(enemy2));
    }

    @Test
    void addMultipleEntitesAtOnce() {
        SaveableComponent<EnemyConfig> enemySaveable = new SaveableComponent<>(e -> enemy1, EnemyConfig.class);
        Entity e1 = new Entity().addComponent(enemySaveable);

        SaveableComponent<EnemyConfig> enemySaveable2 = new SaveableComponent<>(e -> enemy2, EnemyConfig.class);
        Entity e2 = new Entity().addComponent(enemySaveable2);

        SaveableComponent<PlayerConfig> playerSaveable = new SaveableComponent<>(e -> player, PlayerConfig.class);
        Entity p = new Entity().addComponent(playerSaveable);

        areaEntityConfig.addEntities(List.of(e1, e2, p));

        assertTrue(areaEntityConfig.getEntities(EnemyConfig.class).contains(enemy1));
        assertTrue(areaEntityConfig.getEntities(EnemyConfig.class).contains(enemy2));
        assertEquals(areaEntityConfig.getEntity(PlayerConfig.class), player);
    }

    @Test
    void addEntrytoMap() throws InvalidConfigException {
        List<Object> pList = List.of(player);
        areaEntityConfig.addEntry(Map.entry(player.getClass().getSimpleName(), pList));

        assertEquals(areaEntityConfig.getEntity(PlayerConfig.class), player);
    }

    @Test
    void addMultipleEntriestoMap() throws InvalidConfigException {
        List<Object> pList = List.of(player);
        areaEntityConfig.addEntry(Map.entry(player.getClass().getSimpleName(), pList));

        List<Object> eList = List.of(enemy1);
        areaEntityConfig.addEntry(Map.entry(enemy1.getClass().getSimpleName(), eList));

        assertEquals(areaEntityConfig.getEntity(PlayerConfig.class), player);
        assertEquals(areaEntityConfig.getEntity(EnemyConfig.class), enemy1);
    }

    @Test
    void addDuplicateEntriestoMap() throws InvalidConfigException {
        List<Object> e1List = List.of(enemy1);
        areaEntityConfig.addEntry(Map.entry(enemy1.getClass().getSimpleName(), e1List));

        List<Object> e2List = List.of(enemy2);
        assertThrows(InvalidConfigException.class,
                () -> areaEntityConfig.addEntry(Map.entry(enemy2.getClass().getSimpleName(), e2List)));
    }

    private void unorderedMatch(List<?> expectedTextures, List<?> output) {
        assertTrue(expectedTextures.size() == output.size() && expectedTextures.containsAll(output) && output.containsAll(expectedTextures));
    }
}
