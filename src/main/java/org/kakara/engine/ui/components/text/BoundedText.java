package org.kakara.engine.ui.components.text;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.ui.constraints.Constraint;
import org.kakara.engine.ui.font.Font;
import org.kakara.engine.utils.RGBA;
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
 *
 * @since 1.0-Pre1
 */
public class BoundedText extends GeneralUIComponent {
    private final NVGColor nvgColor;
    private final NVGTextRow.Buffer rows = NVGTextRow.create(3);
    private final FloatBuffer lineh = BufferUtils.createFloatBuffer(1);
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
    private UserInterface userInterface;

    /**
     * Create some text.
     * <p>If the text is not displaying, then ensure that the Font is added to the HUD. See {@link UserInterface#addFont(Font)}</p>
     * <p><b>The scale of this component should never be changed.</b></p>
     *
     * @param text The text
     * @param font The font of the text.
     */
    public BoundedText(String text, Font font) {
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
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        this.userInterface = userInterface;
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;

        super.render(relative, userInterface, handler);

        displayText(userInterface, handler);
    }

    @Override
    public void cleanup(GameHandler handler) {
        super.cleanup(handler);

        MemoryUtil.memFree(paragraph);
    }

//    private String getStringFromMem(long start, long end){
//        byte[] bytes = new byte[(int)(end-start)];
//        for(long i = start; i < end; i++){
//            bytes[(int)(i-start)] = MemoryUtil.memGetByte(i);
//        }
//        return new String(bytes);
//    }


    /**
     * Code to display the bounded text.
     * If you breath on this code it might have a seizure.
     *
     * @param userInterface The hud
     * @param handler       The handler.
     */
    private void displayText(UserInterface userInterface, GameHandler handler) {

        nvgSave(userInterface.getVG());

        long start = MemoryUtil.memAddress(paragraph);
        long end = start + paragraph.remaining();
        int nrows, lnum = 0;


        nvgTextMetrics(userInterface.getVG(), null, null, lineh);

        float y = getGlobalPosition().y;

//        NVGTextRow.Buffer buf = NVGTextRow.create(3);
//        nvgTextBreakLines(userInterface.getVG(), "This is a test", calculateLineWidth(handler), buf);
//        System.out.println(getStringFromMem(buf.get(0).start(), buf.get(0).end()));


        while ((nrows = nnvgTextBreakLines(userInterface.getVG(), start, end, calculateLineWidth(handler), MemoryUtil.memAddress(rows), 3)) != 0) {
            for (int i = 0; i < nrows; i++) {
                NVGTextRow row = rows.get(i);

                boolean hit = toRelativeY(y) > maximumBound.y;

                if (hit) break;

                nvgBeginPath(userInterface.getVG());
                nvgFontSize(userInterface.getVG(), calculateSize(handler));
                nvgFontFaceId(userInterface.getVG(), font.getFont());
                nvgTextAlign(userInterface.getVG(), textAlign);
                nvgFontBlur(userInterface.getVG(), blur);
                nvgTextLetterSpacing(userInterface.getVG(), letterSpacing);
                nvgTextLineHeight(userInterface.getVG(), lineHeight);

                nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
                nvgFillColor(userInterface.getVG(), nvgColor);

                nnvgText(userInterface.getVG(), getGlobalPosition().x, y, row.start(), row.end());

                lnum++;
                y += lineh.get(0);
            }
            start = rows.get(nrows - 1).next();
        }

        nvgRestore(userInterface.getVG());
    }

    private float toRelativeX(float x) {
        return x - getGlobalPosition().x;
    }

    private float toRelativeY(float y) {
        return y - getGlobalPosition().y;
    }

    /**
     * Calculate the font size if the window is resized.
     *
     * @param handler Instance of gamehandler.
     * @return The scaled size
     */
    protected float calculateSize(GameHandler handler) {
        if (getParentCanvas().isAutoScale())
            return this.getSize() * ((float) handler.getWindow().getWidth() / (float) handler.getWindow().initialWidth);
        else
            return this.getSize();
    }


    /**
     * Calculate the line width if the window is resized.
     *
     * @param handler Instance of gamehandler.
     * @return the scaled width
     */
    protected float calculateLineWidth(GameHandler handler) {
        if (getParentCanvas().isAutoScale())
            return this.getMaximumBound().x * ((float) handler.getWindow().getWidth() / (float) handler.getWindow().initialWidth);
        else
            return this.getMaximumBound().x;
    }

    /**
     * Get the font of the text.
     *
     * @return The font.
     * @since 1.0-Pre3
     */
    public Font getFont() {
        return font;
    }

    /**
     * Set the font.
     *
     * @param font The font that is to be used.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Get the size of the font
     *
     * @return The size
     */
    public float getSize() {
        return this.size;
    }

    /**
     * Set the size of the text.
     *
     * @param size The size of the text. (Non-Negative)
     */
    public void setSize(float size) {
        this.size = size;
    }

    /**
     * Get the text
     *
     * @return The text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the value of the text!
     *
     * @param text The text.
     */
    public void setText(String text) {
        MemoryUtil.memFree(paragraph);
        this.text = text;
        this.paragraph = MemoryUtil.memUTF8(text);
    }

    /**
     * Get the line height
     *
     * @return The list height
     */
    public float getLineHeight() {
        return this.lineHeight;
    }

    /**
     * Set the height of the space between lines.
     *
     * @param height The height.
     */
    public void setLineHeight(float height) {
        this.lineHeight = height;
    }

    /**
     * Get the letter spacing
     *
     * @return The letter spacing
     */
    public float getLetterSpacing() {
        return this.letterSpacing;
    }

    /**
     * Set the spacing between letters.
     *
     * @param letterSpacing The letter spacing.
     */
    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    /**
     * Set the text align values. {@link org.kakara.engine.ui.font.TextAlign}
     *
     * @param textAlign The text align values.
     */
    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Get the color of the text
     *
     * @return The color.
     */
    public RGBA getColor() {
        return this.color;
    }

    /**
     * Set the color of the text.
     *
     * @param color The color.
     */
    public void setColor(RGBA color) {
        this.color = color;
    }

    /**
     * Get the current blur.
     *
     * @return The current blur.
     */
    public float getBlur() {
        return blur;
    }

    /**
     * Set the blur of the text.
     * <p>This can be used to create shadow effects.</p>
     *
     * @param blur The blur.
     */
    public void setBlur(float blur) {
        this.blur = blur;
    }

    /**
     * Get the boundary of the text.
     *
     * @return Get the boundary of the text.
     */
    public Vector2 getMaximumBound() {
        return maximumBound;
    }

    /**
     * This is the boundary of the text.
     * <p>This value is relative to the position.</p>
     * <code>
     * text.setMaximumBound(new Vector2(50, 50));
     * </code>
     *
     * @param maximumBound The boundary of the text.
     */
    public void setMaximumBound(Vector2 maximumBound) {
        this.maximumBound = maximumBound;
        this.scale = maximumBound.clone();
        for (Constraint cc : constraints) {
            cc.update(this);
        }
    }
}
