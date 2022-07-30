package com.example.fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/com/example/fontRendering/fontVertex.glsl";
    private static final String FRAGMENT_FILE = "src/com/example/fontRendering/fontFragment.glsl";
    
    private int location_color;
    private int location_translation;
    
    public FontShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_translation = super.getUniformLocation("translation");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
    
    protected void loadColor(Vector3f color) {
        super.loadVector(location_color, color);
    }

    protected void loadTranslation(Vector2f translation) {
        super.load2DVector(location_translation, translation);
    }

}
