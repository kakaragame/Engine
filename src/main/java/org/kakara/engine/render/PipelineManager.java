package org.kakara.engine.render;

import org.kakara.engine.GameEngine;
import org.kakara.engine.render.preset.pipeline.ChunkPipeline;
import org.kakara.engine.render.preset.pipeline.ParticlesPipeline;
import org.kakara.engine.render.preset.pipeline.StandardPipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the render pipelines.
 * <p>Obtain the instance of this class at {@link GameEngine#getPipelineManager()}</p>
 *
 * @since 1.0-Pre4
 */
public final class PipelineManager {

    private final List<RenderPipeline> pipelineList;

    /**
     * Internal use only. Do not construct this class.
     */
    public PipelineManager() {
        this.pipelineList = new ArrayList<>();
        pipelineList.add(new StandardPipeline());
        pipelineList.add(new ChunkPipeline());
        pipelineList.add(new ParticlesPipeline());
    }

    /**
     * Add a pipeline.
     *
     * @param pipeline The pipeline to be added.
     */
    public void addPipeline(RenderPipeline pipeline) {
        this.pipelineList.add(pipeline);
    }

    /**
     * Get the list of render pipelines.
     * <p>Note: Do not modify this list! Unintended results could occur.</p>
     *
     * @return The list of render pipelines.
     */
    public List<RenderPipeline> getPipelines() {
        return pipelineList;
    }

    /**
     * Get the standard pipeline.
     *
     * @return The standard pipeline.
     */
    public StandardPipeline getStandardPipeline() {
        return (StandardPipeline) pipelineList.get(0);
    }

    /**
     * Get the chunk pipeline.
     *
     * @return The chunk pipeline.
     */
    public ChunkPipeline getChunkPipeline() {
        return (ChunkPipeline) pipelineList.get(1);
    }

    /**
     * Get the particle pipeline.
     *
     * @return The particle pipeline.
     */
    public ParticlesPipeline getParticlePipeline() {
        return (ParticlesPipeline) pipelineList.get(2);
    }
}
