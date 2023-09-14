package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class ObstacleFactoryTest {

    private static final WallConfigs wallConfigs =
            FileLoader.readClass(WallConfigs.class, "configs/walls.json");

    private static final AsteroidConfig asteroidConfig =
            FileLoader.readClass(AsteroidConfig.class, "configs/asteroid.json");
    private static final MinigameConfigs minigameConfigs =
            FileLoader.readClass(MinigameConfigs.class, "configs/minigame.json");
    private static final TurretConfigs turretconfigs =
            FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");

    private static final BaseEntityConfig treeConfig =
            FileLoader.readClass(BaseEntityConfig.class, "configs/tree.json");

    @BeforeEach
    void setUp() {
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.getResourceService().loadTextures(new String[] {"images/tree.png",
                "images/structures/TurretOne.png", "images/structures/TurretTwo.png"} );
        ServiceLocator.getResourceService().loadTextureAtlases(new String[] {} );
        ServiceLocator.getResourceService().loadAll();

    }

    @Test
    void createLevelOneTurretConfigTest() {
        PlaceableEntity turret = ObstacleFactory.createCustomTurret(turretconfigs.GetTurretConfig(TurretType.LEVEL_ONE));
        assertEquals(200, turret.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(2, turret.getComponent(CombatStatsComponent.class).getBaseAttack());

    }

    @Test
    void createLevelTwoTurretConfigTest() {
        PlaceableEntity turret = ObstacleFactory.createCustomTurret(turretconfigs.GetTurretConfig(TurretType.LEVEL_TWO));
        assertEquals(500, turret.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(5, turret.getComponent(CombatStatsComponent.class).getBaseAttack());
    }
}
