package terrains;

import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public interface Terrain {
    //public static final float SIZE = 800;
	
	//getSize();
    public float getX();
    public float getZ();
    public float getSize();
    public Vector3f getPosition();
    public RawModel getModel();
    public ModelTexture getTexture();
    public TerrainTexturePack getTexturePack();
    public TerrainTexture getBlendMap();
	public float getHeightOfTerrain(float worldX, float worldZ);
	public boolean containsPosition(float worldX, float worldZ);
    public float getHeightOfWater();
}
