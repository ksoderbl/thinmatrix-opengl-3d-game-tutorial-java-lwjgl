package skybox;

import org.lwjgl.util.vector.Vector3f;

public class FoggySky implements Sky {
    public static final float FOG_RED   = 0.5f;
    public static final float FOG_GREEN = 0.7f;
    public static final float FOG_BLUE  = 0.9f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    public static final float FOG_DENSITY = 0.0017f;
    public static final float FOG_GRADIENT = 1.5f;
    
    public float density = FOG_DENSITY;
    public float gradient = FOG_GRADIENT;
    public float red = FOG_RED;
    public float green = FOG_GREEN;
    public float blue = FOG_BLUE;
    
    public FoggySky() {
    	
    }
    
    public FoggySky(float red, float green, float blue) {
    	this.red = red;
    	this.green = green;
    	this.blue = blue;
    }

	public float getRed() {
		return red;
	}
	public float getGreen() {
		return green;
	}
	public float getBlue() {
		return blue;
	}
	public float getDensity() {
		return density;
	}
	public float getGradient() {
		return gradient;
	}
	public Vector3f getColor() {
		return new Vector3f(red, green, blue);
	}
}
