package org.kakara.engine.exceptions.render;

/**
 * When an exception occurs during the render pipeline that is not common enough for its own exception.
 *
 * @since 1.0-Pre4
 */
public class GenericRenderException extends Exception{
    public GenericRenderException(String message){
        super(message);
    }
}
