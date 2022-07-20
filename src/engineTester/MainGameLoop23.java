package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Camera18;
import entities.Camera22;
import entities.Entity;
import entities.Light;
import entities.Player22;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer23;
import skybox.Sky;
import terrains.Terrain;
import terrains.World;
import terrains.World23;
import water.WaterFrameBuffers;
import water.WaterRenderer01;
import water.WaterShader01;
import water.WaterTile;
import water.WaterTile01;

// OpenGL 3D Game Tutorial 23: Texture Atlases
// https://www.youtube.com/watch?v=6T182r4F6J8&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=23

public class MainGameLoop23
{
    public static String title = "OpenGL 3D Game Tutorial 23";
    public static String subTitle = "Texture Atlases"; //"Use keys w, a, s, d to move player, use mouse to control camera";
    public static String subSubTitle = ""; //"Use key c to swap to second camera, move it with arrow keys";
    
    public static void main(String[] args) {
        DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();

        TextMaster.init(loader);
        if (title.length() > 0) {
            FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text = new GUIText(title, 2.0f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
            text.setColor(0.2f, 0.2f, 0.8f);
        }
        if (subTitle.length() > 0) {
            FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text2 = new GUIText(subTitle, 1.5f, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
            text2.setColor(0.8f, 0.2f, 0.2f);
        }
        if (subSubTitle.length() > 0) {
            FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
            GUIText text3 = new GUIText(subSubTitle, 1.25f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
            text3.setColor(0.2f, 0.8f, 0.2f);
        }
        
        World world = new World23(loader);
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
        
        // these should take into account the terrain height
        entities.add(new Entity(rocksModel, new Vector3f(0, 0, 0), 0, 0, 0, 75));
        entities.add(new Entity(boxModel, new Vector3f(100, 10, -300), 0, 0, 0, 10));
        entities.add(new Entity(stallModel, new Vector3f(-50, 0, -250), 0, -50, 0, 2f));
        entities.add(new Entity(barrelModel, new Vector3f(-40, 3, -240), 0, 0, 0, 0.5f));
        entities.add(new Entity(exampleModel, new Vector3f(-30, 0, -230), 0, 0, 0, 1f));
        entities.add(new Entity(lampModel, new Vector3f(-30, 0, -220), 0, 0, 0, 1f));
        Entity boxEntity = new Entity(boxModel, new Vector3f(225.5f, 5, -352.6f), 0, 25f, 0, 5f);
        entities.add(boxEntity);
        
        Random random = new Random(676452);

        for (int i = 0; i < 400; i++) {
            
            float x = 0, y = 0, z = 0, rx = 0, ry = 0, rz = 0, scale = 1;
            int textureIndex = 0;
            
            if (i % 7 == 0) {
                // grass
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                y = world.getHeightOfTerrain(x, z);
                rx = 0;
                ry = random.nextFloat() * 360;
                rz = 0;
                scale = 1.8f;
                entities.add(new Entity(grassModel, new Vector3f(x, y, z), rx, ry, rz, scale));

                // flower
                x = random.nextFloat() * 400 - 200;
                z = random.nextFloat() * -400;
                y = world.getHeightOfTerrain(x, z);
                rx = 0;
                ry = random.nextFloat() * 360;
                rz = 0;
                scale = 2.3f;
                entities.add(new Entity(flowerModel, new Vector3f(x, y, z), rx, ry, rz, scale));
            }

            if (i % 3 == 0) {
                // fern
                textureIndex = random.nextInt(4);
                x = random.nextFloat() * 400 - 400;
                z = random.nextFloat() * -400;
                y = world.getHeightOfTerrain(x, z);
                rx = 10 * random.nextFloat() - 5;
                ry = random.nextFloat() * 360;
                rz = 10 * random.nextFloat() - 5;
                scale = 0.9f;
                entities.add(new Entity(fernModel, textureIndex, new Vector3f(x, y, z), rx, ry, rz, scale));
    
                // low poly tree "bobble"
                textureIndex = random.nextInt(4);
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = world.getHeightOfTerrain(x, z);
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 0.1f + 0.6f;
                entities.add(new Entity(lowPolyTreeModel, textureIndex, new Vector3f(x, y, z), rx, ry, rz, scale));
    
                // tree
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = world.getHeightOfTerrain(x, z);
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 1f + 4f;
                entities.add(new Entity(treeModel, new Vector3f(x, y, z), rx, ry, rz, scale));

                // pine
                x = random.nextFloat() * 800;
                z = random.nextFloat() * -600;
                y = world.getHeightOfTerrain(x, z);
                rx = 4 * random.nextFloat() - 2;
                ry = random.nextFloat() * 360;
                rz = 4 * random.nextFloat() - 2;
                scale = random.nextFloat() * 4f + 1f;
                entities.add(new Entity(pineModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            }
        }
        
        
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        Player22 player = new Player22(playerModel, new Vector3f(0, 0, 0), 0, 180, 0, 0.6f);
        entities.add(player);
        
        Camera camera1 = new Camera22(player);
        camera1.getPosition().translate(0, 20, 0);

        Camera camera2 = new Camera18();
        camera2.getPosition().translate(0, 30, 0);
        
        Camera camera = camera1;

        Light light = new Light(
                new Vector3f(10000, 10000, -10000),
                new Vector3f(1f, 1f, 1f)); // white light
        
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        
        MasterRenderer23 renderer = new MasterRenderer23();
        
        int i = 0;
        int cameraFrames = 0;
        
        // Water
        
        WaterShader01 waterShader = new WaterShader01();
        WaterRenderer01 waterRenderer = new WaterRenderer01(loader, waterShader, renderer.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile01(0, -150, World23.WATER_HEIGHT);
        waters.add(water);
        
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        List<GuiTexture> guiTextures = new ArrayList<>();
        GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.6f, -0.6f), new Vector2f(0.4f, 0.4f));
        guiTextures.add(refrGui);
        guiTextures.add(reflGui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        Sky sky = new Sky();

        //****************Game Loop Below*********************
        
        while (!Display.isCloseRequested()) {
            
            player.move(world);
            
            cameraFrames++;
            // key C used to swap camera
            if (cameraFrames > 10 && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                if (camera == camera1) {
                    camera = camera2;
                }
                else if (camera == camera2) {
                    camera = camera1;
                }
                cameraFrames = 0;
            }
            
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
