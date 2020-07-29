package org.kakara.engine.debug;

import imgui.*;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.kakara.engine.GameHandler;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.scene.AbstractScene;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.items.ObjectCanvas;
import org.kakara.engine.utils.Time;
import org.kakara.engine.weather.Fog;

import java.util.Stack;

public class DebugCanvas implements UICanvas {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    Stack<Integer> fps = new Stack<>();

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

    private void renderSceneInfo(Scene scene){
        ImGui.setNextWindowSize(300, 300, ImGuiCond.Once);
        ImGui.setNextWindowPos(10, 10, ImGuiCond.Once);
        ImGui.begin("Scene Information");
        ImGui.text("Scene Class: " + scene.getClass().getSimpleName());
        ImGui.text("Scene Super Class: " + scene.getClass().getSuperclass().getSimpleName());
        ImGui.separator();
        if(ImGui.collapsingHeader("UserInterface")){
            ImGui.text("# of UICanvases: " + scene.getHUD().getUICanvases().size());
            int i = 0;
            for(UICanvas canvas : scene.getHUD().getUICanvases()){
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

    private void setupImGui() {
        ResourceManager resourceManager = new ResourceManager();
        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null); // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.DockingEnable); // Navigation with keyboard and enabled docking

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        fontAtlas.addFontDefault();

        // Fonts merge example
        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        fontConfig.setPixelSnapH(true);

        fontConfig.setMergeMode(false);
        fontConfig.setPixelSnapH(false);

        // Or directly from the memory
        fontConfig.setName("Roboto-Regular.ttf, 14px"); // This name will be displayed in Style Editor
        fontAtlas.addFontFromMemoryTTF(resourceManager.getResource("Roboto-Regular.ttf").getByteArray(), 14, fontConfig);
        fontConfig.setName("Roboto-Regular.ttf, 16px"); // We can apply a new config value every time we add a new font
        fontAtlas.addFontFromMemoryTTF(resourceManager.getResource("Roboto-Regular.ttf").getByteArray(), 16, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture
        ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);
    }

}
