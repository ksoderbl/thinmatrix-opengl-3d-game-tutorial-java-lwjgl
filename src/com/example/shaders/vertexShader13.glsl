#version 330 core

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec3 color;
out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(position.xyz, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoordinates = textureCoordinates;
    
    color = vec3(position.x, position.y, position.z);
    
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}
