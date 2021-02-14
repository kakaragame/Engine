package org.kakara.engine.test;

import org.kakara.engine.GameHandler;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.input.controller.ControllerManager;
import org.kakara.engine.input.controller.GamePadButtonEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractMenuScene;
import org.kakara.engine.ui.components.shapes.Rectangle;
import org.kakara.engine.ui.components.text.BoundedText;
import org.kakara.engine.ui.components.text.Text;
import org.kakara.engine.ui.constraints.ComponentSide;
import org.kakara.engine.ui.constraints.GeneralConstraint;
import org.kakara.engine.ui.constraints.HorizontalCenterConstraint;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIHoverEnterEvent;
import org.kakara.engine.ui.events.UIHoverLeaveEvent;
import org.kakara.engine.ui.font.Font;
import org.kakara.engine.ui.font.TextAlign;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.items.ObjectCanvas;
import org.kakara.engine.ui.objectcanvas.UIObject;
import org.kakara.engine.utils.RGBA;
import org.kakara.engine.utils.Time;
import org.kakara.engine.utils.Utils;
import org.kakara.engine.window.WindowIcon;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Example of how to make a proper UI Scene.
 */
public class TitleScreenScene extends AbstractMenuScene {
    private final KakaraTest kakaraTest;
    private Text fps;
    private UIObject obj;

    public TitleScreenScene(GameHandler gameHandler, KakaraTest kakaraTest) {
        super(gameHandler);
        this.kakaraTest = kakaraTest;
    }

    @Override
    public void work() {
    }

    @Override
    public void loadGraphics(GameHandler handler) throws IOException {
        // Get the resource manager to load in needed files.
        ResourceManager resourceManager = gameHandler.getResourceManager();

        handler.getWindow().setIcon(new WindowIcon(resourceManager.getResource("oop.png")));

        // Make a new font. It is (fontName, the resource for the font)
        Font roboto = new Font("Roboto-Regular", resourceManager.getResource("Roboto-Regular.ttf"), this);

        // Create a new component canvas. This holds the components for the UI.
        ComponentCanvas cc = new ComponentCanvas(this);

        /*

            Create the title for the menu.

         */
        Text title = new Text("Kakara Engine", roboto);
        title.setSize(200);
        title.setLineWidth(handler.getWindow().getWidth());
        title.setPosition(0, 200);
        title.setTextAlign(TextAlign.CENTER);
        cc.add(title);

        /*

            Create the play button for the menu.

         */
        Rectangle playButton = new Rectangle(new Vector2(0, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        playButton.setColor(new RGBA(0, 150, 150, 1));
        playButton.addConstraint(new HorizontalCenterConstraint(-100));
        // Setup the events for the button.
        playButton.addUActionEvent(UIHoverEnterEvent.class, (UIHoverEnterEvent) location -> playButton.setColor(new RGBA(0, 150, 200, 1)));
        playButton.addUActionEvent(UIHoverLeaveEvent.class, (UIHoverLeaveEvent) location -> playButton.setColor(new RGBA(0, 150, 150, 1)));
        playButton.addUActionEvent(UIClickEvent.class, (UIClickEvent) (location, clickType) -> {
            if (!playButton.isVisible()) return;
            try {
                MainGameScene mgs = new MainGameScene(gameHandler, kakaraTest);
                gameHandler.getSceneManager().setScene(mgs);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Could not switch to the main scene!");
            }
        });

        Text txt = new Text("Play Game!", roboto);
        txt.setPosition(0, playButton.getScale().getY() / 2);
        txt.setTextAlign(TextAlign.CENTER);
        txt.setLineWidth(playButton.getScale().getX());
        playButton.add(txt);

        cc.add(playButton);

        /*

            Create the Popup Information Menu

         */
        Rectangle popupMenu = new Rectangle(new Vector2(0, 100), new Vector2(600, 500));
        popupMenu.setColor(new RGBA(181, 181, 181, 1));
        popupMenu.setVisible(false);
        popupMenu.addConstraint(new HorizontalCenterConstraint());
        Text popupTitle = new Text("The Kakara Engine!", roboto);
        popupTitle.setLineWidth(popupMenu.getScale().getX());
        popupTitle.setPosition(0, 50);
        popupTitle.setSize(40);
        popupTitle.setTextAlign(TextAlign.CENTER);
        popupMenu.add(popupTitle);

        BoundedText popupText = new BoundedText("Welcome to the Kakara Game Engine!\n" +
                "This demo is to demonstrate the functionality of the engine. Please click the play button to view" +
                "to play the demo! Feel free to look through the demo code to see how this was made!", roboto);
        popupText.setMaximumBound(new Vector2(600, 500));
        popupText.setPosition(300, 150);
        popupText.setLineHeight(5);
        popupText.setSize(25);
        popupText.setTextAlign(TextAlign.CENTER);
        popupMenu.add(popupText);

        Rectangle popupClose = new Rectangle(new Vector2(0, 0), new Vector2(100, 70));
        popupClose.setColor(new RGBA(0, 150, 150, 1));
        popupClose.addConstraint(new HorizontalCenterConstraint());
        popupClose.addConstraint(new GeneralConstraint(ComponentSide.BOTTOM, popupMenu, ComponentSide.BOTTOM, 100));

        Text popupCloseTxt = new Text("Close Menu!", roboto);
        popupCloseTxt.setPosition(0, popupClose.getScale().getY() / 2 - 10);
        popupCloseTxt.setTextAlign(TextAlign.CENTER);
        popupCloseTxt.setSize(30);
        popupCloseTxt.setLineWidth(popupClose.getScale().getX());

        popupClose.add(popupCloseTxt);
        popupMenu.add(popupClose);

        popupClose.addUActionEvent(UIHoverEnterEvent.class,
                (UIHoverEnterEvent) location -> popupClose.setColor(new RGBA(0, 150, 200, 1)));
        popupClose.addUActionEvent(UIHoverLeaveEvent.class, (UIHoverLeaveEvent) location -> popupClose.setColor(new RGBA(0, 150, 150, 1)));

        /*

            Create the open menu button.

         */
        Rectangle openMenuButton = new Rectangle(new Vector2(gameHandler.getWindow().getWidth() / 2f + 100, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        openMenuButton.setColor(new RGBA(0, 150, 150, 1));
        openMenuButton.addConstraint(new HorizontalCenterConstraint(100));
        openMenuButton.addUActionEvent(UIHoverEnterEvent.class, (UIHoverEnterEvent) location -> openMenuButton.setColor(new RGBA(0, 150, 200, 1)));
        openMenuButton.addUActionEvent(UIHoverLeaveEvent.class, (UIHoverLeaveEvent) location -> openMenuButton.setColor(new RGBA(0, 150, 150, 1)));
        openMenuButton.addUActionEvent(UIClickEvent.class, (UIClickEvent) (location, clickType) -> {
            popupMenu.setVisible(true);
            openMenuButton.setVisible(false);
            playButton.setVisible(false);

            System.out.println(glfwGetGamepadName(GLFW_JOYSTICK_2));
            System.out.println(glfwGetGamepadName(GLFW_JOYSTICK_1));
        });

        Text openMenuTxt = new Text("Open Info!", roboto);
        openMenuTxt.setPosition(0, openMenuButton.getScale().getY() / 2f);
        openMenuTxt.setTextAlign(TextAlign.CENTER);
        openMenuTxt.setLineWidth(openMenuButton.getScale().getX());
        openMenuButton.add(openMenuTxt);
        cc.add(openMenuButton);

        popupClose.addUActionEvent(UIClickEvent.class, (UIClickEvent) (location, clickType) -> {
            popupMenu.setVisible(false);
            openMenuButton.setVisible(true);
            playButton.setVisible(true);
        });

        cc.add(popupMenu);

        // Create the FPS counter.
        Text fps = new Text("FPS: 000", roboto);
        fps.setPosition(20, 20);
        cc.add(fps);
        this.fps = fps;

//        TODO This needs to be fixed in pre-4
//        BoundedColoredText nTxt = new BoundedColoredText("{#5BE0D5}Yeet {#5BE06D} Am I right? {#F54FFFF}No you are not!{#ED4725}I really love this " +
//                "stuff you know!sdfsdfsdffsdf{#F54FFFF}sddffdssfd{#F54FFFF}sfdsfd{#7e55e6}fdfsdsfdfs", roboto);
//        nTxt.addConstraint(new HorizontalCenterConstraint());
//        nTxt.addConstraint(new VerticalCenterConstraint(200));
//        nTxt.setMaximumBound(new Vector2(400, 400));
//        cc.add(nTxt);

        /*

            Create the Rotating Cube in the background.

         */
        // First a special object canvas is created.
        ObjectCanvas oc = new ObjectCanvas(this);
        Mesh m = new Mesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices);
        InputStream io = Texture.class.getResourceAsStream("/example_texture.png");
        Texture grass = Utils.inputStreamToTexture(io);
        Material mt = new Material(grass);
        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/ovly2.png")));
        m.setMaterial(mt);

        // The the actual cube is created.
        UIObject ui = new UIObject(m);
        ui.setPosition(handler.getWindow().getWidth() / 2f, handler.getWindow().getHeight() / 2f);
        ui.setScale(300);
        obj = ui;
        obj.getRotation().rotateX((float) Math.toRadians(40));
        obj.getRotation().rotateY((float) Math.toRadians(50));
        oc.add(ui);
        add(oc);

        // The main canvas is added after the object canvas.
        add(cc);


        setCursorStatus(true);
        // Set the background of the menu.
        setBackground(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
        getUserInterface().setAutoScale(false);

    }

    @Override
    public void update(float interval) {
        fps.setText("FPS: " + Math.round(1 / Time.getDeltaTime()));
        // Update the object rotation.
        obj.getRotation().rotateY(0.03f);
        obj.getRotation().rotateX(0.03f);
    }

    @EventHandler
    public void onControllerPress(GamePadButtonEvent event){
        System.out.println(event.getButtonID());
    }
}
