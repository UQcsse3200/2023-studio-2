package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class TutnpcAnimationController extends Component {
    private final AssetManager assetManager;
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    private float animationDuration = 10f; // Adjust this to control animation speed.

    public TutnpcAnimationController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/npc/Tutnpc.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("images/npc/Tutnpc.atlas");

        // Start with the default animation.
        animator.startAnimation("row-2-column-1");
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    @Override
    public void update() {
        super.update();

        // Update the animation based on a timer.
        animationTimer += 0.2f;

        if (animationTimer >= animationDuration) {
            animationTimer = 0f; // Reset the timer.

            // Switch between animations.
            switch (animator.getCurrentAnimation()) {
                case "row-2-column-1" -> animator.startAnimation("row-3-column-1");
                case "row-3-column-1" -> animator.startAnimation("row-4-column-1");
                case "row-4-column-1" -> animator.startAnimation("row-2-column-2");
                case "row-2-column-2" -> animator.startAnimation("row-3-column-2");
                case "row-3-column-2" -> animator.startAnimation("row-4-column-2");
                case "row-4-column-2" -> animator.startAnimation("row-2-column-3");
                case "row-2-column-3" -> animator.startAnimation("row-3-column-3");
                case "row-3-column-3" -> animator.startAnimation("row-4-column-3");
                case "row-4-column-3" -> animator.startAnimation("row-2-column-4");
                case "row-2-column-4" -> animator.startAnimation("row-3-column-4");
                case "row-3-column-4" -> animator.startAnimation("row-4-column-4");
                case "row-4-column-4" -> animator.startAnimation("row-2-column-5");
                case "row-2-column-5" -> animator.startAnimation("row-3-column-5");
                case "row-3-column-5" -> animator.startAnimation("row-4-column-5");
                case "row-4-column-5" -> animator.startAnimation("row-2-column-6");
                case "row-2-column-6" -> animator.startAnimation("row-3-column-6");
                case "row-3-column-6" -> animator.startAnimation("row-4-column-6");
                case "row-4-column-6" -> animator.startAnimation("row-2-column-7");
                case "row-2-column-7" -> animator.startAnimation("row-3-column-7");
                case "row-3-column-7" -> animator.startAnimation("row-4-column-7");
                case "row-4-column-7" -> animator.startAnimation("row-2-column-8");
                case "row-2-column-8" -> animator.startAnimation("row-3-column-8");
                case "row-3-column-8" -> animator.startAnimation("row-4-column-8");
                case "row-4-column-8" -> animator.startAnimation("row-2-column-1");
                default ->
                        animator.startAnimation("idle");
            }
        }
    }
}
