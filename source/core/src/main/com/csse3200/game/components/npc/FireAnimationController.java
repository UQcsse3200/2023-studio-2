package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class FireAnimationController extends Component {
        private final AssetManager assetManager;
        private AnimationRenderComponent animator;
        private TextureAtlas atlas;

        private float animationTimer = 0f;
        private float animationDuration = 10f; // Adjust this to control animation speed.

    public FireAnimationController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
        public void create() {
            super.create();

            // Initialize your animator and load the atlas.
            animator = entity.getComponent(AnimationRenderComponent.class);
            assetManager.load("images/minigame/fire.atlas", TextureAtlas.class);
            assetManager.finishLoading();
            atlas = assetManager.get("images/minigame/fire.atlas");

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
