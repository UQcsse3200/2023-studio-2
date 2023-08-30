package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.joinable.JoinableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.buildables.Gate;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.CombatStatsComponent;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
  private static Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

  private EntityService entityService = new EntityService();
  private PhysicsComponent physicsComponent;
  private Vector2 walkDirection = Vector2.Zero.cpy();
  private boolean moving = false;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entity.getEvents().addListener("walk", this::walk);
    entity.getEvents().addListener("walkStop", this::stopWalking);
    entity.getEvents().addListener("attack", this::attack);
    entity.getEvents().addListener("place", this::placeOrUpgradeWall);
    entity.getEvents().addListener("remove", this::removeWall);
    entity.getEvents().addListener("dodged", this::dodged);
  }

  @Override
  public void update() {
    if (moving) {
      updateSpeed();
    }
  }

  private void updateSpeed() {
    Body body = physicsComponent.getBody();
    Vector2 velocity = body.getLinearVelocity();
    Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
    // impulse = (desiredVel - currentVel) * mass
    Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  /**
   * Moves the player towards a given direction.
   *
   * @param direction direction to move in
   */
  void walk(Vector2 direction) {
    this.walkDirection = direction;
    moving = true;
  }

  /**
   * Stops the player from walking.
   */
  void stopWalking() {
    this.walkDirection = Vector2.Zero.cpy();
    updateSpeed();
    moving = false;
  }

  /**
   * Makes the player attack.
   */
  void attack() {
    Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    attackSound.play();
  }


  /**
   * Changes player's immunity status while dodging.
   */
  void dodged() {
    entity.getComponent(CombatStatsComponent.class).changeImmunityStatus();
  }

  /**
   * Sets the maximum speed of the entity given x and y.
   *
   * @param x The horizontal speed
   * @param y The vertical speed
   */
  public void setSpeed(float x, float y) {
    MAX_SPEED = new Vector2(x, y);
  }

  /**
   * Retrieves the players current maximum speed.
   *
   * @return The maximum speed in Vector2 format.
   */
  public Vector2 getSpeed() {
    return MAX_SPEED;
  }

  void placeOrUpgradeWall(int screenX, int screenY) {
    var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);
    Entity existingWall = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

    if (existingWall != null) {
      if (existingWall.getWallType() != WallType.intermediate) {
        existingWall.dispose();
        this.entityService.unregister(existingWall);
        Entity wall = BuildablesFactory.createCustomWall(WallType.intermediate);
        ServiceLocator.getStructurePlacementService().PlaceStructureAt(wall, new GridPoint2(((int) ((location.x) / 2) * 2), ((int) ((location.y) / 2)) * 2), false, false);
      }
    } else {
      Entity wall = BuildablesFactory.createCustomWall(WallType.basic);

      ServiceLocator.getStructurePlacementService().PlaceStructureAt(wall, new GridPoint2(((int) ((location.x) / 2) * 2), ((int) ((location.y) / 2)) * 2), false, false);
      wall.getComponent(JoinableComponent.class).notifyNeighbours(true);
    }
  }

  void removeWall(int screenX, int screenY) {
    var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
    GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);
    Entity existingWall = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

    if (existingWall != null) {
        existingWall.getComponent(JoinableComponent.class).notifyNeighbours(false);
        existingWall.dispose();

        ServiceLocator.getStructurePlacementService().removeStructureAt(gridPosition);
        this.entityService.unregister(existingWall);
    }
  }
}
