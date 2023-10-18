package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Animation controller for the tutorial NPC
 */
public class TutnpcAnimationController extends Component {
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    float animationDuration = 10f; // Adjust this to control animation speed.

    /**
     * Initialise the animation controller
     */
    public TutnpcAnimationController() {}

    /**
     * Create the animation controller and start animations
     */
    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);

        // Start with the default animation.
        animator.startAnimation("Tut_Down");
    }

    /**
     * Updates the animation
     */
    @Override
    public void update() {
        super.update();

        // Update the animation based on a timer.
        animationTimer += 0.2f;

        if (animationTimer >= animationDuration) {
            animationTimer = 0f; // Reset the timer.

            // Switch between animations.
            switch (animator.getCurrentAnimation()) {
                case "Tut_Down" -> animator.startAnimation("Tut_DownRight");
                case "Tut_DownRight" -> animator.startAnimation("Tut_Right");
                case "Tut_Right" -> animator.startAnimation("Tut_UpRight");
                case "Tut_UpRight" -> animator.startAnimation("Tut_Up");
                case "Tut_Up" -> animator.startAnimation("Tut_UpLeft");
                case "Tut_UpLeft" -> animator.startAnimation("Tut_Left");
                case "Tut_Left" -> animator.startAnimation("Tut_DownLeft");
                default ->
                        animator.startAnimation("Tut_Down");
            }
        }
    }
}
