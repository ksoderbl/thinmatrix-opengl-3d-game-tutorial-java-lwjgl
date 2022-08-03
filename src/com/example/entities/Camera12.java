package com.example.entities;

import org.joml.Vector3f;

import com.example.input.Keyboard;

public class Camera12 implements Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    // high or low
    private float pitch;
    // left or right
    private float yaw;
    private float roll;

    public Camera12() {

    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.1f;
        }
    }
    
    public void invertPitch() {
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void printPosition() {
        System.out.println("Camera Pos: (" + position.get(0) + ", " + position.get(1) + ", " + position.get(2) + ")");
    }
}
