package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;

// https://www.youtube.com/watch?v=z2yFlvkBbmk&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=3

public class Renderer05 {

    public void prepare() {
        GL11.glClearColor(1, 0, 0, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }
	
    public void render(RawModel model) {
    	GL30.glBindVertexArray(model.getVaoID());
    	GL20.glEnableVertexAttribArray(0);
    	// OpenGL 3D Game Tutorial 3: Rendering with Index Buffers
    	GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    	GL20.glDisableVertexAttribArray(0);
    	GL30.glBindVertexArray(0);
    }
}
