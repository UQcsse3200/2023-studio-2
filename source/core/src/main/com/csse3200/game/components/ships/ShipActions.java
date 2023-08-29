package com.csse3200.game.components.ships;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Ship events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class ShipActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 flyDirection = Vector2.Zero.cpy();
    private boolean moving = false;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("fly", this::fly);
        entity.getEvents().addListener("flystop", this::flystop);
        //entity.getEvents().addListener("attack", this::attack);
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
        Vector2 desiredVelocity = flyDirection.cpy().scl(MAX_SPEED);
        //impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the ship towards a given direction.
     *
     * @param direction direction to move in
     */
    void fly(Vector2 direction) {
        this.flyDirection = direction;
        moving = true;
    }

    /**
     * Stops the ship from moving
     */
    void flystop() {
        this.flyDirection = Vector2.Zero.cpy();
        updateSpeed();
        moving = false;
    }

    /**
     * Makes the player attack.
     */
    //void attack() {
    //  Sound attackSound = ServiceLocator.getResourceService().getAsset("sounds/Impact4.ogg", Sound.class);
    //attackSound.play();
    //}
}
