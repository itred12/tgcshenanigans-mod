#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform mat4 TextureMat;
uniform mat4 ProjMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform float GlintAlpha;
uniform float GameTime;
uniform vec3 CameraOffset;
uniform mat4 RotMat;


in vec4 glPos;
in vec4 texCoord0;
in vec2 uv0;

out vec4 fragColor;

vec2 sampleCube(vec3 v, int faceIndex) {
    vec3 vAbs = abs(v);
    float ma;
    vec2 uv;
    faceIndex = v.x < 0 ? 4 : 2;
    ma = 0.5 / vAbs.x;
    uv = vec2(v.x < 0.0 ? v.z : -v.z, -v.y);
    return uv * ma;
}


void main() {

    vec2 offset = vec2(-1, 1);
    int speed = 20;


    mat4 translate = mat4(
        1.0, 0.0, 0.0, (2.0 / 1.5) * (GameTime * speed * offset.x),
        0.0, 1.0, 0.0, (2.0 / 1.5) * (GameTime * speed * offset.y),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );



    mat2 scale = mat2((4 / 4.0) * 1);

    vec4 test = vec4(texCoord0.x, texCoord0.y, texCoord0.z, texCoord0.w);

    float near = 0.05;
    float far = (ProjMat[2][2] - 1)/(ProjMat[2][2] + 1);
    int faceIndex = 0;
    vec4 texPos = vec4(sampleCube(normalize((inverse(ProjMat * RotMat) * vec4(glPos.xy / glPos.w * (far - near), far + near, far - near)).xyz), 3), 0.0, 0.1);
    texPos = vec4(-texPos.x, texPos.y, texPos.z, texPos.w);

    vec4 color = textureProj(Sampler0,  texPos  );

    /*
    if (color.a < 0.1) {
        discard;
    }
    */

    fragColor = vec4(color.rgb, color.a);
}
