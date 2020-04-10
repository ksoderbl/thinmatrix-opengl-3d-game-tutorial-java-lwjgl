package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera08 implements Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    // high or low
    private float pitch;
    // left or right
    private float yaw;
    private float roll;

    public Camera08() {

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
    	System.out.println("Camera Pos: (" + position.getX() + ", " + position.getY() + ", " + position.getZ() + ")");
    }
}
