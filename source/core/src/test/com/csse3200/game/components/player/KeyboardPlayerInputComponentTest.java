package com.csse3200.game.components.player;

import com.badlogic.gdx.Input;
import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
public class KeyboardPlayerInputComponentTest {
    @Mock EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
    }

    @Test
    void shouldCallInteractAnyDistanceOnF() {
        KeyboardPlayerInputComponent playerInputComponent = new KeyboardPlayerInputComponent();
        InteractionControllerComponent interactionController = mock(InteractionControllerComponent.class,
                withSettings().useConstructor(false).defaultAnswer(CALLS_REAL_METHODS));
        Entity player = new Entity().addComponent(playerInputComponent).addComponent(interactionController);
        playerInputComponent.keyDown(Input.Keys.F);

        verify(interactionController).interact();
    }
}
