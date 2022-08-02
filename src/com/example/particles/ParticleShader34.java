package com.example.particles;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.example.shaders.ShaderProgram;

public class ParticleShader34 extends ShaderProgram {

    private static final String VERTEX_FILE = "src/com/example/particles/particleVShader34.glsl";
    private static final String FRAGMENT_FILE = "src/com/example/particles/particleFShader34.glsl";

    private int location_modelViewMatrix;
    private int location_projectionMatrix;
    private int location_texOffset1;
    private int location_texOffset2;
    private int location_texCoordInfo;

    public ParticleShader34() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_texOffset1 = super.getUniformLocation("texOffset1");
        location_texOffset2 = super.getUniformLocation("texOffset2");
        location_texCoordInfo = super.getUniformLocation("texCoordInfo");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    protected void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend) {
        super.load2DVector(location_texOffset1, offset1);
        super.load2DVector(location_texOffset2, offset2);
        super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
    }
    
    
    protected void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix(location_modelViewMatrix, modelViewMatrix);
    }

    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

}
