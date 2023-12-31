/**
 * The `BotanistAnimationController` class is responsible for controlling and updating
 * animations for a botanist character in the game.
 */
package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class BotanistAnimationController extends Component {
    private AnimationRenderComponent animator;

    float animationTimer = 0f;

    /**
     * Creates a new BotanistAnimationController with an AssetManager for managing assets.
     */
    public BotanistAnimationController() {}

    @Override
    public void create() {
        super.create();

        // Initialize your animator
        animator = entity.getComponent(AnimationRenderComponent.class);

        // Start with the default animation.
        animator.startAnimation("row-1-column-1");
    }

    @Override
    public void update() {
        super.update();

        // Update the animation based on a timer.
        animationTimer += 0.1f;

        // Adjust this to control animation speed.
        float animationDuration = 10f;
        if (animationTimer >= animationDuration) {
            animationTimer = 0f; // Reset the timer.

            // Switch between animations.
            switch (animator.getCurrentAnimation()) {
                case "row-1-column-1" -> animator.startAnimation("row-1-column-2");
                case "row-1-column-2" -> animator.startAnimation("row-1-column-3");
                case "row-1-column-3" -> animator.startAnimation("row-1-column-4");
                case "row-1-column-4" -> animator.startAnimation("row-1-column-5");
                case "row-1-column-5" -> animator.startAnimation("row-1-column-6");
                case "row-1-column-6" -> animator.startAnimation("row-2-column-1");
                case "row-2-column-1" -> animator.startAnimation("row-2-column-2");
                case "row-2-column-2" -> animator.startAnimation("row-2-column-3");
                case "row-2-column-3" -> animator.startAnimation("row-2-column-4");
                case "row-2-column-4" -> animator.startAnimation("row-2-column-5");
                case "row-2-column-5" -> animator.startAnimation("row-2-column-6");
                case "row-2-column-6" -> animator.startAnimation("row-3-column-1");
                case "row-3-column-1" -> animator.startAnimation("row-3-column-2");
                case "row-3-column-2" -> animator.startAnimation("row-3-column-3");
                case "row-3-column-3" -> animator.startAnimation("row-3-column-4");
                case "row-3-column-4" -> animator.startAnimation("row-4-column-1");
                case "row-4-column-1" -> animator.startAnimation("row-4-column-2");
                case "row-4-column-2" -> animator.startAnimation("row-4-column-3");
                case "row-4-column-3" -> animator.startAnimation("row-1-column-1");

                default ->
                    // Default to the bottom animation if not in any specific state.
                        animator.startAnimation("row-1-column-1");
            }
        }
    }
}
