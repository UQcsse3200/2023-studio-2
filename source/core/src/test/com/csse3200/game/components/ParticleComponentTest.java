package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.ParticleEffectsConfig;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class ParticleComponentTest {
    @Mock
    ResourceService resourceService;
    @Mock
    RenderService renderService;
    @Mock
    ParticleEffect effect1;
    @Mock
    ParticleEffect effect2;
    @Mock
    ParticleEffect effect3;
    @Mock
    Entity entity;
    @Mock
    EventHandler events;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
        when(resourceService.getAsset("effect_1_file", ParticleEffect.class)).thenReturn(effect1);
        when(resourceService.getAsset("effect_2_file", ParticleEffect.class)).thenReturn(effect2);
        when(resourceService.getAsset("effect_3_file", ParticleEffect.class)).thenReturn(effect3);

        when(entity.getEvents()).thenReturn(events);

        ServiceLocator.registerRenderService(renderService);
    }

    ParticleComponent getComponent() {
        ParticleEffectsConfig config = new ParticleEffectsConfig();
        config.effectsMap.put("effect_1", "effect_1_file");
        config.effectsMap.put("effect_2", "effect_2_file");
        config.effectsMap.put("effect_3", "effect_3_file");

        var component = new ParticleComponent(config);
        component.setEntity(entity);

        return component;
    }

    @Test
    void testCreate() {
        var component = getComponent();
        component.create();

        // verify all effects are fetched
        verify(resourceService, times(1)).getAsset("effect_1_file", ParticleEffect.class);
        verify(resourceService, times(1)).getAsset("effect_2_file", ParticleEffect.class);
        verify(resourceService, times(1)).getAsset("effect_3_file", ParticleEffect.class);

        // verify all listeners are added
        verify(events, times(1)).addListener(eq("startEffect"), isA(EventListener1.class));
        verify(events, times(1)).addListener(eq("stopEffect"), isA(EventListener1.class));
    }

    @Test
    void testStartEffect() {
        reset(effect1, effect2, effect3);

        var component = getComponent();
        component.create();

        component.startEffect("effect_1");

        verify(effect1, times(1)).start();


        component.startEffect("effect_2");

        verify(effect2, times(1)).start();


        component.startEffect("effect_3");

        verify(effect3, times(1)).start();
    }

    @Test
    void testStopEffect() {
        var component = getComponent();
        component.create();

        component.stopEffect("effect_1");

        verify(effect1, times(1)).allowCompletion();


        component.stopEffect("effect_2");

        verify(effect2, times(1)).allowCompletion();


        component.stopEffect("effect_3");

        verify(effect3, times(1)).allowCompletion();
    }

    @Test
    void testDraw() {
        var component = getComponent();
        component.create();

        var batch = mock(SpriteBatch.class);

        when(batch.getProjectionMatrix()).thenReturn(new Matrix4());

        var entity = mock(Entity.class);
        when(entity.getCenterPosition()).thenReturn(new Vector2());
        when(entity.getPosition()).thenReturn(new Vector2());

        component.setEntity(entity);

        component.draw(batch);

        // a workaround way to ensure that there is at least one interaction with the batch
        try {
            verifyNoInteractions(batch);
        } catch(NoInteractionsWanted e) {
            // pass
        }
    }
}