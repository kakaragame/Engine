package org.kakara.engine.window;

/**
 * Window rendering options can be changed here.
 */
public class WindowOptions {
    protected boolean antialiasing;
    protected boolean cullFace;

    protected WindowOptions() {
        cullFace = true;
        antialiasing = false;
    }

    /**
     * Check to see if face culling is enabled.
     *
     * @return If face culling is enabled.
     */
    public boolean isFaceCulling() {
        return cullFace;
    }

    /**
     * Enable or disable face culling.
     *
     * @param faceCulling If face culling should be enabled. (Default true).
     */
    public void setFaceCulling(boolean faceCulling) {
        this.cullFace = faceCulling;
    }

    /**
     * If antialiasing is enabled for the hud.
     *
     * @return If antialiasing is enabled.
     */
    public boolean isAntialiasing() {
        return antialiasing;
    }

    /**
     * Enable or disable antialiasing.
     *
     * @param antialiasing If antialiasing should be enabled.
     */
    public void setAntialiasing(boolean antialiasing) {
        this.antialiasing = antialiasing;
    }
}
