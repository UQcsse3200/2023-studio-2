package com.csse3200.game.components.ships;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.services.ServiceLocator;


import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.math.Vector2Utils;

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
    private Vector2 currentVelocity = Vector2.Zero.cpy();
    private AnimationRenderComponent animator;


    private double up = 90;
    private double down = -90;
    private double left = 180;
    private double right = 0;
    private double left2 = -180;

    public ShipActions(int health, int fuel, int acceleration) {
        this.maxHealth = health;
        this.maxFuel = fuel;
        this.currentAcceleration = acceleration;
    }

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("fly", this::fly);
        entity.getEvents().addListener("flystop", this::flystop);
        entity.getEvents().addListener("boostRight", this::thrustersOn);
        entity.getEvents().addListener("brakeOn", this::brakeOn);
        entity.getEvents().addListener("brakeOff", this::brakeOff);

        entity.getEvents().addListener("turnLeft", this::turnLeft);

        body = physicsComponent.getBody();
        body.setLinearDamping(0); //prevents the ship from stopping for no physical reason
        //body.setFixedRotation(false);

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
        this.currentVelocity = this.flyDirection.cpy();
        body.applyForceToCenter(this.currentVelocity.scl(this.currentAcceleration), true);
        this.playAnimation(body.getLinearVelocity());


        //scl(scalar) basically multiply the Vector2 velocity of body by a scalar. Belongs to Vector2.
        //body.applyForceToCenter(this.currentVelocity.scl(this.currentAcceleration), true);
        //body.applyLinearImpulse(this.currentVelocity.scl(this.currentAcceleration), body.getWorldCenter(), true);
    }

    /**
     * Moves the ship towards a given direction.
     *
     * @param direction direction to move in
     */
    void fly(Vector2 direction) {
        this.flyDirection = direction;
        //this.currentVelocity = this.flyDirection.cpy();
        //body.applyForceToCenter(this.currentVelocity.scl(this.currentAcceleration * 5), true);
        moving = true;
    }

    void turnLeft() {
        body.applyTorque(2, true);
        moving = true;
    }

    /**
     * BOOST
     */
    void thrustersOn(Vector2 direction) {
        this.flyDirection = direction;
        //this.currentVelocity = this.flyDirection.cpy();
        //body.applyForceToCenter(this.currentVelocity.scl(this.currentAcceleration), true);
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
     * Apply auto brake to ship, basically slowing the ship down to stop
     * Activates when B key on keyboard is pressed
     */
    void brakeOn() {

        this.body.setLinearDamping(2);
    }

    /**
     * Removes the brake, ship will not automatically slow down
     * Activates when V key on keyboard is pressed
     */
    void brakeOff() {

        this.body.setLinearDamping(0);
    }

    void playAnimation(Vector2 direction) {
        //double currentOrientation = Vector2Utils.angleTo(direction);
        double currentOrientation = Vector2Utils.angleTo(direction);

        if (currentOrientation == up) {
            //
            animator.startAnimation("Ship_UpStill");
        } else if (currentOrientation == down) {
            //
            animator.startAnimation("Ship_DownStill");
        } else if (currentOrientation == right) {
            //
            animator.startAnimation("Ship_RightStill");
        } else if (currentOrientation == left || currentOrientation == left2) {
            //
            animator.startAnimation("Ship_LeftStill");
        } else if (currentOrientation < 90 && currentOrientation > 0) {
            //
            animator.startAnimation("Ship_RightUp");
        } else if (currentOrientation > 90 && currentOrientation < 180) {
            //
            animator.startAnimation("Ship_LeftUp");
        } else if (currentOrientation > -180 && currentOrientation < -90) {
            //
            animator.startAnimation("Ship_LeftDown");
        } else if (currentOrientation > -90 && currentOrientation < 0) {
            //
            animator.startAnimation("Ship_RightDown");
        }


    }


}
