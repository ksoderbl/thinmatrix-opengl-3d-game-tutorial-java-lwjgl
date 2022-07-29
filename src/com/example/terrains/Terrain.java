package com.example.terrains;

import org.joml.Vector3f;

import com.example.models.RawModel;
import com.example.textures.ModelTexture;
import com.example.textures.TerrainTexture;
import com.example.textures.TerrainTexturePack;

public interface Terrain {
    //public static final float SIZE = 800;
    
    //getSize();
    public float getX();
    public float getZ();
    public float getSize();
    public Vector3f getPosition();
    public RawModel getModel();
    public ModelTexture getTexture();
    public TerrainTexturePack getTexturePack();
    public TerrainTexture getBlendMap();
    public float getHeightOfTerrain(float worldX, float worldZ);
    public boolean containsPosition(float worldX, float worldZ);
    public float getHeightOfWater();
}
