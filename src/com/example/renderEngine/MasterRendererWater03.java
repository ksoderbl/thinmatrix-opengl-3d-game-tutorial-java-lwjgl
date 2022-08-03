package com.example.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.example.entities.Camera;
import com.example.entities.Entity;
import com.example.entities.Light;
import com.example.models.TexturedModel;
import com.example.shaders.StaticShaderWater03;
import com.example.skybox.Sky;
import com.example.terrains.Terrain;
import com.example.terrains.TerrainShaderWater03;
import com.example.toolbox.Maths;

public class MasterRendererWater03 {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    public static final float SKY_RED   = 0.6f;
    public static final float SKY_GREEN = 0.7f;
    public static final float SKY_BLUE  = 0.8f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    // haze
    public static final float SKY_DENSITY = 0.0035f;
    public static final float SKY_GRADIENT = 5f;
    // fog
    //public static final float SKY_DENSITY = 0.007f;
    //public static final float SKY_GRADIENT = 1.5f;
    
    private Matrix4f projectionMatrix;

    private StaticShaderWater03 shader = new StaticShaderWater03();
    private EntityRendererWater03 renderer;
    
    private TerrainRendererWater03 terrainRenderer;
    private TerrainShaderWater03 terrainShader = new TerrainShaderWater03();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    
    public MasterRendererWater03() {
        enableCulling();
        projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
        renderer = new EntityRendererWater03(shader, projectionMatrix);
        terrainRenderer = new TerrainRendererWater03(terrainShader, projectionMatrix);
    }
    
    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
    
    public void renderScene(List<Entity> entities, List<Terrain> terrains,
            List<Light> lights, Sky sky, Camera camera, Vector4f clipPlane) {
        for (Entity entity : entities) {
            processEntity(entity);
        }
        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }
        Light light = lights.get(0);
        render(light, sky, camera, clipPlane);
    }

    public void render(Light sun, Sky sky, Camera camera, Vector4f clipPlane) {
        prepare(sky);
        
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColor(sky.getColor());
        shader.loadSkyVariables(sky.getDensity(), sky.getGradient());
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
        
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(sky.getColor());
        terrainShader.loadSkyVariables(sky.getDensity(), sky.getGradient());
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
    
    public void prepare(Sky sky) {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Vector3f skyColor = sky.getColor();
        GL11.glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
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
