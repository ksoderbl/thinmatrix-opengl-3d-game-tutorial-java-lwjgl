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


        float[] cubeVertices = {
                -0.5f,0.5f,-0.5f, // 0
                -0.5f,-0.5f,-0.5f,// 1
                0.5f,-0.5f,-0.5f, // 2
                0.5f,0.5f,-0.5f,  // 3

                -0.5f,0.5f,0.5f,  // 4
                -0.5f,-0.5f,0.5f, // 5
                0.5f,-0.5f,0.5f,  // 6
                0.5f,0.5f,0.5f,   // 7

                0.5f,0.5f,-0.5f,  // 8  = 3
                0.5f,-0.5f,-0.5f, // 9  = 2
                0.5f,-0.5f,0.5f,  // 10 = 6
                0.5f,0.5f,0.5f,   // 11 = 7

                -0.5f,0.5f,-0.5f, // 12 = 0
                -0.5f,-0.5f,-0.5f,// 13 = 1
                -0.5f,-0.5f,0.5f, // 14 = 5
                -0.5f,0.5f,0.5f,  // 15 = 4

                -0.5f,0.5f,0.5f,  // 16 = 4
                -0.5f,0.5f,-0.5f, // 17 = 0
                0.5f,0.5f,-0.5f,  // 18 = 3
                0.5f,0.5f,0.5f,   // 19 = 7

                -0.5f,-0.5f,0.5f, // 20 = 5
                -0.5f,-0.5f,-0.5f,// 21 = 1
                0.5f,-0.5f,-0.5f, // 22 = 2
                0.5f,-0.5f,0.5f   // 23 = 6

        };

        float[] cubeTextureCoords = {

                0,0,
                0,1,
                1,1,
                1,0,

                0,0,
                0,1,
                1,1,
                1,0,

                0,0,
                0,1,
                1,1,
                1,0,

                0,0,
                0,1,
                1,1,
                1,0,

                0,0,
                0,1,
                1,1,
                1,0,

                0,0,
                0,1,
                1,1,
                1,0


        };

        float[] cubeNormals = {

                0,0,-1,
                0,0,-1,
                0,0,-1,
                0,0,-1,

                0,0,1,
                0,0,1,
                0,0,1,
                0,0,1,





                1,0,0,
                1,0,0,
                1,0,0,
                1,0,0,

                -1,0,0,
                -1,0,0,
                -1,0,0,
                -1,0,0,




                0,1,0,
                0,1,0,
                0,1,0,
                0,1,0,

                0,-1,0,
                0,-1,0,
                0,-1,0,
                0,-1,0

        };


        int[] cubeIndices = {
                0,3,1,
                3,2,1, // OK

                4,5,7,
                7,5,6, // OK

                8,11,9,//8,9,11,
                11,10,9,//11,9,10, // OK

                12,13,15,
                15,13,14, // OK

                16,19,17, //16,17,19,
                19, 18, 17, //19,17,18,

                20,21,23,
                23,21,22

        };

        RawModel cubeRawModel = loader.loadToVAO(cubeVertices, cubeTextureCoords, cubeNormals, cubeIndices);
        TexturedModel cubeModel = new TexturedModel(cubeRawModel, new ModelTexture(loader.loadTexture("image")));
        ModelTexture cubeTexture = cubeModel.getTexture();
        cubeTexture.setShineDamper(1.0f);
        cubeTexture.setReflectivity(1.0f);
        //Entity cubeEntity = new Entity(cubeModel, new Vector3f(0, 0, -5), 0, 0,0, 1);

        ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonRawModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
        TexturedModel dragonModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("stallTexture")));
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setShineDamper(10.0f);
        dragonTexture.setReflectivity(1.0f);
        Entity dragon = new Entity(dragonModel, new Vector3f(0, 0, -25), 0, 160,0, 1);


        ModelData treeData = OBJFileLoader.loadOBJ("tree");
        RawModel treeRawModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
        TexturedModel treeModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("tree")));

        ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel lowPolyTreeRawModel = loader.loadToVAO(lowPolyTreeData.getVertices(), lowPolyTreeData.getTextureCoords(), lowPolyTreeData.getNormals(), lowPolyTreeData.getIndices());
        TexturedModel lowPolyTreeModel = new TexturedModel(lowPolyTreeRawModel, new ModelTexture(loader.loadTexture("lowPolyTree")));

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassRawModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        TexturedModel grassModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grassTexture")));
        grassModel.getTexture().setHasTransparency(true);
        grassModel.getTexture().setUseFakeLighting(true);

        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        RawModel fernRawModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
        TexturedModel fernModel = new TexturedModel(fernRawModel, new ModelTexture(loader.loadTexture("fern")));
        fernModel.getTexture().setHasTransparency(true);


        Light light = new Light(new Vector3f(3000,3000,2000), new Vector3f(1,1,1));
        List<Light> lights = new ArrayList<>();
        lights.add(light);

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();

        for (int i = 0; i < 0; i++) {
            float x = random.nextFloat() * 800 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -400;
            entities.add(new Entity(cubeModel, new Vector3f(x, y, z),
                    random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
        }

        for (int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 800 - 400;
            float y = 0;
            float z = random.nextFloat() * -600;
            entities.add(new Entity(treeModel, new Vector3f(x, y, z), 0, 0, 0, 10));
        }

        for (int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 800 - 400;
            float y = 0;
            float z = random.nextFloat() * -600;
            entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z), 0, 0, 0, 1));
        }

        for (int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 800 - 400;
            float y = 0;
            float z = random.nextFloat() * -600;
            entities.add(new Entity(grassModel, new Vector3f(x, y, z), 0, 0, 0, 1));
        }

        for (int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 800 - 400;
            float y = 0;
            float z = random.nextFloat() * -600;
            entities.add(new Entity(fernModel, new Vector3f(x, y, z), 0, 0, 0, 0.6f));
        }




        String heightMap = "heightmap";
        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, heightMap);
        Terrain terrain2 = new Terrain(0, -1, loader, texturePack, blendMap, heightMap);
        Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap, heightMap);
        Terrain terrain4 = new Terrain(-1, -1, loader, texturePack, blendMap, heightMap);
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain);
        terrains.add(terrain2);
        terrains.add(terrain3);
        terrains.add(terrain4);

        MasterRenderer renderer = new MasterRenderer();

        ModelData playerData = OBJFileLoader.loadOBJ("person");
        RawModel playerRawModel = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());
        TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(playerModel, new Vector3f(100, 0, -50), 0, 0,0, 1);
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
        WaterTile water = new WaterTile(60, 60, 5f);
        waters.add(water);

        GuiTexture refrGui = new GuiTexture(fbos.getRefractionTexture(), new Vector2f( 0.75f, -0.75f), new Vector2f(0.25f, 0.25f));
        GuiTexture reflGui = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.75f, -0.75f), new Vector2f(0.25f, 0.25f));
        guiTextures.add(refrGui);
        guiTextures.add(reflGui);

        // *****************Game Loop Below********************

        //boolean fullScreen = false;

        while (!Display.isCloseRequested()) {
            camera.move();
            player.move();

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
