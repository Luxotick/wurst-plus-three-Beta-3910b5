/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.util.elements;

import java.awt.Color;
import me.travis.wurstplusthree.util.elements.Colour;

public class Rainbow {
    public static Color getColour() {
        return Colour.fromHSB((float)(System.currentTimeMillis() % 11520L) / 11520.0f, 1.0f, 1.0f);
    }

    public static Color getFurtherColour(int offset) {
        return Colour.fromHSB((float)((System.currentTimeMillis() + (long)offset) % 11520L) / 11520.0f, 1.0f, 1.0f);
    }
}

