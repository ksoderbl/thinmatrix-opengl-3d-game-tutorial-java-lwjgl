package com.example.renderEngine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.example.input.Keyboard;
import com.example.input.Mouse;

public class Display {
    private static String title;
    private static int width;
    private static int height;
    
    private static long window = -1L;
    
    public static void frameBufferSizeCallback(long window, int w, int h) {
        // System.out.println("frameBufferSizeCallback: w, h: " + w + ", " + h);
        width = w;
        height = h;
        glViewport(0, 0, width, height);
    }

    public static void createDisplay(String Title, int Width, int Height) {
        title = Title;
        width = Width;
        height = Height;

        glfwInit();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        // glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();

        // callbacks
        glfwSetFramebufferSizeCallback(window, Display::frameBufferSizeCallback);
        Keyboard.setWindow(window);
        Mouse.setWindow(window);
    }

    public static boolean isCloseRequested() {
        return glfwWindowShouldClose(window);
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public static void destroy() {
        glfwTerminate();
    }

    public static String getTitle() {
        return title;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
