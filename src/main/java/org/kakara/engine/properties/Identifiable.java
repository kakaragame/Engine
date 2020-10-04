package org.kakara.engine.properties;

import java.util.UUID;

/**
 * Represents objects that can have an identity.
 *
 * @since 1.0-Pre3
 */
public interface Identifiable {
    /**
     * Get the UUID of an object.
     *
     * @return The UUID;
     */
    UUID getUUID();
}
