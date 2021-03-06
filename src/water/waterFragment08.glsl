#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector[4];

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

// OpenGL Water Tutorial 6: Fresnel Effect
const float waterReflectivity = 2.0;

// OpenGL Water Tutorial 7: Normal Maps
uniform sampler2D normalMap;

// OpenGL 3D Game Tutorial 25: Multiple Lights
uniform vec3 lightColor[4];
// OpenGL 3D Game Tutorial 26: Point Lights
uniform vec3 attenuation[4];

// OpenGL Water Tutorial 8: Soft Edges (FINAL)
//const float shineDamper = 20.0;
//const float reflectivity = 0.5;

const float shineDamper = 200.0;
const float reflectivity = 10;

// OpenGL Water Tutorial 8: Soft Edges (FINAL)
uniform sampler2D depthMap;


void main(void) {

	// OpenGL Water Tutorial 4: Projective Texture Mapping at 7:50
	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	
	// OpenGL Water Tutorial 8: Soft Edges (FINAL)
	// These have to be the same as in the MasterRenderer
	// TODO: make uniform variables
	float near = 0.1;
	float far = 100000.0;
	// depth info in r
	float depth = texture(depthMap, refractTexCoords).r;
	float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	
	depth = gl_FragCoord.z;
	float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	float waterDepth = floorDistance - waterDistance;
	
	// OpenGL Water Tutorial 5: DuDv Maps
	//vec2 distortion1 = (texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0) * waveStrength;
	//vec2 distortion2 = (texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y+moveFactor)).rg * 2.0 - 1.0) * waveStrength;
	//vec2 totalDistortion = distortion1 + distortion2;
	
	// OpenGL Water Tutorial 7: Normal Maps
	vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
	distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;
	
	// OpenGL Water Tutorial 8: Soft Edges (FINAL)
	totalDistortion *= clamp(waterDepth/20, 0.0, 1.0);  
	
	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);
	
	// OpenGL Water Tutorial 7: Normal Maps	
	vec4 normalMapColor = texture(normalMap, distortedTexCoords);
	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b * 3, normalMapColor.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	// OpenGL Water Tutorial 6: Fresnel Effect
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, normal);
	// 0.5: less reflective, 10: very reflective
	refractiveFactor = pow(refractiveFactor, waterReflectivity);
	
	vec3 totalSpecularHighlights = vec3(0.0);
	
	for (int i = 0; i < 4; i++) {
		// OpenGL 3D Game Tutorial 26: Point Lights
		// TODO: this was toLightVector, check if it is ok to use fromLightVector here, seems to work
		float distance = length(fromLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
	
		vec3 reflectedLight = reflect(normalize(fromLightVector[i]), normal);
		float specular = max(dot(reflectedLight, viewVector), 0.0);
		specular = pow(specular, shineDamper);
		totalSpecularHighlights = totalSpecularHighlights + lightColor[i] * specular * reflectivity / attFactor;
	}
	
	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	
	// OpenGL Water Tutorial 5: DuDv Maps: add some blue to the mix
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2);
	
	// OpenGL Water Tutorial 8: Soft Edges (FINAL)
	totalSpecularHighlights *= clamp(waterDepth/5, 0.0, 1.0); 
	
	// OpenGL Water Tutorial 7: Normal Maps	
	out_Color = out_Color + vec4(totalSpecularHighlights, 0.0);
	
	// OpenGL Water Tutorial 8: Soft Edges (FINAL)
	out_Color.a = clamp(waterDepth/50, 0.0, 1.0);

	// Tutorial 16: Fog
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
	
	//out_Color = normalMapColor;
	//out_Color = vec4(waterDepth/1000);
}
