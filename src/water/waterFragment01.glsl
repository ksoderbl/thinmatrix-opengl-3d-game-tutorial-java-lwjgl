#version 330 core

in vec2 textureCoords;

// Tutorial 16: Fog
in float visibility;

out vec4 out_Color;

// Tutorial 16: Fog
uniform vec3 skyColor;

void main(void) {

	out_Color = vec4(0.0, 0.0, 1.0, 1.0);

	// Tutorial 16: Fog
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
