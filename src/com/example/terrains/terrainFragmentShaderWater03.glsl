#version 400 core

in vec3 color;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
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

uniform vec3 lightColor;
// material
uniform float shineDamper;
uniform float reflectivity;
// Tutorial 16: Fog
uniform vec3 skyColor;

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
    vec3 unitLightVector = normalize(toLightVector);
    
    float nDot1 = dot(unitNormal, unitLightVector);
    float ambientBrightness = 0.2;
    float brightness = max(nDot1, ambientBrightness);
    vec3 diffuse = brightness * lightColor;
    
    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;
    
    //vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
    vec4 textureColor = totalColor;

    //out_Color = vec4(0.8, 0, 0, 1);
    //out_Color = vec4(color, 1.0);
    out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
        
    // Tutorial 16: Fog
    out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
