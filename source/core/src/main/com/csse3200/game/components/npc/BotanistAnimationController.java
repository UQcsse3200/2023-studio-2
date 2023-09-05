package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class BotanistAnimationController extends Component {
    private final AssetManager assetManager;
    private AnimationRenderComponent animator;
    private TextureAtlas atlas;

    private Animation<TextureAtlas.AtlasRegion> leftAnimation;
    private Animation<TextureAtlas.AtlasRegion> rightAnimation;
    private Animation<TextureAtlas.AtlasRegion> bottomAnimation;
    private Animation<TextureAtlas.AtlasRegion> upAnimation;

    private float animationTimer = 0f;
    private float animationDuration = 10f; // Adjust this to control animation speed.

    public BotanistAnimationController() {
        this.assetManager = new AssetManager();
    }

    @Override
    public void create() {
        super.create();

        // Initialize your animator and load the atlas.
        animator = entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/botanist.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        atlas = assetManager.get("images/botanist.atlas");

        TextureAtlas.AtlasRegion left1 = atlas.findRegion("oldman_left_1");
        TextureAtlas.AtlasRegion  left2 = atlas.findRegion("oldman_left_2");

        TextureAtlas.AtlasRegion right1 = atlas.findRegion("oldman_right_1");
        TextureAtlas.AtlasRegion right2 = atlas.findRegion("oldman_right_2");

        TextureAtlas.AtlasRegion bottom1 = atlas.findRegion("oldman_down_1");
        TextureAtlas.AtlasRegion bottom2 = atlas.findRegion("oldman_down_2");

        TextureAtlas.AtlasRegion top1 = atlas.findRegion("oldman_up_1");
        TextureAtlas.AtlasRegion top2 = atlas.findRegion("oldman_up_2");


        // Initialize the animation sequences.
        leftAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , left1,left2);
        rightAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , right1, right2);
        bottomAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , bottom1,bottom2);
        upAnimation = new Animation<TextureAtlas.AtlasRegion>(1f ,top1,top2 );

        // Start with the default animation.
        animator.startAnimation("oldman_down_1");
    }
    @Override
    public void update() {
        super.update();

//         Update the animation based on a timer.
        animationTimer += 0.1f;

        if (animationTimer >= animationDuration) {
            animationTimer = 0f; // Reset the timer.

            // Switch between animations.
            switch (animator.getCurrentAnimation()) {
                case "oldman_left_1":
                    animator.startAnimation("oldman_up_1");
                    break;
                case "oldman_right_1":
                    animator.startAnimation("oldman_down_1");
                    break;
                case "oldman_down_1":
                    animator.startAnimation("oldman_left_1");
                    break;
                case "oldman_up_1":
                    animator.startAnimation("oldman_right_1");
                    break;
                default:
                    // Default to the bottom animation if not in any specific state.
                    animator.startAnimation("oldman_down_1");
                    break;
            }
        }
    }

}