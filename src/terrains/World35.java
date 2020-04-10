package terrains;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class World35 implements World {
	
	public static float WATER_HEIGHT = 0;
	
	List<Terrain> terrains;
	
	public World35(Loader loader) {
		
		// *********TERRAIN TEXTURE STUFF**********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2")); // was "grassy"
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud")); // was "dirt"
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers")); // was "pinkFlowers"
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
		        rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapLake"));
	
//        // *********TERRAIN TEXTURE STUFF**********
//
//        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
//        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
//        //TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
//        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("rockDiffuse"));
//        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256"));
//
//        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,
//                rTexture, gTexture, bTexture);
//        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
//        

		terrains = new ArrayList<Terrain>();
		
		Terrain terrain = new Terrain35(0, 0, loader, texturePack, blendMap, "heightMapLake");
		terrains.add(terrain);
		//terrains.add(terrain2);
		//terrains.add(terrain3);
		//terrains.add(terrain4);


        //		Terrain terrain1 = new Terrain22(0, 0, loader, texturePack, blendMap, "heightmap");
//		Terrain terrain2 = new Terrain22(-1, 0, loader, texturePack, blendMap, "heightmap");
//		Terrain terrain3 = new Terrain22(-1, -1, loader, texturePack, blendMap, "heightmap");
//		Terrain terrain4 = new Terrain22(0, -1, loader, texturePack, blendMap, "heightmap");
//		terrains.add(terrain1);
//		terrains.add(terrain2);
//		terrains.add(terrain3);
//		terrains.add(terrain4);

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
