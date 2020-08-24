package org.kakara.engine.test;

import org.kakara.engine.GameHandler;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.ui.components.text.BoundedColoredText;
import org.kakara.engine.window.WindowIcon;
import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractMenuScene;
import org.kakara.engine.test.components.LoadingBar;
import org.kakara.engine.test.components.LoadingBarCompleteEvent;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.shapes.Rectangle;
import org.kakara.engine.ui.components.text.BoundedText;
import org.kakara.engine.ui.components.text.Text;
import org.kakara.engine.ui.constraints.*;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIHoverEnterEvent;
import org.kakara.engine.ui.events.UIHoverLeaveEvent;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.items.ObjectCanvas;
import org.kakara.engine.ui.objectcanvas.UIObject;
import org.kakara.engine.ui.font.Font;
import org.kakara.engine.ui.font.TextAlign;
import org.kakara.engine.utils.Time;
import org.kakara.engine.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Example of how to make a proper UI Scene.
 */
public class TitleScreenScene extends AbstractMenuScene {
    private KakaraTest kakaraTest;

    private Text fps;
    private LoadingBar lb;

    UIObject obj;

    public TitleScreenScene(GameHandler gameHandler, KakaraTest kakaraTest) throws Exception {
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

        // Create a new componentcanvas. This holds the components for the UI.
        ComponentCanvas cc = new ComponentCanvas(this);

        // Make some more text for the title screen.
        Text title = new Text("Kakara", roboto);
        title.setSize(200);
        title.setLineWidth(500);
        title.setPosition(0, 200);
        title.addConstraint(new HorizontalCenterConstraint());

        // Create the play button from a rectangle.
        Rectangle playButton = new Rectangle(new Vector2(0, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        playButton.addConstraint(new GridConstraint(4, 7, 1, 4));
        playButton.setColor(new RGBA(0, 150, 150, 1));
        // Setup the events for the button.
        playButton.addUActionEvent(new UIHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                playButton.setColor(new RGBA(0, 150, 200, 1));
            }
        }, UIHoverEnterEvent.class);
        playButton.addUActionEvent(new UIHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                playButton.setColor(new RGBA(0, 150, 150, 1));
            }
        }, UIHoverLeaveEvent.class);
        playButton.addUActionEvent(new UIClickEvent() {
            @Override
            public void OnHUDClick(Vector2 location, MouseClickType clickType) {
                if(!playButton.isVisible()) return;
                try{
                    MainGameScene mgs = new MainGameScene(gameHandler, kakaraTest);
                    gameHandler.getSceneManager().setScene(mgs);
                }catch(Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Could not switch to the main scene!");
                }
            }
        }, UIClickEvent.class);
        Text txt = new Text("Play Game!", roboto);
        txt.setPosition(0, playButton.scale.y/2);
        txt.setTextAlign(TextAlign.CENTER);
        txt.setLineWidth(playButton.scale.x);
        playButton.add(txt);
        cc.add(title);
        cc.add(playButton);

        // Custom Component
        LoadingBar lb = new LoadingBar(new Vector2(300, 300), new Vector2(300, 30), roboto);
        lb.addConstraint(new HorizontalCenterConstraint());
        cc.add(lb);
        this.lb = lb;

        this.lb.addUActionEvent(new LoadingBarCompleteEvent() {
            @Override
            public void LoadingBarCompleteEvent(float percent) {
                System.out.println("hit 100%!");
                lb.setPercent(0);
            }
        }, LoadingBarCompleteEvent.class);

        //end

        Rectangle popupMenu = new Rectangle(new Vector2(gameHandler.getWindow().getWidth()/2-150, 100), new Vector2(300, 500));
        popupMenu.setColor(new RGBA(181, 181, 181, 1));
        popupMenu.setVisible(false);
        Text popupTitle = new Text("Popup Menu!", roboto);
        popupTitle.setLineWidth(popupMenu.scale.x);
        popupTitle.setPosition(0, 50);
        popupTitle.setSize(40);
        popupTitle.setTextAlign(TextAlign.CENTER);
        popupMenu.add(popupTitle);

        Text popupText = new Text("This here is a very cool popup menu! A menu like this has a ton of uses! Maybe a pause menu?", roboto);
        popupText.setLineWidth(popupMenu.scale.x);
        popupText.setPosition(10, 150);
        popupText.setSize(25);
        popupText.setTextAlign(TextAlign.LEFT);
        popupMenu.add(popupText);

        Rectangle popupClose = new Rectangle(new Vector2(popupMenu.getScale().x/2-50, popupMenu.getScale().y - 100), new Vector2(100, 70));
        popupClose.setColor(new RGBA(0, 150, 150, 1));
        Text popupCloseTxt = new Text("Close Menu!", roboto);
        popupCloseTxt.setPosition(0, popupClose.scale.y/2-10);
        popupCloseTxt.setTextAlign(TextAlign.CENTER);
        popupCloseTxt.setSize(30);
        popupCloseTxt.setLineWidth(popupClose.scale.x);
        popupClose.add(popupCloseTxt);

        popupMenu.add(popupClose);

        popupClose.addUActionEvent(new UIHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                popupClose.setColor(new RGBA(0, 150, 200, 1));
            }
        }, UIHoverEnterEvent.class);
        popupClose.addUActionEvent(new UIHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                popupClose.setColor(new RGBA(0, 150, 150, 1));
            }
        }, UIHoverLeaveEvent.class);

        Rectangle openMenuButton = new Rectangle(new Vector2(gameHandler.getWindow().getWidth()/2 + 100, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        openMenuButton.setColor(new RGBA(0, 150, 150, 1));
        openMenuButton.addConstraint(new GridConstraint(4, 7, 2, 4));
        openMenuButton.addUActionEvent(new UIHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                openMenuButton.setColor(new RGBA(0, 150, 200, 1));
            }
        }, UIHoverEnterEvent.class);
        openMenuButton.addUActionEvent(new UIHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                openMenuButton.setColor(new RGBA(0, 150, 150, 1));
            }
        }, UIHoverLeaveEvent.class);
        openMenuButton.addUActionEvent(new UIClickEvent() {
            @Override
            public void OnHUDClick(Vector2 location, MouseClickType clickType) {
                popupMenu.setVisible(true);
                openMenuButton.setVisible(false);
                playButton.setVisible(false);
            }
        }, UIClickEvent.class);
        Text openMenuTxt = new Text("Open Menu!", roboto);
        openMenuTxt.setPosition(0, openMenuButton.scale.y/2);
        openMenuTxt.setTextAlign(TextAlign.CENTER);
        openMenuTxt.setLineWidth(openMenuButton.scale.x);
        openMenuButton.add(openMenuTxt);
        cc.add(openMenuButton);

        popupClose.addUActionEvent(new UIClickEvent() {
            @Override
            public void OnHUDClick(Vector2 location, MouseClickType clickType) {
                popupMenu.setVisible(false);
                openMenuButton.setVisible(true);
                playButton.setVisible(true);
            }
        }, UIClickEvent.class);

        cc.add(popupMenu);

        Text fps = new Text("FPS: 000", roboto);
        fps.setPosition(20, 20);
        cc.add(fps);
        this.fps = fps;

        BoundedText btxt = new BoundedText("This is a test. I really need to make this string super long and stuff just so you know!", roboto);
        btxt.setPosition(300, 60);
        btxt.setMaximumBound(new Vector2(350, 90));
        btxt.addConstraint(new GeneralConstraint(ComponentSide.LEFT, playButton, ComponentSide.RIGHT, 0));
//        btxt.addConstraint(new GeneralConstraint(ComponentSide.TOP, playButton, ComponentSide.TOP, 1));
        btxt.addConstraint(new GeneralConstraint(ComponentSide.BOTTOM, null, ComponentSide.BOTTOM, 0));
        cc.add(btxt);

        title.setVisible(true);

        BoundedColoredText nTxt = new BoundedColoredText("{#5BE0D5}Yeet {#5BE06D} Am I right? {#F54FFFF}No you are not!{#ED4725}I really love this" +
                "stuff you know!", roboto);
        nTxt.addConstraint(new HorizontalCenterConstraint());
        nTxt.addConstraint(new VerticalCenterConstraint(200));
        nTxt.setMaximumBound(new Vector2(300, 400));
        cc.add(nTxt);




        // Make sure to add the component canvas to the hud!
        add(cc);

        ObjectCanvas oc = new ObjectCanvas(this);
        Mesh m = new Mesh(CubeData.vertex, CubeData.texture, CubeData.normal, CubeData.indices);
        InputStream io = Texture.class.getResourceAsStream("/example_texture.png");
        Texture grass = Utils.inputStreamToTexture(io);
        Material mt = new Material(grass);
        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
        mt.addOverlayTexture(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/ovly2.png")));
        m.setMaterial(mt);

        UIObject ui = new UIObject(m);
        ui.setPosition((float)200, (float)200);
        ui.setScale(100);
        obj = ui;
        obj.getRotation().rotateX((float)Math.toRadians(40));
        obj.getRotation().rotateY((float)Math.toRadians(50));
        obj.addUActionEvent(UIClickEvent.class, (UIClickEvent) (location, clickType) -> {
            System.out.println("I got clicked!");
        });
        oc.add(ui);
        add(oc);

        ComponentCanvas ontop = new ComponentCanvas(this);
        Rectangle on = new Rectangle();
        on.setPosition(200, 200);
        on.setScale(40, 40);
        ontop.add(on);
        add(ontop);

        setCurserStatus(true);

        setBackground(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));

        getUserInterface().setAutoScale(false);

//        add(new DebugCanvas());
    }

    @Override
    public void update(float interval) {
        fps.setText("FPS: " + Math.round(1/Time.getDeltaTime()));

        lb.setPercent(lb.getPercent() + Time.getDeltaTime());

//        obj.setPosition(gameHandler.getMouseInput().getCurrentPosition().x, gameHandler.getMouseInput().getCurrentPosition().y);

//        getCamera().setPosition(getCamera().getPosition().add(1,0,0));
//        System.out.println(getCamera().getPosition());
    }
}
