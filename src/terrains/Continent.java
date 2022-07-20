package terrains;

import java.util.Random;

public class Continent {
    
    private int xSize, ySize;
    private double heights[][];
    private double percentWater;
    private double waterHeight;
    private double heightLevels[];
    
    private double areaAtHeight[];
    
    private double sinTable[];
    
    private long atanCalls = 0;
    
    public Continent(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        clear();
    }

    // ySize and xSize are really the Size of the whole world map
    public Continent(int xSize, int ySize, double percentWater, int iterations, long seed) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.percentWater = percentWater;

        // +1 in ysize to have one extra row, so we don't write outside the array
        heights = new double[xSize][ySize+1];
        sinTable = new double[xSize * 2];
        for (int i = 0; i < 2 * xSize; i++) {
            sinTable[i] = (double) Math.sin(i * 2.0 * Math.PI / xSize);
        }
        
        heightLevels = new double[4];
    
        generate(iterations, seed);
    }
    
    public void printOut() {
        Random random = new Random();
        random.setSeed(System.nanoTime());
        
        int shift = 0;
        shift = random.nextInt(16 * xSize);
        
        for (int j = 0; j < ySize; j++) {
            for (int i = 0; i < xSize; i++) {
                double h = heights[(i+shift) % xSize][j];
                
                if (h <= waterHeight) {
                    System.out.print( " " );
                }
                else {
                    System.out.print( "#" );
                }
                
//                if (h <= heightLevels[0]) {        
//                    System.out.print( " " );
//                }
//                else if (h <= heightLevels[1]) {        
//                    System.out.print( "." );
//                }
//                else if (h <= heightLevels[2]) {        
//                    System.out.print( "-" );
//                }
//                else if (h <= heightLevels[3]) {        
//                    System.out.print( "#" );
//                }
//                else {
//                    System.out.print( "^" );
//                }
            }
            System.out.println();
        }
    }
    
    public void generate(int iterations, long seed) {
        clear();
        
        generateAtanTable();

        Random random = new Random();
        random.setSeed(seed);
        
        for (int x = 0; x < iterations; x++) {
            generateGreatCircle(random);
            //generateGreatCircle1(random);
            //generateRectangle(random);
        }
        
//        System.out.println("Max tanb = " + maxTanb);
//        System.out.println("Max log tanb = " + maxLogTanb);
//        System.out.println("Min arg = " + minArg);
//        System.out.println("Max arg = " + maxArg);
//        System.out.println("Atan calls = " + atanCalls);
        
        postProcessGreatCircles();
    }
    
    void generateRectangle(Random random) {
        int x1 = random.nextInt(xSize);
        int x2 = random.nextInt(xSize);
        int y1 = random.nextInt(ySize);
        int y2 = random.nextInt(ySize);
                
        if (x2 < x1) {
            x2 += xSize;
        }

        if (y2 < y1) {
            y2 += ySize;
        }

        for (int y = y1; y <= y2; y++) {
            int ay = y % ySize;
            for (int x = x1; x <= x2; x++) {
                int ax = x % xSize;
                heights[ax][ay] += 1.0;
            }
        }
    }
    
    public void generateGreatCircle1(Random random) {
        double increment = (random.nextBoolean() ? -1.0 : 1.0);
        double xf = random.nextFloat();
        double yf = random.nextFloat();
        double rotx = (xf - 0.5) * Math.PI;
        double roty = (yf - 0.5) * Math.PI;
        double beta = Math.acos(Math.cos(rotx) * Math.cos(roty));
        double tanBeta = Math.tan(beta);
        int x0 = (int) (xSize * (2.0 - yf));
        double oneOverPI = 1.0 / Math.PI;
        
         for (int x = 0; x < xSize; x++) {
            double sina = sinTable[x0 - x];
             //double sina = (double) Math.sin((x0 - x) * 2.0 * Math.PI / xSize);
        
            double tanb = sina * tanBeta;
            double b = Math.atan(tanb);

            double b2 = b * oneOverPI + 0.5;
            int y = (int) (ySize * b2 + 0.5);
            //int y2 = (int) Math.round(ySize * b2);
            //if (y - y2 != 0) {
            //    System.out.println("y = " + y + ", y2 = " + y2);
            //}
            heights[x][y] += increment;
        }
    }

    
    // determines scaling of argument, bigger number gives more precision
    // found these experimentally
    private int scale = 1000;
    private int offset = 50000;
    private int atanTableSize = 100000;

    private double oneOverPI = 1.0 / Math.PI;
    
    private double maxTanb = Double.MIN_VALUE;
    private double maxLogTanb = Double.MIN_VALUE;
    private int maxArg = Integer.MIN_VALUE;
    private int minArg = Integer.MAX_VALUE;
    
    private double atanTable[];
    
    public double atanTableLookup(int arg) {
        atanCalls++;
        
        if (arg < minArg) {
            minArg = arg;
        }
        if (arg > maxArg) {
            maxArg = arg;
        }
        if (arg < 0)
            arg = 0;
        if (arg > atanTableSize - 1)
            arg = atanTableSize - 1;
        return atanTable[arg];
    }

    public double atanExp(int arg) {
        if (arg < minArg) {
            minArg = arg;
        }
        if (arg > maxArg) {
            maxArg = arg;
        }

        double result;
        
        result = Math.atan(Math.exp((arg - offset) / (double)scale));

        return result;
    }

    
    public void generateAtanTable() {
        atanTable = new double[atanTableSize];
        
        for (int i = 0; i < atanTableSize; i++) {
            atanTable[i] = generateAtanExpTableValue(i);
        }
    }
    
    public double generateAtanExpTableValue(int arg) {
        double result;
        
        result = Math.atan(Math.exp((arg - offset) / (double) scale));

        return result;
    }
    
    public int calculateY(int sinInd, double tanBeta) {
        
        double sign = 1.0;
        double sina = sinTable[sinInd];
        double tanb = sina * tanBeta;
        
        // atan(x) = -atan(-x)
        if (tanb < 0) {
            tanb = -tanb;
            sign = -1.0;
        }
        
        if (tanb > maxTanb) {
            maxTanb = tanb;
        }
        
        // let's take ln here
        double logTanb = Math.log(tanb);
        if (logTanb > maxLogTanb) {
            maxLogTanb = logTanb;
        }
        
        
        // Max log tanb = 13.29960807768431
        if (logTanb > 15.0) {
            logTanb = 15.0;
        }
        
        int arg = (int)(logTanb * scale) + offset; // offset is supposed to make the value >= 0
        
        
//        System.out.println("arg          = "+arg);
//        System.out.println("logTanb*100 = "+(logTanb * 1000));
        
        
        // arctan result: -PI/2 -> PI/2
        //double b = sign * Math.atan(Math.exp(logTanb));
        //double b = sign * Math.atan(Math.exp(arg / 1000.0));
        //double b = sign * atanExp(arg);
        double b = sign * atanTableLookup(arg);
        
        // divide by PI, result: -0.5 -> 0.5
        double b2 = b * oneOverPI;
        
        // add 0.5, result 0 -> 1
        double b3 = b2 + 0.5;
        
        // multiply by ySize, result 0 -> ySize
        double b4 = b3 * ySize;
        
        // add 0.5, result 0.5 > ySize + 0.5
        double b5 = b4 + 0.5;
        
        // cast to int, result 0 -> ySize
        int y = (int) b5;
        
        return y;
    }
    
    public void generateGreatCircle(Random random) {
        double increment = (random.nextBoolean() ? -1.0 : 1.0);
        double xf = random.nextFloat();
        double yf = random.nextFloat();
        double rotx = (xf - 0.5) * Math.PI;
        double roty = (yf - 0.5) * Math.PI;
        double beta = Math.acos(Math.cos(rotx) * Math.cos(roty));
        double tanBeta = Math.tan(beta);
        int x0 = (int) (xSize * (2.0 - yf));
        
         for (int x = 0; x < xSize; x++) {
            int y = calculateY(x0 - x, tanBeta);
            heights[x][y] += increment;
        }
    }

    
    
    public void postProcessGreatCircles() {
        for (int x = 0; x < xSize; x++) {
            double height = heights[x][0];
            for (int y = 0; y < ySize; y++) {
                double tmp = heights[x][y];
                height += tmp;
                heights[x][y] = height;
            }
        }
    }
    
    public double getMaxHeight() {
        double maxHeight = Integer.MIN_VALUE;
        for (int j = 0; j < ySize; j++) {
            for (int i = 0; i < xSize; i++) {
                double h = heights[i][j];
                if (h > maxHeight) {
                    maxHeight = h;
                }
            }
        }
        return maxHeight;
    }

    public double getMinHeight() {
        double minHeight = Integer.MAX_VALUE;
        for (int j = 0; j < ySize; j++) {
            for (int i = 0; i < xSize; i++) {
                double h = heights[i][j];
                if (h < minHeight) {
                    minHeight = h;
                }
            }
        }
        return minHeight;
    }

    public void clear() {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                heights[x][y] = 0; 
            }
        }
    }
    
    public double getHeightAt(int x, int y) {
        if (x < 0 || y < 0 || x >= xSize || y >= ySize) {
            return 0;
        }
        return heights[x][y];
    }

    public double getWaterHeightAt(int x, int y) {
        return waterHeight;
    }

    // TODO: the continent could just have some overall offset (delta) value, which would
    // be incremented here
    public void raiseContinent(double delta) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                heights[x][y] += delta; 
            }
        }
    }
    
    // select the heights from whichever continent is higher, the continents are assumed
    // to be the same ySize and xSize
    public void joinContinents(Continent c2) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                double h2 = c2.getHeightAt(x, y);
                if (h2 > heights[x][y]) {
                    heights[x][y] = h2;
                }
            }
        }
    }
    


    void calculateHeightLevels() {
        double max = getMaxHeight();
        double min = getMinHeight();
        System.out.println("calculateHeightLevels: Min height = " + min);
        System.out.println("calculateHeightLevels: Max height = " + max);
        //waterLevel = (double)(min + (percentWater/100.0) * (max - min));
        
        heightLevels[0] = (double)(min + (0.70) * (max - min));
        heightLevels[1] = (double)(min + (0.72) * (max - min));
        heightLevels[2] = (double)(min + (0.74) * (max - min));
        heightLevels[3] = (double)(min + (0.90) * (max - min));
        
        //System.out.println("waterLevel = " + waterLevel);
        
        // Calculate area at each height level
        // Tile areas are scaled as a function of the latitude
        // where latitude goes from -PI/2 -> PI/2 and the
        // area factor is cos(latitude)
        int maxHeight = (int) getMaxHeight();
        areaAtHeight = new double[maxHeight+1];
        for (int h = 0; h <= maxHeight; h++) {
            areaAtHeight[h] = 0;
        }
        
        for (int y = 0; y < ySize; y++) {
            double latitude = Math.PI * 0.5 * ((y + 0.5 - (ySize / 2.0))) / (ySize / 2.0);
            double areaFactor = Math.cos(latitude);
            
//            System.out.println("areaFactor="+areaFactor);
            
            for (int x = 0; x < xSize; x++) {
                int h = (int)getHeightAt(x, y);
                areaAtHeight[h] += areaFactor;
            }
        }
        
//        for (int i = 0; i <= maxHeight; i++) {
//            System.out.println("areaAtHeight["+ i + "] = " + areaAtHeight[i]);
//        }
    }
    
    double calculateWaterLevel(double percentWater) {
        this.percentWater = percentWater;
        // calculate total area
        double totalArea = 0.0;
        int maxHeight = (int) getMaxHeight();
        for (int h = 0; h <= maxHeight; h++) {
            totalArea += areaAtHeight[h];
        }
        
        double neededWaterArea = (percentWater / 100.0) * totalArea;
        
        System.out.println("totalArea: " + totalArea);
        System.out.println("neededWaterArea: " + neededWaterArea);
        System.out.println("waterArea/totalArea: " + (neededWaterArea/totalArea));
        
        // start from 0 level and add up levels until we have enough area
        double waterArea = 0.0;
        
        this.waterHeight = 0.0;
        
        for (int h = 0; h <= maxHeight; h++) {
            waterArea += areaAtHeight[h];
            this.waterHeight = h;
            if (waterArea >= neededWaterArea) {
                break;
            }
        }
        
        System.out.println("waterHeight: " + waterHeight);
        System.out.println("waterArea: " + waterArea);
        
        return this.waterHeight;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

}
