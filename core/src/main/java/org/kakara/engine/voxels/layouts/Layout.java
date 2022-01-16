package org.kakara.engine.voxels.layouts;

import org.kakara.engine.math.Vector3;
import org.kakara.engine.voxels.layouts.types.Indices;
import org.kakara.engine.voxels.layouts.types.Normal;
import org.kakara.engine.voxels.layouts.types.Texture;
import org.kakara.engine.voxels.layouts.types.Vertex;

/**
 * The way blocks are rendered in render blocks.
 * See the source code at {@link BlockLayout} for examples.
 */
public interface Layout {
    Vertex getVertex(Vector3 pos);

    Texture getTextureCords();

    Normal getNormal();

    Indices getIndices();
}
