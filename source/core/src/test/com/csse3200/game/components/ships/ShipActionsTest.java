package com.csse3200.game.components.ships;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.physics.box2d.Body;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShipActionsTest {
    @Mock
    private EventHandler eventHandler;

    @Mock
    private EntityService entityService;

    @Mock
    private PhysicsComponent physicsComponent;

    @Mock
    private Body body;

    @Mock
    private Entity ship;

    @BeforeEach
    void setUp() {
        ServiceLocator.registerEntityService(entityService);

        // Set up a ship entity with ShipActions component
        ship = new Entity();
        ship.addComponent(physicsComponent);
        ship.addComponent(new ShipActions(100, 100, 1));
        ship = ship.getComponent(ShipActions.class);
    }

}