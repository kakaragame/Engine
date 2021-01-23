package org.kakara.engine.debug;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.kakara.engine.GameHandler;
import org.kakara.engine.physics.collision.Collidable;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.scene.AbstractScene;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.items.ObjectCanvas;
import org.kakara.engine.utils.Time;
import org.kakara.engine.weather.Fog;

import java.util.List;
import java.util.Stack;

/**
 * Debug Canvas for debugging only.
 *
 * @deprecated Will be removed in next version.
 * TODO remove this.
 */
public class DebugCanvas implements UICanvas {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    Stack<Integer> fps = new Stack<>();
    Stack<Integer> query = new Stack<>();
    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;
    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        ImGui.createContext();
        imGuiGlfw.init(handler.getWindow().getWindowHandler(), true);
        imGuiGl3.init("#version 150");
    }

    @Override
    public void render(UserInterface userInterface, GameHandler handler) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        renderSceneInfo(userInterface.getScene());
        renderFPSInfo(userInterface.getScene());

        ImGui.render();
        imGuiGl3.render(ImGui.getDrawData());
    }

    @Override
    public void cleanup(GameHandler handler) {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    private void renderFPSInfo(Scene scene){
        ImGui.setNextWindowSize(300, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(10, 300, ImGuiCond.Once);
        ImGui.begin("FPS Information");
        ImGui.text("FPS: " + Math.round(1/ Time.getDeltaTime()));
        fps.push(Math.round(1/ Time.getDeltaTime()));
        if(fps.size() > 50)
            fps.remove(0);
        float[] fpsData = getFPSArray();
        ImGui.plotLines("", fpsData, fps.size(), 0, "Frame Per Second (Lines)", 0, 100, 200, 100);
        ImGui.plotHistogram("", fpsData, fps.size(), 0, "Frame Per Second (Histogram)", 0, 100, 200, 100);
        ImGui.end();
    }

    private float[] getFPSArray(){
        float[] data = new float[fps.size()];
        for(int i = 0; i < fps.size(); i++){
            data[i] = fps.get(i);
        }
        return data;
    }

    private float[] getQuery(){
        float[] data = new float[query.size()];
        for(int i = 0; i < query.size(); i++){
            data[i] = query.get(i);
        }
        return data;
    }

    private void renderSceneInfo(Scene scene){
        ImGui.setNextWindowSize(300, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(10, 10, ImGuiCond.Once);
        ImGui.begin("Scene Information");
        ImGui.text("Scene Class: " + scene.getClass().getSimpleName());
        ImGui.text("Scene Super Class: " + scene.getClass().getSuperclass().getSimpleName());
        ImGui.separator();
        if(ImGui.collapsingHeader("UserInterface")){
            ImGui.text("# of UICanvases: " + scene.getUserInterface().getUICanvases().size());
            int i = 0;
            for(UICanvas canvas : scene.getUserInterface().getUICanvases()){
                if(ImGui.collapsingHeader(canvas.getClass().getSimpleName() + " : " + i)){
                    if(canvas instanceof ComponentCanvas){
                        renderComponentUICanvas((ComponentCanvas) canvas);
                    }
                    else if(canvas instanceof ObjectCanvas){
                        renderObjectUICanvas((ObjectCanvas) canvas);
                    }
                }
                i++;
            }
        }
        if(scene instanceof AbstractScene){
            if(ImGui.collapsingHeader("Abstract Scene Information")){
                AbstractScene abstractScene = (AbstractScene) scene;
                ImGui.text("# of Items: " + abstractScene.getItemHandler().getItems().size());
                if(ImGui.treeNode("Lights")) {
                    ImGui.text("# of Point Lights: " + abstractScene.getLightHandler().getDisplayPointLights().size());
                    ImGui.text("# of Spot Lights: " + abstractScene.getLightHandler().getSpotLights().size());
                    ImGui.treePop();
                }
                ImGui.text("Has Fog: " + (abstractScene.getFog() == Fog.NOFOG ? "true" : "false"));
                ImGui.text("Has Skybox: " + (abstractScene.getSkyBox() == null ? "false" : "true"));
            }
        }
        if(scene instanceof AbstractGameScene){
            if(ImGui.collapsingHeader("Abstract Game Scene Information")){
                AbstractGameScene abstractGameScene = (AbstractGameScene) scene;
                ImGui.text("# of Render Chunks: " + abstractGameScene.getChunkHandler().getRenderChunkList().size());
//                Collidable lookingAt = abstractGameScene.selectGameItems(30);
//                if(lookingAt instanceof RenderBlock){
//                    RenderBlock rb = (RenderBlock) lookingAt;
//                    ImGui.text("Facing Chunk Query Result: " + rb.getParentChunk().getRenderMesh().getQuery().pollPreviousResult());
//                    query.push(rb.getParentChunk().getRenderMesh().getQuery().pollPreviousResult());
//                }else{
//                    ImGui.text("Facing Chunk Query Result: NAN");
//                }
                if(query.size() > 50)
                    query.remove(0);
                float[] queryData = getQuery();
                ImGui.plotHistogram("", queryData, query.size(), 0, "Query Value (Histogram)", 0, 1, 200, 100);
            }
        }
        ImGui.end();
    }

    private void renderComponentUICanvas(ComponentCanvas canvas){
        ImGui.text("# of Components: " + canvas.getComponents().size());
    }

    private void renderObjectUICanvas(ObjectCanvas canvas){
        ImGui.text("# of Objects: " + canvas.getObjects().size());
    }
    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
