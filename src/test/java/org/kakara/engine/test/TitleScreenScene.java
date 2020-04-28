package org.kakara.engine.test;

import org.kakara.engine.Camera;
import org.kakara.engine.GameHandler;
import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.item.Texture;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.AbstractMenuScene;
import org.kakara.engine.test.components.LoadingBar;
import org.kakara.engine.test.components.LoadingBarCompleteEvent;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.Rectangle;
import org.kakara.engine.ui.components.Text;
import org.kakara.engine.ui.events.HUDClickEvent;
import org.kakara.engine.ui.events.HUDHoverEnterEvent;
import org.kakara.engine.ui.events.HUDHoverLeaveEvent;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.ui.text.Font;
import org.kakara.engine.ui.text.TextAlign;
import org.kakara.engine.utils.Time;
import org.kakara.engine.utils.Utils;

import java.io.IOException;

/**
 * Example of how to make a proper UI Scene.
 */
public class TitleScreenScene extends AbstractMenuScene {
    private KakaraTest kakaraTest;

    private Text fps;
    private LoadingBar lb;

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

        // Make a new font. It is (fontName, the resource for the font)
        Font roboto = new Font("Roboto-Regular", resourceManager.getResource("Roboto-Regular.ttf"));
        // Don't forget to add the font to the HUD. The font won't be initialized if you don't
        hud.addFont(roboto);

        // Create a new componentcanvas. This holds the components for the UI.
        ComponentCanvas cc = new ComponentCanvas(this);

        // Make some more text for the title screen.
        Text title = new Text("Kakara", roboto);
        title.setSize(200);
        title.setLineWidth(500);
        title.setPosition(gameHandler.getWindow().getWidth()/2 - 250, 200);

        // Create the play button from a rectangle.
        Rectangle playButton = new Rectangle(new Vector2(gameHandler.getWindow().getWidth()/2 - 100, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        playButton.setColor(new RGBA(0, 150, 150, 1));
        // Setup the events for the button.
        playButton.addUActionEvent(new HUDHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                playButton.setColor(new RGBA(0, 150, 200, 1));
            }
        }, HUDHoverEnterEvent.class);
        playButton.addUActionEvent(new HUDHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                playButton.setColor(new RGBA(0, 150, 150, 1));
            }
        }, HUDHoverLeaveEvent.class);
        playButton.addUActionEvent(new HUDClickEvent() {
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
        }, HUDClickEvent.class);
        Text txt = new Text("Play Game!", roboto);
        txt.setPosition(0, playButton.scale.y/2);
        txt.setTextAlign(TextAlign.CENTER);
        txt.setLineWidth(playButton.scale.x);
        playButton.add(txt);
        cc.add(title);
        cc.add(playButton);

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

        popupClose.addUActionEvent(new HUDHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                popupClose.setColor(new RGBA(0, 150, 200, 1));
            }
        }, HUDHoverEnterEvent.class);
        popupClose.addUActionEvent(new HUDHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                popupClose.setColor(new RGBA(0, 150, 150, 1));
            }
        }, HUDHoverLeaveEvent.class);

        Rectangle openMenuButton = new Rectangle(new Vector2(gameHandler.getWindow().getWidth()/2 + 100, gameHandler.getWindow().getHeight() - 300),
                new Vector2(100, 100));
        openMenuButton.setColor(new RGBA(0, 150, 150, 1));
        openMenuButton.addUActionEvent(new HUDHoverEnterEvent() {
            @Override
            public void OnHudHoverEnter(Vector2 location) {
                openMenuButton.setColor(new RGBA(0, 150, 200, 1));
            }
        }, HUDHoverEnterEvent.class);
        openMenuButton.addUActionEvent(new HUDHoverLeaveEvent() {
            @Override
            public void OnHudHoverLeave(Vector2 location) {
                openMenuButton.setColor(new RGBA(0, 150, 150, 1));
            }
        }, HUDHoverLeaveEvent.class);
        openMenuButton.addUActionEvent(new HUDClickEvent() {
            @Override
            public void OnHUDClick(Vector2 location, MouseClickType clickType) {
                popupMenu.setVisible(true);
                openMenuButton.setVisible(false);
                playButton.setVisible(false);
            }
        }, HUDClickEvent.class);
        Text openMenuTxt = new Text("Open Menu!", roboto);
        openMenuTxt.setPosition(0, openMenuButton.scale.y/2);
        openMenuTxt.setTextAlign(TextAlign.CENTER);
        openMenuTxt.setLineWidth(openMenuButton.scale.x);
        openMenuButton.add(openMenuTxt);
        cc.add(openMenuButton);

        popupClose.addUActionEvent(new HUDClickEvent() {
            @Override
            public void OnHUDClick(Vector2 location, MouseClickType clickType) {
                popupMenu.setVisible(false);
                openMenuButton.setVisible(true);
                playButton.setVisible(true);
            }
        }, HUDClickEvent.class);

        cc.add(popupMenu);

        Text fps = new Text("FPS: 000", roboto);
        fps.setPosition(20, 20);
        cc.add(fps);
        this.fps = fps;

        // Custom Component
        LoadingBar lb = new LoadingBar(new Vector2(300, 300), new Vector2(300, 30), roboto);
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


        // Make sure to add the component canvas to the hud!
        add(cc);

        setCurserStatus(true);

        setBackground(Utils.inputStreamToTexture(Texture.class.getResourceAsStream("/oa.png")));
    }

    @Override
    public void update(float interval) {
        fps.setText("FPS: " + Math.round(1/Time.deltaTime));

        lb.setPercent(lb.getPercent() + Time.deltaTime);
    }
}
