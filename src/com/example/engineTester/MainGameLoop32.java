package com.example.engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.example.entities.Camera;
import com.example.entities.Camera32;
import com.example.entities.Entity;
import com.example.entities.Light;
import com.example.entities.Player32;
import com.example.fontMeshCreator.FontType;
import com.example.fontMeshCreator.GUIText;
import com.example.fontRendering.TextMaster;
import com.example.guis.GuiRenderer;
import com.example.guis.GuiTexture;
import com.example.models.TexturedModel;
import com.example.renderEngine.DisplayManager;
import com.example.renderEngine.Loader;
import com.example.renderEngine.MasterRenderer32;
import com.example.skybox.Sky;
import com.example.terrains.Terrain;
import com.example.terrains.World;
import com.example.terrains.World32;
import toolbox.MousePicker;
import com.example.water.WaterFrameBuffers;
import com.example.water.WaterRenderer32;
import com.example.water.WaterShader30;

// OpenGL 3D Game Tutorial 32: Font Rendering
// https://www.youtube.com/watch?v=mnIQEQoHHCU&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=32

// Hiero: A bitmap font packing tool
// https://libgdx.badlogicgames.com/tools.html
// https://github.com/libgdx/libgdx/wiki/Hiero
// http://www.angelcode.com/products/bmfont/
// https://github.com/libgdx/libgdx/wiki/Bitmap-fonts

public class MainGameLoop32
{

    String tutorial = "OpenGL 3D Game Tutorial 32: Font Rendering";
    String subSubTitle = "Use keys w, a, s, d to move player, use mouse to control camera";
     //"Use key c to swap to second camera, move it with arrow keys";

    List<Entity> entities = new ArrayList<>();
    List<Entity> normalMapEntities = new ArrayList<>();
    Random random = new Random(67645);
    Loader loader = new Loader();

    
    public void addEntity(World world, TexturedModel texturedModel, float rx, float rz, float scale) {
        int numTextureRows = texturedModel.getTexture().getNumberOfRows();
        int numSubTextures = numTextureRows * numTextureRows;
        
        Vector3f position = world.getTerrainPoint(random.nextFloat() * world.getXSize(), random.nextFloat() * world.getZSize(), 0);
        
        if (position.y > world.getHeightOfWater(position.x, position.z) + 4) {
            float ry = random.nextFloat() * 360;
            
            if (numSubTextures > 1) {
                int textureIndex = random.nextInt(numSubTextures);
                entities.add(new Entity(texturedModel, textureIndex, position, rx, ry, rz, scale));
            }
            else {
                entities.add(new Entity(texturedModel, position, rx, ry, rz, scale));
            }
        }
    }
    
    public MainGameLoop32() {
        boolean vsync = true;
        
        float terrainSize = 1600;
        
        float terrainMaxHeight = 100;
        float rocksYOffset = terrainMaxHeight * 0.4075f;
        float waterHeight = terrainMaxHeight * 0.03f;
        
        float playerX = 1368.5f; //terrainSize * 0.3f;
        float playerZ = 419.1f; //terrainSize * 0.3f;
        float playerDir = 270;
        
        // for/haze
        float airDensity = 0.002f;
        float airGradient = 2.5f;

        String title = tutorial.split(":")[0].trim();
        String subTitle = tutorial.split(":")[1].trim();
        
        DisplayManager.createDisplay(tutorial);
        MasterRenderer32 renderer = new MasterRenderer32(loader);
        DisplayManager.setVSync(vsync);

        TextMaster.init(loader);

        FontType font = new FontType(loader.loadFontTextureAtlas("arial"), new File("res/fonts/arial.fnt"));
          FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
          FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
          
        GUIText text, text2, text3;

        text = new GUIText(title, 2f, font, new Vector2f(0.0f, 0.7f), 0.5f, true);
        text.setColor(0.2f, 0.2f, 0.5f);
           text2 = new GUIText(subTitle, 1.5f, font2, new Vector2f(0.0f, 0.8f), 0.5f, true);
           text2.setColor(0.5f, 0.2f, 0.2f);
        text3 = new GUIText(subSubTitle, 1f, font3, new Vector2f(0.0f, 0.9f), 0.5f, true);
        text3.setColor(0.2f, 0.5f, 0.2f);
        
        World world = new World32(loader, terrainSize, terrainMaxHeight, terrainSize * 0.8f, waterHeight);
        List<Terrain> terrains = world.getTerrains();

        // *****************************************

        TexturedModel treeModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel lowPolyTreeModel = loader.createTexturedModel("lowPolyTree", "lowPolyTree4", 2, 1, 0, false, false);
        TexturedModel pineModel = loader.createTexturedModel("pine", "pine", 10, 0.5f);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0, true, true);
        TexturedModel flowerModel = loader.createTexturedModel("grassModel", "flower", 1, 0, true, true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern4", 2, 1, 0, true, false);
        TexturedModel rocksModel = loader.createTexturedModel("rocks", "rocks", 10, 1);
        TexturedModel toonRocksModel = loader.createTexturedModel("toonRocks", "toonRocks", 10, 1);
        TexturedModel boxModel = loader.createTexturedModel("box", "box", 10, 1);
        TexturedModel stallModel = loader.createTexturedModel("stall", "stallTexture", 15, 1);
        TexturedModel oldBarrelModel = loader.createTexturedModel("barrel", "barrel", 20, 0.5f);
        TexturedModel exampleModel = loader.createTexturedModel("example", "white", 1, 0);
        TexturedModel lampModel = loader.createTexturedModel("lamp", "lamp", 1, 0, false, true);

        //******************NORMAL MAP MODELS************************

        TexturedModel barrelModel = loader.createTexturedModel("barrel", "barrel", "barrelNormal", 10, 0.5f);
        TexturedModel crateModel = loader.createTexturedModel("crate", "crate", "crateNormal", 10, 0.5f);
        TexturedModel boulderModel = loader.createTexturedModel("boulder", "boulder", "boulderNormal", 10, 0.5f);
        TexturedModel footballModel = loader.createTexturedModel("foot", "foot", "footNormal", 10, 0.5f);

        //************ENTITIES*******************

        Vector3f barrelPosition = world.getTerrainPoint(75, 75, 5);
        Entity entity = new Entity(barrelModel, barrelPosition, 1f);
        normalMapEntities.add(entity);

        Vector3f boulderPosition = world.getTerrainPoint(15, 75, 5);
        Entity entity2 = new Entity(boulderModel, boulderPosition, 2f);
        normalMapEntities.add(entity2);

        Vector3f cratePosition = world.getTerrainPoint(55, 75, 5);
        Entity entity3 = new Entity(crateModel, cratePosition, 0.04f);
        normalMapEntities.add(entity3);

        Vector3f footballPosition = world.getTerrainPoint(155, 75, 5);
        Entity entity4 = new Entity(footballModel, footballPosition, 1f);
        normalMapEntities.add(entity4);
        
        // add some more boulders
        int count = 0;
        for (int i = 0; i < 300; i++) {
            Vector3f position = world.getTerrainPoint(random.nextFloat() * world.getXSize(), random.nextFloat() * world.getZSize(), random.nextFloat() * -3);
            
            if (position.y > world.getHeightOfWater(position.x, position.z) - 6) {
                count++;
                Entity boulder = new Entity(boulderModel, position,
                        random.nextFloat() * 360.0f, random.nextFloat() * 360.0f, random.nextFloat() * 360.0f, random.nextFloat() * 1.0f + 1f);
                normalMapEntities.add(boulder);
            }
        }
        System.out.println("boulders: " + count);
        // add some stones close to the shoreline
        count = 0;
        for (int i = 0; i < 50000; i++) {
            Vector3f position = world.getTerrainPoint(random.nextFloat() * world.getXSize(), random.nextFloat() * world.getZSize(), -1);
            float h = world.getHeightOfWater(position.x, position.z);
            if (position.y > h - 3 && position.y < h + 3) {
                count++;
                Entity boulder = new Entity(boulderModel, position,
                        160 + random.nextFloat() * 40.0f, random.nextFloat() * 360.0f, 10 + random.nextFloat() * 10.0f, random.nextFloat() * 0.5f + 0.2f);
                normalMapEntities.add(boulder);
            }
        }
        System.out.println("stones: " + count);
       
        // the position of this should be at the center of the terrain tiles
        Vector3f rocksPosition = world.getTerrainPoint(terrainSize/2, terrainSize/2, rocksYOffset);
        entities.add(new Entity(rocksModel, rocksPosition, (terrainSize-1)/2));

        Vector3f boxPosition = world.getTerrainPoint(100, 300, 5);
        entities.add(new Entity(boxModel, boxPosition, 10));

        Vector3f stallPosition = world.getTerrainPoint(50, 250, 0);
        entities.add(new Entity(stallModel, stallPosition, 0, 50, 0, 2f));

        Vector3f oldBarrelPosition = world.getTerrainPoint(40, 240, 3);
        entities.add(new Entity(oldBarrelModel, oldBarrelPosition, 0.5f));

        Vector3f examplePosition = world.getTerrainPoint(30, 230, 0);
        entities.add(new Entity(exampleModel, examplePosition, 1f));

        Vector3f lamp9Position = world.getTerrainPoint(30, 220, 0);
        entities.add(new Entity(lampModel, lamp9Position, 1f));

        Vector3f box2Position = world.getTerrainPoint(225, 352, 5);
        Entity boxEntity = new Entity(boxModel, box2Position, 0, 25f, 0, 5f);
        entities.add(boxEntity);
        
        Sky sky = new Sky(0.2f, .7f, .7f, airDensity, airGradient);       
        
        List<Light> lights = new ArrayList<Light>();

        // OpenGL 3D Game Tutorial 25: Multiple Lights
        Light sun = new Light(new Vector3f(3000, 3000, 3000), new Vector3f(1.3f, 1.3f, 1.3f)); 
        lights.add(sun);
       
        Vector3f lamp1Position = world.getTerrainPoint(126.3969f, 621.307f, 0);
        Vector3f light1Position = new Vector3f(lamp1Position.x, lamp1Position.y + 14, lamp1Position.z); 
        entities.add(new Entity(lampModel, lamp1Position, 1f));
        lights.add(new Light(light1Position, new Vector3f(3, 1, 1), new Vector3f(1, 0.01f, 0.002f)));

        Vector3f lamp2Position = world.getTerrainPoint(175.8717f, 287.5373f, 0);
        Vector3f light2Position = new Vector3f(lamp2Position.x, lamp2Position.y + 14, lamp2Position.z);
        entities.add(new Entity(lampModel, lamp2Position, 1f));
        lights.add(new Light(light2Position, new Vector3f(1, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

        Vector3f lamp3Position = world.getTerrainPoint(362.69772f, 316.70355f, 0);
        Vector3f light3Position = new Vector3f(lamp3Position.x, lamp3Position.y + 14, lamp3Position.z);
        Entity lamp3Entity = new Entity(lampModel, lamp3Position, 1f);
        entities.add(lamp3Entity);
        Light lamp3Light = new Light(light3Position, new Vector3f(12, 12, 8), new Vector3f(1, 0.01f, 0.002f));
        lights.add(lamp3Light);
        
        for (int i = 0; i < 200; i++) {
            if (i % 3 == 0) {
                addEntity(world, grassModel, 0, 0, random.nextFloat() * 0.8f + 1.0f);
                addEntity(world, flowerModel, 0, 0, random.nextFloat() * 0.8f + 1.5f);
            }

            if (i % 2 == 0) {
                addEntity(world, fernModel, 10 * random.nextFloat() - 5, 10 * random.nextFloat() - 5, random.nextFloat() * 0.5f + 0.4f);
                
                // low poly tree "bobble"
                addEntity(world, lowPolyTreeModel, 4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 0.1f + 0.6f);
    
                addEntity(world, treeModel,  4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 1f + 4f);
                addEntity(world, pineModel,  4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 4f + 1f);
                
                addEntity(world, toonRocksModel, 0, 0, 4 * random.nextFloat());
            }
        }

        Vector3f playerPosition = world.getTerrainPoint(playerX, playerZ, 0);
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        Player32 player = new Player32(playerModel, playerPosition, 0, playerDir, 0, 0.6f);
        entities.add(player);
        
        Camera camera = new Camera32(player);
        //camera.getPosition().translate(0, 20, 0);
       
        // Water
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        WaterShader30 waterShader = new WaterShader30();
        WaterRenderer32 waterRenderer = new WaterRenderer32(loader, waterShader, renderer.getProjectionMatrix(), buffers, 0);

        List<GuiTexture> guiTextures = new ArrayList<>();
        //GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.6f, -0.6f), new Vector2f(0.4f, 0.4f));
        //guiTextures.add(refrGui);
        //guiTextures.add(reflGui);

        //GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.7f, 0.5f), new Vector2f(0.125f, 0.125f));
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.7f, 0.8f), new Vector2f(0.2f, 0.2f));
        //GuiTexture gui3 = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.8f, 0.9f), new Vector2f(0.2f, 0.2f));

        //guiTextures.add(gui);
        guiTextures.add(gui2);
        //guiTextures.add(gui3);

        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), world);

        //****************Game Loop Below*********************
        
        float t = 0f;
        
        while (!Display.isCloseRequested()) {
            
            player.move(world);
                       
            camera.move();
            
            //entity.increaseRotation(0.1f, 0.2f, 0.3f);
            //entity2.increaseRotation(0.3f, 0.1f, 0.2f);
            //entity3.increaseRotation(0.2f, 0.3f, 0.1f);
            float dt = DisplayManager.getFrameTimeSeconds();
            t += dt;
            //System.out.println("time: " + t);
            
            entity4.increaseRotation(12f * dt , 20f * dt, 6f * dt);
            
            picker.update();
//            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//            if (terrainPoint != null) {
//                lamp3Entity.setPosition(terrainPoint);
//                lamp3Light.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 14, terrainPoint.z));
//            }
            
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // render to reflection texture: set the clip plane to clip stuff above water
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - world.getHeightOfWater(0, 0));
            // change position and pitch of camera to render the reflection 
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, 1, 0, -world.getHeightOfWater(0, 0)+1f), true);
            camera.getPosition().y += distance;
            camera.invertPitch();

            // render to refraction texture: set the clip plane to clip stuff below water
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, world.getHeightOfWater(0, 0)+1f), true);
            
            // render to screen: set the clip plane at a great height, so it won't clip anything
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, 1000000), false);

            waterRenderer.render(world.getWaterTiles(), sky, camera, lights);
            
            guiRenderer.render(guiTextures);
            
            if (t > 15.0f && t < 30f) {
                float v = -0.01f * (t - 5.0f) * (t - 5.0f);
                text.increasePosition(v * dt, 0f);
                text2.increasePosition(v * dt, 0f);
                text3.increasePosition(v * dt, 0f);
            }
            if (t >= 30f) {
                if (text != null) {
                    TextMaster.removeText(text);
                    text = null;
                }
                if (text2 != null) {
                    TextMaster.removeText(text2);
                    text2 = null;
                }
                if (text3 != null) {
                    TextMaster.removeText(text3);
                    text3 = null;
                }
            }

            TextMaster.render();
            
            // frames = 0 means a new second
            int frames = DisplayManager.updateDisplay();
            
            if (frames == 0) {
                camera.printPosition();
                System.out.println("ray:          " + picker.getCurrentRay());
                System.out.println("terrainPoint: " + picker.getCurrentTerrainPoint());
            }
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public static void main(String[] args) {
        new MainGameLoop32();
    }
}