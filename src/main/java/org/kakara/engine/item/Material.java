package org.kakara.engine.item;

import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * The material to be used on meshes.
 */
public class Material {
    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Texture specularMap;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;

    private List<Texture> overlayTextures;

    public Material() {
        this.ambientColor = DEFAULT_COLOUR;
        this.diffuseColor = DEFAULT_COLOUR;
        this.specularColor = DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 32f;
        this.overlayTextures = new ArrayList<>();
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 32f);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    /**
     * Create a material
     * @param ambientColor The color of the object (RGBA)
     * @param diffuseColor The surrounding color (RGBA)
     * @param specularColor The color when the light is shined (RGBA)
     * @param texture The texture
     * @param reflectance The reflectance of the material.
     */
    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, Texture texture, float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        this.reflectance = reflectance;
        this.overlayTextures = new ArrayList<>();
    }

    /**
     * Set the ambient color of the material
     * @param ambientColor The ambient color (RGBA)
     */
    public void setAmbientColor(Vector4f ambientColor){
        this.ambientColor = ambientColor;
    }

    /**
     * Get the ambient color of the material.
     * @return The ambient color (RGBA)
     */
    public Vector4f getAmbientColor(){
        return ambientColor;
    }

    /**
     * Set the diffuse color of the material.
     * @param diffuseColor The diffuse color (RGBA)
     */
    public void setDiffuseColor(Vector4f diffuseColor){
        this.diffuseColor = diffuseColor;
    }

    /**
     * Get the diffuse color of the material.
     * @return The diffuse color of the material (RGBA)
     */
    public Vector4f getDiffuseColor(){
        return diffuseColor;
    }

    /**
     * Set the specular color of the material.
     * @param specularColor The specular color of the material (RGBA).
     */
    public void setSpecularColor(Vector4f specularColor){
        this.specularColor = specularColor;
    }

    /**
     * Get the specular color of the material
     * @return The specular color (RGBA)
     */
    public Vector4f getSpecularColor(){
        return specularColor;
    }

    /**
     * Set the reflectance.
     * @param reflectance The reflectance
     */
    public void setReflectance(float reflectance){
        this.reflectance = reflectance;
    }

    /**
     * Get the reflectance.
     * @return The reflectance
     */
    public float getReflectance(){
        return reflectance;
    }

    /**
     * If the material has a texture
     * @return If the material has a texture
     */
    public boolean isTextured() {
        return this.texture != null;
    }

    /**
     * Get the texture of the material.
     * @return
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Set the texture of the material.
     * @param texture
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Set the list of overlay textures.
     * <p>A maximum of 5 textures are allowed.</p>
     * @param overlayTextures The list
     * @throws Exception If the list exceeds 5 overlay textures.
     */
    public void setOverlayTextures(List<Texture> overlayTextures) throws Exception{
        if(overlayTextures.size() >= 5)
            throw new Exception("A maximum of 5 overlay textures only.");
        this.overlayTextures = overlayTextures;
    }

    /**
     * Add a texture to the list of overlay textures
     * @param overlayTexture The texture
     * @return True if successful, false if not. (Max limit of 5 overlay textures).
     */
    public boolean addOverlayTexture(Texture overlayTexture){
        if(overlayTextures.size() >= 5){
            return false;
        }
        overlayTextures.add(overlayTexture);
        return true;
    }

    /**
     * Get the list of overlay textures.
     * @return
     */
    public List<Texture> getOverlayTextures(){
        return overlayTextures;
    }


    /**
     * If the material has a normal map
     * <p><b>Not implemented yet</b></p>
     * @return If the material has a normal map
     */
    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

    /**
     * Get the texture of the normal map.
     * <p><b>Not implemented yet.</b></p>
     * @return The texture of the normal map.
     */
    public Texture getNormalMap() {
        return normalMap;
    }

    /**
     * Set the normal map
     * <p><b>Not implemented yet.</b></p>
     * @param normalMap The normal map texture
     */
    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }
}
