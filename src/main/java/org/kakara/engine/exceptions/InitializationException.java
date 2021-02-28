package org.kakara.engine.exceptions;

/**
 * This exception occurs when something fails in the initialization process. (This is only used if none of the other
 * exceptions such as: GenericLoadException or ShaderCreationException apply.)
 *
 * @since 1.0-Pre4
 */
public class InitializationException extends Exception {
    public InitializationException(String message) {
        super(message);
    }
}
