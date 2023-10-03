package com.csse3200.game.components;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Companion.CompanionActions;
import com.csse3200.game.components.player.PlayerActions;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PowerupComponentTest {

    @Mock
    EntityService entityService;

    @BeforeEach
    void setup() {
        entityService = mock(EntityService.class);
        ServiceLocator.registerEntityService(entityService);

    }

    @Test
    void shouldApplyExtraLife() {
        Entity playerEntity = new Entity().addComponent(new CombatStatsComponent(100, 10, 1, false, 1))
                .addComponent(new PlayerActions());

        when(entityService.getPlayer()).thenReturn(playerEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.EXTRA_LIFE);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(2, playerEntity.getComponent(CombatStatsComponent.class).getLives());

    }

    @Test
    void shouldApplyHealthBoost() {
        Entity playerEntity = new Entity().addComponent(new CombatStatsComponent(100, 10, 2, false, 3))
                .addComponent(new PlayerActions());

        Entity companionEntity = new Entity().addComponent(new CombatStatsComponent(50, 5, 1, false))
                .addComponent(new CompanionActions());


        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.HEALTH_BOOST);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(100, playerEntity.getComponent(CombatStatsComponent.class).getHealth());
        assertEquals(50, companionEntity.getComponent(CombatStatsComponent.class).getHealth());
    }

    @Test
    void shouldApplySpeedBoost() {
        Entity playerEntity = new Entity().addComponent(new PlayerActions());
        Entity companionEntity = new Entity()
                .addComponent(new CompanionActions())
                .addComponent(new FollowComponent(playerEntity, 5.0f));

        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.SPEED_BOOST);
        powerupComponent.applyEffect();

        Vector2 playerSpeed = new Vector2(6f, 6f);
        Vector2 companionSpeed = new Vector2(7f, 7f);
        // Assert
        assertEquals(playerSpeed, playerEntity.getComponent(PlayerActions.class).getSpeed());
        assertEquals(playerSpeed, playerEntity.getComponent(PlayerActions.class).getSpeed());
        assertEquals(companionSpeed, companionEntity.getComponent(CompanionActions.class).getSpeed());
        assertEquals(companionSpeed, companionEntity.getComponent(CompanionActions.class).getSpeed());
        assertEquals(5, companionEntity.getComponent(FollowComponent.class).getFollowSpeed());
        // Additional assertions based on your actual implementation
    }
    @Test
    void shouldApplyTempImmunity() throws InterruptedException {
        Entity playerEntity = new Entity().addComponent(new PlayerActions()).addComponent(new CombatStatsComponent(100, 10, 1, false, 1));
        Entity companionEntity = new Entity().addComponent(new CompanionActions()).addComponent(new CombatStatsComponent(100, 10, 1, false, 1));
        when(entityService.getPlayer()).thenReturn(playerEntity);
        when(entityService.getCompanion()).thenReturn(companionEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.TEMP_IMMUNITY);
        powerupComponent.applyEffect();

        // Assert
        assertTrue(playerEntity.getComponent(CombatStatsComponent.class).isImmune);
        assertTrue(companionEntity.getComponent(CombatStatsComponent.class).isImmune);

        // Wait for the duration of immunity
        TimeUnit.MILLISECONDS.sleep(7000);

        // Assert after the duration
        assertFalse(playerEntity.getComponent(CombatStatsComponent.class).isImmune);
        assertFalse(companionEntity.getComponent(CombatStatsComponent.class).isImmune);
    }

    @Test
    void shouldApplyDoubleDamage() throws InterruptedException {
        Entity playerEntity = new Entity().addComponent(new PlayerActions()).addComponent(new CombatStatsComponent(100, 10, 1, false, 1));
        when(entityService.getPlayer()).thenReturn(playerEntity);

        PowerupComponent powerupComponent = new PowerupComponent(PowerupType.DOUBLE_DAMAGE);
        powerupComponent.applyEffect();

        // Assert
        assertEquals(2, playerEntity.getComponent(CombatStatsComponent.class).getAttackMultiplier());

        // Wait for the duration of double damage
        TimeUnit.MILLISECONDS.sleep(powerupComponent.getDuration() + 10);

        // Assert after the duration
        assertEquals(1, playerEntity.getComponent(CombatStatsComponent.class).getAttackMultiplier());
    }
}