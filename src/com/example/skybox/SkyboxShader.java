package com.example.skybox;

import org.joml.Matrix4f;

import com.example.entities.Camera;

import org.joml.Vector3f;

import com.example.renderEngine.DisplayManager;
import com.example.shaders.ShaderProgram;
import com.example.toolbox.Maths;

public class SkyboxShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/com/example/skybox/skyboxVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/com/example/skybox/skyboxFragmentShader.glsl";

    private static final float ROTATE_SPEED = 1f; // was 1f

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_skyColor;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;

    private float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        // remove translation from view matrix
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
        // TODO
        // Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(location_viewMatrix, matrix);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }

    public void connectTextureUnits() {
        super.loadInt(location_cubeMap, 0);
        super.loadInt(location_cubeMap2, 1);
    }

    public void loadBlendFactor(float blend) {
        super.loadFloat(location_blendFactor, blend);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_skyColor = super.getUniformLocation("skyColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}