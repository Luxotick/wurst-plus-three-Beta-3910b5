/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;

@Hack.Registration(name="Shulker Preview", description="lets u see shulker contents", category=Hack.Category.RENDER, isListening=false)
public class ShulkerPreview
extends Hack {
    public static ShulkerPreview INSTACE;

    public ShulkerPreview() {
        INSTACE = this;
    }
}

