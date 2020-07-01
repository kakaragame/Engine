package org.kakara.engine.renderobjects;

import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.item.Texture;
import org.kakara.engine.resources.ResourceManager;
import org.kakara.engine.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The TextureAtlas is one big texture file that is used to display textures on render chunks.
 * <p>The textures on the texture atlas are put together at runtime.</p>
 */
public class TextureAtlas {
    private String output;
    private List<RenderTexture> textures;

    private int textureWidth;
    private int textureHeight;

    private Texture texture;

    private int numberOfRows;

    private Scene currentScene;

    /**
     * Create the texture atlas.
     * @param textures The list of render textures.
     * @param output The location of where the texture atlas is to be outputted.
     * @param currentScene The current scene
     */
    public TextureAtlas(List<RenderTexture> textures, String output, Scene currentScene){
        this(textures, output, currentScene, 400, 300);
    }

    /**
     * Create the texture atlas.
     * @param textures The list of render textures.
     * @param output The location of where the texture atlas is to be outputted.
     * @param currentScene The current scene
     * @param textureWidth The width of each texture
     * @param textureHeight The height of each texture.
     * @since 1.0-Pre1
     */
    public TextureAtlas(List<RenderTexture> textures, String output, Scene currentScene, int textureWidth, int textureHeight){
        this.textures = textures;
        this.output = output;
        this.currentScene = currentScene;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        try {
            calculateTextureAtlas(this.textures);
        }catch(IOException ex){
            GameEngine.LOGGER.error("Could not create texture atlas. Missing texture files?", ex);
        }
    }

    /**
     * Get the list of render textures.
     * @return The list of render textures.
     */
    public List<RenderTexture> getTextures(){
        return textures;
    }

    /**
     * Get the number of rows
     * @return The number of rows.
     */
    public int getNumberOfRows(){
        return numberOfRows;
    }

    /**
     * Get the x offset
     * @param id The id of the texture
     * @return The x offset
     */
    public float getXOffset(int id){
        return ((float) id % numberOfRows)/numberOfRows;
    }

    /**
     * Get the y offset
     * @param id The id of the texture
     * @return The y offset
     */
    public float getYOffset(int id){
        return (float) Math.floor((double) id / (double) numberOfRows)/numberOfRows;
    }

    /**
     * Combine all of the textures into a single image file.
     * @param textures The list of textures
     * @throws IOException If the file could not be read / created.
     */
    private void calculateTextureAtlas(List<RenderTexture> textures) throws IOException {
        if(texture != null)
            texture.cleanup();

        List<InputStream> tempFiles = textures.stream().map(text -> text.getResource().getInputStream()).collect(Collectors.toList());

        int numOfRows = (int) Math.ceil(Math.sqrt(tempFiles.size()));
        this.numberOfRows = numOfRows;

        int w = textureWidth * numOfRows;
        int h = textureHeight * numOfRows;

        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();

        int i = 0;
        for(int y = 0; y < h; y += textureHeight){
            for(int x = 0; x < w; x+=textureWidth){
                if(i >= tempFiles.size()) break;
                g.drawImage(resize(ImageIO.read(tempFiles.get(i)), textureWidth, textureHeight), x, y, null);
                this.textures.get(i).init(i, this.getXOffset(i), this.getYOffset(i));
                i++;
            }
        }

        ImageIO.write(combined, "PNG", new File(this.output, "textureAtlas.png"));
        g.dispose();
        this.texture = new Texture(this.output + File.separator + "textureAtlas.png", currentScene);
    }

    /**
     * Scale the images to be the same size.
     * @param imageToScale The image to scale
     * @param dWidth The width
     * @param dHeight The height
     * @return The image
     * @deprecated Use {@link #resize(BufferedImage, int, int)} instead. To be removed in a future update.
     */
    private BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }

    /**
     * Resize the image to the desired size
     * <p>This method replaces {@link #scale(BufferedImage, int, int)}, but is most likely slower.</p>
     * <p>This method does not have artifacts.</p>
     * @param imageToScale The image to scale
     * @param newW The new width
     * @param newH The new height
     * @return The scaled image.
     */
    private BufferedImage resize(BufferedImage imageToScale, int newW, int newH){
        Image tmp = imageToScale.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * Get the actual game texture for the texture atlas.
     * <p>This is the texture to be used by OpenGL</p>
     * @return The texture.
     */
    public Texture getTexture(){
        return texture;
    }

    /**
     * Recalculate the texture atlas to include new textures!
     * <p>The previous Texture is automatically cleared from memory.</p>
     * @since 1.0-Pre1
     */
    public void recalculateTextureAtlas(){
        try {
            calculateTextureAtlas(textures);
        }catch(IOException ex){
            GameEngine.LOGGER.error("Cannot recalculate the texture atlas!", ex);
        }
    }

    /**
     * Scrub the TextureAtlas from memory.
     * @since 1.0-Pre1
     */
    public void cleanup(){
        if(texture != null)
            texture.cleanup();
    }

    /**
     * Get the width of a texture.
     * @since 1.0-Pre1
     * @return The width.
     */
    public int getTextureWidth(){
        return textureWidth;
    }

    /**
     * Get the height of a texture.
     * @since 1.0-Pre1
     * @return The height.
     */
    public int getTextureHeight(){
        return textureHeight;
    }

    /**
     * Change the resolution of the textures.
     * <p>{@link #recalculateTextureAtlas()} is automatically called by this method.</p>
     * @param width The width
     * @param height The height.
     */
    public void setTextureSize(int width, int height){
        this.textureWidth = width;
        this.textureHeight = height;
        recalculateTextureAtlas();
    }
}
