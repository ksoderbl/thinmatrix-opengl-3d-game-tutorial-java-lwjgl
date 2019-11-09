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
		Terrain terrain1 = new Terrain22(0, 0, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain22(-1, 0, loader, texturePack, blendMap, "heightmap");
		Terrain terrain3 = new Terrain22(-1, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain4 = new Terrain22(0, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain1);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);

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
