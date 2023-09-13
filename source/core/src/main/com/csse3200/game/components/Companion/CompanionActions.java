package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.components.ItemPickupComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.commands.DebugCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * This class represents the action component for interacting with a Companion entity.
 * It handles various actions related to the Companion, such as movement, speed, and attacking.
 */
public class CompanionActions extends Component {
    private String bulletTexturePath;

    private static Vector2 COMPANION_SPEED; // Metres per second

    private static Logger logger; // how to log print

    private static final Vector2 MAX_BOOST_SPEED = new Vector2(8f, 8f);

    private static final Vector2 NORMAL_COMPANION_SPEED = new Vector2(5f, 5f); //the normal speed of companion

    private static final float ROTATION_SPEED = 10.0f;
    private float currentRotation = 5.0f;

    private PhysicsComponent physicsComponent;
    public Vector2 movingDirection = Vector2.Zero.cpy();

    public boolean moving = false;
    private boolean speedBoostActive = false;
    //initialising a reference player entity
    private Entity playerEntity;

    /**
     * Initialise the companion to be facing the player.
     * It sets up event listeners for companion movements and actions.
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("move", this::move);
        entity.getEvents().addListener("moveStop", this::stopMoving);
        entity.getEvents().addListener("attack", this::attack);
        COMPANION_SPEED = (NORMAL_COMPANION_SPEED); //Set the companion to go at the normal speed set

        // Initialize currentRotation based on the initial orientation of the companion
        currentRotation = physicsComponent.getBody().getAngle() * MathUtils.radiansToDegrees;
        if (playerEntity != null) {
            Vector2 playerPosition = playerEntity.getComponent(PhysicsComponent.class).getBody().getPosition();
            physicsComponent.getBody().setTransform(playerPosition, currentRotation * MathUtils.degreesToRadians);
        }

        logger = LoggerFactory.getLogger(CompanionActions.class);
    }

    /**
     * Set the player entity, binding it to the companion.
     *
     * @param playerEntity - the player entity
     */
    public void setPlayerEntity(Entity playerEntity) {
        this.playerEntity = playerEntity;
    }

    /**
     * Set the bullet texture path.
     *
     * @param path - Path????
     */
    public void setBulletTexturePath(String path) {
        bulletTexturePath = path;
    }

    /**
     * Update.
     * This is called once per frame, and will update the companion state.
     * It ensures the companion follows the player and adjusts its speed if boost is activated.
     */
    @Override
    public void update() {
        updateSpeed();

        // if boost is active, adjust speed
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            updateSpeedBoost(true, (MAX_BOOST_SPEED));
        } else {
            updateSpeedBoost(false, (NORMAL_COMPANION_SPEED));
        }
    }

    /**
     * Updates the speed boost status and the speed of the companion.
     *
     * @param speedBoostActive - true/false parameter to tell if in boost mode
     * @param speedValues      - the value/speed in which to update the companion to
     */
    private void updateSpeedBoost(Boolean speedBoostActive, Vector2 speedValues) {
        if (speedBoostActive) {
            COMPANION_SPEED.set(speedValues); //should also set some variable 'on' for companion, such as attack mode.
        } else {
            COMPANION_SPEED.set(speedValues);
        }
    }

    /**
     * Checks if a single movement key has been pressed.
     *
     * @return - true/false boolean if any movement keys were pressed.
     */
    private boolean isMovementKeyPressed() {
        // Check if any of the movement keys are pressed (I, J, K, L)
        return Gdx.input.isKeyPressed(Input.Keys.I) || Gdx.input.isKeyPressed(Input.Keys.J) ||
                Gdx.input.isKeyPressed(Input.Keys.K) || Gdx.input.isKeyPressed(Input.Keys.L);
    }

    /**
     * updateSpeed
     * This function is for regular movement. It gets the velocity of the
     * body, and the desired direction and applies a vector transformation.
     * It uses a movingDirection vector which is constantly updated by keyboard input.
     */
    public void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = movingDirection.cpy().scl(COMPANION_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Method to show if the companion is currently moving or not.
     *
     * @return - boolean true if moving, false if still
     */
    public boolean isCompanionBeingMoved() {
        return this.moving;
    }

    /**
     * Called by the keyboard input.
     * Moves the Companion towards a given direction.
     *
     * @param direction direction to move in
     */
    public void move(Vector2 direction) {
        this.movingDirection = direction;
        moving = true;
    }

    /**
     * Called by the keyboard input.
     * Stops the player from moving. Gives the moving direction a 0,0 value.
     */
    public void stopMoving() {
        this.movingDirection = Vector2.Zero.cpy();
        moving = false;
    }

    /**
     * Makes the companion attack.
     */
    void attack() {
        // Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
        // attackSound.play();
    }

    /**
     * Set the speed to a set number.
     *
     * @param x - how fast in x direction
     * @param y - how fast in y direction
     */
    public void setSpeed(float x, float y) {
        COMPANION_SPEED = new Vector2(x, y);
    }
}