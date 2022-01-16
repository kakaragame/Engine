package org.kakara.engine.gameitems;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.exceptions.GenericLoadException;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.models.StaticModelLoader;

/**
 * Handles the skybox
 */
public class SkyBox extends GameItem {

    /**
     * Create the skybox
     *
     * @param skyBoxTexture  The texture to be used for the skybox
     * @param useUniqueModel If the skybox uses the same texture layout as cube (false) or if it uses the skybox texture layout (true).
     */
    public SkyBox(Texture skyBoxTexture, boolean useUniqueModel) {
        super();
        addComponent(MeshRenderer.class);
        try {
            GameHandler gm = GameHandler.getInstance();
            if (useUniqueModel) {
                Mesh[] skyBoxMesh = StaticModelLoader.load(gm.getResourceManager().getResource("skybox.obj"), "/player", gm.getSceneManager().getCurrentScene(),
                        gm.getResourceManager());
                for (Mesh m : skyBoxMesh) {
                    m.setMaterial(new Material(skyBoxTexture, 0f));
                }

                getComponent(MeshRenderer.class).setMesh(skyBoxMesh);
                this.transform.setScale(100, 100, 100);
            } else {
                Mesh skyBoxMesh = new Mesh(CubeData.skyboxVertex, CubeData.texture, CubeData.normal, CubeData.indices);
                skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0f));
                getComponent(MeshRenderer.class).setMesh(skyBoxMesh);
            }
            this.transform.setPosition(0, 0, 0);
        } catch (Exception ex) {
            throw new GenericLoadException("Error: unable to load sky box object!", ex);
        }
    }

    /**
     * Change the current texture of the skybox
     *
     * @param tx The texture to change to.
     */
    public void setTexture(Texture tx) {
        getMeshRenderer().get().getMesh().getMaterial().ifPresent(mat -> mat.getTexture().cleanup());
        ((Mesh) this.getMeshRenderer().get().getMesh()).setMaterial(new Material(tx, 0f));
    }

}
