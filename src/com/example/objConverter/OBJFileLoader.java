package com.example.objConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.example.models.RawModel;
import com.example.renderEngine.Loader;

public class OBJFileLoader {

    private static final String RES_LOC = "res/";

    public static RawModel loadOBJ(String objFileName, Loader loader) {
        FileReader isr = null;
        String fileName = RES_LOC + objFileName + ".obj";
        File objFile = new File(fileName);
        System.out.println("OBJFileLoader: loading file: " + fileName);
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("OBJFileLoader: File not found: " + fileName);
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int vCount = 0;
        int vtCount = 0;
        int vnCount = 0;
        int fCount = 0;
        try {
            while (true) {
                line = reader.readLine();
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);
                    vCount++;

                } else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]));
                    textures.add(texture);
                    vtCount++;
                } else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    normals.add(normal);
                    vnCount++;
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                Vertex v0 = processVertex(vertex1, vertices, indices);
                Vertex v1 = processVertex(vertex2, vertices, indices);
                Vertex v2 = processVertex(vertex3, vertices, indices);
                calculateTangents(v0, v1, v2, textures);
                line = reader.readLine();
                fCount++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("OBJFileLoader: Error reading the file: " + fileName);
        }
        
        System.out.println("OBJFileLoader:"
                + " vertices: " + vCount
                + " textureCoords: " + vtCount
                + " normals: " + vnCount
                + " faces: " + fCount);
        
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float[] tangentsArray = new float[vertices.size() * 3];
        //float furthest =
                convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray, tangentsArray);
        int[] indicesArray = convertIndicesListToArray(indices);
        // ModelData data = new ModelData(verticesArray, texturesArray,
        // normalsArray, tangentsArray, indicesArray,
        // furthest);
        return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
    }

    // Changed scale() to mul()
    private static void calculateTangents(Vertex v0, Vertex v1, Vertex v2,
            List<Vector2f> textures) {
        // Vector3f deltaPos1 = Vector3f.sub(v1.getPosition(), v0.getPosition(), null);
        Vector3f deltaPos1 = new Vector3f(v1.getPosition());
        deltaPos1.sub(v0.getPosition());
        // Vector3f deltaPos2 = Vector3f.sub(v2.getPosition(), v0.getPosition(), null);
        Vector3f deltaPos2 = new Vector3f(v2.getPosition());
        deltaPos2.sub(v0.getPosition());

        Vector2f uv0 = textures.get(v0.getTextureIndex());
        Vector2f uv1 = textures.get(v1.getTextureIndex());
        Vector2f uv2 = textures.get(v2.getTextureIndex());
        
        // Vector2f deltaUv1 = Vector2f.sub(uv1, uv0, null);
        Vector2f deltaUv1 = new Vector2f(uv1);
        deltaUv1.sub(uv0);
        // Vector2f deltaUv2 = Vector2f.sub(uv2, uv0, null);
        Vector2f deltaUv2 = new Vector2f(uv2);
        deltaUv1.sub(uv0);

        float r = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);
        deltaPos1.mul(deltaUv2.y);
        deltaPos2.mul(deltaUv1.y);

        // Vector3f tangent = Vector3f.sub(deltaPos1, deltaPos2, null);
        Vector3f tangent = new Vector3f(deltaPos1);
        tangent.sub(deltaPos2);

        tangent.mul(r);
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }

    private static Vertex processVertex(String[] vertex, List<Vertex> vertices,
            List<Integer> indices) {
        
        //System.out.println("vertex[0] = " + vertex[0]);
        //System.out.println("vertex[1] = " + vertex[1]);
        //System.out.println("vertex[2] = " + vertex[2]);
        
        int index = Integer.parseInt(vertex[0]) - 1;
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }

    private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
            float[] normalsArray, float[] tangentsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            Vector3f tangent = currentVertex.getAverageTangent();
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;

        }
        return furthestPoint;
    }

    private static Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
            int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex,
                        newNormalIndex, indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }

        }
    }

    private static void removeUnusedVertices(List<Vertex> vertices) {
        for (Vertex vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}