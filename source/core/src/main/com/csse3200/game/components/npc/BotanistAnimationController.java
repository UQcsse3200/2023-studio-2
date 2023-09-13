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

        TextureAtlas.AtlasRegion left1 = atlas.findRegion("row-1-column-1");
        TextureAtlas.AtlasRegion  left2 = atlas.findRegion("row-1-column-2");

        TextureAtlas.AtlasRegion right1 = atlas.findRegion("row-1-column-3");
        TextureAtlas.AtlasRegion right2 = atlas.findRegion("row-1-column-4");

        TextureAtlas.AtlasRegion bottom1 = atlas.findRegion("row-1-column-5");
        TextureAtlas.AtlasRegion bottom2 = atlas.findRegion("row-1-column-6");

        TextureAtlas.AtlasRegion top1 = atlas.findRegion("row-1-column-7");
        TextureAtlas.AtlasRegion top2 = atlas.findRegion("row-1-column-8");


        // Initialize the animation sequences.
        leftAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , left1,left2);
        rightAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , right1, right2);
        bottomAnimation = new Animation<TextureAtlas.AtlasRegion>(1f , bottom1,bottom2);
        upAnimation = new Animation<TextureAtlas.AtlasRegion>(1f ,top1,top2 );

        // Start with the default animation.
        animator.startAnimation("row-1-column-1");
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
                case "row-1-column-1" -> animator.startAnimation("row-1-column-2");
                case "row-1-column-2" -> animator.startAnimation("row-1-column-3");
                case "row-1-column-3" -> animator.startAnimation("row-1-column-4");
                case "row-1-column-4" -> animator.startAnimation("row-1-column-5");
                case "row-1-column-5" -> animator.startAnimation("row-1-column-6");
                case "row-1-column-6" -> animator.startAnimation("row-1-column-7");
                case "row-1-column-7" -> animator.startAnimation("row-1-column-8");
                case "row-1-column-8" -> animator.startAnimation("row-1-column-1");
                default ->
                    // Default to the bottom animation if not in any specific state.
                        animator.startAnimation("row-1-column-1");
            }
        }
    }

}