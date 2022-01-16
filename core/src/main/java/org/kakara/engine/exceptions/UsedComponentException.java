package org.kakara.engine.exceptions;

/**
 * This occurs when a Component instance already exists on another game item.
 *
 * @since 1.0-Pre5
 */
public class UsedComponentException extends RuntimeException {
    public UsedComponentException(String message) {
        super(message);
    }
}
