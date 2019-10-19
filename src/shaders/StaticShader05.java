package shaders;

public class StaticShader05 extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertexShader05.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader05.glsl";

    public StaticShader05() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		
	}
}
