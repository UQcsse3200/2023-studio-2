package com.csse3200.game.components.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.player.WeaponComponent;
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
    /* Reference to player entity */
    protected final Entity player;

    /**
     * Class to store variables of a spawned weapon
     */
    public WeaponControllerComponent(WeaponConfig config, float attackDirection, Entity player) {
        this.player = player;
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
        if (--this.remainingDuration <= 0) {
            despawn();
        }
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
     *
     * @param animator - animation component for the entity
     */
    protected abstract void initial_animation(AnimationRenderComponent animator);

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
    private void despawn() {
        AnimationRenderComponent animator = entity.getComponent(AnimationRenderComponent.class);
        if (animator != null) {
            entity.getEvents().trigger("playSound", "stop");
            animator.stopAnimation();
        }
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