package org.kakara.engine.exceptions.render;

/**
 * When an exception occurs when a shader was not found.
 *
 * @since 1.0-Pre4
 */
public class ShaderNotFoundException extends RuntimeException{
    public ShaderNotFoundException(String message){
        super(message);
    }
}
