package org.kakara.engine.exceptions.render;

/**
 * This is used when an exception occurs when creating or using a uniform.
 *
 * @since 1.0-Pre4
 */
public class ShaderUniformException extends Exception {
    public ShaderUniformException(String message) {
        super(message);
    }
}
