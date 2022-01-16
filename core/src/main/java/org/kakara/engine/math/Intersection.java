package org.kakara.engine.math;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.kakara.engine.Camera;
import org.kakara.engine.gameitems.GameItem;

/**
 * Handles the intersection of a ray and blocks.
 *
 * @since 1.0-Pre1
 */
public class Intersection {

    private Intersection() {
    }

    /**
     * Check to see if the collidable intersects with the ray from the camera.
     *
     * @param collidable The collidable
     * @param camera     The camera
     * @param result     This result vector is mutated and stores the near and far values. You can use it to compare the result with other results.
     *                   <p>The x value contains the near value, the y value contains the far value.</p>
     * @return If the way intersects with the collidable.
     */
    public static boolean intersect(GameItem collidable, Camera camera, Vector2 result) {
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(collidable.transform.getPosition().toJoml());
        max.set(collidable.transform.getPosition().toJoml());
        Vector3 scale = collidable.transform.getScale();
        min.add(-scale.x / 2, -scale.y / 2, -scale.z / 2);
        max.add(scale.x / 2, scale.y / 2, scale.z / 2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }

    /**
     * Check to see if a point intersects with a ray from the camera.
     *
     * @param x      The x value of the point.
     * @param y      The y value of the point.
     * @param z      The z value of the point.
     * @param camera The camera.
     * @param result This result vector is mutated and stores the near and far values. You can use it to compare the result with other results.
     *               <p>The x value contains the near value, the y value contains the far value.</p>
     * @return If the ray intersects with the point.
     */
    public static boolean intersect(int x, int y, int z, Camera camera, Vector2 result) {
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(new Vector3f(x, y, z));
        max.set(new Vector3f(x, y, z));
        min.add(-1f / 2, -1f / 2, -1f / 2);
        max.add(1f / 2, 1f / 2, 1f / 2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }

    /**
     * Check to see if a vector intersects with a ray from the camera.
     *
     * @param position The vector
     * @param camera   The camera
     * @param result   This result vector is mutated and stores the near and far values. You can use it to compare the result with other results.
     *                 <p>The x value contains the near value, the y value contains the far value.</p>
     * @return If the ray intersects with the vector.
     */
    public static boolean intersect(Vector3 position, Camera camera, Vector2 result) {
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(position.toJoml());
        max.set(position.toJoml());
        min.add(-1f / 2, -1f / 2, -1f / 2);
        max.add(1f / 2, 1f / 2, 1f / 2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }
}
