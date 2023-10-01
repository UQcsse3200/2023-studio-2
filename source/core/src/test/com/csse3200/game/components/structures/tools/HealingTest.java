package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.resources.Resource;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class HealingTest {
    @Mock
    StructurePlacementService structurePlacementService;
    @Mock
    PlaceableEntity placeableEntity;
    @Mock
    CombatStatsComponent combatStatsComponent;
    @Mock
    GameStateObserver stateObserver;

    @Test
    void healNotEnoughResources (){
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(combatStatsComponent.getHealth()).thenReturn(50);
        when(combatStatsComponent.getMaxHealth()).thenReturn(100);

        when(stateObserver.getStateData(any())).thenReturn(0);

        var position = new GridPoint2(0, 0);
        Healing healing = new Healing(new ObjectMap<>());
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        healing.interact(mock(Entity.class), position);
        verify(combatStatsComponent, never()).setHealth(100);
    }

    @Test
    void healEnoughResources (){
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        ServiceLocator.registerGameStateObserverService(stateObserver);
        when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(combatStatsComponent.getHealth()).thenReturn(50);
        when(combatStatsComponent.getMaxHealth()).thenReturn(100);

        when(stateObserver.getStateData(any())).thenReturn(100);

        var position = new GridPoint2(0, 0);
        Healing healing = new Healing(new ObjectMap<>());
        when(structurePlacementService.getStructureAt(position)).thenReturn(placeableEntity);

        healing.interact(mock(Entity.class), position);
        verify(stateObserver, times(1)).trigger("resourceAdd", Resource.Durasteel.toString(), -100);
        verify(stateObserver, times(1)).trigger("resourceAdd", Resource.Solstite.toString(), -100);
        verify(combatStatsComponent, times(1)).setHealth(100);
    }
}


