/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.DoubleSetting;

@Hack.Registration(name="Item Physics", description="Apply physics to items", category=Hack.Category.RENDER, isListening=false)
public class ItemPhysics
extends Hack {
    public static ItemPhysics INSTANCE;
    public DoubleSetting Scaling = new DoubleSetting("Scaling", (Double)0.5, (Double)0.0, (Double)10.0, this);

    public ItemPhysics() {
        INSTANCE = this;
    }
}

