#version 400

in vec2 outTexCoord;
in vec3 vecNormal;
in vec3 vecPos;
// Position with view matrix added in.
in vec3 vecViewPos;

out vec4 fragColor;

struct Material
{
    sampler2D diffuse;
    sampler2D specular;
    sampler2D overlayTexture;

    // A max of 5 overlay textures.
    sampler2D overlayTextures[5];
    int numberOfOverlays;

    vec3 specularColor;
    int hasSpecularText;
    float reflectance;
};

// Handle directional lighting
struct DirLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

// Handle point lighting
struct PointLight{
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

// Handles spot lights.
struct SpotLight {
    vec3 position;
    vec3 direction;
    float cutOff;
    float outerCutOff;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

// Handles the settings for the fog
struct Fog
{
    int activeFog;
    vec3 color;
    float density;
};

uniform DirLight dirLight;
// The position of the camera.
uniform vec3 viewPos;
uniform Material material;

// Defined number of point lights.
#define NR_POINT_LIGHTS 20
// Defined number of spot lights.
#define NR_SPOT_LIGHTS 10
uniform PointLight pointLights[NR_POINT_LIGHTS];
uniform SpotLight spotLights[NR_SPOT_LIGHTS];

uniform Fog fog;


vec4 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec4 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir);
vec4 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir);
vec4 calcFog(vec3 pos, vec4 color, Fog fog, DirLight dirLight);

vec4 finalDiffuse;
vec4 finalSpecular;

void CalcDiffAndSpec(){
    vec4 tempDiffuse = texture(material.diffuse, outTexCoord);
    for(int i = 0; i < material.numberOfOverlays; i++){
        vec4 overlay = texture(material.overlayTextures[i], outTexCoord);
        tempDiffuse = mix(tempDiffuse, overlay, overlay.a);
    }
    finalDiffuse = tempDiffuse;
    vec4 tempSpecular;
    if(material.hasSpecularText == 1)
        tempSpecular = vec4(material.specularColor, 1.0);
    else
        tempSpecular = texture(material.specular, outTexCoord);
    // Skipping adding in the overlays for now.
   finalSpecular = tempSpecular;
}


void main()
{
    CalcDiffAndSpec();

    vec3 norm = normalize(vecNormal);
    vec3 viewDir = normalize(viewPos - vecPos);

    vec4 result = CalcDirLight(dirLight, norm, viewDir);
    for(int i = 0; i < NR_POINT_LIGHTS; i++){
         //Check to see if the light exists.
        if(pointLights[i].constant > 0) {
            result += CalcPointLight(pointLights[i], norm, vecPos, viewDir);
        }
    }
    for(int i = 0; i < NR_SPOT_LIGHTS; i++){
             //Check to see if the light exists.
            if(spotLights[i].constant > 0) {
                result += CalcSpotLight(spotLights[i], norm, vecPos, viewDir);
            }
        }

    fragColor = result;

     if ( fog.activeFog == 1 )
        {
            fragColor = calcFog(vecViewPos, fragColor, fog, dirLight);
        }
}

vec4 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.reflectance);
    // combine results
    vec4 ambient  = vec4(light.ambient, 1.0f)  * finalDiffuse;
    vec4 diffuse  = vec4(light.diffuse  * diff, 1.0f) * finalDiffuse;
    vec4 specular = vec4(light.specular * spec, 1.0f) * finalSpecular;
    return (ambient + diffuse + specular);
}

vec4 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.reflectance);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // combine results
    vec4 ambient  = vec4(light.ambient, 1.0f)  * finalDiffuse;
    vec4 diffuse  = vec4(light.diffuse * diff, 1.0f) * finalDiffuse;
    vec4 specular = vec4(light.specular * spec, 1.0f) * finalSpecular;
    ambient  *= attenuation;
    diffuse  *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

vec4 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.reflectance);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    // combine results
    vec4 ambient = vec4(light.ambient, 1.0f) * finalDiffuse;
    vec4 diffuse = vec4(light.diffuse * diff, 1.0f) * finalDiffuse;
    vec4 specular = vec4(light.specular * spec, 1.0f) * finalSpecular;
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);
}

//Calculate the fog in the scene.
vec4 calcFog(vec3 pos, vec4 color, Fog fog, DirLight dirLight)
{
    vec3 fogColor = fog.color * (dirLight.ambient);
    float distance = length(pos);
    float fogFactor = 1.0 / exp( (distance * fog.density)* (distance * fog.density));
    fogFactor = clamp( fogFactor, 0.0, 1.0 );

    vec3 resultColor = mix(fogColor, color.xyz, fogFactor);
    return vec4(resultColor.xyz, color.w);
}