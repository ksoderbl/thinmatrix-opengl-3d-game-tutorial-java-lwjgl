#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main(void){

	out_Color = texture(guiTexture, vec2(textureCoords.x, textureCoords.y));

}