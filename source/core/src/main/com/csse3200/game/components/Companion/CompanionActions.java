package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.FollowComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.terminal.commands.DebugCommand;

/**
 * Action component for interacting with the Companion. Companion events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class CompanionActions extends Component {
    private String bulletTexturePath;

    private static Vector2 COMPANION_SPEED; // Metres per second

    private static final Vector2 MAX_BOOST_SPEED = new Vector2(8f, 8f);

    private static final Vector2 NORMAL_COMPANION_SPEED = new Vector2( 5f, 5f); //the normal speed of companion

    private static final float ROTATION_SPEED = 10.0f;
    private float currentRotation = 5.0f;

    private PhysicsComponent physicsComponent;
    public Vector2 movingDirection = Vector2.Zero.cpy();

    public boolean moving = false;
    private boolean speedBoostActive = false;
    //initialising a reference player entity
    private Entity playerEntity;

    /**
     * Initialise the companion to be facing the player
     */
    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("move", this::move);
        entity.getEvents().addListener("moveStop", this::stopMoving);
        entity.getEvents().addListener("attack", this::attack);
        COMPANION_SPEED = (NORMAL_COMPANION_SPEED); //Set the companion to go at the normal speed set

        // Initialize currentRotation based on the initial orientation of the companion
        currentRotation = physicsComponent.getBody().getAngle()*MathUtils.radiansToDegrees;
        if (playerEntity != null) {
            Vector2 playerPosition = playerEntity.getComponent(PhysicsComponent.class).getBody().getPosition();
            physicsComponent.getBody().setTransform(playerPosition, currentRotation * MathUtils.degreesToRadians);
        }
    }

    /**
     * Set the player entity, binding it to the companion
     * @param playerEntity - the player entity
     */
    public void setPlayerEntity(Entity playerEntity){
        this.playerEntity = playerEntity;
    }

    /**
     * Set the bullet texture path
     * @param path - Path????
     */
    public void setBulletTexturePath(String path){
        bulletTexturePath = path;
    }

    /**
     * Update
     * This is called once per frame, and will update the companion state
     * We make sure we are following the player, and that if boost is activated, our speed is adjusted
     */
    @Override
    public void update() {
        //
        if (playerEntity != null && moving) {
            updateFollowPlayer();
        } else if (moving) {
            updateSpeed();
        }

        // if boost is active, adjust speed
        if (Gdx.input.isKeyPressed(Input.Keys.B)){
            updateSpeedBoost(true, (MAX_BOOST_SPEED));
        } else {
            updateSpeedBoost(false, (NORMAL_COMPANION_SPEED));
        }
    }

    /**
     * NEW UPDATE FOLLOW PLAYER
     * UpdateMovement
     * Handles all the movement required of the companion on each frame
     * Checks if the companion is in boost mode, and adjusts the speed accordingly
     *
     * Then, checks if the companion is moving or not. If it is not moving, it moves the companion
     * towards the player
     *
     */
    public void newUpdateFollowPlayer() {

        // Check if boost is on
        if (Gdx.input.isKeyPressed(Input.Keys.B)){
            updateSpeedBoost(true, (MAX_BOOST_SPEED));
        } else {
            updateSpeedBoost(false, (NORMAL_COMPANION_SPEED));
        }

        // Update actual movement of the companion
        // Check if we need to follow or are moving independently
        if (moving) {
            //update the speed as if you are just moving like normal
            updateSpeed();
            System.out.println("Moving independently");
        } else {
            // Must move towards player
            System.out.println("Not moving - follow player");
            entity.getComponent(FollowComponent.class).update();
        }

    }

    /**
     * updateFollowPlayer
     * Making sure that the companion is following the player on the map (being gravitationally attracted)
     */
    public void updateFollowPlayer() {
        Vector2 playerPosition = playerEntity.getComponent(PhysicsComponent.class).getBody().getPosition();
        Vector2 companionPosition = physicsComponent.getBody().getPosition();

        // Calculate direction vector towards the player
        Vector2 directionToPlayer = playerPosition.cpy().sub(companionPosition);
        float distanceToPlayer = directionToPlayer.len();

        double minDistanceThreshold = 50.0f;
        if (distanceToPlayer < minDistanceThreshold) {
            physicsComponent.getBody().setActive(false); // Disable physics simulation
        } else {
            physicsComponent.getBody().setActive(true); // Enable physics simulation
            updateSpeed(); // Only apply speed if physics is active
        }

        // Check if any movement key is pressed
        boolean isMovementKeyPressed = isMovementKeyPressed();

        if (!isMovementKeyPressed) {
            // Calculate direction vector towards the player
            movingDirection = playerPosition.cpy().sub(companionPosition).nor();
            // Move the companion towards the player
            movingDirection.nor();
            updateSpeed();

            // Calculate the rotation angle towards the player
            float targetRotation = movingDirection.angleDeg() + 90;

            // Interpolate the rotation angle smoothly
            currentRotation = MathUtils.lerpAngleDeg(currentRotation, targetRotation, ROTATION_SPEED * Gdx.graphics.getDeltaTime());

            // Set the new rotation for the companion
            physicsComponent.getBody().setTransform(companionPosition, currentRotation * MathUtils.degreesToRadians);

        } else {
            // Stop the companion from walking when movement keys are pressed
            stopMoving();
        }
    }

    /**
     * Updates the speed boost status and the speed of the companion
     * @param speedBoostActive - true/false parameter to tell if in boost mode
     * @param speedValues - the value/speed in which to update the companion to
     */
    private void updateSpeedBoost(Boolean speedBoostActive, Vector2 speedValues) {
        if (speedBoostActive) {
            COMPANION_SPEED.set(speedValues); //should also set some variable 'on' for companion, such as attack mode.
        } else {
            COMPANION_SPEED.set(speedValues);
        }
    }

    /**
     * Checks if a single movement key has been pressed
     * @return - true/false boolean if any movement keys were pressed.
     */
    private boolean isMovementKeyPressed() {
        // Check if any of the movement keys are pressed (I, J, K, L)
        return Gdx.input.isKeyPressed(Input.Keys.I) || Gdx.input.isKeyPressed(Input.Keys.J) ||
                Gdx.input.isKeyPressed(Input.Keys.K) || Gdx.input.isKeyPressed(Input.Keys.L);
    }

    /**
     * NOT SURE
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
     * Moves the Companion towards a given direction.
     *
     * @param direction direction to move in
     */
    void move(Vector2 direction) {
        this.movingDirection = direction;
        moving = true;
    }

    /**
     * Stops the player from moving. Gives the moving direction a 0,0 value
     */
    void stopMoving() {
        this.movingDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }

    /**
     * Makes the companion attack.
     */
    void attack() {
        Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
        attackSound.play();
    }

    /**
     * set the speed to a set number
     * @param x - how fast in x direction
     * @param y - how fast in y direction
     */
    public void setSpeed(float x, float y) {
        COMPANION_SPEED = new Vector2(x, y);
    }


}
