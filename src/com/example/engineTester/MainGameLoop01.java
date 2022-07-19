package com.example.engineTester;

import com.example.renderEngine.DisplayManager;

public class MainGameLoop01 {
	public static String title = "OpenGL 3D Game Tutorial 1";
	public static String subTitle = "The Display";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);

        while (!DisplayManager.isCloseRequested()) {
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }
}
