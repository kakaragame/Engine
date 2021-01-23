package org.kakara.engine.gameitems.mesh;

import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.old_GameItem;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.render.culling.FrustumCullingFilter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The main mesh interface.
 *
 * @since 1.0-Pre1
 */
public interface IMesh {
    /**
     * Render the mesh.
     *
     * <p>For internal use only.</p>
     */
    void render();

    /**
     * Cleanup the mesh.
     *
     * <p>The should be triggered automatically by the engine when the object is delete or the scene is unloaded.</p>
     */
    void cleanUp();

    /**
     * Get the material used by a Mesh.
     *
     * @return The Optional Material. (A mesh does not need a material.)
     */
    Optional<Material> getMaterial();

    /**
     * Render a list of game items that have the same mesh.
     *
     * <p>For internal use only.</p>
     *
     * @param gameItems The list of game items.
     * @param filter    The FrustumFilter for to render against.
     * @param consumer  The consumer to trigger on render.
     */
    void renderList(List<GameItem> gameItems, FrustumCullingFilter filter, Consumer<GameItem> consumer);

    /**
     * Get if the mesh has wireframe mode enabled.
     *
     * @return If the mesh has wireframe mode enabled.
     */
    boolean isWireframe();

    /**
     * Set if wireframe mode is enabled for the mesh.
     *
     * @param value If wireframe mode should be enabled.
     */
    void setWireframe(boolean value);
}
