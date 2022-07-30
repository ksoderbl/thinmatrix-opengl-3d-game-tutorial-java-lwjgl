package terrains;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterTile;

public class World23 implements World {
    
    public static float WATER_HEIGHT = 1;
    
    List<Terrain> terrains;
    
    public World23(Loader loader) {
        
        // *********TERRAIN TEXTURE STUFF**********

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        //TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("rockDiffuse"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
                rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        
        terrains = new ArrayList<Terrain>();
        
        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                Terrain terrain = new Terrain23(x, z, loader, texturePack, blendMap, "heightmap");
                terrains.add(terrain);
            }
        }
        
        System.out.println("World: generated " + terrains.size() + " terrains.");
    }
    
    public float getHeightOfTerrain(float worldX, float worldZ) {
        float height = 0;
        Terrain terrain = null;

        // find which terrain we are standing on
        // this could be optimized with a hash table
        for (int i = 0; i < terrains.size(); i++) {
            Terrain t = terrains.get(i);
            if (t.containsPosition(worldX, worldZ)) {
                terrain = t;
                //System.out.println("getHeightOfTerrain: i = " + i);
                break;
            }
        }
        
        // if we got a terrain, get terrain height
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(worldX, worldZ);
        }
        
        //System.out.println("getHeightOfTerrain: (" + worldX + ", " + worldZ + "), height " + height);
        
        return height;
    }
    
    // return a point in space that is at worldX, worldZ, at yOffset units above the terrain
    public Vector3f getTerrainPoint(float worldX, float worldZ, float yOffset) {
        float y = getHeightOfTerrain(worldX, worldZ) + yOffset;
        return new Vector3f(worldX, y, worldZ);
    }
    
    public float getHeightOfWater(float worldX, float worldZ) {
        return WATER_HEIGHT;
    }
    
    public List<Terrain> getTerrains() {
        return terrains;
    }
    
    public Terrain getTerrain(float worldX, float worldZ) {
        return null;
    }
    
    // this class doesn't have handle water
    public List<WaterTile> getWaterTiles() {
        return null;
    }
    
    // not used
    public float getTerrainSize() {
        return 0;
    }
    
    public float getXSize() {
        return 0;
    }
    
    public float getZSize() {
        return 0;
    }

}
