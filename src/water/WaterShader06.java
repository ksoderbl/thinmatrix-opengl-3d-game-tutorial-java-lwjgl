package water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;

public class WaterShader06 extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex06.glsl";
	private final static String FRAGMENT_FILE = "src/water/waterFragment06.glsl";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_waveStrength;
    private int location_tiling;
    private int location_moveFactor;
    private int location_cameraPosition;
	
    private int location_skyColor;
    private int location_skyDensity;
    private int location_skyGradient;

	public WaterShader06() {
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
		location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        location_dudvMap = getUniformLocation("dudvMap");
        location_waveStrength = getUniformLocation("waveStrength");
        location_tiling = getUniformLocation("tiling");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPosition = getUniformLocation("cameraPosition");
		
		location_skyColor = super.getUniformLocation("skyColor");
		location_skyDensity = super.getUniformLocation("skyDensity");
		location_skyGradient = super.getUniformLocation("skyGradient");
	}
	
	public void connectTextureUnits() {
	    super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
    }

    public void loadMoveFactor(float factor) {
	    super.loadFloat(location_moveFactor, factor);
    }
    
    public void loadTiling(float factor) {
        super.loadFloat(location_tiling, factor);
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

    public void loadSkyVariables(float density, float gradient) {
        super.loadFloat(location_skyDensity, density);
        super.loadFloat(location_skyGradient, gradient);
    }
    
    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(location_skyColor, skyColor);
    }
	
    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(location_skyColor, new Vector3f(r, g, b));
    }
}
