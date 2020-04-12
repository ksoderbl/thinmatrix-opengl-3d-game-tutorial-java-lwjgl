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

public class ParticleMaster34 {

	private static List<Particle34> particles = new ArrayList<Particle34>();
	private static ParticleRenderer34 renderer;
	
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer34(loader, projectionMatrix);
	}
	
	public static void update(Camera camera) {
		Iterator<Particle34> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle34 p = iterator.next();
			boolean stillActive = p.update(camera);
			if (!stillActive) {
				iterator.remove();
			}
		}
	}
	
	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);		
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle34 particle) {
		particles.add(particle);
	}
}
