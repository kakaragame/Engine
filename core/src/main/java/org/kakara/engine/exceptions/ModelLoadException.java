package org.kakara.engine.exceptions;

/**
 * When the model fails to load.
 */
public class ModelLoadException extends Exception {
    public ModelLoadException() {
    }

    public ModelLoadException(String message) {
        super(message);
    }

    public ModelLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelLoadException(Throwable cause) {
        super(cause);
    }

    public ModelLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
