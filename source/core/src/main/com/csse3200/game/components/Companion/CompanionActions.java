package com.csse3200.game.components.Companion;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.CombatStatsComponent;

/**
 * Action component for interacting with the Companion. Companion events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class CompanionActions extends Component {
    private String bulletTexturePath;

    private static Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("walk", this::walk);
        entity.getEvents().addListener("walkStop", this::stopWalking);
        entity.getEvents().addListener("attack", this::attack);
    }
    //initialising a reference player entity
    private Entity playerEntity;

    public void setPlayerEntity(Entity playerEntity){
        this.playerEntity = playerEntity;
    }

    @Override
    public void update() {
        if (playerEntity != null && moving) {
            updateFollowPlayer();
        } else if (moving) {
            updateSpeed();
        }
    }
//functionality for basic player tracking
    private void updateFollowPlayer() {
        Vector2 playerPosition = playerEntity.getComponent(PhysicsComponent.class).getBody().getPosition();
        Vector2 companionPosition = physicsComponent.getBody().getPosition();

        // Calculate direction vector towards the player
        walkDirection = playerPosition.cpy().sub(companionPosition).nor();

        // Update the speed to make the companion move towards the player
        updateSpeed();
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

    /**
     * sets max speed in directions x and y
     * @param x
     * @param y
     */
    public static void setSpeed(float x, float y)
    {
        MAX_SPEED = new Vector2(x,y);
    }

    /**
     * returns the max speed
     * @return
     */
    public Vector2 getSpeed() {
        return MAX_SPEED;
    }
}
