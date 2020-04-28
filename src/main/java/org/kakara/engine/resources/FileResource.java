package org.kakara.engine.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * This resource is to get information from files (not in a jar).
 * <p>See {@link JarResource} for the jar code.</p>
 */
public class FileResource implements Resource {
    private URL url;

    public FileResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] getByteArray() {
        try {
            return getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return ByteBuffer.wrap(getByteArray());
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getPath() {
        return url.getPath();
    }

    @Override
    public String toString() {
        return url.getPath();
    }
}
