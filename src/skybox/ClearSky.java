package skybox;

import org.lwjgl.util.vector.Vector3f;

public class ClearSky implements Sky {
    public static final float CLEAR_RED   = 0.2f;
    public static final float CLEAR_GREEN = 0.6f;
    public static final float CLEAR_BLUE  = 1.0f;
    
    public static final float CLEAR_DENSITY = 0.00015f;
    public static final float CLEAR_GRADIENT = 7f;
    
    public float density = CLEAR_DENSITY;
    public float gradient = CLEAR_GRADIENT;
    public float red = CLEAR_RED;
    public float green = CLEAR_GREEN;
    public float blue = CLEAR_BLUE;
    
    public ClearSky() {
    	
    }
    
    public ClearSky(float red, float green, float blue) {
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
