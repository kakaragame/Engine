#version 400

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

in vec2 outOverlayCoord;
in float[1] outHasTexture;

in vec2 outTexCoord;
in vec3 outVertexNormal;
in vec3 outVertexPos;
in vec4 mlightviewVertexPos;
in mat4 outModelViewMatrix;

out vec4 fragColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation att;
};

struct SpotLight
{
    PointLight pl;
    vec3 conedir;
    float cutoff;
};

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;

    sampler2D overlayTextures[5];
    int numberOfOverlays;
};

// Handles the settings for the fog
struct Fog
{
   int activeFog;
   vec3 color;
   float density;
};

uniform sampler2D textureAtlas;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;
uniform sampler2D shadowMap;

uniform Fog fog;

uniform float reflectance;


vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textCoord){
        ambientC = texture(textureAtlas, textCoord);
        diffuseC = texture(textureAtlas, textCoord);
        specularC = texture(textureAtlas, textCoord);
}


vec4 calcFog(vec3 pos, vec4 color, Fog fog, vec3 ambientLight, DirectionalLight dirLight);

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = specularC * light_intensity  * specularFactor * reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_color / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.pl.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec3 from_light_dir  = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 color = vec4(0, 0, 0, 0);

    if ( spot_alfa > light.cutoff )
    {
        color = calcPointLight(light.pl, position, normal);
        color *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
    }
    return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

vec4 overlayTexture(vec4 tex1, vec4 tex2){
    return vec4(
            tex2.x < 0.5 ? ( 2.0 * tex2.x * tex1.x ) : ( 1.0 - 2.0 * ( 1.0 - tex2.x ) * ( 1.0 - tex1.x ) ),
            tex2.y < 0.5 ? ( 2.0 * tex2.y * tex1.y ) : ( 1.0 - 2.0 * ( 1.0 - tex2.y ) * ( 1.0 - tex1.y ) ),
            tex2.z < 0.5 ? ( 2.0 * tex2.z * tex1.z ) : ( 1.0 - 2.0 * ( 1.0 - tex2.z ) * ( 1.0 - tex1.z ) ),
            tex2.a < 0.5 ? ( 2.0 * tex2.a * tex1.a ) : ( 1.0 - 2.0 * ( 1.0 - tex2.a ) * ( 1.0 - tex1.a ) )
        );
}

//Calculate the overlay textures for each block.
void calculateOverlayTextures()
{
    vec4 tempDiffuse = ambientC;
    if(outHasTexture[0] == 1){
        vec4 overlay = texture(textureAtlas, outOverlayCoord);
        //tempDiffuse = vec4(mix(tempDiffuse, overlay, overlay.a));

        tempDiffuse = overlay * overlay.a + tempDiffuse * (1.0 - overlay.a);
    }
    ambientC = tempDiffuse;
    specularC = tempDiffuse;
    diffuseC = tempDiffuse;
}

float calcShadow(vec4 position)
{
    vec3 projCoords = position.xyz;
        // Transform from screen coordinates to texture coordinates
        projCoords = projCoords * 0.5 + 0.5;
        float bias = 0.05;

        float shadowFactor = 0.0;
        vec2 inc = 1.0 / textureSize(shadowMap, 0);
        for(int row = -1; row <= 1; ++row)
        {
            for(int col = -1; col <= 1; ++col)
            {
                float textDepth = texture(shadowMap, projCoords.xy + vec2(row, col) * inc).r;
                shadowFactor += projCoords.z - bias > textDepth ? 1.0 : 0.0;
            }
        }
        shadowFactor /= 9.0;

        if(projCoords.z > 1.0)
        {
            shadowFactor = 1.0;
        }

        return 1 - shadowFactor;
}


void main()
{
    setupColors(material, outTexCoord);

    // Discard transparent pixels.
    if(ambientC.a < 0.1)
        discard;

    calculateOverlayTextures();

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, outVertexPos, outVertexNormal);

        for (int i=0; i<MAX_POINT_LIGHTS; i++)
        {
            if ( pointLights[i].intensity > 0 )
            {
                diffuseSpecularComp += calcPointLight(pointLights[i], outVertexPos, outVertexNormal);
            }
        }

        for (int i=0; i<MAX_SPOT_LIGHTS; i++)
        {
            if ( spotLights[i].pl.intensity > 0 )
            {
                diffuseSpecularComp += calcSpotLight(spotLights[i], outVertexPos, outVertexNormal);
            }
        }

        float shadow = calcShadow(mlightviewVertexPos);

        fragColor = clamp(ambientC * vec4(ambientLight, 1) + diffuseSpecularComp * shadow, 0, 1);

    if ( fog.activeFog == 1 )
    {
        fragColor = calcFog(outVertexPos, fragColor, fog, ambientLight, directionalLight);
    }

}

//Calculate the fog in the scene.
vec4 calcFog(vec3 pos, vec4 color, Fog fog, vec3 ambientLight, DirectionalLight dirLight)
{
    vec3 fogColor = fog.color * (ambientLight + dirLight.color * dirLight.intensity);
    float distance = length(pos);
    float fogFactor = 1.0 / exp( (distance * fog.density)* (distance * fog.density));
    fogFactor = clamp( fogFactor, 0.0, 1.0 );

    vec3 resultColour = mix(fogColor, color.xyz, fogFactor);
    return vec4(resultColour.xyz, 1);
}