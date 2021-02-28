#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;
layout (location=5) in mat4 modelViewInstancedMatrix;
layout (location=9) in mat4 modelLightViewInstancedMatrix;

uniform int isInstanced;
uniform mat4 modelLightViewNonInstancedMatrix;
uniform mat4 orthoProjectionMatrix;

void main()
{
    vec4 initPos = vec4(0, 0, 0, 0);
    mat4 modelLightViewMatrix;
    if ( isInstanced > 0 )
    {
        modelLightViewMatrix = modelLightViewInstancedMatrix;
        initPos = vec4(position, 1.0);
    }
    else
    {
        modelLightViewMatrix = modelLightViewNonInstancedMatrix;
        initPos = vec4(position, 1.0);
    }
    gl_Position = orthoProjectionMatrix * modelLightViewMatrix * initPos;
}