package org.kakara.engine.test;

import org.kakara.engine.Game;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.KeyPressEvent;
import org.kakara.engine.events.event.MouseClickEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

public class KakaraTest implements Game {

    private GameHandler gInst;
    @Override
    public void start(GameHandler handler) throws Exception {
        gInst = handler;

        TitleScreenScene tss = new TitleScreenScene(handler, this);
//        MainGameScene mgs = new MainGameScene(handler, this);
        gInst.getSceneManager().setScene(tss);
        try {
            gInst.getSoundManager().init();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        gInst.getSoundManager().setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
//        gInst.getSoundManager().setListener(new SoundListener());
//
//        SoundBuffer buffBack = new SoundBuffer(handler.getResourceManager().getResource("sounds/background.ogg"));
//        gInst.getSoundManager().addSoundBuffer(buffBack);
//        SoundSource sourceBack = new SoundSource(true, true);
//        sourceBack.setBuffer(buffBack.getBufferId());
//        gInst.getSoundManager().addSoundSource("MUSIC", sourceBack);
//        gInst.getSoundManager().playSoundSource("MUSIC");
    }


    @Override
    public void update() {

    }

    @Override
    public void exit() {
        gInst.exit();
    }

    @EventHandler
    public void onMouseClick(MouseClickEvent evt) {
        System.out.println("clicked1");
    }

    @EventHandler
    public void onKeyEvent(KeyPressEvent evt) {
        if (evt.isKeyPressed(GLFW_KEY_TAB)) {
            //Engine API replaced GLFW methods.
            gInst.getWindow().setCursorVisibility(!gInst.getWindow().isCursorVisable());
            gInst.getMouseInput().setCursorPosition(gInst.getWindow().getWidth() / 2, gInst.getWindow().getHeight() / 2);
        }
    }
}
