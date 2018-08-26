package water;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

public class WaterShader extends ShaderProgram {

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
	}

	public void connectTextureUnits() {
	    super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
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
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
