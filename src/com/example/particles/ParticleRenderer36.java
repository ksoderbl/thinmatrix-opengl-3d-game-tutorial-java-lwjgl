package com.example.particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.models.RawModel;
import com.example.renderEngine.Loader;
import com.example.toolbox.Maths;

public class ParticleRenderer36 {
    
    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final int MAX_INSTANCES = 10000; // max this many particles
    private static final int INSTANCE_DATA_LENGTH = 21; // OpenGL 3D Game Tutorial 36: Instanced Rendering at 16 minutes
    
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
    
    private RawModel quad;
    private ParticleShader36 shader;
    
    private Loader loader;
    private int vbo;
    private int pointer = 0;
    
    protected ParticleRenderer36(Loader loader, Matrix4f projectionMatrix) {
        this.loader = loader;
        this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
        loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20);
        shader = new ParticleShader36();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    protected void render(Map<ParticleTexture36, List<Particle36>> particles, Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();
        for (ParticleTexture36 texture : particles.keySet()) {
            bindTexture(texture);
            List<Particle36> particleList = particles.get(texture);
            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
            shader.loadNumberOfRows(texture.getNumberOfRows());
            for (Particle36 particle : particleList) {
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(),
                        particle.getScale(), viewMatrix, vboData);
                updateTexCoordInfo(particle, vboData);
            }
            loader.updateVbo(vbo, vboData, buffer);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
        }
        finishRendering();
    }

    protected void cleanUp() {
        shader.cleanUp();
    }

    private void updateTexCoordInfo(Particle36 particle, float[] data) {
        data[pointer++] = particle.getTexOffset1().x;
        data[pointer++] = particle.getTexOffset1().y;
        data[pointer++] = particle.getTexOffset2().x;
        data[pointer++] = particle.getTexOffset2().y;
        data[pointer++] = particle.getBlend();
    }
    
    private void bindTexture(ParticleTexture36 texture) {
        if (texture.isAdditive()) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }
    
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, 
            Matrix4f viewMatrix, float[] vboData) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        // Sets the rotation 3x3 part of the model matrix to the transpose
        // of the 3x3 rotation part of the view matrix.
        modelMatrix.m00(viewMatrix.m00());
        modelMatrix.m01(viewMatrix.m10());
        modelMatrix.m02(viewMatrix.m20());
        modelMatrix.m10(viewMatrix.m01());
        modelMatrix.m11(viewMatrix.m11());
        modelMatrix.m12(viewMatrix.m21());
        modelMatrix.m20(viewMatrix.m02());
        modelMatrix.m21(viewMatrix.m12());
        modelMatrix.m22(viewMatrix.m22());
        
        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.mul(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null); 
        
//        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
//        Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
//        Matrix4f.mul(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);

        storeMatrixData(modelViewMatrix, vboData);
    }

    public void storeMatrixData(Matrix4f matrix, float[] vboData) {
        vboData[pointer++] = matrix.m00();
        vboData[pointer++] = matrix.m01();
        vboData[pointer++] = matrix.m02();
        vboData[pointer++] = matrix.m03();
        vboData[pointer++] = matrix.m10();
        vboData[pointer++] = matrix.m11();
        vboData[pointer++] = matrix.m12();
        vboData[pointer++] = matrix.m13();
        vboData[pointer++] = matrix.m20();
        vboData[pointer++] = matrix.m21();
        vboData[pointer++] = matrix.m22();
        vboData[pointer++] = matrix.m23();
        vboData[pointer++] = matrix.m30();
        vboData[pointer++] = matrix.m31();
        vboData[pointer++] = matrix.m32();
        vboData[pointer++] = matrix.m33();
    }
    
    private void prepare() {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
    }
    
    private void finishRendering() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}
