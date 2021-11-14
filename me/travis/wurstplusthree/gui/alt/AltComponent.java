/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.gui.alt;

import java.awt.Color;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.alt.MainAltGui;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Alt;

public class AltComponent {
    Alt alt;
    int offset;
    MainAltGui parent;

    public AltComponent(Alt alt, int offset, MainAltGui parent) {
        this.alt = alt;
        this.offset = offset;
        this.parent = parent;
    }

    public void render() {
        RenderUtil2D.drawRectMC(40, 20 + this.offset, 140, 20 + this.offset + 40, -1728053248);
        WurstplusThree.GUI_FONT_MANAGER.drawStringWithShadow(this.alt.getUsername(), 60.0f, 20 + this.offset + 10, new Color(255, 255, 255).hashCode());
    }
}

