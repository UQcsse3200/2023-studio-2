package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.EnvironmentStatsComponent;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.components.structures.tools.PlacementValidity;
import com.csse3200.game.components.structures.tools.ToolResponse;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
    private Vector2 maxspeed = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private boolean sliding = false;
    private float freezeFactor = 1.0f;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("selectToolIndex", this::selectToolIndex);
        entity.getEvents().addListener("remove", this::remove);
        entity.getEvents().addListener("dodged", this::dodged);
        entity.getEvents().addListener("change_structure", this::changeStructure);
    }

    @Override
    public void update() {
        // Set the condition to always true for now
//        if (MapGameArea.isFreezing()) {
//            if(freezeFactor > 0.5f) {
//                freezeFactor -= 0.001f;
//            } else {
//                freezeFactor = 0.5f;
//            }
//        } else {
//            if(freezeFactor < 1.0f) {
//                freezeFactor += 0.02f;
//            } else {
//                freezeFactor = 1.0f;
//            }
//        }
        if (entity.getComponent(EnvironmentStatsComponent.class) != null) {
            freezeFactor = 1.0f - (float) entity.getComponent(EnvironmentStatsComponent.class).getFrozenLevel() / 100;
        }

        if (moving) {
            updateSpeed();
        }
    }

    /**
     * The player gets slower when walking on certain frozen tiles
     */
    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        float speedMult = MapGameArea.getSpeedMult();
        Vector2 desiredVelocity = walkDirection.cpy().scl(new Vector2(maxspeed.x * speedMult, maxspeed.y * speedMult));
        desiredVelocity.scl(freezeFactor); // Reduce speed when the condition is true (always true for now)

        if(sliding) {
            velocity.scl(0.95f);
            if(velocity.isZero(0.01f)){
                sliding = false;
            }
        }
        else {
            Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
            body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        }
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
        sliding = MapGameArea.isOnIce();
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
        maxspeed = new Vector2(x, y);
    }

    /**
     * Retrieves the players current maximum speed.
     *
     * @return The maximum speed in Vector2 format.
     */
    public Vector2 getSpeed() {
        return maxspeed;
    }

    /**
     * Converts the screen coords to a grid position and then places the selected structure
     * doesn't exist at the grid position, otherwise upgrades the existing structure.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void place(int screenX, int screenY) {
        // gets the gridPosition of the wall from the screen click
        var location = ServiceLocator.getTerrainService().screenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2((int)location.x, (int)location.y);

        var structurePicker = getEntity().getComponent(StructureToolPicker.class);
        structurePicker.interact(gridPosition);
    }

    /**
     * Selects the tool at the given index in the structure picker.
     *
     * @param index - the index of the tool to select.
     */
    void selectToolIndex(int index) {
        var structurePicker = getEntity().getComponent(StructureToolPicker.class);
        structurePicker.selectIndex(index);
    }

    /**
     * Removes the structure the corresponding grid value from screen coords.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void remove(int screenX, int screenY) {
        var location = ServiceLocator.getTerrainService().screenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2((int)location.x, (int)location.y);
        Entity structure = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        if (structure != null) {
            var distance = this.getEntity().getCenterPosition().scl(2).dst(new Vector2(gridPosition.x, gridPosition.y));
            float removalRange = 5.0f;
            if (distance <= removalRange) {
                ServiceLocator.getStructurePlacementService().removeStructureAt(gridPosition);
            } else {
                ToolResponse response = new ToolResponse(PlacementValidity.OUT_OF_RANGE, "Out of range.");
                logger.error(response.getMessage());
                this.getEntity().getEvents().trigger("displayWarningAtPosition", response.getMessage(),
                        new Vector2((float) gridPosition.x / 2, (float) gridPosition.y / 2));
                this.getEntity().getEvents().trigger("playSound", "alert");
            }
        }
    }

    void changeStructure() {
        var picker = entity.getComponent(StructureToolPicker.class);

        if (picker == null) {
            return;
        }

        picker.show();
    }


    public boolean isMoving() {
        return false;
    }
}
