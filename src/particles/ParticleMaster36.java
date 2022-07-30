package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import renderEngine.Loader;
import entities.Camera;

public class ParticleMaster36 {

    private static Map<ParticleTexture36, List<Particle36>> particles = new HashMap<ParticleTexture36, List<Particle36>>();
    private static ParticleRenderer36 renderer;
    
    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer36(loader, projectionMatrix);
    }
    
    public static void update(Camera camera) {
        Iterator<Entry<ParticleTexture36, List<Particle36>>> mapIterator = particles.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Entry<ParticleTexture36, List<Particle36>> entry = mapIterator.next();
            List<Particle36> list = entry.getValue();
            Iterator<Particle36> iterator = list.iterator();
            while (iterator.hasNext()) {
                Particle36 p = iterator.next();
                boolean stillActive = p.update(camera);
                if (!stillActive) {
                    iterator.remove();
                    if (list.isEmpty()) {
                        mapIterator.remove();
                    }
                }
            }
            if (!entry.getKey().isAdditive()) {
                InsertionSort36.sortHighToLow(list);
            }
        }
    }
    
    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);        
    }
    
    public static void cleanUp() {
        renderer.cleanUp();
    }
    
    public static void addParticle(Particle36 particle) {
        List<Particle36> list = particles.get(particle.getTexture());
        if (list == null) {
            list = new ArrayList<Particle36>();
            particles.put(particle.getTexture(), list);
        }
        list.add(particle);
    }
}
