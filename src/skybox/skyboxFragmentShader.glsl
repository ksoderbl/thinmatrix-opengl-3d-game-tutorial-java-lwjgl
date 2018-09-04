#version 150

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 skyColor;

const float lowerLimit = 0.0;
// 300 (not 30) because the FAR_PLANE variables is 10000 and not 1000
const float upperLimit = 300.0;

const float levels = 8.0; // tutorial 30 cel shading

void main(void) {
        vec4 texture1 = texture(cubeMap, textureCoords);
        vec4 texture2 = texture(cubeMap2, textureCoords);
        vec4 finalColor = mix(texture1, texture2, blendFactor);

        // cel shading
        float amount = (finalColor.r + finalColor.g + finalColor.b) / 3.0;
        amount = floor(amount * levels) / levels;
        finalColor.rgb = amount * skyColor;

        float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
        factor = clamp(factor, 0.0, 1.0);
        out_Color = mix(vec4(skyColor, 1.0), finalColor, factor);
}
