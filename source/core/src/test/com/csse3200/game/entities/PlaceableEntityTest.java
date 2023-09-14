package com.csse3200.game.entities;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.structures.Placeable;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameStateObserver;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class PlaceableEntityTest {
    PlaceableEntity placeableEntity;

    @Mock
    private GameStateObserver stateObserver;

    @Mock
    private PlaceableComponent placeable;
    @Mock
    private PlaceableComponent2 placeable2;
    @Mock
    private PlaceableComponent3 placeable3;

    @Mock
    private Component notPlaceable;

    @Test
    void placed() {
        placeableEntity.placed();

        verify(placeable, times(1)).placed();
        verify(placeable2, times(1)).placed();
        verify(placeable3, times(1)).placed();
        verifyNoInteractions(notPlaceable);
    }

    @Test
    void willPlace() {
        placeableEntity.willPlace();

        verify(placeable, times(1)).willPlace();
        verify(placeable2, times(1)).willPlace();
        verify(placeable3, times(1)).willPlace();
        verifyNoInteractions(notPlaceable);
    }

    @Test
    void removed() {
        placeableEntity.removed();

        verify(placeable, times(1)).removed();
        verify(placeable2, times(1)).removed();
        verify(placeable3, times(1)).removed();
        verifyNoInteractions(notPlaceable);
    }

    @Test
    void willRemove() {
        placeableEntity.willRemove();

        verify(placeable, times(1)).willRemove();
        verify(placeable2, times(1)).willRemove();
        verify(placeable3, times(1)).willRemove();
        verifyNoInteractions(notPlaceable);
    }

    @BeforeEach
    void before() {
        placeableEntity = new PlaceableEntity();
        placeableEntity.addComponent(placeable);
        placeableEntity.addComponent(placeable2);
        placeableEntity.addComponent(placeable3);
        placeableEntity.addComponent(notPlaceable);

        reset(placeable);
        reset(placeable2);
        reset(placeable3);
        reset(notPlaceable);
    }

    @AfterEach
    void after() {
        ServiceLocator.clear();
    }

    private class PlaceableComponent extends Component implements Placeable {

    }
    private class PlaceableComponent2 extends Component implements Placeable {

    }
    private class PlaceableComponent3 extends Component implements Placeable {

    }
}