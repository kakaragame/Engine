package org.kakara.engine.item.particles;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the particles.
 */
public class ParticleHandler {

    private List<ParticleEmitter> particleEmitters;
    public ParticleHandler(){
        particleEmitters = new ArrayList<>();
    }

    /**
     * Add a particle to the scene
     * @param emitter The particle to add
     */
    public void addParticleEmitter(ParticleEmitter emitter){
        particleEmitters.add(emitter);
    }

    /**
     * Remove a particle from the scene
     * @since 1.0-Pre1
     * @param emitter The particle to remove
     */
    public void removeParticleEmitter(ParticleEmitter emitter){
        particleEmitters.remove(emitter);
    }

    /**
     * Get the list of particle emitters in the scene.
     * @return The list of particles
     */
    public List<ParticleEmitter> getParticleEmitters(){
        return particleEmitters;
    }

    /**
     * Clear the list of particles.
     */
    public void clear(){
        particleEmitters.clear();
    }
}
