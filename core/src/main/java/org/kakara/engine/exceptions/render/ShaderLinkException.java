package org.kakara.engine.exceptions.render;

/**
 * This is used when an exception occurs when linking a shader.
 *
 * @since 1.0-Pre4
 */
public class ShaderLinkException extends Exception {
    public ShaderLinkException(String message) {
        super(message);
    }
}
