package org.kakara.engine.gameitems.particles;

import org.kakara.engine.gameitems.GameItem;

import java.util.List;

public interface ParticleEmitter {
    void cleanup();

    Particle getBaseParticle();

    List<GameItem> getParticles();
}
