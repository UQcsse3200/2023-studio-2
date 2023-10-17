package com.csse3200.game.components.companion;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class represents the animation controller component for a companion entity.
 * It handles animations for both the companion and an accompanying infant companion.
 */
public class CompanionAnimationController extends Component {
    AnimationRenderComponent animator;
    AnimationRenderComponent infanimator;

    /**
     * Creates the CompanionAnimationController and sets up event listeners for animations.
     */
    @Override
    public void create() {
        super.create();

        // Initialize infant companion animation renderer and set event listeners
       /* infanimator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft1", this::infanimateLeft);
        entity.getEvents().addListener("walkRight1", this::infanimateRight);
        entity.getEvents().addListener("walkUp1", this::infanimateUp);
        entity.getEvents().addListener("walkDown1", this::infanimateDown);*/

        // Initialize companion animation renderer and set event listeners
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft", this::animateLeft);
        entity.getEvents().addListener("walkRight", this::animateRight);
        entity.getEvents().addListener("walkUp", this::animateUp);
        entity.getEvents().addListener("walkDown", this::animateDown);
        entity.getEvents().addListener("walkUpLeft", this::animateUpLeft);
        entity.getEvents().addListener("walkUpRight", this::animateUpRight);
        entity.getEvents().addListener("walkDownLeft", this::animateDownLeft);
        entity.getEvents().addListener("walkDownRight", this::animateDownRight);
        entity.getEvents().addListener("walkStopAnimation", this::animateStop);
       /* entity.getEvents().addListener("walkStopAnimation", this::animateStop);*/
    }

    /**
     * Start the animation for moving the infant companion to the left.
     */
    /*void infanimateLeft() {
        infanimator.startAnimation("LEFT_1");
    }

    *//**
     * Start the animation for moving the infant companion to the right.
     *//*
    void infanimateRight() {
        infanimator.startAnimation("RIGHT_1");
    }

    *//**
     * Start the animation for moving the infant companion up.
     *//*
    void infanimateUp() {
        infanimator.startAnimation("UP_1");
    }

    *//**
     * Start the animation for moving the infant companion down.
     *//*
    void infanimateDown() {
        infanimator.startAnimation("DOWN_1");
    }*/

    /**
     * Start the animation for moving the companion to the left.
     */
    void animateLeft() {
        animator.startAnimation("Companion_Left");
    }

    /**
     * Start the animation for moving the companion to the right.
     */
    void animateRight() {
        animator.startAnimation("Companion_Right");
    }

    /**
     * Start the animation for moving the companion up.
     */
    void animateUp() {
        animator.startAnimation("Companion_Up");
    }

    /**
     * Start the animation for moving the companion down.
     */
    void animateDown() {
        animator.startAnimation("Companion_Down");
    }

    /**
     * Start the animation for moving the companion up and to the left.
     */
    void animateUpLeft() {
        animator.startAnimation("Companion_UpLeft");
    }

    /**
     * Start the animation for moving the companion up and to the right.
     */
    void animateUpRight() {
        animator.startAnimation("Companion_UpRight");
    }

    /**
     * Start the animation for moving the companion down and to the left.
     */
    void animateDownLeft() {
        animator.startAnimation("Companion_DownLeft");
    }

    /**
     * Start the animation for moving the companion down and to the right.
     */
    void animateDownRight() {
        animator.startAnimation("Companion_DownRight");
    }
    void animateStop(Vector2 lastDirection) {
        if (lastDirection.x < -0.1) {
            animator.startAnimation("Companion_StandLeft");
        } else if (lastDirection.x > 0.1) {
            animator.startAnimation("Companion_StandRight");
        } else if (lastDirection.y > 0.1) {
            animator.startAnimation("Companion_StandUp");
        } else if (lastDirection.y < -0.1){
            animator.startAnimation("Companion_StandDown");
        }
    }
}
