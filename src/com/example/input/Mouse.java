package com.example.input;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

// https://legacy.lwjgl.org/javadoc/org/lwjgl/input/Mouse.html

public class Mouse {

    private static HashMap<Integer, String> buttonsDown = new HashMap<Integer, String>();

    // Callback method used with Java 8 method references.
    public static void mouseCallback(long window, int button, int action, int mods) {
        System.out.println("mouseCallback: button, action, mods: " + button + ", " + action + ", " + mods);

        switch (button) {
            case GLFW_MOUSE_BUTTON_LEFT: System.out.println("left"); break;
            case GLFW_MOUSE_BUTTON_MIDDLE: System.out.println("middle"); break;
            case GLFW_MOUSE_BUTTON_RIGHT: System.out.println("right"); break;
            default: break;
        }

        if (action == GLFW_PRESS) {
            buttonsDown.put(button, "down");
        }
        else if (action == GLFW_RELEASE) {
            buttonsDown.remove(button);
        }
    }

    public static void setWindow(long window) {
        glfwSetMouseButtonCallback(window, Mouse::mouseCallback);        
    }

    // See if a particular mouse button is down.
    // Parameters:
    //     button - The index of the button you wish to test (0..getButtonCount-1)
    // Returns:
    //     true if the specified button is down
    public static boolean isButtonDown(int button) {
        return buttonsDown.containsKey(button);
    }

    // Retrieves the absolute position. It will be clamped to 0...width-1.
    // Returns:
    // Absolute x axis position of mouse
    public static int getX() {
        return 0;
    }

    // Retrieves the absolute position. It will be clamped to 0...height-1.
    // Returns:
    // Absolute y axis position of mouse
    public static int getY() {
        return 0;
    }

    // Returns:
    // Movement on the x axis since last time getDX() was called.
    public static int getDX() {
        // TODO
        return 0;
    }

    // Returns:
    // Movement on the y axis since last time getDY() was called.
    public static int getDY() {
        // TODO
        return 0;
    }

    // Returns:
    // Movement of the wheel since last time getDWheel() was called
    public static int getDWheel() {
        // TODO
        return 0;
    }

}
