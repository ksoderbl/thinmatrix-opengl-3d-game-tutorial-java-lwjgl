package com.example.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;

public class Maths {

    public static float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    // // For OpenGL 3D Game Tutorial 24: Rendering GUIs
    // public static Matrix4f createTransformationMatrix(
    //         Vector2f translation, Vector2f scale) {
    //     Matrix4f matrix = new Matrix4f();
    //     // matrix.setIdentity();
    //     // Matrix4f.translate(translation, matrix, matrix);
    //     // Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
    //     return matrix;
    // }

    // For OpenGL 3D Game Tutorial 7: Matrices & Uniform Variables
    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            float rx,
            float ry,
            float rz,
            float scale) {

        // Matrix4f matrix = new Matrix4f();
        // matrix.setIdentity();
        // Matrix4f.translate(translation, matrix, matrix);
        // Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        // Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        // Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        // Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

        Matrix4f matrix = new Matrix4f();

        System.out.println("matrix1: \n" + matrix.toString());

        matrix.translate(translation);

        // System.out.println("matrix after translation: \n" + matrix.toString());

        matrix.rotateXYZ(rx, ry, rz);

        // System.out.println("matrix after rotation: \n" + matrix.toString());

        matrix.scale(scale);

        // System.out.println("matrix after scaling: \n" + matrix.toString());

        return matrix;
    }

    // public static Matrix4f createViewMatrix(Camera camera) {
    //     Matrix4f viewMatrix = new Matrix4f();
    //     // viewMatrix.setIdentity();
    //     // Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
    //     // Matrix4f.rotate((float) Math.toRadians(camera.getYaw()),   new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
    //     // Matrix4f.rotate((float) Math.toRadians(camera.getRoll()),  new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
    //     // Vector3f cameraPos = camera.getPosition();
    //     // Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    //     // Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    //     return viewMatrix;
    // }

    // // same as above, but without Camera argument
    // public static Matrix4f createViewMatrix(
    //         Vector3f cameraPos,
    //         float pitch,
    //         float yaw,
    //         float roll) {
    //     Matrix4f viewMatrix = new Matrix4f();
    //     // viewMatrix.setIdentity();
    //     // Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
    //     // Matrix4f.rotate((float) Math.toRadians(yaw),   new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
    //     // Matrix4f.rotate((float) Math.toRadians(roll),  new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
    //     // Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    //     // Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
    //     return viewMatrix;
    // }
}
