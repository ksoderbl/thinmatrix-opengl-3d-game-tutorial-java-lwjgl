#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;
//in float visibility;

out vec4 out_Color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;

uniform float moveFactor;
uniform float waterReflectivity; // for fresnel effect
uniform vec3 lightColor;

uniform float waveStrength; // = 0.02
uniform float shineDamper;  // = 20.0, for normal map
uniform float reflectivity; // = 0.6.  for normal map

//uniform vec3 skyColor;


void main(void) {

    // ndc = normalized device coordinates
    vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0 + 0.5;
    vec2 refractTexCoords = vec2(ndc.x, ndc.y);
    vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);

    //vec2 distortion1 = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0;
    //distortion1 *= waveStrength;
    //vec2 distortion2 = texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y + moveFactor)).rg * 2.0 - 1.0;
    //distortion2 *= waveStrength;
    //vec2 totalDistortion = distortion1 + distortion2;

    //vec2 distortion1 = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0;
    vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
    distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y  + moveFactor);
    vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;

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

    vec4 normalMapColor = texture(normalMap, distortedTexCoords);
    vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
    vec3 unitNormal = normalize(normal);

    vec3 reflectedLight = reflect(normalize(fromLightVector), unitNormal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity;

	out_Color = mix(reflectColor, refractColor, refractiveFactor);
	// make water more blue
	out_Color = mix(out_Color, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0.0);

	//out_Color = normalMapColor;

	//out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
	//out_Color = vec4(0.0, 0.0, 1.0, 1.0);
}