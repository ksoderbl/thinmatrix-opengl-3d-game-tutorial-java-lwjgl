package com.example.input;

import static org.lwjgl.glfw.GLFW.*;

// https://legacy.lwjgl.org/javadoc/org/lwjgl/input/Mouse.html

public class Mouse {

    // Callback method used with Java 8 method references.
    public static void mouseCallback(long win, int button, int action, int mods) {
        System.out.println("mouseCallback: win: " + win);
        System.out.println("mouseCallback: button: " + button);
        System.out.println("mouseCallback: action: " + action);
        System.out.println("mouseCallback: mods: " + mods);

        if (action == GLFW_PRESS) {
            System.out.println("Pressed!");
        }
    }

    // See if a particular mouse button is down.
    // Parameters:
    //     button - The index of the button you wish to test (0..getButtonCount-1)
    // Returns:
    //     true if the specified button is down
    public static boolean isButtonDown(int button) {
        // TODO
        return false;
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
