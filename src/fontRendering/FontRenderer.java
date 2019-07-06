package fontRendering;

import fontMeshCreator.GUIText;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}

	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare(){}
	
	private void renderText(GUIText text){}
	
	private void endRendering(){}

}
