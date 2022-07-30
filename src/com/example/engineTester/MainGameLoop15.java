package com.example.engineTester;

// import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.entities.Camera13;
import com.example.entities.Entity;
import com.example.entities.Light;
// import com.example.fontMeshCreator.FontType;
// import com.example.fontMeshCreator.GUIText;
// import com.example.fontRendering.TextMaster;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.MasterRenderer15;
import com.example.terrains.Terrain14;
import com.example.textures.ModelTexture;

public class MainGameLoop15
{
    public static String title = "OpenGL 3D Game Tutorial 15";
    public static String subTitle = "Transparency";
    public static String subSubTitle = "Press w, a, s or d to move";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Camera13 camera = new Camera13();
        camera.getPosition().set(0, 4, 0);

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

        TexturedModel staticModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0);
        grassModel.getTexture().setHasTransparency(true);
        grassModel.getTexture().setUseFakeLighting(true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern1", 1, 0);
        fernModel.getTexture().setHasTransparency(true);
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 500; i++) {
            float x = random.nextFloat() * 800 - 400;
            float y = 0; 
            float z = random.nextFloat() * -600; 
            float rx = 4 * random.nextFloat() - 2;
            float ry = random.nextFloat() * 360;
            float rz = 4 * random.nextFloat() - 2;
            float scale = 3f;
            
            entities.add(new Entity(staticModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            rx = 0;
            ry = random.nextFloat() * 360;
            rz = 0;
            scale = 1f;

            entities.add(new Entity(grassModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            x = random.nextFloat() * 800 - 400;
            z = random.nextFloat() * -600;
            rx = 10 * random.nextFloat() - 5;
            ry = random.nextFloat() * 360;
            rz = 10 * random.nextFloat() - 5;
            scale = 0.6f;

            entities.add(new Entity(fernModel, new Vector3f(x, y, z), rx, ry, rz, scale));
        }
        
        Light light = new Light(
                new Vector3f(20000, 20000, 3000),
                new Vector3f(1f, 1f, 1f)); // white light
        
        ModelTexture terrainModelTexture = new ModelTexture(loader.loadTexture("grass"));
        Terrain14 terrain = new Terrain14(0, -1, loader, terrainModelTexture);
        Terrain14 terrain2 = new Terrain14(-1, -1, loader, terrainModelTexture);

        MasterRenderer15 renderer = new MasterRenderer15();
        
        int i = 0;
        
        while (!Display.isCloseRequested()) {
            //int i = 0;
            //for (Entity entity : entities) {
            //    //entities[i].increasePosition(0, 0, 0.00001f*i);
            //    entity.increaseRotation(0f, 0.4f*i, 0f);
            //    i++;
            //}
            camera.move();
            
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            
            renderer.render(light, camera);
            // TextMaster.render();
            DisplayManager.updateDisplay();
            
            if ((i % 60) == 0) {
                camera.printPosition();
            }
            i++;
        }

        // TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
