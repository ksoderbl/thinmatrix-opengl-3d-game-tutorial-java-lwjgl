#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

// these should be uniforms and passed in as a function of the font size used

// large size chars
const float width = 0.51;
const float edge = 0.02;

// medium size chars
//const float width = 0.5;
//const float edge = 0.1;

// small size chars
//const float width = 0.46;
//const float edge = 0.19;

const float borderWidth = 0.7;
const float borderEdge = 0.1;

const vec3 outlineColor = vec3(1.0, 0.0, 0.0);

void main(void) {
	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);
	
	out_color = vec4(overallColor, overallAlpha);
}
