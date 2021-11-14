/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.manager.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Locale;
import me.travis.wurstplusthree.gui.font.CustomFont;
import me.travis.wurstplusthree.util.Globals;
import me.travis.wurstplusthree.util.RenderUtil;
import me.travis.wurstplusthree.util.elements.Rainbow;

public class GuiFont
implements Globals {
    private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
    public String fontName = "Tahoma";
    public int fontSize = 16;
    private CustomFont font = new CustomFont(new Font(this.fontName, 0, this.fontSize), true, false);

    public void setFont() {
        this.font = new CustomFont(new Font(this.fontName, 0, this.fontSize), true, false);
    }

    public void reset() {
        this.setFont("Tahoma");
        this.setFontSize(16);
        this.setFont();
    }

    public boolean setFont(String fontName) {
        for (String font : this.fonts) {
            if (!fontName.equalsIgnoreCase(font)) continue;
            this.fontName = font;
            this.setFont();
            return true;
        }
        return false;
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.setFont();
    }

    public String setRandomFont() {
        this.fontName = RenderUtil.getRandomFont();
        this.setFont();
        return this.fontName;
    }

    public void drawStringWithShadow(String string, float x, float y, int colour) {
        this.drawString(string, x, y, colour, true);
    }

    public float drawString(String string, float x, float y, int colour, boolean shadow) {
        if (shadow) {
            return this.font.drawStringWithShadow(string, x, y, colour);
        }
        return this.font.drawString(string, x, y, colour);
    }

    public void drawStringRainbow(String string, float x, float y, boolean shadow) {
        if (shadow) {
            this.font.drawStringWithShadow(string, x, y, Rainbow.getColour().getRGB());
        } else {
            this.font.drawString(string, x, y, Rainbow.getColour().getRGB());
        }
    }

    public int getTextHeight() {
        return this.font.getStringHeight();
    }

    public int getTextWidth(String string) {
        return this.font.getStringWidth(string);
    }
}

