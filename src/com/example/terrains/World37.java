package terrains;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterTile;
import water.WaterTile32;

public class World37 implements World {

    float waterHeight;
    float waterSize;
    float terrainSize;
    
    float xSize;
    float zSize;
    
    int terrainVertexCount;
    
    List<Terrain> terrains = new ArrayList<>();
    List<WaterTile> waterTiles = new ArrayList<>();
    
    public World37(Loader loader, int xTiles, int zTiles, float terrainSize, float terrainMaxHeight, int terrainVertexCount, float waterSize) {
        
        // *********TERRAIN TEXTURE STUFF**********
        
        xSize = terrainSize * xTiles;
        zSize = terrainSize * zTiles;

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        //TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("rockDiffuse"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapLake"));
        
        this.terrainSize = terrainSize;
        this.terrainVertexCount = terrainVertexCount;

        // tiles are assumed to be square
        float waterOffset = (terrainSize - waterSize) * 0.5f;
        //this.waterHeight = waterHeight;
        this.waterSize = waterSize;
        
        for (int x = 0; x < xTiles; x++) {
            for (int z = 0; z < zTiles; z++) {
                Terrain terrain = new Terrain37(x, z, terrainSize, terrainMaxHeight, loader, texturePack, blendMap, "heightMapLake", terrainVertexCount);
                terrains.add(terrain);
                // center the water tile on the terrain tile
                waterHeight = terrain.getHeightOfWater();
                System.out.println("===> waterHeight = " + waterHeight);
                WaterTile water = new WaterTile32(x * terrainSize + waterOffset, z * terrainSize + waterOffset, waterHeight, waterSize);
                waterTiles.add(water);
            }
        }
        
        System.out.println("" + this.getClass().getName() + ": generated " + terrains.size() + " terrains.");
        
    }
    
    public float getHeightOfTerrain(float worldX, float worldZ) {
        float height = 0;
        Terrain terrain = getTerrain(worldX, worldZ);
    
        // if we got a terrain, get terrain height
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(worldX, worldZ);
        }
        
        //System.out.println("" + this.getClass().getName() + ": getHeightOfTerrain: (" + worldX + ", " + worldZ + "), height " + height);
        
        return height;
    }
    
    // return a point in space that is at worldX, worldZ, at yOffset units above the terrain
    public Vector3f getTerrainPoint(float worldX, float worldZ, float yOffset) {
        float y = getHeightOfTerrain(worldX, worldZ) + yOffset;
        return new Vector3f(worldX, y, worldZ);
    }
    
    public float getHeightOfWater(float worldX, float worldZ) {
        return waterHeight;
    }
    
    public List<Terrain> getTerrains() {
        return terrains;
    }
    
    public Terrain getTerrain(float worldX, float worldZ) {
        // this could be optimized with a hash table
        for (int i = 0; i < terrains.size(); i++) {
            Terrain terrain = terrains.get(i);
            if (terrain.containsPosition(worldX, worldZ)) {
                return terrain;
            }
        }
        return null;
    }
    
    public List<WaterTile> getWaterTiles() {
        return waterTiles;
    }
    
    public float getTerrainSize() {
        return terrainSize;
    }
    
    public float getXSize() {
        return xSize;
    }
    
    public float getZSize() {
        return zSize;
    }
}
