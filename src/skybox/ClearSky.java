package skybox;

import org.lwjgl.util.vector.Vector3f;

public class ClearSky implements Sky {
    public static final float CLEAR_RED   = 0.2f;
    public static final float CLEAR_GREEN = 0.6f;
    public static final float CLEAR_BLUE  = 1.0f;
    
    public static final float CLEAR_DENSITY = 0.00015f;
    public static final float CLEAR_GRADIENT = 5f;

	public float getRed() {
		return CLEAR_RED;
	}
	public float getGreen() {
		return CLEAR_GREEN;
	}
	public float getBlue() {
		return CLEAR_BLUE;
	}
	public float getDensity() {
		return CLEAR_DENSITY;
	}
	public float getGradient() {
		return CLEAR_GRADIENT;
	}
	public Vector3f getColor() {
		return new Vector3f(CLEAR_RED, CLEAR_GREEN, CLEAR_BLUE);
	}
}
