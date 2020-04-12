#version 140

out vec4 out_color;

in vec2 textureCoords;

uniform sampler2D particleTexture;

void main(void)
{
	out_color = texture(particleTexture, textureCoords);
}
