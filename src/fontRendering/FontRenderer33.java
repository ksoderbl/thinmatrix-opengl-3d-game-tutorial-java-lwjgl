package fontRendering;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.FontType33;
import fontMeshCreator.GUIText33;

public class FontRenderer33 {

	private FontShader33 shader;

	public FontRenderer33() {
		shader = new FontShader33();
	}

	public void render(Map<FontType33, List<GUIText33>> texts) {
		prepare();
		for (FontType33 font : texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			for (GUIText33 text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText33 text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		shader.loadWidth(0.5f);
		shader.loadEdge(0.1f);
		shader.loadBorderWidth(0.7f);
		shader.loadBorderEdge(0.1f);
		shader.loadOffset(new Vector2f(0.0f, 0.0f));
		shader.loadOutlineColor(new Vector3f(1.0f, 1.0f, 1.0f));
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void endRendering() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

}
