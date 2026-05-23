#version 150

in vec3 Position;
in vec2 UV0;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 TextureMat;
uniform int FogShape;
uniform float GameTime;
uniform vec3 ChunkOffset;

out vec4 glPos;
out vec4 texCoord0;
out vec2 uv0;


vec4 projection_from_position(vec4 position) {
    vec4 projection = position;
    projection.xy = vec2(projection.x + projection.w , projection.y + projection.w);
    //projection.zw = position.zw;

    return projection;
}

void main() {
    mat4 translate = mat4(
    1.0, 0.0, 0.0, 0.0,
    0.0, 1.0, 0, 0.0,
    0.0, 0.0, 1.0, 0.0,
    0, 0, 0.0, 1.0 + 1
    );

    mat4 test = ModelViewMat * translate;

    // Controls where the blocks themselves are rendered, overall unimportant
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1)  ;



    //vec4 translatedTex = (TextureMat * vec4(UV0, -1.0, -1.0));
    vec4 projectedTex = projection_from_position( gl_Position );

    texCoord0 = ProjMat * ModelViewMat * vec4(Position, 1);
    glPos = gl_Position;

    uv0 = UV0;


}

