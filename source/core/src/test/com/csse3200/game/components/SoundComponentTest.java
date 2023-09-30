package com.csse3200.game.components;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.events.EventHandler;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(GameExtension.class)
class SoundComponentTest {
    @Mock
    ResourceService resourceService;
    @Mock
    Sound sound1;
    @Mock
    Sound sound2;
    @Mock
    Sound sound3;
    @Mock
    Entity entity;
    @Mock
    EventHandler events;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerResourceService(resourceService);
        when(resourceService.getAsset("sound_1_file", Sound.class)).thenReturn(sound1);
        when(resourceService.getAsset("sound_2_file", Sound.class)).thenReturn(sound2);
        when(resourceService.getAsset("sound_3_file", Sound.class)).thenReturn(sound3);

        when(entity.getEvents()).thenReturn(events);
    }

    SoundComponent getComponent() {
        SoundsConfig config = new SoundsConfig();
        config.soundsMap.put("sound_1", "sound_1_file");
        config.soundsMap.put("sound_2", "sound_2_file");
        config.soundsMap.put("sound_3", "sound_3_file");

        var component = new SoundComponent(config);
        component.setEntity(entity);

        return component;
    }

    @Test
    void testCreate() {
        var component = getComponent();
        component.create();

        // verify all sounds are fetched
        verify(resourceService, times(1)).getAsset("sound_1_file", Sound.class);
        verify(resourceService, times(1)).getAsset("sound_2_file", Sound.class);
        verify(resourceService, times(1)).getAsset("sound_3_file", Sound.class);

        // verify all listeners are added
        verify(events, times(1)).addListener(eq("playSound"), isA(EventListener1.class));
        verify(events, times(1)).addListener(eq("loopSound"), isA(EventListener1.class));
        verify(events, times(1)).addListener(eq("stopSound"), isA(EventListener1.class));
    }

    @Test
    void testPlaySound() {
        testPlaySoundWithVolume(1f);
        testPlaySoundWithVolume(0.5f);
        testPlaySoundWithVolume(0f);
    }

    void testPlaySoundWithVolume(float volume) {
        reset(sound1, sound2, sound3);

        var settings = new UserSettings.Settings();
        settings.soundVolume = volume;

        UserSettings.set(settings, true);

        var component = getComponent();
        component.create();

        component.playSound("sound_1");

        verify(sound1, times(1)).play(volume);


        component.playSound("sound_2");

        verify(sound2, times(1)).play(volume);


        component.playSound("sound_3");

        verify(sound3, times(1)).play(volume);
    }

    @Test
    void loopSound() {
        testLoopSoundWithVolume(1f);
        testLoopSoundWithVolume(0.5f);
        testLoopSoundWithVolume(0f);
    }

    void testLoopSoundWithVolume(float volume) {
        reset(sound1, sound2, sound3);

        var settings = new UserSettings.Settings();
        settings.soundVolume = volume;

        UserSettings.set(settings, true);

        var component = getComponent();
        component.create();

        component.loopSound("sound_1");

        verify(sound1, times(1)).loop(volume);


        component.loopSound("sound_2");

        verify(sound2, times(1)).loop(volume);


        component.loopSound("sound_3");

        verify(sound3, times(1)).loop(volume);
    }

    @Test
    void stopSound() {
        var component = getComponent();
        component.create();

        component.stopSound("sound_1");

        verify(sound1, times(1)).stop();


        component.stopSound("sound_2");

        verify(sound2, times(1)).stop();


        component.stopSound("sound_3");

        verify(sound3, times(1)).stop();
    }
}