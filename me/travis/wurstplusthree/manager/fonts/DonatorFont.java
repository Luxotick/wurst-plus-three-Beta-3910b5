/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.manager.fonts;

import java.awt.Font;
import me.travis.wurstplusthree.gui.font.CustomFont;
import me.travis.wurstplusthree.util.Globals;

public class DonatorFont
implements Globals {
    private final String fontName = "Tahoma";
    private final int smallSize = 15;
    private final int mediumSize = 19;
    private final int largeSize = 24;
    private final CustomFont smallFont = new CustomFont(new Font("Tahoma", 0, 15), true, false);
    private final CustomFont mediumFont = new CustomFont(new Font("Tahoma", 0, 19), true, false);
    private final CustomFont largeFont = new CustomFont(new Font("Tahoma", 0, 24), true, false);

    public void drawSmallStringRainbow(String string, float x, float y, int colour) {
        this.smallFont.drawStringWithShadow(string, x, y, colour);
    }

    public void drawMediumStringRainbow(String string, float x, float y, int colour) {
        this.mediumFont.drawStringWithShadow(string, x, y, colour);
    }

    public void drawLargeStringRainbow(String string, float x, float y, int colour) {
        this.largeFont.drawStringWithShadow(string, x, y, colour);
    }
}

