package org.kakara.engine.gameitems.particles;

import org.joml.Quaternionf;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.math.Vector3;

/**
 * Handles the particles.
 */
public class Particle extends GameItem {

    private Vector3 speed;

    //Time to live in milliseconds
    private long ttl;

    private long updateTextureMillis;

    private long currentAnimTimeMillis;

    private final int animFrames;

    public Particle(Mesh mesh, Vector3 speed, long ttl, long updateTextureMillis) {
        super(mesh);
        this.speed = speed.clone();
        this.ttl = ttl;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimTimeMillis = 0;
        Texture texture = this.getMesh().getMaterial().get().getTexture();
        this.animFrames = texture.getNumCols() * texture.getNumRows();
    }

    public Particle(Particle baseParticle) {
        super(baseParticle.getMesh());
        Vector3 aux = baseParticle.transform.getPosition();
        transform.setPosition(aux.x, aux.y, aux.z);
        Quaternionf rotation = new Quaternionf(baseParticle.transform.getRotation());
        transform.setRotation(rotation);
        transform.setScale(baseParticle.transform.getScale());
        this.speed = baseParticle.speed.clone();
        this.ttl = baseParticle.getTtl();
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimTimeMillis = 0;
        this.animFrames = baseParticle.getAnimFrames();
    }

    public Vector3 getSpeed() {
        return speed;
    }

    public void setSpeed(Vector3 speed) {
        this.speed = speed;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public int getAnimFrames() {
        return animFrames;
    }

    public long getUpdateTextureMillis() {
        return updateTextureMillis;
    }

    public void setUpdateTextureMills(long updateTextureMillis) {
        this.updateTextureMillis = updateTextureMillis;
    }

    public long updateTtl(long elapsedTime) {
        this.ttl -= elapsedTime;
        this.currentAnimTimeMillis += elapsedTime;
        if (this.currentAnimTimeMillis >= this.getUpdateTextureMillis() && this.animFrames > 0) {
            this.currentAnimTimeMillis = 0;
            int pos = this.getTextPos();
            pos++;
            if (pos < this.animFrames) {
                this.setTextPos(pos);
            } else {
                this.setTextPos(0);
            }
        }
        return this.ttl;
    }
}
