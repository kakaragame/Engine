package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.Component;

public class Resolver extends Component {
    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void onCollision(ColliderComponent other) {
        CollisionManager cm = GameHandler.getInstance().getCurrentScene().getCollisionManager();
        assert cm != null;

        CollisionManager.Contact contact = cm.isColliding(other, getGameItem().getComponent(ColliderComponent.class));
        while (contact.isIntersecting()) {
            contact = cm.isCollidingX(other, getGameItem().getComponent(ColliderComponent.class));
            getGameItem().transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
        }
    }
}
