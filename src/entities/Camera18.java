package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera18 implements Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    // high or low
    private float pitch = 15;
    // left or right
    private float yaw;
    private float roll;

    public Camera18() {

    }

    public void move() {
    	if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
    		position.z -= 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
    		position.z += 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
    		position.x += 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
    		position.x -= 1f;
    	}
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
}
