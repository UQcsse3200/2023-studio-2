package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Controls the animation of an astronaut NPC using LibGDX's AnimationRenderComponent.
 */
public class AstronautAnimationController extends Component {
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    float animationDuration = 10f; // Adjust this to control animation speed.

    /**
     * Creates a new AstronautAnimationController with an AssetManager for managing assets.
     */
    public AstronautAnimationController() {}

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);

        // Start with the default animation.
        animator.startAnimation("Astro_Stand");
    }

    @Override
    public void dispose() {
        super.dispose();
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
                case "Astro_Left" -> animator.startAnimation("Astro_Left");
                case "Astro_Right" -> animator.startAnimation("Astro_Right");
                default ->
                    // Default to the bottom animation if not in any specific state.
                        animator.startAnimation("Astro_Stand");
            }
        }
    }
}
