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

uniform sampler2D modelTexture;
uniform vec3 lightColor[4];
// material
uniform float shineDamper;
uniform float reflectivity;
// Tutorial 16: Fog
uniform vec3 skyColor;

void main(void)
{
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < 4; i++) {
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormal, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + brightness * lightColor[i];
		totalSpecular = totalSpecular + dampedFactor * reflectivity * lightColor[i];
	}
	float ambientBrightness = 0.2;
	totalDiffuse = max(totalDiffuse, ambientBrightness);
	
	vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
	if (textureColor.a < 0.5) {
		// discard transparent parts of texture
		discard;
	}

	//out_Color = vec4(0.8, 0, 0, 1);
	//out_Color = vec4(color, 1.0);
	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	
	// Tutorial 16: Fog
	out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
