#version 330 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector[4];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;

// Water Tutorial 5: DuDv Maps
uniform float tiling;

// OpenGL Water Tutorial 7: Normal Maps
uniform vec3 lightPosition[4];

// Tutorial 16: Fog
out float visibility;

// Tutorial 16: Fog
uniform float skyDensity;
uniform float skyGradient;

void main(void)
{
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	clipSpace = projectionMatrix * positionRelativeToCam;

	gl_Position = clipSpace;

	textureCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
	toCameraVector = cameraPosition - worldPosition.xyz;
	for (int i = 0; i < 4; i++) {
		fromLightVector[i] = worldPosition.xyz - lightPosition[i];
	}
	
	// Tutorial 16: Fog: distance of this vertex from camera
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * skyDensity), skyGradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
