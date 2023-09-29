package com.csse3200.game.components.Car;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;


public class CarAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("stopMoving", this::animateStopMoving);
        entity.getEvents().addListener("startMoving", this::animateMoving);
        entity.getEvents().addListener("car", this::animateIdle);
        animator.startAnimation("car_right");
    }
    void animateStopMoving(String direction, String tool) {
        String animation = String.format("car_stop_%s_%s", direction, tool);
        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }
    void animateMoving(String direction, String tool) {
        String animation = String.format("car_move_%s_%s", direction, tool);
        if (!animator.getCurrentAnimation().equals(animation)) {
            animator.startAnimation(animation);
        }
    }
    void animateIdle(String direction) {
        animator.startAnimation(String.format("car_%s", direction));
    }
}
