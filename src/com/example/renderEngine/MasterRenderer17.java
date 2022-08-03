package com.example.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.joml.Matrix4f;

import com.example.entities.Camera;
import com.example.entities.Entity;
import com.example.entities.Light;
import com.example.models.TexturedModel;
import com.example.shaders.StaticShader16;
import com.example.terrains.Terrain;
import com.example.terrains.TerrainShader17;
import com.example.toolbox.Maths;

public class MasterRenderer17 {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    public static final float SKY_RED   = 0.6f;
    public static final float SKY_GREEN = 0.7f;
    public static final float SKY_BLUE  = 0.8f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    // haze
    //public static final float SKY_DENSITY = 0.0035f;
    //public static final float SKY_GRADIENT = 5f;
    // fog
    public static final float SKY_DENSITY = 0.007f;
    public static final float SKY_GRADIENT = 1.5f;
    
    private Matrix4f projectionMatrix;

    private StaticShader16 shader = new StaticShader16();
    private EntityRenderer16 renderer;
    
    private TerrainRenderer17 terrainRenderer;
    private TerrainShader17 terrainShader = new TerrainShader17();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    
    public MasterRenderer17() {
        enableCulling();
        projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
        renderer = new EntityRenderer16(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer17(terrainShader, projectionMatrix);
    }
    
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(Light sun, Camera camera) {
        prepare();
        
        shader.start();
        shader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        shader.loadSkyVariables(SKY_DENSITY, SKY_GRADIENT);
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
        
        terrainShader.start();
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadSkyVariables(SKY_DENSITY, SKY_GRADIENT);
        terrainShader.loadLight(sun);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
    }
    
    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }
    
    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
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
