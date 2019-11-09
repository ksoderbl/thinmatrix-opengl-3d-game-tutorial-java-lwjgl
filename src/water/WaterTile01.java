package water;

public class WaterTile01 implements WaterTile {

	public static final float TILE_SIZE = 60;

	private float height;
	private float x,z;
	
	public WaterTile01(float x, float z, float height) {
		this.x = x;
		this.z = z;
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
	
	public float getSize() {
		return TILE_SIZE;
	}
}
