package org.kakara.engine.sound;

import org.kakara.engine.resources.Resource;
import org.kakara.engine.sound.exceptions.SoundNotFoundException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * The center point of the Sound System.
 * <p>
 * This allows you to add SoundSources and change the listener position
 */
public class SoundManager {

    private final List<SoundBuffer> soundBufferList = new ArrayList<>();
    private final Map<String, SoundSource> soundSourceMap = new HashMap<>();
    private long device;
    private long context;
    private SoundListener listener;

    /**
     * Load the sound player device.
     */
    public void init() {
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    /**
     * Add a new SoundSOurce
     *
     * @param name        the name of the source.
     * @param soundSource the new soundsource
     */
    public void addSoundSource(String name, SoundSource soundSource) {
        soundSourceMap.put(name, soundSource);
    }

    /**
     * Get the sound source
     *
     * @param name the name of soundsource
     * @return the sound source if found
     */
    public SoundSource getSoundSource(String name) {
        return soundSourceMap.get(name);
    }

    /**
     * Players a sound source if found
     * * @param name sound source to play
     */
    public void playSoundSource(String name) {
        if (listener == null) return;
        SoundSource soundSource = soundSourceMap.get(name);
        if (soundSource == null) {
            throw new SoundNotFoundException("Unable to locate sound: " + name);
        }
        if (!soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    /**
     * Remove a sound source
     *
     * @param name the name of soundsource
     */
    public void removeSoundSource(String name) {
        soundSourceMap.remove(name);
    }

    /**
     * Add a new SoundBuffer
     *
     * @param soundBuffer the new soundBuffer
     */
    public void addSoundBuffer(SoundBuffer soundBuffer) {
        soundBufferList.add(soundBuffer);
    }

    /**
     * Adds/Creates a new SoundBuffer
     *
     * @param resource resource file for sound
     * @return the buffer id
     * @throws Exception If sound buffer failed to load
     */
    public int addNewSoundBuffer(Resource resource) throws Exception {
        SoundBuffer buffer = new SoundBuffer(resource);
        addSoundBuffer(buffer);
        return buffer.getBufferId();
    }

    /**
     * Gets the SoundListener
     *
     * @return the sound listener
     */
    public SoundListener getListener() {
        return listener;
    }

    /**
     * Sets a new Listener
     *
     * @param listener the new listener
     */
    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    /**
     * Cleans up the sound system by removing all buffers and soundsources. It will also close the device
     */
    public void cleanup() {
        for (SoundSource soundSource : soundSourceMap.values()) {
            soundSource.cleanup();
        }
        soundSourceMap.clear();
        for (SoundBuffer soundBuffer : soundBufferList) {
            soundBuffer.cleanup();
        }
        soundBufferList.clear();
        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
}