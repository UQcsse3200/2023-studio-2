package com.csse3200.game.components.ships;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;




/**
 * This class listens to events relevant to a ship's state and plays the animation
 * when one of the events is triggered
 */
public class ShipAnimationController extends Component {
    private AnimationRenderComponent animator;

    /**
     * Changes animation based on ship velocity
     */
    @Override
    public void create() {
        super.create();

        animator = this.getEntity().getComponent(AnimationRenderComponent.class);
        //Bugged, TBD
        //entity.getEvents().addListener("boostRight", this::animateLeft);
    }

    /**
     * Initialise boost to left animation
     */
    void animateLeft() {
        animator.startAnimation("Ship_LeftStill");
    }
}