package org.kakara.engine.exceptions;

/**
 * This exception is thrown when ever something is done in the wrong thread.
 * <p><b>This is a critical exception. It should never be caught and ignored! It is not safe to continue the operation of the program if this is thrown.</b></p>
 *
 * @since 1.0-Pre2
 */
public class InvalidThreadException extends RuntimeException {
    public InvalidThreadException(String message) {
        super(message);
    }
}
