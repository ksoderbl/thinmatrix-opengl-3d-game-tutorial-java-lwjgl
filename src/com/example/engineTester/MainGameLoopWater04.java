package com.example.engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.example.entities.Camera;
import com.example.entities.Camera18;
import com.example.entities.CameraWater04;
import com.example.entities.Entity;
import com.example.entities.Light;
import com.example.entities.PlayerWater04;
import com.example.fontMeshCreator.FontType;
import com.example.fontMeshCreator.GUIText;
import com.example.fontRendering.TextMaster;
import com.example.models.TexturedModel;
import com.example.renderEngine.Display;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.MasterRenderer23;
import com.example.skybox.Sky;
import com.example.terrains.Terrain;
import com.example.terrains.World;
import com.example.terrains.WorldWater04;
import com.example.water.WaterFrameBuffers;
import com.example.water.WaterRenderer04;
import com.example.water.WaterShader04;
import com.example.water.WaterTile;
import com.example.water.WaterTile04;

public class MainGameLoopWater04
{
    public static String title = "OpenGL Water Tutorial 4";
    public static String subTitle = "Projective Texture Mapping";
    public static String subSubTitle = "Use keys w, a, s, d to move player, use mouse to control camera";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();

        TextMaster.init(loader);
        if (title.length() > 0) {
            FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text = new GUIText(title, 1.3f, font, new Vector2f(0.0f, 0.85f), 0.3f, true);
            text.setColor(0.1f, 0.1f, 0.4f);
        }
        if (subTitle.length() > 0) {
            FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text2 = new GUIText(subTitle, 1f, font2, new Vector2f(0.0f, 0.9f), 0.3f, true);
            text2.setColor(0.4f, 0.1f, 0.1f);
        }
        if (subSubTitle.length() > 0) {
            FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text3 = new GUIText(subSubTitle, 0.7f, font3, new Vector2f(0.0f, 0.95f), 0.3f, true);
            text3.setColor(0.1f, 0.4f, 0.1f);
        }
        
        float terrainSize = 20000;
        float terrainMaxHeight = 7000;
        float waterHeight = 0;
        World world = new WorldWater04(loader, terrainSize, terrainMaxHeight, waterHeight);
        List<Terrain> terrains = world.getTerrains();

        // *****************************************

        TexturedModel treeModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel lowPolyTreeModel = loader.createTexturedModel("lowPolyTree", "lowPolyTree4", 2, 1, 0, false, false);
        TexturedModel pineModel = loader.createTexturedModel("pine", "pine", 10, 0.5f);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0, true, true);
        TexturedModel flowerModel = loader.createTexturedModel("grassModel", "flower", 1, 0, true, true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern4", 2, 1, 0, true, false);
        TexturedModel rocksModel = loader.createTexturedModel("rocks", "rocks", 10, 1);
        TexturedModel boxModel = loader.createTexturedModel("box", "box", 10, 1);
        TexturedModel stallModel = loader.createTexturedModel("stall", "stallTexture", 15, 1);
        TexturedModel barrelModel = loader.createTexturedModel("barrel", "barrel", 20, 0.5f);
        TexturedModel exampleModel = loader.createTexturedModel("example", "white", 1, 0);
        TexturedModel lampModel = loader.createTexturedModel("lamp", "lamp", 1, 0);
        
        List<Entity> entities = new ArrayList<>();

        float ex, ey, ez;
 
        entities.add(new Entity(rocksModel, new Vector3f(0, 0, 0), 0, 0, 0, 75));

        ex = 100;
        ez = 300;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        entities.add(new Entity(boxModel, new Vector3f(ex, ey, ez), 0, 0, 0, 10));

        ex = -50;
        ez = 250;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(stallModel, new Vector3f(ex, ey, ez), 0, -50, 0, 2f));

        ex = -40;
        ez = 240;
        ey = world.getHeightOfTerrain(ex, ez) + 3;
        entities.add(new Entity(barrelModel, new Vector3f(ex, ey, ez), 0, 0, 0, 0.5f));

        ex = -30;
        ez = 230;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(exampleModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f));

        ex = -30;
        ez = 220;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(lampModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f));

        ex = 225;
        ez = 352;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        Entity boxEntity = new Entity(boxModel, new Vector3f(ex, ey, ez), 0, 25f, 0, 5f);
        entities.add(boxEntity);
        
        Random random = new Random(676452);

        for (int i = 0; i < 1000; i++) {
            
            float x = 0, y = 0, z = 0, rx = 0, ry = 0, rz = 0, scale = 1;
            int textureIndex = 0;
            
            if (i % 7 == 0) {
                // grass
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 0;
                    ry = random.nextFloat() * 360;
                    rz = 0;
                    scale = 1.8f;
                    entities.add(new Entity(grassModel, new Vector3f(x, y, z), rx, ry, rz, scale));
                }

                // flower
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 0;
                    ry = random.nextFloat() * 360;
                    rz = 0;
                    scale = 2.3f;
                    entities.add(new Entity(flowerModel, new Vector3f(x, y, z), rx, ry, rz, scale));
                }
            }

            if (i % 3 == 0) {
                // fern
                textureIndex = random.nextInt(4);
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 10 * random.nextFloat() - 5;
                    ry = random.nextFloat() * 360;
                    rz = 10 * random.nextFloat() - 5;
                    scale = 0.9f;
                    entities.add(new Entity(fernModel, textureIndex, new Vector3f(x, y, z), rx, ry, rz, scale));
                }
    
                // low poly tree "bobble"
                textureIndex = random.nextInt(4);
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 4 * random.nextFloat() - 2;
                    ry = random.nextFloat() * 360;
                    rz = 4 * random.nextFloat() - 2;
                    scale = random.nextFloat() * 0.1f + 0.6f;
                    entities.add(new Entity(lowPolyTreeModel, textureIndex, new Vector3f(x, y, z), rx, ry, rz, scale));
                }
    
                // tree
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 4 * random.nextFloat() - 2;
                    ry = random.nextFloat() * 360;
                    rz = 4 * random.nextFloat() - 2;
                    scale = random.nextFloat() * 1f + 4f;
                    entities.add(new Entity(treeModel, new Vector3f(x, y, z), rx, ry, rz, scale));
                }

                // pine
                x = random.nextFloat() * terrainSize - terrainSize / 2;
                z = random.nextFloat() * terrainSize - terrainSize / 2;
                y = world.getHeightOfTerrain(x, z);
                if (y > world.getHeightOfWater(x, z)) {
                    rx = 4 * random.nextFloat() - 2;
                    ry = random.nextFloat() * 360;
                    rz = 4 * random.nextFloat() - 2;
                    scale = random.nextFloat() * 4f + 1f;
                    entities.add(new Entity(pineModel, new Vector3f(x, y, z), rx, ry, rz, scale));
                }
            }
        }

        float px = 500f; //-2163f;
        float pz = 500f; //2972f;
        float py = world.getHeightOfTerrain(px, pz);
        
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        PlayerWater04 player = new PlayerWater04(playerModel, new Vector3f(px, py, pz), 0, 180, 0, 0.6f);
        entities.add(player);
        
        Camera camera1 = new CameraWater04(player);
        camera1.getPosition().set(0, 20, 0);

        Camera camera2 = new Camera18();
        camera2.getPosition().set(0, 30, 0);
        
        Camera camera = camera1;

        Light light = new Light(
                new Vector3f(10000, 10000, -10000),
                new Vector3f(1f, 1f, 1f)); // white light
        
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        
        MasterRenderer23 renderer = new MasterRenderer23();
        
        int i = 0;
        
        // Water
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        WaterShader04 waterShader = new WaterShader04();
        WaterRenderer04 waterRenderer = new WaterRenderer04(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile04(0, 0, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(-1 * terrainSize, 0, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(-1 * terrainSize, -1 * terrainSize, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(0, -1 * terrainSize, waterHeight, terrainSize);
        waters.add(water);
        


//        List<GuiTexture> guiTextures = new ArrayList<>();
//        GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
//        GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
//        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.6f, -0.6f), new Vector2f(0.4f, 0.4f));
//        guiTextures.add(refrGui);
//        guiTextures.add(reflGui);
//        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        Sky sky = new Sky(0.5f, 0.7f, 0.9f, 0.00015f, 7f);

        //****************Game Loop Below*********************
        
        while (!Display.isCloseRequested()) {
            
            player.move(world);
            
            camera.move();
            
            //camera2.getPosition().set(0, 0, -0.02f);
            
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
            //guiRenderer.render(guiTextures);

            TextMaster.render();
            
            DisplayManager.updateDisplay();
            
            if ((i % 60) == 0) {
                camera.printPosition();
            }
            i++;
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
