package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WallConfigs;
import com.csse3200.game.entities.configs.AsteroidConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.buildables.Wall;

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
   * @param restitution Asteroid bounce once hit
   * @return Asteroid entity of given width, height and bounciness
   */
  public static Entity createAsteroid(float width, float height, float restitution) {
    ColliderComponent asteroidCollider = new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE);
    asteroidCollider.setRestitution(restitution);
    Entity asteroid = new Entity()
            .addComponent(new TextureRenderComponent("images/meteor.png"))
            .addComponent(new PhysicsComponent().setBodyType(BodyType.DynamicBody))
            .addComponent(asteroidCollider);
    asteroid.setScale(width, height);
    return asteroid;
  }

  public static Entity createCustomWall(WallType type) {
    return new Wall(configs.GetWallConfig(type));
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
