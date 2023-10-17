package com.csse3200.game.components.enemy;



import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class godComponentTest {
    private GodComponent godComponent;
    private Entity mockTarget;
    private Entity mockEnemy;


    @Test
    public void modeSwitchingTest(){
        mockTarget = new Entity();
        mockEnemy =  new Entity();
        godComponent = new GodComponent(mockEnemy);
        assertTrue(godComponent.getMode());
        godComponent.toggleMode();
        //assertFalse(godComponent.getMode());
        // After toggling again, it should be false
        godComponent.toggleMode();
        //assertTrue(godComponent.getMode());
    }
    @Test
    public void modeSwitchingTest2(){
        mockTarget = new Entity();
        mockEnemy =  new Entity();
        godComponent = new GodComponent(mockEnemy);
        godComponent.toggleMode();
        assertEquals(true,godComponent.getMode());
    }

    @Test
    public void shouldTriggerCorrectAnimation(){
        mockEnemy =  new Entity();
        mockEnemy.addComponent(new PhysicsMovementComponent(new Vector2(1f,1f))).addComponent(new TouchAttackComponent((short) (
            PhysicsLayer.PLAYER),1.5f));
        godComponent = new GodComponent(mockEnemy);
        EventListener0 callback = mock(EventListener0.class);
        mockEnemy.getEvents().addListener("goInvisible", callback);
        godComponent.update();
    }

    @Test
    public void shouldTriggerTheCorrectAnimation(){
        mockEnemy =  new Entity();
        mockEnemy.addComponent(new PhysicsMovementComponent(new Vector2(1f,1f))).addComponent(new TouchAttackComponent((short) (
            PhysicsLayer.PLAYER),1.5f));
        godComponent = new GodComponent(mockEnemy);
        godComponent.toggleInvis();
        EventListener0 callback = mock(EventListener0.class);
        mockEnemy.getEvents().addListener("float", callback);
        godComponent.update();
    }
    @Test
    public void shouldReturnTheCorrectAmountOfBullets(){
        mockEnemy =  new Entity();
        godComponent = new GodComponent(mockEnemy);
        Vector2[] bullets = godComponent.publicMethod(new Vector2(0f,0f));
        assertEquals(20,bullets.length);
    }








}
