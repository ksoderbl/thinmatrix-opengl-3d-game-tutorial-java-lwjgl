package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

public class ParticleShader35 extends ShaderProgram {

	private static final String VERTEX_FILE = "src/particles/particleVShader35.glsl";
	private static final String FRAGMENT_FILE = "src/particles/particleFShader35.glsl";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader35() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	protected void loadModelViewMatrix(Matrix4f modelViewMatrix) {
		super.loadMatrix(location_modelViewMatrix, modelViewMatrix);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
