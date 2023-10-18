package com.csse3200.game.components.ships;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Action component for interacting with the player. Ship events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class ShipActions extends Component {
    private int maxHealth;
    private int maxFuel;
    private final int currentAcceleration;


    private Body body;
    private Vector2 flyDirection = Vector2.Zero.cpy();
    private boolean moving = false;
    private AnimationRenderComponent animator;

    /**
     * Initialize the health, fuel and acceleration of the ship
     * @param acceleration Magnitude of force applied to the ship
     */
    public ShipActions(int acceleration) {
        //temporary value of health to 1 for testing
        this.maxHealth =  1;
        //temporary value of fuel to 20 for testing
        this.maxFuel = 20;
        this.currentAcceleration = acceleration;
    }

    /**
     * Initialize animation and physics of the ship
     */
    @Override
    public void create() {
        PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("fly", this::fly);
        entity.getEvents().addListener("flystop", this::flystop);
        entity.getEvents().addListener("brakeOn", this::brakeOn);
        entity.getEvents().addListener("brakeOff", this::brakeOff);


        body = physicsComponent.getBody();
        body.setLinearDamping(0); //prevents the ship from stopping for no physical reason
    }

    /**
     * Called each render call. Updates the speed of the ship
     */
    @Override
    public void update() {
        if (moving) {
            if (this.maxHealth <= 0) {
                kaboom();
            }
            updateSpeed();
        }
    }

    /**
     * Updates the speed and animation of the ship
     */
    private void updateSpeed() {

        if (this.maxFuel > 0) {
            boost();
        }
        this.playAnimation(body.getLinearVelocity());
    }

    /**
     * Push the ship towards a given direction
     */
    void boost() {
        Vector2 currentVelocity = this.flyDirection.cpy();
        body.applyForceToCenter(currentVelocity.scl(this.currentAcceleration), true);
    }

    /**
     * Moves the ship towards a given direction.
     *
     * @param direction direction to move in
     */
    void fly(Vector2 direction) {

        //Spends fuel each button press and release
        if (this.maxFuel > 0) {
            this.flyDirection = direction;
            moving = true;
            this.maxFuel -= 1;
            updatedFuel();
        } else {
            noFuel();

        }

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

    /**
     * Given the direction, obtain the angle of the direction vector to the world origin,
     * use the angle to determine the animation representing ship orientation to be played
     * @param direction
     */
    void playAnimation(Vector2 direction) {

        double currentOrientation = Vector2Utils.angleTo(direction);

        //These are used for determining ship orientation by ship velocity direction
        double up = 90;
        double left = 180;
        double down = -90;
        double right = 0;
        double left2 = -180;
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

    /**
     * Changes the max available fuel for the ship
     *
     * @param fuel
     */
    public void setFuel(int fuel) {
        this.maxFuel = fuel;
    }

    /**
     * Changes the max health of the ship
     *
     * @param health
     */
    public void setHealth(int health) {
        this.maxHealth = health;
    }

    /**
     * get the amount of fuel
     * @return maxFuel
     */
    public int getMaxFuel() {return this.maxFuel;}

    /**
     * get the amount of health
     * @return maxHealth
     */
    public int getMaxHealth() {return this.maxHealth;}

    /**
     * Inform ShipStatsDisplay about new health value
     */
    public void updatedHealth() {entity.getEvents().trigger("updateShipHealth");}

    /**
     * Inform ShipStatsDisplay about new fuel value
     */
    public void updatedFuel() {entity.getEvents().trigger("updateShipFuel");}

    /**
     * Inform ShipStatsDisplay about empty fuel tank
     */
    public void noFuel() { entity.getEvents().trigger("noFuel"); }

    /**
     * Inform ShipStatsDisplay ship has gone Kaboom!
     */
    public void kaboom() { entity.getEvents().trigger("Kaboom"); }

    /**
     * Ship hits an obstacle, reduce health by 1
     */
    public void hit() {
        this.maxHealth -= 1;
        this.updatedHealth();
    }



}
