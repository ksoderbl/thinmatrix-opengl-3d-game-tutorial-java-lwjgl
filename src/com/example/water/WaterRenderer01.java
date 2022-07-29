package com.example.water;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.models.RawModel;
import com.example.renderEngine.Loader;
import com.example.skybox.Sky;
import com.example.toolbox.Maths;

public class WaterRenderer01 {

    private RawModel quad;
    private WaterShader01 shader;

    public WaterRenderer01(Loader loader, WaterShader01 shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
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

        //float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVAO(vertices, 2);
    }

}
