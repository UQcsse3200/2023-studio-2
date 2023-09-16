package com.csse3200.game.components.structures;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExtractorAnimationControllerTest {

    private ExtractorAnimationController extractorController;
    private AnimationRenderComponent animator;
    private CombatStatsComponent healthStats;

    @Before
    public void setUp() {
        extractorController = new ExtractorAnimationController();
        animator = Mockito.mock(AnimationRenderComponent.class);
        healthStats = Mockito.mock(CombatStatsComponent.class);

        extractorController.animator = animator;
        extractorController.entity = new Entity();
        extractorController.entity.addComponent(healthStats);
    }

    @Test
    public void testExtractingAnimation() {
        // Sets up scenario where extractor is not broken
        Mockito.when(healthStats.isDead()).thenReturn(false);

        // Sets health (which does not do anything) to 0 as a placeholder value
        extractorController.updatingHealth(0);
        Mockito.verify(animator).startAnimation("animateExtracting");
    }

    @Test
    public void testBrokenAnimation() {
        // Sets up scenario where extractor is broken
        Mockito.when(healthStats.isDead()).thenReturn(true);

        // Sets health (which does not do anything) to 0 as a placeholder value
        extractorController.updatingHealth(0);
        Mockito.verify(animator).startAnimation("animateBroken");
    }
}
