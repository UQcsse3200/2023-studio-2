package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class JailAnimationController extends Component {
    private final AssetManager assetManager;
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    private float animationDuration = 10f; // Adjust this to control animation speed.

    public JailAnimationController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/Jail/jail.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("images/Jail/jail.atlas");

        // Start with the default animation.
        animator.startAnimation("jail_close");
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
                case "jail_close" -> animator.startAnimation("jail_open");

                default ->
                        animator.startAnimation("jail_close");
            }
        }
    }
}
