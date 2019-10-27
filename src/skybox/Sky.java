package skybox;

import org.lwjgl.util.vector.Vector3f;

public interface Sky {
    //public static final float SKY_RED   = 0.6f;
    //public static final float SKY_GREEN = 0.7f;
    //public static final float SKY_BLUE  = 0.8f;
    
    // OpenGL 3D Game Tutorial 16: Fog
    // haze
    //public static final float SKY_DENSITY = 0.0035f;
    //public static final float SKY_GRADIENT = 5f;
    // fog
    //public static final float SKY_DENSITY = 0.007f;
    //public static final float SKY_GRADIENT = 1.5f;

    //public static final float SKY_DENSITY = 0.001f;
    //public static final float SKY_GRADIENT = 1.5f;
	
	public Vector3f getColor();
	public float getRed();
	public float getGreen();
	public float getBlue();
	public float getDensity();
	public float getGradient();
}
