#version 330 core

in vec4 clipSpace;
//in float visibility;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

//uniform vec3 skyColor;

void main(void) {

    // ndc = normalized device coordinates
    vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
    vec4 refractColor = texture(refractionTexture, refractTexCoords);

	out_Color = mix(reflectColor, refractColor, 0.5);

	//out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}