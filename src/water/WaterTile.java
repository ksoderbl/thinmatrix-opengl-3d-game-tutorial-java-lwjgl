package water;

public class WaterTile {

	// tile scale, the 1:1 scale of the tile is 2x2
	public static final float TILE_SCALE = 60;
	public static final float TILE_SIZE = TILE_SCALE * 2;

	private float height;
	private float x,z;
	
	public WaterTile(float centerX, float centerZ, float height){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}



}
