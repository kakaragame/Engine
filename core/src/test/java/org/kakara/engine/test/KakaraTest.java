package org.kakara.engine.test;

import org.kakara.engine.Game;
import org.kakara.engine.GameHandler;
import org.kakara.engine.scene.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class KakaraTest implements Game {

    private GameHandler gInst;

    @Override
    public void start(GameHandler handler) throws Exception {

        gInst = handler;

        handler.getWindow().setClearColor(1, 0, 0, 1);

//        TitleScreenScene tss = new TitleScreenScene(handler, this);
////        MainGameScene mgs = new MainGameScene(handler, this);
//        gInst.getSceneManager().setScene(tss);
        try {
            gInst.getSoundManager().init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(glfwGetGamepadName(GLFW_JOYSTICK_2));
        System.out.println(glfwGetGamepadName(GLFW_JOYSTICK_1));

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
    public Scene firstScene(GameHandler handler) throws Exception {
        TitleScreenScene tss = new TitleScreenScene(handler, this);
        return tss;
    }


    @Override
    public void update() {
    }

    @Override
    public void exit() {
        System.out.println("The game has been terminated.");
    }
}
