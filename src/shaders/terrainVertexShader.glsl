#version 150

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

//out vec3 color;
out vec2 pass_textureCoordinates;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix; // objects translation, rotation and scaling in the world cooridinates
uniform mat4 projectionMatrix;     // frustum
uniform mat4 viewMatrix;           // camera
uniform vec3 lightPosition;

//const float fogDensity = 0.007;
//const float fogGradient = 1.5;
uniform float fogDensity;
uniform float fogGradient;

uniform vec4 plane;

void main() {
    //gl_Position = vec4(position.xyz, 1.0);
    //color = vec3(position.x + 0.5, 1.0, position.y + 0.5);
    //color = vec3(position.x + 0.5, 0.0, position.y + 0.5);

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_ClipDistance[0] = dot(worldPosition, plane);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;
    pass_textureCoordinates = textureCoordinates;

    // surface normal in world coordinates
    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    // vector from world position of light to world position of camera
    toLightVector = lightPosition - worldPosition.xyz;
    // vector from world position of fragment to world position of camera
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
