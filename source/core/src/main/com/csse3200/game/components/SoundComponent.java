package com.csse3200.game.components;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SoundComponent extends Component {
    private final SoundsConfig soundsConfig;
    private final Map<String, Sound> sounds;
    private final Set<String> looping;

    public SoundComponent(SoundsConfig soundsConfig) {
        this.soundsConfig = soundsConfig;
        sounds = new HashMap<>();
        looping = new HashSet<>();
    }

    @Override
    public void create() {
        super.create();

        // loads the sounds and stores them in a map for later access
        for (var entry : soundsConfig.soundsMap.entries()) {
            Sound sound = ServiceLocator.getResourceService().getAsset(entry.value, Sound.class);

            sounds.put(entry.key, sound);
        }

        entity.getEvents().addListener("playSound", this::playSound);
        entity.getEvents().addListener("loopSound", this::loopSound);
        entity.getEvents().addListener("stopSound", this::stopSound);
    }

    public void playSound(String soundName) {
        var sound = sounds.get(soundName);

        if (sound == null) {
            return;
        }

        var settings = UserSettings.get();
        // TODO change to specific volume setting
        sound.play(settings.soundVolume);
    }

    public void loopSound(String soundName) {
        var sound = sounds.get(soundName);

        if (sound == null || looping.contains(soundName)) {
            return;
        }

        var settings = UserSettings.get();
        // TODO change to specific volume setting
        sound.loop(settings.soundVolume);
        looping.add(soundName);
    }

    public void stopSound(String soundName) {
        var sound = sounds.get(soundName);

        if (sound == null) {
            return;
        }

        sound.stop();
        looping.remove(soundName);
    }

    @Override
    public void dispose() {
        super.dispose();

        sounds.values().forEach(Sound::dispose);
    }
}
