
#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;
layout (location=3) in vec2 overlayCoord;
layout (location=4) in float[1] hasTexture;

out float[1] outHasTexture;
out vec2 outOverlayCoord;
out vec2 outTexCoord;
out vec3 outVertexNormal;
out vec3 outVertexPos;
out vec4 mlightviewVertexPos;
out mat4 outModelViewMatrix;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 modelLightViewMatrix;
uniform mat4 orthoProjectionMatrix;

void main()
{
    vec4 pos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * pos;
    outTexCoord = texCoord;
    outVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    outVertexPos = pos.xyz;
    mlightviewVertexPos = orthoProjectionMatrix * modelLightViewMatrix * vec4(position, 1.0);
    outModelViewMatrix = modelViewMatrix;

    // For the overlay system.
    outHasTexture = hasTexture;
    outOverlayCoord = overlayCoord;
}