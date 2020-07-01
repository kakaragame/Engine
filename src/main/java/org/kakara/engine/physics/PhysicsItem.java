package org.kakara.engine.physics;

import org.kakara.engine.math.Vector3;

public interface PhysicsItem {
    void setVelocity(Vector3 velocity);

    void setVelocityX(float x);
    void setVelocityY(float y);
    void setVelocityZ(float z);

    Vector3 getVelocity();

    void setAcceleration(Vector3 acceleration);

    void applyAcceleration(Vector3 acceleration);

    Vector3 getAcceleration();
}
