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
import renderEngine.MasterRenderer14;
import terrains.Terrain14;
import textures.ModelTexture;

// OpenGL 3D Game Tutorial 14: Simple Terrain
// https://www.youtube.com/watch?v=yNYwZMmgTJk&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=14

public class MainGameLoop14
{
	public static String title = "OpenGL 3D Game Tutorial 14";
	public static String subTitle = "Simple Terrain";
	public static String subSubTitle = "Press, w, a, s or d to move";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        Camera13 camera = new Camera13();
        camera.getPosition().translate(0, 7, 0);

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

        TexturedModel staticModel = loader.createTexturedModel("tree", "tree", 1, 0);
        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < 200; i++) {
        	
        	/*
        	float x = ((10 * random.nextFloat()) - 5) * 0.8f;
        	float y = ((10 * random.nextFloat()) - 5) * 0.8f;
        	float z = -10f + ((10 * random.nextFloat()) - 5) * 0.8f;
            float rx = 0;
            float ry = 0; 
            float rz = 0; //360 * random.nextFloat();
            float tmp = random.nextFloat();
            float scale = 0.1f; //tmp * tmp * 0.1f;
            */
        	
        	float x = random.nextFloat() * 100 - 50;
        	float y = 0; 
        	float z = random.nextFloat() * -300; 
            float rx = 0; //random.nextFloat() * 180f;
            float ry = random.nextFloat() * 180f;
            float rz = 0; //random.nextFloat() * 180f;
            float scale = 1f;

            entities.add(new Entity(staticModel, new Vector3f(x, y, z),
            		rx, ry, rz, scale));
        }
        
        Light light = new Light(
        		new Vector3f(3000, 2000, 3000),
        		new Vector3f(1f, 1f, 1f)); // white light
        
        ModelTexture terrainModelTexture = new ModelTexture(loader.loadTexture("grass"));
        Terrain14 terrain = new Terrain14(0, -1, loader, terrainModelTexture);
        Terrain14 terrain2 = new Terrain14(-1, -1, loader, terrainModelTexture);

        MasterRenderer14 renderer = new MasterRenderer14();
        
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
            
            Vector3f cameraPos = camera.getPosition();
            if ((i % 60) == 0)
            	System.out.println("Camera Pos: (x = " + cameraPos.getX() + ", z = " + cameraPos.getZ() + ")");
            i++;
        }

        TextMaster.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
