/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityEnderCrystal
 */
package me.travis.wurstplusthree.hack.hacks.combat;

import me.travis.wurstplusthree.hack.hacks.combat.CrystalAura;
import net.minecraft.entity.item.EntityEnderCrystal;

final class Threads
extends Thread {
    EntityEnderCrystal bestCrystal;

    @Override
    public void run() {
        CrystalAura.INSTANCE.staticEnderCrystal = this.bestCrystal = CrystalAura.INSTANCE.getBestCrystal();
    }
}

