package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthBarComponentTest {

    /*@Test
    void updateHealth() {
        Entity entity = new Entity().addComponent(new HealthBarComponent(true));
        entity.getComponent(HealthBarComponent.class).updateHealth(100);
        assertEquals(100, entity.getComponent(HealthBarComponent.class).getHealthBar().getValue());
    }

    @Test
    void show() {
        Entity entity = new Entity().addComponent(new HealthBarComponent(true));
        entity.getComponent(HealthBarComponent.class).show();
        assertEquals(true, entity.getComponent(HealthBarComponent.class).getHealthBar().isVisible());
    }

    @Test
    void hide() {
        Entity entity = new Entity().addComponent(new HealthBarComponent(true));
        entity.getComponent(HealthBarComponent.class).hide();
        assertEquals(false, entity.getComponent(HealthBarComponent.class).getHealthBar().isVisible());
    }*/
}