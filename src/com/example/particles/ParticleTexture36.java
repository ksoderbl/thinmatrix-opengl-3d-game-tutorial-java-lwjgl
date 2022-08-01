package particles;

public class ParticleTexture36 {

    private int textureID;
    private int numberOfRows;
    boolean additive;
    
    public ParticleTexture36(int textureID, int numberOfRows, boolean additive) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    public boolean isAdditive() {
        return additive;
    }
    
    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
