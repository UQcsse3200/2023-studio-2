package com.csse3200.game.components.player;

import com.csse3200.game.components.InteractableComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class InteractionControllerComponentTest {
    @Mock
    EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
    }

    @Test
    void shouldInteractWithSingleEntity() {
        KeyboardPlayerInputComponent playerInputComponent = new KeyboardPlayerInputComponent();
        InteractionControllerComponent interactionController = mock(InteractionControllerComponent.class,
                withSettings().useConstructor(false).defaultAnswer(CALLS_REAL_METHODS));
        Entity player = new Entity().addComponent(playerInputComponent).addComponent(interactionController);

        Entity target = new Entity().addComponent(new InteractableComponent(entity -> assertTrue(true), Float.MAX_VALUE));

        interactionController.interact();
    }

    @Test
    void shouldInteractWithClosestEntity() {
        KeyboardPlayerInputComponent playerInputComponent = new KeyboardPlayerInputComponent();
        InteractionControllerComponent interactionController = mock(InteractionControllerComponent.class,
                withSettings().useConstructor(false).defaultAnswer(CALLS_REAL_METHODS));
        Entity player = new Entity().addComponent(playerInputComponent).addComponent(interactionController);

        Entity dummy = new Entity().addComponent(new InteractableComponent(entity -> fail(), Float.MAX_VALUE));
        Entity target = new Entity().addComponent(new InteractableComponent(entity -> assertTrue(true), Float.MAX_VALUE));
        target.setPosition(player.getCenterPosition());
        dummy.setPosition(player.getCenterPosition().add(10f, 10f));

        interactionController.interact();
    }

    @Test
    void shouldInteractWithAllEntities() {
        KeyboardPlayerInputComponent playerInputComponent = new KeyboardPlayerInputComponent();
        InteractionControllerComponent interactionController = mock(InteractionControllerComponent.class,
                withSettings().useConstructor(true).defaultAnswer(CALLS_REAL_METHODS));
        Entity player = new Entity().addComponent(playerInputComponent).addComponent(interactionController);

        List<Entity> interacted = new ArrayList<>();
        Entity dummy = new Entity().addComponent(new InteractableComponent(interacted::add, Float.MAX_VALUE));
        Entity target = new Entity().addComponent(new InteractableComponent(interacted::add, Float.MAX_VALUE));
        target.setPosition(player.getCenterPosition());
        dummy.setPosition(player.getCenterPosition().add(10f, 10f));

        interactionController.interact();
        List<Entity> expected = Arrays.asList(dummy, target);
        assertTrue(expected.containsAll(interacted));
    }
}
