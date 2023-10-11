package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * A component responsible for controlling the animation of a jail object.
 */
public class JailAnimationController extends Component {
    private final AssetManager assetManager;
    public AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private float animationTimer = 0f;
    float animationDuration = 10f; // Adjust this to control animation speed.

    /**
     * Creates a new JailAnimationController with an AssetManager for managing assets.
     *
     * @param assetManager The AssetManager used for loading assets.
     */
    public JailAnimationController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void create() {
        super.create();

        // Initialize the animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/Jail/jail.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("images/Jail/jail.atlas");

        // Start with the default animation.
        animator.startAnimation("jail_close");
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
                case "jail_close" -> animator.startAnimation("jail_open");
                default ->
                        animator.startAnimation("jail_close");
            }
        }
    }
}
