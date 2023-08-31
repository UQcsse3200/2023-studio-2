package com.csse3200.game.components.Companion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the Companion. Companion events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class CompanionActions extends Component {
    private String bulletTexturePath;

    private static Vector2 MAX_NORMAL_SPEED = new Vector2(4f, 4f); // Metres per second

    private static final float ROTATION_SPEED = 10.0f; // Adjust the rotation speed as needed
    private float followRadius = 10.0f;
    private float currentRotation = 5.0f;
    private float minDistance = 50.0f; // Define the minimum distance

    private PhysicsComponent physicsComponent;
    public Vector2 walkDirection = Vector2.Zero.cpy();
    public boolean moving = false;
    private boolean speedBoostActive = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);

        // Initialize currentRotation based on the initial orientation of the companion
        currentRotation = physicsComponent.getBody().getAngle()*MathUtils.radiansToDegrees;
        if (playerEntity != null) {
            Vector2 playerPosition = playerEntity.getComponent(PhysicsComponent.class).getBody().getPosition();
            physicsComponent.getBody().setTransform(playerPosition, currentRotation * MathUtils.degreesToRadians);
        }
    }
    //initialising a reference player entity
    private Entity playerEntity;

    public void setPlayerEntity(Entity playerEntity){
        this.playerEntity = playerEntity;
    }

    public void setBulletTexturePath(String path){
        bulletTexturePath = path;
    }
    @Override
    public void update() {
        if (playerEntity != null && moving) {
            updateFollowPlayer();
        } else if (moving) {
            updateSpeed();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.B)){
            speedBoostActive = true;
            MAX_NORMAL_SPEED.set(8f,8f);
        } else {
            speedBoostActive = false;
            MAX_NORMAL_SPEED.set(4f, 4f);
        }
    }
    //functionality for basic player tracking
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
            walkDirection = playerPosition.cpy().sub(companionPosition).nor();

            if (walkDirection.len() >= followRadius) {
                // Move the companion towards the player
                walkDirection.nor();
                updateSpeed();

                // Calculate the rotation angle towards the player
                float targetRotation = walkDirection.angleDeg() + 90;

                // Interpolate the rotation angle smoothly
                currentRotation = MathUtils.lerpAngleDeg(currentRotation, targetRotation, ROTATION_SPEED * Gdx.graphics.getDeltaTime());

                // Set the new rotation for the companion
                physicsComponent.getBody().setTransform(companionPosition, currentRotation * MathUtils.degreesToRadians);
            } else {
                // Stop the companion from walking
                stopWalking();
            }
            // Ensure minimum distance
            if (distanceToPlayer < minDistance) {
                Vector2 newPosition = companionPosition.cpy().add(directionToPlayer.nor().scl(minDistance));
                physicsComponent.getBody().setTransform(newPosition, physicsComponent.getBody().getAngle());
            }
        } else {
            // Stop the companion from walking when movement keys are pressed
            stopWalking();
        }
    }
    private boolean isMovementKeyPressed() {
        // Check if any of the movement keys are pressed (I, J, K, L)
        return Gdx.input.isKeyPressed(Input.Keys.I) || Gdx.input.isKeyPressed(Input.Keys.J) ||
                Gdx.input.isKeyPressed(Input.Keys.K) || Gdx.input.isKeyPressed(Input.Keys.L);
    }

    public void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_NORMAL_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }


    /**
     * Moves the Companion towards a given direction.
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
     * Makes the companion attack.
     */
    void attack() {
        Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
        attackSound.play();
    }


}
