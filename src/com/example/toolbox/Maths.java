package com.example.toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.renderEngine.Display;

public class Maths {

    public static float baryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    // // For OpenGL 3D Game Tutorial 24: Rendering GUIs
    public static Matrix4f createTransformationMatrix(
            Vector2f translation, Vector2f scale) {

        // matrix.setIdentity();
        // Matrix4f.translate(translation, matrix, matrix);
        // Matrix4f.mul(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);

        Matrix4f m = new Matrix4f();

        m.translate(translation.x, translation.y, 0);
        m.scale(scale.x, scale.y, 1.0f);

        // Something weird going on since I need this transpose here for the transformation to work
        // correctly.
        m.transpose();
        
        return m;
    }

    private static void printMatrix(Matrix4f m, String name) {
        System.out.println("Matrix " + name);
        System.out.println("" + m.m00() + " " + m.m10() + " " + m.m20() + " " + m.m30());
        System.out.println("" + m.m01() + " " + m.m11() + " " + m.m21() + " " + m.m31());
        System.out.println("" + m.m02() + " " + m.m12() + " " + m.m22() + " " + m.m32());
        System.out.println("" + m.m03() + " " + m.m13() + " " + m.m23() + " " + m.m33());
    }

    // For OpenGL 3D Game Tutorial 7: Matrices & Uniform Variables
    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            float rx,
            float ry,
            float rz,
            float scale) {

        // // Matrix4f matrix = new Matrix4f();
        // // matrix.setIdentity();
        // // Matrix4f.translate(translation, matrix, matrix);
        // // Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        // // Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        // // Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        // // Matrix4f.mul(new Vector3f(scale, scale, scale), matrix, matrix);

        // Matrix4f matrix = new Matrix4f();

        // // System.out.println("matrix1: \n" + matrix.toString());

        // matrix.translate(translation);

        // // System.out.println("matrix after translation: \n" + matrix.toString());

        // matrix.rotateXYZ((float) Math.toRadians(rx), (float) Math.toRadians(ry), (float) Math.toRadians(rz));

        // // System.out.println("matrix after rotation: \n" + matrix.toString());

        // matrix.scale(scale);

        // // System.out.println("matrix after scaling: \n" + matrix.toString());

        // return matrix;

        // Matrix4f t = new Matrix4f();

        // System.out.println("t1: \n" + t.toString());
        // printMatrix(t, "t1");

        // t.translate(translation);

        // System.out.println("t2: \n" + t.toString());
        // printMatrix(t, "t2");

        // Matrix4f s = new Matrix4f();
        // Matrix4f r = new Matrix4f();
        // Matrix4f m = new Matrix4f();

        // m.mul(s);
        // m.mul(r);
        // m.mul(t);

        // System.out.println("m: \n" + m.toString());
        // printMatrix(m, "m");

        // m.transpose();

        // System.out.println("m transposed: \n" + m.toString());
        // printMatrix(m, "m transposed");

        Matrix4f m = new Matrix4f();

        m.translate(translation);
        m.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0));
        m.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0));
        m.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1));
        m.scale(scale);

        // Something weird going on since I need this transpose here for the transformation to work
        // correctly.
        m.transpose();

        return m;
    }

    public static Matrix4f createProjectionMatrix(float fov, float nearPlane, float farPlane) {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustumLength = farPlane - nearPlane;

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((farPlane + nearPlane) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * nearPlane * farPlane) / frustumLength));
        projectionMatrix.m33(0);

        // Doesn't seem to work right without this transpose here for some reason.
        projectionMatrix.transpose();

        return projectionMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()),   new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()),  new Vector3f(0, 0, 1));
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos);

        // Doesn't seem to work right without this transpose here for some reason.
        viewMatrix.transpose();

        return viewMatrix;
    }

    // // same as above, but without Camera argument
    public static Matrix4f createViewMatrix(
            Vector3f cameraPos,
            float pitch,
            float yaw,
            float roll) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(yaw),   new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(roll),  new Vector3f(0, 0, 1));
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos);

        // Doesn't seem to work right without this transpose here for some reason.
        viewMatrix.transpose();

        return viewMatrix;
    }
}
