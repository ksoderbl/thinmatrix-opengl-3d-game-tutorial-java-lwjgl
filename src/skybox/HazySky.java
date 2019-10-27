package skybox;

import org.lwjgl.util.vector.Vector3f;

public class HazySky implements Sky {
    public static final float HAZE_RED   = 0.6f;
    public static final float HAZE_GREEN = 0.7f;
    public static final float HAZE_BLUE  = 0.8f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    // haze
    public static final float HAZE_DENSITY = 0.0035f;
    public static final float HAZE_GRADIENT = 5f;

	public float getRed() {
		return HAZE_RED;
	}
	public float getGreen() {
		return HAZE_GREEN;
	}
	public float getBlue() {
		return HAZE_BLUE;
	}
	public float getDensity() {
		return HAZE_DENSITY;
	}
	public float getGradient() {
		return HAZE_GRADIENT;
	}
	public Vector3f getColor() {
		return new Vector3f(HAZE_RED, HAZE_GREEN, HAZE_BLUE);
	}
}
