package com.example.engineTester;

// import java.io.File;
import java.util.Random;

// import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.entities.Camera08;
import com.example.entities.Entity;
// import com.example.fontMeshCreator.FontType;
// import com.example.fontMeshCreator.GUIText;
// import com.example.fontRendering.TextMaster;
import com.example.models.RawModel;
import com.example.models.TexturedModel;
import com.example.objConverter.OBJFileLoader;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.Renderer08;
import com.example.shaders.StaticShader08;
import com.example.textures.ModelTexture;

// Tutorial 9:
// https://www.youtube.com/watch?v=KMWUjNE0fYI&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP
// Tutorial 10:
// https://www.youtube.com/watch?v=YKFYtekgnP8&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP

public class MainGameLoop10
{
    public static String title = "OpenGL 3D Game Tutorial 10";
    public static String subTitle = "Loading 3D Models";
    public static String subSubTitle = "Press, w, a, s or d to move";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        StaticShader08 shader = new StaticShader08();
        Renderer08 renderer = new Renderer08(shader);

        RawModel model = OBJFileLoader.loadOBJ("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
        TexturedModel staticModel = new TexturedModel(model, texture);
        
        // TextMaster.init(loader);
        
        // FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        // GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        // text.setColor(0.2f, 0.2f, 0.8f);
        
        // FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        // GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        // text2.setColor(0.8f, 0.2f, 0.2f);
        
        // FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        // GUIText text3 = new GUIText(subSubTitle, 1.5f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
        // text3.setColor(0.8f, 0.8f, 0.2f);
        
        
        // create some random entities 
        Entity[] entities = new Entity[ 1000 ];
        Random random = new Random();
        
        for (int i = 0; i < entities.length; i++) {
            Vector3f translation = new Vector3f(
                    ((20 * random.nextFloat()) - 10) * 0.8f,
                    ((20 * random.nextFloat()) - 10) * 0.8f,
                    -25f + ((20 * random.nextFloat()) - 10) * 0.8f);
            float rx = 0;
            float ry = 0; 
            float rz = 360 * random.nextFloat();
            float tmp = random.nextFloat();
            float scale = tmp * tmp * 0.1f;

            entities[i] = new Entity(staticModel, translation, rx, ry, rz, scale);
        }
        
        Camera08 camera = new Camera08();
        
        while (!Display.isCloseRequested()) {
            
            for (int i = 0; i < entities.length; i++) {
                //entities[i].increasePosition(0, 0, 0.00001f*i);
                entities[i].increaseRotation(0.002f*i, 0.003f*i, 0.001f*i);
            }

            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            for (int i = 0; i < entities.length; i++) {
                renderer.render(entities[i], shader);
            }
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
