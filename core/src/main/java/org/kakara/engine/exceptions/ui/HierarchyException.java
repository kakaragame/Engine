package org.kakara.engine.exceptions.ui;

/**
 * This is used when there is an error with the Hierarchy of the UI.
 *
 * @since 1.0-Pre4
 */
public class HierarchyException extends RuntimeException {
    public HierarchyException(String message) {
        super(message);
    }
}
