package com.example.renderEngine;

import static java.lang.System.*;

public class DisplayManager {
    private static final int CREATE_WIDTH = 1280;
    private static final int CREATE_HEIGHT = 720;
    // private static final int FPS_CAP = 120;

    private static long lastFrameTime = getCurrentTime();
    private static float delta = 1.0f / 60f;  // TODO
    
    private static long oldNanoTime = 0;
    private static int frames = 0;

    private static boolean vsync = true; // TODO

    public static void createDisplay(String title) {
        createDisplay(title, CREATE_WIDTH, CREATE_HEIGHT);
    }
    
    public static void createDisplay(String title, int width, int height) {
        Display.createDisplay(title, width, height);
    }

    // returns number of frames drawn since fps printout
    // 0 means fps was just printed out
    public static int updateDisplay() {
        if (vsync) {
            // TODO
            // Display.sync(FPS_CAP);
        }

        // TODO
        // if (Display.wasResized()) {
        //     // TODO: create a new projection matrix here?
        //     GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        // }
        
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;

        // fps calculation
        frames += 1;
        long nanoTime = System.nanoTime();
        long deltaTime = nanoTime - oldNanoTime;
        if (deltaTime > 1000000000) {
            if (oldNanoTime > 0) {
                double seconds = deltaTime * 1e-9;
                double fps = frames / seconds;
                // System.out.println("fps = " + fps);
                frames = 0;
            }
            oldNanoTime = nanoTime;
        }
        
        return frames;
    }

    public static float getFrameTimeSeconds() {
        return delta;
    }
    
    public static void setVSync(boolean value) {
        vsync = value;
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    // returns current time in milliseconds
    private static long getCurrentTime() {
        // return Sys.getTime()*1000/Sys.getTimerResolution();
        return currentTimeMillis();
    }

}
