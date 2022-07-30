package com.example.engineTester;

// import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.example.entities.Camera;
import com.example.entities.Camera18;
import com.example.entities.Camera19;
import com.example.entities.Entity;
import com.example.entities.Light;
import com.example.entities.Player19;
// import com.example.fontMeshCreator.FontType;
// import com.example.fontMeshCreator.GUIText;
// import com.example.fontRendering.TextMaster;
import com.example.guis.GuiRenderer;
import com.example.guis.GuiTexture;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.MasterRenderer21;
import com.example.skybox.Sky;
import com.example.terrains.Terrain;
import com.example.terrains.Terrain21;
import com.example.textures.TerrainTexture;
import com.example.textures.TerrainTexturePack;
import com.example.water.WaterFrameBuffers;
import com.example.water.WaterRenderer01;
import com.example.water.WaterShader01;
import com.example.water.WaterTile;
import com.example.water.WaterTile01;

public class MainGameLoop21
{
    public static String title = "OpenGL 3D Game Tutorial 21: Terrain Height Maps";
    public static String subTitle = "Use keys w, a, s, d to move player, use mouse to control camera";
    public static String subSubTitle = "Use key c to swap to second camera, move it with arrow keys";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();

        // TextMaster.init(loader);
        // if (title.length() > 0) {
        //     FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        //     GUIText text = new GUIText(title, 2.0f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        //     text.setColor(0.2f, 0.2f, 0.8f);
        // }
        // if (subTitle.length() > 0) {
        //     FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        //     GUIText text2 = new GUIText(subTitle, 1.5f, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        //     text2.setColor(0.8f, 0.2f, 0.2f);
        // }
        // if (subSubTitle.length() > 0) {
        //     FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        //     GUIText text3 = new GUIText(subSubTitle, 1.25f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
        //     text3.setColor(0.2f, 0.8f, 0.2f);
        // }
        
        // *********TERRAIN TEXTURE STUFF**********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        //TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("rockDiffuse"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
                rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        // *****************************************

        TexturedModel treeModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel lowPolyTreeModel = loader.createTexturedModel("lowPolyTree", "lowPolyTree", 1, 0);
        TexturedModel pineModel = loader.createTexturedModel("pine", "pine", 10, 0.5f);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0, true, true);
        TexturedModel flowerModel = loader.createTexturedModel("grassModel", "flower", 1, 0, true, true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern1", 1, 0, true, false);
        TexturedModel rocksModel = loader.createTexturedModel("rocks", "rocks", 10, 1);
        TexturedModel boxModel = loader.createTexturedModel("box", "box", 10, 1);
        TexturedModel stallModel = loader.createTexturedModel("stall", "stallTexture", 15, 1);
        TexturedModel barrelModel = loader.createTexturedModel("barrel", "barrel", 20, 0.5f);
        TexturedModel exampleModel = loader.createTexturedModel("example", "white", 1, 0);
        TexturedModel lampModel = loader.createTexturedModel("lamp", "lamp", 1, 0);
        
        List<Entity> entities = new ArrayList<>();
        
        entities.add(new Entity(rocksModel, new Vector3f(0, 0, 0), 0, 0, 0, 75));
        entities.add(new Entity(boxModel, new Vector3f(100, 10, -300), 0, 0, 0, 10));
        entities.add(new Entity(stallModel, new Vector3f(-50, 0, -250), 0, -50, 0, 2f));
        entities.add(new Entity(barrelModel, new Vector3f(-40, 3, -240), 0, 0, 0, 0.5f));
        entities.add(new Entity(exampleModel, new Vector3f(-30, 0, -230), 0, 0, 0, 1f));
        entities.add(new Entity(lampModel, new Vector3f(-30, 0, -220), 0, 0, 0, 1f));
        
        Random random = new Random(676452);

        for (int i = 0; i < 400; i++) {
            
            float x = 0, y = 0, z = 0, rx = 0, ry = 0, rz = 0, scale = 1;
            
            if (i % 7 == 0) {
                // grass
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                rx = 0;
                ry = random.nextFloat() * 360;
                rz = 0;
                scale = 1.8f;
                entities.add(new Entity(grassModel, new Vector3f(x, y, z), rx, ry, rz, scale));

                // flower
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                rx = 0;
                ry = random.nextFloat() * 360;
                rz = 0;
                scale = 2.3f;
                entities.add(new Entity(flowerModel, new Vector3f(x, y, z), rx, ry, rz, scale));
            }

            if (i % 3 == 0) {
                // fern
                x = random.nextFloat() * 400 - 400;
                z = random.nextFloat() * -400;
                rx = 10 * random.nextFloat() - 5;
                ry = random.nextFloat() * 360;
                rz = 10 * random.nextFloat() - 5;
                scale = 0.9f;
                entities.add(new Entity(fernModel, new Vector3f(x, y, z), rx, ry, rz, scale));
    
                // low poly tree "bobble"
                x = random.nextFloat() * 800 - 400;
                y = 0; 
                z = random.nextFloat() * -600; 
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 0.1f + 0.6f;
                entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), rx, ry, rz, scale));
    
                // tree
                x = random.nextFloat() * 800 - 400;
                y = 0; 
                z = random.nextFloat() * -600; 
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 1f + 4f;
                entities.add(new Entity(treeModel, new Vector3f(x, y, z), rx, ry, rz, scale));

                // pine
                x = random.nextFloat() * 800;
                y = 0; 
                z = random.nextFloat() * -600; 
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 4f + 1f;
                entities.add(new Entity(pineModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            }
        }
        
        
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        Player19 player = new Player19(playerModel, new Vector3f(0, 0, -50), 0, 180, 0, 0.6f);
        entities.add(player);
        
        Camera19 camera1 = new Camera19(player);
        camera1.getPosition().set(0, 20, 0);

        Camera18 camera2 = new Camera18();
        camera2.getPosition().set(0, 30, 0);
        
        Camera camera = camera1;

        Light light = new Light(
                new Vector3f(20000, 40000, 20000),
                new Vector3f(1f, 1f, 1f)); // white light
        
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        
        //ModelTexture terrainModelTexture = new ModelTexture(loader.loadTexture("grass"));
        //Terrain terrain = new Terrain17(0, -1, loader, texturePack, blendMap);
        //Terrain terrain2 = new Terrain17(-1, -1, loader, texturePack, blendMap);
        
        List<Terrain> terrains = new ArrayList<Terrain>();
        Terrain terrain = new Terrain21(0, -1, loader, texturePack, blendMap, "heightmap");
        terrains.add(terrain);

        MasterRenderer21 renderer = new MasterRenderer21();
        
        int i = 0;
        // int cameraFrames = 0;
        
        // Water
        
        WaterShader01 waterShader = new WaterShader01();
        WaterRenderer01 waterRenderer = new WaterRenderer01(loader, waterShader, renderer.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile01(0, -150, 1);
        waters.add(water);
        
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        List<GuiTexture> guiTextures = new ArrayList<>();
        GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.6f, -0.6f), new Vector2f(0.4f, 0.4f));
        guiTextures.add(refrGui);
        guiTextures.add(reflGui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        Sky sky = new Sky(0.2f, 0.6f, 1f, 0.00015f, 7f);

        //****************Game Loop Below*********************
        
        while (!Display.isCloseRequested()) {
            player.move();
            
            // cameraFrames++;
            // // key C used to swap camera
            // if (cameraFrames > 10 && Keyboard.isKeyDown(Keyboard.KEY_C)) {
            //     if (camera == camera1) {
            //         camera = camera2;
            //     }
            //     else if (camera == camera2) {
            //         camera = camera1;
            //     }
            //     cameraFrames = 0;
            // }
            
            camera.move();
            
            //camera2.getPosition().translate(0, 0, -0.02f);
            
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // render to reflection texture: set the clip plane to clip stuff above water
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            // change position and pitch of camera to render the reflection 
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, 1, 0, -water.getHeight()));
            camera.getPosition().y += distance;
            camera.invertPitch();

            // render to refraction texture: set the clip plane to clip stuff below water
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, water.getHeight()));
            
            // render to screen: set the clip plane at a great height, so it won't clip anything
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, 1000000));

            waterRenderer.render(waters, sky, camera);
            guiRenderer.render(guiTextures);

            // TextMaster.render();
            
            DisplayManager.updateDisplay();
            
            if ((i % 60) == 0) {
                camera.printPosition();
            }
            i++;
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        // TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
