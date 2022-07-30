package terrains;

import java.util.Random;

public class Worldmap {

    private int xSize, ySize;
    private int numContinents;
    private Continent[] continents;
    private Continent world;
    private double maxHeight = 0;
    private double minHeight = 0;
    private double wantedMaxHeight = 1;
    
    public Worldmap(int xSize, int ySize, int iterations, int numContinents, double percentWater, long seed1, long seed2, float wantedMaxHeight) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.numContinents = numContinents;
        this.wantedMaxHeight = wantedMaxHeight;
        
        System.out.println("xSize = " + xSize);
        System.out.println("ySize = " + ySize);
        System.out.println("iterations = " + iterations);
        System.out.println("wantedMaxHeight = " + wantedMaxHeight);
        
        continents = new Continent[numContinents];
                
        for (int i = 0; i < numContinents; i++) {
            System.out.println("generating continent " + (i + 1) + " of " + numContinents);
            continents[i] = new Continent(xSize, ySize, percentWater, iterations, seed1 + i * seed2);
        }
    
        // join generated continents to the world map
        Random random = new Random();
        random.setSeed(seed1 + numContinents * seed2);
        
        world = continents[0];
        double maxHeight0 = world.getMaxHeight();
        for (int i = 1; i < numContinents; i++) {
            double maxHeightI = continents[i].getMaxHeight();
            double delta = maxHeight0 - maxHeightI;
            // Let some continents highest peaks be higher than others
            delta += (random.nextInt(19) - 9);
            
            continents[i].raiseContinent(delta);
            
            world.joinContinents(continents[i]);
        }

        minHeight = world.getMinHeight();
        maxHeight = world.getMaxHeight();
        System.out.println("Min height = " + minHeight);
        System.out.println("Max height = " + maxHeight);
        
        // set minHeight to 0
        world.raiseContinent(-minHeight);

        minHeight = world.getMinHeight();
        maxHeight = world.getMaxHeight();
        System.out.println("New min height = " + minHeight);
        System.out.println("New max height = " + maxHeight);

        // TODO: calculate water level properly
        world.calculateHeightLevels();
        double waterHeight = world.calculateWaterLevel(percentWater);
        
        System.out.println("WaterHeight = " + waterHeight);
        
    }

    public void printOut() {
//        for (int i = 0; i < numContinents; i++) {
//            System.out.println("continent " + (i + 1) + " of " + numContinents + ":");
//            continents[i].printOut();
//        }
        world.printOut();
    }
    
    public double getHeightAt(int x, int y) {
        return world.getHeightAt(x, y) * (wantedMaxHeight/maxHeight);
    }

    public double getWaterHeightAt(int x, int y) {
        return world.getWaterHeightAt(x, y) * (wantedMaxHeight/maxHeight);
    }
    
    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }


    
}
