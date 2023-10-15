package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.components.structures.CostComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.stubbing.StrictnessSelector;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class HealToolTest {
    @Mock
    StructurePlacementService structurePlacementService;
    @Mock
    PlaceableEntity placeableEntity;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
    }

    @Test
    void testCanInteractWithEnoughResources(){
        var position = new GridPoint2(0, 0);
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        var cost = new ObjectMap<String, Integer>();
        var resources = new HashMap<Resource, Integer>();

        // test a couple different resources
        // No cost and no resources should work
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, true);

        resources.put(Resource.Nebulite, 10);
        // No cost and some resources should work
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, true);

        cost.put(Resource.Nebulite.toString(), 5);
        // Exact amount required should work
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, true);

        cost.put(Resource.Nebulite.toString(), 10);
        // Full heal should work
        testCanInteractWithCostAndResources(cost, resources, 0, 100, position, true);

        resources.put(Resource.Solstite, 100);
        resources.put(Resource.Durasteel, 5);
        cost.put(Resource.Solstite.toString(), 100);
        cost.put(Resource.Durasteel.toString(), 5);
        // Multiple resources should work
        testCanInteractWithCostAndResources(cost, resources, 0, 100, position, true);
    }

    void testCanInteractWithCostAndResources(ObjectMap<String, Integer> cost, Map<Resource, Integer> resources,
                                             int health, int maxHealth, GridPoint2 position, boolean validExpected) {
        GameStateObserver stateObserver = mock(GameStateObserver.class);
        ServiceLocator.registerGameStateObserverService(stateObserver);

        CombatStatsComponent combatStatsComponent = mock(CombatStatsComponent.class);
        CostComponent costComponent = mock(CostComponent.class);

        lenient().when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        lenient().when(placeableEntity.getComponent(CostComponent.class)).thenReturn(costComponent);

        lenient().when(combatStatsComponent.getHealth()).thenReturn(health);
        lenient().when(combatStatsComponent.getMaxHealth()).thenReturn(maxHealth);

        for (var resourceEntry : resources.entrySet()) {
            lenient().when(stateObserver.getStateData("resource/" + resourceEntry.getKey().toString()))
                    .thenReturn(resourceEntry.getValue());
        }

        lenient().when(costComponent.getCost()).thenReturn(new ObjectMap<>(cost));

        HealTool healTool = new HealTool(new ObjectMap<>());
        var player = mock(Entity.class);

        var validity = healTool.canInteract(player, position);

        Assertions.assertEquals(validExpected, validity.isValid());
    }

    @Test
    void testCanInteractWithInsufficientResources(){
        var position = new GridPoint2(0, 0);
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        var cost = new ObjectMap<String, Integer>();
        var resources = new HashMap<Resource, Integer>();

        cost.put(Resource.Nebulite.toString(), 10);
        // Cost with no resources should fail
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, false);

        resources.put(Resource.Nebulite, 4);
        // One under required resources should fail
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, false);

        resources.put(Resource.Solstite, 99);
        resources.put(Resource.Durasteel, 4);
        cost.put(Resource.Solstite.toString(), 100);
        cost.put(Resource.Durasteel.toString(), 5);
        // Multiple resources all under should fail
        testCanInteractWithCostAndResources(cost, resources, 0, 100, position, false);

        resources.put(Resource.Solstite, 100);
        resources.put(Resource.Durasteel, 5);
        // Multiple resources with only one under should fail
        testCanInteractWithCostAndResources(cost, resources, 0, 100, position, false);
    }

    @Test
    void testCanInteractWithNoStructure(){
        var position = new GridPoint2(0, 0);
        when(structurePlacementService.getStructureAt(position)).thenReturn(null);

        var cost = new ObjectMap<String, Integer>();
        var resources = new HashMap<Resource, Integer>();

        // No structure at position should fail
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, false);

        var neighbour_position = new GridPoint2(1, 1);
        lenient().when(structurePlacementService.getStructureAt(neighbour_position)).thenReturn(placeableEntity);

        // Structure at neighbouring position should fail
        testCanInteractWithCostAndResources(cost, resources, 50, 100, position, false);
    }

    @Test
    void testCanInteractWithNoCombatStats(){
        GameStateObserver stateObserver = mock(GameStateObserver.class);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        CostComponent costComponent = mock(CostComponent.class);

        lenient().when(placeableEntity.getComponent(CostComponent.class)).thenReturn(costComponent);

        HealTool healTool = new HealTool(new ObjectMap<>());
        var player = mock(Entity.class);
        var position = new GridPoint2(0, 0);

        var validity = healTool.canInteract(player, position);

        // should fail if no combat stats
        Assertions.assertFalse(validity.isValid());
    }

    @Test
    void testCanInteractWithNoCost(){
        GameStateObserver stateObserver = mock(GameStateObserver.class);
        ServiceLocator.registerGameStateObserverService(stateObserver);

        CombatStatsComponent combatStatsComponent = mock(CombatStatsComponent.class);

        lenient().when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        lenient().when(combatStatsComponent.getHealth()).thenReturn(50);
        lenient().when(combatStatsComponent.getMaxHealth()).thenReturn(100);

        HealTool healTool = new HealTool(new ObjectMap<>());
        var player = mock(Entity.class);
        var position = new GridPoint2(0, 0);

        var validity = healTool.canInteract(player, position);

        // should fail if no cost component
        Assertions.assertFalse(validity.isValid());
    }

    @Test
    void testCanInteractWithFullHealth() {
        var position = new GridPoint2(0, 0);
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        var cost = new ObjectMap<String, Integer>();
        var resources = new HashMap<Resource, Integer>();

        // Should fail if the structure is full health.
        testCanInteractWithCostAndResources(cost, resources, 100, 100, position, false);

        // Ensure it isn't assuming maxHealth is 100
        testCanInteractWithCostAndResources(cost, resources, 173, 173, position, false);

        // Test edge case and divide by 0 error
        testCanInteractWithCostAndResources(cost, resources, 0, 0, position, false);
    }

    @Test
    void testPerformInteraction() {
        var position = new GridPoint2(0, 0);
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        var cost = new ObjectMap<String, Integer>();
        var resources = new HashMap<Resource, Integer>();

        // should heal to full with no cost
        testInteractWithParameters(cost, resources, 0, 100, position);

        resources.put(Resource.Solstite, 100);
        cost.put(Resource.Solstite.toString(), 100);
        // should heal to full and deduct single cost
        testInteractWithParameters(cost, resources, 0, 100, position);

        resources.put(Resource.Durasteel, 50);
        cost.put(Resource.Durasteel.toString(), 50);
        resources.put(Resource.Nebulite, 1);
        cost.put(Resource.Nebulite.toString(), 1);
        // should heal to full and deduct multiple cost
        testInteractWithParameters(cost, resources, 0, 100, position);

        // try only half heal
        testInteractWithParameters(cost, resources, 50, 100, position);

        // ensure it doesn't assume max health is 100
        testInteractWithParameters(cost, resources, 50, 27341, position);
    }



    void testInteractWithParameters(ObjectMap<String, Integer> cost, Map<Resource, Integer> resources,
                      int health, int maxHealth, GridPoint2 position) {
        GameStateObserver stateObserver = mock(GameStateObserver.class);
        ServiceLocator.registerGameStateObserverService(stateObserver);

        CombatStatsComponent combatStatsComponent = mock(CombatStatsComponent.class);
        CostComponent costComponent = mock(CostComponent.class);

        lenient().when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        lenient().when(placeableEntity.getComponent(CostComponent.class)).thenReturn(costComponent);

        lenient().when(combatStatsComponent.getHealth()).thenReturn(health);
        lenient().when(combatStatsComponent.getMaxHealth()).thenReturn(maxHealth);

        for (var resourceEntry : resources.entrySet()) {
            lenient().when(stateObserver.getStateData("resource/" + resourceEntry.getKey().toString()))
                    .thenReturn(resourceEntry.getValue());
        }

        lenient().when(costComponent.getCost()).thenReturn(new ObjectMap<>(cost));

        HealTool healTool = new HealTool(new ObjectMap<>());
        var player = mock(Entity.class);

        // required to calculate requiredResources
        healTool.canInteract(player, position);

        healTool.interact(player, position);

        float healPercent = (float) (maxHealth - health) /maxHealth;

        for (var entry : cost.entries()) {
            verify(stateObserver, times(1)).trigger("resourceAdd", entry.key,
                    -(int)Math.ceil(entry.value * healPercent));
        }

        verify(combatStatsComponent, times(1)).setHealth(maxHealth);
    }
}


