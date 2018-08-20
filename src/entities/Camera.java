package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private Vector3f position = new Vector3f(0, 200, 0);
    private float pitch = 90.0f; // high or low
    private float yaw = 0;   // left or right
    private float roll;  // tilt: 180 deg = upside down
    private int moves = 0;

    public Camera() {}

    public void move() {

        double angle = Math.toRadians(yaw);
        double s = 1.0 * Math.sin(angle);
        double c = 1.0 * Math.cos(angle);

        if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                pitch += 2.0f;
                //cout << "pitch : " << pitch << endl;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                pitch -= 2.0f;
                //cout << "pitch : " << pitch << endl;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
                position.x -= c;
                position.z -= s;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
                position.x += c;
                position.z += s;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR )) {
                position.y += 1.5f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
                position.y -= 1.5f;
                if (position.y < 0.1f) {
                        position.y = 0.1f;
                }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                position.x += s;
                position.z -= c;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                position.x -= s;
                position.z += c;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
               yaw -= 2.0f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                yaw += 2.0f;
        }




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


        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            roll += 1f;
            if (roll >= 360f) {
                roll -= 360f;
            }
        }


        moves++;
        if (moves >= 60) {
            System.err.println("Camera at (" + position.x + ", " + position.y + ", " + position.z + ")");
            System.err.println("Camera (pitch, yaw, roll) = (" + pitch + ", " + yaw + ", " + roll + ")");
            moves = 0;
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
}
