package org.kakara.engine.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A Math utility class to provide methods for useful math calculations.
 */
public class KMath {

    /**
     * The maximum error a float could reasonably have. (Not to be used for exact calculations.) <br>
     * Use the following instead:
     * @see java.math.BigDecimal
     */
    public static final float FLOAT_MAX_ERROR = 0.00001f;
    /**
     * The minimum error a float could reasonably have. (Not to be used for exact calculations.) <br>
     * Use the following instead:
     * @see java.math.BigDecimal
     */
    public static final float FLOAT_MIN_ERROR = -0.00001f;

    /**
     * Find the distance between two vectors.
     * @param point1 The first point
     * @param point2 The second point
     * @return The distance (always positive)
     */
    public static float distance(Vector3 point1, Vector3 point2){
        return (float) Math.sqrt(Math.pow(point2.x - point1.x, 2) + Math.pow(point2.y - point1.y, 2) + Math.pow(point2.z - point1.z, 2));
    }

    /**
     * Find the distance between two set of points.
     * @param x1 The first x
     * @param y1 The first y
     * @param z1 The first z
     * @param x2 The second x
     * @param y2 The second y
     * @param z2 The second z
     * @return The distance (always positive)
     */
    public static float distance(float x1, float y1, float z1, float x2, float y2, float z2){
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }

    /**
     * Truncate a float by x places.
     * @param input The float
     * @param places The number of places
     * @return The truncated float.
     */
    public static float truncate(float input, int places){
        BigDecimal bd = new BigDecimal(input).setScale(places, RoundingMode.DOWN).stripTrailingZeros();
        return bd.floatValue();
    }

    /**
     * Convert Eular to Quaternion
     * <p>It is suggested to use Quaternions instead of Eulars in general.</p>
     * @param rotation The vector3 rotation.
     * @return The quaternion.
     */
    public static Quaternionf eularToQuaternion(Vector3 rotation){
        double qx = Math.sin(rotation.z/2) * Math.cos(rotation.y/2) * Math.cos(rotation.x/2) - Math.cos(rotation.z/2) * Math.sin(rotation.y/2) * Math.sin(rotation.x/2);
        double qy = Math.cos(rotation.z/2) * Math.sin(rotation.y/2) * Math.cos(rotation.x/2) + Math.sin(rotation.z/2) * Math.cos(rotation.y/2) * Math.sin(rotation.x/2);
        double qz = Math.cos(rotation.z/2) * Math.cos(rotation.y/2) * Math.sin(rotation.x/2) - Math.sin(rotation.z/2) * Math.sin(rotation.y/2) * Math.cos(rotation.x/2);
        double qw = Math.cos(rotation.z/2) * Math.cos(rotation.y/2) * Math.cos(rotation.x/2) + Math.sin(rotation.z/2) * Math.sin(rotation.y/2) * Math.sin(rotation.x/2);
        return new Quaternionf((float)qz, (float)qy, (float)qx, (float)qw);
    }

    /**
     * Convert Quaternions to Eular angles.
     * @param q The Quaternion
     * @return The Vector3 Eular angles.
     */
    public static Vector3 quaternionToEular(Quaternionf q){
        Vector3f eular = new Vector3f();
        q.getEulerAnglesXYZ(eular);
        return new Vector3(eular);
    }
}
