#version 330 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec3 color;
out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
// Tutorial 16: Fog
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

// Tutorial 15: Transparency
uniform float useFakeLighting;
// Tutorial 16: Fog
uniform float skyDensity;
uniform float skyGradient;
// Tutorial 23: Texture Atlases
uniform float numberOfRows;
uniform vec2 textureOffset; 

//clip plane that clips at distance 4 from the xz-plane
//const vec4 plane = vec4(0, -1, 0, 4);
uniform vec4 clipPlane;

void main(void)
{
	vec4 worldPosition = transformationMatrix * vec4(position.xyz, 1.0);
	
	// positive clipdistance: vertex is not culled
	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	
	// Tutorial 23: Texture Atlases
	pass_textureCoordinates = (textureCoordinates/numberOfRows) + textureOffset;
	
	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		// y direction is straight up
		actualNormal = vec3(0.0, 1.0, 0.0);
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

	// Tutorial 16: Fog: distance of this vertex from camera
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * skyDensity), skyGradient));
	visibility = clamp(visibility, 0.0, 1.0);

	// pass position as color for testing	
	color = vec3(position.x, position.y, position.z);
}
