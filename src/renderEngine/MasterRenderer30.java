package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader30;
import shaders.TerrainShader30;
import skybox.Sky;
import skybox.SkyboxRenderer30;
import terrains.Terrain;

public class MasterRenderer30 {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 100000;
	
	// Sky variables moved to skybox
	
	private Matrix4f projectionMatrix;

    private StaticShader30 shader = new StaticShader30();
    private EntityRenderer30 renderer;
    
    private TerrainRenderer30 terrainRenderer;
    private TerrainShader30 terrainShader = new TerrainShader30();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();
    
    private SkyboxRenderer30 skyboxRenderer;
    
    public MasterRenderer30(Loader loader) {
    	enableCulling();
    	createProjectionMatrix();
    	renderer = new EntityRenderer30(shader, projectionMatrix);
    	terrainRenderer = new TerrainRenderer30(terrainShader, projectionMatrix);
    	skyboxRenderer = new SkyboxRenderer30(loader, projectionMatrix);
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
        render(lights, sky, camera, clipPlane);
    }

    public void render(List<Light> lights, Sky sky, Camera camera, Vector4f clipPlane) {
        prepare(sky);
        
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColor(sky.getColor());
        shader.loadSkyVariables(sky.getDensity(), sky.getGradient());
    	shader.loadLights(lights);
    	shader.loadViewMatrix(camera);
    	renderer.render(entities);
        shader.stop();
        
    	terrainShader.start();
    	terrainShader.loadClipPlane(clipPlane);
    	terrainShader.loadSkyColor(sky.getColor());
    	terrainShader.loadSkyVariables(sky.getDensity(), sky.getGradient());
        terrainShader.loadLights(lights);
    	terrainShader.loadViewMatrix(camera);
    	terrainRenderer.render(terrains);
    	terrainShader.stop();
        
        skyboxRenderer.render(camera, sky.getColor());
        
    	entities.clear();
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
