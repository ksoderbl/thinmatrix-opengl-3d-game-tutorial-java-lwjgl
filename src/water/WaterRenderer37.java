package water;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import skybox.Sky;
import toolbox.Maths;

public class WaterRenderer37 {
	
	private static final String DUDV_MAP = "waterDUDV";
	private static final String NORMAL_MAP = "normalMap";

	private RawModel quad;
	private WaterShader37 shader;
	private WaterFrameBuffers fbos;

	private float waveSpeed = 0.03f;
    private float waterTiling = 20f; // was 6 in OpenGL Water Tutorial 5: DuDv Maps
	private float moveFactor = 0f;
    private float waveStrength = 0.04f; // 0.02 before water tutorial 8
    private float waterReflectivity = 2f; // for fresnel effect, thinmatrix had 0.5
    private float shineDamper = 10.0f; // for normal maps
    private float reflectivity = 0.1f; // for normal maps
	
	private int dudvTexture;
	private int normalMap;
	
	private float shadingLevels;
	
	public WaterRenderer37(Loader loader, WaterShader37 shader, Matrix4f projectionMatrix,
			float nearPlane, float farPlane, WaterFrameBuffers fbos, float shadingLevels) {
		this.shader = shader;
		this.fbos = fbos;
		this.shadingLevels = shadingLevels;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadNearPlane(nearPlane);
        shader.loadFarPlane(farPlane);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Sky sky, Camera camera, List<Light> lights) {
		prepareRender(sky, camera, lights);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
                    0, 0, 0, tile.getSize());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Sky sky, Camera camera, List<Light> lights) {
		shader.start();
		shader.loadViewMatrix(camera);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_1))
		    waterTiling -= 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_2))
            waterTiling += 0.1;

		shader.loadWaterTiling(waterTiling);

		moveFactor += waveSpeed * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
        shader.loadWaveStrength(waveStrength); // set waveStrength to 0 to remove the dudvMap distortion
        shader.loadWaterReflectivity(waterReflectivity); // e.g.  0.5
        shader.loadShineVariables(shineDamper, reflectivity);
        
        shader.loadLights(lights);
        shader.loadSkyColor(sky.getColor());
        shader.loadSkyVariables(sky.getDensity(), sky.getGradient());
        shader.loadShadingLevels(shadingLevels);
       
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
	    GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = {
				 0, 0,
				 0, 1,
				 1, 0,
				 1, 0,
				 0, 1,
				 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
