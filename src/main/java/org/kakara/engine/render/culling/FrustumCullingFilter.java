package org.kakara.engine.render.culling;

import org.jetbrains.annotations.Nullable;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.collision.ColliderComponent;

/**
 * This class handles the Frustum Culling Code.
 *
 * @since 1.0-Pre3
 */
public class FrustumCullingFilter {
    private final Matrix4f projViewMatrix;
    private final FrustumIntersection frustumInt;

    public FrustumCullingFilter() {
        projViewMatrix = new Matrix4f();
        frustumInt = new FrustumIntersection();
    }

    /**
     * Updates the frustum to represent the current view field.
     *
     * @param projMatrix The projection matrix.
     * @param viewMatrix The view matrix.
     */
    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        projViewMatrix.set(projMatrix);
        projViewMatrix.mul(viewMatrix);
        frustumInt.set(projViewMatrix);
    }

    /**
     * Test a render object against the frustum.
     *
     * @param position The position of the object.
     * @param xs       The x size
     * @param ys       The y size
     * @param zs       The z size
     * @return If the object is visible.
     */
    public boolean testRenderObject(Vector3 position, float xs, float ys, float zs) {
        return frustumInt.testAab(position.toJoml(), position.add(xs, ys, zs).toJoml());
    }

    /**
     * Test a collider against the frustum.
     *
     * @param collider The collider to test.
     * @return If the collider is visible.
     */
    public boolean testCollider(@Nullable ColliderComponent collider) {
        return collider == null ||
                frustumInt.testAab(collider.getAbsolutePoint1().toJoml(), collider.getAbsolutePoint2().toJoml());
    }
}
