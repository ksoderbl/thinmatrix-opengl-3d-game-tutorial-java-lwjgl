package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera18;
import entities.Entity;
import entities.Light;
import entities.Player18;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRendererWater01;
import skybox.FoggySky;
import skybox.Sky;
import terrains.Terrain;
import terrains.Terrain17;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterRenderer01;
import water.WaterShader01;
import water.WaterTile;
import water.WaterTile01;

// OpenGL Water Tutorial 1: Introduction
// https://www.youtube.com/watch?v=HusvGeEDU_U&list=PLRIWtICgwaX23jiqVByUs0bqhnalNTNZh&index=1

public class MainGameLoopWater01
{
	public static String title = "OpenGL Water Tutorial 1";
	public static String subTitle = "Introduction";
	public static String subSubTitle = "Press, w, a, s or d to move player, arrow keys to move camera";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Camera18 camera = new Camera18();
        camera.getPosition().translate(0, 20, 0);

        TextMaster.init(loader);
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        text.setColor(0.0f, 0.0f, 0.8f);
        FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        text2.setColor(0.2f, 0.4f, 0.8f);
        FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text3 = new GUIText(subSubTitle, 1.5f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
        text3.setColor(0.4f, 0.8f, 0.8f);
        
        // *********TERRAIN TEXTURE STUFF**********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
                rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        // *****************************************

        TexturedModel treeModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel lowPolyTreeModel = loader.createTexturedModel("lowPolyTree", "lowPolyTree", 1, 0);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0, true, true);
        TexturedModel flowerModel = loader.createTexturedModel("grassModel", "flower", 1, 0, true, true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern1", 1, 0, true, false);
        
        
        List<Entity> entities = new ArrayList<>();
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
        	}
        }
        
        TexturedModel playerModel = loader.createTexturedModel("stanfordBunny", "white", 10, 1);
        Player18 player = new Player18(playerModel, new Vector3f(0, 4, -50), 0, 0, 0, 0.5f);
        entities.add(player);
        
        Light light = new Light(
        		new Vector3f(20000, 40000, 20000),
        		new Vector3f(1f, 1f, 1f)); // white light
        
        List<Light> lights = new ArrayList<Light>();
        lights.add(light);
        
        //ModelTexture terrainModelTexture = new ModelTexture(loader.loadTexture("grass"));
        Terrain terrain = new Terrain17(0, -1, loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain17(-1, -1, loader, texturePack, blendMap);
        
        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);
        terrains.add(terrain2);

        MasterRendererWater01 renderer = new MasterRendererWater01();
        
        int i = 0;
        
        // Water
        
        WaterShader01 waterShader = new WaterShader01();
        WaterRenderer01 waterRenderer = new WaterRenderer01(loader, waterShader, renderer.getProjectionMatrix());
        List<WaterTile> waters = new ArrayList<>();
        waters.add(new WaterTile01(0, -150, 1));
        
        Sky sky = new FoggySky();

        //****************Game Loop Below*********************
        
        while (!Display.isCloseRequested()) {
        	player.move();
        	camera.move();
        	
        	renderer.renderScene(entities, terrains, lights, sky, camera);
        	waterRenderer.render(waters, sky, camera);

        	TextMaster.render();
            
        	DisplayManager.updateDisplay();
            
            Vector3f cameraPos = camera.getPosition();
            if ((i % 60) == 0)
            	System.out.println("Camera Pos: (x = " + cameraPos.getX() + ", z = " + cameraPos.getZ() + ")");
            i++;
        }

        waterShader.cleanUp();
        TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
