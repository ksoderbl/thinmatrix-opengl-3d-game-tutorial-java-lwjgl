package terrains;

import java.util.ArrayList;
import java.util.List;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class World22 implements World {
	
	List<Terrain> terrains;
	
	public World22(Loader loader) {
		
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
        
        for (int x = -4; x < 4; x++) {
            for (int z = -4; z < 4; z++) {
        		Terrain terrain = new Terrain22(x, z, loader, texturePack, blendMap, "heightmap");
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
	
	public List<Terrain> getTerrains() {
		return terrains;
	}
}
