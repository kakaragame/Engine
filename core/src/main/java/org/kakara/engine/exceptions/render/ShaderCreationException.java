package org.kakara.engine.exceptions.render;

/**
 * This is used when an exception occurs during shader creation.
 *
 * @since 1.0-Pre4
 */
public class ShaderCreationException extends Exception {
    public ShaderCreationException(String message) {
        super(message);
    }
}
