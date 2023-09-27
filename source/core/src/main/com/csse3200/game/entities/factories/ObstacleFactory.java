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
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  public static float WALL_SIZE = 0.1f;

  private static final WallConfigs configs =
          FileLoader.readClass(WallConfigs.class, "configs/walls.json");

  private static final AsteroidConfig asteroidConfig =
          FileLoader.readClass(AsteroidConfig.class, "configs/asteroid.json");
  private static final MinigameConfigs minigameConfigs =
          FileLoader.readClass(MinigameConfigs.class, "configs/minigame.json");
  private static final TurretConfigs turretconfigs =
          FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");

  private static final BaseEntityConfig treeConfig =
          FileLoader.readClass(BaseEntityConfig.class, "configs/tree.json");
  protected static TerrainComponent terrain;

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree(BaseEntityConfig config) {
    Entity tree =
            new Entity()
                    .addComponent(new TextureRenderComponent(config.spritePath))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
  }

  //TODO: REMOVE - LEGACY
  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    return createTree(treeConfig);
  }

  /**
   * Creates a visible obstacle
   * @return Environment entity
   */
  public static Entity createEnvironment() {
    Entity environment =
            new Entity().addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    environment.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    return environment;
  }

  /**
   * Creates an obstacle with a custom sized collision box
   * @param sizeX the length of the new collision box
   * @param sizeY the height of the new collision box
   * @param posX the relative x coordinate of the top left corner of the collision box
   * @param posY the relative y coordinate of the top left corner of the collision box
   * @return Environment entity with the set collision box
   */
  public static Entity createEnvironment(float sizeX, float sizeY, float posX, float posY) {
    Entity environment =
            new Entity().addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    environment.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    PhysicsUtils.setCustomCollider(environment, sizeX, sizeY, posX, posY);
    return environment;
  }

  /**
   * Creates a tree top entity.
   * @return entity
   */
  public static Entity createTreeTop(TreeTopConfig treeTopConfig) {
    Entity treeTop =
            new Entity()
                    .addComponent(new TextureRenderComponent(treeTopConfig.spritePath)) // Replace with the path to your tree top texture
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.NONE));

    treeTop.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    treeTop.getComponent(TextureRenderComponent.class).scaleEntity();
    treeTop.scaleHeight(treeTopConfig.scaleH);
    PhysicsUtils.setScaledCollider(treeTop, treeTopConfig.scaleX, treeTopConfig.scaleY);
    return treeTop;
  }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    wall.setScale(width, height);
    return wall;
  }

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
            .addComponent(asteroidCollider);
    asteroid.setScale(config.scale);
    return asteroid;
  }

  //TODO: REMOVE - LEGACY
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
            .addComponent(new TextureRenderComponent("images/meteor.png"))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
            .addComponent(asteroidCollider);
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
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    asteroid.setScale(config.scale);
    return asteroid;
  }

  public static Entity createBorder(float width, float height) {
    Entity asteroid = new Entity()
            .addComponent(new TextureRenderComponent("images/stone.png"))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    asteroid.setScale(width, height);
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

  public static Entity createObstacleEnemy(float width, float height){
    minigameConfigs.obstacleEnemy.scale = new Vector2(width, height);
    return createObstacleEnemy(minigameConfigs.obstacleEnemy);
  }

  public static Entity createObstacleGameGoal(BaseEntityConfig config){ //TODO: Could create custom config type if necessary
    Entity goal = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))

            //.addComponent(new TextureRenderComponent(config.spritePath))


            .addComponent(new TextureRenderComponent("images/wormhole.png"));
    //NEED to decide spritePath texture or png texture
    goal.setScale(config.scale);

    return goal;
  }

  //TODO: REMOVE - LEGACY
  public static Entity createObstacleGameGoal(float width, float height){
    minigameConfigs.obstacleGameGoal.scale = new Vector2(width, height);
    return createObstacleGameGoal(minigameConfigs.obstacleGameGoal);
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }

  public static PlaceableEntity createCustomTurret(TurretConfig config) {
    return new Turret(config);
  }

  //TODO: REMOVE - LEGACY
  public static PlaceableEntity createCustomTurret(TurretType type, Entity player) {
    return new Turret(type,player);
  }

}
