package com.csse3200.game.components.enemy;

import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertNotNull;

public class jumpTest {
    @Test
    public void enemyShouldExist(){
        Entity mockEnemy =  new Entity();
        JumpComponent jumpComponent = new JumpComponent(mockEnemy);
        mockEnemy.addComponent(jumpComponent);
        assertNotNull(jumpComponent.getEntity());
    }
}
