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

    public static final int textureWidth = 300;
    public static final int textureHeight = 225;

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
        this.textures = textures;
        this.output = output;
        this.currentScene = currentScene;
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
        // This might now work V
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
                g.drawImage(scale(ImageIO.read(tempFiles.get(i)), textureWidth, textureHeight), x, y, null);
                this.textures.get(i).init(i, this.getXOffset(i), this.getYOffset(i));
                i++;
            }
        }

        ImageIO.write(combined, "PNG", new File(this.output, "textureAtlas.png"));
        g.dispose();
        ResourceManager rm = GameHandler.getInstance().getResourceManager();
        this.texture = new Texture(this.output + File.separator + "textureAtlas.png", currentScene);
    }

    /**
     * Scale the images to be the same size.
     * @param imageToScale The image to scale
     * @param dWidth The width
     * @param dHeight The height
     * @return The image
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
     * Get the actual game texture for the texture atlas.
     * <p>This is the texture to be used by OpenGL</p>
     * @return The texture.
     */
    public Texture getTexture(){
        return texture;
    }
}
