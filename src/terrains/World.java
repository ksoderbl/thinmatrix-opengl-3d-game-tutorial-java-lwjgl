package terrains;

import java.util.List;

public interface World {
	public float getHeightOfTerrain(float worldX, float worldZ);
	public float getHeightOfWater(float worldX, float worldZ);
	public List<Terrain> getTerrains();
}
