package com.example.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.example.entities.Camera;
import com.example.input.Mouse;
import com.example.renderEngine.Display;
import com.example.terrains.Terrain;
import com.example.terrains.World;

// http://antongerdelan.net/opengl/raycasting.html
public class MousePicker {

    private static final int RECURSION_COUNT = 2000; // was 200
    private static final float RAY_RANGE = 10000; // was 600

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private World world;
    private Vector3f currentTerrainPoint;

    public MousePicker(Camera camera, Matrix4f projectionMatrix, World world) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Maths.createViewMatrix(camera);
        this.world = world;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay() {
        // viewport space
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();

        // normalized device space
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        // homogenous clip space
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1);
        // eye space
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        // world space
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        // TODO
        // Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        // Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        // Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        Vector3f mouseRay = new Vector3f();
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        //
        // Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        // Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        Vector4f eyeCoords = new Vector4f();
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    // convert to opengl coordinate system -1 -> 1, -1 -> 1,
    // origin at the center of screen
    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY) {
        float x = (2f * mouseX) / Display.getWidth() - 1;
        float y = (2f * mouseY) / Display.getHeight() - 1;
        return new Vector2f(x, y);
    }

    //**********************************************************

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        //return Vector3f.add(start, scaledRay, null);
        return start.add(scaledRay);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.get(0), endPoint.get(2));
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getTerrain(testPoint.get(0), testPoint.get(2));
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.get(0), testPoint.get(2));
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

    private Terrain getTerrain(float worldX, float worldZ) {
        // TODO
        //int x = worldX / Terrain.SIZE;
        //int z = worldZ / Terrain.SIZE;
        //return terrains[x][z];
        //return terrain;
        return world.getTerrain(worldX, worldZ);
    }
}
