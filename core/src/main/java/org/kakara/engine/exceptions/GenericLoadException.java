package org.kakara.engine.exceptions;

/**
 * This is used when a generic load exception occurs.
 *
 * @since 1.0-Pre4
 */
public class GenericLoadException extends RuntimeException {
    public GenericLoadException(String message) {
        super(message);
    }

    public GenericLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
