/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.ColourSetting;
import me.travis.wurstplusthree.util.elements.Colour;

@Hack.Registration(name="Pitbull Esp", description="makes everyones skin pitbull", category=Hack.Category.RENDER, isListening=false)
public class Pitbull
extends Hack {
    public static Pitbull INSTANCE;
    public ColourSetting texture = new ColourSetting("Texture", new Colour(255, 255, 255, 255), (Hack)this);

    public Pitbull() {
        INSTANCE = this;
    }
}

