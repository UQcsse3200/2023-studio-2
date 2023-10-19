package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.EntityPlacementService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ActionFeedbackComponentTest {
    @Mock
    Entity entity;
    @Mock
    EventHandler events;

    @Test
    void create() {
        when(entity.getEvents()).thenReturn(events);

        var component = new ActionFeedbackComponent();
        component.setEntity(entity);

        component.create();

        // check expected events are registered.
        verify(events).addListener(eq("displayWarning"), isA(EventListener1.class));
        verify(events).addListener(eq("displayWarningAtPosition"), isA(EventListener2.class));
    }

    @Test
    void displayWarning() {
        var entityPlacementService = mock(EntityPlacementService.class);
        ServiceLocator.registerEntityPlacementService(entityPlacementService);


        when(entity.getPosition()).thenReturn(new Vector2(0, 2));

        var component = new ActionFeedbackComponent();
        component.setEntity(entity);
        component.displayWarning("Test alert");

        verify(entity, times(1)).getPosition();
        verify(entityPlacementService, times(1))
                .placeEntityAt(isA(Entity.class), eq(new Vector2(0, 2)));
    }

    @Test
    void displayWarningAtPosition() {
        var entityPlacementService = mock(EntityPlacementService.class);
        ServiceLocator.registerEntityPlacementService(entityPlacementService);

        var component = new ActionFeedbackComponent();
        component.displayWarningAtPosition("Test alert", new Vector2(2, 0));

        verify(entityPlacementService, times(1))
                .placeEntityAt(isA(Entity.class), eq(new Vector2(2,0)));
    }
}