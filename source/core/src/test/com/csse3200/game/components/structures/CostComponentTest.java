package com.csse3200.game.components.structures;

import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class CostComponentTest {
    @Mock
    GameStateObserver gameStateObserver;
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerGameStateObserverService(gameStateObserver);
    }

    @Test
    void placed() {
        var cost = new ObjectMap<String, Integer>();

        // test with no elements
        testPlacedWithCost(cost);

        cost.put("test1", 10);

        // test with only one element
        testPlacedWithCost(cost);

        cost.put("test2", 1000);
        cost.put("test3", 0);
        cost.put("test4", -1000);

        // test with multiple elements
        testPlacedWithCost(cost);
    }

    void testPlacedWithCost(ObjectMap<String, Integer> cost) {
        reset(gameStateObserver);

        var costComponent = new CostComponent(cost);
        costComponent.placed();
        for (var costEntry : cost.entries()) {
            verify(gameStateObserver).trigger("resourceAdd", costEntry.key, -costEntry.value);
        }
    }

    @Test
    void removed() {
        var cost = new ObjectMap<String, Integer>();

        // test with no elements
        testRemovedWithCost(cost);

        cost.put("test1", 10);

        // test with only one element
        testRemovedWithCost(cost);

        cost.put("test2", 1000);
        cost.put("test3", 0);
        cost.put("test4", -1000);

        // test with multiple elements
        testRemovedWithCost(cost);
    }

    void testRemovedWithCost(ObjectMap<String, Integer> cost) {
        reset(gameStateObserver);

        var costComponent = new CostComponent(cost);
        var entity = mock(Entity.class);
        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(null);

        costComponent.setEntity(entity);
        costComponent.removed();
        for (var costEntry : cost.entries()) {
            verify(gameStateObserver).trigger("resourceAdd", costEntry.key, costEntry.value);
        }
    }

    @Test
    void removedWithCombatStatsComponent() {
        var cost = new ObjectMap<String, Integer>();

        // test with no elements
        testRemovedWithCostAndHealth(cost, 100, 100);

        cost.put("test1", 10);

        // test with only one element
        testRemovedWithCostAndHealth(cost, 100, 100);
        testRemovedWithCostAndHealth(cost, 50, 100);
        testRemovedWithCostAndHealth(cost, 0, 100);

        cost.put("test2", 1000);
        cost.put("test3", 0);
        cost.put("test4", -1000);

        // test with multiple elements
        testRemovedWithCostAndHealth(cost, 100, 100);
        testRemovedWithCostAndHealth(cost, 50, 100);
        testRemovedWithCostAndHealth(cost, 0, 100);
    }

    void testRemovedWithCostAndHealth(ObjectMap<String, Integer> cost, int health, int maxHealth) {
        reset(gameStateObserver);

        var costComponent = new CostComponent(cost);
        var entity = mock(Entity.class);
        var combatStatsComponent = new CombatStatsComponent(maxHealth, 0, 0, false);
        combatStatsComponent.setHealth(health);

        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        costComponent.setEntity(entity);
        costComponent.removed();
        for (var costEntry : cost.entries()) {
            verify(gameStateObserver).trigger("resourceAdd",
                    costEntry.key,
                    costEntry.value * (health / maxHealth));
        }
    }

    @AfterEach
    void afterEach() {
        ServiceLocator.clear();
    }
}