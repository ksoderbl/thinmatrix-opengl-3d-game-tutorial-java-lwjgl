package com.example.entities;

import com.example.models.TexturedModel;
import org.joml.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    // OpenGL 3D Game Tutorial 23: Texture Atlases
    // for 2x2:
    // 0 1
    // 2 3
    private int textureIndex = 0;

    // Entity with 0 rotation
    public Entity(TexturedModel model, Vector3f position, float scale) {
        this.model = model;
        this.position = position;
        this.scale = scale;
    }
    
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    // OpenGL 3D Game Tutorial 23: Texture Atlases
    // This constructor allows selecting the textureIndex to use, e.g. 0-3 for a 2x2 texture atlas.
    public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.textureIndex = textureIndex;
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    // OpenGL 3D Game Tutorial 23: Texture Atlases
    public float getTextureXOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        float offset = (float)column/(float)model.getTexture().getNumberOfRows();
        return offset;
    }

    // OpenGL 3D Game Tutorial 23: Texture Atlases
    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        float offset = (float)row/(float)model.getTexture().getNumberOfRows();
        return offset;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
