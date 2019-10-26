package water;

import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

    //private static final int MAX_LIGHTS = 4;

	private final static String VERTEX_FILE = "src/water/waterVertex.glsl";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.glsl";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_waterTiling;
	private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_waveStrength;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_waterReflectivity;
    private int location_normalMap;
    private int location_lightColor;
    private int location_lightPosition;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_depthMap;
    private int location_nearPlane;
    private int location_farPlane;


	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_waterTiling = getUniformLocation("waterTiling");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_waveStrength = getUniformLocation("waveStrength");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
        location_waterReflectivity = getUniformLocation("waterReflectivity");
        location_normalMap = getUniformLocation("normalMap");
        location_lightColor = getUniformLocation("lightColor");
        location_lightPosition = getUniformLocation("lightPosition");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_depthMap = super.getUniformLocation("depthMap");
        location_nearPlane = super.getUniformLocation("nearPlane");
        location_farPlane = super.getUniformLocation("farPlane");

	}

	public void connectTextureUnits() {
	    super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
        super.loadInt(location_depthMap, 4);
    }

    public void loadShineVariables(float shineDamper, float reflectivity) {
        super.loadFloat(location_shineDamper, shineDamper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    // TODO: multiple lights
    public void loadLight(Light sun) {
	    super.loadVector(location_lightColor, sun.getColor());
        super.loadVector(location_lightPosition, sun.getPosition());
    }

    public void loadNearPlane(float nearPlane) {
	    super.loadFloat(location_nearPlane, nearPlane);
    }

    public void loadFarPlane(float farPlane) {
        super.loadFloat(location_farPlane, farPlane);
    }

    // 10.0: very reflective, 0.5: quite transparent
    public void loadWaterReflectivity(float reflectivity) {
        super.loadFloat(location_waterReflectivity, reflectivity);
    }

    public void loadWaterTiling(float factor) {
        super.loadFloat(location_waterTiling, factor);
    }

    public void loadMoveFactor(float factor) {
	    super.loadFloat(location_moveFactor, factor);
    }

    public void loadWaveStrength(float factor) {
        super.loadFloat(location_waveStrength, factor);
    }

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
