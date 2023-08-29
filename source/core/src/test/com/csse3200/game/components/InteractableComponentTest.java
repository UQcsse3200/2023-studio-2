package com.csse3200.game.components;

import com.csse3200.game.components.player.InteractionControllerComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InteractableComponentTest {
    @Mock
    EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
    }

    @Test
    void shouldNotBeInteractableOutsideDistance() {
        Entity controller = new Entity().addComponent(new InteractionControllerComponent(false));
        Entity target = new Entity().addComponent(new InteractableComponent(entity -> fail(), 10));
        target.setPosition(controller.getCenterPosition().add(11f, 0));

        controller.getComponent(InteractionControllerComponent.class).interact();
    }

    @Test
    void shouldBeInteractableInsideDistance() {
        Entity controller = new Entity().addComponent(new InteractionControllerComponent(false));
        Entity target = new Entity().addComponent(new InteractableComponent(entity -> fail(), 10));
        target.setPosition(controller.getCenterPosition().add(9f, 0));

        controller.getComponent(InteractionControllerComponent.class).interact();
    }

    @Test
    void shouldBeInteractableAtDistance() {
        Entity controller = new Entity().addComponent(new InteractionControllerComponent(false));
        Entity target = new Entity().addComponent(new InteractableComponent(entity -> fail(), 10));
        target.setPosition(controller.getCenterPosition().add(10f, 0));

        controller.getComponent(InteractionControllerComponent.class).interact();
    }

    @Test
    void shouldAllBeInteractableIfWithinDistance() {
        Entity controller = new Entity().addComponent(new InteractionControllerComponent(true));
        Entity target = new Entity().addComponent(new InteractableComponent(entity -> fail(), 12));
        Entity target2 = new Entity().addComponent(new InteractableComponent(entity -> assertTrue(true), 10));
        Entity target3 = new Entity().addComponent(new InteractableComponent(entity -> assertTrue(true), 8));
        target.setPosition(controller.getCenterPosition().add(10f, 0));
        target2.setPosition(controller.getCenterPosition().add(10f, 0));
        target3.setPosition(controller.getCenterPosition().add(10f, 0));

        controller.getComponent(InteractionControllerComponent.class).interact();
    }
}
