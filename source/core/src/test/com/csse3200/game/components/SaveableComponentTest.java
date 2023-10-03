package com.csse3200.game.components;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.HealthEntityConfig;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
class SaveableComponentTest {
    Entity saveableEntity;
    BaseEntityConfig config;

    @Test
    void functionRunsOnSave() {
        config = new BaseEntityConfig();
        saveableEntity = new Entity().addComponent(new SaveableComponent<>(e -> config, BaseEntityConfig.class));

        assertEquals(config, saveableEntity.getComponent(SaveableComponent.class).save());
    }

    @Test
    void saveReflectsChanges() {
        saveableEntity = new Entity()
                .addComponent(new CombatStatsComponent(100, 10, 1, false));
        saveableEntity.addComponent(new SaveableComponent<>(e -> {
            HealthEntityConfig entityConfig = new HealthEntityConfig();
            entityConfig.health = e.getComponent(CombatStatsComponent.class).getHealth();
            return entityConfig;
        }, HealthEntityConfig.class));

        saveableEntity.getComponent(CombatStatsComponent.class).setHealth(50);

        assertEquals(50,
                ((HealthEntityConfig) saveableEntity.getComponent(SaveableComponent.class).save()).health);
    }
}
