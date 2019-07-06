package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {
    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();
        TextMaster.init(loader);
        
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/fonts/candara.fnt"));
        GUIText text = new GUIText("A sample string of text!", 8, font, new Vector2f(0.0f, 0.0f), 1.0f, true);
        text.setColor(0.2f, 0.2f, 0.2f);

        // *********TERRAIN TEXTURE STUFF**********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadGameTexture("grassy2")); // was "grassy"
        TerrainTexture rTexture = new TerrainTexture(loader.loadGameTexture("mud")); // was "dirt"
        TerrainTexture gTexture = new TerrainTexture(loader.loadGameTexture("grassFlowers")); // was "pinkFlowers"
        TerrainTexture bTexture = new TerrainTexture(loader.loadGameTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
                rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadGameTexture("blendMapLake"));

        // *****************************************

        /*
        ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonRawModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
        TexturedModel dragonModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadGameTexture("stallTexture")));
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setShineDamper(10.0f);
        dragonTexture.setReflectivity(1.0f);
        Entity dragon = new Entity(dragonModel, new Vector3f(0, 0, -25), 0, 160,0, 1);


        ModelData pineData = OBJFileLoader.loadOBJ("pine");
        RawModel pineRawModel = loader.loadToVAO(pineData.getVertices(), pineData.getTextureCoords(), pineData.getNormals(), pineData.getIndices());
        TexturedModel pineModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadGameTexture("pine")));

        ModelData lowPolyTreeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel lowPolyTreeRawModel = loader.loadToVAO(lowPolyTreeData.getVertices(), lowPolyTreeData.getTextureCoords(), lowPolyTreeData.getNormals(), lowPolyTreeData.getIndices());
        TexturedModel lowPolyTreeModel = new TexturedModel(lowPolyTreeRawModel, new ModelTexture(loader.loadGameTexture("lowPolyTree")));

        ModelData bobbleTreeData = OBJFileLoader.loadOBJ("bobbleTree");
        RawModel bobbleTreeRawModel = loader.loadToVAO(bobbleTreeData.getVertices(), bobbleTreeData.getTextureCoords(), bobbleTreeData.getNormals(), bobbleTreeData.getIndices());
        TexturedModel bobbleTreeModel = new TexturedModel(bobbleTreeRawModel, new ModelTexture(loader.loadGameTexture("bobbleTree")));

        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel grassRawModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
        TexturedModel grassModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadGameTexture("grassTexture")));
        grassModel.getTexture().setHasTransparency(true);
        grassModel.getTexture().setUseFakeLighting(true);
        */

        TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocks", loader),
                new ModelTexture(loader.loadGameTexture("rocks")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadGameTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
                fernTextureAtlas);
        fern.getTexture().setHasTransparency(true);

        /*
        ModelData toonRocksData = OBJFileLoader.loadOBJ("toonRocks");
        RawModel toonRocksRawModel = loader.loadToVAO(toonRocksData.getVertices(), toonRocksData.getTextureCoords(), toonRocksData.getNormals(), toonRocksData.getIndices());
        TexturedModel toonRocksModel = new TexturedModel(toonRocksRawModel, new ModelTexture(loader.loadGameTexture("toonRocks")));

        String heightMap = "heightmap";
        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, heightMap);
        //Terrain terrain2 = new Terrain(0, 0, loader, texturePack, blendMap, heightMap);
        //Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap, heightMap);
        //Terrain terrain4 = new Terrain(-1, -1, loader, texturePack, blendMap, heightMap);
        List<Terrain> terrains = new ArrayList<>();
        */

        TexturedModel pine = new TexturedModel(OBJFileLoader.loadOBJ("pine", loader),
        		new ModelTexture(loader.loadGameTexture("pine")));
        pine.getTexture().setHasTransparency(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightMapLake");
        		List<Terrain> terrains = new ArrayList<>();
        terrains.add(terrain);
        //terrains.add(terrain2);
        //terrains.add(terrain3);
        //terrains.add(terrain4);

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
        		new ModelTexture(loader.loadGameTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);

        List<Entity> entities = new ArrayList<>();
        List<Entity> normalMapEntities = new ArrayList<>();

        //******************NORMAL MAP MODELS************************

        TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
                new ModelTexture(loader.loadGameTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadGameTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);

		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadGameTexture("crate")));
        crateModel.getTexture().setNormalMap(loader.loadGameTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
                new ModelTexture(loader.loadGameTexture("boulder")));
        boulderModel.getTexture().setNormalMap(loader.loadGameTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);

        //************ENTITIES*******************

        Entity entity = new Entity(barrelModel, new Vector3f(75, 0, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(95, 0, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(55, 0, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);

        Random random = new Random(5666778);
        for (int i = 0; i < 60; i++) {
            if (i % 3 == 0) {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, 0.9f));
                }
            }
            if (i % 2 == 0) {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x > 50 && x < 100) || (z < -50 && z > -100)) {
                } else {
                	float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(pine, 1, new Vector3f(x, y, z), 0,
                            random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                    }
            }
        }
        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));

        //*******************OTHER SETUP***************

        /*
        Random random = new Random(676452);

        for (int i = 0; i < 400; i++) {
            if (i % 1 == 0) {
                float x = random.nextFloat() * Terrain.SIZE;
                float z = random.nextFloat() * Terrain.SIZE;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, 0.9f));
            }

            if (i % 5 == 0) {
                float x, y, z;

                x = random.nextFloat() * Terrain.SIZE;
                z = random.nextFloat() * Terrain.SIZE;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(pineModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

                x = random.nextFloat() * Terrain.SIZE;
                z = random.nextFloat() * Terrain.SIZE;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(lowPolyTreeModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

                x = random.nextFloat() * Terrain.SIZE;
                z = random.nextFloat() * Terrain.SIZE;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grassModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.1f + 0.6f));

                x = random.nextFloat() * Terrain.SIZE;
                z = random.nextFloat() * Terrain.SIZE;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(toonRocksModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 2.0f + 1.0f));

                x = random.nextFloat() * Terrain.SIZE;
                z = random.nextFloat() * Terrain.SIZE;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobbleTreeModel, new Vector3f(x, y, z),
                        0, random.nextFloat() * 360, 0, random.nextFloat() * 0.5f + 0.5f));
            }
        }
        */

        /*
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(10000,10000,3000), new Vector3f(1.0f,1.0f,1.0f)));
        lights.add(new Light(new Vector3f(185,10,-293 + Terrain.SIZE), new Vector3f(2,0,0), new Vector3f(1,0.01f,0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300 + Terrain.SIZE), new Vector3f(0,2,2), new Vector3f(1,0.01f,0.002f)));
        //lights.add(new Light(new Vector3f(293,7,-305 + Terrain.SIZE), new Vector3f(2,2,0), new Vector3f(1,0.01f,0.002f)));

        ModelData lampData = OBJFileLoader.loadOBJ("lamp");
        RawModel lampRawModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
        TexturedModel lampModel = new TexturedModel(lampRawModel, new ModelTexture(loader.loadGameTexture("lamp")));
        lampModel.getTexture().setUseFakeLighting(true);

        entities.add(new Entity(lampModel, new Vector3f(185,-4.7f,-293 + Terrain.SIZE), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(370, 4.2f,-300 + Terrain.SIZE), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(293,-6.8f,-305 + Terrain.SIZE), 0, 0, 0, 1));
        */

        List<Light> lights = new ArrayList<>();
        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);

        MasterRenderer renderer = new MasterRenderer(loader);

        /*
        ModelData playerData = OBJFileLoader.loadOBJ("person");
        RawModel playerRawModel = loader.loadToVAO(playerData.getVertices(), playerData.getTextureCoords(), playerData.getNormals(), playerData.getIndices());
        TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadGameTexture("playerTexture")));
        Player player = new Player(playerModel, new Vector3f(Terrain.SIZE/2, 0, Terrain.SIZE/2), 0, 0,0, 0.5f);
        entities.add(player);
        */
        RawModel playerRawModel = OBJLoader.loadObjModel("person", loader);
        TexturedModel playerModel = new TexturedModel(playerRawModel, new ModelTexture(
                loader.loadGameTexture("playerTexture")));

        Player player = new Player(playerModel, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        entities.add(player);

        Camera camera = new Camera(player);

        List<GuiTexture> guiTextures = new ArrayList<>();
        /*
        GuiTexture gui = new GuiTexture(loader.loadGameTexture("socuwan"), new Vector2f(0.7f, 0.5f), new Vector2f(0.125f, 0.125f));
        GuiTexture gui2 = new GuiTexture(loader.loadGameTexture("thinmatrix"), new Vector2f(0.5f, 0.6f), new Vector2f(0.2f, 0.2f));
        */
        GuiTexture gui3 = new GuiTexture(loader.loadGameTexture("health"), new Vector2f(0.8f, 0.9f), new Vector2f(0.2f, 0.2f));
        /*
        guiTextures.add(gui);
        guiTextures.add(gui2);
        */
        guiTextures.add(gui3);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
        /*
        Entity lampEntity = new Entity(lampModel, new Vector3f(0, 0, 0), 0, 0, 0, 1);
        entities.add(lampEntity);
        Light light = new Light(new Vector3f(0, 14, 0), new Vector3f(3, 3, 0), new Vector3f(1,0.01f,0.002f));
        lights.add(light);
        Light sun = lights.get(0);
        */

        //**********Water Renderer Set-up************************

        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(),
                renderer.getNearPlane(), renderer.getFarPlane(), buffers);
        List<WaterTile> waters = new ArrayList<>();

        /*
        int maxWaterIndex = 3;
        for (int j = -maxWaterIndex; j <= maxWaterIndex; j++) {
            for (int i = -maxWaterIndex; i <= maxWaterIndex; i++) {
                WaterTile water = new WaterTile(
                        Terrain.SIZE / 2 + i * WaterTile.TILE_SIZE,
                        Terrain.SIZE / 2 + j * WaterTile.TILE_SIZE,
                        2f);
                waters.add(water);
            }
        }
        WaterTile water = waters.get(0);
        */
        WaterTile water = new WaterTile(75, -75, 0);
        waters.add(water);

        GuiTexture refrGui = new GuiTexture(buffers.getRefractionTexture(), new Vector2f( 0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        GuiTexture reflGui = new GuiTexture(buffers.getReflectionTexture(), new Vector2f(-0.8f, -0.8f), new Vector2f(0.2f, 0.2f));
        guiTextures.add(refrGui);
        guiTextures.add(reflGui);

        //****************Game Loop Below*********************

        //boolean fullScreen = false;

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

        int loops = 0;

        while (!Display.isCloseRequested()) {
            player.move(terrain, water); // TODO: find which terrain the player is on
            camera.move();

            picker.update();
            /*
            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
            if ((loops % 60) == 0) {
                System.out.println(terrainPoint);
            }
            */
            /*
            if (terrainPoint != null) {
                lampEntity.setPosition(terrainPoint);
                light.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 14, terrainPoint.z));
            }
            */

            entity.increaseRotation(0.1f, 0.2f, 0.3f);
            entity2.increaseRotation(0.3f, 0.1f, 0.2f);
            entity3.increaseRotation(0.2f, 0.3f, 0.1f);

            //render reflection texture
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            //renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+0.5f), true);
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 0.1f), true);
            camera.getPosition().y += distance;
            camera.invertPitch();

            //render refraction texture
            buffers.bindRefractionFrameBuffer();
            //renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()+0.5f), true);
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight() + 0.1f), true);
            buffers.unbindCurrentFrameBuffer();

            //render to screen
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 1000000), false);

            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);
            TextMaster.render();
            
            DisplayManager.updateDisplay();

            loops++;
        }

        //*********Clean Up Below**************

        TextMaster.cleanUp();
		buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
