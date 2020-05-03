package org.kakara.engine.math;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.kakara.engine.Camera;
import org.kakara.engine.collision.Collidable;

/**
 * Handles the intersection of a ray and blocks.
 * @since 1.0-Pre1
 */
public class Intersection {

    /**
     * Check to see if the collidable intersects with the ray from the camera.
     * @param collidable The collidable
     * @param camera The camera
     * @param result This result stores the near and far values. You can use it to compare the result with other results.
     *               <p>The x value contains the near value, the y value contains the far value.</p>
     * @return If it intersects.
     */
    public static boolean intersect(Collidable collidable, Camera camera, Vector2 result){
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(collidable.getColPosition().toJoml());
        max.set(collidable.getColPosition().toJoml());
        min.add(-collidable.getColScale()/2, -collidable.getColScale()/2, -collidable.getColScale()/2);
        max.add(collidable.getColScale()/2, collidable.getColScale()/2, collidable.getColScale()/2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }

    //todo finish commenting this stuff.
    public static boolean intersect(int x, int y, int z, Camera camera, Vector2 result){
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(new Vector3f(x, y, z));
        max.set(new Vector3f(x, y, z));
        min.add(-1f/2, -1f/2, -1f/2);
        max.add(1f/2, 1f/2, 1f/2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }

    public static boolean intersect(Vector3 position, Camera camera, Vector2 result){
        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        min.set(position.toJoml());
        max.set(position.toJoml());
        min.add(-1f/2, -1f/2, -1f/2);
        max.add(1f/2, 1f/2, 1f/2);
        boolean val = Intersectionf.intersectRayAab(camera.getPosition().toJoml(), dir, min, max, nearFar);
        result.x = nearFar.x;
        result.y = nearFar.y;
        return val;
    }
}
