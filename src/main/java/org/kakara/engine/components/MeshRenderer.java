package org.kakara.engine.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gameitems.ItemHandler;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.gameitems.mesh.Mesh;

public class MeshRenderer extends Component {
    private IMesh[] mesh;
    private boolean visible = true;

    @Override
    public void start() { }

    @Override
    public void update() { }

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

    public void setMesh(IMesh mesh){
        if(mesh != null)
            this.mesh[0].cleanUp();
        this.mesh = new IMesh[1];
        this.mesh[0] = mesh;
    }

    public void setMesh(IMesh[] mesh){
        // TODO Fix this.
//        if(mesh != null)
//            for(IMesh m : mesh)
//                m.cleanUp();
        this.mesh = mesh;
    }

    public IMesh getMesh(){
        return mesh[0];
    }

    public IMesh[] getMeshes(){
        return mesh;
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }
}
