#version 400 core

in vec2 pass_textureCoordinates;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void)
{
    /*
    if (pass_textureCoordinates.x * pass_textureCoordinates.x + pass_textureCoordinates.y > 1) {
        out_Color = texture(textureSampler, pass_textureCoordinates);
    }
    else {
        out_Color = vec4(color, 1.0);
    }
    */
    //out_Color = vec4(0.8, 0, 0, 1);
    //out_Color = vec4(color, 1.0);
    out_Color = texture(textureSampler, pass_textureCoordinates);
}
