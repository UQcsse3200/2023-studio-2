package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class HealthBarComponentTest {
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

        // above max health so shouldn't change
        component.updateHealth(200);
        assertEquals(100, component.getHealthBar().getValue());

        component.updateHealth(20);
        assertEquals(20, component.getHealthBar().getValue());

        combatStatsComponent.setHealth(50);

        // ensures health bar updates when combat stats does
        assertEquals(combatStatsComponent.getHealth(), component.getHealthBar().getValue());

    }

    @Test
    void showTest() {
        HealthBarComponent component = new HealthBarComponent(true);

        component.show();

        assertTrue(component.enabled);
    }

    @Test
    void hideTest() {
        HealthBarComponent component = new HealthBarComponent(true);

        component.hide();
        assertFalse(component.enabled);
    }
}