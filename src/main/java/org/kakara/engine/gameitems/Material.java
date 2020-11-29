package org.kakara.engine.gameitems;

import org.kakara.engine.utils.RGBA;

import java.util.ArrayList;
import java.util.List;

/**
 * The material to be used on meshes.
 */
public class Material {
    public static final RGBA DEFAULT_COLOUR = new RGBA(255, 255, 255, 1.0f);

    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    private RGBA ambientColor;
    private RGBA diffuseColor;
    private RGBA specularColor;

    private List<Texture> overlayTextures;

    /**
     * Create a material with the default values.
     */
    public Material() {
        this.ambientColor = DEFAULT_COLOUR;
        this.diffuseColor = DEFAULT_COLOUR;
        this.specularColor = DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 32f;
        this.overlayTextures = new ArrayList<>();
    }

    /**
     * Create a new material object.
     *
     * @param color       The color of the object.
     * @param reflectance The reflectance of the object.
     */
    public Material(RGBA color, float reflectance) {
        this(color, color, color, null, reflectance);
    }

    /**
     * Create a new material using a texture.
     *
     * @param texture The texture to use.
     */
    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 32f);
    }

    /**
     * Create a new material using a texture.
     *
     * @param texture     The texture to use.
     * @param reflectance The reflectance of the object.
     */
    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    /**
     * Create a material
     *
     * @param ambientColor  The color of the object (RGBA)
     * @param diffuseColor  The surrounding color (RGBA)
     * @param specularColor The color when the light is shined (RGBA)
     * @param texture       The texture
     * @param reflectance   The reflectance of the material.
     */
    public Material(RGBA ambientColor, RGBA diffuseColor, RGBA specularColor, Texture texture, float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        this.reflectance = reflectance;
        this.overlayTextures = new ArrayList<>();
    }

    /**
     * Get the ambient color of the material.
     *
     * @return The ambient color (RGBA)
     */
    public RGBA getAmbientColor() {
        return ambientColor;
    }

    /**
     * Set the ambient color of the material
     *
     * @param ambientColor The ambient color (RGBA)
     */
    public void setAmbientColor(RGBA ambientColor) {
        this.ambientColor = ambientColor;
    }

    /**
     * Get the diffuse color of the material.
     *
     * @return The diffuse color of the material (RGBA)
     */
    public RGBA getDiffuseColor() {
        return diffuseColor;
    }

    /**
     * Set the diffuse color of the material.
     *
     * @param diffuseColor The diffuse color (RGBA)
     */
    public void setDiffuseColor(RGBA diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    /**
     * Get the specular color of the material
     *
     * @return The specular color (RGBA)
     */
    public RGBA getSpecularColor() {
        return specularColor;
    }

    /**
     * Set the specular color of the material.
     *
     * @param specularColor The specular color of the material (RGBA).
     */
    public void setSpecularColor(RGBA specularColor) {
        this.specularColor = specularColor;
    }

    /**
     * Get the reflectance.
     *
     * @return The reflectance
     */
    public float getReflectance() {
        return reflectance;
    }

    /**
     * Set the reflectance.
     *
     * @param reflectance The reflectance
     */
    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    /**
     * If the material has a texture
     *
     * @return If the material has a texture
     */
    public boolean isTextured() {
        return this.texture != null;
    }

    /**
     * Get the texture of the material.
     *
     * @return The texture of the material.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Set the texture of the material.
     *
     * @param texture The texture to set the material to.
     */
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Add a texture to the list of overlay textures
     *
     * @param overlayTexture The texture
     * @return True if successful, false if not. (Max limit of 5 overlay textures).
     */
    public boolean addOverlayTexture(Texture overlayTexture) {
        if (overlayTextures.size() >= 5) {
            return false;
        }
        overlayTextures.add(overlayTexture);
        return true;
    }

    /**
     * Get the list of overlay textures.
     *
     * @return The list of overlay textures.
     */
    public List<Texture> getOverlayTextures() {
        return overlayTextures;
    }

    /**
     * Set the list of overlay textures.
     * <p>A maximum of 5 textures are allowed. IllegalArgumentException is thrown if over 5.</p>
     *
     * @param overlayTextures The list of overlaw textures.
     */
    public void setOverlayTextures(List<Texture> overlayTextures) {
        if (overlayTextures.size() >= 5)
            throw new IllegalArgumentException("A maximum of 5 overlay textures only.");
        this.overlayTextures = overlayTextures;
    }

    /**
     * If the material has a normal map
     * <p><b>Not implemented yet</b></p>
     *
     * @return If the material has a normal map
     */
    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

    /**
     * Get the texture of the normal map.
     * <p><b>Not implemented yet.</b></p>
     *
     * @return The texture of the normal map.
     */
    public Texture getNormalMap() {
        return normalMap;
    }

    /**
     * Set the normal map
     * <p><b>Not implemented yet.</b></p>
     *
     * @param normalMap The normal map texture
     */
    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }
}
