package com.example.engineTester;

// import java.io.File;

// import org.lwjgl.opengl.GL11;
// import org.joml.Vector2f;

import com.example.models.RawModel;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.Renderer05;
import com.example.shaders.StaticShader05;

public class MainGameLoop05
{
	public static String title = "OpenGL 3D Game Tutorial 5";
	public static String subTitle = "Coloring using Shaders";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Renderer05 renderer = new Renderer05();
        StaticShader05 shader = new StaticShader05();

        float[] vertices = {
        	-0.5f, 0.5f, 0f,
        	-0.5f, -0.5f, 0f,
        	0.5f, -0.5f, 0f,
        	0.5f, 0.5f, 0f,
        };
        
        int[] indices = {
        	0, 1, 3,	// Top left triangle
        	3, 1, 2		// Bottom right triangle
        };
        
        RawModel model = loader.loadToVAO(vertices, indices);
        
        while (!DisplayManager.isCloseRequested()) {
        	renderer.prepare();
        	shader.start();
            renderer.render(model);
            shader.stop();
            
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
