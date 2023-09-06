package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.buildables.TurretType;
import com.csse3200.game.entities.configs.TurretConfigs;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.entities.configs.AsteroidConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.entities.buildables.Turret;

import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.buildables.Wall;
import com.sun.jdi.Type;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  private static final WallConfigs configs =
          FileLoader.readClass(WallConfigs.class, "configs/walls.json");

  private static final AsteroidConfig asteroidCustom =
          FileLoader.readClass(AsteroidConfig.class, "configs/asteroid.json");
  private static final TurretConfigs turretconfigs =
          FileLoader.readClass(TurretConfigs.class, "configs/turrets.json");



  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createTree() {
    Entity tree =
        new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    tree.getComponent(TextureRenderComponent.class).scaleEntity();
    tree.scaleHeight(2.5f);
    PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
    return tree;
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
  public static Entity createStaticAsteroid(float width, float height) {
    Entity asteroid = new Entity()
            .addComponent(new TextureRenderComponent("images/meteor.png"))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    asteroid.setScale(width, height);
    return asteroid;
  }

  public static Entity createObstacleEnemy(float width, float height){
    Entity enemy = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TextureRenderComponent("images/obstacle-enemy.png"));
    enemy.setScale(width, height);
    return enemy;
  }

  public static Entity createObstacleGameGoal(float width, float height){
    Entity goal = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TextureRenderComponent("images/wormhole.jpg"));
    goal.setScale(width, height);
    return goal;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }


  public static Entity createCustomTurret(TurretType type, Entity player) {
    return new Turret(type,player);
  }

}
