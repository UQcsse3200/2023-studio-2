package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class KeyboardPlayerInputComponentTest {

    @Mock EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = new EntityService();
        ServiceLocator.registerEntityService(entityService);
    }

    private Vector2 direction(KeyboardPlayerInputComponent comp) {
        return comp.getDirection();
    }

}