package com.csse3200.game.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class DeathComponentTest {
    private DeathComponent deathComponent;
    private Entity mockEntity;
    private CombatStatsComponent mockCombatStats;
    private HitboxComponent mockHitboxComponent;
    private AnimationRenderComponent mockAnimator;

    @BeforeEach
    void setUp() {
        deathComponent = new DeathComponent();

        mockEntity = mock(Entity.class);
        mockCombatStats = mock(CombatStatsComponent.class);
        mockHitboxComponent = mock(HitboxComponent.class);
        mockAnimator = mock(AnimationRenderComponent.class);
        EventHandler mockEventHandler = mock(EventHandler.class);

        when(mockEntity.getComponent(CombatStatsComponent.class)).thenReturn(mockCombatStats);
        when(mockEntity.getComponent(HitboxComponent.class)).thenReturn(mockHitboxComponent);
        when(mockEntity.getComponent(AnimationRenderComponent.class)).thenReturn(mockAnimator);
        when(mockEntity.getEvents()).thenReturn(mockEventHandler);

        deathComponent.setEntity(mockEntity);
        deathComponent.create();
    }

    @Test
    void shouldNotKillIfNotDead() {
        Fixture mockMe = mock(Fixture.class);
        Fixture mockOther = mock(Fixture.class);

        when(mockHitboxComponent.getFixture()).thenReturn(mockMe);
        when(mockCombatStats.isDead()).thenReturn(false);

        deathComponent.kill(mockMe, mockOther);

        verify(mockAnimator, times(0)).stopAnimation();
        verify(mockEntity.getComponent(HitboxComponent.class), times(0)).setLayer((short) 0);
        verify(mockEntity.getEvents(), times(0)).trigger("dispose");
    }
}

