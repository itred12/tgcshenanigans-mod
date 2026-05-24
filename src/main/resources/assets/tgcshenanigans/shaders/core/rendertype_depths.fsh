#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform mat4 TextureMat;
uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 fragmentColor;

out vec4 fragColor;

// Basically identical to the enchantment glint shader, but we dont care about fog, I don't think

void main() {

    vec4 color = texture(Sampler0, texCoord0) * fragmentColor;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = color;
}
