package terrains;

import java.util.ArrayList;
import java.util.List;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class World29 implements World {

	float waterHeight;
	
	List<Terrain> terrains;
	
	public World29(Loader loader, float terrainSize, float terrainMaxHeight, float waterHeight) {
		
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
        		Terrain terrain = new TerrainWater04(x, z, terrainSize, terrainMaxHeight, loader, texturePack, blendMap, "heightmap");
        		terrains.add(terrain);
            }
        }
        
        System.out.println("World29: generated " + terrains.size() + " terrains.");
        
        this.waterHeight = waterHeight;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float height = 0;
		Terrain terrain = getTerrain(worldX, worldZ);
	
		// if we got a terrain, get terrain height
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(worldX, worldZ);
		}
		
		//System.out.println("World29: getHeightOfTerrain: (" + worldX + ", " + worldZ + "), height " + height);
		
		return height;
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
}
