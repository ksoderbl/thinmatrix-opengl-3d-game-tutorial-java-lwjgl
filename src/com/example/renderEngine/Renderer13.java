package com.example.renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Entity;
import com.example.models.RawModel;
import com.example.models.TexturedModel;
import com.example.shaders.StaticShader13;
import com.example.textures.ModelTexture;
import com.example.toolbox.Maths;

public class Renderer13 {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    private Matrix4f projectionMatrix;
    private StaticShader13 shader;
    
    public Renderer13(StaticShader13 shader) {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0.3f, 0.0f, 0.0f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            int vertexCount = model.getRawModel().getVertexCount();
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
    
    public void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // textureCoordinates
        GL20.glEnableVertexAttribArray(2); // normal
        ModelTexture texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }
    
    public void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    
    private void prepareInstance(Entity entity) {
        Vector3f translation = entity.getPosition();
        float rx = entity.getRotX();
        float ry = entity.getRotY();
        float rz = entity.getRotZ();
        float scale = entity.getScale();
        
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                translation, rx, ry, rz, scale);
        shader.loadTransformationMatrix(transformationMatrix);
    }
    
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public float getNearPlane() {
        return NEAR_PLANE;
    }

    public float getFarPlane() {
        return FAR_PLANE;
    }
}
