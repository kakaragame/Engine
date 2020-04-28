package org.kakara.engine.item;

import org.kakara.engine.GameHandler;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.models.StaticModelLoader;

/**
 * Handles the skybox
 */
public class SkyBox extends MeshGameItem {

    /**
     * Create the skybox
     * @param skyBoxTexture The texture to be used for the skybox
     * @param useUniqueModel If the skybox uses the same texture layout as cube (false) or if it uses the skybox texture layout (true).
     * @throws Exception
     */
    public SkyBox(Texture skyBoxTexture, boolean useUniqueModel) throws Exception {
        super();
        GameHandler gm = GameHandler.getInstance();
        if(useUniqueModel){
            Mesh[] skyBoxMesh = StaticModelLoader.load(gm.getResourceManager().getResource("../skybox.obj"), "/player", gm.getSceneManager().getCurrentScene(),
                    gm.getResourceManager());
            for(Mesh m : skyBoxMesh){
                m.setMaterial(new Material(skyBoxTexture, 0f));
            }

            setMeshes(skyBoxMesh);
            setScale(100);
        }
        else{
            Mesh skyBoxMesh = new Mesh(CubeData.skyboxVertex, CubeData.texture, CubeData.normal, CubeData.indices);
            skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0f));
            setMesh(skyBoxMesh);
        }
        setPosition(0, 0, 0);
    }

    /**
     * Change the current texture of the skybox
     * @param tx The texture to change to.
     */
    public void setTexture(Texture tx){
        this.getMesh().getMaterial().getTexture().cleanup();
        this.getMesh().setMaterial(new Material(tx, 0f));
    }

}
