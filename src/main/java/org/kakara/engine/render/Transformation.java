package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.kakara.engine.item.GameItem;

/**
 * The transformation handler.
 */
public class Transformation {
    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f modelMatrix;
    private final Matrix4f modelLightMatrix;
    private final Matrix4f modelLightViewMatrix;
    private final Matrix4f lightViewMatrix;
    private final Matrix4f orthoProjMatrix;
    private final Matrix4f ortho2DMatrix;
    private final Matrix4f orthoModelMatrix;

    public Transformation() {
        modelViewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        modelLightMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        orthoProjMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    /**
     * Update the projection matrix
     * @return The project matrix.
     */
    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        projectionMatrix.identity();
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    /**
     * Get the project matrix.
     * @return
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Build the model matrix.
     * @param gameItem The game item.
     * @return The model matrix.
     */
    public Matrix4f buildModelMatrix(GameItem gameItem) {
        Quaternionf rotation = gameItem.getRotation();
        return modelMatrix.translationRotateScale(
                gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
    }

    /**
     * Get the matrix for the model position, rotation, and scale.
     *
     * @param gameItem The game item to get the matrix for
     * @return The matrix.
     */
    public Matrix4f getModelMatrix(GameItem gameItem) {
        Quaternionf rotation = gameItem.getRotation();
        modelViewMatrix.translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.getScale(),
                gameItem.getScale(), gameItem.getScale());
        return modelViewMatrix;
    }

    /**
     * Get the otho projection
     * @return The otho projection
     */
    public final Matrix4f getOrthoProjectionMatrix() {
        return orthoProjMatrix;
    }

    /**
     * Update the ortho projection
     * @param left -
     * @param right -
     * @param bottom -
     * @param top -
     * @param zNear -
     * @param zFar -
     * @return The ortho projection.
     */
    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        orthoProjMatrix.identity();
        orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
        return orthoProjMatrix;
    }

    /**
     * Get the light view matrix.
     * @return The light view matrix.
     */
    public Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    /**
     * Set the light view matrix.
     * @param lightViewMatrix The light view matrix.
     */
    public void setLightViewMatrix(Matrix4f lightViewMatrix) {
        this.lightViewMatrix.set(lightViewMatrix);
    }

    /**
     * Update the light view matrix
     * @param position The position
     * @param rotation The rotation
     * @return The light view matrix.
     */
    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation) {
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    /**
     * Update the generic view matrix.
     * @param position The position
     * @param rotation The rotation
     * @param matrix The matrix in question.
     * @return The updated matrix.
     */
    public static  Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    /**
     * Get the 2d projection matrix.
     * @param left -
     * @param right -
     * @param bottom -
     * @param top -
     * @return The orth 2d projection matrix.
     */
    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top) {
        ortho2DMatrix.identity();
        ortho2DMatrix.setOrtho2D(left, right, bottom, top);
        return ortho2DMatrix;
    }

    /**
     * Build the model view matrix
     * @param gameItem The game item
     * @param matrix The view matrix.
     * @return The model view matrix.
     */
    public Matrix4f buildModelViewMatrix(GameItem gameItem, Matrix4f matrix) {
        Quaternionf rotation = gameItem.getRotation();
        modelMatrix.translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.getScale(),
                gameItem.getScale(), gameItem.getScale());
        modelViewMatrix.set(matrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    /**
     * Build the model view matrix.
     * @param modelMatrix The model matrix
     * @param viewMatrix The view matrix.
     * @return The model view matrix.
     */
    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    /**
     * Build the light view matrix.
     * @param gameItem The game item
     * @param matrix The light view matrix.
     * @return The model light view matrix.
     */
    public Matrix4f buildModelLightViewMatrix(GameItem gameItem, Matrix4f matrix) {
        Quaternionf rotation = gameItem.getRotation();
        modelLightMatrix.translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.getScale(),
                gameItem.getScale(), gameItem.getScale());
        modelLightViewMatrix.set(matrix);
        return modelLightViewMatrix.mul(modelLightMatrix);
    }

    /**
     * Build the model light view matrix
     * @param modelMatrix The model matrix
     * @param lightViewMatrix the light view matrix.
     * @return The model light view matrix.
     */
    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    /**
     * Build the ortho projection model matrix
     * @param gameItem The game item
     * @param orthoMatrix The ortho matrix
     * @return The otho project model matrix.
     */
    public Matrix4f buildOrthoProjModelMatrix(GameItem gameItem, Matrix4f orthoMatrix) {
        Quaternionf rotation = gameItem.getRotation();
        modelMatrix.identity().translationRotateScale(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z, rotation.x, rotation.y, rotation.z, rotation.w, gameItem.getScale(),
                gameItem.getScale(), gameItem.getScale());
        orthoModelMatrix.set(orthoMatrix);
        orthoModelMatrix.mul(modelMatrix);
        return orthoModelMatrix;
    }
}
