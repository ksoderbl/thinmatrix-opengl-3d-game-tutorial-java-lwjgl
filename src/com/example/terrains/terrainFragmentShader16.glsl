#version 400 core

in vec3 color;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
// Tutorial 16: Fog
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
// material
uniform float shineDamper;
uniform float reflectivity;
// Tutorial 16: Fog
uniform vec3 skyColor;

void main(void)
{
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
    
    vec4 textureColor = texture(modelTexture, pass_textureCoordinates);

    //out_Color = vec4(0.8, 0, 0, 1);
    //out_Color = vec4(color, 1.0);
    out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
        
    // Tutorial 16: Fog
    out_Color = mix(vec4(skyColor, 1), out_Color, visibility);
}
