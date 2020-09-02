package org.kakara.engine.models;

import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.resources.Resource;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.Scene;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * Cache all of the texture objects so they can be referenced / deleted at the
 */
public class TextureCache {
    private static TextureCache instance;

    private Map<String, Texture> texturesMap;
    private ResourceManager resourceManager;

    private TextureCache(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        texturesMap = new HashMap<>();
    }

    /**
     * Get the instanced of the texture cache
     *
     * @param resourceManager The resource manager
     * @return The texture cache.
     */
    public static synchronized TextureCache getInstance(ResourceManager resourceManager) {
        if (instance == null) {
            instance = new TextureCache(resourceManager);
        }
        return instance;
    }

    /**
     * Get a texture.
     * <p>If the texture already exists, return that, if not create a new one.</p>
     *
     * @param path         The path
     * @param currentScene The current scene
     * @return The texture found or created.
     * @throws MalformedURLException If the resource could not be found.
     */
    public Texture getTexture(String path, Scene currentScene) throws MalformedURLException {
        Texture texture = texturesMap.get(path);
        if (texture == null) {
            Resource resource = resourceManager.getResource(path);
            if (resource == null) {
                throw new MissingResourceException("Unable to locate resource: " + path, path, path);
            }
            texture = new Texture(resource, currentScene);

            texturesMap.put(path, texture);
        }
        return texture;
    }

    /**
     * Remove unused textures from the scene
     *
     * @param scene The current scene.
     */
    public void cleanup(Scene scene) {
        Iterator<Map.Entry<String, Texture>> it = texturesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Texture> text = it.next();
            if (text.getValue().getCurrentScene() == scene) {
                text.getValue().cleanup();
                it.remove();
            }
        }
    }
}
