package com.csse3200.game.components.structures;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.StructurePlacementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class StructureDestroyComponentTest {
    @Mock
    CombatStatsComponent combatStatsComponent;
    @Mock
    Entity entity;
    @Mock
    StructurePlacementService structurePlacementService;

    @Test
    void updateIsDead() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);

        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        when(combatStatsComponent.isDead()).thenReturn(true);

        var position = mock(GridPoint2.class);
        when(structurePlacementService.getStructurePosition(entity)).thenReturn(position);

        var structureDestroyComponent = new StructureDestroyComponent();
        structureDestroyComponent.setEntity(entity);

        structureDestroyComponent.update();

        verify(structurePlacementService, times(1)).removeStructureAt(position);
    }

    @Test
    void updateIsNotDead() {
        ServiceLocator.registerStructurePlacementService(structurePlacementService);

        when(entity.getComponent(CombatStatsComponent.class)).thenReturn(combatStatsComponent);

        when(combatStatsComponent.isDead()).thenReturn(false);

        var position = mock(GridPoint2.class);

        var structureDestroyComponent = new StructureDestroyComponent();
        structureDestroyComponent.setEntity(entity);

        structureDestroyComponent.update();

        verify(structurePlacementService, never()).removeStructureAt(position);
    }
}