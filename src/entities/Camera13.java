package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera13 {

    private Vector3f position = new Vector3f(0, 0, 0);
    // high or low
    private float pitch;
    // left or right
    private float yaw;
    private float roll;

    public Camera13() {

    }

    public void move() {
    	if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
    		position.z -= 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
    		position.z += 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
    		position.x += 1f;
    	}
    	if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
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
