package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private final float MIN_DISTANCE_FROM_PLAYER = 5;
    private final float MAX_DISTANCE_FROM_PLAYER = 1000;
    private final float MIN_PITCH = 3;
    private final float MAX_PITCH = 90 - MIN_PITCH;
    private final float OFFSET_CAMERA_Y = 7;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(100, 35, 50);
    private float pitch = 20.0f; // high or low
    private float yaw = 0;   // left or right
    private float roll;  // tilt: 180 deg = upside down
    private int moves = 0;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);

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

    public void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + OFFSET_CAMERA_Y;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer < MIN_DISTANCE_FROM_PLAYER)
            distanceFromPlayer = MIN_DISTANCE_FROM_PLAYER;
        if (distanceFromPlayer > MAX_DISTANCE_FROM_PLAYER)
            distanceFromPlayer = MAX_DISTANCE_FROM_PLAYER;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
            if (pitch < MIN_PITCH)
                pitch = MIN_PITCH;
            if (pitch > MAX_PITCH)
                pitch = MAX_PITCH;
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }
}
