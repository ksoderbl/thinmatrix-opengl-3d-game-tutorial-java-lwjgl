#version 140

in vec2 position;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main(void) {
	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords1 = textureCoords;
	textureCoords2 = textureCoords;
	
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}
