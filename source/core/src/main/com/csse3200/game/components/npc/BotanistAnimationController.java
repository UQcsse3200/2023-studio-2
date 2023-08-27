package com.csse3200.game.components.npc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class BotanistAnimationController extends Component {
    private AssetManager assetManager;

    public BotanistAnimationController() {
        this.assetManager = new AssetManager();
    }
    private AnimationRenderComponent animator;

    // Animation frames
    private Texture left1, left2;
    private Texture right1, right2;
    private Texture bottom1, bottom2;
    private Texture top1, top2;


    @Override
    public void create() {

        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        assetManager.load("images/oldman_left_1.png", Texture.class);
        assetManager.load("images/oldman_left_2.png", Texture.class);
        assetManager.load("images/oldman_right_1.png", Texture.class);
        assetManager.load("images/oldman_right_2.png", Texture.class);
        assetManager.load("images/oldman_down_1.png", Texture.class);
        assetManager.load("images/oldman_down_2.png", Texture.class);
        assetManager.load("images/oldman_up_1.png", Texture.class);
        assetManager.load("images/oldman_up_2.png", Texture.class);
        assetManager.load("images/botanist.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        left1 = assetManager.get("images/oldman_left_1.png");
        left2 = assetManager.get("images/oldman_left_2.png");

        right1 = assetManager.get("images/oldman_right_1.png");
        right2 = assetManager.get("images/oldman_right_2.png");

        bottom1 = assetManager.get("images/oldman_down_1.png");
        bottom2 = assetManager.get("images/oldman_down_2.png");

        top1 = assetManager.get("images/oldman_up_1.png");
        top2 = assetManager.get("images/oldman_up_2.png");

        Animation leftAnimation = new Animation(0.1f, left1, left2);
        Animation rightAnimation = new Animation(0.1f, right1, right2);
        Animation bottomAnimation = new Animation(0.1f, bottom1, bottom2);
        Animation upAnimation = new Animation(0.1f, top1, top2);

        animator = new AnimationRenderComponent(assetManager.get("images/botanist.atlas", TextureAtlas.class));


        float leftDuration = leftAnimation.getAnimationDuration();
        animator.addAnimation("angry_float", leftDuration, Animation.PlayMode.LOOP);

        float rightDuration = rightAnimation.getAnimationDuration();
        animator.addAnimation("angry_float", rightDuration, Animation.PlayMode.LOOP);
        float bottomDuration = bottomAnimation.getAnimationDuration();
        animator.addAnimation("float", bottomDuration, Animation.PlayMode.LOOP);
        float topDuration = upAnimation.getAnimationDuration();
        animator.addAnimation("default", topDuration, Animation.PlayMode.LOOP);

    }
}
