package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class represents a component responsible for controlling the animation of a botanist NPC entity.
 * It listens to relevant events and triggers animations accordingly.
 */
public class BotanistAnimationController extends Component {
    private AnimationRenderComponent animator;
    private String direction = "right";

    /**
     * Called when the component is created. Initializes the animator and sets up event listeners.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Listen for the "changeDirection" event and update the direction.
        entity.getEvents().addListener("changeDirection", this::setDirection);

        // Listen for the "wanderStart" event and trigger the wander animation.
        entity.getEvents().addListener("wanderStart", this::animateWander);

        // Listen for the "idle" event and trigger the idle animation.
        entity.getEvents().addListener("idle", this::animateIdle);
    }

    /**
     * Triggers the animation for wandering in the current direction.
     */
    void animateWander() {
        animator.startAnimation("wanderStart_" + direction);
    }

    /**
     * Triggers the animation for being idle in the current direction.
     */
    void animateIdle() {
        animator.startAnimation("idle_" + direction);
    }

    /**
     * Sets the direction for animation.
     *
     * @param direction The new direction for animation ("left" or "right").
     */
    void setDirection(String direction) {
        this.direction = direction;
    }
}
