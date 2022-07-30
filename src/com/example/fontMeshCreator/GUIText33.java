package com.example.fontMeshCreator;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.fontRendering.TextMaster33;

public class GUIText33 {

    private String textString;
    private float fontSize;

    private int textMeshVao;
    private int vertexCount;
    private Vector3f color = new Vector3f(0f, 0f, 0f);
    private Vector2f position;
    private float width = 0.5f;
    private float edge = 0.1f;
    private float borderWidth = 0.7f;
    private float borderEdge = 0.1f;
    private Vector2f offset = new Vector2f(0.0f, 0.0f);
    private Vector3f outlineColor = new Vector3f(1f, 1f, 1f);
    
    private float lineMaxSize;
    private int numberOfLines;

    private FontType33 font;

    private boolean centerText = false;

    public GUIText33(String text, float fontSize, FontType33 font, Vector2f position, float maxLineLength,
            boolean centered) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        TextMaster33.loadText(this);
    }

    public void remove() {
        TextMaster33.removeText(this);
    }

    public FontType33 getFont() {
        return font;
    }

    public void setColor(float r, float g, float b) {
        color.set(r, g, b);
    }

    public Vector3f getColor() {
        return color;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void increasePosition(float dx, float dy) {
        this.position.x += dx;
        this.position.y += dy;
    }
    
    public Vector2f getPosition() {
        return position;
    }

    public int getMesh() {
        return textMeshVao;
    }

    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    protected float getFontSize() {
        return fontSize;
    }

    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    protected boolean isCentered() {
        return centerText;
    }

    protected float getMaxLineSize() {
        return lineMaxSize;
    }

    protected String getTextString() {
        return textString;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getEdge() {
        return edge;
    }

    public void setEdge(float edge) {
        this.edge = edge;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getBorderEdge() {
        return borderEdge;
    }

    public void setBorderEdge(float borderEdge) {
        this.borderEdge = borderEdge;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }

    public Vector3f getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(float r, float g, float b) {
        outlineColor.set(r, g, b);
    }
}
