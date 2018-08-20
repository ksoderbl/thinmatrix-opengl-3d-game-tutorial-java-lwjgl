#version 150

// outputs from vertexShader
//in vec3 color;
in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main() {
    //out_Color = vec4(color, 1.0);

    // unit surface normal - in world coordinates
    vec3 unitNormal = normalize(surfaceNormal);

    // unit vector to light - in world coordinates
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);

    float ambientBrightness = 0.2;
    float brightness = max(nDot1, ambientBrightness);
    vec3 diffuse = brightness * lightColor;

    // unit vector pointing from this fragment to the camera - in world coordinates
    vec3 unitVectorToCamera = normalize(toCameraVector);

    // unit vector from light - in world coordinates
    vec3 lightDirection = -unitLightVector;

    // light is reflected from the surface in this direction
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    // how much of the reflected light is going to the camera (before damping)
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    vec4 textureColor = texture(modelTexture, pass_textureCoordinates);
    if (textureColor.a < 0.5) {
        discard;
    }

    out_Color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}
