package water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import models.RawModel;
import renderEngine.Loader;
import skybox.Sky;
import toolbox.Maths;

public class WaterRenderer04 {

	private RawModel quad;
	private WaterShader04 shader;
	private WaterFrameBuffers fbos;

	public WaterRenderer04(Loader loader, WaterShader04 shader, Matrix4f projectionMatrix,
			WaterFrameBuffers fbos) {
		this.shader = shader;
		this.fbos = fbos;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Sky sky, Camera camera) {
		prepareRender(sky, camera);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
                    0, 0, 0, tile.getSize());
			shader.loadTransformationMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Sky sky, Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
        shader.loadSkyColor(sky.getColor());
        shader.loadSkyVariables(sky.getDensity(), sky.getGradient());
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
    }
	
	private void unbind() {
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
