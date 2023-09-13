//package com.csse3200.game.components.npc;
//
//import com.badlogic.gdx.assets.AssetManager;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
//import com.csse3200.game.components.Component;
//import com.csse3200.game.rendering.AnimationRenderComponent;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//public class BotanistAnimationControllerTest {
//
//    private BotanistAnimationController animationController;
//    private AnimationRenderComponent animator;
//    private AssetManager assetManager;
//    private TextureAtlas atlas;
//
//    @Before
//    public void setUp() {
//        animator = Mockito.mock(AnimationRenderComponent.class);
//        assetManager = new AssetManager();
//        atlas = new TextureAtlas(); // You can mock this if needed
//        animationController = new BotanistAnimationController(assetManager, animator, atlas);
//        animationController.create();
//    }
//
//    @Test
//    public void testDefaultAnimation() {
//        // Ensure the default animation is set correctly
//        Mockito.verify(animator).startAnimation("oldman_down_1");
//    }
//
//    @Test
//    public void testUpdateAnimation() {
//        // Test updating the animation
//        animationController.update();
//
//        // Verify that the animator's startAnimation method is called with a valid animation name
//        Mockito.verify(animator, Mockito.atLeastOnce()).startAnimation(Mockito.anyString());
//    }
//}
