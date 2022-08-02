#version 400 core

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

// Tutorial 16: Fog
uniform float skyDensity;
uniform float skyGradient;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(position.xyz, 1.0);
    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    
    // tiling factor removed in Tutorial 17: Multitexturing
    pass_textureCoordinates = textureCoordinates;
    
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
    
    // Tutorial 16: Fog: distance of this vertex from camera
    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * skyDensity), skyGradient));
    visibility = clamp(visibility, 0.0, 1.0);
    
    // pass position as color for testing    
    color = vec3(position.x, position.y, position.z);
}
