package org.kakara.engine.gameitems.mesh;

import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.Material;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The main mesh interface.
 *
 * @since 1.0-Pre1
 */
public interface IMesh {
    void render();

    void cleanUp();

    Optional<Material> getMaterial();

    void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer);

    boolean isWireframe();

    void setWireframe(boolean value);
}
