/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package me.travis.wurstplusthree.util.elements;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class NametagRenderer {
    private final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();

    public void glColor(int red, int green, int blue, int alpha) {
        GlStateManager.color((float)((float)red / 255.0f), (float)((float)green / 255.0f), (float)((float)blue / 255.0f), (float)((float)alpha / 255.0f));
    }

    public void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.color((float)red, (float)green, (float)blue, (float)alpha);
    }

    public void resetCaps() {
        this.glCapMap.forEach((arg_0, arg_1) -> this.setGlState(arg_0, arg_1));
    }

    public void enableGlCap(int cap) {
        this.setGlCap(cap, true);
    }

    public void enableGlCap(int ... caps) {
        for (int cap : caps) {
            this.setGlCap(cap, true);
        }
    }

    public void disableGlCap(int cap) {
        this.setGlCap(cap, false);
    }

    public void disableGlCap(int ... caps) {
        for (int cap : caps) {
            this.setGlCap(cap, false);
        }
    }

    public void setGlCap(int cap, boolean state) {
        this.glCapMap.put(cap, GL11.glGetBoolean((int)cap));
        this.setGlState(cap, state);
    }

    public void setGlState(int cap, boolean state) {
        if (state) {
            GL11.glEnable((int)cap);
        } else {
            GL11.glDisable((int)cap);
        }
    }
}

