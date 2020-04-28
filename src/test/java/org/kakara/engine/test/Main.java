package org.kakara.engine.test;

import org.kakara.engine.GameEngine;
import org.lwjgl.system.Configuration;

public class Main {

    public static void main(String[] args){
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel","debug");
        KakaraTest kt = new KakaraTest();

//        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);

        GameEngine gameEng = new GameEngine("Kakara Engine :: Test", 1080, 720, true, kt);
        gameEng.run();
    }
}
