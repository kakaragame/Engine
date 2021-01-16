package org.kakara.engine.resources;

import org.kakara.engine.GameEngine;
import org.kakara.engine.utils.Utils;

import java.io.File;
import java.net.MalformedURLException;

/**
 * Manages the resources of the game.
 */
public class ResourceManager {
    private final String internalLocation;
    private final File externalLocation;

    public ResourceManager() {
        this("/resources/", new File(Utils.getCurrentDirectory(), "resources"));
    }

    /**
     * Creates the resource manager
     *
     * @param internalLocation The internal location of the jar. (Ex: resources)
     * @param externalLocation The external location. (Ex: Utils.getCurrentDirectory()).
     */
    public ResourceManager(String internalLocation, File externalLocation) {
        this.internalLocation = internalLocation;
        this.externalLocation = externalLocation;
    }

    /**
     * Get a resource from the specified file path.
     * <p>Use '/' as the path separator. The separator will automatically be changed to match other operating system formats.</p>
     *
     * @param resourcePath Path to the resource (Can be inside or outside of the jar).
     * @return path to the resource. (This will return null if the MalformedURLException occurs. Aka: if the path is not correct).
     */
    public Resource getResource(String resourcePath) {
        File externalResource = new File(externalLocation, resourcePath.replace("/", File.separator));
        GameEngine.LOGGER.debug(externalResource.getAbsolutePath());
        if (externalResource.exists()) {
            try {
                return new FileResource(externalResource.toURI().toURL(), resourcePath);
            } catch (MalformedURLException ex) {
                return null;
            }
        } else {
            String location = internalLocation + resourcePath;
            location = location.replace("//", "/");
            GameEngine.LOGGER.debug(location);
            return new JarResource(location, resourcePath);
        }
    }

    /**
     * @deprecated Not Implemented
     * @param url the Url.
     * @return the resource.
     */
    public Resource getExternalResource(String url) {
        return null;
    }
}
