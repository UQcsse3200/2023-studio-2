package com.csse3200.game.components.structures.tools;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.ObjectMap;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.PlaceableEntity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @Test
    void interactTest (){
        ServiceLocator.registerStructurePlacementService(structurePlacementService);
        when(structurePlacementService.getStructureAt(any())).thenReturn(placeableEntity);
        when(placeableEntity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);
        when(combatStatsComponent.getMaxHealth()).thenReturn(100);
        Healing healing = new Healing(new ObjectMap<>());
        healing.interact(mock(Entity.class), new GridPoint2());
        verify(combatStatsComponent, times(1)).setHealth(100);

    }

}