package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain37 implements Terrain {

    //public static final float SIZE = 20000; // Thinmatrix has 800
    //public static final float MAX_HEIGHT = 9000; // 40
    //private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    //private static final float HEIGHT_OFFSET = 0;

    private float x;
    private float z;
    private float size;
    private float maxHeight;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private int vertexCount;
    private int gridX;
    private int gridZ;
    private float waterHeight;
    
    // hard coded seed to get the same result every time
    private static final int SEED = 431; //new Random().nextInt(1000000000);

    private float[][] heights;

    public Terrain37(int gridX, int gridZ, float size, float maxHeight, Loader loader, TerrainTexturePack texturePack,
                   TerrainTexture blendMap, String heightMap, int vertexCount) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.size = size;
        this.maxHeight = maxHeight;
        this.x = gridX * size;
        this.z = gridZ * size;
        this.vertexCount = vertexCount;
        this.gridX = gridX;
        this.gridZ = gridZ;
        this.waterHeight = 0;
        
        long nanoTime1 = System.nanoTime();
        this.model = generateTerrain(loader, heightMap, vertexCount, maxHeight);
        long nanoTime2 = System.nanoTime();
        float delta = (nanoTime2 - nanoTime1) / 1e3f;
        
        System.out.println("Terrain37: generateTerrain took " + delta + " microseconds");
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
    
    public float getSize() {
    	return size;
    }
    
    public Vector3f getPosition() {
    	return new Vector3f(x, 0, z);
    }
    
    public RawModel getModel() {
        return model;
    }
    
    // uses texture pack, so can return null
    public ModelTexture getTexture() {
    	return null;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
    
    public float getHeightOfWater() {
    	return waterHeight;
    }
    
	public boolean containsPosition(float worldX, float worldZ) {
		if (worldX < x || worldX >= x + size)
			return false;
		if (worldZ < z || worldZ >= z + size)
			return false;
		return true;
	}

    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = size / ((float)heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        if (xCoord <= (1 - zCoord)) {
            answer = Maths.baryCentric(
                new Vector3f(0, heights[gridX][gridZ], 0),
                new Vector3f(1, heights[gridX + 1][gridZ], 0),
                new Vector3f(0, heights[gridX][gridZ + 1], 1),
                new Vector2f(xCoord, zCoord));
        } else {
            answer = Maths.baryCentric(
                new Vector3f(1, heights[gridX + 1][gridZ], 0),
                new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                new Vector3f(0, heights[gridX][gridZ + 1], 1),
                new Vector2f(xCoord, zCoord));
        }
        return answer;
    }

    private RawModel generateTerrain(Loader loader, String heightMap, int vertexCount, float maxHeight) {
    	
    	HeightsGenerator37 generator = new HeightsGenerator37(gridX, gridZ, vertexCount, SEED, maxHeight);
    	
        this.waterHeight = generator.getWaterHeight();

        BufferedImage image = null;
        String fileName = "res/" + heightMap + ".png";
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.err.println("Terrain: File not found: " + fileName);
            e.printStackTrace();
        }

        //int VERTEX_COUNT = image.getHeight();
        int VERTEX_COUNT = vertexCount;
         
        int count = VERTEX_COUNT * VERTEX_COUNT;
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];

        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * size;
                float height = getHeight(j, i, generator);
                vertices[vertexPointer * 3 + 1] = height;
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * size;
                Vector3f normal = calculateNormal(j, i, generator);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        
        generator.getInfo();
        
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, HeightsGenerator37 generator) {
        float heightL = getHeight(x-1, z, generator);
        float heightR = getHeight(x+1, z, generator);
        float heightD = getHeight(x, z-1, generator);
        float heightU = getHeight(x, z+1, generator);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, HeightsGenerator37 generator) {
        return generator.generateHeight(x, z);
    }

    
//    private float getHeight(int x, int z, BufferedImage image) {
//        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) {
//            return HEIGHT_OFFSET;
//        }
//        float height = image.getRGB(x, z);
//        height += MAX_PIXEL_COLOR / 2f;
//        height /= MAX_PIXEL_COLOR / 2f;
//        height *= maxHeight;
//        height += HEIGHT_OFFSET;
//        return height;
//    }
}
