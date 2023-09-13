package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.entities.buildables.Wall;
import com.csse3200.game.entities.buildables.WallType;
import com.csse3200.game.entities.factories.BuildablesFactory;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameStateInteraction;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.structures.StructurePicker;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create()
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
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("remove", this::removeWall);
        entity.getEvents().addListener("dodged", this::dodged);
        entity.getEvents().addListener("repair", this::repairWall);
        entity.getEvents().addListener("change_structure", this::changeStructure);
        entity.getEvents().addListener("inventory", this::updateInventory);
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
     * Updates inventory
     * @param i - used for determining inventory action
     */
    void updateInventory(int i) {
        switch (i) {
            case 1:
                entity.getComponent(InventoryComponent.class).setEquipped(1);
                break;
            case 2:
                entity.getComponent(InventoryComponent.class).setEquipped(2);
                break;
            case 3:
                entity.getComponent(InventoryComponent.class).setEquipped(3);
                break;
            default:
                entity.getComponent(InventoryComponent.class).cycleEquipped();
                break;
        }
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
<<<<<<< HEAD
     * Converts the screen coords to a grid position and then places the selected structure
     * doesn't exist at the grid position, otherwise upgrades the existing structure.
=======
     * Converts the screen coords to a grid position and then places a wall if a
     * wall
     * doesn't exist at the grid position, otherwise upgrades the wall.
>>>>>>> feature/player
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void place(int screenX, int screenY) {
        // gets the gridPosition of the wall from the screen click
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2(((int) (location.x / 2) * 2), ((int) (location.y / 2)) * 2);

        var structurePicker = getEntity().getComponent(StructurePicker.class);
        structurePicker.interact(entity, gridPosition);
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
        Entity structure = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        if (structure != null) {
            ServiceLocator.getStructurePlacementService().removeStructureAt(gridPosition);
            structure.dispose();
        }
    }

    /**
     * Converts screen coords to grid coords and then repairs the wall at the grid
     * position.
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

                entity.getComponent(HealthBarComponent.class).updateHealth(entity.getComponent(CombatStatsComponent.class).getMaxHealth());
            }
        }
    }

    void changeStructure() {
        var picker = entity.getComponent(StructurePicker.class);

        if (picker == null) {
            return;
        }

        picker.show();
    }


}