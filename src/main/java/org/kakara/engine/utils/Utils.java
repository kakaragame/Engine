package org.kakara.engine.utils;

import org.kakara.engine.gameitems.Texture;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static org.lwjgl.BufferUtils.createByteBuffer;

/**
 * A class full of important Utilities
 */
public class Utils {
    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    /**
     * Convert a resource into a File.
     *
     * @param url The resource
     * @return The file
     * @throws URISyntaxException
     */
    public static File getFileFromResource(URL url) throws URISyntaxException {
        return new File(url.toURI());
    }

    /**
     * Such a hacky thing. We need to fix this.
     * TODO fix this
     *
     * @param url The url to remove
     * @return The removal of file:/ and jar:
     */
    public static String removeFile(String url) {
        return url.replace("file:/", "").replace("jar:", "");
    }

    /**
     * Convert a list into an int array
     *
     * @param list The list
     * @return The array
     */
    public static int[] listIntToArray(List<Integer> list) {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

    /**
     * Convert a float list to a float array
     *
     * @param list The list
     * @return The array
     */
    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    /**
     * Check if a resource file exists
     *
     * @param fileName The file name
     * @return if it exists
     * @deprecated unused
     */
    public static boolean existsResourceFile(String fileName) {
        boolean result;
        try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
            result = is != null;
        } catch (Exception excp) {
            result = false;
        }
        return result;
    }

    /**
     * Convert a resource ot byte buffer
     *
     * @param resource   The file path
     * @param bufferSize The buffer size
     * @return The byte buffer.
     * @throws IOException If the file can't be read or does not exist.
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = Utils.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    /**
     * Resize a buffer.
     *
     * @param buffer      The buffer to resize
     * @param newCapacity The new capacity
     * @return The new buffer.
     */
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Convert an inputstream into a texture.
     *
     * @param io The input stream.
     * @return The texture.
     * @throws IOException If a file does not exist or can't be read.
     */
    public static Texture inputStreamToTexture(InputStream io) throws IOException {
        byte[] ioBytes = io.readAllBytes();
        ByteBuffer bb = MemoryUtil.memAlloc(ioBytes.length);
        bb.put(ioBytes);
        bb.flip();
        return new Texture(bb);
    }

    /**
     * Convert an inputstream into a bytebuffer.
     *
     * @param io The input stream.
     * @return The byte buffer.
     * @throws IOException If a file does not exist or can't be read.
     */
    public static ByteBuffer inputStreamToByteBuffer(InputStream io) throws IOException {
        byte[] ioBytes = io.readAllBytes();
        ByteBuffer bb = MemoryUtil.memAlloc(ioBytes.length);
        bb.put(ioBytes);
        bb.flip();
        return bb;
    }

    /**
     * Get the current directory
     *
     * @return The current directory.
     */
    public static File getCurrentDirectory() {
        File file = new File(".");
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file.getAbsoluteFile();
        }
    }
}
