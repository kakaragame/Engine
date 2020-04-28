#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;
layout (location=5) in mat4 modelViewInstancedMatrix;
layout (location=9) in mat4 modelLightViewInstancedMatrix;
layout (location=14) in float selectedInstanced;

out vec2 outTexCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out vec4 mlightviewVertexPos;
out mat4 outModelViewMatrix;
out float outSelected;

uniform mat4 orthoProjectionMatrix;
uniform int isInstanced;
uniform mat4 modelViewNonInstancedMatrix;
uniform mat4 modelLightViewNonInstancedMatrix;
uniform mat4 projectionMatrix;
uniform float selectedNonInstanced;

void main()
{
    vec4 initPos = vec4(0, 0, 0, 0);
    vec4 initNormal = vec4(0, 0, 0, 0);
    mat4 modelViewMatrix;
    mat4 lightViewMatrix;
    if ( isInstanced > 0 )
    {
        modelViewMatrix = modelViewInstancedMatrix;
        lightViewMatrix = modelLightViewInstancedMatrix;
        outSelected = selectedInstanced;

        initPos = vec4(position, 1.0);
        initNormal = vec4(vertexNormal, 0.0);
    }else{
        modelViewMatrix = modelViewNonInstancedMatrix;
        lightViewMatrix = modelLightViewNonInstancedMatrix;
        outSelected = selectedNonInstanced;
    }
    vec4 mvPos = modelViewMatrix * initPos;
    gl_Position = projectionMatrix * mvPos;
    outTexCoord = texCoord;
    mvVertexNormal = normalize(modelViewMatrix * initNormal).xyz;
    mvVertexPos = mvPos.xyz;
    mlightviewVertexPos = orthoProjectionMatrix * lightViewMatrix * initPos;
    outModelViewMatrix = modelViewMatrix;
}