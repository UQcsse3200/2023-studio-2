package com.csse3200.game.components.Companion;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class CompanionAnimationController extends Component {
    AnimationRenderComponent animator;


    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
                entity.getEvents().addListener("walkLeft", this::animateLeft);
                entity.getEvents().addListener("walkRight", this::animateRight);
                entity.getEvents().addListener("walkUp", this::animateUp);
                entity.getEvents().addListener("walkDown", this::animateDown);
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
}