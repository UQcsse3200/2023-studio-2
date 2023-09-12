package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a player's state and plays the animation when one
 * of the events is triggered.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Adds event listener to the entity to change the animations
     */
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
        entity.getEvents().addListener("walkStopAnimation", this::animateStop);
        entity.getEvents().addListener("dodgeDown", this::animateDodgeDown);
        entity.getEvents().addListener("dodgeLeft", this::animateDodgeLeft);
        entity.getEvents().addListener("dodgeRight", this::animateDodgeRight);
        entity.getEvents().addListener("dodgeUp", this::animateDodgeUp);
        entity.getEvents().addListener("playerDeath", this::animateDeath);
    }

    /**
     * Initialise walk left animation
     */
    void animateLeft() {
        animator.startAnimation("Character_Left");
    }

    /**
     * Initialise walk right animation
     */
    void animateRight() {
        animator.startAnimation("Character_Right");
    }

    /**
     * Initialise walk up animation
     */
    void animateUp() {
        animator.startAnimation("Character_Up");
    }

    /**
     * Initialise walk down animation
     */
    void animateDown() {
        animator.startAnimation("Character_Down");
    }

    /**
     * Initialise walk up-left animation
     */
    void animateUpLeft() {
        animator.startAnimation("Character_UpLeft");
    }

    /**
     * Initialise walk up-right animation
     */
    void animateUpRight() {
        animator.startAnimation("Character_UpRight");
    }

    /**
     * Initialise walk down-left animation
     */
    void animateDownLeft() {
        animator.startAnimation("Character_DownLeft");
    }

    /**
     * Initialise walk down-right animation
     */
    void animateDownRight() {
        animator.startAnimation("Character_DownRight");
    }

    /**
     * Initialise stand facing down animation
     */
    void animateStop(Vector2 lastDirection) {
        if (lastDirection.x < -0.1) {
            animator.startAnimation("Character_StandLeft");
        } else if (lastDirection.x > 0.1) {
            animator.startAnimation("Character_StandRight");
        } else if (lastDirection.y > 0.1) {
            animator.startAnimation("Character_StandUp");
        } else if (lastDirection.y < -0.1){
            animator.startAnimation("Character_StandDown");
        }
    }

    /**
     * Initialise stand facing left animation
     */
    void animateStandLeft() {
        animator.startAnimation("Character_StandLeft");
    }

    /**
     * Initialise stand facing right animation
     */
    void animateStandRight() {
        animator.startAnimation("Character_StandRight");
    }

    /**
     * Initialise stand facing up animation
     */
    void animateStandUp() {
        animator.startAnimation("Character_StandUp");
    }

    /**
     * Initialise dodge down animation
     */
    void animateDodgeDown() {
        animator.startAnimation("Character_RollDown");
    }

    /**
     * Initialise dodge left animation
     */
    void animateDodgeLeft() {
        animator.startAnimation("Character_RollLeft");
    }

    /**
     * Initialise dodge right animation
     */
    void animateDodgeRight() {
        animator.startAnimation("Character_RollRight");
    }

    /**
     * Initialise dodge up animation
     */
    void animateDodgeUp() {
        animator.startAnimation("Character_RollUp");
    }
    void animateDeath() {
        animator.startAnimation("Character_Death");
        System.out.println("death");
    }
}