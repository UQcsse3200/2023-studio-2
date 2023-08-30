package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft", this::animateLeft);
        entity.getEvents().addListener("walkRight", this::animateRight);
        entity.getEvents().addListener("walkUp", this::animateUp);
        entity.getEvents().addListener("walkDown", this::animateDown);
        entity.getEvents().addListener("walkUpLeft", this::animateUpLeft);
        entity.getEvents().addListener("walkUpRight", this::animateUpRight);
        entity.getEvents().addListener("walkDownLeft", this::animateDownLeft);
        entity.getEvents().addListener("walkDownRight", this::animateDownRight);
    }

    void animateLeft() {
        animator.startAnimation("LEFT");
    }
    void animateRight() {
        animator.startAnimation("RIGHT");
    }
    void animateUp() {
        animator.startAnimation("UP");
    }
    void animateDown() {
        animator.startAnimation("DOWN");
    }
    void animateUpLeft() {
        animator.startAnimation("UP_LEFT");
    }
    void animateUpRight() {
        animator.startAnimation("UP_RIGHT");
    }
    void animateDownLeft() {
        animator.startAnimation("DOWN_LEFT");
    }
    void animateDownRight() {
        animator.startAnimation("DOWN_RIGHT");
    }
}