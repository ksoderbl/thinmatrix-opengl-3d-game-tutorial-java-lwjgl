package com.example.engineTester;

import java.io.File;

import org.lwjgl.opengl.GL11;
import org.joml.Vector2f;

import com.example.fontMeshCreator.FontType;
import com.example.fontMeshCreator.GUIText;
import com.example.fontRendering.TextMaster;
import com.example.models.RawModel;
import com.example.renderEngine.Display;
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
            0, 1, 3,    // Top left triangle
            3, 1, 2     // Bottom right triangle
        };
        
        RawModel model = loader.loadToVAO(vertices, indices);
        
        TextMaster.init(loader);
        
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        text.setColor(0.2f, 0.2f, 0.8f);
        
        FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        text2.setColor(0.8f, 0.2f, 0.2f);

        while (!Display.isCloseRequested()) {
            
            // disable depth test because TextMaster turns it on
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            renderer.prepare();
            shader.start();
            renderer.render(model);
            shader.stop();
            
            TextMaster.render();
                        
            DisplayManager.updateDisplay();
        }

        TextMaster.cleanUp();
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
