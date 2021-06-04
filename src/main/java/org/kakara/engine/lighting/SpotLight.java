package org.kakara.engine.lighting;

import org.joml.Vector3f;
import org.kakara.engine.math.Vector3;

/**
 * Spot / Beam based lighting.
 */
public class SpotLight extends PointLight {
    private Vector3f coneDirection;
    private float cutOff;

    /**
     * Construct a spot light.
     *
     * <p>The default settings are slightly different from a normal PointLight.</p>
     *
     * @param position      The position of the spot light.
     * @param coneDirection The direction of the cone
     * @param cutOffAngle   The angle of cutoff.
     */
    public SpotLight(Vector3 position, Vector3f coneDirection, float cutOffAngle) {
        super(LightColor.WHITE, position, 1);
        this.attenuation.setExponent(0.02f);
        this.attenuation.setConstant(0);
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    /**
     * Make a spot light off of another one.
     *
     * <p>All values are cloned.</p>
     *
     * @param spotLight Clone a spot light.
     */
    public SpotLight(SpotLight spotLight) {
        super(spotLight);
        this.coneDirection = new Vector3f(spotLight.coneDirection);
        setCutOff(spotLight.getCutOff());
    }

    /**
     * Get the cone direction
     *
     * @return the cone direction.
     */
    public Vector3f getConeDirection() {
        return coneDirection;
    }

    /**
     * Set the cone direction
     *
     * @param coneDirection Set the cone direction
     */
    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    /**
     * Get the cut off angle.
     *
     * @return The cutoff
     */
    public float getCutOff() {
        return cutOff;
    }

    /**
     * Set the cut off.
     *
     * <p>In most cases you will want to use {@link #setCutOffAngle(float)} instead.</p>
     *
     * @param cutOff the cute off.
     */
    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    /**
     * Set the cut off angle.
     *
     * @param cutOffAngle The angle
     */
    public final void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float) Math.cos(Math.toRadians(cutOffAngle)));
    }

}
