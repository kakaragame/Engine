package org.kakara.engine.renderobjects.oct;

import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.RenderBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @deprecated Replaced by Ryandw11's Octree Library
 */
public class SubOctChunk {
    private List<RenderBlock> even;
    private List<RenderBlock> odd;
    public SubOctChunk(){
        even = new ArrayList<>();
        odd = new ArrayList<>();
    }

    public void add(RenderBlock block){
        Vector3 position = block.getPosition();
        int index = (int) Math.abs(Math.floor(position.x + position.y + position.z));
        if(index % 2 == 0){
            even.add(block);
        }else{
            odd.add(block);
        }
    }

    /**
     * Can return null
     * @param vec
     * @return
     */
    public RenderBlock get(Vector3 vec){
        int index = (int) Math.abs(Math.floor(vec.x + vec.y + vec.z));
        if(index % 2 == 0){
            List<RenderBlock> blcks = even.stream().filter(blck -> {
                Vector3 pos = blck.getPosition();
                return pos.x == vec.x && pos.y == vec.y && pos.z == vec.z;
            }).collect(Collectors.toList());
            if(blcks.size() < 1) return null;
            return blcks.get(0);
        }else{
            List<RenderBlock> blcks = odd.stream().filter(blck -> {
                Vector3 pos = blck.getPosition();
                return pos.x == vec.x && pos.y == vec.y && pos.z == vec.z;
            }).collect(Collectors.toList());
            if(blcks.size() < 1) return null;
            if(blcks.size() > 1) System.out.println("fweo");
            return blcks.get(0);
        }
    }

    public void printDebug(){
        System.out.println(even);
        System.out.println(odd);
    }
}
