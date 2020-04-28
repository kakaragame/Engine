package org.kakara.engine.sound;

import org.kakara.engine.math.Vector3;

import static org.lwjgl.openal.AL10.*;

/**
 * This is a representation of the listener.
 * <p>
 * You want to have the listener be the same location of the camera or your player depending on what the game is doing
 */
public class SoundListener {
    /**
     * A SoundBuffer with with a position of 0,0,0
     */
    public SoundListener() {
        this(new Vector3(0, 0, 0));
    }

    /**
     * A sound buffer with a custom location
     *
     * @param position the location of your listener
     */
    public SoundListener(Vector3 position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);

    }

    /**
     * change the position of the listener
     *
     * @param position new position of the listener
     */
    public void setPosition(Vector3 position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    @Deprecated
    public void setSpeed(Vector3 speed) {
        alListener3f(AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    @Deprecated
    public void setOrientation(Vector3 at, Vector3 up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        alListenerfv(AL_ORIENTATION, data);
    }
}
