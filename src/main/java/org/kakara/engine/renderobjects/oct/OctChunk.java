package org.kakara.engine.renderobjects.oct;

import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.RenderBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Replaced by Ryandw11's Octree Library
 */
public class OctChunk {
    private List<SubOctChunk> octChunks;
    public OctChunk(){
        octChunks = new ArrayList<>();
    }

    public void add(RenderBlock block){
        Vector3 position = block.getPosition();
        int index = (int) (Math.abs(Math.floor(position.x/2 + position.y/2 + position.z/2)));
        if(octChunks.size() > index){
            octChunks.get(index).add(block);
        }else{
            do{
                octChunks.add(new SubOctChunk());
            }while(octChunks.size() <= index);
            octChunks.get(index).add(block);
        }
    }

    public List<SubOctChunk> getOctChunks(){
        return octChunks;
    }

    public RenderBlock get(Vector3 vec){
        if(vec.x < 0 || vec.y < 0 || vec.z < 0) return null;
        if(vec.x > 16 || vec.y > 16 || vec.z > 16) return null;
        int index = (int) (Math.floor(vec.x/2 + vec.y/2 + vec.z/2));
        if(octChunks.size() <= index) return null;
        return octChunks.get(index).get(vec);
    }
}
