package com.csse3200.game.entities.buildables;

//import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.DisplayEntityHealthComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallConfig;
//import com.csse3200.game.input.InputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
//import com.csse3200.game.services.ServiceLocator;

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
public class Wall extends Entity {
    /**
     * Constructor to create a Wall entity based on the given config.
     *
     * <p>Predefined wall properties are loaded from a config stored as a json file and should have
     * the properties stored in 'WallConfig'.
     */
    public Wall(WallConfig config) {
        super();

        addComponent(new TextureRenderComponent(config.texture));
        addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody));
        addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL));
        addComponent(new HitboxComponent().setLayer(PhysicsLayer.WALL));
        addComponent(new CombatStatsComponent(config.health, 0));
        addComponent(new DisplayEntityHealthComponent(true));

        PhysicsUtils.setScaledCollider(this, 1f, 0.5f);

        setScale(1f, 1f);
        getComponent(TextureRenderComponent.class).scaleEntity();
    }
}
