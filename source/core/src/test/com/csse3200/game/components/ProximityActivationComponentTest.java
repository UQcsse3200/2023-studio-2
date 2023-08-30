package com.csse3200.game.components;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class ProximityActivationComponentTest {

    @Test
    public void updateTest() {

        Entity entityMock = mock(Entity.class);
        when(entityMock.getCenterPosition()).thenReturn(new Vector2(8, 8));

        List<Entity> entities = new ArrayList<>();
        entities.add(entityMock);

        ProximityActivationComponent.ProximityFunc enteredFunc = mock(ProximityActivationComponent.ProximityFunc.class);
        ProximityActivationComponent.ProximityFunc exitedFunc = mock(ProximityActivationComponent.ProximityFunc.class);

        ProximityActivationComponent component = new ProximityActivationComponent(
                10f, entities, enteredFunc, exitedFunc);

        component.update();

        verify(enteredFunc).call(entityMock);
        verify(exitedFunc, never()).call(entityMock);
    }

    @Test
    public void entityIsInProximityTest() {
        Entity entityMock = mock(Entity.class);
        when(entityMock.getCenterPosition()).thenReturn(new Vector2(8, 8));

        ProximityActivationComponent.ProximityFunc enteredFunc = mock(ProximityActivationComponent.ProximityFunc.class);
        ProximityActivationComponent.ProximityFunc exitedFunc = mock(ProximityActivationComponent.ProximityFunc.class);

        ProximityActivationComponent component = new ProximityActivationComponent(
                10f, entityMock, enteredFunc, exitedFunc);

        assertTrue(component.entityIsInProximity(entityMock));

    }
}