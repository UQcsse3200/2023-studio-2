package com.csse3200.game.components;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.MiniDisplay.MiniScreenDisplay;
import com.csse3200.game.entities.configs.SoundsConfig;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This component can be used to trigger sounds by an entity to play.
 */
public class SoundComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(SoundComponent.class);
    private final SoundsConfig soundsConfig;
    private final Map<String, Sound> sounds;
    private final Set<String> looping;

    /**
     * Creates a new SoundComponent using the sounds specified in the soundsConfig file.
     * @param soundsConfig - the config file containing the sounds which can be played.
     */
    public SoundComponent(SoundsConfig soundsConfig) {
        this.soundsConfig = soundsConfig;
        sounds = new HashMap<>();
        looping = new HashSet<>();
    }

    /**
     * Creates a new SoundComponent which contains the specified sound
     *
     * @param soundId - the id of the given sound
     * @param soundPath - the path to locate the sound
     */
    public SoundComponent(String soundId, String soundPath) {
        this.soundsConfig = new SoundsConfig();
        this.soundsConfig.soundsMap.put(soundId, soundPath);
        sounds = new HashMap<>();
        looping = new HashSet<>();
    }

    /**
     * Loads the sounds specified in the config file into a hashmap for later use.
     */
    @Override
    public void create() {
        super.create();

        // loads the sounds and stores them in a map for later access
        for (var entry : soundsConfig.soundsMap.entries()) {
            logger.debug("Entity {} loading in sound {} with id {}", entity.toString(), entry.value, entry.key);

            Sound sound = ServiceLocator.getResourceService().getAsset(entry.value, Sound.class);

            if (sound == null) {
                // debug log will occur in the ResourceService class
                continue;
            }

            sounds.put(entry.key, sound);
        }

        entity.getEvents().addListener("playSound", this::playSound);
        entity.getEvents().addListener("loopSound", this::loopSound);
        entity.getEvents().addListener("stopSound", this::stopSound);
    }

    /**
     * Plays the sound which soundName maps to if it exists. If soundName does not map to
     * a loaded sound, nothing happens.
     * @param soundName - the name of the sound to play as specified in the config file.
     */
    public void playSound(String soundName) {
        logger.debug("Entity {} playing sound {}", entity.toString(), soundName);

        var sound = sounds.get(soundName);

        if (sound == null) {
            logger.warn("Sound {} does not exist for entity {}", soundName, entity.toString());
            return;
        }

        var settings = UserSettings.get();
        sound.play(settings.soundVolume);
    }

    /**
     * Loops the sound which soundName maps to if it exists. If soundName does not map to
     * a loaded sound, nothing happens.
     * @param soundName - the name of the sound to loop as specified in the config file.
     */
    public void loopSound(String soundName) {
        if (looping.contains(soundName)) {
            return;
        }

        logger.debug("Begin looping sound {} for entity {}", soundName, entity.toString());
        var sound = sounds.get(soundName);

        if (sound == null) {
            logger.warn("Sound {} does not exist for entity {}", soundName, entity.toString());
            return;
        }

        var settings = UserSettings.get();
        sound.loop(settings.soundVolume);
        looping.add(soundName);
    }

    /**
     * Stops the sound which soundName maps to if it exists. If soundName does not map to
     * a loaded sound, nothing happens. If the specified sound is not playing, nothing happens.
     * @param soundName - the name of the sound to stop as specified in the config file.
     */
    public void stopSound(String soundName) {
        logger.debug("Stop sound {} for entity {}", soundName, entity.toString());
        var sound = sounds.get(soundName);

        if (sound == null) {
            logger.warn("Sound {} does not exist for entity {}", soundName, entity.toString());
            return;
        }

        sound.stop();
        looping.remove(soundName);
    }
}
