package particles;

public class ParticleTexture {

    private int textureID;
    private int numberOfRows;
    boolean additive;
    
    public ParticleTexture(int textureID, int numberOfRows, boolean additive) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    public boolean usesAdditiveBlending() {
        return additive;
    }
    
    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
