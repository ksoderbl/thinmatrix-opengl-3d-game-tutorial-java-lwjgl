package terrains;

import java.util.Random;

public class HeightsGenerator37 {

	private float amplitude = 70f;
	private Random random = new Random();
	private int seed;
	
	public HeightsGenerator37() {
		this.seed = random.nextInt(1000000000);
	}

	public float generateHeight (int x, int z) {
		return 1;
	}
	
}
