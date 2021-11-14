/*
 * Decompiled with CFR 0.150.
 */
package me.travis.wurstplusthree.hack.hacks.render;

import me.travis.wurstplusthree.hack.Hack;
import me.travis.wurstplusthree.setting.type.IntSetting;

@Hack.Registration(name="Fov", description="tabbott mode", category=Hack.Category.RENDER, isListening=false)
public class Fov
extends Hack {
    IntSetting fov = new IntSetting("Fov", 130, 90, 179, this);
    float fovOld;

    @Override
    public void onEnable() {
        this.fovOld = Fov.mc.gameSettings.fovSetting;
    }

    @Override
    public void onUpdate() {
        Fov.mc.gameSettings.fovSetting = this.fov.getValue().intValue();
    }

    @Override
    public void onDisable() {
        Fov.mc.gameSettings.fovSetting = this.fovOld;
    }
}

