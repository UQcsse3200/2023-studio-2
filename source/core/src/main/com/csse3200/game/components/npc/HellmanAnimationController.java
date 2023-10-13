package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class HellmanAnimationController extends Component {
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    float animationDuration = 10f; // Adjust this to control animation speed.

    public HellmanAnimationController() {}

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        // Start with the default animation.
        animator.startAnimation("row-1-column-1");
    }

    @Override
    public void update() {
        super.update();

        // Update the animation based on a timer.
        animationTimer += 0.1f;

        if (animationTimer >= animationDuration) {
            animationTimer = 0f; // Reset the timer.

            // Switch between animations.
            switch (animator.getCurrentAnimation()) {
                case "row-1-column-1" -> animator.startAnimation("row-1-column-2");
                case "row-1-column-2" -> animator.startAnimation("row-1-column-3");
                case "row-1-column-3" -> animator.startAnimation("row-1-column-4");
                case "row-1-column-4" -> animator.startAnimation("row-1-column-5");
                case "row-1-column-5" -> animator.startAnimation("row-1-column-6");
                case "row-1-column-6" -> animator.startAnimation("row-1-column-7");
                case "row-1-column-7" -> animator.startAnimation("row-1-column-8");
                case "row-1-column-8" -> animator.startAnimation("row-1-column-9");
                case "row-1-column-9" -> animator.startAnimation("row-1-column-10");
                case "row-1-column-10" -> animator.startAnimation("row-1-column-11");
                case "row-1-column-11" -> animator.startAnimation("row-1-column-12");
                case "row-1-column-12" -> animator.startAnimation("row-1-column-13");
                case "row-1-column-13" -> animator.startAnimation("row-1-column-14");
                case "row-1-column-14" -> animator.startAnimation("row-1-column-1");
                default ->
                        animator.startAnimation("row-1-column-1");
            }
        }
    }
}
