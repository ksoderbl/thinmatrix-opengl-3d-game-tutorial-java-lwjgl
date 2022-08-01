package com.example.textures;

import java.nio.ByteBuffer;

import org.lwjgl.stb.STBImage;

public class TextureData {

    private int width;
    private int height;
    private ByteBuffer buffer;

    public TextureData(ByteBuffer buffer, int width, int height) {
        this.buffer = buffer;
        this.width = width;
        this.height = height;
    }

    // Added as optimization, so we don't need to copy the buffer.
    public void freeBuffer() {
        STBImage.stbi_image_free(buffer);
        buffer = null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
