package skybox;

import org.lwjgl.util.vector.Vector3f;

public class Sky {
    public static final float CLEAR_RED   = 0.2f;
    public static final float CLEAR_GREEN = 0.6f;
    public static final float CLEAR_BLUE  = 1.0f;
    public static final float CLEAR_DENSITY = 0.00015f;
    public static final float CLEAR_GRADIENT = 7f;

    // OpenGL 3D Game Tutorial 16: Fog
    public static final float FOG_RED   = 0.5f;
    public static final float FOG_GREEN = 0.7f;
    public static final float FOG_BLUE  = 0.9f;
    public static final float FOG_DENSITY = 0.001f;
    public static final float FOG_GRADIENT = 3.0f;
    
    public static final float HAZE_RED   = 0.6f;
    public static final float HAZE_GREEN = 0.7f;
    public static final float HAZE_BLUE  = 0.8f;
    public static final float HAZE_DENSITY = 0.0035f;
    public static final float HAZE_GRADIENT = 5f;
    
    public float density = CLEAR_DENSITY;
    public float gradient = CLEAR_GRADIENT;
    public float red = CLEAR_RED;
    public float green = CLEAR_GREEN;
    public float blue = CLEAR_BLUE;
    
    public Sky() {
        // default values
    }
    
    public Sky(float red, float green, float blue, float density, float gradient) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.density = density;
        this.gradient = gradient;
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
