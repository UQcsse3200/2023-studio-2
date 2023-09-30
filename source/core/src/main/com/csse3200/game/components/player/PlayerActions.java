package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.areas.MapGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.HealthBarComponent;
import com.csse3200.game.components.structures.StructureToolPicker;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.GameStateInteraction;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be
 * initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
    private static Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private final EntityService entityService = new EntityService();
    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private boolean sliding = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("remove", this::remove);
        entity.getEvents().addListener("dodged", this::dodged);
        entity.getEvents().addListener("change_structure", this::changeStructure);
        GameStateInteraction gameStateInteraction = new GameStateInteraction();
    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
    }

    /**
     * Updates the player's velocity in the direction they are walking
     */
    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        float speedMult = MapGameArea.getSpeedMult();
        Vector2 desiredVelocity = walkDirection.cpy().scl(new Vector2(MAX_SPEED.x * speedMult, MAX_SPEED.y * speedMult));
        if(sliding) {
            velocity.scl(0.95f);
            if(velocity.isZero(0.01f)){
                sliding = false;
            }
        }
        else{
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
        boolean onIce = MapGameArea.isOnIce(); //TODO: Implement function in MapGameArea.java to handle this
        this.walkDirection = Vector2.Zero.cpy();
        if(onIce) {
            sliding = true;
        }
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
     * Converts the screen coords to a grid position and then places the selected structure
     * doesn't exist at the grid position, otherwise upgrades the existing structure.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void place(int screenX, int screenY) {
        // gets the gridPosition of the wall from the screen click
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2((int)location.x, (int)location.y);

        var structurePicker = getEntity().getComponent(StructureToolPicker.class);
        structurePicker.interact(gridPosition);
    }

    /**
     * Removes the structure the corresponding grid value from screen coords.
     *
     * @param screenX - the x coord of the screen
     * @param screenY - the y coord of teh screen
     */
    void remove(int screenX, int screenY) {
        var location = ServiceLocator.getTerrainService().ScreenCoordsToGameCoords(screenX, screenY);
        GridPoint2 gridPosition = new GridPoint2((int)location.x, (int)location.y);
        Entity structure = ServiceLocator.getStructurePlacementService().getStructureAt(gridPosition);

        if (structure != null) {
            ServiceLocator.getStructurePlacementService().removeStructureAt(gridPosition);
        }
    }

    void changeStructure() {
        var picker = entity.getComponent(StructureToolPicker.class);

        if (picker == null) {
            return;
        }

        picker.show();
    }


}