package org.kakara.engine.gameitems;

import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.render.culling.FrustumCullingFilter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * This mesh is used to store GameItems without a MeshRenderer component.
 */
public class NullMesh implements IMesh {
    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public Optional<Material> getMaterial() {
        return Optional.empty();
    }

    @Override
    public void renderList(List<GameItem> gameItems, FrustumCullingFilter filter, Consumer<GameItem> consumer) {

    }

    @Override
    public boolean isWireframe() {
        return false;
    }

    @Override
    public void setWireframe(boolean value) {

    }
}
