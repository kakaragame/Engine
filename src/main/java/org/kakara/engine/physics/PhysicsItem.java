package org.kakara.engine.physics;

import org.kakara.engine.Camera;
import org.kakara.engine.math.Vector3;

/**
 * An item that can be interacted with by the Physics engine.
 * @since 1.0-Pre2
 */
public interface PhysicsItem {
    void setVelocity(Vector3 velocity);

    void setVelocityX(float x);
    void setVelocityY(float y);
    void setVelocityZ(float z);

    void setVelocityByCamera(Vector3 velocity, Camera camera);

    Vector3 getVelocity();

    void setAcceleration(Vector3 acceleration);

    void applyAcceleration(Vector3 acceleration);

    Vector3 getAcceleration();
}
