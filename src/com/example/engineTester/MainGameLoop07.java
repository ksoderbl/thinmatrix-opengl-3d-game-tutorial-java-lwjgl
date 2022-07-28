package com.example.engineTester;

// import java.io.File;
// import java.util.Random;

// import org.lwjgl.opengl.GL11;
import org.joml.Matrix4f;
// import org.joml.Vector2f;
import org.joml.Vector3f;

// import com.example.fontMeshCreator.FontType;
// import com.example.fontMeshCreator.GUIText;
// import com.example.fontRendering.TextMaster;
import com.example.models.RawModel;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.Renderer07;
import com.example.shaders.StaticShader07;
import com.example.textures.ModelTexture;
import com.example.toolbox.Maths;

public class MainGameLoop07
{
    public static String title = "OpenGL 3D Game Tutorial 7";
    public static String subTitle = "Matrices & Uniform Variables";

    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Renderer07 renderer = new Renderer07();
        StaticShader07 shader = new StaticShader07();

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
        
        float[] textureCoords = {
            0, 0,    // V0
            0, 1,    // V1
            1, 1,    // V2
            1, 0     // V3
        };
        
        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        
        // TextMaster.init(loader);
        
        // FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        // GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        // text.setColor(0.2f, 0.2f, 0.8f);
        
        // FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        // GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        // text2.setColor(0.8f, 0.2f, 0.2f);
        
        Vector3f translation = new Vector3f(0.0f, 0.0f, 0.0f);

        Matrix4f m = Maths.createTransformationMatrix(translation, 45.0f, 45.0f, 45.0f, 1.0f);

        while (!Display.isCloseRequested()) {
            
            // disable depth test because TextMaster turns it on
            // GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            renderer.prepare();
            shader.start();
            shader.loadTransformationMatrix(m);
            renderer.render(texturedModel);
            shader.stop();
            
            // TextMaster.render();
                        
            DisplayManager.updateDisplay();
        }

        // TextMaster.cleanUp();
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
