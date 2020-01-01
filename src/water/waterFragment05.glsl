#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;

// Tutorial 16: Fog
in float visibility;

out vec4 out_Color;

// Tutorial 16: Fog
uniform vec3 skyColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

// OpenGL Water Tutorial 5: DuDv Maps
uniform sampler2D dudvMap;
uniform float moveFactor;
uniform float waveStrength;

void main(void) {

	// OpenGL Water Tutorial 4: Projective Texture Mapping at 7:50
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	
	// OpenGL Water Tutorial 5: DuDv Maps
	vec2 distortion1 = (texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0) * waveStrength;
	vec2 distortion2 = (texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y+moveFactor)).rg * 2.0 - 1.0) * waveStrength;
	vec2 totalDistortion = distortion1 + distortion2;
	
	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);

	out_Color = mix(reflectColor, refractColor, 0.5);
	
	// OpenGL Water Tutorial 5: DuDv Maps: add some blue to the mix
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2); 

	// Tutorial 16: Fog
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
