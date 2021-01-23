package org.kakara.engine.components;

import org.kakara.engine.gameitems.mesh.IMesh;

public class MeshRenderer extends Component {
    private IMesh[] mesh;

    @Override
    public void start() { }

    @Override
    public void update() { }

    public void setMesh(IMesh mesh){
        this.mesh[0].cleanUp();
        this.mesh[0] = mesh;
    }

    public void setMesh(IMesh[] mesh){
        for(IMesh m : mesh){
            m.cleanUp();
        }
        this.mesh = mesh;
    }

    public IMesh getMesh(){
        return mesh[0];
    }

    public IMesh[] getMeshes(){
        return mesh;
    }
}
