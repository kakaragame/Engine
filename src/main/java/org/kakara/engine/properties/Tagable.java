package org.kakara.engine.properties;

import java.util.List;

/**
 * This item can have a tag and store data.
 * @since 1.0-Pre2
 */
public interface Tagable {
    /**
     * Set the list of data for the tagable object.
     * @param data The list of data.
     */
    void setData(List<Object> data);

    /**
     * Get the list of data for the tagable object.
     * @return The list of data.
     */
    List<Object> getData();

    /**
     * Set the tag for the object.
     * @param tag The tag
     */
    void setTag(String tag);

    /**
     * Get the tag for the object
     * @return The tag.
     */
    String getTag();
}
