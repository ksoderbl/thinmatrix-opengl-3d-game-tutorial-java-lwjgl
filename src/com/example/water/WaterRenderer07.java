package com.example.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.entities.Light;
import com.example.models.RawModel;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.skybox.Sky;
import com.example.toolbox.Maths;

public class WaterRenderer07 {
    
    private static final String DUDV_MAP = "waterDUDV";
    private static final String NORMAL_MAP = "normalMap";
    
    private static final float WAVE_SPEED = 0.03f;

    private RawModel quad;
    private WaterShader07 shader;
    private WaterFrameBuffers fbos;
    
    // tiling has to be huge since the water tiles are huge
    private float tiling = 600f; // was 6 in OpenGL Water Tutorial 5: DuDv Maps
    
    private float moveFactor = 0f;
    private float waveStrength = 0.02f; // const 0.02 in water tutorial 5
    
    private int dudvTexture;
    private int normalMap;
    
    public WaterRenderer07(Loader loader, WaterShader07 shader, Matrix4f projectionMatrix,
            WaterFrameBuffers fbos) {
        this.shader = shader;
        this.fbos = fbos;
        dudvTexture = loader.loadTexture(DUDV_MAP);
        normalMap = loader.loadTexture(NORMAL_MAP);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
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
        shader.loadTiling(tiling);
        moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);
        shader.loadWaveStrength(waveStrength); // set waveStrength to 0 to remove the dudvMap distortion
        shader.loadLights(lights);

        shader.loadSkyColor(sky.getColor());
        shader.loadSkyVariables(sky.getDensity(), sky.getGradient());
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
