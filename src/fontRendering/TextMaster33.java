package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderEngine.Loader;
import fontMeshCreator.FontType33;
import fontMeshCreator.GUIText33;
import fontMeshCreator.TextMeshData;

public class TextMaster33 {

    private static Loader loader;
    private static Map<FontType33, List<GUIText33>> texts = new HashMap<FontType33, List<GUIText33>>();
    private static FontRenderer33 renderer;
    
    public static void init(Loader theLoader) {
        renderer = new FontRenderer33();
        loader = theLoader;
    }
    
    public static void render() {
        renderer.render(texts);
    }
    
    public static void loadText(GUIText33 text) {
        FontType33 font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText33> textBatch = texts.get(font);
        if (textBatch == null) {
            textBatch = new ArrayList<GUIText33>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }
    
    public static void removeText(GUIText33 text) {
        List<GUIText33> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if (textBatch.isEmpty()) {
            texts.remove(text.getFont());
        }
    }
    
    public static void cleanUp() {
        renderer.cleanUp();
    }
}
