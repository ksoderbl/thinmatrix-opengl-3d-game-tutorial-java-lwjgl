package com.example.particles;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.models.RawModel;
import com.example.renderEngine.Loader;
import com.example.toolbox.Maths;

public class ParticleRenderer34 {
    
    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    
    private RawModel quad;
    private ParticleShader34 shader;
    
    protected ParticleRenderer34(Loader loader, Matrix4f projectionMatrix) {
        quad = loader.loadToVAO(VERTICES, 2);
        shader = new ParticleShader34();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    protected void render(List<Particle34> particles, Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();
        for (Particle34 particle : particles) {
            updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }
        finishRendering();
    }

    protected void cleanUp() {
        shader.cleanUp();
    }

    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
        Matrix4f modelMatrix = new Matrix4f();
        
        // Matrix4f.translate(position, modelMatrix, modelMatrix);
        modelMatrix.translate(position);

        // Sets the rotation 3x3 part of the model matrix to the transpose
        // of the 3x3 rotation part of the view matrix.
        // See video OpenGL 3D Game Tutorial 34: Particle Effects at about 8 minutes.
        modelMatrix.m00(viewMatrix.m00());
        modelMatrix.m01(viewMatrix.m10());
        modelMatrix.m02(viewMatrix.m20());
        modelMatrix.m10(viewMatrix.m01());
        modelMatrix.m11(viewMatrix.m11());
        modelMatrix.m12(viewMatrix.m21());
        modelMatrix.m20(viewMatrix.m02());
        modelMatrix.m21(viewMatrix.m12());
        modelMatrix.m22(viewMatrix.m22());

        // Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        // Matrix4f.mul(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        // Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null); 
        modelMatrix.rotate((float)Math.toRadians(rotation), new Vector3f(0, 0, 1));
        modelMatrix.scale(scale, scale, scale);
        Matrix4f modelViewMatrix = viewMatrix.mul(modelMatrix);

        shader.loadModelViewMatrix(modelViewMatrix);
    }

    private void prepare() {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
    }
    
    private void finishRendering() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}
