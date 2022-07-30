#version 330 core

in vec4 clipSpace;

// Tutorial 16: Fog
in float visibility;

out vec4 out_Color;

// Tutorial 16: Fog
uniform vec3 skyColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main(void) {
    // OpenGL Water Tutorial 4: Projective Texture Mapping at 7:50
    vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
    vec4 refractColor = texture(refractionTexture, refractTexCoords);

    out_Color = mix(reflectColor, refractColor, 0.5);

    // Tutorial 16: Fog
    out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
