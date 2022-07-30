package water;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.example.entities.Camera;
import com.example.entities.Light;
import com.example.shaders.ShaderProgram;
import com.example.toolbox.Maths;

public class WaterShader08 extends ShaderProgram {
    
    private static final int MAX_LIGHTS = 4;

    private final static String VERTEX_FILE = "src/water/waterVertex08.glsl";
    private final static String FRAGMENT_FILE = "src/water/waterFragment08.glsl";

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
    private int location_normalMap;
    private int location_depthMap;
    
    private int location_lightColor[];
    private int location_lightPosition[];
    private int location_attenuation[];
    
    private int location_skyColor;
    private int location_skyDensity;
    private int location_skyGradient;

    public WaterShader08() {
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
        location_normalMap = getUniformLocation("normalMap");
        location_depthMap = getUniformLocation("depthMap");
        
        // OpenGL 3D Game Tutorial 25: Multiple Lights,
        // OpenGL 3D Game Tutorial 26: Point Lights
        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        
        location_skyColor = super.getUniformLocation("skyColor");
        location_skyDensity = super.getUniformLocation("skyDensity");
        location_skyGradient = super.getUniformLocation("skyGradient");
    }
    
    public void connectTextureUnits() {
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
        super.loadInt(location_depthMap, 4);
    }
    
    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColor[i], lights.get(i).getColor());
                super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            }
            else {
                super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
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
