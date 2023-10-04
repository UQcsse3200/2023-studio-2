package com.csse3200.game.components.structures;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.FOVComponent;
import com.csse3200.game.entities.buildables.Turret;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class TurretAnimationController extends Component {
    AnimationRenderComponent animator;

/**
     * Adds action listener to the turret
     */
    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        this.animator.startAnimation("normal");
        entity.getEvents().addListener("updateShoot", this::updateShoot);
    }

    /**
     * Initialise animation
     */
    void updateShoot(){
        this.animator.startAnimation("normal");
    }
}
