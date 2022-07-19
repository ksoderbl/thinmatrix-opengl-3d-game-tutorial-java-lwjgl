package com.example.renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {
    private static final int CREATE_WIDTH = 800;
    private static final int CREATE_HEIGHT = 600;
    private static long window = -1L;

    public static void createDisplay(String title) {
    	createDisplay(title, CREATE_WIDTH, CREATE_HEIGHT);
    }
    
    public static void createDisplay(String title, int width, int height) {
        glfwInit();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
    }

    public static boolean isCloseRequested() {
        return glfwWindowShouldClose(window);
    }

    public static void updateDisplay() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public static void closeDisplay() {
        glfwTerminate();
    }
}
