package com.example.renderEngine;



public class DisplayManager {
    private static final int CREATE_WIDTH = 1280;
    private static final int CREATE_HEIGHT = 720;
    // private static final int FPS_CAP = 120;
    private static Display display;

    // private static boolean vsync = true; // TODO

    public static void createDisplay(String title) {
        createDisplay(title, CREATE_WIDTH, CREATE_HEIGHT);
    }
    
    public static void createDisplay(String title, int width, int height) {
        Display.createDisplay(title, width, height);
    }

    public static void updateDisplay() {
        Display.updateDisplay();
    }

    public static void closeDisplay() {
        Display.closeDisplay();
    }
}
