package com.csse3200.game.components.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.SoundComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class to control the movement of weapons that have been spawned
 */
public abstract class WeaponControllerComponent extends Component {
    protected WeaponConfig config;
    /* Variable to hold the remaining life (in game ticks) of a weapon */
    protected float remainingDuration;
    /* The rotation of the weapon indicating its "forward" direction*/
    protected float currentRotation;
    /* Reference to player entity */
    protected final Entity player;
    /* attack Number in sequence */
    protected int attackNum = 0;
    /* game time */
    private final GameTime timeSource;

    /* animator used to control visual*/
    protected AnimationRenderComponent animator = null;
    /* SOund component*/
    protected SoundComponent soundComp = null;

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponConfig config, float attackDirection, Entity player, int attackNum) {
        this.player = player;
        this.config = config;
        this.remainingDuration = config.weaponDuration;
        this.currentRotation = attackDirection;
        this.attackNum = attackNum;
        timeSource = ServiceLocator.getTimeSource();
    }


    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponConfig config, float attackDirection, Entity player) {
        this.player = player;
        this.config = config;
        this.remainingDuration = config.weaponDuration;
        this.currentRotation = attackDirection;
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Create class - implemented class implement initial weapon setup including adding required animations
     * and controlling initial movement direction and animation
     */
    @Override
    public void create() {
        this.entity.getEvents().addListener("death", this::setDuration);
        initial_rotation();
        initial_position();

        set_animations();
        initial_animation();

        set_sound();
    }

    /**
     * Function to control projectile movement once it has spawned
     */
    @Override
    public void update() {
        rotate();
        move();
        reanimate();

        remainingDuration -= timeSource.getDeltaTime() * 100;
        if (this.remainingDuration <= 0) {
            despawn();
        }
    }

    public int get_spawn_delay() {
        return 0;
    }

    /**
     * Function to add animations based on standard animation configurations (Overidable)
     */
    protected void set_animations() {
        animator = entity.getComponent(AnimationRenderComponent.class);
        animator.addAnimation("ATTACK", 0.07f, Animation.PlayMode.NORMAL);
    }

    /**
     * Function to initlise sound component
     */
    protected void set_sound() {
        SoundComponent sComp = entity.getComponent(SoundComponent.class);
        if (sComp.checkExists("start")) {
            sComp.playSound("start");
        }

    }

    /**
     * Set initial rotation (defaults to attack direction)
     */
    protected abstract void initial_rotation();

    /**
     * Set initial position
     */
    protected abstract void initial_position();

    /**
     * Set intial animation
     */
    protected abstract void initial_animation();

    /**
     * Update entity rotation every tick
     */
    protected abstract void rotate();

    /**
     * Update entity location every tick
     */
    protected abstract void move();

    /**
     * Update entity animation if required
     */
    protected abstract void reanimate();

    /**
     * respawn a weapon when it runs out of duration or health
     */
    protected void despawn() {
        //entity.getEvents().trigger("playSound", "stop");
        animator.stopAnimation();
        Gdx.app.postRunnable(entity::dispose);
    }

    /**
     * Sets the remaining duration
     *
     * @param duration duration to set reamining duration
     */
    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /** Returns the current weapons config **/
    public WeaponConfig getConfig() {
        return config;
    }

    /**
     * Returns the game position in a given direction at a given distance relative to the player
     * to center a given attack entity
     *
     * @param direction direction the position should be in
     * @param distance  distance infront of the player
     * @return position in the given direction at the distance
     */
    protected Vector2 positionInDirection(float direction, float distance) {
        var radians = Math.toRadians(direction);
        return new Vector2((float) Math.cos(radians), (float) Math.sin(radians)).scl(distance);
    }
}