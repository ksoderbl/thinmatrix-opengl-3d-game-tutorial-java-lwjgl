package com.example.fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.shaders.ShaderProgram;

public class FontShader33 extends ShaderProgram{

    private static final String VERTEX_FILE = "src/com/example/fontRendering/fontVertex33.glsl";
    private static final String FRAGMENT_FILE = "src/com/example/fontRendering/fontFragment33.glsl";
    
    private int location_color;
    private int location_translation;
    private int location_width;
    private int location_edge;
    private int location_borderWidth;
    private int location_borderEdge;
    private int location_offset;
    private int location_outlineColor;

    
    public FontShader33() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_color = super.getUniformLocation("color");
        location_translation = super.getUniformLocation("translation");
        location_width = super.getUniformLocation("width");
        location_edge = super.getUniformLocation("edge");
        location_borderWidth = super.getUniformLocation("borderWidth");
        location_borderEdge = super.getUniformLocation("borderEdge");
        location_offset = super.getUniformLocation("offset");
        location_outlineColor = super.getUniformLocation("outlineColor");
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

    protected void loadWidth(float width) {
        super.loadFloat(location_width, width);
    }

    protected void loadEdge(float edge) {
        super.loadFloat(location_edge, edge);
    }

    protected void loadBorderWidth(float borderWidth) {
        super.loadFloat(location_borderWidth, borderWidth);
    }

    protected void loadBorderEdge(float borderEdge) {
        super.loadFloat(location_borderEdge, borderEdge);
    }

    protected void loadOffset(Vector2f offset) {
        super.load2DVector(location_offset, offset);
    }
    
    protected void loadOutlineColor(Vector3f outlineColor) {
        super.loadVector(location_outlineColor, outlineColor);
    }
}
