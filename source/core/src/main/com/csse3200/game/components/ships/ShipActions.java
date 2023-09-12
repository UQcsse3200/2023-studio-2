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
    private int maxHealth;
    private int maxFuel;
    private int currentAcceleration;


    private PhysicsComponent physicsComponent;
    private Body body;
    private Vector2 flyDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private Vector2 currentVelocity;

    public ShipActions(int health, int fuel, int acceleration) {
        this.maxHealth = health;
        this.maxFuel = fuel;
        this.currentAcceleration = acceleration;
    }

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        entity.getEvents().addListener("fly", this::fly);
        entity.getEvents().addListener("flystop", this::flystop);
        entity.getEvents().addListener("flystop", this::flystop);
        entity.getEvents().addListener("brakeOn", this::brakeOn);
        entity.getEvents().addListener("brakeOff", this::brakeOff);

        body = physicsComponent.getBody();
        body.setLinearDamping(0); //prevents the ship from stopping for no physical reason

        //entity.getEvents().addListener("attack", this::attack);
    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
    }

    private void updateSpeed() {

        //TBD for all these comments, testing movement.
        //Body body = physicsComponent.getBody();
        //this.currentVelocity = this.body.getLinearVelocity().sub(flyDirection.cpy().scl(1));
        //Vector2 desiredVelocity = flyDirection.cpy().scl(MAX_SPEED);
        //impulse = (desiredVel - currentVel) * mass
        //uses impulse to apply velocity instantly

        //if acceleration? how does one do it?
        //Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        //body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        //body.applyForceToCenter(desiredVelocity.scl(3), true);


        //scl(scalar) basically multiply the Vector2 velocity of body by a scalar. Belongs to Vector2.
        body.applyForceToCenter(flyDirection.cpy().scl(2), true);
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
     * BOOST
     */
    void thrustersOn(Vector2 direction) {
        this.flyDirection = direction;
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
     * Apply auto brake to ship, basically slowing the ship down to stop
     * Activates when B key on keyboard is pressed
     */
    void brakeOn() {

        this.body.setLinearDamping(1);
    }

    /**
     * Removes the brake, ship will not automatically slow down
     * Activates when V key on keyboard is pressed
     */
    void brakeOff() {

        this.body.setLinearDamping(0);
    }


}
