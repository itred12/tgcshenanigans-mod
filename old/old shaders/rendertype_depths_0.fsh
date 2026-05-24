#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform mat4 TextureMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GlintAlpha;
uniform float GameTime;


in vec4 texCoord0;
in vec2 uv0;

out vec4 fragColor;

void main() {

    vec2 offset = vec2(-1, 1);
    int speed = 20;


    mat4 translate = mat4(
        1.0, 0.0, 0.0, (2.0 / 1.5) * (GameTime * speed * offset.x),
        0.0, 1.0, 0.0, (2.0 / 1.5) * (GameTime * speed * offset.y),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );



    mat2 scale = mat2((4 / 4.0) * 2);


    vec4 color = textureProj(Sampler0, texCoord0 * translate * mat4(scale) );
    /*
    if (color.a < 0.1) {
        discard;
    }
    */

    fragColor = vec4(color.rgb, color.a);
}
