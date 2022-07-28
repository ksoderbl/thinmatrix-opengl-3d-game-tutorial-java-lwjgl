package com.example.shaders;

public class StaticShader05 extends ShaderProgram {

    private static final String VERTEX_FILE = "src/com/example/shaders/vertexShader05.glsl";
    private static final String FRAGMENT_FILE = "src/com/example/shaders/fragmentShader05.glsl";

    public StaticShader05() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        
    }
}
