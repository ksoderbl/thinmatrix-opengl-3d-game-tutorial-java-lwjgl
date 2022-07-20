package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import renderEngine.Loader;
import entities.Camera;

public class ParticleMaster35 {

    private static Map<ParticleTexture35, List<Particle35>> particles = new HashMap<ParticleTexture35, List<Particle35>>();
    private static ParticleRenderer35 renderer;
    
    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer35(loader, projectionMatrix);
    }
    
    public static void update(Camera camera) {
        Iterator<Entry<ParticleTexture35, List<Particle35>>> mapIterator = particles.entrySet().iterator();
        while (mapIterator.hasNext()) {
            List<Particle35> list = mapIterator.next().getValue();
            Iterator<Particle35> iterator = list.iterator();
            while (iterator.hasNext()) {
                Particle35 p = iterator.next();
                boolean stillActive = p.update(camera);
                if (!stillActive) {
                    iterator.remove();
                    if (list.isEmpty()) {
                        mapIterator.remove();
                    }
                }
            }
            InsertionSort35.sortHighToLow(list);
        }
    }
    
    public static void renderParticles(Camera camera) {
        renderer.render(particles, camera);        
    }
    
    public static void cleanUp() {
        renderer.cleanUp();
    }
    
    public static void addParticle(Particle35 particle) {
        List<Particle35> list = particles.get(particle.getTexture());
        if (list == null) {
            list = new ArrayList<Particle35>();
            particles.put(particle.getTexture(), list);
        }
        list.add(particle);
    }
}
