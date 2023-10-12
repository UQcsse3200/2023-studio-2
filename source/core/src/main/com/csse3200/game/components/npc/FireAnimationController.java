package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * The FireAnimationController manages the animation of fire in a game.
 * It loads the required assets, updates the animation, and controls its speed.
 */
public class FireAnimationController extends Component {
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    private float animationDuration = 10f; // Adjust this to control animation speed.

    /**
     * Creates a new FireAnimationController.
     */
    public FireAnimationController() {}

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);

        // Start with the default animation.
        animator.startAnimation("image_part1");
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
                case "image_part1" -> animator.startAnimation("image_part2");
                case "image_part2" -> animator.startAnimation("image_part3");
                case "image_part3" -> animator.startAnimation("image_part4");
                case "image_part4" -> animator.startAnimation("image_part5");
                default ->
                        animator.startAnimation("image_part1");
            }
        }
    }
}
