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
import entities.CameraWater04;
import entities.Entity;
import entities.Light;
import entities.PlayerWater04;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer27;
import skybox.Sky;
import terrains.Terrain;
import terrains.World;
import terrains.WorldWater04;
import water.WaterFrameBuffers;
import water.WaterRenderer08;
import water.WaterShader08;
import water.WaterTile;
import water.WaterTile04;

// OpenGL Water Tutorial 8: Soft Edges (FINAL)
// https://www.youtube.com/watch?v=qgDPSnZPGMA&list=PLRIWtICgwaX23jiqVByUs0bqhnalNTNZh&index=8

public class MainGameLoopWater08
{
	public static String title = "OpenGL Water Tutorial 8";
	public static String subTitle = "Soft Edges (FINAL)";
	public static String subSubTitle = "Use keys w, a, s, d to move player, use mouse to control camera";
	 //"Use key c to swap to second camera, move it with arrow keys";
	
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
        float terrainMaxHeight = 8000;
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
        TexturedModel lampModel = loader.createTexturedModel("lamp", "lamp", 1, 0, false, true);
        
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
        
        
        // TODO: make these move
        List<Light> lights = new ArrayList<Light>();

        // OpenGL 3D Game Tutorial 25: Multiple Lights
        lights.add(new Light(new Vector3f(30000, 300, 0), new Vector3f(0.59f, 0.59f, 0.59f)));
       
        ex = 1126.3969f;
        ez = 2621.307f;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(lampModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f));
        lights.add(new Light(new Vector3f(ex, ey+14, ez), new Vector3f(3, 1, 1), new Vector3f(1, 0.01f, 0.002f)));

        ex = 375.8717f;
        ez = 587.5373f;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(lampModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f));
        lights.add(new Light(new Vector3f(ex, ey+14, ez), new Vector3f(1, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

        ex = 362.69772f;
        ez = 616.70355f;
        ey = world.getHeightOfTerrain(ex, ez);
        entities.add(new Entity(lampModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f));
        lights.add(new Light(new Vector3f(ex, ey+14, ez), new Vector3f(0, 1, 2), new Vector3f(1, 0.01f, 0.002f)));
        
        
        Random random = new Random(676452);

        for (int i = 0; i < 2000; i++) {
        	
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

    	float px = 350f; //-2163f;
    	float pz = 540f; //2972f;
    	float py = world.getHeightOfTerrain(px, pz);
        
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        PlayerWater04 player = new PlayerWater04(playerModel, new Vector3f(px, py, pz), 0, 2, 0, 0.6f);
        entities.add(player);
        
        Camera camera1 = new CameraWater04(player);
        camera1.getPosition().translate(0, 20, 0);

        Camera camera2 = new Camera18();
        camera2.getPosition().translate(0, 30, 0);
        
        Camera camera = camera1;

        MasterRenderer27 renderer = new MasterRenderer27(loader);
        
        int i = 0;
        int cameraFrames = 0;
        
        // Water
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        WaterShader08 waterShader = new WaterShader08();
        WaterRenderer08 waterRenderer = new WaterRenderer08(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile04(0, 0, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(-1 * terrainSize, 0, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(-1 * terrainSize, -1 * terrainSize, waterHeight, terrainSize);
        waters.add(water);
        water = new WaterTile04(0, -1 * terrainSize, waterHeight, terrainSize);
        waters.add(water);
        
        Sky sky = new Sky(0.388f, 0.552f, 0.678f, 0.00015f, 7f);

//        List<GuiTexture> guiTextures = new ArrayList<>();
//        GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
//        GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
//        //GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.6f, -0.6f), new Vector2f(0.4f, 0.4f));
//        guiTextures.add(refrGui);
//        guiTextures.add(reflGui);
//        GuiRenderer guiRenderer = new GuiRenderer(loader);
        
        List<GuiTexture> guiTextures = new ArrayList<>();

//        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.7f, 0.5f), new Vector2f(0.125f, 0.125f));
//        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.7f, 0.8f), new Vector2f(0.2f, 0.2f));
        GuiTexture gui3 = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.8f, 0.9f), new Vector2f(0.2f, 0.2f));

//        guiTextures.add(gui);
//        guiTextures.add(gui2);
        guiTextures.add(gui3);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

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
        	renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, 1, 0, -water.getHeight()+1f));
            camera.getPosition().y += distance;
            camera.invertPitch();

        	// render to refraction texture: set the clip plane to clip stuff below water
        	buffers.bindRefractionFrameBuffer();
        	renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, water.getHeight()+1f));
        	
        	// render to screen: set the clip plane at a great height, so it won't clip anything
        	buffers.unbindCurrentFrameBuffer();
        	renderer.renderScene(entities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, 1000000));

        	waterRenderer.render(waters, sky, camera, lights);
        	
        	guiRenderer.render(guiTextures);

        	TextMaster.render();
            
        	DisplayManager.updateDisplay();
            
            Vector3f cameraPos = camera.getPosition();
            if ((i % 60) == 0) {
            	System.out.println("Camera Pos: (x = " + cameraPos.getX() + ", z = " + cameraPos.getZ() + ")");
            }
            i++;
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        TextMaster.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
