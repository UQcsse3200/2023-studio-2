package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.joinable.JoinableComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameStateInteraction;
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
    private GameStateInteraction gameStateInteraction;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("place", this::placeWallOrUpgradeWall);
        entity.getEvents().addListener("ctrl_place", this::placeGate);
        entity.getEvents().addListener("remove", this::removeWall);
        entity.getEvents().addListener("dodged", this::dodged);
        entity.getEvents().addListener("repair", this::repairWall);
        gameStateInteraction = new GameStateInteraction();
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

    /**
     * Converts the screen coords to a grid position and then places a wall if a wall
     * doesn't exist at the grid position, otherwise upgrades the wall.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void placeWallOrUpgradeWall(int screenX, int screenY) {
        // gets the gridPosition of the wall from the screen click
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);

        // gets the structure at that position if it exists.
        Entity structure = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        // if structure doesn't exist at position, adds wall.
        if (structure == null) {
            Entity wall = BuildablesFactory.createCustomWall(WallType.basic, entity);
            updateResources(-2);
            ServiceLocator.getStructurePlacementService().PlaceStructureAt(wall, gridPosition, false, false);
            wall.getComponent(JoinableComponent.class).notifyNeighbours(true);
            // if the existing structure is a wall, attempt upgrade.
        } else if (structure instanceof Wall existingWall) {
            if (existingWall.getWallType() == WallType.basic) {
                updateResources(-2);
                structure.dispose();
                this.entityService.unregister(structure);
                Entity wall = BuildablesFactory.createCustomWall(WallType.intermediate, entity);
                ServiceLocator.getStructurePlacementService().PlaceStructureAt(wall, gridPosition, false, false);
            }
        }

        // does nothing if the existing structure is not a wall.
    }


    /**
     * Converts the screen coords to a grid position and then places a gate at the
     * grid position.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    private void placeGate(int screenX, int screenY) {
        // gets the gridPosition of the wall from the screen click
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);

        // gets the structure at that position if it exists.
        Entity structure = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        // if structure doesn't exist at position, adds wall.
        if (structure == null) {
            updateResources(-2);
            Entity gate = BuildablesFactory.createGate(entity);
            ServiceLocator.getStructurePlacementService().PlaceStructureAt(gate, gridPosition, false, false);
            gate.getComponent(JoinableComponent.class).notifyNeighbours(true);
        }
    }

    /**
     * Removes a wall or gate the corresponding grid value from screen coords.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void removeWall(int screenX, int screenY) {
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);
        Entity existingWall = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        if (existingWall != null) {
            var joinableComponent = existingWall.getComponent(JoinableComponent.class);

            if (joinableComponent != null) {
                joinableComponent.notifyNeighbours(false);
            }

            existingWall.dispose();
            updateResources(1);
            ServiceLocator.getStructurePlacementService().removeStructureAt(gridPosition);
            this.entityService.unregister(existingWall);
        }
    }

    /**
     * Converts screen coords to grid coords and then repairs the wall at the grid position.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void repairWall(int screenX, int screenY) {
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);
        Entity existingWall = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        if (existingWall != null) {
            if (existingWall.getComponent(CombatStatsComponent.class).getHealth() < existingWall.getComponent(CombatStatsComponent.class).getMaxHealth()) {
                updateResources(-1);
                entity.getComponent(HealthBarComponent.class).updateHealth(entity.getComponent(CombatStatsComponent.class).getMaxHealth());
            }
        }
    }

    /**
     * Updates the Unobtanium resource supply.
     *
     * @param change - how much to change the existing resource by.
     */
    void updateResources(int change) {
        gameStateInteraction.updateResource(Resource.Solstite.toString(), change);

    }
 }