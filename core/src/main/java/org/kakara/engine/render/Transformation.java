package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.ui.objectcanvas.UIObject;

/**
 * The transformation handler.
 * <p>
 * This class handles the transformation math for GameObjects so that they can be rendered.
 */
public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f modelLightViewMatrix;
    private final Matrix4f lightViewMatrix;
    private final Matrix4f orthoProjMatrix;

    public Transformation() {
        modelViewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        orthoProjMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    /**
     * Update the generic view matrix.
     *
     * @param position The position
     * @param rotation The rotation
     * @param matrix   The matrix in question.
     * @return The updated matrix.
     */
    public static Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    /**
     * Update the projection matrix.
     *
     * @param fov    The fov.
     * @param width  The width.
     * @param height The height.
     * @param zNear  The z near
     * @param zFar   The z far.
     * @return The updated projection matrix.
     */
    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        projectionMatrix.identity();
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    /**
     * Get the project matrix.
     *
     * @return The projection matrix.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Build the model matrix.
     *
     * @param gameItem The game item.
     * @return The model matrix.
     */
    public Matrix4f buildModelMatrix(GameItem gameItem) {
        Quaternionf rotation = gameItem.transform.getRotation();
        return modelMatrix.translationRotateScale(
                gameItem.transform.getPosition().x, gameItem.transform.getPosition().y, gameItem.transform.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.transform.getScale().x, gameItem.transform.getScale().y, gameItem.transform.getScale().z);
    }

    /**
     * Get the otho projection
     *
     * @return The otho projection
     */
    public final Matrix4f getOrthoProjectionMatrix() {
        return orthoProjMatrix;
    }

    /**
     * Update the ortho projection
     *
     * @param left   -
     * @param right  -
     * @param bottom -
     * @param top    -
     * @param zNear  -
     * @param zFar   -
     * @return The ortho projection.
     */
    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        orthoProjMatrix.identity();
        orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
        return orthoProjMatrix;
    }

    /**
     * Get the light view matrix.
     *
     * @return The light view matrix.
     */
    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    /**
     * Update the light view matrix
     *
     * @param position The position
     * @param rotation The rotation
     * @return The light view matrix.
     */
    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }


    /**
     * Build the model view matrix
     *
     * @param gameItem The game item
     * @param matrix   The view matrix.
     * @return The model view matrix.
     */
    public Matrix4f buildModelViewMatrix(GameItem gameItem, Matrix4f matrix) {
        Quaternionf rotation = gameItem.transform.getRotation();
        modelMatrix.translationRotateScale(gameItem.transform.getPosition().x, gameItem.transform.getPosition().y, gameItem.transform.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.transform.getScale().x,
                gameItem.transform.getScale().y, gameItem.transform.getScale().z);
        modelViewMatrix.set(matrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    /**
     * Construct the model matrix for the 3D UI elements.
     *
     * @param gameItem The UIObject to construct for.
     * @return The model matrix.
     */
    public Matrix4f buildModelViewMatrixUI(UIObject gameItem) {
        Quaternionf rotation = gameItem.getRotation();
        Matrix4f modelMatrixOne = new Matrix4f();
        modelMatrixOne.translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, 0, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.getScale(),
                gameItem.getScale(), gameItem.getScale());
        return modelMatrixOne;
    }

    /**
     * Construct the ortho projection for the 3D UI layer.
     *
     * @param left   The left value
     * @param right  The right value
     * @param bottom The bottom value
     * @param top    The top value.
     * @return The ortho projection matrix.
     */
    public Matrix4f buildOrtho(float left, float right, float bottom, float top) {
        orthoProjMatrix.identity();
        orthoProjMatrix.setOrtho(left, right, bottom, top, -100, 1000);
        return orthoProjMatrix;
    }

    /**
     * Build the model view matrix.
     *
     * @param modelMatrix The model matrix
     * @param viewMatrix  The view matrix.
     * @return The model view matrix.
     */
    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    /**
     * Build the model light view matrix
     *
     * @param modelMatrix     The model matrix
     * @param lightViewMatrix the light view matrix.
     * @return The model light view matrix.
     */
    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }
}
