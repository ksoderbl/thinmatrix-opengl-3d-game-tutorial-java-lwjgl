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
import com.example.shaders.StaticShader30;
import textures.ModelTexture;
import com.example.toolbox.Maths;

public class EntityRenderer30 {
    
    private StaticShader30 shader;
    
    // Tutorial 30: Cel Shading
    private float shadingLevels = 10.0f;
    
    public EntityRenderer30(StaticShader30 shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    public void render(Map<TexturedModel, List<Entity>> entities) {
        
        shader.loadShadingLevels(shadingLevels);
        
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
        // OpenGL 3D Game Tutorial 23: Texture Atlases
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }
    
    public void unbindTexturedModel() {
        MasterRenderer.enableCulling();
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
        // OpenGL 3D Game Tutorial 23: Texture Atlases
        shader.loadTextureOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
}
