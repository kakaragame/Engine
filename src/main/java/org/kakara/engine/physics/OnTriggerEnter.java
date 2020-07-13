package org.kakara.engine.physics;

import org.kakara.engine.physics.collision.Collidable;

public interface OnTriggerEnter {
    void onTriggerEnter(Collidable other);
}
