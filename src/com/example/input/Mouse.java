package com.example.input;

import java.util.HashMap;

import com.example.renderEngine.Display;

import static org.lwjgl.glfw.GLFW.*;

// https://legacy.lwjgl.org/javadoc/org/lwjgl/input/Mouse.html

public class Mouse {

    private static HashMap<Integer, String> buttonsDown = new HashMap<Integer, String>();

    private static double mouseX = 0;
    private static double mouseY = 0;

    // for getDX and getDY
    private static int oldX = 0;
    private static int oldY = 0;
    private static boolean haveOldX = false;
    private static boolean haveOldY = false;

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

    public static void cursorPosCallback(long window, double x, double y) {
        System.out.println("cursorPosCallback: x, y: " + x + ", " + y);
        mouseX = x;
        mouseY = y;
    }

    public static void setWindow(long window) {
        glfwSetMouseButtonCallback(window, Mouse::mouseCallback);
        glfwSetCursorPosCallback(window, Mouse::cursorPosCallback);
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
        int width = Display.getWidth();
        int x = (int) Math.round(mouseX);
        if (x < 0) {
            x = 0;
        }
        else if (x > width - 1) {
            x =  width - 1;
        }
        System.out.println("x: " + x);
        return x;
    }

    // Retrieves the absolute position. It will be clamped to 0...height-1.
    // Returns:
    // Absolute y axis position of mouse
    public static int getY() {
        int height = Display.getHeight();
        int y = (int) Math.round(mouseY);
        if (y < 0) {
            y = 0;
        }
        else if (y > height - 1) {
            y = height - 1;
        }
        System.out.println("y: " + y);
        return y;
    }

    // Returns:
    // Movement on the x axis since last time getDX() was called.
    public static int getDX() {
        int x = getX();

        if (haveOldX) {
            int dx = x - oldX;
            oldX = x;
            haveOldX = true;
            // System.out.println("DX: "+dx);
            return dx;
        }
        
        oldX = x;
        haveOldX = true;
        return 0;
    }

    // Returns:
    // Movement on the y axis since last time getDY() was called.
    public static int getDY() {
        int y = getY();

        if (haveOldY) {
            int dy = y - oldY;
            oldY = y;
            haveOldY = true;
            // System.out.println("DY: "+dy);

            // returning -dy here because we want the opposition direction for the move
            return -dy;
        }
        
        oldY = y;
        haveOldY = true;
        return 0;
    }

    // Returns:
    // Movement of the wheel since last time getDWheel() was called
    public static int getDWheel() {
        // TODO
        return 0;
    }

}
