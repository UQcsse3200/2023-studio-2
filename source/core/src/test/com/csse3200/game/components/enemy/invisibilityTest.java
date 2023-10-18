package com.csse3200.game.components.enemy;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class invisibilityTest {

    private Entity mockTarget;
    private Entity mockEnemy;

    @Test
    public void shouldBeInVisFromBeginning(){
        mockEnemy =  new Entity();
        mockEnemy.addComponent(new PhysicsMovementComponent(new Vector2(1f,1f))).addComponent(new TouchAttackComponent((short) (
            PhysicsLayer.PLAYER),1.5f));
        InvisibilityComponent invisibilityComponent = new InvisibilityComponent(mockEnemy);
        assertTrue(invisibilityComponent.getInvisMode());
    }

    @Test
    public void shouldTriggerCorrectAnimation(){
        mockEnemy =  new Entity();
        mockEnemy.addComponent(new PhysicsMovementComponent(new Vector2(1f,1f))).addComponent(new TouchAttackComponent((short) (
            PhysicsLayer.PLAYER),1.5f));
        InvisibilityComponent invisibilityComponent = new InvisibilityComponent(mockEnemy);
        EventListener0 callback = mock(EventListener0.class);
        mockEnemy.getEvents().addListener("goInvisible", callback);
        invisibilityComponent.update();
    }

    @Test
    public void shouldTriggerCorrectAnimation2(){
        mockEnemy =  new Entity();
        mockEnemy.addComponent(new PhysicsMovementComponent(new Vector2(1f,1f))).addComponent(new TouchAttackComponent((short) (
            PhysicsLayer.PLAYER),1.5f));
        InvisibilityComponent invisibilityComponent = new InvisibilityComponent(mockEnemy);
        invisibilityComponent.toggleInvis();
        EventListener0 callback = mock(EventListener0.class);
        mockEnemy.getEvents().addListener("goInvisible", callback);
        invisibilityComponent.update();
    }
}
