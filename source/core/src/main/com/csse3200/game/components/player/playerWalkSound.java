package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * This class is responsible for managing the walking sound effects.
 * It provides the method such as start, stop and dispose of the sound
 */

public class playerWalkSound {
    private Sound walkingSound; // sound object for walking sound effect
    private boolean isWalking; // Flag to track whether the player is currently walking

    /**
     *
     * @param walkSoundFile
     * this parameter denotes the walking sound audio file
     */
    public playerWalkSound(String walkSoundFile) {
        walkingSound = Gdx.audio.newSound(Gdx.files.internal(walkSoundFile));
    }

    /**
     * This startWalking method is checking whether the player is walking or not.
     * of player is walking, the below method will start playing sound
     */
    public void startWalking() {
        if (!isWalking) {
            walkingSound.loop();
            isWalking = true;
        }
    }

    /**
     * This stoptWalking method is checking whether the player is walking or not.
     * of player is not walking, the below method will stop the sound.
     */
    public void stopWalking() {
        if (isWalking) {
            walkingSound.stop();
            isWalking = false;
        }
    }

    /**
     * The dispose method should be called when the sound manager
     * is no longer needed to release system resources.
     */
    public void dispose() {
        walkingSound.dispose();
    }
}
