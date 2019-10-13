package engineTester;

import java.io.File;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import renderEngine.DisplayManager;
import renderEngine.Loader;

// https://www.youtube.com/watch?v=VS8wlS9hF8E&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=2

public class MainGameLoop01
{
	public static String title = "OpenGL 3D Game Tutorial 1";
	public static String subTitle = "The Display";
	
    public static void main(String[] args) {
    	DisplayManager.createDisplay(title + ": " + subTitle);
        Loader loader = new Loader();
        TextMaster.init(loader);
        
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText(title, 2.5f, font, new Vector2f(0.0f, 0.1f), 1.0f, true);
        text.setColor(0.2f, 0.2f, 0.8f);
        
        FontType font2 = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text2 = new GUIText(subTitle, 2, font2, new Vector2f(0.0f, 0.2f), 1.0f, true);
        text2.setColor(0.8f, 0.2f, 0.2f);

        while (!Display.isCloseRequested()) {
            TextMaster.render();
            
            DisplayManager.updateDisplay();
        }

        TextMaster.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
