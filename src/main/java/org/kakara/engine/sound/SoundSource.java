package org.kakara.engine.sound;

import org.kakara.engine.math.Vector3;

import static org.lwjgl.openal.AL10.*;

/**
 * This represents a source of sound. Like that scary monster down the hall
 */
public class SoundSource {
    private final int sourceId;

    /**
     * A new Sound Source
     *
     * @param loop     does it loop
     * @param relative is it relative to the position
     */
    public SoundSource(boolean loop, boolean relative) {
        this.sourceId = alGenSources();
        if (loop) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }
        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }

    }

    /**
     * Set the BufferID
     *
     * @param bufferId the buffer id
     */
    public void setBuffer(int bufferId) {
        stop();
        alSourcei(sourceId, AL_BUFFER, bufferId);
    }

    /**
     * Sets the position of the sound source
     *
     * @param position the position
     */
    public void setPosition(Vector3 position) {
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
    }

    @Deprecated
    public void setSpeed(Vector3 speed) {
        alSource3f(sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    @Deprecated

    public void setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
    }

    @Deprecated

    public void setProperty(int param, float value) {
        alSourcef(sourceId, param, value);
    }

    /**
     * Plays the SoundSource
     */
    public void play() {
        alSourcePlay(sourceId);
    }

    /**
     * Is the sound source playing
     *
     * @return if the sound is playing
     */
    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    /**
     * Pause the sound
     */
    public void pause() {
        alSourcePause(sourceId);
    }

    /**
     * Stop the sound
     * <p>
     * Now the monster is dead
     */
    public void stop() {
        alSourceStop(sourceId);
    }

    /**
     * Clean up the sound source for safe removal
     */
    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
    }
}
