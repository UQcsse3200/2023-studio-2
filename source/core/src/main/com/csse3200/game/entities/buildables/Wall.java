package com.csse3200.game.entities.buildables;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.*;
import com.csse3200.game.components.structures.JoinLayer;
import com.csse3200.game.components.structures.JoinableComponent;
import com.csse3200.game.components.structures.JoinableComponentShapes;
import com.csse3200.game.components.structures.StructureDestroyComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.configs.TurretConfig;
import com.csse3200.game.entities.configs.WallConfig;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Objects;

/**
 * Core wall class. Wall inherits entity and adds the required components and functionality for
 * a functional wall within the game. All walls have a static body and a WALL ColliderComponent.
 * Walls' take up 1 tile on the map and have custom health and textures (as defined in configs/walls.json).
 *
 * <p>Example use:
 *
 * <pre>
 * WallConfig config = new WallConfig();s
 * Entity wall = new Wall(config);
 * </pre>
 */
public class Wall extends PlaceableEntity {
    private static final JoinableComponentShapes shapes =
            FileLoader.readClass(JoinableComponentShapes.class, "vertices/walls.json");

    WallType type;

    /**
     * Constructor to create a Wall entity based on the given config.
     *
     * <p>Predefined wall properties are loaded from a config stored as a json file and should have
     * the properties stored in 'WallConfig'.
     */
    /**
     *
     * This adding multiple components for interaction
     * and also added Sound Component to trigger sound effects
     * @param config - the config for the wall
     */
    public Wall(WallConfig config) {
        super(2, 2);

        var textures = ServiceLocator.getResourceService().getAsset(config.spritePath, TextureAtlas.class);

        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL));
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.STRUCTURE));
        addComponent(new CombatStatsComponent(config.health, config.maxHealth, 0,0,false));
        addComponent(new HealthBarComponent(true));
        addComponent(new JoinableComponent(textures, JoinLayer.WALLS, shapes));
        addComponent(new StructureDestroyComponent());
        addComponent(new SoundComponent(config.sounds));
        addComponent(new SaveableComponent<>(wall -> save(wall, config), WallConfig.class));

        getComponent(JoinableComponent.class).scaleEntity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Wall wall = (Wall) o;
        return type == wall.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }

    /**
     * A function to save the wall's properties into its config.
     * @param entity - the wall to save.
     * @param config - the existing config for the wall.
     * @return the updated config for the wall.
     */
    private static WallConfig save(Entity entity, WallConfig config) {
        if (!(entity instanceof Wall)) {
            return new WallConfig();
        }

        config.position = entity.getGridPosition();
        config.health = entity.getComponent(CombatStatsComponent.class).getHealth();

        return config;
    }
}
