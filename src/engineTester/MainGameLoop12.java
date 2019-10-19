package engineTester;

import java.io.File;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera12;
import entities.Entity;
import entities.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer12;
import shaders.StaticShader12;
import textures.ModelTexture;

// OpenGL 3D Game Tutorial 12: Specular Lighting
// https://www.youtube.com/watch?v=GZ_1xOm-3qU&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=12

public class MainGameLoop12
{
	public static String title = "OpenGL 3D Game Tutorial 12";
	public static String subTitle = "Specular Lighting";
	public static String subSubTitle = "Press, w, a, s or d to move";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        StaticShader12 shader = new StaticShader12();
        Renderer12 renderer = new Renderer12(shader);

        RawModel model = OBJFileLoader.loadOBJ("dragon", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
        TexturedModel staticModel = new TexturedModel(model, texture);
        texture.setShineDamper(2);
        texture.setReflectivity(1.0f);
        
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
        
        
        // create some random entities 
        Entity[] entities = new Entity[ 20 ];
        Random random = new Random();
        
        for (int i = 0; i < entities.length; i++) {
        	Vector3f translation = new Vector3f(
        			((10 * random.nextFloat()) - 5) * 0.8f,
        			((10 * random.nextFloat()) - 5) * 0.8f,
        			-10f + ((10 * random.nextFloat()) - 5) * 0.8f);
            float rx = 0;
            float ry = 0; 
            float rz = 0; //360 * random.nextFloat();
            float tmp = random.nextFloat();
            float scale = 0.1f; //tmp * tmp * 0.1f;

            entities[i] = new Entity(staticModel, translation, rx, ry, rz, scale);
        }
        
        Light light = new Light(
        		new Vector3f(0, 0, -10),
        		new Vector3f(1f, 1f, 1f)); // white light
        
        Camera12 camera = new Camera12();
        
        while (!Display.isCloseRequested()) {
        	
        	for (int i = 0; i < entities.length; i++) {
        		//entities[i].increasePosition(0, 0, 0.00001f*i);
        		entities[i].increaseRotation(0, 0.1f*i, 0);
        	}
        	
        	//light.getPosition().translate(
        	//		((10 * random.nextFloat()) - 5) / 10f,
        	//		((10 * random.nextFloat()) - 5) / 10f,
			//		((10 * random.nextFloat()) - 5) / 10f);

        	camera.move();
        	renderer.prepare();
        	shader.start();
        	shader.loadLight(light);
        	shader.loadViewMatrix(camera);
        	for (int i = 0; i < entities.length; i++) {
        		//if (i == 0) {
        		//	entities[i].setPosition(light.getPosition());
        		//}
	            renderer.render(entities[i], shader);
        	}
            shader.stop();
            
        	TextMaster.render();
        	            
            DisplayManager.updateDisplay();
        }

        TextMaster.cleanUp();
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
