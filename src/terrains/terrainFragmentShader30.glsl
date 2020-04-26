#version 330 core

in vec3 color;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
// OpenGL 3D Game Tutorial 25: Multiple Lights
in vec3 toLightVector[4];
in vec3 toCameraVector;
// Tutorial 16: Fog
in float visibility;

out vec4 out_Color;

// Tutorial 17: Multitexturing
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
//uniform sampler2D modelTexture;

// OpenGL 3D Game Tutorial 25: Multiple Lights
uniform vec3 lightColor[4];
// OpenGL 3D Game Tutorial 26: Point Lights
uniform vec3 attenuation[4];
// material
uniform float shineDamper;
uniform float reflectivity;
// Tutorial 16: Fog
uniform vec3 skyColor;

// Tutorial 30: Cel Shading
uniform float shadingLevels;

void main(void)
{
	// Tutorial 17: Multitexturing: non tiled textured coordinates in pass_textureCoordinates
	vec4 blendMapColor = texture(blendMap, pass_textureCoordinates);
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	float tilingFactor = 40.0;
	vec2 tiledCoords = pass_textureCoordinates * tilingFactor;
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);	
	
	for (int i = 0; i < 4; i++) {
			
		// OpenGL 3D Game Tutorial 26: Point Lights
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance); 
		
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		
		// Tutorial 30: Cel Shading
		float level = 0;
		if (shadingLevels > 0.1) {
			level = floor(brightness * shadingLevels);
			brightness = level / shadingLevels;
		}
		
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		
		// Tutorial 30: Cel Shading
		if (shadingLevels > 0.1) {
			level = floor(dampedFactor * shadingLevels);
			dampedFactor = level / shadingLevels;
		}
		
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attFactor;
	}
	
	float ambientBrightness = 0.2;
	totalDiffuse = max(totalDiffuse, ambientBrightness);
	
	//vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
	vec4 textureColor = totalColor;

	//out_Color = vec4(0.8, 0, 0, 1);
	//out_Color = vec4(color, 1.0);
	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
		
	// Tutorial 16: Fog
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
