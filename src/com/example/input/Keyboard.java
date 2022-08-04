package com.example.input;

import static org.lwjgl.glfw.GLFW.*;

// https://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html

public class Keyboard {

    // Callback method used with Java 8 method references.
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        System.out.println("keyCallback: key, scancode, action, mods: " + key + ", " + scancode + ", " + action + ", " + mods);
    }

    public static void setWindow(long window) {
        glfwSetKeyCallback(window, Keyboard::keyCallback);        
    }

    public static int KEY_W     = 0; // TODO
    public static int KEY_S     = 1; // TODO
    public static int KEY_D     = 2; // TODO
    public static int KEY_A     = 3; // TODO
    
    public static int KEY_SPACE = 4; // TODO

    public static int KEY_UP    = 5; // TODO
    public static int KEY_DOWN  = 6; // TODO
    public static int KEY_RIGHT = 7; // TODO
    public static int KEY_LEFT  = 8; // TODO

    // Checks to see if a key is down.
    
    // Parameters:
    //     key - Keycode to check
    // Returns:
    //     true if the key is down according to the last poll()
    public static boolean isKeyDown(int key) {
        return false;
    }
}
