package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera13;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader13;

public class MasterRenderer13 {

    private StaticShader13 shader = new StaticShader13();
    private Renderer13 renderer = new Renderer13(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public void render(Light sun, Camera13 camera) {
        renderer.prepare();
        shader.start();
    	shader.loadLight(sun);
    	shader.loadViewMatrix(camera);
    	renderer.render(entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}
