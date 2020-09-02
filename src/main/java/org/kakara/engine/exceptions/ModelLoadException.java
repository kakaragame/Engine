package org.kakara.engine.exceptions;

/**
 * When the model fails to load.
 */
public class ModelLoadException extends Exception {
    public ModelLoadException(String string) {
        super(string);
    }
}
