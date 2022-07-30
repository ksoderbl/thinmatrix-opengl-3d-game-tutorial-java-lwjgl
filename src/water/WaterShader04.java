package water;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;

public class WaterShader04 extends ShaderProgram {

    private final static String VERTEX_FILE = "src/water/waterVertex04.glsl";
    private final static String FRAGMENT_FILE = "src/water/waterFragment04.glsl";

    private int location_transformationMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    
    private int location_skyColor;
    private int location_skyDensity;
    private int location_skyGradient;

    public WaterShader04() {
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
        location_transformationMatrix = getUniformLocation("transformationMatrix");
        
        location_reflectionTexture = getUniformLocation("reflectionTexture");
        location_refractionTexture = getUniformLocation("refractionTexture");
        
        location_skyColor = super.getUniformLocation("skyColor");
        location_skyDensity = super.getUniformLocation("skyDensity");
        location_skyGradient = super.getUniformLocation("skyGradient");
    }
    
    public void connectTextureUnits() {
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
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

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }
    
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix){
        loadMatrix(location_transformationMatrix, transformationMatrix);
    }

}
