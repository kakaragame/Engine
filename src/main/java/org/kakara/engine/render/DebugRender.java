package org.kakara.engine.render;

import org.kakara.engine.math.Vector3;

/**
 * Used to render debug displays.
 * NOT TO BE USED IN PRODUCTION BUILD. REMOVE BEFORE COMPILATION.
 * @deprecated To be removed.
 */
public class DebugRender {
    /**
     * Returns all of the points
     * @param point1
     * @param point2
     * @return
     */
    public static float[] getPositions(Vector3 point1, Vector3 point2){
        float[] positions = {

                point1.x, point1.y + point2.y, point1.z,
                point1.x + point2.x, point1.y + point2.y, point1.z,
                point1.x, point1.y + point2.y, point1.z + point2.z,
                point1.x + point2.x, point1.y + point2.y, point1.z + point2.z,

                point1.x, point1.y, point1.z,
                point1.x + point2.x, point1.y, point1.z,
            point1.x, point1.y, point1.z + point2.z,
            point1.x + point2.x, point1.y, point1.z + point2.z,
            // bottom
                // V8: V4 repeated
                point1.x, point1.y, point1.z,
                // V9: V5 repeated
                point1.x + point2.x, point1.y, point1.z,
                // V10: V0 repeated
                point1.x, point1.y + point2.y, point1.z,
                // V11: V3 repeated
                point1.x + point2.x, point1.y + point2.y, point1.z + point2.z,

                // For text coords in right face
                // V12: V3 repeated
                point1.x + point2.x, point1.y + point2.y, point1.z + point2.z,
                // V13: V2 repeated
                point1.x, point1.y + point2.y, point1.z + point2.z,

                // For text coords in left face
                // V14: V0 repeated
                point1.x, point1.y + point2.y, point1.z,
                // V15: V1 repeated
                point1.x + point2.x, point1.y + point2.y, point1.z,

                // For text coords in bottom face
                // V16: V6 repeated
                point1.x + point2.x, point1.y, point1.z,
                // V17: V7 repeated
                point1.x, point1.y, point1.z + point2.z,
                // V18: V1 repeated
                point1.x + point2.x, point1.y + point2.y, point1.z,
                // V19: V2 repeated
                point1.x, point1.y + point2.y, point1.z + point2.z,

        };
        return positions;
    }

    /**
     * Gets all of the indices.
     * @return
     */
    public static int[] getIndices(){
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7};
        return indices;
    }
}
