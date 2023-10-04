package com.csse3200.game.components.player;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /**
     * the below Test has been implemented to
     * test the Sound of player's walk Sound
     */
    @Test
    public void testTriggerWalkEvent() {
        Gdx.app = mock(Application.class);

//        instance for KeyboardPlayerInputComponent
        KeyboardPlayerInputComponent Sound = new KeyboardPlayerInputComponent();

//        Mocking an 'Entity' object named 'WalkEvent' for testing purposes
        Entity WalkEvent = mock(Entity.class);

//        Setting the 'WalkEvent' as the entity for sound events.
        Sound.setEntity(WalkEvent);

//        Mocking the event handling for the 'WalkEvent' entity.
        when(WalkEvent.getEvents()).thenReturn(mock(EventHandler.class));

//        Testing the whole trigger Walk Event to test Player's walk Sound
        Sound.triggerWalkEvent();
    }

    /**
     * the below Test has been implemented to
     * test the Sound of player's Dodge Sound
     */
    @Test
    public void testTriggerDodgeEvent() {

        Gdx.app = mock(Application.class);

//        instance for KeyboardPlayerInputComponent
        KeyboardPlayerInputComponent Sound = new KeyboardPlayerInputComponent();

//        Setting the 'DodgeEvent' as the entity for sound events.
        Entity DodgeEvent = mock(Entity.class);

//        Setting the 'DodgeEvent' as the entity for sound events.
        Sound.setEntity(DodgeEvent);

//        Mocking the event handling for the 'DodgeEvent' entity.
        when(DodgeEvent.getEvents()).thenReturn(mock(EventHandler.class));

//        Testing the whole trigger Dodge Event to test Player's dodge Sound
        Sound.triggerDodgeEvent();

    }

}