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

        entity.getEvents().addListener("walkStop", this::animateStandDown);
        /*
        entity.getEvents().addListener("standDown", this::animateStandDown);
        entity.getEvents().addListener("standLeft", this::animateStandLeft);
        entity.getEvents().addListener("standRight", this::animateStandRight);
        entity.getEvents().addListener("standUp", this::animateStandUp);*/
        entity.getEvents().addListener("dodgeDown", this::animateDodgeDown);
        entity.getEvents().addListener("dodgeLeft", this::animateDodgeLeft);
        entity.getEvents().addListener("dodgeRight", this::animateDodgeRight);
        entity.getEvents().addListener("dodgeUp", this::animateDodgeUp);
    }

    void animateLeft() {
        animator.startAnimation("Character_Left");
    }
    void animateRight() {
        animator.startAnimation("Character_Right");
    }
    void animateUp() {
        animator.startAnimation("Character_Up");
    }
    void animateDown() {
        animator.startAnimation("Character_Down");
    }
    void animateUpLeft() {
        animator.startAnimation("Character_UpLeft");
    }
    void animateUpRight() {
        animator.startAnimation("Character_UpRight");
    }
    void animateDownLeft() {
        animator.startAnimation("Character_DownLeft");
    }
    void animateDownRight() {
        animator.startAnimation("Character_DownRight");
    }

    void animateStandDown() {
        animator.startAnimation("Character_StandDown");
    }
    void animateStandLeft() {
        animator.startAnimation("Character_StandLeft");
    }
    void animateStandRight() {
        animator.startAnimation("Character_StandRight");
    }
    void animateStandUp() {
        animator.startAnimation("Character_StandUp");
    }

    void animateDodgeDown() {
        animator.startAnimation("Character_RollDown");
    }
    void animateDodgeLeft() {
        animator.startAnimation("Character_RollLeft");
    }
    void animateDodgeRight() {
        animator.startAnimation("Character_RollRight");
    }
    void animateDodgeUp() {
        animator.startAnimation("Character_RollUp");
    }
}