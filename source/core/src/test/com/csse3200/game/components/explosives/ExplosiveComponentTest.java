package com.csse3200.game.components.explosives;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.EntityPlacementService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ExplosiveComponentTest {

    @Test
    void create() {
        var component = new ExplosiveComponent(new ExplosiveConfig());

        var entity = mock(Entity.class);
        var events = mock(EventHandler.class);
        when(entity.getEvents()).thenReturn(events);
        component.setEntity(entity);

        component.create();

        verify(events, times(1)).addListener(eq("explode"), any(EventListener0.class));
        verify(events, times(1)).addListener(eq("chainExplode"), any(EventListener1.class));
    }

    /*@Test
    void testExploded() {
        var resourceService = mock(ResourceService.class);
        var entityPlacementService = mock(EntityPlacementService.class);
        var entityService = mock(EntityService.class);

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityPlacementService(entityPlacementService);
        ServiceLocator.registerEntityService(entityService);


        var config = new ExplosiveConfig();
        config.baseAttack = 10;
        config.damageRadius = 5;
        config.chainRadius = 5;
        config.chainable = false;
        config.soundPath = "explosion.wav";
        config.effectPath = "explosion.effect";

        var component = new ExplosiveComponent(config);

        var entity = mock(Entity.class);
        when(entity.getEvents()).thenReturn(new EventHandler());
        when(entity.getPosition()).thenReturn(new Vector2(0, 2));
        component.setEntity(entity);

        component.create();

        entity.getEvents().trigger("explode");
        verify(entityPlacementService, times(1))
                .placeEntityAt(any(), eq(new Vector2(0, 2)));
    }*/

    /*@Test
    void testDamageNeighbours() {
        var resourceService = mock(ResourceService.class);
        var entityPlacementService = mock(EntityPlacementService.class);
        var entityService = mock(EntityService.class);

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityPlacementService(entityPlacementService);
        ServiceLocator.registerEntityService(entityService);

        var config = new ExplosiveConfig();
        config.baseAttack = 10;
        config.damageRadius = 5;
        config.chainRadius = 5;
        config.chainable = false;
        config.soundPath = "explosion.wav";
        config.effectPath = "explosion.effect";

        var component = new ExplosiveComponent(config);

        var entity = mock(Entity.class);
        when(entity.getEvents()).thenReturn(new EventHandler());
        when(entity.getPosition()).thenReturn(new Vector2(0, 2));
        when(entity.getCenterPosition()).thenReturn(new Vector2(1, 3));
        component.setEntity(entity);

        component.create();

        List<Entity> neighbours = new ArrayList<>();

        var inRangeEntity = mock(Entity.class);
        when(inRangeEntity.getCenterPosition()).thenReturn(new Vector2(1, 3));
        var inRangeEntityHealth = mock(CombatStatsComponent.class);
        when(inRangeEntity.getComponent(CombatStatsComponent.class)).thenReturn(inRangeEntityHealth);
        neighbours.add(inRangeEntity);

        var outOfRangeEntity = mock(Entity.class);
        when(outOfRangeEntity.getCenterPosition()).thenReturn(new Vector2(25, 30));
        var outOfRangeEntityHealth = mock(CombatStatsComponent.class);
        when(outOfRangeEntity.getComponent(CombatStatsComponent.class)).thenReturn(outOfRangeEntityHealth);
        neighbours.add(outOfRangeEntity);

        when(entityService.getEntitiesByComponent(CombatStatsComponent.class))
                .thenReturn(neighbours);
        when(entityService.getEntitiesByComponent(ExplosiveComponent.class))
                .thenReturn(new ArrayList<>());

        entity.getEvents().trigger("explode");
        verify(entityPlacementService, times(1))
                .placeEntityAt(any(), eq(new Vector2(0, 2)));

        verify(inRangeEntityHealth, times(1)).addHealth(-10);
        verify(outOfRangeEntityHealth, never()).addHealth(anyInt());
    }*/

    /*@Test
    void testExplodeNeighbours() {
        var resourceService = mock(ResourceService.class);
        var entityPlacementService = mock(EntityPlacementService.class);
        var entityService = mock(EntityService.class);

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityPlacementService(entityPlacementService);
        ServiceLocator.registerEntityService(entityService);

        var config = new ExplosiveConfig();
        config.baseAttack = 10;
        config.damageRadius = 5;
        config.chainRadius = 5;
        config.chainable = false;
        config.soundPath = "explosion.wav";
        config.effectPath = "explosion.effect";

        var component = new ExplosiveComponent(config);

        var entity = mock(Entity.class);
        when(entity.getEvents()).thenReturn(new EventHandler());
        when(entity.getPosition()).thenReturn(new Vector2(0, 2));
        when(entity.getCenterPosition()).thenReturn(new Vector2(1, 3));
        component.setEntity(entity);

        component.create();

        List<Entity> neighbours = new ArrayList<>();

        var inRangeEntity = mock(Entity.class);
        when(inRangeEntity.getCenterPosition()).thenReturn(new Vector2(1, 3));
        var inRangeEntityEvents = mock(EventHandler.class);
        when(inRangeEntity.getEvents()).thenReturn(inRangeEntityEvents);
        neighbours.add(inRangeEntity);

        var outOfRangeEntity = mock(Entity.class);
        when(outOfRangeEntity.getCenterPosition()).thenReturn(new Vector2(25, 30));
        neighbours.add(outOfRangeEntity);

        when(entityService.getEntitiesByComponent(CombatStatsComponent.class))
                .thenReturn(new ArrayList<>());
        when(entityService.getEntitiesByComponent(ExplosiveComponent.class))
                .thenReturn(neighbours);

        entity.getEvents().trigger("explode");
        verify(entityPlacementService, times(1))
                .placeEntityAt(any(), eq(new Vector2(0, 2)));

        verify(inRangeEntityEvents, times(1)).trigger(eq("chainExplode"), anyFloat());
    }*/

    @AfterEach
    void after() {
        ServiceLocator.clear();
    }
}