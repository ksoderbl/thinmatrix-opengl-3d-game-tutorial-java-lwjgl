package terrains;

import java.util.List;
import org.lwjgl.util.vector.Vector3f;

public interface World {
	public float getHeightOfTerrain(float worldX, float worldZ);
	public Vector3f getTerrainPoint(float worldX, float worldZ, float yOffset);
	public float getHeightOfWater(float worldX, float worldZ);
	public List<Terrain> getTerrains();
	public Terrain getTerrain(float worldX, float worldZ);
}
