package org.kakara.engine.ui.components.text;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.font.Font;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Displays text to the UI.
 * <p>The scale of the text is the line width in the x position.</p>
 * <p>As of 1.0-Pre1, all text properties are implemented.</p>
 */
public class Text extends GeneralComponent {
    private String text;
    private Font font;
    private float size;
    private float letterSpacing;
    private float lineHeight;
    private float lineWidth;
    private int textAlign;
    private float blur;
    private RGBA color;

    private NVGColor nvgColor;
    private UserInterface userInterface;

    /**
     * Create some text.
     * <p>If the text is not displaying, then ensure that the Font is added to the HUD. See {@link UserInterface#addFont(Font)}</p>
     * <p><b>The scale of this component should never be changed.</b></p>
     * @param text The text
     * @param font The font of the text.
     */
    public Text(String text, Font font){
        this.text = text;
        this.font = font;
        this.size = 30;
        this.lineWidth = 100;
        this.letterSpacing = 1;
        this.lineHeight = 1;
        this.textAlign = NVG_ALIGN_LEFT;
        this.color = new RGBA(255, 255, 255, 1);
        this.scale = new Vector2(100, 0);

        this.nvgColor = NVGColor.create();
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        this.userInterface = userInterface;
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if(!isVisible()) return;

        pollRender(relative, userInterface, handler);

        nvgBeginPath(userInterface.getVG());
        nvgFontSize(userInterface.getVG(), calculateSize(handler));
        nvgFontFaceId(userInterface.getVG(), font.getFont());
        nvgTextAlign(userInterface.getVG(), textAlign);
        nvgFontBlur(userInterface.getVG(), blur);
        nvgTextLetterSpacing(userInterface.getVG(), letterSpacing);
        nvgTextLineHeight(userInterface.getVG(), lineHeight);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);

        nvgTextBox(userInterface.getVG(), getTruePosition().x, getTruePosition().y, this.calculateLineWidth(handler),  text);
    }

    /**
     * Calculate the font size if the window is resized.
     * @param handler Instance of gamehandler.
     * @return The scaled size
     */
    protected float calculateSize(GameHandler handler){
        if(userInterface.isAutoScaled())
            return this.getSize() * ((float)handler.getWindow().getWidth()/(float)handler.getWindow().initalWidth);
        else
            return this.getSize();
    }


    /**
     * Calculate the line width if the window is resized.
     * @param handler Instance of gamehandler.
     * @return the scaled width
     */
    protected float calculateLineWidth(GameHandler handler){
        if(userInterface.isAutoScaled())
            return this.getLineWidth() * ((float)handler.getWindow().getWidth()/(float)handler.getWindow().initalWidth);
        else
            return this.getLineWidth();
    }

    /**
     * Set the font.
     * @param font The font that is to be used.
     */
    public void setFont(Font font){
        this.font = font;
    }

    /**
     * Get the font of the text.
     * @since 1.0-Pre3
     * @return The font.
     */
    public Font getFont(){
        return font;
    }

    /**
     * Set the size of the text.
     * @param size The size of the text. (Non-Negative)
     */
    public void setSize(float size){
        this.size = size;
    }

    /**
     * Get the size of the font
     * @return The size
     */
    public float getSize(){
        return this.size;
    }

    /**
     * Set the value of the text!
     * @param text The text.
     */
    public void setText(String text){
        this.text = text;
    }

    /**
     * Get the text
     * @return The text.
     */
    public String getText(){
        return this.text;
    }

    /**
     * Set how wide the text should be before it wraps.
     * <p>The scale of the text is automatically set with this method.</p>
     * @param width The width.
     */
    public void setLineWidth(float width){
        this.lineWidth = width;
        this.scale = new Vector2(width, 0);
    }

    /**
     * Get the line width of the text.
     * @return The line width
     */
    public float getLineWidth(){
        return this.lineWidth;
    }

    /**
     * Set the height of the space between lines.
     * @param height The height.
     */
    public void setLineHeight(float height){
        this.lineHeight = height;
    }

    /**
     * Get the line height
     * @return The list height
     */
    public float getLineHeight(){
        return this.lineHeight;
    }

    /**
     * Set the spacing between letters.
     * @param letterSpacing The letter spacing.
     */
    public void setLetterSpacing(float letterSpacing){
        this.letterSpacing = letterSpacing;
    }

    /**
     * Get the letter spacing
     * @return The letter spacing
     */
    public float getLetterSpacing(){
        return this.letterSpacing;
    }

    /**
     * Set the text align values. {@see ui.text.TextAlign}
     * @param textAlign The text align values.
     */
    public void setTextAlign(int textAlign){
        this.textAlign = textAlign;
    }

    /**
     * Set the color of the text.
     * @param color The color.
     */
    public void setColor(RGBA color){
        this.color = color;
    }

    /**
     * Get the color of the text
     * @return The color.
     */
    public RGBA getColor(){
        return this.color;
    }

    /**
     * Set the blur of the text.
     * <p>This can be used to create shadow effects.</p>
     * @since 1.0-Pre1
     * @param blur The blur.
     */
    public void setBlur(float blur){
        this.blur = blur;
    }

    /**
     * Get the current blur.
     * @since 1.0-Pre1
     * @return The current blur.
     */
    public float getBlur(){
        return blur;
    }

}
