#version 400 core

in vec2 position;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main(void) {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}
