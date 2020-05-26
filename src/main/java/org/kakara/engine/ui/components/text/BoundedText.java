package org.kakara.engine.ui.components.text;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.constraints.Constraint;
import org.kakara.engine.ui.text.Font;
import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGTextRow;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Displays text to the UI in a bounded region.
 * <p><b>The scale is automatically set and should not be edited!</b></p>
 * @since 1.0-Pre1
 */
public class BoundedText extends GeneralComponent {
    private String text;
    private ByteBuffer paragraph;
    private Font font;
    private float size;
    private float letterSpacing;
    private float lineHeight;
    private int textAlign;
    private float blur;
    private Vector2 maximumBound;
    private RGBA color;
    private NVGColor nvgColor;
    private NVGTextRow.Buffer rows = NVGTextRow.create(3);

    /**
     * Create some text.
     * <p>If the text is not displaying, then ensure that the Font is added to the HUD. See {@link HUD#addFont(Font)}</p>
     * <p><b>The scale of this component should never be changed.</b></p>
     * @param text The text
     * @param font The font of the text.
     */
    public BoundedText(String text, Font font){
        this.paragraph = MemoryUtil.memUTF8(text, false);
        this.text = text;
        this.font = font;
        this.size = 30;
        this.letterSpacing = 1;
        this.lineHeight = 1;
        this.textAlign = NVG_ALIGN_LEFT;
        this.color = new RGBA(255, 255, 255, 1);
        this.scale = new Vector2(100, 0);
        this.maximumBound = new Vector2(100, 100);

        this.nvgColor = NVGColor.create();
    }

    @Override
    public void init(HUD hud, GameHandler handler) {
        pollInit(hud, handler);
    }

    @Override
    public void render(Vector2 relative, HUD hud, GameHandler handler) {
        if(!isVisible()) return;

        pollRender(relative, hud, handler);

        displayText(hud, handler);
    }

    @Override
    public void cleanup(GameHandler handler){
        super.cleanup(handler);

        MemoryUtil.memFree(paragraph);
        MemoryUtil.memFree(rows);
        nvgColor.free();
    }


    /**
     * Code to display the bounded text.
     * If you breath on this code it might have a seizure.
     * @param hud The hud
     * @param handler The handler.
     */
    private void displayText(HUD hud, GameHandler handler){
        FloatBuffer lineh = BufferUtils.createFloatBuffer(1);
        long start = MemoryUtil.memAddress(paragraph);
        long end = start + paragraph.remaining();
        int  nrows, lnum = 0;



        nvgTextMetrics(hud.getVG(), null, null, lineh);

        float y = getTruePosition().y;


        while ((nrows = nnvgTextBreakLines(hud.getVG(), start, end, calculateLineWidth(handler), MemoryUtil.memAddress(rows), 3)) != 0) {
            for (int i = 0; i < nrows; i++) {
                NVGTextRow row = rows.get(i);

                boolean hit = toRelativeY(y) > maximumBound.y;

                if(hit) break;

                nvgBeginPath(hud.getVG());
                nvgFontSize(hud.getVG(), calculateSize(handler));
                nvgFontFaceId(hud.getVG(), font.getFont());
                nvgTextAlign(hud.getVG(), textAlign);
                nvgFontBlur(hud.getVG(), blur);
                nvgTextLetterSpacing(hud.getVG(), letterSpacing);
                nvgTextLineHeight(hud.getVG(), lineHeight);

                nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
                nvgFillColor(hud.getVG(), nvgColor);

                nnvgText(hud.getVG(), getTruePosition().x, y, row.start(), row.end());

                lnum++;
                y += lineh.get(0);
            }
            start = rows.get(nrows - 1).next();
        }

        MemoryUtil.memFree(lineh);
    }

    private float toRelativeX(float x){
        return x - getTruePosition().x;
    }

    private float toRelativeY(float y){
        return y - getTruePosition().y;
    }

    /**
     * Calculate the font size if the window is resized.
     * @param handler Instance of gamehandler.
     * @return The scaled size
     */
    protected float calculateSize(GameHandler handler){
        return this.getSize() * ((float)handler.getWindow().getWidth()/(float)handler.getWindow().initalWidth);
    }


    /**
     * Calculate the line width if the window is resized.
     * @param handler Instance of gamehandler.
     * @return the scaled width
     */
    protected float calculateLineWidth(GameHandler handler){
        return this.getMaximumBound().x * ((float)handler.getWindow().getWidth()/(float)handler.getWindow().initalWidth);
    }

    /**
     * Set the font.
     * @param font The font that is to be used.
     */
    public void setFont(Font font){
        this.font = font;
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
        MemoryUtil.memFree(paragraph);
        this.text = text;
        this.paragraph = MemoryUtil.memUTF8(text);
    }

    /**
     * Get the text
     * @return The text.
     */
    public String getText(){
        return this.text;
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
     * @param blur The blur.
     */
    public void setBlur(float blur){
        this.blur = blur;
    }

    /**
     * Get the current blur.
     * @return The current blur.
     */
    public float getBlur(){
        return blur;
    }


    /**
     * This is the boundary of the text.
     * <p>This value is relative to the position.</p>
     * <code>
     *     text.setMaximumBound(new Vector2(50, 50));
     * </code>
     * @param maximumBound The boundary of the text.
     */
    public void setMaximumBound(Vector2 maximumBound){
        this.maximumBound = maximumBound;
        this.scale = maximumBound.clone();
        for(Constraint cc : constraints){
            cc.update(this);
        }
    }

    /**
     * Get the boundary of the text.
     * @return Get the boundary of the text.
     */
    public Vector2 getMaximumBound(){
        return maximumBound;
    }
}
