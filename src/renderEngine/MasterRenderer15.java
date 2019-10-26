package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader15;
import shaders.TerrainShader14;
import terrains.Terrain14;

public class MasterRenderer15 {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;

    private StaticShader15 shader = new StaticShader15();
    private EntityRenderer15 renderer;
    
    private TerrainRenderer14 terrainRenderer;
    private TerrainShader14 terrainShader = new TerrainShader14();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain14> terrains = new ArrayList<>();
    
    public MasterRenderer15() {
    	enableCulling();
    	createProjectionMatrix();
    	renderer = new EntityRenderer15(shader, projectionMatrix);
    	terrainRenderer = new TerrainRenderer14(terrainShader, projectionMatrix);
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
    	shader.loadLight(sun);
    	shader.loadViewMatrix(camera);
    	renderer.render(entities);
        shader.stop();
    	entities.clear();
        
    	terrainShader.start();
        terrainShader.loadLight(sun);
    	terrainShader.loadViewMatrix(camera);
    	terrainRenderer.render(terrains);
    	terrainShader.stop();
        terrains.clear();
    }
    
    public void processTerrain(Terrain14 terrain) {
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
        GL11.glClearColor(0.6f, 1.0f, 1.0f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
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
