package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a chicken entity's state and plays the animation when one
 * of the events is triggered.
 * TODO: figure out if we need only BotanistAnimationController
 */
public class BotanistAnimationController extends Component {
    AnimationRenderComponent animator;
    String direction = "right";

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener("changeDirection", this::setDirection);
        entity.getEvents().addListener("walk", this::animateWander);
        entity.getEvents().addListener("idle", this::animateIdle);
    }

    void animateWander() {
        animator.startAnimation("walk_" + direction);
    }

    void animateIdle() { animator.startAnimation("idle_" + direction);}

    void setDirection(String direction){
        this.direction = direction;
    }
}