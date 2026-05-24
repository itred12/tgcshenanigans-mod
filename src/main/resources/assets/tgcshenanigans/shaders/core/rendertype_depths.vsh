#version 150

in vec4 Color;
in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 TextureMat;

out vec2 texCoord0;
out vec4 fragmentColor;

// Slightly modified version of the enchantment glint shader

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1);

    // TextureMat is modified at runtime in TGCSRenderTypes to animate it.
    // That's combined with UV0 to paste it onto the face of the block.
    texCoord0 = (TextureMat * vec4(UV0, 1.0, 1.0)).xy;
    fragmentColor = Color;
}

