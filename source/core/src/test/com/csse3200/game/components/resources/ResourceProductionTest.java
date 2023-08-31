package com.csse3200.game.components.resources;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import com.csse3200.game.services.GameTime;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.security.Provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ResourceProductionTest{
    @Mock GameTime gameTime;
    Entity entity;
    ProductionComponent productionComponent;
    CombatStatsComponent combatStatsComponent;

    private final long tickRate = 1000;
    private final int tickSize = 1;
    private final int maxHealth = 1;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerGameStateObserverService(new GameStateObserver());
        productionComponent = new ProductionComponent(Resource.Solstite, tickRate, tickSize);
        combatStatsComponent = new CombatStatsComponent(maxHealth, 0, 1, false);
        when(gameTime.getTime()).thenReturn(0L);
        when(gameTime.getTime()).thenCallRealMethod();
        productionComponent.setTimer(gameTime);
    }

    @Test
    void validMultiplierWithoutCombatStats() {
        Entity entity = new Entity().addComponent(productionComponent);
        entity.create();
        assertEquals(1, productionComponent.getProductionModifier());
    }

    @Test
    void validMultiplier() {
        Entity entity = new Entity().addComponent(productionComponent).addComponent(combatStatsComponent);
        entity.create();
        for (int i = maxHealth; i >= 0; i--) {
            entity.getComponent(CombatStatsComponent.class).setHealth(i);
            assertEquals(i == 0 ? 0 : 1, productionComponent.getProductionModifier());
        }
    }

    @Test
    void producesConsistently() {
        Entity entity = new Entity().addComponent(productionComponent);
        entity.create();
        for (int i = 1; i < 10; i++) {
            when(gameTime.getTime()).thenReturn((tickRate * i));
            entity.update();
            assertEquals(tickSize * i, ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));
        }
    }

    @Test
    void producesCatchup() {
        Entity entity = new Entity().addComponent(productionComponent);
        entity.create();
        when(gameTime.getTime()).thenReturn((long) (tickRate * 10.5));
        entity.update();
        assertEquals(tickSize * 10, ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));
    }

    @Test
    void producesEarly() {
        Entity entity = new Entity().addComponent(productionComponent);
        entity.create();
        when(gameTime.getTime()).thenReturn(0L);
        entity.update();
        assertNull(ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));

        when(gameTime.getTime()).thenReturn(500L);
        entity.update();
        assertNull(ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));

        when(gameTime.getTime()).thenReturn(1000L);
        entity.update();
        assertEquals(tickSize, ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));
    }

    @Test
    void producesTightBound() {
        Entity entity = new Entity().addComponent(productionComponent);
        entity.create();
        when(gameTime.getTime()).thenReturn(999L);
        entity.update();
        assertNull(ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));

        when(gameTime.getTime()).thenReturn(1000L);
        entity.update();
        assertEquals(tickSize, ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));

        when(gameTime.getTime()).thenReturn(1001L);
        entity.update();
        assertEquals(tickSize, ServiceLocator.getGameStateObserverService().getStateData("resource/Solstite"));
    }
}
