package org.kakara.engine.sound.exceptions;

/**
 * Exception thrown if sound is not found.
 */
public class SoundNotFoundException extends RuntimeException {
    /**
     * Generic Exception
     *
     * @param s your message
     */
    public SoundNotFoundException(String s) {
        super(s);
    }
}
