package org.kakara.engine.exceptions;

/**
 * When the texture fails to load.
 */
public class TextureException extends RuntimeException {
    public TextureException(String s) {
        super(s);
    }

    public TextureException(Exception e) {
        super(e);
    }

    public TextureException(String s, Exception e) {
        super(s, e);
    }
}
