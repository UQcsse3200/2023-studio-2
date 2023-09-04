package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link HealthBarComponent} class.
 */
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class HealthBarComponentTest {

    /**
     * Test the updateHealth method of HealthBarComponent.
     */
    @Test
    void updateHealthTest() {
        ServiceLocator.registerRenderService(mock(RenderService.class));

        HealthBarComponent component = new HealthBarComponent(true);
        CombatStatsComponent combatStatsComponent =
                new CombatStatsComponent(100, 0, 1, false);
        Entity entity = new Entity().addComponent(combatStatsComponent);

        component.setEntity(entity);

        component.create();

        assertEquals(combatStatsComponent.getHealth(), component.getHealthBar().getValue());

        // Above max health so shouldn't change
        component.updateHealth(200);
        assertEquals(100, component.getHealthBar().getValue());

        component.updateHealth(20);
        assertEquals(20, component.getHealthBar().getValue());

        combatStatsComponent.setHealth(50);

        // Ensures health bar updates when combat stats does
        assertEquals(combatStatsComponent.getHealth(), component.getHealthBar().getValue());
    }

    /**
     * Test the show method of HealthBarComponent.
     */
    @Test
    void showTest() {
        HealthBarComponent component = new HealthBarComponent(true);

        component.show();

        assertTrue(component.enabled);
    }

    /**
     * Test the hide method of HealthBarComponent.
     */
    @Test
    void hideTest() {
        HealthBarComponent component = new HealthBarComponent(true);

        component.hide();

        assertFalse(component.enabled);
    }
}
