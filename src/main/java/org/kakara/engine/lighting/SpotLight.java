package org.kakara.engine.lighting;

import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.math.KMath;
import org.kakara.engine.math.Vector3;

/**
 * Spot / Beam based lighting.
 */
public class SpotLight implements Comparable<SpotLight> {
    private PointLight pointLight;

    private Vector3f coneDirection;

    private float cutOff;

    /**
     * Define a spot light.
     * @param pointLight The light the that this light will inherit its properties from.
     * @param coneDirection The direction of the cone
     * @param cutOffAngle The angle of cutoff.
     */
    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    /**
     * Make a spot light off of another one
     * @param spotLight Clone a spot light.
     */
    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.getPointLight()),
                new Vector3f(spotLight.getConeDirection()),
                0);
        setCutOff(spotLight.getCutOff());
    }

    /**
     * Get the point light
     * @return The point light
     */
    public PointLight getPointLight() {
        return pointLight;
    }

    /**
     * Set the point light
     * @param pointLight The point light
     */
    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    /**
     * Get the cone direction
     * @return the cone direction.
     */
    public Vector3f getConeDirection() {
        return coneDirection;
    }

    /**
     * Set the cone direction
     * @param coneDirection Set the cone direction
     */
    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    /**
     * Get the cut off angle.
     * @return The cutoff
     */
    public float getCutOff() {
        return cutOff;
    }

    /**
     * Set the cut off
     * @param cutOff the cute off.
     */
    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    /**
     * Set the cut off angle.
     * @param cutOffAngle The angle
     */
    public final void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float)Math.cos(Math.toRadians(cutOffAngle)));
    }

    @Override
    public int compareTo(SpotLight o) {
        Vector3 cameraPos = GameHandler.getInstance().getCamera().getPosition();
        return Math.round(KMath.distance(cameraPos, getPointLight().getPosition()) - KMath.distance(cameraPos, o.getPointLight().getPosition()));
    }

}
