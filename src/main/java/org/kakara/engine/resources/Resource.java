package org.kakara.engine.resources;

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Handles the different types of resources that can be obtained.
 * <p>This system is to be used when a file is to be obtained for use.</p>
 */
public interface Resource {
    /**
     * Get the input stream.
     * @return The input stream.
     */
    InputStream getInputStream();

    /**
     * Get the URL for the resource
     * @return the url.
     */
    URL getURL();

    /**
     * Get the path
     * @return The path
     */
    String getPath();

    /**
     * Get the byte array of the resource
     * @return The byte array
     */
    byte[] getByteArray();

    /**
     * Get the byte buffer of the resource
     * @return The byte buffer.
     */
    ByteBuffer getByteBuffer();
}