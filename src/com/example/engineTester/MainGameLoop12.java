package com.example.engineTester;

// import java.io.File;
import java.util.Random;

// import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.entities.Camera12;
import com.example.entities.Entity;
import com.example.entities.Light;
// import com.example.fontMeshCreator.FontType;
// import com.example.fontMeshCreator.GUIText;
// import com.example.fontRendering.TextMaster;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.Renderer12;
import com.example.shaders.StaticShader12;

public class MainGameLoop12
{
    public static String title = "OpenGL 3D Game Tutorial 12";
    public static String subTitle = "Specular Lighting";
    public static String subSubTitle = "Press, w, a, s or d to move";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        StaticShader12 shader = new StaticShader12();
        Renderer12 renderer = new Renderer12(shader);

        TexturedModel staticModel = loader.createTexturedModel("dragon", "white", 2, 1);
        
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
        Entity[] entities = new Entity[ 20 ];
        Random random = new Random();
        
        for (int i = 0; i < entities.length; i++) {
            Vector3f translation = new Vector3f(
                    ((10 * random.nextFloat()) - 5) * 0.8f,
                    ((10 * random.nextFloat()) - 5) * 0.8f,
                    -10f + ((10 * random.nextFloat()) - 5) * 0.8f);
            float rx = 0;
            float ry = 0; 
            float rz = 0; //360 * random.nextFloat();
            float scale = 0.1f; //tmp * tmp * 0.1f;

            entities[i] = new Entity(staticModel, translation, rx, ry, rz, scale);
        }
        
        Light light = new Light(
                new Vector3f(0, 0, -10),
                new Vector3f(1f, 1f, 1f)); // white light
        
        Camera12 camera = new Camera12();
        
        while (!Display.isCloseRequested()) {
            
            for (int i = 0; i < entities.length; i++) {
                //entities[i].increasePosition(0, 0, 0.00001f*i);
                entities[i].increaseRotation(0, 0.1f*i, 0);
            }
            
            //light.getPosition().translate(
            //        ((10 * random.nextFloat()) - 5) / 10f,
            //        ((10 * random.nextFloat()) - 5) / 10f,
            //        ((10 * random.nextFloat()) - 5) / 10f);

            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            for (int i = 0; i < entities.length; i++) {
                //if (i == 0) {
                //    entities[i].setPosition(light.getPosition());
                //}
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
