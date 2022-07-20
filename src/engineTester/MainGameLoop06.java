package engineTester;

import java.io.File;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer06;
import shaders.StaticShader06;
import textures.ModelTexture;

// https://www.youtube.com/watch?v=SPt-aogu72A&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=6

public class MainGameLoop06
{
    public static String title = "OpenGL 3D Game Tutorial 6";
    public static String subTitle = "Texturing";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Renderer06 renderer = new Renderer06();
        StaticShader06 shader = new StaticShader06();

        float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
        };
        
        int[] indices = {
            0, 1, 3,    // Top left triangle
            3, 1, 2        // Bottom right triangle
        };
        
        float[] textureCoords = {
            0, 0,    // V0
            0, 1,    // V1
            1, 1,    // V2
            1, 0    // V3
        };
        
        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        
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
            renderer.render(texturedModel);
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
