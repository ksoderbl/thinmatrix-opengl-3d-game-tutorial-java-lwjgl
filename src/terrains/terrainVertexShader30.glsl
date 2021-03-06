#version 330 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec3 color;
out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
// OpenGL 3D Game Tutorial 25: Multiple Lights
out vec3 toLightVector[4];
out vec3 toCameraVector;
// Tutorial 16: Fog
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
// OpenGL 3D Game Tutorial 25: Multiple Lights
uniform vec3 lightPosition[4];

// Tutorial 16: Fog
uniform float skyDensity;
uniform float skyGradient;

// OpenGL Water Tutorial 3: Clipping Planes
uniform vec4 clipPlane;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position.xyz, 1.0);
	
	// positive clipdistance: vertex is not culled
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	// tiling factor removed in Tutorial 17: Multitexturing
	pass_textureCoordinates = textureCoordinates;
	
	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
	
	for (int i = 0; i < 4; i++) {
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	// Tutorial 16: Fog: distance of this vertex from camera
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * skyDensity), skyGradient));
	visibility = clamp(visibility, 0.0, 1.0);
	
	// pass position as color for testing	
	color = vec3(position.x, position.y, position.z);
}
