package org.kakara.engine.components;

import org.kakara.engine.gameitems.mesh.IMesh;

import java.util.Arrays;
import java.util.Objects;

/**
 * The MeshRender component handles meshes for the GameItem.
 *
 * <p>The MeshRenderer stores the meshes for the GameItem, which are then rendered by a Pipeline.</p>
 */
public class MeshRenderer extends Component {
    private IMesh[] mesh;
    private boolean visible = true;

    @Override
    public void start() {
    }

    @Override
    public void update() {
    }

    @Override
    public void afterInit() {

    }

    @Override
    public void cleanup() {
        int numMeshes = this.mesh != null ? this.mesh.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.mesh[i].cleanUp();
        }
    }

    @Override
    public void onRemove() {

    }

    /**
     * Set the mesh for the component.
     *
     * @param mesh The mesh to set.
     */
    public void setMesh(IMesh mesh) {
        Objects.requireNonNull(mesh);
        if (this.mesh != null)
            this.mesh[0].cleanUp();
        this.mesh = new IMesh[1];
        this.mesh[0] = mesh;
    }

    /**
     * Set the meshes for the component.
     *
     * @param mesh The array of meshes to set.
     */
    public void setMesh(IMesh[] mesh) {
        Objects.requireNonNull(mesh);
        if (this.mesh != null)
            for (IMesh m : this.mesh)
                m.cleanUp();
        this.mesh = mesh;
    }

    /**
     * Get the mesh.
     *
     * @return The mesh.
     */
    public IMesh getMesh() {
        return mesh[0];
    }

    /**
     * Get the meshes.
     *
     * @return The meshes.
     */
    public IMesh[] getMeshes() {
        return mesh;
    }

    /**
     * If the mesh is visible.
     *
     * @return If the mesh is visible.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set if the mesh is visible.
     *
     * @param visible if the mesh is visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "MeshRenderer{" +
                "mesh=" + Arrays.toString(mesh) +
                ", visible=" + visible +
                '}';
    }
}
