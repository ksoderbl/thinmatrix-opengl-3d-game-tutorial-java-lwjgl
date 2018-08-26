#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
//in float visibility;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform float waveStrength;
uniform float moveFactor;
uniform float waterReflectivity;

//uniform vec3 skyColor;


void main(void) {

    // ndc = normalized device coordinates
    vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    vec2 distortion1 = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0;
    distortion1 *= waveStrength;
    vec2 distortion2 = texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y + moveFactor)).rg * 2.0 - 1.0;
    distortion2 *= waveStrength;
    vec2 totalDistortion = distortion1 + distortion2;

    float minTexCoord = 0.005;
    float maxTexCoord = 1.0 - minTexCoord;

    refractTexCoords += totalDistortion;
    refractTexCoords = clamp(refractTexCoords, minTexCoord, maxTexCoord);

    reflectTexCoords += totalDistortion;
    reflectTexCoords.x = clamp(reflectTexCoords.x, minTexCoord, maxTexCoord);
    reflectTexCoords.y = clamp(reflectTexCoords.y, -maxTexCoord, -minTexCoord);

    vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
    vec4 refractColor = texture(refractionTexture, refractTexCoords);

    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, vec3(0.0, 1.0, 0.0));
    refractiveFactor = pow(refractiveFactor, waterReflectivity);

	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	// make water more blue
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2);

	//out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}