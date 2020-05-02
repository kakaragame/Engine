package org.kakara.engine.resources;

import org.apache.commons.io.IOUtils;
import org.kakara.engine.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Get resources from inside of the jar.
 */
public class JarResource implements Resource {
    private String path;
    private URL url;
    private Class<?> justTheClass;
    private String originalPath;

    public JarResource(String path, URL url, Class<?> justTheClass, String originalPath) {
        this.path = path;
        this.url = url;
        this.justTheClass = justTheClass;
        this.originalPath = originalPath;
    }

    public JarResource(String path, URL url, String originalPath) {
        this(path, url, JarResource.class, originalPath);
    }

    public JarResource(String path, String originalPath) {
        this(path, JarResource.class.getResource(path), JarResource.class, originalPath);
    }

    @Override
    public String getOriginalPath() {
        return originalPath;
    }

    @Override
    public InputStream getInputStream() {
        return justTheClass.getResourceAsStream(path);
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public byte[] getByteArray() {
        try {
            return IOUtils.toByteArray(getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        try {
            return Utils.ioResourceToByteBuffer(getPath(), 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return getURL().getPath();
    }
}
