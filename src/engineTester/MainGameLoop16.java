package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera13;
import entities.Entity;
import entities.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer16;
import terrains.Terrain14;
import textures.ModelTexture;

// OpenGL 3D Game Tutorial 16: Fog
// https://www.youtube.com/watch?v=qslBNLeSPUc&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=16

public class MainGameLoop16
{
	public static String title = "OpenGL 3D Game Tutorial 16";
	public static String subTitle = "Fog";
	public static String subSubTitle = "Press, w, a, s or d to move";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Camera13 camera = new Camera13();
        camera.getPosition().translate(0, 4, 0);

        TextMaster.init(loader);
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        text.setColor(0.2f, 0.2f, 0.8f);
        FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        text2.setColor(0.8f, 0.2f, 0.2f);
        FontType font3 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text3 = new GUIText(subSubTitle, 1.5f, font3, new Vector2f(0.0f, 0.3f), 1.0f, true);
        text3.setColor(0.8f, 0.8f, 0.2f);

        TexturedModel treeModel = loader.createTexturedModel("tree", "tree", 1, 0);
        TexturedModel lowPolyTreeModel = loader.createTexturedModel("lowPolyTree", "lowPolyTree", 1, 0);
        TexturedModel grassModel = loader.createTexturedModel("grassModel", "grassTexture", 1, 0, true, true);
        TexturedModel flowerModel = loader.createTexturedModel("grassModel", "flower", 1, 0, true, true);
        TexturedModel fernModel = loader.createTexturedModel("fern", "fern1", 1, 0, true, false);
        
        
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 200; i++) {
        	
        	// tree
        	float x = random.nextFloat() * 800 - 400;
        	float y = 0; 
        	float z = random.nextFloat() * -600; 
            float rx = 4 * random.nextFloat() - 2;
            float ry = random.nextFloat() * 360;
            float rz = 4 * random.nextFloat() - 2;
            float scale = 3f;
            
            entities.add(new Entity(treeModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            // low poly tree
        	x = random.nextFloat() * 800 - 400;
        	y = 0; 
        	z = random.nextFloat() * -600; 
            rx = 4 * random.nextFloat() - 2;
            ry = random.nextFloat() * 360;
            rz = 4 * random.nextFloat() - 2;
            scale = 0.5f;
            entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), rx, ry, rz, scale));
            
            // grass
            x = random.nextFloat() * 800 - 400;
        	z = random.nextFloat() * -600;
            rx = 0;
            ry = random.nextFloat() * 360;
            rz = 0;
            scale = 1f;
            entities.add(new Entity(grassModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            // flower
            x = random.nextFloat() * 800 - 400;
        	z = random.nextFloat() * -600;
            rx = 0;
            ry = random.nextFloat() * 360;
            rz = 0;
            scale = 1f;
            entities.add(new Entity(flowerModel, new Vector3f(x, y, z), rx, ry, rz, scale));

            // fern
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

        MasterRenderer16 renderer = new MasterRenderer16();
        
        int i = 0;
        
        while (!Display.isCloseRequested()) {
        	//int i = 0;
        	//for (Entity entity : entities) {
        	//	//entities[i].increasePosition(0, 0, 0.00001f*i);
        	//	entity.increaseRotation(0f, 0.4f*i, 0f);
        	//	i++;
        	//}
        	camera.move();
        	
        	for (Entity entity : entities) {
        		renderer.processEntity(entity);
        	}
        	renderer.processTerrain(terrain);
        	renderer.processTerrain(terrain2);
        	
            renderer.render(light, camera);
        	TextMaster.render();
            DisplayManager.updateDisplay();
            
            if ((i % 60) == 0) {
            	camera.printPosition();
            }
            i++;
        }

        TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
