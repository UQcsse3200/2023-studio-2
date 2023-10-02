package com.csse3200.game.components.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.WeaponConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Class to control the movement of weapons that have been spawned
 */
public abstract class WeaponControllerComponent extends Component {
    protected WeaponConfig config;
    /* Variable to hold the remaining life (in game ticks) of a weapon */
    protected int remainingDuration;
    /* The rotation of the weapon indicating its "forward" direction*/
    protected float currentRotation;

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponConfig config, float attackDirection) {
        this.config = config;
        this.remainingDuration = config.weaponDuration;
        this.currentRotation = attackDirection;
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

        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator != null) {
            add_animations(animator);
            initial_animation(animator);
        }
    }

    /**
     * Function to control projectile movement once it has spawned
     */
    @Override
    public void update() {
        rotate();
        move();
        reanimate();
        if (--this.remainingDuration <= 0) {despawn();}
    }

    /**
     * Function to add animations based on standard animation configurations (Overidable)
     */
    protected void add_animations(AnimationRenderComponent animator) {
        switch (config.animationType) {
            case 8:
                animator.addAnimation("LEFT3", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT3", 0.07f, Animation.PlayMode.NORMAL);
            case 6:
                animator.addAnimation("LEFT2", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT2", 0.07f, Animation.PlayMode.NORMAL);
            case 4:
                animator.addAnimation("LEFT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("RIGHT1", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("DOWN", 0.07f, Animation.PlayMode.NORMAL);
            default:
                animator.addAnimation("UP", 0.07f, Animation.PlayMode.NORMAL);
                animator.addAnimation("STATIC", 0.07f, Animation.PlayMode.NORMAL);
        }
    }

    protected abstract void initial_rotation();

    protected abstract void initial_position();

    protected abstract void initial_animation(AnimationRenderComponent animator);

    /**
     * Update entity rotation (Still needs to be set) //TODO fix this requirement
     */
    protected abstract void rotate();

    /**
     * Update entity location
     */
    protected abstract void move();

    /**
     * Update entity animation
     */
    protected abstract void reanimate();

    /**
     * Despawn a weapon when it runs out of durtaion or health
     */
    private void despawn() {
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator != null ) {animator.stopAnimation();}
        Gdx.app.postRunnable(entity::dispose);
    }

    /**
     * Sets the remaining duration
     * @param duration duration to set reamining duration
     */
    private void setDuration(int duration) {
        this.remainingDuration = duration;
    }

    /**
     * Returns the game position in a given direction at a given distance relative to the player
     * to center a given attack entity
     * @param direction direction the position should be in
     * @param distance distance infront of the player
     * @return position in the given direction at the distance
     */
    protected Vector2 positionInDirection(float direction, float distance) {
        float radians = (float) Math.toRadians(direction);
        return new Vector2((float) Math.cos(radians), (float) Math.sin(radians)).scl(distance);
    }
}

