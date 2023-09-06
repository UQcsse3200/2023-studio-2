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

    @Test
    void placed() {
        placeableEntity.placed();

        verify(placeable, times(1)).placed();
    }

    @Test
    void removed() {
        placeableEntity.removed();

        verify(placeable, times(1)).removed();
    }

    @BeforeEach
    void before() {
        placeableEntity = new PlaceableEntity();
        placeableEntity.addComponent(placeable);
    }

    @AfterEach
    void after() {
        ServiceLocator.clear();
    }

    class PlaceableComponent extends Component implements Placeable {

        @Override
        public void placed() {

        }

        @Override
        public void removed() {

        }
    }
}