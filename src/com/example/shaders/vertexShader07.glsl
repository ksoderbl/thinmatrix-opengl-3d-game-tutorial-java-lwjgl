#version 330 core

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

vec4 v1 = vec4(0.5, 0, 0, 0);
vec4 v2 = vec4(0.5, 0.5, 0, 0);
vec4 v3 = vec4(0, 0, 1, 0);
vec4 v4 = vec4(0, 0, 0, 1);

mat4 transformationMatrix = mat4(v1, v2, v3, v4);


void main() {
    gl_Position = transformationMatrix * vec4(position, 1.0);
    pass_textureCoords = textureCoords;
}
