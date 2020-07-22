package org.kakara.engine.item.mesh;

import org.kakara.engine.item.GameItem;
import org.kakara.engine.item.Material;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The main mesh interface.
 * @since 1.0-Pre1
 */
public interface IMesh {
    void render();
    void cleanUp();
    Optional<Material> getMaterial();
    void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer);
    void setWireframe(boolean value);
    boolean isWireframe();
}
