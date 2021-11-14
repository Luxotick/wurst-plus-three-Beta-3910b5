/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;

@Hack.Registration(name="Small Shield", description="trvsf moment", category=Hack.Category.RENDER, isListening=false)
public class SmallShield
extends Hack {
    @Override
    public void onUpdate() {
        SmallShield.mc.entityRenderer.itemRenderer.equippedProgressOffHand = -1.0f;
    }
}

