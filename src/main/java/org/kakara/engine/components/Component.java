package org.kakara.engine.components;

import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.physics.collision.ColliderComponent;

public abstract class Component {

    private GameItem gameItem;

    // TODO implement start and update.
    public abstract void start();
    public abstract void update();

    public void physicsUpdate(float deltaTime) {}

    public void cleanup(){}

    public void onRemove() {}

    public void onCollision(ColliderComponent other){}

    public final void init(GameItem item){
        // TODO implement non generic exception.
        if(gameItem != null)
            throw new RuntimeException("This component already has a GameItem!");
        this.gameItem = item;

        afterInit();
    }

    public void afterInit(){}

    public final <T extends Component> T getComponent(Class<T> component){
        return gameItem.getComponent(component);
    }

    public final <T extends Component> T addComponent(Class<T> component){
        return gameItem.addComponent(component);
    }

    public final GameItem getGameItem(){
        return gameItem;
    }
}
