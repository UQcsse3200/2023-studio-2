package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * The AstroAnimationController manages the animation of an astronaut NPC in the game.
 * It loads the required assets, updates the animation, and controls its speed and direction.
 */
public class AstroAnimationController extends Component {
    private final AssetManager assetManager;
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    float animationTimer = 0f;
    float animationDuration = 10f; // Adjust this to control animation speed.

    /**
     * Creates a new AstroAnimationController with an AssetManager for managing assets.
     *
     * @param assetManager The AssetManager used to load and manage game assets.
     */
    public AstroAnimationController(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/npc/Astro_NPC.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("images/npc/Astro_NPC.atlas");

        // Start with the default animation.
        animator.startAnimation("Astro_Down");
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
                case "Astro_Down" -> animator.startAnimation("Astro_DownRight");
                case "Astro_DownRight" -> animator.startAnimation("Astro_Right");
                case "Astro_Right" -> animator.startAnimation("Astro_UpRight");
                case "Astro_UpRight" -> animator.startAnimation("Astro_Up");
                case "Astro_Up" -> animator.startAnimation("Astro_UpLeft");
                case "Astro_UpLeft" -> animator.startAnimation("Astro_Left");
                case "Astro_Left" -> animator.startAnimation("Astro_DownLeft");
                default ->
                        animator.startAnimation("Astro_Down");
            }
        }
    }
}
