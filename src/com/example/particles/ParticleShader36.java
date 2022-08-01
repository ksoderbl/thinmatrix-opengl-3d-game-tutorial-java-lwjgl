package particles;

import org.joml.Matrix4f;

import com.example.shaders.ShaderProgram;

public class ParticleShader36 extends ShaderProgram {

    private static final String VERTEX_FILE = "src/particles/particleVShader36.glsl";
    private static final String FRAGMENT_FILE = "src/particles/particleFShader36.glsl";

    private int location_numberOfRows;
    private int location_projectionMatrix;

    public ParticleShader36() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "texOffsets");
        super.bindAttribute(6, "blendFactor");
    }

    protected void loadNumberOfRows(float numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

}
