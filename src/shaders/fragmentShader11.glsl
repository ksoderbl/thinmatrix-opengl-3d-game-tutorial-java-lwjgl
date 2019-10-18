#version 400 core

in vec3 color;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;

void main(void)
{
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.0);
	vec3 diffuse = brightness * lightColor;

	//out_Color = vec4(0.8, 0, 0, 1);
	//out_Color = vec4(color, 1.0);
	out_Color = vec4(diffuse, 1.0) * texture(modelTexture, pass_textureCoordinates);
}
