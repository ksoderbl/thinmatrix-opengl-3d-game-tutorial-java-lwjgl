package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Camera31;
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
import renderEngine.MasterRenderer31;
import skybox.Sky;
import terrains.Terrain;
import terrains.World;
import terrains.World29;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer30;
import water.WaterShader30;
import water.WaterTile;
import water.WaterTile04;

// OpenGL 3D Game Tutorial 31: Normal Mapping
// https://www.youtube.com/watch?v=4DUfwAEx4Ts&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=31

// Raycasting
// http://antongerdelan.net/opengl/raycasting.html

public class MainGameLoop31
{
	public static void main(String[] args) {
		new MainGameLoop31();
	}
	
	String title = "OpenGL 3D Game Tutorial 31";
	String subTitle = "Normal Mapping";
	String subSubTitle = "Use keys w, a, s, d to move player, use mouse to control camera";
	 //"Use key c to swap to second camera, move it with arrow keys";
	
    float terrainSize = 4000;
    float terrainMaxHeight = 500;
    float waterSize = terrainSize;
    float waterHeight = 0;
    
    Random random = new Random(676451);
    Loader loader = new Loader();
    List<Entity> entities = new ArrayList<>();
    List<Entity> normalMapEntities = new ArrayList<>();
    
    boolean vsync = true;
    
    public void addEntity(World world, TexturedModel texturedModel, float rx, float rz, float scale) {
    	int numTextureRows = texturedModel.getTexture().getNumberOfRows();
    	int numSubTextures = numTextureRows * numTextureRows;
    	
	    float x = random.nextFloat() * terrainSize - terrainSize / 2;
		float z = random.nextFloat() * terrainSize - terrainSize / 2;
		float y = world.getHeightOfTerrain(x, z);
		if (y > world.getHeightOfWater(x, z)) {
	        float ry = random.nextFloat() * 360;
	        
	        if (numSubTextures > 1) {
	        	int textureIndex = random.nextInt(numSubTextures);
	        	entities.add(new Entity(texturedModel, textureIndex, new Vector3f(x, y, z), rx, ry, rz, scale));
	        }
	        else {
	        	entities.add(new Entity(texturedModel, new Vector3f(x, y, z), rx, ry, rz, scale));
	        }
		}
    }
	
    public MainGameLoop31() {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        MasterRenderer31 renderer = new MasterRenderer31(loader);
    	DisplayManager.setVSync(vsync);

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
        
        World world = new World29(loader, terrainSize, terrainMaxHeight, waterHeight);
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
        TexturedModel footBallModel = loader.createTexturedModel("foot", "foot", "footNormal", 10, 0.5f);

        //************ENTITIES*******************

        float ex, ey, ez;

        ex = 75;
        ez = 75;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        Entity entity = new Entity(barrelModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f);
        normalMapEntities.add(entity);

        ex = 15;
        ez = 75;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        Entity entity2 = new Entity(boulderModel, new Vector3f(ex, ey, ez), 0, 0, 0, 2f);
        normalMapEntities.add(entity2);

        ex = 55;
        ez = 75;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        Entity entity3 = new Entity(crateModel, new Vector3f(ex, ey, ez), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity3);

        ex = 155;
        ez = 75;
        ey = world.getHeightOfTerrain(ex, ez) + 5;
        Entity entity4 = new Entity(footBallModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f);
        normalMapEntities.add(entity4);
       
        
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
        entities.add(new Entity(oldBarrelModel, new Vector3f(ex, ey, ez), 0, 0, 0, 0.5f));

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
        
        Sky sky = new Sky(0.388f, 0.552f, 0.678f, 0.001f, 3f);
        
        List<Light> lights = new ArrayList<Light>();

        // OpenGL 3D Game Tutorial 25: Multiple Lights
        lights.add(new Light(new Vector3f(30000, 300, 0), new Vector3f(0.39f, 0.55f, 0.68f)));
       
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
        Entity lampEntity = new Entity(lampModel, new Vector3f(ex, ey, ez), 0, 0, 0, 1f);
        entities.add(lampEntity);
        Light lampLight = new Light(new Vector3f(ex, ey+14, ez), new Vector3f(12, 12, 8), new Vector3f(1, 0.01f, 0.002f));
        lights.add(lampLight);
        
        for (int i = 0; i < 2000; i++) {
        	if (i % 3 == 0) {
        		addEntity(world, grassModel, 0, 0, 1.8f);
        		addEntity(world, flowerModel, 0, 0, 2.3f);
        	}

        	if (i % 2 == 0) {
        		addEntity(world, fernModel, 10 * random.nextFloat() - 5, 10 * random.nextFloat() - 5, 0.9f);
        		
	            // low poly tree "bobble"
        		addEntity(world, lowPolyTreeModel, 4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 0.1f + 0.6f);
	
        		addEntity(world, treeModel,  4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 1f + 4f);
	        	addEntity(world, pineModel,  4 * random.nextFloat() - 2, 4 * random.nextFloat() - 2, random.nextFloat() * 4f + 1f);
	        	
	        	addEntity(world, toonRocksModel, 0, 0, 4 * random.nextFloat());
        	}
        }

    	float px = -35f; //-2163f;
    	float pz = 110; //2972f;
    	float py = world.getHeightOfTerrain(px, pz);
        
        TexturedModel playerModel = loader.createTexturedModel("person", "playerTexture", 1, 0);
        PlayerWater04 player = new PlayerWater04(playerModel, new Vector3f(px, py, pz), 0, 90, 0, 0.6f);
        entities.add(player);
        
        Camera camera = new Camera31(player);
        //camera.getPosition().translate(0, 20, 0);
       
        // Water
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        
        WaterShader30 waterShader = new WaterShader30();
        WaterRenderer30 waterRenderer = new WaterRenderer30(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile04(0, 0, waterHeight, waterSize);
        waters.add(water);
        water = new WaterTile04(-1 * waterSize, 0, waterHeight, waterSize);
        waters.add(water);
        water = new WaterTile04(-1 * waterSize, -1 * waterSize, waterHeight, waterSize);
        waters.add(water);
        water = new WaterTile04(0, -1 * waterSize, waterHeight, waterSize);
        waters.add(water);
      

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
        
        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), world);

        //****************Game Loop Below*********************
        
        while (!Display.isCloseRequested()) {
        	
        	player.move(world);
        	       	
        	camera.move();
        	
            //entity.increaseRotation(0.1f, 0.2f, 0.3f);
            //entity2.increaseRotation(0.3f, 0.1f, 0.2f);
            //entity3.increaseRotation(0.2f, 0.3f, 0.1f);
        	float dt = DisplayManager.getFrameTimeSeconds();
            entity4.increaseRotation(12f * dt , 20f * dt, 6f * dt);
        	
            picker.update();
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if (terrainPoint != null) {
            	lampEntity.setPosition(terrainPoint);
            	lampLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 14, terrainPoint.z));
            }
        	
        	GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

        	// render to reflection texture: set the clip plane to clip stuff above water
        	buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            // change position and pitch of camera to render the reflection 
            camera.getPosition().y -= distance;
            camera.invertPitch();
        	renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, 1, 0, -water.getHeight()+1f), true);
            camera.getPosition().y += distance;
            camera.invertPitch();

        	// render to refraction texture: set the clip plane to clip stuff below water
        	buffers.bindRefractionFrameBuffer();
        	renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, water.getHeight()+1f), true);
        	
        	// render to screen: set the clip plane at a great height, so it won't clip anything
        	buffers.unbindCurrentFrameBuffer();
        	renderer.renderScene(entities, normalMapEntities, terrains, lights, sky, camera, new Vector4f(0, -1, 0, 1000000), false);

        	waterRenderer.render(waters, sky, camera, lights);
        	
        	guiRenderer.render(guiTextures);

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


}
