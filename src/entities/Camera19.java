package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera19 implements Camera {

    private final float MIN_DISTANCE_FROM_PLAYER = 1;
    //private final float MAX_DISTANCE_FROM_PLAYER = 1000;
    private final float MIN_PITCH = -90;
    private final float MAX_PITCH = 90;
    private final float CAMERA_Y_OFFSET = 7;
    private final float CAMERA_PITCH_OFFSET = 0; // ThinMatrix has 4
    private final float ZOOM_LEVEL_FACTOR = 0.1f;
    private final float PITCH_CHANGE_FACTOR = 0.2f;
    private final float ANGLE_AROUND_PLAYER_CHANGE_FACTOR = 0.3f;

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20.0f; // high or low
    private float yaw = 0;   // left or right
    private float roll;  // tilt: 180 deg = upside down
    private int moves = 0;

    private Player19 player;

    public Camera19(Player19 player) {
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
            //System.err.println("Camera at (" + position.x + ", " + position.y + ", " + position.z + ")");
            //System.err.println("Camera (pitch, yaw, roll) = (" + pitch + ", " + yaw + ", " + roll + ")");
            moves = 0;
        }

        yaw %= 360;
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
    
    public void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + CAMERA_Y_OFFSET;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch + CAMERA_PITCH_OFFSET)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch + CAMERA_PITCH_OFFSET)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * ZOOM_LEVEL_FACTOR;
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer < MIN_DISTANCE_FROM_PLAYER)
            distanceFromPlayer = MIN_DISTANCE_FROM_PLAYER;
        //if (distanceFromPlayer > MAX_DISTANCE_FROM_PLAYER)
        //    distanceFromPlayer = MAX_DISTANCE_FROM_PLAYER;
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * PITCH_CHANGE_FACTOR;
            pitch -= pitchChange;
            if (pitch < MIN_PITCH) {
                pitch = MIN_PITCH;
            } else if (pitch > MAX_PITCH) {
                pitch = MAX_PITCH;
            }
        }
    }

    private void calculateAngleAroundPlayer() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * ANGLE_AROUND_PLAYER_CHANGE_FACTOR;
            angleAroundPlayer -= angleChange;
        }
    }
}
