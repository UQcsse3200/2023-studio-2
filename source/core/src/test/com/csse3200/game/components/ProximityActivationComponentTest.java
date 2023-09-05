package com.csse3200.game.components;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link ProximityActivationComponent} class.
 */
public class ProximityActivationComponentTest {

    /**
     * Test the update method of ProximityActivationComponent.
     */
    @Test
    public void updateTest() {

        Entity entityMock = mock(Entity.class);
        when(entityMock.getCenterPosition()).thenReturn(new Vector2(0, 0));

        Entity targetEntityMock = mock(Entity.class);
        when(targetEntityMock.getCenterPosition()).thenReturn(new Vector2(3, 3));

        List<Entity> entities = new ArrayList<>();
        entities.add(targetEntityMock);

        ProximityActivationComponent.ProximityFunc enteredFunc = mock(ProximityActivationComponent.ProximityFunc.class);
        ProximityActivationComponent.ProximityFunc exitedFunc = mock(ProximityActivationComponent.ProximityFunc.class);

        ProximityActivationComponent component = new ProximityActivationComponent(
                10f, entities, enteredFunc, exitedFunc);

        component.setEntity(entityMock);

        component.update();

        verify(enteredFunc).call(targetEntityMock);
        verify(exitedFunc, never()).call(targetEntityMock);
    }

    /**
     * Test the entityIsInProximity method of ProximityActivationComponent.
     */
    @Test
    public void entityIsInProximityTest() {
        Entity entityMock = mock(Entity.class);
        when(entityMock.getCenterPosition()).thenReturn(new Vector2(0, 0));

        Entity targetEntityMock = mock(Entity.class);
        when(targetEntityMock.getCenterPosition()).thenReturn(new Vector2(3, 3));

        ProximityActivationComponent.ProximityFunc enteredFunc = mock(ProximityActivationComponent.ProximityFunc.class);
        ProximityActivationComponent.ProximityFunc exitedFunc = mock(ProximityActivationComponent.ProximityFunc.class);

        ProximityActivationComponent component = new ProximityActivationComponent(
                10f, targetEntityMock, enteredFunc, exitedFunc);

        component.setEntity(entityMock);

        assertTrue(component.entityIsInProximity(targetEntityMock));
    }
}
