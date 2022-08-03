package com.example.engineTester;

import java.io.File;
// import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Matrix4f;

import com.example.entities.Camera08;
import com.example.entities.Entity;
import com.example.fontMeshCreator.FontType;
import com.example.fontMeshCreator.GUIText;
import com.example.fontRendering.TextMaster;
import com.example.models.RawModel;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.Renderer08;
import com.example.shaders.StaticShader08;
import com.example.textures.ModelTexture;
import com.example.toolbox.Maths;

public class MainGameLoop08
{
    public static String title = "OpenGL 3D Game Tutorial 8";
    public static String subTitle = "Model, View & Projection Matrices";
    public static String subSubTitle = "Press w, a, s or d to move";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        StaticShader08 shader = new StaticShader08();
        Renderer08 renderer = new Renderer08(shader);

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
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        TextMaster.init(loader);
        
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        text.setColor(0.2f, 0.2f, 0.8f);
        
        FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        text2.setColor(0.8f, 0.2f, 0.2f);
        
        FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text3 = new GUIText(subSubTitle, 1.5f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
        text3.setColor(0.8f, 0.8f, 0.2f);
        
        
        // // create some random entities 
        // Entity[] entities = new Entity[ 1000 ];
        // Random random = new Random();
        
        // for (int i = 0; i < entities.length; i++) {
        //     Vector3f translation = new Vector3f(
        //             ((2 * random.nextFloat()) - 1) * 0.8f,
        //             (-0.5f + (random.nextFloat())) * 0.8f,
        //             -5f);
        //     float rx = 0;
        //     float ry = 0; 
        //     float rz = 360 * random.nextFloat();
        //     float tmp = random.nextFloat();
        //     float scale = tmp * tmp * 0.1f;

        //     entities[i] = new Entity(staticModel, translation, rx, ry, rz, scale);
        // }
        
        Vector3f translation = new Vector3f(-1, 0, 0);
        float rx = 0.0f;
        float ry = 0.0f;
        float rz = 0.0f;
        float scale = 1.0f;
        Entity entity = new Entity(staticModel, translation, rx, ry, rz, scale);
        
        // Camera08 camera = new Camera08();
        Matrix4f m = Maths.createTransformationMatrix(translation, 45.0f, 45.0f, 45.0f, 1.0f);
        
        while (!Display.isCloseRequested()) {
            entity.increasePosition(0.002f, 0, 0);
            entity.increaseRotation(0, 1, 0);


            // disable depth test because TextMaster turns it on
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            // disable depth test because TextMaster turns it on
            // GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            // for (int i = 0; i < entities.length; i++) {
            //     //entities[i].increasePosition(0, 0, 0.00001f*i);
            //     entities[i].increaseRotation(0.002f*i, 0.003f*i, 0.001f*i);
            // }

            // camera.move();
            renderer.prepare();
            shader.start();
            shader.loadTransformationMatrix(m);
            // shader.loadViewMatrix(camera);
            // for (int i = 0; i < entities.length; i++) {
            //     renderer.render(entities[i], shader);
            // }
            renderer.render(entity, shader);
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
