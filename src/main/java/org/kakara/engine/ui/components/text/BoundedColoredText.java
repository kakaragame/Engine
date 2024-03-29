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
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Displays colored text to the UI in a bounded region.
 * <p>This class does not contain as setColor method. Colors are set in the strings themselves by using color codes.
 * <code>
 * {#FFFFFF}
 * </code>
 * Color codes are just hex colors wrapped in curly braces. '{}'. 3 letter hex color codes are not supported. Example String:
 * <code>
 * "{#5BE0D5}I am a cool color! {#5BE06D} Me too! {#F54FFF}Another cool color! {#ED4725}I am red!"
 * </code>
 * </p>
 * <p><b>The scale is automatically set and should not be edited!</b></p>
 *
 * @since 1.0-Pre3
 */
public class BoundedColoredText extends GeneralUIComponent {
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
    private int lnum;

    private UserInterface userInterface;

    /**
     * Create some text.
     * <p>If the text is not displaying, then ensure that the Font is added to the HUD. See {@link UserInterface#addFont(Font)}</p>
     * <p><b>The scale of this component should never be changed.</b></p>
     *
     * @param text The text
     * @param font The font of the text.
     */
    public BoundedColoredText(String text, Font font) {
        this.paragraph = MemoryUtil.memUTF8(text, false);
        this.text = text;
        this.font = font;
        this.size = 30;
        this.letterSpacing = 1;
        this.lineHeight = 1;
        this.textAlign = NVG_ALIGN_LEFT;
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

    /**
     * Inform the system to count the number of lines in the text.
     * <p>This information can be retrieved from {@link #getLineNumbers()} after this method is called.</p>
     *
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    public void calculateLineNumbers(UserInterface userInterface, GameHandler handler) {
        nvgSave(userInterface.getVG());

        nvgTextMetrics(userInterface.getVG(), null, null, lineh);

        float y = getGlobalPosition().y;
        float x = getGlobalPosition().x;

        float curWith = 0;
        lnum = 0;
        for (Map.Entry<String, RGBA> entry : splitColors(text).entrySet()) {
            float[] prevBounds = new float[4];

            try (MemoryStack stack = MemoryStack.stackPush()) {
                ByteBuffer para = stack.UTF8(entry.getKey(), false);

                long start = MemoryUtil.memAddress(para);
                long end = start + para.remaining();
                int nrows;

                int z = 0;
                while ((nrows = nnvgTextBreakLines(userInterface.getVG(), start, end, calculateLineWidth(handler) - curWith, MemoryUtil.memAddress(rows), 3)) != 0) {
                    for (int i = 0; i < nrows; i++) {
                        NVGTextRow row = rows.get(i);

                        boolean hit = toRelativeY(y) > maximumBound.y;

                        if (hit) break;

                        nnvgTextBounds(userInterface.getVG(), x, y, row.start(), row.end(), prevBounds);

                        x += prevBounds[2] - prevBounds[0];
                        curWith += prevBounds[2] - prevBounds[0];
                        if (curWith >= calculateLineWidth(handler)) {
                            y += lineh.get(0);
                            x = getGlobalPosition().x;
                            curWith = 0;
                            lnum += 1;
                        }
                    }
                    start = rows.get(nrows - 1).next();
                }

            }
        }

        nvgRestore(userInterface.getVG());
        lnum++;
    }

    /**
     * Code to display the *colored* bounded text.
     * If you breath on this code it might have a seizure.
     *
     * @param userInterface The hud
     * @param handler       The handler.
     */
    private void displayText(UserInterface userInterface, GameHandler handler) {

        nvgSave(userInterface.getVG());

        nvgTextMetrics(userInterface.getVG(), null, null, lineh);

        float y = getGlobalPosition().y;
        float x = getGlobalPosition().x;

        float curWith = 0;
        lnum = 0;
        for (Map.Entry<String, RGBA> entry : splitColors(text).entrySet()) {
            float[] prevBounds = new float[4];

            try (MemoryStack stack = MemoryStack.stackPush()) {
                ByteBuffer para = stack.UTF8(entry.getKey(), false);

                long start = MemoryUtil.memAddress(para);
                long end = start + para.remaining();
                int nrows;

                int z = 0;
                while ((nrows = nnvgTextBreakLines(userInterface.getVG(), start, end, calculateLineWidth(handler) - curWith, MemoryUtil.memAddress(rows), 3)) != 0) {
                    for (int i = 0; i < nrows; i++) {
                        NVGTextRow row = rows.get(i);

                        boolean hit = toRelativeY(y) > maximumBound.y;

                        if (hit) break;

                        nvgBeginPath(userInterface.getVG());
                        nvgFontSize(userInterface.getVG(), calculateSize(handler));
                        nvgFontFaceId(userInterface.getVG(), font.getFontId());
                        nvgTextAlign(userInterface.getVG(), textAlign);
                        nvgFontBlur(userInterface.getVG(), blur);
                        nvgTextLetterSpacing(userInterface.getVG(), letterSpacing);
                        nvgTextLineHeight(userInterface.getVG(), lineHeight);

                        nvgRGBA((byte) entry.getValue().r, (byte) entry.getValue().g, (byte) entry.getValue().b, (byte) entry.getValue().aToNano(), nvgColor);
                        nvgFillColor(userInterface.getVG(), nvgColor);

                        nnvgTextBounds(userInterface.getVG(), x, y, row.start(), row.end(), prevBounds);
                        nnvgText(userInterface.getVG(), x, y, row.start(), row.end());
                        x += prevBounds[2] - prevBounds[0];
                        curWith += prevBounds[2] - prevBounds[0];
                        if (curWith >= calculateLineWidth(handler)) {
                            y += lineh.get(0);
                            x = getGlobalPosition().x;
                            curWith = 0;
                            lnum += 1;
                        }
                    }
                    start = rows.get(nrows - 1).next();
                }

            }
        }

        nvgRestore(userInterface.getVG());
        lnum++;
    }

    /**
     * Split up the colors in a message for processing.
     *
     * @param message The message.
     * @return The Map of strings and their colors.
     */
    protected Map<String, RGBA> splitColors(String message) {
        List<String> normalMessage = new ArrayList<>(Arrays.asList(message.split("\\{(#[^}]{6})}")));
        List<String> colorCodes = new ArrayList<>();
        colorCodes.add("{#FFFFFF}");
        Matcher m = Pattern.compile("\\{(#[^}]{6})}").matcher(message);
        while (m.find()) {
            colorCodes.add(m.group());
        }
        for (int i = 0; i < colorCodes.size(); i++) {
            if (!isChatCode(colorCodes.get(i)) && i != 0) {
                normalMessage.set(i - 1, normalMessage.get(i - 1) + colorCodes.get(i) + normalMessage.get(i));
                normalMessage.remove(i);
                colorCodes.remove(i);
                i--;
            }
        }
        Map<String, RGBA> output = new LinkedHashMap<>();
        for (int i = 0; i < normalMessage.size(); i++) {
            output.put(normalMessage.get(i), hex2Rgb(colorCodes.get(i)));
        }
        return output;
    }

    /**
     * Check to make sure a chat color code is valid.
     *
     * @param code The code.
     * @return If it is valid.
     */
    protected boolean isChatCode(String code) {
        try {
            hex2Rgb(code);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Convert the hex String to the {@link RGBA} class.
     *
     * @param colorStr The hex string.
     * @return The hex String in RGBA form.
     */
    protected RGBA hex2Rgb(String colorStr) {
        String hexCode = colorStr.replace("{", "").replace("}", "");
        return new RGBA(
                Integer.valueOf(hexCode.substring(1, 3), 16),
                Integer.valueOf(hexCode.substring(3, 5), 16),
                Integer.valueOf(hexCode.substring(5, 7), 16), 1);
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
     * Get the number of lines of text.
     * <p>Note: This number is not known until
     * the first render call.</p>
     *
     * @return The number of lines of text.
     */
    public int getLineNumbers() {
        return lnum;
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
