package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {
    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //******** TERRAIN TEXTURE STUFF ********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
                rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        //***************************************

        ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonRawModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
        TexturedModel dragonModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setShineDamper(10.0f);
        dragonTexture.setReflectivity(1.0f);
        Entity dragon = new Entity(dragonModel, new Vector3f(0, 0, -25), 0, 160,0, 1);


        ModelData pineData = OBJFileLoader.loadOBJ("pine");
        RawModel pineRawModel = loader.loadToVAO(pineData.getVertices(), pineData.getTextureCoords(), pineData.getNormals(), pineData.getIndices());
        TexturedModel pineModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadTexture("pine")));

        ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel lowPolyTreeRawModel = loader.loadToVAO(lowPolyTreeData.getVertices(), lowPolyTreeData.getTextureCoords(), lowPolyTreeData.getNormals(), lowPolyTreeData.getIndices());
        TexturedModel lowPolyTreeModel = new TexturedModel(lowPolyTreeRawModel, new ModelTexture(loader.loadTexture("lowPolyTree")));

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassRawModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        TexturedModel grassModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grassTexture")));
        grassModel.getTexture().setHasTransparency(true);
        grassModel.getTexture().setUseFakeLighting(true);

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        RawModel fernRawModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
        TexturedModel fernModel = new TexturedModel(fernRawModel, fernTextureAtlas);
        fernModel.getTexture().setHasTransparency(true);

        String heightMap = "heightmap";
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, heightMap);
        //Terrain terrain2 = new Terrain(0, 0, loader, texturePack, blendMap, heightMap);
        //Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap, heightMap);
        //Terrain terrain4 = new Terrain(-1, -1, loader, texturePack, blendMap, heightMap);
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain);
        //terrains.add(terrain2);
        //terrains.add(terrain3);
        //terrains.add(terrain4);

        List<Entity> entities = new ArrayList<Entity>();

        Random random = new Random(676452);

        for (int i = 0; i < 400; i++) {
            if (i % 1 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, 0.9f));
            }

            if (i % 5 == 0) {
                float x = random.nextFloat() * 800 - 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(pineModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grassModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));
            }
        }


        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,1000,-7000), new Vector3f(0.4f,0.4f,0.4f)));
        lights.add(new Light(new Vector3f(185,10,-293), new Vector3f(2,0,0), new Vector3f(1,0.01f,0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300), new Vector3f(0,2,2), new Vector3f(1,0.01f,0.002f)));
        lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(2,2,0), new Vector3f(1,0.01f,0.002f)));

        ModelData lampData = OBJFileLoader.loadOBJ("lamp");
        RawModel lampRawModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
        TexturedModel lampModel = new TexturedModel(lampRawModel, new ModelTexture(loader.loadTexture("lamp")));
        lampModel.getTexture().setUseFakeLighting(true);

        entities.add(new Entity(lampModel, new Vector3f(185,-4.7f,-293), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(370, 4.2f,-300), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(293,-6.8f,-305), 0, 0, 0, 1));

        MasterRenderer renderer = new MasterRenderer();

        ModelData playerData = OBJFileLoader.loadOBJ("person");
        RawModel playerRawModel = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());
        TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(playerModel, new Vector3f(0, 0, 0), 0, 0,0, 0.5f);
        entities.add(player);

        Camera camera = new Camera(player);

        List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.7f, 0.5f), new Vector2f(0.125f, 0.125f));
        GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.5f, 0.6f), new Vector2f(0.2f, 0.2f));
        GuiTexture gui3 = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.8f, 0.9f), new Vector2f(0.2f, 0.2f));
        guiTextures.add(gui);
        guiTextures.add(gui2);
        guiTextures.add(gui3);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile(30, -30, 2f);
        waters.add(water);

        GuiTexture refrGui = new GuiTexture(fbos.getRefractionTexture(), new Vector2f( 0.75f, -0.75f), new Vector2f(0.25f, 0.25f));
        GuiTexture reflGui = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.75f, -0.75f), new Vector2f(0.25f, 0.25f));
        guiTextures.add(refrGui);
        guiTextures.add(reflGui);

        // *****************Game Loop Below********************

        //boolean fullScreen = false;

        while (!Display.isCloseRequested()) {
            player.move(terrain); // TODO: find which terrain the player is on
            camera.move();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()));
            camera.getPosition().y += distance;
            camera.invertPitch();

            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

            fbos.unbindCurrentFrameBuffer();

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

            renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, 1000000));
            waterRenderer.render(waters, camera);

            //guiRenderer.render(guiTExtures);

            /*
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                fullScreen = !fullScreen;
                try {
                    Display.setFullscreen(fullScreen);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
            }
            */

            guiRenderer.render(guiTextures);
            DisplayManager.updateDisplay();
        }

        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
