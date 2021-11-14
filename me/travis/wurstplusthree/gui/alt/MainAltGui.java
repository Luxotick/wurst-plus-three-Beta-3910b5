/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.travis.wurstplusthree.gui.alt;

import java.awt.Color;
import java.util.ArrayList;
import me.travis.wurstplusthree.WurstplusThree;
import me.travis.wurstplusthree.gui.alt.AltComponent;
import me.travis.wurstplusthree.manager.AltManager;
import me.travis.wurstplusthree.util.RenderUtil2D;
import me.travis.wurstplusthree.util.elements.Alt;
import net.minecraft.client.gui.GuiScreen;

public class MainAltGui
extends GuiScreen {
    AltManager altManager = WurstplusThree.ALT_MANAGER;
    ArrayList<Alt> alts = this.altManager.getAlts();
    ArrayList<AltComponent> altComponents;
    int offset;
    public static final int x = 40;
    public static final int y = 20;
    public static final int width = 100;
    public static final int height = 40;
    public static final int GUI_TRANSPARENCY = -1728053248;

    public void initGui() {
        this.alts.add(new Alt("Madmegsox", "123"));
        this.altComponents = new ArrayList();
        for (Alt alt : this.alts) {
            this.altComponents.add(new AltComponent(alt, this.offset, this));
            this.offset += 50;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil2D.drawRectMC(0, 0, this.mc.displayWidth, this.mc.displayWidth, new Color(28, 28, 28).hashCode());
        for (AltComponent alt : this.altComponents) {
            alt.render();
        }
    }
}

