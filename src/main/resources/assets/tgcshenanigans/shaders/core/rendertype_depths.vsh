#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 TextureMat;
uniform int FogShape;

out vec4 texCoord0;
out vec2 uv0;


vec4 projection_from_position(vec4 position, vec2 offset) {
    vec4 projection = position * 0.5;
    projection.xy = vec2(projection.x + projection.w, projection.y + projection.w);
    projection.zw = position.zw;

    return projection;
}

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1);

    //vec4 translatedTex = (TextureMat * vec4(UV0, -1.0, -1.0));
    vec4 projectedTex = projection_from_position(gl_Position, UV0);


    texCoord0 = projectedTex;
    uv0 = UV0;

}

