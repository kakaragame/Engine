package org.kakara.engine.sound;

import org.jetbrains.annotations.NotNull;
import org.kakara.engine.resources.JarResource;
import org.kakara.engine.resources.Resource;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Handles a Sound File.
 */
public class SoundBuffer {
    private final int bufferId;
    private ShortBuffer pcm = null;

    /**
     * SoundBuffer
     *
     * @param file resource for the sound file
     * @throws Exception thrown if unable to read resource file
     */
    public SoundBuffer(@NotNull Resource file) throws Exception {
        this.bufferId = alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(file, info);


            // Copy to buffer
            alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
    }

    /**
     * The Buffer ID inside OPENGL
     *
     * @return the Buffer Id in OpenGL
     */
    public int getBufferId() {
        return this.bufferId;
    }

    /**
     * Deletes the Buffer
     */
    public void cleanup() {
        alDeleteBuffers(this.bufferId);
        if (pcm != null) {
            MemoryUtil.memFree(pcm);
        }
    }

    private ShortBuffer readVorbis(Resource resource, STBVorbisInfo info) throws Exception {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            long decoder;
            IntBuffer error = stack.mallocInt(1);

            if (resource instanceof JarResource) {
                ByteBuffer vorbis = resource.getByteBuffer();
                decoder = stb_vorbis_open_memory(vorbis, error, null);
            } else {
                decoder = stb_vorbis_open_filename(resource.getPath(), error, null);
            }
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }

}
