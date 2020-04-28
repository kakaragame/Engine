package org.kakara.engine.renderobjects.renderlayouts;

import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.renderlayouts.types.Indices;
import org.kakara.engine.renderobjects.renderlayouts.types.Normal;
import org.kakara.engine.renderobjects.renderlayouts.types.Texture;
import org.kakara.engine.renderobjects.renderlayouts.types.Vertex;

/**
 * The way blocks are rendered in render blocks.
 * See the source code at {@link BlockLayout} for examples.
 * TODO Add a way to add a transparency option.
 */
public interface Layout {
    Vertex getVertex(Vector3 pos);
    Texture getTextureCords();
    Normal getNormal();
    Indices getIndices();
}
