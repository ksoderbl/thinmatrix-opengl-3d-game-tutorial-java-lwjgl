#version 140

in vec2 position;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main(void)
{
	textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}
