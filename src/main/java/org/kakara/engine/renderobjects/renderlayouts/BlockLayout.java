package org.kakara.engine.renderobjects.renderlayouts;

import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.renderlayouts.types.Indices;
import org.kakara.engine.renderobjects.renderlayouts.types.Normal;
import org.kakara.engine.renderobjects.renderlayouts.types.Texture;
import org.kakara.engine.renderobjects.renderlayouts.types.Vertex;

/**
 * The class that stores the layouts for the blocks in the render chunks
 */
public class BlockLayout implements Layout {

    @Override
    public Vertex getVertex(final Vector3 pos) {
        return new Vertex() {
            @Override
            public float[] getFront() {
                return new float[] {-0.5f + pos.x, 0.5f + pos.y, 0.5f + pos.z,
                        -0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, 0.5f + pos.y, 0.5f + pos.z};
            }
            @Override
            public float[] getBack() {
                return new float[]{
                        -0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z,
                        -0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z,
                        0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z
                };
            }
            @Override
            public float[] getTop() {
                return new float[]{ -0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z,
                        -0.5f+ pos.x, 0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, 0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z};
            }
            @Override
            public float[] getBottom() {
                return new float[]{ -0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z,
                        -0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z};
            }
            @Override
            public float[] getRight() {
                return new float[]{0.5f+ pos.x, 0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z,
                        0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z};
            }
            @Override
            public float[] getLeft() {
                return new float[]{-0.5f+ pos.x, 0.5f + pos.y, -0.5f + pos.z,
                        -0.5f+ pos.x, -0.5f + pos.y, -0.5f + pos.z,
                        -0.5f+ pos.x, -0.5f + pos.y, 0.5f + pos.z,
                        -0.5f+ pos.x, 0.5f + pos.y, 0.5f + pos.z};
            }
        };
    }

    @Override
    public Texture getTextureCords() {
        return new Texture() {
            @Override
            public float[] getFront(float xOffset, float yOffset, int rows) {
                return new float[]{
                        0.25f/rows + xOffset, 0.33f/rows + yOffset,
                        0.25f/rows + xOffset, 0.66f/rows + yOffset,
                        0.5f/rows + xOffset, 0.66f/rows + yOffset,
                        0.5f/rows + xOffset, 0.33f/rows + yOffset
                };
            }

            @Override
            public float[] getBack(float xOffset, float yOffset, int rows) {
                return new float[]{
                        1f/rows + xOffset, 0.33f/rows + yOffset,
                        1f/rows + xOffset, 0.66f/rows + yOffset,
                        0.75f/rows + xOffset, 0.66f/rows + yOffset,
                        0.75f/rows + xOffset, 0.33f/rows + yOffset
                };
            }

            @Override
            public float[] getTop(float xOffset, float yOffset, int rows) {
                return new float[]{
                        0.25f/rows + xOffset, 0 + yOffset,
                        0.25f/rows + xOffset, 0.33f/rows + yOffset,
                        0.5f/rows + xOffset, 0.33f/rows + yOffset,
                        0.5f/rows + xOffset, 0 + yOffset
                };
            }

            @Override
            public float[] getBottom(float xOffset, float yOffset, int rows) {
                return new float[]{
                        0.25f/rows + xOffset, 0.66f/rows + yOffset,
                        0.25f/rows + xOffset, 1f/rows + yOffset,
                        0.5f/rows + xOffset, 1f/rows + yOffset,
                        0.5f/rows + xOffset, 0.66f/rows + yOffset
                };
            }

            @Override
            public float[] getRight(float xOffset, float yOffset, int rows) {
                return new float[]{
                        0.5f/rows + xOffset, 0.33f/rows + yOffset,
                        0.5f/rows + xOffset, 0.66f/rows + yOffset,
                        0.75f/rows + xOffset, 0.66f/rows + yOffset,
                        0.75f/rows + xOffset, 0.33f/rows + yOffset
                };
            }

            @Override
            public float[] getLeft(float xOffset, float yOffset, int rows) {
                return new float[]{
                        0 + xOffset, 0.33f/rows + yOffset,
                        0 + xOffset, 0.66f/rows + yOffset,
                        0.25f/rows + xOffset, 0.66f/rows + yOffset,
                        0.25f/rows + xOffset, 0.33f/rows + yOffset
                };
            }
        };
    }

    @Override
    public Normal getNormal() {
        return new Normal() {
            @Override
            public float[] getFront() {
                return new float[]{
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1,
                        0, 0, 1
                };
            }

            @Override
            public float[] getBack() {
                return new float[]{
                        0, 0, -1,
                        0, 0, -1,
                        0, 0, -1,
                        0, 0, -1
                };
            }

            @Override
            public float[] getTop() {
                return new float[]{
                        0, 1, 0,
                        0, 1, 0,
                        0, 1, 0,
                        0, 1, 0
                };
            }

            @Override
            public float[] getBottom() {
                return new float[]{
                        0, -1, 0,
                        0, -1, 0,
                        0, -1, 0,
                        0, -1, 0
                };
            }

            @Override
            public float[] getRight() {
                return new float[]{
                        1, 0, 0,
                        1, 0, 0,
                        1, 0, 0,
                        1, 0, 0
                };
            }

            @Override
            public float[] getLeft() {
                return new float[]{
                        -1, 0, 0,
                        -1, 0, 0,
                        -1, 0, 0,
                        -1, 0, 0
                };
            }
        };
    }

    @Override
    public Indices getIndices() {
        return new Indices() {
            @Override
            public int[] getFront(int i) {
                return new int[]{i, i+1, i+2, i+2, i+3, i};
            }

            @Override
            public int[] getBack(int i) {
                return new int[]{i, i+3, i+2, i+2, i+1, i};
            }

            @Override
            public int[] getTop(int i) {
                return new int[]{i, i+1, i+2, i+2, i+3, i};
            }

            @Override
            public int[] getBottom(int i) {
                return new int[]{i, i+3, i+2, i+2, i+1, i};
            }

            @Override
            public int[] getRight(int i) {
                return new int[]{i, i+1, i+2, i+2, i+3, i};
            }

            @Override
            public int[] getLeft(int i) {
                return new int[]{i, i+1, i+2, i+2, i+3, i};
            }
        };
    }
}
