package org.kakara.engine.engine;

/**
 * Contains all of the data for a cube.
 * <p>Pre-made cube data for your pleasure.</p>
 */
public final class CubeData {

    public static final float[] vertex = {
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            //back
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            //top
            -0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            //bottom
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            //right
            0.5f, 0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,

            // left
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f
    };

    public static final float[] texture = {
            0.25f, 0.33f,
            0.25f, 0.66f,
            0.5f, 0.66f,
            0.5f, 0.33f,
            //back
            1f, 0.33f,
            1f, 0.66f,
            0.75f, 0.66f,
            0.75f, 0.33f,
            //top
            0.25f, 0,
            0.25f, 0.33f,
            0.5f, 0.33f,
            0.5f, 0,
            // bottom
            0.25f, 0.66f,
            0.25f, 1,
            0.5f, 1,
            0.5f, 0.66f,
            // right
            0.5f, 0.33f,
            0.5f, 0.66f,
            0.75f, 0.66f,
            0.75f, 0.33f,
            // left
            0, 0.33f,
            0, 0.66f,
            0.25f, 0.66f,
            0.25f, 0.33f
    };

    public static final float[] normal = {
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            // back
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            // Top
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            // bottom
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            // right
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            // left
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0
    };

    public static final int[] indices = {
            0, 1, 2, 2, 3, 0,
            // back
            4, 7, 6, 6, 5, 4,
            // top
            8, 9, 10, 10, 11, 8,
            // bottom
            12, 15, 14, 14, 13, 12,
            // right
            16, 17, 18, 18, 19, 16,
            // left
            20, 21, 22, 22, 23, 20
    };

    public static final float[] skyboxVertex = {
            -100f, 100f, 100f,
            -100f, -100f, 100f,
            100f, -100f, 100f,
            100f, 100f, 100f,
            //back
            -100f, 100f, -100f,
            -100f, -100f, -100f,
            100f, -100f, -100f,
            100f, 100f, -100f,
            //top
            -100f, 100f, -100f,
            -100f, 100f, 100f,
            100f, 100f, 100f,
            100f, 100f, -100f,
            //bottom
            -100f, -100f, -100f,
            -100f, -100f, 100f,
            100f, -100f, 100f,
            100f, -100f, -100f,
            //right
            100f, 100f, 100f,
            100f, -100f, 100f,
            100f, -100f, -100f,
            100f, 100f, -100f,

            // left
            -100f, 100f, -100f,
            -100f, -100f, -100f,
            -100f, -100f, 100f,
            -100f, 100f, 100f
    };
}
