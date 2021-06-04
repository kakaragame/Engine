package org.kakara.engine.test2;

import org.kakara.engine.components.Component;

public class RotateCube extends Component {
    @Override
    public void start() {

    }

    @Override
    public void update() {
        getGameItem().transform.getRotation().rotateXYZ((float) Math.toRadians(5), (float) Math.toRadians(5), 0);
    }
}
