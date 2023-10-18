package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Turret;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.configs.*;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.components.ships.SpaceCollideDamageComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.physics.components.HitboxComponent;

/**
 * Creates collidable objects in the Space Obstacle Minigame
 */
public class MinigameObjectFactory {

    private static final AsteroidConfig asteroidConfig =
            FileLoader.readClass(AsteroidConfig.class, "configs/asteroid.json");
    private static final MinigameConfigs minigameConfigs =
            FileLoader.readClass(MinigameConfigs.class, "configs/minigame.json");

    //TODO: Should this be refactored to a "Minigame factory"?
    /**
     * Creates an Asteroid that has bounce from config
     * @param config Configuration file to match asteroid to
     * @return Asteroid entity
     */
    public static Entity createAsteroid(AsteroidConfig config) {
        ColliderComponent asteroidCollider = new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE);
        Entity asteroid = new Entity()
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(asteroidCollider)
                .addComponent(new SpaceCollideDamageComponent(PhysicsLayer.SHIP, (float) 0.5));
        asteroid.setScale(config.scale);
        return asteroid;
    }

    //TODO: Change the game areas method to use the config method instead
    /**
     * Creates an Asteroid that has bounce
     * @param width Asteroid width in world units
     * @param height Asteroid height in world units
     * @return Asteroid entity of given width and height
     */
    public static Entity createAsteroid(float width, float height) {
        ColliderComponent asteroidCollider = new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE);
        //asteroidCollider.setRestitution(restitution); bounce removed
        Entity asteroid = new Entity()
                .addComponent(new TextureRenderComponent("images/minigame/meteor.png"))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(asteroidCollider)
                .addComponent(new SpaceCollideDamageComponent(PhysicsLayer.SHIP, (float) 0.5));
        asteroid.setScale(width, height);
        return asteroid;
    }

    /**
     * Create a new static asteroid from the given config file
     * @param config Configuration file to match asteroid to
     * @return Asteroid Entity
     */
    public static Entity createStaticAsteroid(AsteroidConfig config) {
        Entity asteroid = new Entity()
                .addComponent(new TextureRenderComponent(config.spritePath))
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new SpaceCollideDamageComponent(PhysicsLayer.SHIP, (float) 0.5));
        asteroid.setScale(config.scale);
        return asteroid;
    }

    public static Entity createStaticAsteroid(float width, float height) {
        asteroidConfig.scale = new Vector2(width, height);
        return createStaticAsteroid(asteroidConfig);
    }

    /**
     * Create an obstacle enemy to match given config file
     * @param config Configuration file to match enemy to
     * @return ObstacleEnemy
     */
    public static Entity createObstacleEnemy(BaseEntityConfig config){

        Entity enemy = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent(config.spritePath));
        enemy.setScale(config.scale);
        return enemy;
    }

    //TODO: Decide if this is necessary
    public static Entity createObstacleEnemy(float width, float height){
        minigameConfigs.obstacleEnemy.scale = new Vector2(width, height);
        return createObstacleEnemy(minigameConfigs.obstacleEnemy);
    }

    /**
     * Creates the goal where the ship should reach
     * @param config
     * @return
     */
    public static Entity createObstacleGameGoal(BaseEntityConfig config){ //TODO: Could create custom config type if necessary
        Entity goal = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))

                //.addComponent(new TextureRenderComponent(config.spritePath))


                .addComponent(new TextureRenderComponent("images/minigame/wormhole.png"));
        //NEED to decide spritePath texture or png texture
        goal.setScale(config.scale);

        return goal;
    }

    //TODO: Change the game areas method to use the config method instead
    /**
     * Does not uses config
     * @param width
     * @param height
     * @return
     */
    public static Entity createObstacleGameGoal(float width, float height){
        minigameConfigs.obstacleGameGoal.scale = new Vector2(width, height);
        return createObstacleGameGoal(minigameConfigs.obstacleGameGoal);
    }


    private MinigameObjectFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}