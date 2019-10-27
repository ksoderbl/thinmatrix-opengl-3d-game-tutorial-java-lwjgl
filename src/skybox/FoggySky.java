package skybox;

import org.lwjgl.util.vector.Vector3f;

public class FoggySky implements Sky {
    public static final float FOG_RED   = 0.5f;
    public static final float FOG_GREEN = 0.7f;
    public static final float FOG_BLUE  = 0.9f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    public static final float FOG_DENSITY = 0.007f;
    public static final float FOG_GRADIENT = 1.5f;

	public float getRed() {
		return FOG_RED;
	}
	public float getGreen() {
		return FOG_GREEN;
	}
	public float getBlue() {
		return FOG_BLUE;
	}
	public float getDensity() {
		return FOG_DENSITY;
	}
	public float getGradient() {
		return FOG_GRADIENT;
	}
	public Vector3f getColor() {
		return new Vector3f(FOG_RED, FOG_GREEN, FOG_BLUE);
	}
}
